package com.tomgs.pulsar.transaction;

import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.transaction.Transaction;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 1、要使用pulsar的事务，前提需要pulsar的broker已经开启事务
 *
 * @author tomgs
 * @version 2022/3/2 1.0
 */
public class TransactionTest {

    @Test
    public void testTxnBase() throws PulsarClientException, ExecutionException, InterruptedException {
        // 创建一个支持事务的客户端
        PulsarClient pulsarClient = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .enableTransaction(true) // 开启事务
                .build();

        // 创建一个事务
        Transaction txn = pulsarClient
                .newTransaction()
                .withTransactionTimeout(5, TimeUnit.MINUTES)
                .build()
                .get();

        try {
            //3.1: 接收消息
            Consumer<byte[]> consumer = pulsarClient.newConsumer()
                    .topic("persistent://public/default/txn_t1")
                    .subscriptionName("my-subscription")
                    //.enableBatchIndexAcknowledgment(true)  开启批量消息确认
                    .subscribe();

            //3.2 获取消息
            Message<byte[]> message = consumer.receive();

            System.out.println("消息为:" + message.getTopicName() + ":" + new String(message.getData()));

            //3.3  将接收到的消息, 处理后, 发送到另一个Topic中
            Producer<byte[]> producer = pulsarClient.newProducer()
                    .topic("persistent://public/default/txn_t2")
                    .sendTimeout(0, TimeUnit.MILLISECONDS)
                    .create();

            producer.newMessage(txn).value(message.getData()).send();

            System.out.println(1111);
            //3.4: 确认输入的消息
            consumer.acknowledge(message);
            //4. 如果正常, 就提交事务
            txn.commit();
        } catch (Exception e) {
            System.out.println(1111);
            // 否则就回滚事务
            txn.abort();
            e.printStackTrace();
        }
    }

}
