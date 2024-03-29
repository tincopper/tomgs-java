/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tomgs.learning.dubbo.server.demo1;


import com.tomgs.learning.dubbo.api.Helloworld;
import com.tomgs.learning.dubbo.api.Greeter;

public class IGreeter1Impl implements Greeter {
    @Override
    public Helloworld.User sayHello(Helloworld.HelloRequest request) {
        System.out.println("receiv: " + request);
        Helloworld.User usr = Helloworld.User.newBuilder()
                .setName("hello " + request.getName())
                .setAge(18)
                .setId("12345").build();
        return usr;
    }

    @Override
    public String sayHello2(String name) {
        System.out.println("receiv: " + name);
        return "hello: " + name;
    }
}
