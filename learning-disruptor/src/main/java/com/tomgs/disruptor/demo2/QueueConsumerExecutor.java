/*
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.tomgs.disruptor.demo2;


/**
 * The type Queue consumer executor.
 *
 * @param <T> the type parameter
 * @author sixh chebin
 */
public abstract class QueueConsumerExecutor<T> implements Runnable {
    /**
     * 数据处理.
     */
    private T data;

    /**
     * 获取数据.
     *
     * @return 数据 ；
     */
    public T getData() {
        return data;
    }

    /**
     * 设置一个数据.
     *
     * @param data data;
     */
    public void setData(T data) {
        this.data = data;
    }
}
