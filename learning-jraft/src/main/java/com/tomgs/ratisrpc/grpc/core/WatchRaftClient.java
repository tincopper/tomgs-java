package com.tomgs.ratisrpc.grpc.core;

import org.apache.ratis.client.RaftClient;
import org.apache.ratis.client.RaftClientRpc;
import org.apache.ratis.client.api.*;
import org.apache.ratis.client.impl.RaftClientImpl;
import org.apache.ratis.protocol.ClientId;
import org.apache.ratis.protocol.RaftPeerId;

import java.io.IOException;

/**
 * WatchRaftClient
 *
 * @author tomgs
 * @since 1.0
 */
public class WatchRaftClient implements RaftClient {

    private final RaftClientImpl raftClient;

    public WatchRaftClient(final RaftClientImpl raftClient) {
        this.raftClient = raftClient;
    }

    @Override
    public ClientId getId() {
        return raftClient.getId();
    }

    @Override
    public RaftPeerId getLeaderId() {
        return raftClient.getLeaderId();
    }

    @Override
    public RaftClientRpc getClientRpc() {
        return raftClient.getClientRpc();
    }

    @Override
    public AdminApi admin() {
        return raftClient.admin();
    }

    @Override
    public GroupManagementApi getGroupManagementApi(RaftPeerId server) {
        return raftClient.getGroupManagementApi(server);
    }

    @Override
    public SnapshotManagementApi getSnapshotManagementApi() {
        return raftClient.getSnapshotManagementApi();
    }

    @Override
    public SnapshotManagementApi getSnapshotManagementApi(RaftPeerId server) {
        return raftClient.getSnapshotManagementApi(server);
    }

    @Override
    public LeaderElectionManagementApi getLeaderElectionManagementApi(RaftPeerId server) {
        return raftClient.getLeaderElectionManagementApi(server);
    }

    @Override
    public BlockingApi io() {
        return raftClient.io();
    }

    @Override
    public AsyncApi async() {
        return raftClient.async();
    }

    @Override
    public MessageStreamApi getMessageStreamApi() {
        return raftClient.getMessageStreamApi();
    }

    @Override
    public DataStreamApi getDataStreamApi() {
        return raftClient.getDataStreamApi();
    }

    @Override
    public void close() throws IOException {
        raftClient.close();
    }
}
