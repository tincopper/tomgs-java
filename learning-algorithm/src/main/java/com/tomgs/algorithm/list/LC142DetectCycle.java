package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC142DetectCycle
 * <p>
 * <a href="https://leetcode.cn/problems/linked-list-cycle-ii/">142. 环形链表 II</a>
 * <p>
 * 给定一个链表的头节点  head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 *
 * @author tomgs
 * @version 1.0
 */
public class LC142DetectCycle {

    public ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;

        boolean hasCycle = false;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                hasCycle = true;
                break;
            }
        }

        if (!hasCycle) {
            return null;
        }

        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        return slow;
    }

    @Test
    public void test() {
        ListNode listNode = new ListNode(3);
        ListNode listNode1 = new ListNode(2);
        ListNode listNode2 = new ListNode(0);
        ListNode listNode3 = new ListNode(4);

        listNode.next = listNode1;
        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode3.next = listNode1;

        ListNode result = detectCycle(listNode);
        System.out.println(result.val);
    }

}
