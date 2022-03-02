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

import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.*;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.protocol.TermIndex;
import org.apache.ratis.server.raftlog.RaftLog;
import org.apache.ratis.server.storage.RaftStorage;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.apache.ratis.statemachine.impl.SimpleStateMachineStorage;
import org.apache.ratis.statemachine.impl.SingleFileSnapshotInfo;
import org.apache.ratis.util.JavaUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
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

    private AtomicLong leaderTerm = new AtomicLong(-1L);

    private AtomicLong transactions = new AtomicLong(0);

    private AtomicBoolean isLeader = new AtomicBoolean(false);

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
    @Override
    public void initialize(RaftServer server, RaftGroupId groupId,
                           RaftStorage raftStorage) throws IOException {
        super.initialize(server, groupId, raftStorage);
        this.storage.init(raftStorage);
        load(storage.getLatestSnapshot());
    }

    /**
     * very similar to initialize method, but doesn't initialize the storage
     * system because the state machine reinitialized from the PAUSE state and
     * storage system initialized before.
     *
     * @throws IOException if any error happens during load state
     */
    @Override
    public void reinitialize() throws IOException {
        load(storage.getLatestSnapshot());
    }

    /**
     * Store the current state as an snapshot file in the stateMachineStorage.
     *
     * @return the index of the snapshot
     */
    @Override
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
    }

    /**
     * Load the state of the state machine from the storage.
     *
     * @param snapshot to load
     * @return the index of the snapshot or -1 if snapshot is invalid
     * @throws IOException if any error happens during read from storage
     */
    private long load(SingleFileSnapshotInfo snapshot) throws IOException {
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

    /**
     * Handle GET command, which used by clients to get the counter value.
     *
     * @param request the GET message
     * @return the Message containing the current counter value
     */
    @Override
    public CompletableFuture<Message> query(Message request) {
        String msg = request.getContent().toString(Charset.defaultCharset());
        if (!msg.equals("GET")) {
            return CompletableFuture.completedFuture(
                    Message.valueOf("Invalid Command"));
        }
        return CompletableFuture.completedFuture(
                Message.valueOf(leaderTerm.toString()));
    }

    /**
     * Apply the INCREMENT command by incrementing the counter object.
     *
     * @param trx the transaction context
     * @return the message containing the updated counter value
     */
    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
        final RaftProtos.LogEntryProto entry = trx.getLogEntry();

        //check if the command is valid
        String logData = entry.getStateMachineLogEntry().getLogData()
                .toString(Charset.defaultCharset());
        if (!logData.equals("INCREMENT")) {
            return CompletableFuture.completedFuture(
                    Message.valueOf("Invalid Command"));
        }
        //update the last applied term and index
        final long index = entry.getIndex();
        updateLastAppliedTermIndex(entry.getTerm(), index);

        //actual execution of the command: increment the counter
        leaderTerm.incrementAndGet();

        //return the new value of the counter to the client
        final CompletableFuture<Message> f =
                CompletableFuture.completedFuture(Message.valueOf(leaderTerm.toString()));

        //if leader, log the incremented value and it's log index
        if (trx.getServerRole() == RaftProtos.RaftPeerRole.LEADER) {
            LOG.info("{}: Increment to {}", index, leaderTerm.toString());
        }

        return f;
    }

    @Override
    public void notifyLeaderChanged(RaftGroupMemberId groupMemberId, RaftPeerId newLeaderId) {
        System.out.println("notifyLeaderChanged ================================ " + groupMemberId + ":" + newLeaderId);
        RaftPeerId currentPeerId = groupMemberId.getPeerId();
        if (currentPeerId.equals(newLeaderId)) {
            isLeader.set(true);
            System.out.println("LEADER");
        } else {
            isLeader.set(false);
            System.out.println("FOLLOWER");
        }
    }

    @Override
    public void notifyNotLeader(Collection<TransactionContext> pendingEntries) throws IOException {
        // 不是主节点时回调
        System.out.println("notifyNotLeader ================================ " + pendingEntries);
    }

    @Override
    public TransactionContext startTransaction(RaftClientRequest request) throws IOException {
        System.out.println("startTransaction ================================ " + request);
        // 只有leader 才会调用此方法，所以进入此方法的即为leader
        isLeader.set(true);
        // send the next transaction id as the "context" from SM
        return TransactionContext.newBuilder()
                .setStateMachine(this)
                .setClientRequest(request)
                .setStateMachineContext(transactions.incrementAndGet())
                .build();
    }

    public boolean isLeader() {
        return isLeader.get();
    }

}
