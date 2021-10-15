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
package com.tomgs.registry.jraft.rhea.errors;

import com.tomgs.registry.jraft.rhea.errors.ApiException;

/**
 *
 * @author jiachun.fjc
 */
public class StoreHeartbeatOutOfDateException extends ApiException {

    private static final long serialVersionUID = -6941357836000397648L;

    public StoreHeartbeatOutOfDateException() {
    }

    public StoreHeartbeatOutOfDateException(String message) {
        super(message);
    }

    public StoreHeartbeatOutOfDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreHeartbeatOutOfDateException(Throwable cause) {
        super(cause);
    }
}