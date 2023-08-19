package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC876MiddleNode
 * <p>
 * <a href="https://leetcode.cn/problems/middle-of-the-linked-list/">876. 链表的中间结点</a>
 * 给你单链表的头结点 head ，请你找出并返回链表的中间结点。
 * <p>
 * 如果有两个中间结点，则返回第二个中间结点。
 * <p>
 * 输入：head = [1,2,3,4,5]
 * 输出：[3,4,5]
 * 解释：链表只有一个中间结点，值为 3 。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC876MiddleNode {

    // 自己写的
    public ListNode middleNode(ListNode head) {
        ListNode p1 = head, p2 = head;
        while (p2 != null) {
            if (p2.next != null) {
                p2 = p2.next.next;
            } else {
                break;
            }
            p1 = p1.next;
        }
        return p1;
    }

    // 别人写的
    public ListNode middleNode2(ListNode head) {
        ListNode slow = head, fast = head;
        // 快指针走到末尾时停止
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    @Test
    public void test() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5, 6);
        final ListNode result = middleNode(listNode);
        System.out.println(result.toPrettyString());
    }

    @Test
    public void test2() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5, 6);
        final ListNode result = middleNode2(listNode);
        System.out.println(result.toPrettyString());
    }

}
