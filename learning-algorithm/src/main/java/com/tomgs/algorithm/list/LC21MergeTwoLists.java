package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * <a href="https://leetcode.cn/problems/merge-two-sorted-lists/">21. 合并两个有序链表</a>
 * <p>
 * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 * <p>
 *
 * @author tomgs
 * @version 1.0
 */
public class LC21MergeTwoLists {

    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode l = list1, r = list2;
        ListNode head = new ListNode(-1), p = head;

        while (l != null && r != null) {
            if (l.val >= r.val) {
                p.next = r;
                r = r.next;
            } else if (l.val < r.val) {
                p.next = l;
                l = l.next;
            }
            p = p.next;
        }

        if (l != null) {
            p.next = l;
        }

        if (r != null) {
            p.next = r;
        }

        return head.next;
    }

    @Test
    public void test() {
        ListNode list1 = ListNode.createListNode(1, 2, 4);
        ListNode list2 = ListNode.createListNode(1, 3, 4);
        ListNode listNode = mergeTwoLists(list1, list2);

        System.out.println(listNode.toPrettyString());
    }

}
