package com.tomgs.algorithm.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.cn/problems/binary-tree-preorder-traversal/description/">144. 二叉树的前序遍历</a>
 * <p>
 * 给你二叉树的根节点 root ，返回它节点值的 前序 遍历。
 *
 * @author tomgs
 * @version 1.0
 */
public class LC144PreorderTraversal {

    List<Integer> res = new ArrayList<>();

    // 遍历思维
    public List<Integer> preorderTraversal(TreeNode root) {
        traverse(root);
        return res;
    }

    public void traverse(TreeNode root) {
        if (root == null) {
            return;
        }

        // 前序遍历
        res.add(root.val);
        traverse(root.left);
        // 中序遍历
        traverse(root.right);
        // 后序遍历
    }

    // 分解问题思维
    // 前序遍历 = 根节点 + 左子树节点 + 右子树节点
    public List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        // 根节点
        res.add(root.val);
        // 左子树
        res.addAll(preorderTraversal2(root.left));
        // 右子树
        res.addAll(preorderTraversal2(root.right));

        return res;
    }

    @Test
    public void test() {
        TreeNode right = new TreeNode(2);
        right.left = new TreeNode(3);
        TreeNode root = new TreeNode(1);
        root.left = null;
        root.right = right;

        System.out.println(root);

        List<Integer> result = preorderTraversal(root);
        System.out.println(result);
    }

    @Test
    public void test2() {
        TreeNode right = new TreeNode(2);
        right.left = new TreeNode(3);
        TreeNode root = new TreeNode(1);
        root.left = null;
        root.right = right;

        System.out.println(root);

        List<Integer> result = preorderTraversal2(root);
        System.out.println(result);
    }
}
