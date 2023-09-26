package com.tomgs.algorithm.tree;

import org.junit.Test;

/**
 * LC543DiameterOfBinaryTree
 * <p>
 * <a href="https://leetcode.cn/problems/diameter-of-binary-tree/">543. 二叉树的直径</a>
 * <p>
 * 给你一棵二叉树的根节点，返回该树的 直径 。
 * 二叉树的 直径 是指树中任意两个节点之间最长路径的 长度 。这条路径可能经过也可能不经过根节点 root 。
 * 两节点之间路径的 长度 由它们之间边数表示。
 * <p>
 * 输入：root = [1,2,3,4,5]
 * 输出：3
 * 解释：3 ，取路径 [4,2,1,3] 或 [5,2,1,3] 的长度。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC543DiameterOfBinaryTree {

    // 每一条二叉树的「直径」长度，就是一个节点的左右子树的最大深度之和。
    int maxDiameter = 0;

    public int diameterOfBinaryTree(TreeNode root) {
        maxDepth(root);
        return maxDiameter;
    }

    int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int leftDeep = maxDepth(root.left);
        int rightDeep = maxDepth(root.right);
        // 直径 = 左子树直径 + 右子树直径
        int diameter = leftDeep + rightDeep;
        maxDiameter = Math.max(diameter, maxDiameter);
        // 返回树的深度
        return Math.max(leftDeep, rightDeep) + 1;
    }

    /**
     * 一条路径的长度为该路径经过的节点数减一，所以求直径（即求路径长度的最大值）等效于求路径经过节点数的最大值减一。
     * 而任意一条路径均可以被看作由某个节点为起点，从其左儿子和右儿子向下遍历的路径拼接得到。
     */
    // 容易理解版
    int ans;
    public int diameterOfBinaryTree2(TreeNode root) {
        ans = 1;
        depth(root);
        return ans - 1;
    }

    public int depth(TreeNode node) {
        if (node == null) {
            return 0; // 访问到空节点了，返回0
        }
        int L = depth(node.left); // 左儿子为根的子树的深度
        int R = depth(node.right); // 右儿子为根的子树的深度
        ans = Math.max(ans, L + R + 1); // 计算d_node即L+R+1 并更新ans
        return Math.max(L, R) + 1; // 返回该节点为根的子树的深度
    }

    @Test
    public void test() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);

        System.out.println(root);

        final int result = diameterOfBinaryTree(root);
        System.out.println(result);
    }
}
