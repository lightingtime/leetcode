// ============================================================
// LeetCode 221. 最大正方形 (Maximal Square)
// 难度：Medium | 分类：动态规划（二维网格 DP）
// 链接：https://leetcode.cn/problems/maximal-square/
// 复习日期：2026-09-15（第 4 次复习 · 一刷 2026-08-19 · 上次复习 2026-09-12）
//
// 思路：一维滚动 DP，按以当前格为右下角的最大正方形边长转移。
// 复杂度：时间 O(mn)，空间 O(n)
// ============================================================

import java.util.*;

public class LC0221_MaximalSquare {

    // ==== 提交代码开始 ====
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) {
            return 0;
        }
        int n = matrix[0].length;
        if (n == 0) {
            return 0;
        }
        int[] dp = new int[n];
        int max = 0;
        for (int i = 0; i < m; i++) {
            int leftTop = dp[0];
            for (int j = 0; j < n; j++) {
                int pre = dp[j];
                if (matrix[i][j] == '0') {
                    dp[j] = 0;
                } else {
                    if (i > 0 && j > 0) {
                        dp[j] = Math.min(dp[j - 1], Math.min(pre, leftTop)) + 1;
                    } else {
                        dp[j] = 1;
                    }
                }
                leftTop = pre;
                max = Math.max(max, dp[j]);
            }
        }
        return max * max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0221_MaximalSquare s = new LC0221_MaximalSquare();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{new char[]{'1', '0', '1', '0', '0'}, new char[]{'1', '0', '1', '1', '1'}, new char[]{'1', '1', '1', '1', '1'}, new char[]{'1', '0', '0', '1', '0'}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'0', '1'}, new char[]{'1', '0'}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maximalSquare(new char[][]{new char[]{'0'}}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- WA 回归用例（力扣失败输入）----
        try { if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{
                new char[]{'1','0','1','1','0','1'}, new char[]{'1','1','1','1','1','1'},
                new char[]{'0','1','1','0','1','1'}, new char[]{'1','1','1','0','1','0'},
                new char[]{'0','1','1','1','1','1'}, new char[]{'1','1','0','1','1','1'}}), "WA回归6x6")) failures++; } catch (Throwable t) { failures++; System.out.println("WA回归6x6 异常: " + t); }

        // ---- 边界测试（与一刷归档保持一致）----
        // 单元素：'1' -> 1，'0' -> 0
        try { if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'1'}}), "单元素1")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素1 异常: " + t); }
        // 全零：无 1，面积为 0
        try { if (!TestUtil.checkEq(0, s.maximalSquare(new char[][]{new char[]{'0', '0'}, new char[]{'0', '0'}}), "全零2x2")) failures++; } catch (Throwable t) { failures++; System.out.println("全零2x2 异常: " + t); }
        // 单行 / 单列：最多只能形成 1x1
        try { if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'1', '1', '1'}}), "单行全1")) failures++; } catch (Throwable t) { failures++; System.out.println("单行全1 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'1'}, new char[]{'1'}, new char[]{'1'}}), "单列全1")) failures++; } catch (Throwable t) { failures++; System.out.println("单列全1 异常: " + t); }
        // 全一：正方形边长 = min(行,列)
        try { if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{new char[]{'1', '1'}, new char[]{'1', '1'}}), "全一2x2")) failures++; } catch (Throwable t) { failures++; System.out.println("全一2x2 异常: " + t); }
        try { if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{new char[]{'1', '1', '1'}, new char[]{'1', '1', '1'}}), "全一2x3长条")) failures++; } catch (Throwable t) { failures++; System.out.println("全一2x3长条 异常: " + t); }
        try { if (!TestUtil.checkEq(9, s.maximalSquare(new char[][]{new char[]{'1', '1', '1'}, new char[]{'1', '1', '1'}, new char[]{'1', '1', '1'}}), "全一3x3")) failures++; } catch (Throwable t) { failures++; System.out.println("全一3x3 异常: " + t); }
        // 零矩阵中嵌入 2x2 全一正方形
        try { if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{new char[]{'0', '0', '0'}, new char[]{'0', '1', '1'}, new char[]{'0', '1', '1'}}), "嵌入2x2")) failures++; } catch (Throwable t) { failures++; System.out.println("嵌入2x2 异常: " + t); }
        // 对角线上有 0，破坏 2x2：最多 1x1
        try { if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'1', '1'}, new char[]{'1', '0'}}), "对角0破坏")) failures++; } catch (Throwable t) { failures++; System.out.println("对角0破坏 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
