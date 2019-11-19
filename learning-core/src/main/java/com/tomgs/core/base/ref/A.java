package com.tomgs.core.base.ref;

import java.util.Objects;

/**
 *  
 *
 * @author tomgs
 * @version 2019/11/4 1.0 
 */
public class A {
    int a = 1;

    public A() {
        System.out.println("A......");
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        A a1 = (A) o;
        return a == a1.a;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a);
    }

    @Override
    public String toString() {
        return this.hashCode() + "@A{" +
                "a=" + a +
                '}';
    }
}
