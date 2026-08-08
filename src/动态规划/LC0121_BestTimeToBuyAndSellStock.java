// ============================================================
// LeetCode 121. 买卖股票的最佳时机 (Best Time to Buy and Sell Stock)
// 难度：Easy | 分类：动态规划
// 链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
// 刷题日期：2026-08-09
//
// ============================================================

import java.util.*;

public class LC0121_BestTimeToBuyAndSellStock {

    // ==== 提交代码开始 ====
    public int maxProfit(int[] prices) {
        int max = 0;
        int minPrice = prices[0];
        for (int i = 0; i < prices.length; i++) {
            max = Math.max(max, prices[i] - minPrice);
            minPrice = Math.min(prices[i], minPrice);
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0121_BestTimeToBuyAndSellStock s = new LC0121_BestTimeToBuyAndSellStock();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(5, s.maxProfit(new int[]{7, 1, 5, 3, 6, 4}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{7, 6, 4, 3, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{5}), "边界-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.maxProfit(new int[]{1, 2, 3, 4, 5}), "边界-全递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全递增 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{5, 4, 3, 2, 1}), "边界-全递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全递减 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{3, 3, 3}), "边界-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同 异常: " + t); }
        try {
            int[] up = new int[100000];
            for (int i = 0; i < up.length; i++) up[i] = i;
            if (!TestUtil.checkEq(99999, s.maxProfit(up), "边界-长递增数组")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长递增数组 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
