package com.tomgs.algorithm.list;

import org.junit.Test;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * LC23MergeKLists
 * <p>
 * <a href="https://leetcode.cn/problems/merge-k-sorted-lists/">23. 合并 K 个升序链表</a>
 * 给你一个链表数组，每个链表都已经按升序排列。
 * <p>
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 * <p>
 * 输入：lists = [[1,4,5],[1,3,4],[2,6]]
 * 输出：[1,1,2,3,4,4,5,6]
 * 解释：链表数组如下：
 * [
 * 1->4->5,
 * 1->3->4,
 * 2->6
 * ]
 * 将它们合并到一个有序链表中得到。
 * 1->1->2->3->4->4->5->6
 *
 * @author tomgs
 * @since 1.0
 */
public class LC23MergeKLists {

    public ListNode mergeKLists(ListNode[] lists) {
        // 方法1、先两两合并，然后再最终合并，转成为合并两个链表的解法
        // 方法2、通过最小堆，每次从堆里获取最小的元素放入到合并的链表当中即可
        if (lists.length == 0) {
            return null;
        }
        ListNode head = new ListNode(-1), p = head;
        PriorityQueue<ListNode> queue = new PriorityQueue<>((o1, o2) -> o1.val - o2.val);

        for (ListNode listNode : lists) {
            if (listNode != null) {
                queue.add(listNode);
            }
        }

        while (!queue.isEmpty()) {
            // 最小的头节点
            final ListNode node = queue.poll();
            p.next = node;
            if (node.next != null) {
                queue.add(node.next);
            }
            p = p.next;
        }

        return head.next;
    }

    public ListNode mergeKLists2(ListNode[] lists) {
        // 方法1、先两两合并，然后再最终合并，转成为合并两个链表的解法
        if (lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }
        ListNode listNode = mergeTwoLists(lists[0], lists[1]);
        for (int i = 2; i < lists.length; i++) {
            listNode = mergeTwoLists(listNode, lists[i]);
        }

        return listNode;
    }

    // 分而治之
    public ListNode mergeKLists3(ListNode[] lists) {
        return merge(lists, 0, lists.length - 1);
    }

    private ListNode merge(ListNode[] lists, int l, int r) {
        if (l == r) {
            return lists[l];
        }
        if (l > r) {
            return null;
        }
        int mid = (l + r) >> 1;
        return mergeTwoLists(merge(lists, l, mid), merge(lists, mid + 1, r));
    }

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
        ListNode[] listNodes = {
                ListNode.createListNode(1, 4, 5),
                ListNode.createListNode(1, 3, 4),
                ListNode.createListNode(2, 6)
        };
        final ListNode listNode = mergeKLists(listNodes);
        System.out.println(listNode.toPrettyString());
    }

    @Test
    public void test2() {
        ListNode[] listNodes = {
                ListNode.createListNode(1, 4, 5),
                ListNode.createListNode(1, 3, 4),
                ListNode.createListNode(2, 6)
        };
        final ListNode listNode = mergeKLists2(listNodes);
        System.out.println(listNode.toPrettyString());
    }

    @Test
    public void testPriorityQueue() {
        ListNode listNode = ListNode.createListNode(1);
        ListNode listNode2 = ListNode.createListNode(2);
        ListNode listNode3 = ListNode.createListNode(3);
        PriorityQueue<ListNode> queue = new PriorityQueue<>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val - o2.val;
            }
        });

        queue.add(listNode);
        queue.add(listNode2);
        queue.add(listNode3);

        final ListNode poll = queue.poll();
        System.out.println(poll);
    }

}
