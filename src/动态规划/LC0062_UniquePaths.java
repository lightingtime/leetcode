// ============================================================
// LeetCode 62. 不同路径 (Unique Paths)
// 难度：Medium | 分类：动态规划（子类型：线性/网格 DP）
// 链接：https://leetcode.cn/problems/unique-paths/
// 复习日期：2026-09-01（复习 · 一刷 2026-08-09）
// 一刷思路：一维滚动 DP（dp[j] = dp[j]（上） + dp[j-1]（左）），O(m*n)/O(n)
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
                dp[j] += dp[j - 1];
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
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(3, s.uniquePaths(3, 2), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(28, s.uniquePaths(7, 3), "示例3")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(6, s.uniquePaths(3, 3), "示例4")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例4 异常: " + t);
        }

        // ---- 边界测试（与一刷归档保持一致）----
        try {
            if (!TestUtil.checkEq(1, s.uniquePaths(1, 1), "边界-1x1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-1x1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(1, s.uniquePaths(1, 10), "边界-单行")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-单行 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(1, s.uniquePaths(10, 1), "边界-单列")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-单列 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(2, s.uniquePaths(2, 2), "边界-2x2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-2x2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(601080390, s.uniquePaths(17, 17), "边界-大网格")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-大网格 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
