/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomgs.ratisrpc.grpc.client;

import com.tomgs.learning.grpc.proto.WatchRequest;
import com.tomgs.learning.grpc.proto.WatchResponse;
import org.apache.ratis.protocol.RaftClientReply;
import org.apache.ratis.protocol.RaftClientRequest;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.util.IOUtils;
import org.apache.ratis.util.JavaUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/** The client side rpc of a raft service. */
public interface WatchClientRpc extends RaftPeer.Add, Closeable {

  /** Send a request. */
  WatchResponse sendRequest(WatchRequest request) throws IOException;

  /**
   * Handle the given throwable. For example, try reconnecting.
   *
   * @return true if the given throwable is handled; otherwise, the call is an no-op, return false.
   */
  default boolean handleException(RaftPeerId serverId, Throwable t, boolean reconnect) {
    return false;
  }

  /**
   * Determine if the given throwable should be handled. For example, try reconnecting.
   *
   * @return true if the given throwable should be handled; otherwise, return false.
   */
  default boolean shouldReconnect(Throwable t) {
    return IOUtils.shouldReconnect(t);
  }
}
