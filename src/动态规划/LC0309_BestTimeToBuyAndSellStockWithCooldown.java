// ============================================================
// LeetCode 309. 买卖股票的最佳时机含冷冻期 (Best Time to Buy and Sell Stock with Cooldown)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
// 刷题日期：2026-08-19
//
// ============================================================


public class LC0309_BestTimeToBuyAndSellStockWithCooldown {

    // ==== 提交代码开始 ====
    public int maxProfit(int[] prices) {
        return maxProfit(prices, 1);
    }

    public int maxProfit(int[] prices, int k) {
        int[][] dp = new int[prices.length][3];
        dp[0][0] = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][2] - prices[i]);
            dp[i][1] = dp[i - 1][0] + prices[i];
            dp[i][2] = Math.max(dp[i-1][2], i-k >= 0 ? dp[i-k][1] : 0);
        }
        return Math.max(dp[prices.length - 1][1], dp[prices.length - 1][2]);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0309_BestTimeToBuyAndSellStockWithCooldown s = new LC0309_BestTimeToBuyAndSellStockWithCooldown();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.maxProfit(new int[]{1, 2, 3, 0, 2}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题逻辑设计）----
        // 全相同：无利可图，应返回 0
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{5, 5, 5, 5}), "边界-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同 异常: " + t); }
        // 单调递减：只卖不买，应返回 0
        try {
            if (!TestUtil.checkEq(0, s.maxProfit(new int[]{4, 3, 2, 1}), "边界-单调递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单调递减 异常: " + t); }
        // 单调递增：一次性持有到最后最优（中途卖出会引入冷冻期），3 天 1->4 赚 3
        try {
            if (!TestUtil.checkEq(3, s.maxProfit(new int[]{1, 2, 3, 4}), "边界-单调递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单调递增 异常: " + t); }
        // 冷冻期必须隔一天：交替 1,2,1,2... 每笔交易后次日是冷冻期，
        // 6 天最多完成 2 笔（忽略冷冻期会错算成 3 笔）
        try {
            if (!TestUtil.checkEq(2, s.maxProfit(new int[]{1, 2, 1, 2, 1, 2}), "边界-冷冻期隔天")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-冷冻期隔天 异常: " + t); }
        // 短线小赚不划算：1买2卖只赚 1 且第 2 天进入冷冻期错过价 4，
        // 应直接 1->4 持有赚 3
        try {
            if (!TestUtil.checkEq(3, s.maxProfit(new int[]{1, 2, 4}), "边界-长持有胜短线")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长持有胜短线 异常: " + t); }
        // 下跌后低点买入并持有到最高点，中间卖出反而因冷冻期错过峰值
        try {
            if (!TestUtil.checkEq(2, s.maxProfit(new int[]{5, 4, 3, 2, 1, 2, 3}), "边界-低点持有")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-低点持有 异常: " + t); }
        // 早期交易不如等待更好入场：2,3 先赚 1 后 4,5 再赚 1（共 2），
        // 不如 1->5 直接赚 4；注意冷冻期禁止 1 买 4 卖后立即再买
        try {
            if (!TestUtil.checkEq(4, s.maxProfit(new int[]{2, 3, 1, 4, 5}), "边界-等待入场")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-等待入场 异常: " + t); }
        // 大数组（1000 天单调递增）：长持有赚 999，同时验证性能
        {
            int[] big = new int[1000];
            for (int i = 0; i < big.length; i++) big[i] = i + 1;
            try {
                if (!TestUtil.checkEq(999, s.maxProfit(big), "边界-大数组递增")) failures++;
            } catch (Throwable t) { failures++; System.out.println("边界-大数组递增 异常: " + t); }
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}