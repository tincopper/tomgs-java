package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * JZ22GetKthFromEnd
 * <p>
 * <a href="https://leetcode.cn/problems/lian-biao-zhong-dao-shu-di-kge-jie-dian-lcof/description/">剑指 Offer 22. 链表中倒数第k个节点</a>
 * <p>
 * 输入一个链表，输出该链表中倒数第k个节点。为了符合大多数人的习惯，本题从1开始计数，即链表的尾节点是倒数第1个节点。
 * <p>
 * 例如，一个链表有 6 个节点，从头节点开始，它们的值依次是 1、2、3、4、5、6。这个链表的倒数第 3 个节点是值为 4 的节点。
 * <p>
 * 给定一个链表: 1->2->3->4->5->null, 和 k = 2.
 * <p>
 * 返回链表 4->5.
 *
 * @author tomgs
 * @since 1.0
 */
public class JZ22GetKthFromEnd {

    /**
     * 第一个指针先偏移k个位置，第二个指针才开始执行
     * 然后两个指针同时往后移动，第一个指针到链表尾部，第一个指针就是倒数第k个位置
     */
    public ListNode getKthFromEnd(ListNode head, int k) {
        // p1，p2分别为指向head的指针
        ListNode p1 = head, p2 = head;
        int index = 0;
        while (p2 != null) {
            if (index == k) {
                p1 = p1.next;
                p2 = p2.next;
            } else {
                p2 = p2.next;
                index++;
            }
        }

        return p1;
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
        final ListNode kthFromEnd = getKthFromEnd2(listNode, 2);
        System.out.println(kthFromEnd.toPrettyString());
    }

}
