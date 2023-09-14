package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC24SwapPairs
 * <p>
 * <a href="https://leetcode.cn/problems/swap-nodes-in-pairs/">24. 两两交换链表中的节点</a>
 * 给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。
 * 输入：head = [1,2,3,4]
 * 输出：[2,1,4,3]
 *
 * @author tomgs
 * @since 1.0
 */
public class LC24SwapPairs {

    // 多指针法
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(-1), p = dummy;
        p.next = head;

        while (p.next != null && p.next.next != null) {
            ListNode first = p.next;
            ListNode second = p.next.next;
            first.next = second.next;
            second.next = first;
            p.next = second;
            p = first;
        }

        return dummy.next;
    }

    // 递归方法
    // 把链表当成二叉树
    public ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode first = head;
        ListNode second = head.next;
        ListNode third = head.next.next;
        // 反转
        second.next = first;
        // 利用递归定义，将剩下的链表节点两两翻转，接到后面
        first.next = swapPairs(third);
        // 现在整个链表都成功翻转了，返回新的头结点
        return second;
    }

    @Test
    public void test() {
        ListNode list = ListNode.createListNode(1, 2, 3, 4);
        final ListNode result = swapPairs(list);
        System.out.println(result.toPrettyString());
    }

    @Test
    public void test2() {
        ListNode list = ListNode.createListNode(1, 2, 3, 4);
        final ListNode result = swapPairs2(list);
        System.out.println(result.toPrettyString());
    }
}
