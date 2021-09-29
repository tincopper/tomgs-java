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
package com.tomgs.registry.jraft.rhea.cmd.store;

import com.alipay.sofa.jraft.util.BytesUtil;
import com.tomgs.registry.jraft.rhea.util.concurrent.DistributedLock;

/**
 *
 * @author jiachun.fjc
 */
public class KeyUnlockRequest extends BaseRequest {

    private static final long        serialVersionUID = 4301415966181665575L;

    private byte[]                   key;
    private DistributedLock.Acquirer acquirer;

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public DistributedLock.Acquirer getAcquirer() {
        return acquirer;
    }

    public void setAcquirer(DistributedLock.Acquirer acquirer) {
        this.acquirer = acquirer;
    }

    @Override
    public byte magic() {
        return KEY_UNLOCK;
    }

    @Override
    public String toString() {
        return "KeyUnlockRequest{" + "key=" + BytesUtil.toHex(key) + ", acquirer=" + acquirer + "} " + super.toString();
    }
}
