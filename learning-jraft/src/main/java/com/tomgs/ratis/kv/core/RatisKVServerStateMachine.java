package com.tomgs.ratis.kv.core;

import com.alipay.remoting.exception.CodecException;
import com.alipay.sofa.jraft.util.BytesUtil;
import com.tomgs.ratis.kv.protocol.*;
import com.tomgs.ratis.kv.storage.DBStore;
import com.tomgs.ratis.kv.storage.StorageEngine;
import com.tomgs.ratis.kv.watch.DataChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.storage.RaftStorage;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.apache.ratis.statemachine.impl.SimpleStateMachineStorage;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * RatisServerStateMachine
 *
 * @author tomgs
 * @since 2022/3/22
 */
@Slf4j
public class RatisKVServerStateMachine extends BaseStateMachine {

    private final SimpleStateMachineStorage storage =
            new SimpleStateMachineStorage();

    private final ProtostuffSerializer serializer;

    private final StorageEngine storageEngine;

    private final DBStore dbStore;

    private RaftGroupId raftGroupId;

    private final static BlockingQueue<DataChangeEvent> eventQueue = new ArrayBlockingQueue<>(8);

    private final static Map<byte[], byte[]> watchMap = new TreeMap<>(BytesUtil.getDefaultByteArrayComparator());

    private final static byte[] EMPTY_BYTE = new byte[0];

    public RatisKVServerStateMachine(final StorageEngine storageEngine) {
        this.storageEngine = storageEngine;
        this.serializer = new ProtostuffSerializer();
        this.dbStore = this.storageEngine.getRawDBStore();
    }

    @Override
    public void initialize(RaftServer raftServer, RaftGroupId id, RaftStorage raftStorage) throws IOException {
        getLifeCycle().startAndTransition(() -> {
            super.initialize(raftServer, raftGroupId, raftStorage);
            this.raftGroupId = id;
            storage.init(raftStorage);
        });
    }

    @Override
    public void reinitialize() throws IOException {
        super.reinitialize();
        /*getLifeCycle().startAndTransition(() -> {
            loadSnapshotInfoFromDB();
            this.ozoneManagerDoubleBuffer = buildDoubleBufferForRatis();
            handler.updateDoubleBuffer(ozoneManagerDoubleBuffer);
        });*/
    }

    @Override
    public long takeSnapshot() throws IOException {
        return super.takeSnapshot();
    }

    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
        final ByteString logData = trx.getStateMachineLogEntry().getLogData();
        try {
            return CompletableFuture.completedFuture(runCommand(Message.valueOf(logData)));
        } catch (Exception e) {
            return completeExceptionally(e);
        }
    }

    @Override
    public CompletableFuture<Message> query(Message request) {
        try {
            // do something
            return CompletableFuture.completedFuture(runCommand(request));
        } catch (Exception e) {
            return completeExceptionally(e);
        }
    }

    private Message runCommand(Message request) throws CodecException, InterruptedException {
        /*
         *   OMResponse response = handler.handleReadRequest(request);
         *   return OMRatisHelper.convertResponseToMessage(response);
         *   //
         *   public static Message convertResponseToMessage(OMResponse response) {
         *       byte[] requestBytes = response.toByteArray();
         *       return Message.valueOf(ByteString.copyFrom(requestBytes));
         *   }
         */
        RatisKVRequest kvRequest = serializer.deserialize(request.getContent().toByteArray(), RatisKVRequest.class.getName());
        final CmdType cmdType = kvRequest.getCmdType();
        RatisKVResponse.RatisKVResponseBuilder builder = RatisKVResponse.builder()
                .requestId(kvRequest.getRequestId())
                .traceId(kvRequest.getTraceId())
                .cmdType(cmdType)
                .success(true);
        switch (cmdType) {
            case GET:
                log.info("GET op.");
                final GetRequest getRequest = kvRequest.getGetRequest();
                byte[] result = dbStore.get(getRequest.getKey());
                GetResponse getResponse = GetResponse.builder()
                        .value(result)
                        .build();
                builder.getResponse(getResponse);
                break;
            case PUT:
                log.info("PUT op.");
                final PutRequest putRequest = kvRequest.getPutRequest();
                try {
                    dbStore.put(putRequest.getKey(), putRequest.getValue());
                } catch (Exception e) {
                    log.error("PUT exception: {}", e.getMessage(), e);
                    builder.success(false)
                            .message(e.getMessage());
                }
                if (watchMap.containsKey(putRequest.getKey())) {
                    eventQueue.put(new DataChangeEvent(DataChangeEvent.Type.NODE_ADDED,
                            serializer.deserialize(putRequest.getKey(), String.class.getName()),
                            putRequest.getValue()));
                }
                break;
            case DELETE:
                log.info("DELETE op.");
                final DeleteRequest deleteRequest = kvRequest.getDeleteRequest();
                try {
                    dbStore.delete(deleteRequest.getKey());
                } catch (Exception e) {
                    log.error("DELETE exception: {}", e.getMessage(), e);
                    builder.success(false)
                            .message(e.getMessage());
                }
                if (watchMap.containsKey(deleteRequest.getKey())) {
                    eventQueue.put(new DataChangeEvent(DataChangeEvent.Type.NODE_REMOVED,
                            serializer.deserialize(deleteRequest.getKey(), String.class.getName()),
                            EMPTY_BYTE));
                }
                break;
            case WATCH:
                log.info("WATCH op.");
                final WatchRequest watchRequest = kvRequest.getWatchRequest();
                try {
                    watchMap.put(watchRequest.getKey(), EMPTY_BYTE);
                } catch (Exception e) {
                    log.error("WATCH exception: {}", e.getMessage(), e);
                    builder.success(false)
                            .message(e.getMessage());
                }
                break;
            case UNWATCH:
                log.info("UNWATCH op.");
                final UnwatchRequest unwatchRequest = kvRequest.getUnwatchRequest();
                try {
                    watchMap.remove(unwatchRequest.getKey());
                } catch (Exception e) {
                    log.error("UNWATCH exception: {}", e.getMessage(), e);
                    builder.success(false)
                            .message(e.getMessage());
                }
                break;
            case BPOP:
                log.info("BPOP op.");
                try {
                    final DataChangeEvent event = eventQueue.poll(120, TimeUnit.SECONDS);
                    log.info("BPOP event: {}", event);
                    BPopResponse response = new BPopResponse();
                    response.setCmdType(cmdType);
                    response.setEvent(event);
                    builder.bPopResponse(response);
                } catch (InterruptedException e) {
                    log.error("BPOP exception: {}", e.getMessage(), e);
                    builder.success(false)
                            .message(e.getMessage());
                }
                break;
            default:
                throw new RuntimeException("Unsupported request type: " + request.getClass().getName());
        }
        return Message.valueOf(ByteString.copyFrom(serializer.serialize(builder.build())));
    }

    @Override
    public void close() throws IOException {
        super.close();
        storageEngine.close();
    }

    private static <T> CompletableFuture<T> completeExceptionally(Exception e) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }

}
