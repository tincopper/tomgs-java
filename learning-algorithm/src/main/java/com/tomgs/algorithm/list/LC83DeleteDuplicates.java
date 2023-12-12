package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC83DeleteDuplicates
 * <p>
 * <a href="https://leetcode.cn/problems/remove-duplicates-from-sorted-list/">83. 删除排序链表中的重复元素</a>
 * <p>
 * 给定一个已排序的链表的头 head ， 删除所有重复的元素，使每个元素只出现一次 。返回 已排序的链表 。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC83DeleteDuplicates {

    public ListNode deleteDuplicates(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null) {
            if (slow.val != fast.val) {
                //slow = slow.next;
                //slow.val = fast.val;
                // 将slow的下一个节点指向fast
                slow.next = fast;
                slow = slow.next;
            }
            fast = fast.next;
        }
        if (slow != null) {
            slow.next = null;
        }
        return head;
    }

    @Test
    public void test() {
        ListNode listNode = ListNode.createListNode(1, 1, 2, 3, 3, 4);
        final ListNode result = deleteDuplicates(listNode);
        System.out.println(result.toPrettyString());
    }

}
