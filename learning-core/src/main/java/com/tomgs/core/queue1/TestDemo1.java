package com.tomgs.core.queue1;

/**
 * @author tangzhongyuan
 * @create 2019-04-13 14:55
 **/
public class TestDemo1 extends AbstractTask {

    private int id;
    private int count;

    @Override
    public ITask runTask() {
        System.out.println("====>" + getId());

        count++;
        if (count >= 4) {
            return null;
        }
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static void main(String[] args) {
        ExecutorHandler instance = ExecutorHandler.getInstance();
        TestDemo1 demo1 = new TestDemo1();
        demo1.setId(1);
        demo1.setPriority(Priority.HIGH);

        TestDemo1 demo2 = new TestDemo1();
        demo2.setId(2);
        demo2.setPriority(Priority.HIGH);

        TestDemo1 demo3 = new TestDemo1();
        demo3.setId(3);
        demo3.setPriority(Priority.HIGH);
        demo3.setSeq(10);

        instance.addTask(demo1);
        instance.addTask(demo2);
        instance.addTask(demo3);
    }
}
