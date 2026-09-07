// ============================================================
// LeetCode 62. 不同路径 (Unique Paths)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/unique-paths/
// 二刷日期：2026-09-07（第 3 次复习 · 一刷 2026-08-09 · 上次 2026-09-02 较强）
// 一刷/上次思路：一维滚动 DP——dp 全 1 初始化，双层循环 dp[j] = dp[j-1] + dp[j]
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0062_UniquePaths {

    // ==== 提交代码开始 ====
    public int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[j] = dp[j - 1] + dp[j];
            }
        }
        return dp[n - 1];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0062_UniquePaths s = new LC0062_UniquePaths();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(28, s.uniquePaths(3, 7), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.uniquePaths(3, 2), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(28, s.uniquePaths(7, 3), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.uniquePaths(3, 3), "示例4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例4 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // TODO: 补充空输入 / 单元素 / 全相同 / 大数等边界
        // 例如： try { if (!TestUtil.checkEq(期望, s.uniquePaths(边界输入), "边界1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 若题目允许任意顺序返回（下标对 / 集合），用 TestUtil.checkEqUnordered 代替 TestUtil.checkEq

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}