package com.tomgs.algorithm.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * LC116116PopulatingNextRightNode
 * <p>
 * <a href="https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/">116. 填充每个节点的下一个右侧节点指针</a>
 * 给定一个 完美二叉树 ，其所有叶子节点都在同一层，每个父节点都有两个子节点。二叉树定义如下：
 * <p>
 * struct Node {
 *   int val;
 *   Node *left;
 *   Node *right;
 *   Node *next;
 * }
 * 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL。
 * <p>
 * 初始状态下，所有 next 指针都被设置为 NULL。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC116PopulatingNextRightNode {

    // 层序遍历
    public Node connect(Node root) {
        if (root == null) {
            return null;
        }
        List<Node> curNodes = new ArrayList<>();
        curNodes.add(root);
        levelTraverse(curNodes);

        return root;
    }

    public void levelTraverse(List<Node> nodes) {
        if (nodes.isEmpty()) {
            return;
        }

        // 交换
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (i + 1 < nodes.size()) {
                node.next = nodes.get(i + 1);
            }
        }

        // 遍历
        List<Node> nextNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node.left != null) {
                nextNodes.add(node.left);
            }
            if (node.right != null) {
                nextNodes.add(node.right);
            }
        }

        levelTraverse(nextNodes);
    }

    // 将二叉树转为三叉树的方式
    public Node connect2(Node root) {
        if (root == null) {
            return null;
        }
        // 遍历三叉树
        traverse(root.left, root.right);

        return root;
    }

    public void  traverse(Node node1, Node node2) {
        if (node1 == null || node2 == null) {
            return;
        }
        /** 前序位置 **/
        // 将传入的两个节点串起来
        node1.next = node2;
        // 连接相同父节点的两个子节点
        traverse(node1.left, node1.right);
        traverse(node2.left, node2.right);
        // 连接跨越父节点的两个子节点
        traverse(node1.right, node2.left);
    }

    @Test
    public void test() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.left.left = new Node(4);
        root.left.right = new Node(5);

        root.right = new Node(3);
        root.right.left = new Node(6);
        root.right.right = new Node(7);

        System.out.println(root);

        System.out.println(connect(root));
        System.out.println(connect2(root));
    }

}
