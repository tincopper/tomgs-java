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

    public ListNode reverseList3(ListNode head) {
        ListNode result = new ListNode(-1);
        ListNode cur = head;

        while (cur != null) {
            // 获取之前的链表
            ListNode pre = result.next;
            // 将当前节点指向头节点
            result.next = cur;
            // 设置下一个节点指针
            cur = cur.next;
            // 反转节点
            result.next.next = pre;
        }

        return result.next;
    }

    /**
     * 递归方式
     * 1、找到递归的结束点
     * 2、从后往前执行
      */
    public ListNode reverse(ListNode head) {
        // 遍历到最后一个节点即退出递归
        if (head == null || head.next == null) {
            return head;
        }
        // 否则继续递归，直到最后一个节点，记住递归的时候是不会往下执行的，所以往下执行了这个节点就是最后一个节点
        ListNode last = reverse(head.next);

        // 1->2->3
        // head = 2
        // last = 3
        // 从后往前进行链表反转
        head.next.next = head; // 翻转
        head.next = null;      // 将尾节点置空，3 -> 2 -> 3，所以需要将2的下一个节点设置为空，否则死循环

        return last;
    }

    /**
     * 反转链表前 N 个节点
     *  将链表的前 n 个节点反转（n <= 链表长度）
     *  1, 2, 3, 4, 5；n = 3
     *  3, 2, 1, 4, 5
     */
    // 后继节点
    ListNode successor = null;
    public ListNode reverseN(ListNode head, int n) {
        // 递归方式
        // 找到递归结束点
        // 此时head节点为3
        if (n == 1) {
            // 记录后继节点
            successor = head.next;
            // 反转
            head.next = null;
            return head;
        }

        // 递归
        ListNode last = reverseN(head.next, n - 1);
        // 进行链表反转
        head.next.next =  head;
        head.next = successor;

        return last;
    }

    /**
     * 反转区间链表
     * 给一个索引区间 [m, n]（索引从 1 开始），仅仅反转区间中的链表元素。
     * 1, 2, 3, 4, 5；m = 2, n = 4
     * <p>
     * 1、递归：
     * 2、先迭代到m，再使用reverseN方法
     *
     */
    ListNode reverseBetween(ListNode head, int m, int n) {
        // 1、找到递归结束点
        if (m == 1) {
            return reverseN(head, n);
        }

        /**
         * 如果 m != 1 怎么办？如果我们把 head 的索引视为 1，那么我们是想从第 m 个元素开始反转对吧；
         * 如果把 head.next 的索引视为 1 呢？那么相对于 head.next，反转的区间应该是从第 m - 1 个元素开始的；
         * 那么对于 head.next.next 呢……
         */
        head.next = reverseBetween(head.next, m - 1, n - 1);
        return head;
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

    @Test
    public void test3() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5);
        final ListNode reversed = reverse(listNode);
        System.out.println(reversed.toPrettyString());
    }

    @Test
    public void test4() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5);
        final ListNode reversed = reverseN(listNode, 3);
        System.out.println(reversed.toPrettyString());
    }

    @Test
    public void test5() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5);
        final ListNode reversed = reverseBetween(listNode, 2, 4);
        System.out.println(reversed.toPrettyString());
    }

    @Test
    public void test6() {
        ListNode listNode = ListNode.createListNode(1, 2, 3, 4, 5);
        final ListNode reversed = reverseList3(listNode);
        System.out.println(reversed.toPrettyString());
    }
}
