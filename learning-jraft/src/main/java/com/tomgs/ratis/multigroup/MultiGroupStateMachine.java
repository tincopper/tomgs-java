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
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.client.api.GroupManagementApi;
import org.apache.ratis.protocol.*;
import org.apache.ratis.protocol.exceptions.AlreadyExistsException;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.protocol.TermIndex;
import org.apache.ratis.server.raftlog.RaftLog;
import org.apache.ratis.server.storage.RaftStorage;
import org.apache.ratis.statemachine.StateMachine;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.apache.ratis.statemachine.impl.SimpleStateMachineStorage;
import org.apache.ratis.statemachine.impl.SingleFileSnapshotInfo;
import org.apache.ratis.util.JavaUtils;

import java.io.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * State machine implementation for Counter server application. This class
 * maintain a {@link AtomicInteger} object as a state and accept two commands:
 * GET and INCREMENT, GET is a ReadOnly command which will be handled by
 * {@code query} method however INCREMENT is a transactional command which
 * will be handled by {@code applyTransaction}.
 */
public class MultiGroupStateMachine extends BaseStateMachine {
    private final SimpleStateMachineStorage storage =
            new SimpleStateMachineStorage();

    private final String groupId;

    private AtomicLong leaderTerm = new AtomicLong(-1L);

    private AtomicBoolean isLeader = new AtomicBoolean(false);

    private final Map<RaftGroupId, StateMachine> registry = new ConcurrentHashMap<>();

    public MultiGroupStateMachine(String groupId) {
        this.groupId = groupId;
        registry.put(Constants.RAFT_GROUP1.getGroupId(),
                new MultiGroupCounterStateMachine("test-counter-1"));
        registry.put(Constants.RAFT_GROUP2.getGroupId(),
                new MultiGroupCounterStateMachine("test-counter-2"));
    }

    /**
     * initialize the state machine by initilize the state machine storage and
     * calling the load method which reads the last applied command and restore it
     * in counter object)
     *
     * @param server      the current server information
     * @param groupId     the cluster groupId
     * @param raftStorage the raft storage which is used to keep raft related
     *                    stuff
     * @throws IOException if any error happens during load state
     */
    /*@Override
    public void initialize(RaftServer server, RaftGroupId groupId,
                           RaftStorage raftStorage) throws IOException {
        super.initialize(server, groupId, raftStorage);
        this.storage.init(raftStorage);
        load(storage.getLatestSnapshot());
    }*/

    /**
     * very similar to initialize method, but doesn't initialize the storage
     * system because the state machine reinitialized from the PAUSE state and
     * storage system initialized before.
     *
     * @throws IOException if any error happens during load state
     */
    /*@Override
    public void reinitialize() throws IOException {
        load(storage.getLatestSnapshot());
    }*/

    /**
     * Store the current state as an snapshot file in the stateMachineStorage.
     *
     * @return the index of the snapshot
     */
    /*@Override
    public long takeSnapshot() {
        //get the last applied index
        final TermIndex last = getLastAppliedTermIndex();

        //create a file with a proper name to store the snapshot
        final File snapshotFile =
                storage.getSnapshotFile(last.getTerm(), last.getIndex());

        //serialize the counter object and write it into the snapshot file
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(snapshotFile)))) {
            out.writeObject(leaderTerm);
        } catch (IOException ioe) {
            LOG.warn("Failed to write snapshot file \"" + snapshotFile
                    + "\", last applied index=" + last);
        }

        //return the index of the stored snapshot (which is the last applied one)
        return last.getIndex();
    }*/

    /**
     * Load the state of the state machine from the storage.
     *
     * @param snapshot to load
     * @return the index of the snapshot or -1 if snapshot is invalid
     * @throws IOException if any error happens during read from storage
     */
    /*private long load(SingleFileSnapshotInfo snapshot) throws IOException {
        //check the snapshot nullity
        if (snapshot == null) {
            LOG.warn("The snapshot info is null.");
            return RaftLog.INVALID_LOG_INDEX;
        }

        //check the existance of the snapshot file
        final File snapshotFile = snapshot.getFile().getPath().toFile();
        if (!snapshotFile.exists()) {
            LOG.warn("The snapshot file {} does not exist for snapshot {}",
                    snapshotFile, snapshot);
            return RaftLog.INVALID_LOG_INDEX;
        }

        //load the TermIndex object for the snapshot using the file name pattern of
        // the snapshot
        final TermIndex last =
                SimpleStateMachineStorage.getTermIndexFromSnapshotFile(snapshotFile);

        //read the file and cast it to the AtomicInteger and set the counter
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(snapshotFile)))) {
            //set the last applied termIndex to the termIndex of the snapshot
            setLastAppliedTermIndex(last);

            //read, cast and set the counter
            leaderTerm = JavaUtils.cast(in.readObject());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        return last.getIndex();
    }
*/
    @Override
    public void notifyLeaderChanged(RaftGroupMemberId groupMemberId, RaftPeerId newLeaderId) {
        LOG.info("----------------> " + groupId);
        System.out.println("notifyLeaderChanged ================================ " + groupMemberId + ":" + newLeaderId);
        RaftPeerId currentPeerId = groupMemberId.getPeerId();
        if (currentPeerId.equals(newLeaderId)) {
            isLeader.set(true);
            System.out.println("LEADER");

            for(RaftGroupId gid : registry.keySet()) {
                final RaftGroup newGroup = RaftGroup.valueOf(gid, Constants.PEERS);
                LOG.info("add new group: " + newGroup);
                try (final RaftClient client = MultiRaftCounterClient.buildClient(newGroup)) {
                    for (RaftPeer p : newGroup.getPeers()) {
                        GroupManagementApi groupManagementApi = client.getGroupManagementApi(p.getId());
                        groupManagementApi.add(newGroup);
                    }
                } catch (AlreadyExistsException e) {
                    // do not log
                } catch (IOException e) {
                    LOG.error("add group fail: {}", e.getMessage(), e);
                }
            }
        } else {
            isLeader.set(false);
            System.out.println("FOLLOWER");
        }
    }

    @Override
    public void notifyNotLeader(Collection<TransactionContext> pendingEntries) throws IOException {
        LOG.info("----------------> " + groupId);
        // 不是主节点时回调
        System.out.println("notifyNotLeader ================================ " + pendingEntries);
    }

    public boolean isLeader() {
        return isLeader.get();
    }

}
