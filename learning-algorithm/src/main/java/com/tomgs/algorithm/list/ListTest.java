package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * @author tangzhongyuan
 * @since 2019-07-18 13:55
 **/
public class ListTest {

    /**
     * 反转链表
     *  1->2->3->4->5->NULL 转换为 5->4->3->2->1->NULL
     */
    @Test
    public void reverseListTest() {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        ListNode node5 = new ListNode(5);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        System.out.println(node1);

        ListNode listNode = reverseListNode(node1);
        System.out.println(listNode);

        ListNode listNode1 = reverseListNode2(listNode);
        System.out.println(listNode1);
    }

    /**
     * 时间复杂度O(n)
     * 空间复杂度O(1)
     * @param curNode
     * @return
     */
    private ListNode reverseListNode(ListNode curNode) {
        if (curNode == null || curNode.next == null) {
            return null;
        }
        ListNode tmpNode = null;
        ListNode nextNode = null;
        while (curNode != null) {
            tmpNode = curNode.next; // tmpNode = 2 -> 3 -> 4 -> 5 -> null
            curNode.next = nextNode; //curNode = 1 -> null
            nextNode = curNode; //nextNode = 1 -> null
            curNode = tmpNode; // curNode = 2 -> 3 -> 4 -> 5 -> null
        }
        return nextNode;
    }

    /**
     * 时间复杂度和空间复杂度都是O(n)
     * @param curNode
     * @return
     */
    private ListNode reverseListNode2(ListNode curNode) {
        if (curNode == null || curNode.next == null) {
            return curNode;
        }
        ListNode temp = curNode.next;
        ListNode newHead = reverseListNode2(curNode.next);
        temp.next = curNode;
        curNode.next = null;
        return newHead;
    }
}
