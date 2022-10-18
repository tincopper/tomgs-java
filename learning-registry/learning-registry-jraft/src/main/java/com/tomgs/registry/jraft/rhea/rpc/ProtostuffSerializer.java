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
package com.tomgs.registry.jraft.rhea.rpc;

import com.alipay.remoting.serialization.Serializer;
import com.tomgs.registry.jraft.rhea.serialization.Serializers;
import com.tomgs.registry.jraft.rhea.util.Maps;
import com.alipay.sofa.jraft.util.internal.ThrowUtil;

import java.util.concurrent.ConcurrentMap;

/**
 * @author jiachun.fjc
 */
public class ProtostuffSerializer implements Serializer {

    public static final ProtostuffSerializer INSTANCE   = new ProtostuffSerializer();

    private static final ConcurrentMap<String, Class<?>>              classCache = Maps.newConcurrentMap();

    private final com.tomgs.registry.jraft.rhea.serialization.Serializer delegate   = Serializers.getSerializer(Serializers.PROTO_STUFF);

    @Override
    public byte[] serialize(final Object obj) {
        return this.delegate.writeObject(obj);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(final byte[] data, final String classOfT) {
        Class<?> clazz = classCache.get(classOfT);
        if (clazz == null) {
            try {
                final Class<?> newClazz = Class.forName(classOfT);
                clazz = classCache.putIfAbsent(classOfT, newClazz);
                if (clazz == null) {
                    clazz = newClazz;
                }
            } catch (final Exception e) {
                ThrowUtil.throwException(e);
            }
        }
        return (T) this.delegate.readObject(data, clazz);
    }
}
