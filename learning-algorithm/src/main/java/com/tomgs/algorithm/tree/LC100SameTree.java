package com.tomgs.algorithm.tree;

import org.junit.Test;

/**
 * LC100SameTree
 * <p>
 * <a href="https://leetcode.cn/problems/same-tree/?show=1">100. 相同的树</a>
 * <p>
 * 给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC100SameTree {

    // 分解问题法
    // 只需要根节点相同、左右子树相同，那么就可以认为两颗树相同
    public boolean isSameTree(TreeNode p, TreeNode q) {
        // 判断根结点是否相同
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null || p.val != q.val) {
            return false;
        }

        // 判断左子树是否相同
        boolean leftIsSameTree = isSameTree(p.left, q.left);
        // 判断右子树是否相同
        boolean rightIsSameTree = isSameTree(p.right, q.right);
        return leftIsSameTree && rightIsSameTree;
    }

    @Test
    public void test() {
        TreeNode p = new TreeNode(1);
        p.left = new TreeNode(2);
        p.right = new TreeNode(3);

        TreeNode q = new TreeNode(1);
        q.left = new TreeNode(2);
        q.right = new TreeNode(3);

        System.out.println(isSameTree(p, q));
    }
}
