package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC206ReverseList
 *
 * @author tomgs
 * @since 1.0
 */
public class LC206ReverseList {

    // 头插法
    public ListNode reverseList(ListNode head) {
        ListNode cur = head;
        ListNode tmp = new ListNode(-1), p = tmp;
        while (cur != null) {
            ListNode h = cur;
            cur = cur.next;
            // 头插法
            h.next = p.next;
            p.next = h;
        }

        return tmp.next;
    }

    // 迭代法
    public ListNode reverseList2(ListNode head) {
        // 将当前节点指向前一个节点
        ListNode cur = head;
        ListNode pre = null;
        while (cur != null) {
            ListNode tmp = cur;
            cur = cur.next;
            tmp.next = pre;
            pre = tmp;
        }

        return pre;
    }

    @Test
    public void test() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5);
        final ListNode reversed = reverseList(listNode);
        System.out.println(reversed.toPrettyString());
    }

    @Test
    public void test2() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5);
        final ListNode reversed = reverseList2(listNode);
        System.out.println(reversed.toPrettyString());
    }

}
