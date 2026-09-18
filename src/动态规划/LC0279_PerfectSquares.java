// ============================================================
// LeetCode 279. 完全平方数 (Perfect Squares)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/perfect-squares/
// 刷题日期：2026-09-18
// 二刷 · 一刷 2026-08-10 · 一维 DP 枚举最后一块平方数
// 测试用例与一刷归档保持一致
// ============================================================

import java.util.*;

public class LC0279_PerfectSquares {

    // ==== 提交代码开始 ====
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= Math.sqrt(i); j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0279_PerfectSquares s = new LC0279_PerfectSquares();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.numSquares(12), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.numSquares(13), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(1, s.numSquares(1), "边界-最小n")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-最小n 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.numSquares(2), "边界-两小平方和")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两小平方和 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.numSquares(4), "边界-本身是平方数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-本身是平方数 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.numSquares(7), "边界-经典7")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-经典7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.numSquares(18), "边界-9+9")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-9+9 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.numSquares(10000), "边界-上限大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-上限大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
