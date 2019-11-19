package com.tomgs.core.oom;

import sun.misc.VM;

import java.nio.ByteBuffer;

/**
 *  堆外内存溢出
 *  -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:MaxDirectMemorySize=5m
 *
 * @author tomgs
 * @version 2019/11/10 1.0 
 */
public class DirectBufferMemoryDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("配置的maxDirectMemory:" + (VM.maxDirectMemory() / (double) 1024 / 1024) + "MB");

        Thread.sleep(3000);
        //-XX:MaxDirectMemorySize=5m 模拟分配6m出现内存溢出
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6 * 1024 * 1024);
    }
}
