package com.tomgs.core.classloader.simple;

/**
 * 对于静态字段，只有直接定义这个字段的类才会被初始化，
 * 因此通过其子类来引用父类中定义的静态字段，只会触发父类的初始化而不会触发子类的初始化。
 *
 * @author tangzhongyuan
 * @since 2019-07-17 16:57
 **/

public class NotInitialization {
    static {
        i = 0;
        /*
         * <clinit>()方法是由编译器自动收集类中的所有类变量的赋值动作和静态语句块static{}中的语句合并产生的，
         * 编译器收集的顺序是由语句在源文件中出现的顺序所决定的，静态语句块只能访问到定义在静态语句块之前的变量，
         * 定义在它之后的变量，在前面的静态语句块可以赋值，但是不能访问。
         */
        //System.out.println(i);//这句编译器会报错：Cannot reference a field before it is defined（非法向前应用）

    }

    static int i = 1;

    public static void main(String[] args) {
        System.out.println(i);
        System.out.println(SubClass.value);
    }
}

class SSClass {
    static {
        System.out.println("SSClass");
    }
}

class SuperClass extends SSClass {
    static {
        System.out.println("SuperClass init!");
    }

    public static int value = 123;

    public SuperClass() {
        System.out.println("init SuperClass");
    }
}

class SubClass extends SuperClass {
    static {
        System.out.println("SubClass init");
    }

    static int a;

    public SubClass() {
        System.out.println("init SubClass");
    }
}

