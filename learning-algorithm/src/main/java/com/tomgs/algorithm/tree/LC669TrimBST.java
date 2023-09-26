package com.tomgs.algorithm.tree;

import org.junit.Test;

import java.util.List;

/**
 * LC669TrimBST
 *
 * <p>
 * <a href="https://leetcode.cn/problems/trim-a-binary-search-tree/">669. 修剪二叉搜索树</a>
 * <p>
 * 给你二叉搜索树的根节点 root ，同时给定最小边界low 和最大边界 high。通过修剪二叉搜索树，使得所有节点的值在[low, high]中。修剪树 不应该 改变保留在树中的元素的相对结构 (即，如果没有被移除，原有的父代子代关系都应当保留)。 可以证明，存在 唯一的答案 。
 * <p>
 * 所以结果应当返回修剪好的二叉搜索树的新的根节点。注意，根节点可能会根据给定的边界发生改变。
 * @author tomgs
 * @since 1.0
 */
public class LC669TrimBST {

    /**
     * 二叉搜索树（Binary Search Tree，简称BST）是一种特殊的二叉树，它具有以下特点：
     * <p>
     * 1. 若它的左子树不为空，则左子树上所有结点的值均小于它的根结点的值；
     * 2. 若它的右子树不为空，则右子树上所有结点的值均大于它的根结点的值；
     * 3. 它的左右子树也分别为二叉搜索树。
     * <p>
     * 这些特点保证了二叉搜索树中的所有结点都满足以下条件：对于每个结点，其左子树中的所有结点的值均小于该结点的值，而右子树中的所有结点的值均大于该结点的值。这使得在二叉搜索树中查找、插入和删除操作的时间复杂度为O(log n)，其中n为树中的结点数。
     */
    public TreeNode trimBST(TreeNode root, int low, int high) {
        while (root != null) {
            if (root.val < low) {
                // 左子树都可以删除
                root = root.right;
            } else if (root.val > high) {
                // 右子树都可以删除
                root = root.left;
            } else {
                // 在区间内无需修改
                break;
            }
        }
        if (root == null) {
            return null;
        }

        final TreeNode leftNode = trimBST(root.left, low, high);
        final TreeNode rightNode = trimBST(root.right, low, high);
        root.left = leftNode;
        root.right = rightNode;
        return root;
    }

    public TreeNode trimBST2(TreeNode root, int low, int high) {
        if (root == null) {
            return null;
        }

        if (root.val < low) {
            // 说明左子树不符合要求，所以返回修剪后的右子树即可
            return trimBST2(root.right, low, high);
        } else if (root.val > high) {
            // 说明右子树不符合要求，所以返回修剪后的左子树即可
            return trimBST2(root.left, low, high);
        } else {
            // 如果结点的值位于区间 [low,high]，
            // 我们将结点的左结点设为对它的左子树修剪后的结果，右结点设为对它的右子树进行修剪后的结果。
            root.left = trimBST2(root.left, low, high);
            root.right = trimBST2(root.right, low, high);
        }
        return root;
    }

    // 迭代法
    // 如果一个结点 node 符合要求，即它的值位于区间 [low,high]，那么它的左子树与右子树应该如何修剪？
    // 我们先讨论左子树的修剪：
    // node 的左结点为空结点：不需要修剪
    // node 的左结点非空：
    // 如果它的左结点 left 的值小于 low，那么 left 以及 left 的左子树都不符合要求，我们将 node 的左结点设为 left 的右结点，然后再重新对 node 的左子树进行修剪。
    // 如果它的左结点 left 的值大于等于 low，又因为 node 的值已经符合要求，所以 left 的右子树一定符合要求。基于此，我们只需要对 left 的左子树进行修剪。我们令 node 等于 left ，然后再重新对 node 的左子树进行修剪。
    // 以上过程可以迭代处理。对于右子树的修剪同理。
    //
    // 我们对根结点进行判断，如果根结点不符合要求，我们将根结点设为对应的左结点或右结点，直到根结点符合要求，然后将根结点作为符合要求的结点，依次修剪它的左子树与右子树。
    public TreeNode trimBST3(TreeNode root, int low, int high) {
        while (root != null && (root.val < low || root.val > high)) {
            if (root.val < low) {
                root = root.right;
            } else {
                root = root.left;
            }
        }
        if (root == null) {
            return null;
        }

        for (TreeNode node = root; node.left != null;) {
            if (node.left.val < low) {
                node.left = node.left.right;
            } else {
                node = node.left;
            }
        }


        return root;
    }

    @Test
    public void test() {
        TreeNode left = new TreeNode(0);
        TreeNode right = new TreeNode(2);
        TreeNode root = new TreeNode(1);
        root.left = left;
        root.right = right;

        System.out.println(root);

        TreeNode result = trimBST(root, 1, 2);
        System.out.println(result);
    }

    @Test
    public void test2() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(1);
        root.right = new TreeNode(4);
        root.left.right = new TreeNode(2);

        System.out.println(root);

        TreeNode result = trimBST(root, 3, 4);
        System.out.println(result);
    }

    @Test
    public void test3() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(1);
        root.right = new TreeNode(4);
        root.left.right = new TreeNode(2);

        System.out.println(root);

        TreeNode result = trimBST2(root, 3, 4);
        System.out.println(result);
    }

}
