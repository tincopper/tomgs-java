/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tomgs.registry.jraft.rhea.metadata;

import com.tomgs.registry.jraft.rhea.metadata.Peer;

import java.io.Serializable;

/**
 *
 * @author jiachun.fjc
 */
public class PeerStats implements Serializable {

    private static final long serialVersionUID = 785959293291029071L;

    private Peer              peer;
    private int               downSeconds;

    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
        this.peer = peer;
    }

    public int getDownSeconds() {
        return downSeconds;
    }

    public void setDownSeconds(int downSeconds) {
        this.downSeconds = downSeconds;
    }

    @Override
    public String toString() {
        return "PeerStats{" + "peer=" + peer + ", downSeconds=" + downSeconds + '}';
    }
}
