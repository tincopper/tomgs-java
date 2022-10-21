package com.tomgs.ratisrpc.grpc.core;

import org.apache.ratis.client.RaftClientRpc;
import org.apache.ratis.grpc.client.GrpcClientRpc;
import org.apache.ratis.protocol.RaftClientReply;
import org.apache.ratis.protocol.RaftClientRequest;
import org.apache.ratis.protocol.RaftPeer;

import java.io.IOException;
import java.util.Collection;

/**
 * WatchClientRpc
 *
 * @author tomgs
 * @since 1.0
 */
public class WatchClientRpc implements RaftClientRpc {

    private final GrpcClientRpc grpcClientRpc;

    public WatchClientRpc(final GrpcClientRpc grpcClientRpc) {
        this.grpcClientRpc = grpcClientRpc;
    }

    @Override
    public RaftClientReply sendRequest(RaftClientRequest request) throws IOException {
        return grpcClientRpc.sendRequest(request);
    }

    @Override
    public void close() throws IOException {
        grpcClientRpc.close();
    }

    @Override
    public void addRaftPeers(Collection<RaftPeer> peers) {
        grpcClientRpc.addRaftPeers(peers);
    }
}
