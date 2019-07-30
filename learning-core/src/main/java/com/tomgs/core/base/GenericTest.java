package com.tomgs.core.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author tangzhongyuan
 * @since 2019-07-15 10:56
 **/
public class GenericTest {

    public static void main(String[] args) {
        final int i = 2 ^ 3 | 3 & 1;
        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a");

        final String s = Demo1.get(arrayList);
        System.out.println(s);

        final ArrayList<String> list = Demo1.getList();

        final ArrayList<Demo1.Inner<String, String>> list1 = Demo1.Inner.getList();
        //Demo1.Inner.<String, Long>.comparingByValue();
    }

}

interface Demo1<K, V> {

    K getKey();

    V getValue();

    public static <V> String get(List<V> test) {
        return test.toString();
    }

    public static <V> ArrayList<V> getList() {
        return new ArrayList<>();
    }

    interface Inner<K, V> {

        K getKey();

        V getValue();

        static <K, V> ArrayList<Demo1.Inner<K, V>> getList() {
            return new ArrayList<>();
        }

        static <K, V extends Comparable<? super V>> Comparator<Demo1.Inner<K, V>> comparingByValue() {
            return (Comparator<Demo1.Inner<K, V>> & Serializable)
                    (c1, c2) -> c1.getValue().compareTo(c2.getValue());
        }
    }
}
