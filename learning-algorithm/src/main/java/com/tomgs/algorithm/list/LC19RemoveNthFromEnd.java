package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC19RemoveNthFromEnd
 * <p>
 * <a href="https://leetcode.cn/problems/remove-nth-node-from-end-of-list/">19. 删除链表的倒数第 N 个结点</a>
 * <p>
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 * <p>
 * 输入：head = [1,2,3,4,5], n = 2
 * 输出：[1,2,3,5]
 *
 * @author tomgs
 * @since 1.0
 */
public class LC19RemoveNthFromEnd {

    // 进阶：你能尝试使用一趟扫描实现吗？
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // 首先找到倒数第n个节点，然后将节点删除
        ListNode p1 = head, p2 = head;
        ListNode result = new ListNode(-1), p = result;
        // p1先走n步，因为n从1开始
        for (int i = 0; i < n; i++) {
            p1 = p1.next;
        }

        while (p1 != null) {
            p1 = p1.next;

            p.next = p2;
            p = p.next;
            // p 在 p2的前面一位
            p2 = p2.next;
        }

        if (p2 != null) {
            p.next = p2.next;
        }

        return result.next;
    }

    public ListNode removeNthFromEnd2(ListNode head, int n) {
        // 首先找到倒数第n个节点，然后将节点删除
        // 先定义一个头节点，用于遍历结果
        ListNode result = new ListNode(-1);
        // 指向链表
        result.next = head;

        // 找到删除前一个节点
        final ListNode node = getKthFromEnd2(result, n + 1);
        node.next = node.next.next;

        return result.next;
    }

    public ListNode getKthFromEnd2(ListNode head, int k) {
        // p1，p2分别为指向head的指针
        ListNode p1 = head, p2 = head;
        // 快指针p2先走k步
        for (int i = 0; i < k; i++) {
            p2 = p2.next;
        }

        // 然后p1，p2指针一起前行
        while (p2 != null) {
            p1 = p1.next;
            p2 = p2.next;
        }

        return p1;
    }

    @Test
    public void test() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5);
        final ListNode result = removeNthFromEnd(listNode, 2);
        System.out.println(result.toPrettyString());
    }

    @Test
    public void test2() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5);
        final ListNode result = removeNthFromEnd2(listNode, 2);
        System.out.println(result.toPrettyString());
    }

}
