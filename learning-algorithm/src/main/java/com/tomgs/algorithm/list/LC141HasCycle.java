package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC141HasCycle
 * <p>
 * <a href="https://leetcode.cn/problems/linked-list-cycle/">141. 环形链表</a>
 * <p>
 * 给你一个链表的头节点 head ，判断链表中是否有环。
 * <p>
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。 为了表示给定链表中的环，评测系统内部使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。注意：pos 不作为参数进行传递 。仅仅是为了标识链表的实际情况。
 * <p>
 * 如果链表中存在环 ，则返回 true 。 否则，返回 false 。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC141HasCycle {

    public boolean hasCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (fast == slow) {
                return true;
            }
        }

        return false;
    }

    @Test
    public void test() {
        ListNode listNode = ListNode.createListNode(3);
        ListNode listNode1 = ListNode.createListNode(2);
        ListNode listNode2 = ListNode.createListNode(0);
        ListNode listNode3 = ListNode.createListNode(4);

        listNode.next = listNode1;
        listNode1.next = listNode2;
        listNode2.next = listNode3;
        listNode3.next = listNode1;
        final boolean b = hasCycle(listNode);
        System.out.println(b);
    }
}
