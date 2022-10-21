/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tomgs.ratis.multigroup;

import com.tomgs.ratis.common.Constants;
import com.tomgs.ratis.counter.server.CounterStateMachine;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.client.impl.ClientProtoUtils;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.GroupManagementRequest;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.exceptions.AlreadyExistsException;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.server.impl.ServerImplUtils;
import org.apache.ratis.statemachine.StateMachine;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.apache.ratis.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Simplest Ratis server, use a simple state machine {@link CounterStateMachine}
 * which maintain a counter across multi server.
 * This server application designed to run several times with different
 * parameters (1,2 or 3). server addresses hard coded in {@link Constants}
 * <p>
 * Run this application three times with three different parameter set-up a
 * ratis cluster which maintain a counter value replicated in each server memory
 * <p>
 * public void testStateMachineRegistry() throws Throwable {
 * final Map<RaftGroupId, StateMachine> registry = new ConcurrentHashMap<>();
 * registry.put(RaftGroupId.randomId(), new SimpleStateMachine4Testing());
 * registry.put(RaftGroupId.randomId(), new SMTransactionContext());
 * <p>
 * try (MiniRaftClusterWithSimulatedRpc cluster = newCluster(0)) {
 * cluster.setStateMachineRegistry(registry::get);
 * <p>
 * final RaftPeerId id = RaftPeerId.valueOf("s0");
 * cluster.putNewServer(id, null, true);
 * cluster.start();
 * <p>
 * for (RaftGroupId gid : registry.keySet()) {
 * final RaftGroup newGroup = RaftGroup.valueOf(gid, cluster.getPeers());
 * LOG.info("add new group: " + newGroup);
 * try (final RaftClient client = cluster.createClient(newGroup)) {
 * for (RaftPeer p : newGroup.getPeers()) {
 * client.getGroupManagementApi(p.getId()).add(newGroup);
 * }
 * }
 * }
 * <p>
 * final RaftServer server = cluster.getServer(id);
 * for (Map.Entry<RaftGroupId, StateMachine> e : registry.entrySet()) {
 * Assert.assertSame(e.getValue(), server.getDivision(e.getKey()).getStateMachine());
 * }
 * }
 * }
 */
public final class MultiRaftCounterServer2 implements Closeable {
    public static final Logger LOG = LoggerFactory.getLogger(MultiRaftCounterServer2.class);

    private final RaftServer server;

    private static final Map<RaftGroupId, StateMachine> registry = new ConcurrentHashMap<>();

    private static final Map<RaftGroupId, RaftGroup> groups = new ConcurrentHashMap<>();

    public MultiRaftCounterServer2(RaftPeer peer, File storageDir) throws IOException {
        registry.put(Constants.RAFT_GROUP1.getGroupId(),
                new MultiGroupCounterStateMachine("test-counter-1"));
        registry.put(Constants.RAFT_GROUP2.getGroupId(),
                new MultiGroupCounterStateMachine("test-counter-2"));

        // 这个主要是来设置不同节点在不同分组中的优先级，这样可以避免在不同分组的主节点都落在同一个节点上，失去了多分组的意义
        groups.put(Constants.RAFT_GROUP1.getGroupId(), Constants.RAFT_GROUP1);
        groups.put(Constants.RAFT_GROUP2.getGroupId(), Constants.RAFT_GROUP2);

        //create a property object
        RaftProperties properties = new RaftProperties();

        //set the storage directory (different for each peer) in RaftProperty object
        RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(storageDir));

        //set the port which server listen to in RaftProperty object
        final int port = NetUtils.createSocketAddr(peer.getAddress()).getPort();
        GrpcConfigKeys.Server.setPort(properties, port);

        //create and start the Raft server
        // 多分组raft一定需要使用RaftServerProxy
        // this.server = ServerImplUtils.newRaftServer(peer.getId(), null, registry::get, properties, null);
        this.server = ServerImplUtils.newRaftServer(peer.getId(), Constants.RAFT_GROUP1, registry::get,
                null, properties, null);
    }

    public void start() throws IOException {
        server.start();

        // 这种方式太复杂了。。。
        /*RaftProtos.GroupManagementRequestProto groupManagementRequestProto = RaftProtos
                .GroupManagementRequestProto
                .newBuilder()
                .setGroupAdd(RaftProtos.GroupAddRequestProto.newBuilder()
                        .setGroup(RaftProtos.RaftGroupProto.newBuilder()
                                .setGroupId(RaftProtos.RaftGroupIdProto.newBuilder()
                                        .setId(ByteString.copyFrom("123".getBytes()))
                                        .build())
                                .setPeers(0, RaftProtos.RaftPeerProto.newBuilder()
                                        .setId(ByteString.copyFrom("12345678901".getBytes()))
                                        .setAddress("127.0.0.1:8081")
                                        .setPriority(1)
                                        .build())
                                .setPeers(1, RaftProtos.RaftPeerProto.newBuilder()
                                        .setId(ByteString.copyFrom("12345678902".getBytes()))
                                        .setAddress("127.0.0.1:8082")
                                        .setPriority(1)
                                        .build())
                                .setPeers(2, RaftProtos.RaftPeerProto.newBuilder()
                                        .setId(ByteString.copyFrom("12345678903".getBytes()))
                                        .setAddress("127.0.0.1:8083")
                                        .setPriority(1)
                                        .build())
                                .build())
                        .build())
                .build();
        final GroupManagementRequest groupManagementRequest = ClientProtoUtils.toGroupManagementRequest(groupManagementRequestProto);
        server.groupManagement(groupManagementRequest);*/

        // 创建多个分组，这个可以在运行时创建，非常方便
        for (RaftGroupId gid : registry.keySet()) {
            // 这里在Group添加Peers时可以设置节点优先级，这样可以使leader分布在不同的节点
            final RaftGroup newGroup = RaftGroup.valueOf(gid, groups.get(gid).getPeers());
            LOG.info("add new group: " + newGroup);
            try (final RaftClient client = MultiRaftCounterClient.buildClient(newGroup)) {
                for (RaftPeer p : newGroup.getPeers()) {
                    try {
                        client.getGroupManagementApi(p.getId()).add(newGroup);
                    } catch (AlreadyExistsException e) {
                        // do not log
                    } catch (IOException ioe) {
                        LOG.warn("Add group failed for {}", p, ioe);
                    }
                }
            }
        }

    }

    @Override
    public void close() throws IOException {
        server.close();
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("Usage: java -cp *.jar com.tomgs.ratis.counter.server.MultiRaftCounterServer {serverIndex}");
            System.err.println("{serverIndex} could be 1, 2 or 3");
            System.exit(1);
        }

        //find current peer object based on application parameter
        final RaftPeer currentPeer = Constants.PEERS.get(Integer.parseInt(args[0]) - 1);

        //start a counter server
        final File storageDir = new File("./target/" + currentPeer.getId());
        final MultiRaftCounterServer2 multiRaftCounterServer = new MultiRaftCounterServer2(currentPeer, storageDir);
        multiRaftCounterServer.start();

        //exit when any input entered
        Scanner scanner = new Scanner(System.in, UTF_8.name());
        scanner.nextLine();
        multiRaftCounterServer.close();
    }
}
