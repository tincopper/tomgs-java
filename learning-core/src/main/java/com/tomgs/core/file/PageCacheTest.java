package com.tomgs.core.file;

import cn.hutool.core.date.StopWatch;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

/**
 * PageCacheTest
 *
 * @author tomgs
 * @since 2022/1/3
 */
public class PageCacheTest {

    private ExecutorService executorService;

    @Before
    public void setUp() {
        executorService = Executors.newFixedThreadPool(4);
    }

    @Test
    public void testPageCache() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        FileInputStream fileInputStream = new FileInputStream(new File("E:\\workspace\\hodor\\logs\\hodor-scheduler_2021-08-25_2.log.gz"));
        GZIPInputStream gis = new GZIPInputStream(fileInputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(gis));
        String readLine;
        while ((readLine = reader.readLine()) != null) {
            System.out.println(readLine);
        }
        stopWatch.stop();
        System.out.println("cost: " + stopWatch.getTotalTimeMillis());
    }

    @Test
    public void testPageCache2() throws IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        FileInputStream fileInputStream = new FileInputStream(new File("E:\\workspace\\hodor\\logs\\hodor-scheduler_2021-08-25_2.log.gz"));
        FileChannel fc = fileInputStream.getChannel();
        GZIPInputStream gis = new GZIPInputStream(Channels.newInputStream(fc));

        BufferedReader reader = new BufferedReader(new InputStreamReader(gis));
        String readLine;
        while ((readLine = reader.readLine()) != null) {
            System.out.println(readLine);
        }

        stopWatch.stop();
        System.out.println("cost: " + stopWatch.getTotalTimeMillis());
    }

    /**
     * 验证单线程读取数据并且处理数据的情况
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testReadFile1() throws IOException, InterruptedException {
        InputStream inputStream = getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("\\.")) {
                continue;
            }
            // 直接读取一个文件内容30M内容为1s
            // 业务处理
            // Thread.sleep(1);

            System.out.println(Thread.currentThread().getName() + "--> " + line);
        }
    }

    /**
     * 字节流的方式读取文件
     *
     * @throws IOException
     */
    @Test
    public void testReadFile2() throws IOException {
        InputStream inputStream = getInputStream();
        //BufferedReader reader = new BufferedReader(new InputStreamReader(gis));
        BufferedInputStream stream = new BufferedInputStream(inputStream);
        byte[] bytes = "\\.".getBytes(StandardCharsets.UTF_8);

        int read;
        while ((read = stream.read()) != -1) {
            if (read == '\\') {
                if (stream.read() == '.') {
                    System.out.println("end");
                    continue;
                }
                System.out.println(read);
            }
        }
    }

    /**
     * 1P - 4C模型，一个生产者多个消费者的情况处理文件数据
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testReadFile3() throws IOException, InterruptedException {
        InputStream inputStream = getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        ConsumerPool pool = new ConsumerPool();
        ConsumerPool.ConsumerThread consumerThread = pool.getConsumerThread();
        String line;
        while ((line = reader.readLine()) != null) {
            consumerThread.put(line);
            if (line.startsWith("\\.")) {
                consumerThread = pool.getConsumerThread();
            }
        }
    }

    /**
     * Disruptor模型，一个生产者多个消费者的情况处理文件数据
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testReadFile4() throws IOException, InterruptedException {
        InputStream inputStream = getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        DisruptorPool pool = new DisruptorPool();
        DisruptorPool.ConsumerHandler consumerHandler = pool.getConsumerHandler();
        String line;
        while ((line = reader.readLine()) != null) {
            consumerHandler.put(line);
            if (line.startsWith("\\.")) {
                consumerHandler = pool.getConsumerHandler();
            }
        }
    }

    private InputStream getInputStream() throws IOException {
        String fileName = "F:\\dml_only.sql";
        InputStream inputStream = new FileInputStream(fileName);
        if (fileName.endsWith(".gz")) {
            inputStream = new GZIPInputStream(inputStream);
        }
        return inputStream;
    }

}
