package com.tomgs.core.queue1;

/**
 *
 *  任务执行状态检查
 *
 * @author tangzhongyuan
 * @create 2019-03-15 12:08
 **/
public class ExecutorHandler {

    private static volatile ExecutorHandler instance = null;
    private static PriorityTaskQueue queue;

    private ExecutorHandler() {
        queue = new PriorityTaskQueue(1);
        queue.start();
    }

    public static synchronized ExecutorHandler getInstance() {
        if (instance == null) {
            instance = new ExecutorHandler();
        }
        return instance;
    }

    public int addTask(ITask task) {
        return queue.addTask(task);
    }

    public int queueSize() {
        return queue.size();
    }

}
