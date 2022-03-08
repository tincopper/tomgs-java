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
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.util.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Simplest Ratis server, use a simple state machine {@link CounterStateMachine}
 * which maintain a counter across multi server.
 * This server application designed to run several times with different
 * parameters (1,2 or 3). server addresses hard coded in {@link Constants}
 * <p>
 * Run this application three times with three different parameter set-up a
 * ratis cluster which maintain a counter value replicated in each server memory
 *
 *     public void testStateMachineRegistry() throws Throwable {
 *         final Map<RaftGroupId, StateMachine> registry = new ConcurrentHashMap<>();
 *         registry.put(RaftGroupId.randomId(), new SimpleStateMachine4Testing());
 *         registry.put(RaftGroupId.randomId(), new SMTransactionContext());
 *
 *         try (MiniRaftClusterWithSimulatedRpc cluster = newCluster(0)) {
 *             cluster.setStateMachineRegistry(registry::get);
 *
 *             final RaftPeerId id = RaftPeerId.valueOf("s0");
 *             cluster.putNewServer(id, null, true);
 *             cluster.start();
 *
 *             for (RaftGroupId gid : registry.keySet()) {
 *                 final RaftGroup newGroup = RaftGroup.valueOf(gid, cluster.getPeers());
 *                 LOG.info("add new group: " + newGroup);
 *                 try (final RaftClient client = cluster.createClient(newGroup)) {
 *                     for (RaftPeer p : newGroup.getPeers()) {
 *                         client.getGroupManagementApi(p.getId()).add(newGroup);
 *                     }
 *                 }
 *             }
 *
 *             final RaftServer server = cluster.getServer(id);
 *             for (Map.Entry<RaftGroupId, StateMachine> e : registry.entrySet()) {
 *                 Assert.assertSame(e.getValue(), server.getDivision(e.getKey()).getStateMachine());
 *             }
 *         }
 *     }
 */
public final class MultiRaftCounterServer2 implements Closeable {
    public static final Logger LOG = LoggerFactory.getLogger(MultiRaftCounterServer2.class);

    private final RaftServer server;


    public MultiRaftCounterServer2(RaftPeer peer, File storageDir) throws IOException {
        //create a property object
        RaftProperties properties = new RaftProperties();

        //set the storage directory (different for each peer) in RaftProperty object
        RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(storageDir));

        //set the port which server listen to in RaftProperty object
        final int port = NetUtils.createSocketAddr(peer.getAddress()).getPort();
        GrpcConfigKeys.Server.setPort(properties, port);

        //create and start the Raft server
        //final RaftPeerId id = RaftPeerId.valueOf("s0");
        //this.server = ServerImplUtils.newRaftServer(peer.getId(), null, registry::get, properties, null);

        this.server = RaftServer.newBuilder()
                .setGroup(Constants.RAFT_GROUP)
                .setProperties(properties)
                .setServerId(peer.getId())
                .setStateMachine(new MultiGroupStateMachine("multi-master"))
                .build();
    }

    public void start() throws IOException {
        server.start();
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
        final File storageDir = new File("./" + currentPeer.getId());
        final MultiRaftCounterServer2 multiRaftCounterServer = new MultiRaftCounterServer2(currentPeer, storageDir);
        multiRaftCounterServer.start();

        //exit when any input entered
        Scanner scanner = new Scanner(System.in, UTF_8.name());
        scanner.nextLine();
        multiRaftCounterServer.close();
    }
}
