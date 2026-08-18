// ============================================================
// LeetCode 64. 最小路径和 (Minimum Path Sum)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/minimum-path-sum/
// 刷题日期：2026-08-18
//
// ============================================================

import java.util.*;

public class LC0064_MinimumPathSum {

    // ==== 提交代码开始 ====
    public int minPathSum(int[][] grid) {
        int[] dp = new int[grid[0].length];
        for (int i = 0; i < dp.length; i++) {
            if (i == 0) {
                dp[i] = grid[0][i];
            } else {
                dp[i] = dp[i - 1] + grid[0][i];
            }
        }
        for (int i = 1; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (j == 0) {
                    dp[j] = dp[j] + grid[i][0];
                } else {
                    dp[j] = Math.min(dp[j], dp[j - 1]) + grid[i][j];
                }
            }
        }
        return dp[grid[0].length - 1];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0064_MinimumPathSum s = new LC0064_MinimumPathSum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(7, s.minPathSum(new int[][]{new int[]{1, 3, 1}, new int[]{1, 5, 1}, new int[]{4, 2, 1}}), "示例1"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(12, s.minPathSum(new int[][]{new int[]{1, 2, 3}, new int[]{4, 5, 6}}), "示例2"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        // 边界1-单行：只能一直向右，和为整行
        try {
            if (!TestUtil.checkEq(6, s.minPathSum(new int[][]{new int[]{1, 2, 3}}), "边界1-单行")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1-单行 异常: " + t);
        }
        // 边界2-单列：只能一直向下，和为整列
        try {
            if (!TestUtil.checkEq(6, s.minPathSum(new int[][]{new int[]{1}, new int[]{2}, new int[]{3}}), "边界2-单列"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2-单列 异常: " + t);
        }
        // 边界3-单元素：答案就是 grid[0][0]
        try {
            if (!TestUtil.checkEq(7, s.minPathSum(new int[][]{new int[]{7}}), "边界3-单元素")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3-单元素 异常: " + t);
        }
        // 边界4-全零矩阵：最小和为 0
        try {
            if (!TestUtil.checkEq(0, s.minPathSum(new int[][]{new int[]{0, 0}, new int[]{0, 0}}), "边界4-全零"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4-全零 异常: " + t);
        }
        // 边界5-多路径选择：先下后右的 1+1+1+1=4 是最优，验证不是只取每步最小格子
        try {
            if (!TestUtil.checkEq(4, s.minPathSum(new int[][]{new int[]{1, 2, 3}, new int[]{1, 1, 1}}), "边界5-多路径选择"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5-多路径选择 异常: " + t);
        }
        // 边界6-大矩阵（200x200 全 1）：必须走 200+200-1=399 格，验证不越界与长度计算
        try {
            int[][] big = new int[200][200];
            for (int[] row : big) java.util.Arrays.fill(row, 1);
            if (!TestUtil.checkEq(399, s.minPathSum(big), "边界6-大矩阵")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6-大矩阵 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}