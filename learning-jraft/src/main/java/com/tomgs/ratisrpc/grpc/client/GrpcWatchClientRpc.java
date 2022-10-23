package com.tomgs.ratisrpc.grpc.client;

import com.tomgs.learning.grpc.proto.WatchRequest;
import com.tomgs.learning.grpc.proto.WatchResponse;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.protocol.ClientId;
import org.apache.ratis.protocol.RaftPeer;

import java.io.IOException;
import java.util.Collection;

/**
 * GrpcWatchClientRpc
 *
 * @author tomgs
 * @version 1.0
 */
public class GrpcWatchClientRpc implements WatchClientRpc {

    public GrpcWatchClientRpc(ClientId clientId, RaftProperties properties) {

    }

    @Override
    public WatchResponse sendRequest(WatchRequest request) throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void addRaftPeers(Collection<RaftPeer> collection) {

    }

}
