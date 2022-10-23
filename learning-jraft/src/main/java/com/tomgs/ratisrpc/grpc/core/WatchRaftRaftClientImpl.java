package com.tomgs.ratisrpc.grpc.core;

import com.tomgs.ratisrpc.grpc.client.WatchClientRpc;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.client.RaftClientRpc;
import org.apache.ratis.client.api.*;
import org.apache.ratis.protocol.ClientId;
import org.apache.ratis.protocol.RaftPeerId;

import java.io.IOException;

/**
 * WatchRaftClient
 *
 * @author tomgs
 * @since 1.0
 */
public class WatchRaftRaftClientImpl implements WatchRaftClient {

    private final RaftClient raftClient;

    private final WatchClientRpc watchClientRpc;

    public WatchRaftRaftClientImpl(final RaftClient raftClient, final WatchClientRpc watchClientRpc) {
        this.raftClient = raftClient;
        this.watchClientRpc = watchClientRpc;
    }

    @Override
    public RaftClient raftClient() {
        return raftClient;
    }
    
}
