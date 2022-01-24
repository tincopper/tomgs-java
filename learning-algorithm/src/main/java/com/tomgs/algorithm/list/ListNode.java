package com.tomgs.algorithm.list;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangzhongyuan
 * @since 2019-07-18 13:54
 **/
public class ListNode {
    public int value;

    public ListNode next;

    public ListNode(int value) {
        this.value = value;
    }

    public static ListNode createListNode(int ...elements) {
        List<ListNode> list = new ArrayList<>();
        for (int element : elements) {
            list.add(new ListNode(element));
        }
        for (int i = 0; i < list.size() - 1; i++) {
            ListNode listNode = list.get(i);
            listNode.setNext(list.get(i + 1));
        }
        return list.get(0);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", next=" + next +
                '}';
    }

    public String toPrettyString() {
        StringBuilder sb = new StringBuilder();
        ListNode nextNode = this;
        while (nextNode != null) {
            sb.append(nextNode.value).append(", ");
            nextNode = nextNode.next;
        }
        sb.append("NULL");
        return sb.toString();
    }

}
