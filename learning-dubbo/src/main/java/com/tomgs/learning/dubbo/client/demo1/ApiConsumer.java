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

package com.tomgs.learning.dubbo.client.demo1;

import com.tomgs.learning.dubbo.api.Helloworld;
import com.tomgs.learning.dubbo.api.Greeter;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ApiConsumer {

    public static void main(String[] args) throws InterruptedException, IOException {

        ApplicationConfig applicationConfig = new ApplicationConfig("demo-consumer");
        // 关闭qos服务
        applicationConfig.setQosEnable(false);
        ReferenceConfig<Greeter> ref = new ReferenceConfig<>();
        ref.setInterface(Greeter.class);
        ref.setCheck(false);
        ref.setProtocol(CommonConstants.DUBBO_PROTOCOL);
        ref.setLazy(false);
        ref.setTimeout(100000);
        ref.setApplication(applicationConfig);
        ref.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        final Greeter iGreeter = ref.get();

        System.out.println("dubbo ref started");

        //sayHello(iGreeter);
        sayHello2(iGreeter);

        System.in.read();
    }

    private static void sayHello2(Greeter iGreeter) {
        try {
            String reply = iGreeter.sayHello2("tomgs");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Reply:" + reply);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void sayHello(Greeter iGreeter) {
        Helloworld.HelloRequest req = Helloworld.HelloRequest.newBuilder().setName("laurence").build();
        try {
            final Helloworld.User reply = iGreeter.sayHello(req);
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Reply:" + reply);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
