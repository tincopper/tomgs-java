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
package com.tomgs.registry.jraft.rhea.cmd.pd;

import com.tomgs.registry.jraft.rhea.metadata.Region;
import com.tomgs.registry.jraft.rhea.metadata.RegionStats;
import com.tomgs.registry.jraft.rhea.util.Pair;

import java.util.List;

/**
 *
 * @author jiachun.fjc
 */
public class RegionHeartbeatRequest extends BaseRequest {

    private static final long               serialVersionUID = -5149082334939576598L;

    private long                            storeId;
    private long                            leastKeysOnSplit;
    private List<Pair<Region, RegionStats>> regionStatsList;

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getLeastKeysOnSplit() {
        return leastKeysOnSplit;
    }

    public void setLeastKeysOnSplit(long leastKeysOnSplit) {
        this.leastKeysOnSplit = leastKeysOnSplit;
    }

    public List<Pair<Region, RegionStats>> getRegionStatsList() {
        return regionStatsList;
    }

    public void setRegionStatsList(List<Pair<Region, RegionStats>> regionStatsList) {
        this.regionStatsList = regionStatsList;
    }

    @Override
    public byte magic() {
        return REGION_HEARTBEAT;
    }

    @Override
    public String toString() {
        return "RegionHeartbeatRequest{" + "storeId=" + storeId + ", leastKeysOnSplit=" + leastKeysOnSplit
               + ", regionStatsList=" + regionStatsList + "} " + super.toString();
    }
}
