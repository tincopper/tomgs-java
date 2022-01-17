package com.tomgs.algorithm.array;

import org.junit.Test;

/**
 * 121. 买卖股票的最佳时机
 * <p>
 * 给定一个数组 prices ，它的第 i 个元素prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * <p>
 * 示例 1：
 * 输入：[7,1,5,3,6,4]
 * 输出：5
 * 解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
 * 注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
 * <p>
 * 示例 2：
 * <p>
 * 输入：prices = [7,6,4,3,1]
 * 输出：0
 * 解释：在这种情况下, 没有交易完成, 所以最大利润为 0。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author tomgs
 * @version 2022/1/17 1.0
 */
public class LC121MaxProfit {

    // 方法1
    public int maxProfit(int[] prices) {
        int p1 = 0, p2 = 1, diff = 0;
        int maxProfit = 0;
        while (p1 < prices.length && p2 < prices.length) {
            diff = prices[p2] - prices[p1];
            if (diff < 0) {
                // 说明找到比最小值还小的数
                p1=p2++;
            } else {
                p2++;
                if (diff > maxProfit) {
                    maxProfit = diff;
                }
            }
        }

        return maxProfit;
    }

    // 方法2
    public int maxProfit2(int[] prices) {
        int min = prices[0];
        int len = prices.length;
        int max = 0;
        for (int i = 1; i < len; i++) {
            int price = prices[i];
            if (price > min) {
                max = Math.max(max, price - min);
            }
            min = Math.min(min, price);
        }
        return max;
    }

    @Test
    public void test1() {
        int[] prices = {7, 1, 5, 3, 6, 4};
        int maxProfit = maxProfit(prices);
        System.out.println(maxProfit); // 5
    }

    @Test
    public void test2() {
        int[] prices = {7, 6, 4, 3, 1};
        int maxProfit = maxProfit(prices);
        System.out.println(maxProfit); // 0
    }

    @Test
    public void test3() {
        int[] prices = {2, 1, 2, 1, 0, 1, 2};
        int maxProfit = maxProfit(prices);
        System.out.println(maxProfit); // 2
    }

}
