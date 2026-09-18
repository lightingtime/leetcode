// ============================================================
// LeetCode 322. 零钱兑换 (Coin Change)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/coin-change/
// 刷题日期：2026-09-18
// 二刷 · 一刷 2026-08-10（一次 AC，非最优=否）：一维 DP（完全背包，外层金额内层硬币）
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归）
//
// 思路：完全背包一维 DP，dp[i] = 凑出金额 i 的最少硬币数，dp[i] = min(dp[i - c] + 1)
// 复杂度：时间 O(amount · coins) 空间 O(amount)
// ============================================================

import java.util.*;

public class LC0322_CoinChange {

    // ==== 提交代码开始 ====
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, 100001);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (i - coins[j] >= 0) {
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        return dp[amount] == 100001 ? -1 : dp[amount];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0322_CoinChange s = new LC0322_CoinChange();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.coinChange(new int[]{1, 2, 5}, 11), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.coinChange(new int[]{2}, 3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.coinChange(new int[]{1}, 0), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(5, s.coinChange(new int[]{5}, 25), "边界-单硬币整除")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单硬币整除 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.coinChange(new int[]{2, 5}, 6), "边界-无1面额")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-无1面额 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.coinChange(new int[]{7}, 3), "边界-硬币大于金额")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-硬币大于金额 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.coinChange(new int[]{1, 3, 4}, 6), "边界-贪心反例")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-贪心反例 异常: " + t); }
        try {
            if (!TestUtil.checkEq(400, s.coinChange(new int[]{1, 5, 10, 25}, 10000), "边界-上限大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-上限大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
