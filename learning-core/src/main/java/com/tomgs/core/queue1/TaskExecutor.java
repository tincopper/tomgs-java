package com.tomgs.core.queue1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * task 执行器
 *
 * @author tangzhongyuan
 * @create 2019-03-14 9:50
 **/
public class TaskExecutor extends Thread {

    private final BlockingQueue<ITask> taskQueue;
    private volatile boolean isRuning = true;

    public TaskExecutor(BlockingQueue<ITask> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (isRuning) {
            try {
                ITask task = taskQueue.take();
                consumer(task);
                //延迟1s再去消费
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();

                if (isRuning) {
                    continue;
                }
                interrupt();
                break;
            }
        }
    }

    public void shutdown() {
        isRuning = false;
        interrupt();
    }

    public void consumer(ITask task) {
        if (task != null) {
            ITask run = task.run();
            if (run != null) {
                taskQueue.offer(run);
            }
        }
    }
}
