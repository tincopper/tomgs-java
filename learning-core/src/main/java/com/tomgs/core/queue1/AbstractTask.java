package com.tomgs.core.queue1;

/**
 * @author tangzhongyuan
 * @create 2019-03-14 12:00
 **/
public abstract class AbstractTask implements ITask {

    private Priority priority = Priority.DEFAULT;
    private int seq = 0;

    @Override
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public Priority getPriority() {
        return this.priority;
    }

    @Override
    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public int getSeq() {
        return this.seq;
    }

    @Override
    public ITask run() {
        seq++;
        return runTask();
    }

    protected abstract ITask runTask();

    @Override
    public int compareTo(ITask task) {
        Priority otherPriority = task.getPriority();
        Priority thisPriority = this.getPriority();

        return thisPriority == otherPriority ? this.getSeq() - task.getSeq()
                : otherPriority.ordinal() - thisPriority.ordinal();
    }
}
