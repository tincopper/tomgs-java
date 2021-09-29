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
package com.tomgs.jraft.kv.core;

import com.alipay.sofa.jraft.rhea.client.DefaultRheaKVStore;
import com.alipay.sofa.jraft.rhea.client.RheaKVStore;
import com.alipay.sofa.jraft.rhea.client.pd.PlacementDriverClient;
import com.alipay.sofa.jraft.rhea.options.RheaKVStoreOptions;
import com.alipay.sofa.jraft.rpc.RpcServer;

/**
 *
 * @author jiachun.fjc
 */
public class Node {

    private final RheaKVStoreOptions options;

    private DefaultRheaKVStore rheaKVStore;

    public Node(RheaKVStoreOptions options) {
        this.options = options;
    }

    public void start() {
        this.rheaKVStore = new DefaultRheaKVStore();
        this.rheaKVStore.init(this.options);
        PlacementDriverClient client = this.rheaKVStore.getPlacementDriverClient();
        RpcServer rpcServer = rheaKVStore.getStoreEngine().getRpcServer();
    }

    public void stop() {
        this.rheaKVStore.shutdown();
    }

    public RheaKVStore getRheaKVStore() {
        return rheaKVStore;
    }
}
