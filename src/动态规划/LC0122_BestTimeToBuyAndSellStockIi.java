// ============================================================
// LeetCode 122. 买卖股票的最佳时机 II (Best Time to Buy and Sell Stock II)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
// 刷题日期：2026-08-26
//
// 思路：贪心——交易次数不限，把每一个「价格上涨的相邻差值」都累加：
//       max(0, prices[i]-prices[i-1]) 逐项求和，等价于每个局部低点买入、高点卖出
// DP 子类型：状态机 DP——「任意时刻最多持有一股」意味着只有两个状态：持有一支 / 不持有，
//            状态沿天数推进，每天按「买/卖/不动」在状态间转移；也可贪心只累加正差价
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0122_BestTimeToBuyAndSellStockIi {

    // ==== 提交代码开始 ====
    public int maxProfit(int[] prices) {
        int max = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                max += (prices[i] - prices[i - 1]);
            }
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0122_BestTimeToBuyAndSellStockIi s = new LC0122_BestTimeToBuyAndSellStockIi();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(7, s.maxProfit(new int[]{7, 1, 5, 3, 6, 4}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.maxProfit(new int[]{1, 2, 3, 4, 5}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{7, 6, 4, 3, 1}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= prices.length <= 3*10^4，0 <= prices[i] <= 10^4
        // 边界1: 单日，无法交易
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{5}), "边界1: 单日")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 全相等价格，来回交易无收益
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{3, 3, 3, 3}), "边界2: 全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 含 0 价（约束允许 prices[i]=0），0 买入 1 卖出
        try {
            if (!TestUtil.checkEq(1, s.maxProfit(new int[]{0, 1}), "边界3: 零价买入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 锯齿形 [1,2,1,2]，两段各赚 1
        try {
            if (!TestUtil.checkEq(2, s.maxProfit(new int[]{1, 2, 1, 2}), "边界4: 锯齿震荡")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 先涨后跌再涨 [1,3,1,4] → (3-1)+(4-1)=5
        try {
            if (!TestUtil.checkEq(5, s.maxProfit(new int[]{1, 3, 1, 4}), "边界5: 两段交易")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 长度 30000 上限，1,2 交替 → 每对赚 1 共 15000
        try {
            int[] big = new int[30000];
            for (int i = 0; i < 30000; i++) big[i] = (i % 2 == 0) ? 1 : 2;
            if (!TestUtil.checkEq(15000, s.maxProfit(big), "边界6: 上限交替")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}