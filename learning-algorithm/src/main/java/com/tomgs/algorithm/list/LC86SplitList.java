package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC86SplitList
 * <p>
 * <a href="https://leetcode.cn/problems/partition-list/">86. 分隔链表</a>
 * 给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。
 * <p>
 * 你应当 保留 两个分区中每个节点的初始相对位置。
 * <p>
 * 输入：head = [1,4,3,2,5,2], x = 3
 * 输出：[1,2,2,4,3,5]
 * <p>
 * 输入：head = [2,1], x = 2
 * 输出：[1,2]
 *
 * @author tomgs
 * @version 1.0
 */
public class LC86SplitList {

    public ListNode partition(ListNode head, int x) {
        // 存放小于x的值
        ListNode h1 = new ListNode(-1), p1 = h1;
        // 存放大于x的值
        ListNode h2 = new ListNode(-1), p2 = h2;

        ListNode p = head;
        while (p != null) {
            if (p.val < x) {
                p1.next = new ListNode(p.val);
                p1 = p1.next;
            } else if (p.val >= x) {
                p2.next = new ListNode(p.val);
                p2 = p2.next;
            }
            p = p.next;
        }

        p1.next = h2.next;

        return h1.next;
    }

    @Test
    public void test() {
        ListNode head = ListNode.createListNode(1, 4, 3, 2, 5, 2);
        ListNode result = partition(head, 3);

        System.out.println(result.toPrettyString());
    }

    @Test
    public void test2() {
        ListNode head = ListNode.createListNode(2, 1);
        ListNode result = partition(head, 2);

        System.out.println(result.toPrettyString());
    }

}
