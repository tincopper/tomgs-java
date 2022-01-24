package com.tomgs.algorithm.list;

import org.junit.Test;

/**
 * LC2：两数相加
 * <p>
 * 给你两个非空链表，表示两个非负的整数。
 * 它们按照逆序的方式存储，并且每一个节点只能存储一位数字。
 * <p>
 * 将两个数相加，并以相同形式返回一个表示和的链表。
 * 除了数字0以外，这两个数都不会以0开头。
 * <p>
 * 示例：
 * l1 = [2, 4, 3], l2 = [5, 6, 4]
 * output = [7, 0, 8]
 * ==> 342 + 465 = 807
 * <p>
 * l1 = [9,9,9,9,9,9], l2 = [9,9,9,9]
 * output = [8,9,9,9,0,1]
 *
 * @author tomgs
 * @since 2022/1/24
 */
public class LC2AddTwoNumbers {

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode headNode = null;
        ListNode tailNode = null;
        // 进位
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int v1 = l1 == null ? 0 : l1.value;
            int v2 = l2 == null ? 0 : l2.value;

            int v = v1 + v2 + carry;
            int quotient = v / 10;
            int mod = v % 10;

            carry = quotient;
            if (headNode == null) {
                headNode = new ListNode(mod);
            } else {
                ListNode node = new ListNode(mod);
                if (tailNode == null) {
                    headNode.setNext(node);
                } else {
                    tailNode.setNext(node);
                }
                tailNode = node;
            }
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        return headNode;
    }

    /**
     * 官方解法
     */
    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode head = null, tail = null;
        // 进位值
        int carry = 0;

        while (l1 != null || l2 != null) {
            int v1 = l1 == null ? 0 : l1.value;
            int v2 = l2 == null ? 0 : l2.value;
            int sumVal = v1 + v2 + carry;
            carry = sumVal / 10;
            if (head == null) { // first node
                head = tail = new ListNode(sumVal % 10);
            } else {
                tail.next = new ListNode(sumVal % 10);
                tail = tail.next;
            }
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        // 遍历完之后看一下有没有进位
        if (carry > 0) {
            tail.setNext(new ListNode(carry));
        }

        return head;
    }

    @Test
    public void test() {
        ListNode l1 = ListNode.createListNode(2, 4, 3);
        System.out.println(l1.toPrettyString());

        ListNode l2 = ListNode.createListNode(5, 6, 4);
        System.out.println(l2.toPrettyString());

        printResult(l1, l2);
    }

    @Test
    public void test1() {
        ListNode l1 = ListNode.createListNode(0);
        System.out.println(l1.toPrettyString());

        ListNode l2 = ListNode.createListNode(0);
        System.out.println(l2.toPrettyString());

        printResult(l1, l2);
    }

    @Test
    public void test2() {
        ListNode l1 = ListNode.createListNode(9, 9, 9, 9, 9, 9);
        System.out.println(l1.toPrettyString());

        ListNode l2 = ListNode.createListNode(9, 9, 9, 9);
        System.out.println(l2.toPrettyString());

        printResult(l1, l2);
    }

    private void printResult(ListNode l1, ListNode l2) {
        ListNode result = addTwoNumbers(l1, l2);
        ListNode result2 = addTwoNumbers2(l1, l2);
        System.out.println(result.toPrettyString());
        System.out.println(result2.toPrettyString());
    }

}
