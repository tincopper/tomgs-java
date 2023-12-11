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

    public ListNode mergeTwoLists2(ListNode list1, ListNode list2) {
        // 输入：l1 = [1,2,4], l2 = [1,3,4]
        // 输出：[1,1,2,3,4,4]

        // 定义一个链表头为head，p用于遍历链表的指针
        ListNode head = new ListNode(-1), p = head;
        // p1, p2 分别为用于遍历链表list1，list2的指针
        ListNode p1 = list1, p2 = list2;

        while (p1 != null && p2 != null) {
            // 比较两个链表的对应节点的大小
            if (p1.val > p2.val) {
                p.next = p2;
                p2 = p2.next;
            } else {
                p.next = p1;
                p1 = p1.next;
            }
            p = p.next;
        }

        if (p1 != null) {
            p.next = p1;
        }

        if (p2 != null) {
            p.next = p2;
        }

        return head.next;
    }

    public ListNode mergeTwoLists3(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(-1); // 定义一个虚拟链表
        ListNode p = dummy; // 用于指向dummy链表的指针
        ListNode p1 = list1; // 遍历list1节点指针
        ListNode p2 = list2; // 遍历list2节点指针
        // p1不为空，且p2不为空，两个指针同步进行
        while (p1 != null && p2 != null) {
            if (p1.val < p2.val) {
                // 赋值给dummy链表
                p.next = p1;
                p = p.next;
                // p1指针往前移
                p1 = p1.next;
            } else {
                p.next = p2;
                p = p.next;
                // p2指针往前移
                p2 = p2.next;
            }
        }
        // 判断是否两个链表有剩余
        if (p1 != null) {
            p.next = p1;
        }
        if (p2 != null) {
            p.next = p2;
        }

        // 返回头节点的下个开始的链表
        return dummy.next;
    }

    @Test
    public void test() {
        ListNode list1 = ListNode.createListNode(1, 2, 4);
        ListNode list2 = ListNode.createListNode(1, 3, 4);
        ListNode listNode = mergeTwoLists2(list1, list2);

        System.out.println(listNode.toPrettyString());
    }

    @Test
    public void test2() {
        ListNode list1 = ListNode.createListNode(1, 2, 4);
        ListNode list2 = ListNode.createListNode(1, 3, 4);
        ListNode listNode = mergeTwoLists3(list1, list2);

        System.out.println(listNode.toPrettyString());
    }

}
