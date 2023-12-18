package com.tomgs.algorithm.tree;

import org.junit.Test;

/**
 * LC114FlattenBinaryTreeToLinkedList
 * <p>
 * <a href="https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/">114. 二叉树展开为链表</a>
 * 给你二叉树的根结点 root ，请你将它展开为一个单链表：
 * <p>
 * 展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
 * 展开后的单链表应该与二叉树 先序遍历 顺序相同。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC114FlattenBinaryTreeToLinkedList {

    /**
     * 分解问题方法：
     * 1、先拉平左右子树
     * 2、将左子树作为右子树
     * 3、将原先的右子树 接到 当前右子树 末尾
     */
    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        // 1、拉平左右子树
        flatten(root.left);
        flatten(root.right);

        /**** 后序遍历位置 ****/
        // 左右子树已经被拉平成一条链表
        TreeNode left = root.left;   // 原先的左子树
        TreeNode right = root.right; // 原先的右子树

        // 2、将左子树作为右子树
        root.left = null;
        root.right = left;

        // 3、将原先的右子树接到当前右子树的末端
        TreeNode p = root;
        while (p.right != null) {
            p = p.right;
        }
        p.right = right;
    }

    // 分解问题的方法
    // 先将左子树拉平
    // 然后将右子树拉平
    // 然后将右子树接到左子树后边
    // 然后将整个左子树作为右子树
    // 这个肯定需要在后序位置处理数据，因为要拉平需要找到最后的节点
    public void flatten2(TreeNode root) {
        flattenLeft(root);
        flattenRight(root);
    }

    public void flattenLeft(TreeNode root) {
        if (root == null) {
            return;
        }
        flattenLeft(root.left);
        //flatten(root.right);

        // 后续遍历
        TreeNode left = root.left;
        TreeNode right = root.right;
        if (left == null) {
            return;
        }
        // 找到最后一个
        while (left.left != null) {
            left = left.left;
        }
        left.left = right;
        root.right = null;
    }

    public void flattenRight(TreeNode root) {
        if (root == null) {
            return;
        }
        if (root.right == null) {
            root.right = root.left;
            root.left = null;
        }
        flattenRight(root.right);
    }

    @Test
    public void test() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(4);

        root.right = new TreeNode(5);
        root.right.right = new TreeNode(6);

        System.out.println(root);

        flatten(root);

        System.out.println(root);
    }
}
