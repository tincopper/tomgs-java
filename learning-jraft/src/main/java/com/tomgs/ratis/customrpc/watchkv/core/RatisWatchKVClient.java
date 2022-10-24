package com.tomgs.ratis.customrpc.watchkv.core;

import com.tomgs.common.ProtostuffSerializer;
import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.learning.grpc.proto.WatchCancelRequest;
import com.tomgs.learning.grpc.proto.WatchCreateRequest;
import com.tomgs.learning.grpc.proto.WatchRequest;
import com.tomgs.ratis.kv.core.GroupManager;
import com.tomgs.ratis.kv.exception.RatisKVClientException;
import com.tomgs.ratis.kv.protocol.*;
import com.tomgs.ratis.kv.watch.DataChangeEvent;
import com.tomgs.ratis.kv.watch.DataChangeListener;
import com.tomgs.ratisrpc.grpc.WatchGrpcFactory;
import com.tomgs.ratisrpc.grpc.core.WatchRaftClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.client.RaftClientConfigKeys;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.ClientId;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftClientReply;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.retry.ExponentialBackoffRetry;
import org.apache.ratis.retry.RetryPolicies;
import org.apache.ratis.retry.RetryPolicy;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.apache.ratis.util.TimeDuration;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.tomgs.ratis.kv.core.GroupManager.RATIS_KV_GROUP_ID;

/**
 * RatisVKClient
 *
 * @author tomgs
 * @since 2022/3/22
 */
@Slf4j
public class RatisWatchKVClient<K, V> implements CacheClient<K, V> {

    private final ProtostuffSerializer serializer;

    private final RaftClient raftClient;

    private final WatchRaftClient watchRaftClient;

    private final CacheSourceConfig cacheSourceConfig;

    public RatisWatchKVClient(final CacheSourceConfig cacheSourceConfig) {
        this.cacheSourceConfig = cacheSourceConfig;
        this.serializer = ProtostuffSerializer.INSTANCE;
        // create peers
        final String[] addresses = Optional.ofNullable(cacheSourceConfig.getServerAddresses())
                .map(s -> s.split(","))
                .orElse(null);
        if (addresses == null || addresses.length == 0) {
            throw new IllegalArgumentException("Failed to get " + cacheSourceConfig.getServerAddresses() + " from " + cacheSourceConfig);
        }

        RaftGroup raftGroup = GroupManager.getInstance().getRaftGroup(RATIS_KV_GROUP_ID, addresses);
        ExponentialBackoffRetry retryPolicy = ExponentialBackoffRetry.newBuilder()
                .setBaseSleepTime(TimeDuration.valueOf(1000, TimeUnit.MILLISECONDS))
                .setMaxAttempts(10)
                .setMaxSleepTime(
                        TimeDuration.valueOf(100_000, TimeUnit.MILLISECONDS))
                .build();
        this.raftClient = buildClient(raftGroup, retryPolicy);
        this.watchRaftClient = buildWatchClient(raftGroup, RetryPolicies.retryForeverNoSleep());
    }

    private WatchRaftClient buildWatchClient(RaftGroup raftGroup, RetryPolicy retryPolicy) {
        RaftProperties raftProperties = new RaftProperties();
        RaftClientConfigKeys.Rpc.setRequestTimeout(raftProperties,
                TimeDuration.valueOf(90, TimeUnit.SECONDS));
        WatchRaftClient.Builder builder = WatchRaftClient.newBuilder()
                .setProperties(raftProperties)
                .setRaftGroup(raftGroup)
                .setRetryPolicy(retryPolicy)
                .setClientRpc(
                        new WatchGrpcFactory(new Parameters())
                                .newRaftClientRpc(ClientId.randomId(), raftProperties))
                .setWatchClientRpc(
                        new WatchGrpcFactory(new Parameters())
                                .newWatchClientRpc(ClientId.randomId(), raftProperties));
        return builder.build();
    }

    private RaftClient buildClient(RaftGroup raftGroup, RetryPolicy retryPolicy) {
        RaftProperties raftProperties = new RaftProperties();
        RaftClientConfigKeys.Rpc.setRequestTimeout(raftProperties,
                TimeDuration.valueOf(90, TimeUnit.SECONDS));
        RaftClient.Builder builder = RaftClient.newBuilder()
                .setProperties(raftProperties)
                .setRaftGroup(raftGroup)
                .setRetryPolicy(retryPolicy)
                .setClientRpc(
                        new GrpcFactory(new Parameters())
                                .newRaftClientRpc(ClientId.randomId(), raftProperties));
        return builder.build();
    }

    private RatisKVResponse handleRatisKVReadRequest(RatisKVRequest request) throws IOException {
        final byte[] bytes = serializer.serialize(request);
        final RaftClientReply raftClientReply = raftClient.io().sendReadOnly(Message.valueOf(ByteString.copyFrom(bytes)));
        log.info("read log index : {}", raftClientReply.getLogIndex());
        return serializer.deserialize(raftClientReply.getMessage().getContent().toByteArray(), RatisKVResponse.class.getName());
    }

