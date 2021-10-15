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

import com.tomgs.registry.jraft.rhea.errors.InvalidRegionEpochException;
import com.tomgs.registry.jraft.rhea.errors.InvalidRegionMembershipException;
import com.tomgs.registry.jraft.rhea.errors.InvalidRegionVersionException;

/**
 * @author jiachun.fjc
 */
public final class ApiExceptionHelper {

    // require refresh region route table
    public static boolean isInvalidEpoch(final Throwable cause) {
        return cause instanceof InvalidRegionMembershipException //
               || cause instanceof InvalidRegionVersionException //
               || cause instanceof InvalidRegionEpochException;
    }

    private ApiExceptionHelper() {
    }
}