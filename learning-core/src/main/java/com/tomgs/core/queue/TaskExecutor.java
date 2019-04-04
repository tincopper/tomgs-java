package com.tomgs.core.queue;

import java.util.concurrent.BlockingQueue;

/**
 * task 执行器
 *
 * @author tangzhongyuan
 * @create 2019-03-14 9:50
 **/
public class TaskExecutor extends Thread {

    private BlockingQueue<ITask> taskQueue;
    private boolean isRuning = true;

    public TaskExecutor(BlockingQueue<ITask> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (isRuning) {
            try {
                ITask task = taskQueue.take();
                consumer(task);
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
            task.run();
        }
    }
}
