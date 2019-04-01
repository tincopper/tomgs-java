package com.tomgs.core.queue;

/**
 * @author tangzhongyuan
 * @create 2019-03-14 9:44
 **/
public interface ITask extends Comparable<ITask> {

    void run();

    void setPriority(Priority priority);

    Priority getPriority();

    void setSeq(int seq);

    int getSeq();
}
