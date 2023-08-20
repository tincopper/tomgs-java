package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * <a href="https://leetcode.cn/problems/intersection-of-two-linked-lists/">160. 相交链表</a>
 * <p>
 * 给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null 。
 *
 * @author tomgs
 * @version 1.0
 */
public class LC160GetIntersectionNode {

    /**
     * 1、通过hashSet找到相交的点
     * 2、通过双指针找到相交的点，将两条链表进行拼接再比较
     * 3、如果把两条链表首尾相连，那么「寻找两条链表的交点」的问题转换成了前面讲的「寻找环起点」的问题（这种不好搞）
     * 4、先求链表长度，然后找到长度差n，长的先走n，然后双指针一起走，有相等的节点即为交叉的节点
     */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // 第二种解法
        ListNode p1 = headA, p2 = headB;
        while (p1 != p2) {
            // 如果节点headA为空拼接headB
            if (p1 == null) {
                p1 = headB;
            } else {
                p1 = p1.next;
            }

            if (p2 == null) {
                p2 = headA;
            } else {
                p2 = p2.next;
            }
        }

        return p1;
    }

    @Test
    public void test() {
        ListNode head1 = ListNode.createListNode(1, 8);

        ListNode headA = ListNode.createListNode(4);
        headA.next = head1;

        ListNode headB = ListNode.createListNode(5, 6);
        headB.next.next = head1;

        ListNode result = getIntersectionNode(headA, headB);
        System.out.println(result.toPrettyString());

    }

}