    private RatisKVResponse handleRatisKVWriteRequest(RatisKVRequest request) throws IOException {
        final byte[] bytes = serializer.serialize(request);
        final RaftClientReply raftClientReply = raftClient.io().send(Message.valueOf(ByteString.copyFrom(bytes)));
        log.info("write log index : {}", raftClientReply.getLogIndex());
        return serializer.deserialize(raftClientReply.getMessage().getContent().toByteArray(), RatisKVResponse.class.getName());
    }

    @Override
    public V get(K key) {
        try {
            GetRequest getRequest = new GetRequest();
            getRequest.setKey(serializer.serialize(key));
            RatisKVRequest request = RatisKVRequest.builder()
                    .cmdType(CmdType.GET)
                    .requestId(123L)
                    .traceId(123L)
                    .getRequest(getRequest)
                    .build();
            RatisKVResponse response = handleRatisKVReadRequest(request);
            if (response.getSuccess()) {
                final byte[] value = response.getGetResponse().getValue();
                if (value == null) {
                    return null;
                }
                return serializer.deserialize(value, cacheSourceConfig.getValueSederClass().getName());
            } else {
                throw new RatisKVClientException(response.getMessage());
            }
        } catch (Exception e) {
            throw new RatisKVClientException(e.getMessage(), e);
        }
    }

    @Override
    public void put(K key, V value) {
        try {
            PutRequest putRequest = new PutRequest();
            putRequest.setKey(serializer.serialize(key));
            putRequest.setValue(serializer.serialize(value));
            RatisKVRequest request = RatisKVRequest.builder()
                    .cmdType(CmdType.PUT)
                    .requestId(123L)
                    .traceId(123L)
                    .putRequest(putRequest)
                    .build();
            RatisKVResponse response = handleRatisKVWriteRequest(request);
            if (!response.getSuccess()) {
                throw new RatisKVClientException(response.getMessage());
            }
        } catch (Exception e) {
            throw new RatisKVClientException(e.getMessage(), e);
        }
    }

    @Override
    public void put(K key, V value, int expire) {

    }

    @Override
    public void delete(K key) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest();
            deleteRequest.setKey(serializer.serialize(key));
            RatisKVRequest request = RatisKVRequest.builder()
                    .cmdType(CmdType.DELETE)
                    .requestId(123L)
                    .traceId(123L)
                    .deleteRequest(deleteRequest)
                    .build();
            RatisKVResponse response = handleRatisKVWriteRequest(request);
            if (!response.getSuccess()) {
                throw new RatisKVClientException(response.getMessage());
            }
        } catch (Exception e) {
            throw new RatisKVClientException(e.getMessage(), e);
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void close() {

    }

    @Override
    public void watch(K key, DataChangeListener dataChangeListener) {
        final byte[] watchKey = serializer.serialize(key);
        WatchCreateRequest createRequest = WatchCreateRequest.newBuilder()
                .setWatchId(123L)
                .setKey(ByteString.copyFrom(watchKey))
                .build();
        WatchRequest watchRequest = WatchRequest.newBuilder()
                .setNodeIdBytes(ByteString.copyFromUtf8("127.0.0.1"))
                .setCreateRequest(createRequest)
                .build();

        watchRaftClient.watchClientRpc().watch(watchRequest, changeEvent -> {
            try {
                final String dataKey = serializer.deserialize(changeEvent.getKey().toByteArray(), String.class.getName());
                final com.tomgs.learning.grpc.proto.DataChangeEvent.Type type = changeEvent.getType();
                final byte[] dataBytes = changeEvent.getData().toByteArray();
                final String data = serializer.deserialize(dataBytes, String.class.getName());

                log.debug("watch reply: key: {}, type: {}, data: {}", dataKey, type, data);

                DataChangeEvent dataChangeEvent = new DataChangeEvent(type.name(), dataKey, dataBytes);
                dataChangeListener.dataChanged(dataChangeEvent);
            } catch (Exception e) {
                log.error("[{}]Error to process server push response: {}", createRequest.getWatchId(), changeEvent.toString());
            }
        });

        //watchRequestStreamObserver.onNext(watchRequest);
        //listenerMap.put((String) key, dataChangeListener);
    }

    @Override
    public void unwatch(K key) {
        final byte[] watchKey = serializer.serialize(key);
        WatchCancelRequest createRequest = WatchCancelRequest.newBuilder()
                .setWatchId(123L)
                .setKey(ByteString.copyFrom(watchKey))
                .build();
        WatchRequest watchRequest = WatchRequest.newBuilder()
                .setNodeIdBytes(ByteString.copyFromUtf8("127.0.0.1"))
                .setCancelRequest(createRequest)
                .build();

        watchRaftClient.watchClientRpc().unwatch(watchRequest);
        //watchRequestStreamObserver.onNext(watchRequest);
        //listenerMap.remove((String) key);
    }

}
