package com.tomgs.core.queue;

/**
 *  
 *
 * @author tomgs
 * @version 2019/4/2 1.0 
 */
public class TaskDemo implements Runnable,Comparable<TaskDemo> {

    private String name;
    private Priority priority;

    public TaskDemo(String name, Priority priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public void run() {
        System.out.println(name + "--" + Thread.currentThread().getName() + "--" + this.priority);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(TaskDemo task) {
        Priority otherPriority = task.getPriority();
        Priority thisPriority = this.getPriority();

        return otherPriority.ordinal() < thisPriority.ordinal() ? -1 : 1;
    }
}
