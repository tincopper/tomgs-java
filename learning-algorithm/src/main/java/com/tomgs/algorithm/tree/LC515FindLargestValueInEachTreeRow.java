package com.tomgs.algorithm.tree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * LC515FindLargestValueInEachTreeRow
 * <p>
 * <a href="https://leetcode.cn/problems/find-largest-value-in-each-tree-row/">515. 在每个树行中找最大值</a>
 * <p>
 * 给定一棵二叉树的根节点 root ，请找出该二叉树中每一层的最大值。
 *
 * @author tomgs
 * @since 1.0
 */
public class LC515FindLargestValueInEachTreeRow {

    List<Integer> res = new ArrayList<>();

    // 1、先进行层序遍历
    // 2、在遍历层的时候比较当前层的最大值
    public List<Integer> largestValues(TreeNode root) {
        if (root == null) {
            return res;
        }
        // 按层遍历
        List<TreeNode> nodes = new ArrayList<>();
        nodes.add(root);

        levelTraverse(nodes);

        return res;
    }

    private void levelTraverse(List<TreeNode> currNodes) {
        if (currNodes.isEmpty()) {
            return;
        }

        Integer maxVal = null;
        List<TreeNode> nextNodes = new ArrayList<>();
        for (TreeNode currNode : currNodes) {
            // 取最大值，这里这样做是因为可能会有负数的情况
            if (maxVal == null) {
                maxVal = currNode.val;
            } else {
                maxVal = Math.max(maxVal, currNode.val);
            }
            if (currNode.left != null) {
                nextNodes.add(currNode.left);
            }
            if (currNode.right != null) {
                nextNodes.add(currNode.right);
            }
        }

        res.add(maxVal);

        levelTraverse(nextNodes);
    }

    @Test
    public void test() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(3);
        root.left.left = new TreeNode(5);
        root.left.right = new TreeNode(3);

        root.right = new TreeNode(2);
        root.right.right = new TreeNode(9);

        System.out.println(root);

        System.out.println(largestValues(root));
    }

    @Test
    public void test2() {
        TreeNode root = new TreeNode(0);
        root.left = new TreeNode(-1);

        System.out.println(root);

        System.out.println(largestValues(root));
    }

}
