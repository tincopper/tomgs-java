package com.tomgs.algorithm.tree;

import org.junit.Test;

/**
 * LC226InvertTree
 * <p>
 * <a href="https://leetcode.cn/problems/invert-binary-tree/">226. 翻转二叉树</a>
 * 给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC226InvertTree {

    /**
     * 递归方式
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }

        // 反转左子树
        TreeNode left = invertTree(root.left);
        // 反转右子树
        TreeNode right = invertTree(root.right);
        // 反转根节点左右子树
        root.right = left;
        root.left = right;
        return root;
    }

    /**
     * 迭代方式
     */
    public TreeNode invertTree2(TreeNode root) {
        // 迭代
        traverse(root);

        return root;
    }

    private void traverse(TreeNode node) {
        if (node == null) {
            return;
        }

        // 前序遍历
        // 每一个节点需要做的事就是交换它的左右子节点
        TreeNode tmp = node.left;
        node.left = node.right;
        node.right = tmp;

        traverse(node.left);
        traverse(node.right);
    }

    @Test
    public void test() {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        root.right = new TreeNode(7);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(9);

        System.out.println(root);

        System.out.println(invertTree(root));
        System.out.println(invertTree2(root));
    }

}
