package com.tomgs.ratis.kv.core;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.tomgs.common.kv.CacheClient;
import com.tomgs.common.kv.CacheSourceConfig;
import com.tomgs.ratis.kv.exception.RatisKVClientException;
import com.tomgs.ratis.kv.protocol.*;
import com.tomgs.ratis.kv.watch.DataChangeEvent;
import com.tomgs.ratis.kv.watch.DataChangeListener;
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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.tomgs.ratis.kv.core.GroupManager.RATIS_KV_GROUP_ID;

/**
 * RatisVKClient
 *
 * @author tomgs
 * @since 2022/3/22
 */
@Slf4j
public class RatisVKClient<K, V> implements CacheClient<K, V> {

    private final ProtostuffSerializer serializer;

    private final RaftClient raftClient;

    private final RaftClient watchRaftClient;

    private final CacheSourceConfig cacheSourceConfig;

    private final ScheduledExecutorService scheduledExecutorService;

    private final AtomicBoolean enableWatchThread = new AtomicBoolean(false);

    private static final Map<String, DataChangeListener> listenerMap = new ConcurrentHashMap<>();

    public RatisVKClient(final CacheSourceConfig cacheSourceConfig) {
        this.cacheSourceConfig = cacheSourceConfig;
        this.serializer = ProtostuffSerializer.INSTANCE;
        // create peers
        final String[] addresses = Optional.ofNullable(cacheSourceConfig.getServerAddresses())
                .map(s -> s.split(","))
                .orElse(null);
        if (addresses == null || addresses.length == 0) {
            throw new IllegalArgumentException("Failed to get " + cacheSourceConfig.getServerAddresses() + " from " + cacheSourceConfig);
        }

        final RaftGroup raftGroup = GroupManager.getInstance().getRaftGroup(RATIS_KV_GROUP_ID, addresses);

        ExponentialBackoffRetry retryPolicy = ExponentialBackoffRetry.newBuilder()
                .setBaseSleepTime(TimeDuration.valueOf(1000, TimeUnit.MILLISECONDS))
                .setMaxAttempts(10)
                .setMaxSleepTime(
                        TimeDuration.valueOf(100_000, TimeUnit.MILLISECONDS))
                .build();
        this.raftClient = buildClient(raftGroup, retryPolicy);
        this.watchRaftClient = buildClient(raftGroup, RetryPolicies.retryForeverNoSleep());
        this.scheduledExecutorService =
                Executors.newScheduledThreadPool(1, ThreadFactoryBuilder.create()
                        .setNamePrefix("watch-loop")
                        .setDaemon(true)
                        .build());
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

    private RatisKVResponse handleRatisKVWatchRequest(RatisKVRequest request) throws IOException {
        final byte[] bytes = serializer.serialize(request);
        final RaftClientReply raftClientReply = watchRaftClient.io().sendReadOnly(Message.valueOf(ByteString.copyFrom(bytes)));
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
        final byte[] keySer = serializer.serialize(key);
        try {
            WatchRequest watchRequest = new WatchRequest();
            watchRequest.setKey(keySer);
            RatisKVRequest request = RatisKVRequest.builder()
                    .cmdType(CmdType.WATCH)
                    .requestId(123L)
                    .traceId(123L)
                    .watchRequest(watchRequest)
                    .build();
            RatisKVResponse response = handleRatisKVWriteRequest(request);
            if (!response.getSuccess()) {
                throw new RatisKVClientException(response.getMessage());
            }
            // add to listener map
            listenerMap.put((String) key, dataChangeListener);
            // start watch
            if (enableWatchThread.compareAndSet(false, true)) {
                scheduledExecutorService.scheduleWithFixedDelay(() -> {
                    try {
                        this.watchLoop();
                    } catch (Exception e) {
                        log.warn("watch loop exception: {}", e.getMessage(), e);
                    }
                }, 1000, 1000, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            throw new RatisKVClientException(e.getMessage(), e);
        }
    }

    @Override
    public void unwatch(K key) {
        final byte[] keySer = serializer.serialize(key);
        try {
            UnwatchRequest unwatchRequest = new UnwatchRequest();
            unwatchRequest.setKey(keySer);
            RatisKVRequest request = RatisKVRequest.builder()
                    .cmdType(CmdType.UNWATCH)
                    .requestId(123L)
                    .traceId(123L)
                    .unwatchRequest(unwatchRequest)
                    .build();
            RatisKVResponse response = handleRatisKVWriteRequest(request);
            if (!response.getSuccess()) {
                throw new RatisKVClientException(response.getMessage());
            }
            // add to listener map
            listenerMap.remove((String) key);
        } catch (Exception e) {
            throw new RatisKVClientException(e.getMessage(), e);
        }
    }

    private void watchLoop() throws IOException {
        BPopRequest bPopRequest = new BPopRequest();
        RatisKVRequest request = RatisKVRequest.builder()
                .cmdType(CmdType.BPOP)
                .requestId(123L)
                .traceId(123L)
                .bPopRequest(bPopRequest)
                .build();
        RatisKVResponse response = handleRatisKVWatchRequest(request);
        if (!response.getSuccess()) {
            return;
        }
        final BPopResponse bPopResponse = response.getBPopResponse();
        final DataChangeEvent event = bPopResponse.getEvent();
        if (event == null) {
            return;
        }
        final DataChangeListener dataChangeListener = listenerMap.get(event.getPath());
        dataChangeListener.dataChanged(event);
    }

}
