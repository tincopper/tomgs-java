package com.tomgs.algorithm.tree;

import org.junit.Test;

/**
 * LC104MaxDepth
 * <p>
 * <a href="https://leetcode.cn/problems/maximum-depth-of-binary-tree/description/">104. 二叉树的最大深度</a>
 * <p>
 * 给定一个二叉树 root ，返回其最大深度。
 * <p>
 * 二叉树的 最大深度 是指从根节点到最远叶子节点的最长路径上的节点数。
 * <p>
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：3
 *
 * @author tomgs
 * @since 1.0
 */
public class LC104MaxDepth {

    // 拆解子问题的方式
    // 先获取左子树的最大深度，再获取右子树的最大深度，比较大小 获取最大深度子树 再加 根节点即为最大深度
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDepth = maxDepth(root.left);
        int rightDepth = maxDepth(root.right);
        return Math.max(leftDepth, rightDepth) + 1;
    }

    // 迭代法的方式，通过遍历二叉树记录最大的深度
    // 最大深度
    int maxDepth = 0;
    // 当前深度
    int depth = 0;
    public int maxDepth2(TreeNode root) {
        //遍历
        traverse(root);
        return maxDepth;
    }

    private void traverse(TreeNode root) {
        if (root == null) {
            return;
        }
        // 前序
        depth++;
        // 遍历到子节点为null时进行比较，取最大深度
        if (root.left == null && root.right == null) {
            maxDepth = Math.max(depth, maxDepth);
        }
        traverse(root.left);
        traverse(root.right);
        // 后序
        // 离开一个节点的时候需要对当前深度 -1
        depth--;
    }

    @Test
    public void test() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        System.out.println(root);

        int result = maxDepth(root);
        System.out.println(result);
    }

    @Test
    public void test2() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        System.out.println(root);

        int result = maxDepth2(root);
        System.out.println(result);
    }
}
