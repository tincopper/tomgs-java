package com.tomgs.ratis.kv.core;

import com.alipay.remoting.exception.CodecException;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.storage.RaftStorage;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.apache.ratis.thirdparty.com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * RatisServerStateMachine
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisKVServerStateMachine extends BaseStateMachine {

    private final ProtostuffSerializer serializer = new ProtostuffSerializer();

    @Override
    public void initialize(RaftServer raftServer, RaftGroupId raftGroupId, RaftStorage storage) throws IOException {
        super.initialize(raftServer, raftGroupId, storage);
    }

    @Override
    public void reinitialize() throws IOException {
        super.reinitialize();
    }

    @Override
    public long takeSnapshot() throws IOException {
        return super.takeSnapshot();
    }

    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
        return super.applyTransaction(trx);
    }

    @Override
    public CompletableFuture<Message> query(Message request) {
        try {
            RatisKVRequest kvRequest = serializer.deserialize(request.getContent().toByteArray(), RatisKVRequest.class.getName());
            // do something
            byte[] bytes = serializer.serialize("success");
            return CompletableFuture.completedFuture(Message.valueOf(ByteString.copyFrom(bytes)));
        } catch (CodecException e) {
            return completeExceptionally(e);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    private static <T> CompletableFuture<T> completeExceptionally(Exception e) {
        final CompletableFuture<T> future = new CompletableFuture<>();
        future.completeExceptionally(e);
        return future;
    }

}
