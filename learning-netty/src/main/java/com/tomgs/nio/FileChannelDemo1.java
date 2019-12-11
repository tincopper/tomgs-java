package com.tomgs.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *  FileChannel学习用例
 *
 * @author tomgs
 * @version 2019/11/25 1.0 
 */
public class FileChannelDemo1 {

    public static void main(String[] args) throws IOException {

        String str = "hello world!";
        FileOutputStream fileInputStream = new FileOutputStream("F:\\test.txt");
        FileChannel channel = fileInputStream.getChannel();

        //写数据
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(str.getBytes());

        //将其转换为读
        byteBuffer.flip();
        //将数据写入文件
        channel.write(byteBuffer);
        fileInputStream.close();
    }
    
}
