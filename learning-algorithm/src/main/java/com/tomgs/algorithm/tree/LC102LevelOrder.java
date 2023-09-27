package com.tomgs.algorithm.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * LC102LevelOrder
 * <p>
 * <a href="https://leetcode.cn/problems/binary-tree-level-order-traversal/">102. 二叉树的层序遍历</a>
 * <p>
 * 给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。
 * <p>
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：[[3],[9,20],[15,7]]
 *
 * @author tomgs
 * @since 1.0
 */
public class LC102LevelOrder {

    List<List<Integer>> result = new ArrayList<>();
    public List<List<Integer>> levelOrder(TreeNode root) {
        traverse(root, 0);
        return result;
    }

    public void traverse(TreeNode root, int level) {
        if (root == null) {
            return;
        }

        // 前序位置，看看是否已经存储 level 层的节点了
        if (result.size() <= level) {
            result.add(level, new ArrayList<>());
        }
        result.get(level).add(root.val);

        traverse(root.left, level + 1);
        traverse(root.right, level + 1);
    }

    // 遍历当前层的时候把下一层的节点放入队列里面
    public List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        // 队列存储当前层的节点数据
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            List<Integer> tmp = new ArrayList<>();
            final int size = queue.size();
            for (int i = 0; i < size; i++) {
                // 获取当前层的数据
                final TreeNode treeNode = queue.poll();
                tmp.add(treeNode.val);
                // 遍历下一层
                if (treeNode.left != null) {
                    queue.offer(treeNode.left);
                }
                if (treeNode.right != null) {
                    queue.offer(treeNode.right);
                }
            }
            result.add(tmp);
        }

        return result;
    }

    @Test
    public void test() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.left.left = new TreeNode(19);
        root.left.right = new TreeNode(29);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        System.out.println(root);

        final List<List<Integer>> result = levelOrder(root);
        System.out.println(result);
    }

    @Test
    public void test2() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.left.left = new TreeNode(19);
        root.left.right = new TreeNode(29);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);

        System.out.println(root);

        final List<List<Integer>> result = levelOrder2(root);
        System.out.println(result);
    }
}
