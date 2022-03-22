package com.tomgs.ratis.kv.core;

import com.tomgs.common.kv.CacheServer;
import com.tomgs.common.kv.CacheSourceConfig;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.util.NetUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * RatisKVServer
 *
 * @author tomgs
 * @since 2022/3/22
 */
public class RatisKVServer implements CacheServer {

    private final RaftServer server;

    private RaftGroup raftGroup;

    public RatisKVServer(final CacheSourceConfig sourceConfig) throws IOException {
        // create peers
        final String[] addresses = Optional.ofNullable(sourceConfig.getServerAddresses())
                .map(s -> s.split(","))
                .orElse(null);
        if (addresses == null || addresses.length == 0) {
            throw new IllegalArgumentException("Failed to get " + sourceConfig.getServerAddresses() + " from " + sourceConfig);
        }

        final RaftPeer currentPeer = RaftPeer.newBuilder().setId(sourceConfig.getEndpoint().replace(":", "_")).setAddress(sourceConfig.getEndpoint()).build();

        final File storageDir = new File(sourceConfig.getDataPath() + "/" + currentPeer.getId());
        final File dbDir = new File(sourceConfig.getDataPath()  + "/" + currentPeer.getId());

        //create a property object
        RaftProperties properties = new RaftProperties();
        //set the storage directory (different for each peer) in RaftProperty object
        RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(storageDir));
        //set the port which server listen to in RaftProperty object
        final int port = NetUtils.createSocketAddr(sourceConfig.getEndpoint()).getPort();
        GrpcConfigKeys.Server.setPort(properties, port);
        //create the counter state machine which hold the counter value
        RatisKVServerStateMachine serverStateMachine = new RatisKVServerStateMachine();
        //create and start the Raft server
        this.server = RaftServer.newBuilder()
                .setGroup(getRaftGroup(addresses))
                .setProperties(properties)
                .setServerId(currentPeer.getId())
                .setStateMachine(serverStateMachine)
                .build();
    }

    @Override
    public void start() throws IOException {
        server.start();
    }

    @Override
    public void close() throws IOException {
        server.close();
    }

    private static final UUID CLUSTER_GROUP_ID = UUID.fromString("02511d47-d67c-49a3-9011-abb3109a44c1");

    public synchronized RaftGroup getRaftGroup(String[] addresses) {
        if (raftGroup != null) {
            return raftGroup;
        }
        final List<RaftPeer> peers = new ArrayList<>(addresses.length);
        for (String address : addresses) {
            peers.add(RaftPeer.newBuilder().setId(address.replace(":", "_")).setAddress(address).build());
        }
        final List<RaftPeer> raftPeers = Collections.unmodifiableList(peers);
        raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(CLUSTER_GROUP_ID), raftPeers);
        return raftGroup;
    }

}
