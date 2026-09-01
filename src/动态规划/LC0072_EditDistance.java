// ============================================================
// LeetCode 72. 编辑距离 (Edit Distance)
// 难度：Medium | 分类：动态规划（子类型：线性/网格 DP）
// 链接：https://leetcode.cn/problems/edit-distance/
// 复习日期：2026-09-01（复习 · 一刷 2026-08-19）
// 一刷思路：DP 二维表格 dp[i][j]（O(mn)/O(mn)，optimal=false）
// 二刷要求：写出空间优化版——滚动数组 O(n)，diag 变量每列末更新为 prevRowSame 保持「斜对角」语义
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0072_EditDistance {

    // ==== 提交代码开始 ====
    public int minDistance(String word1, String word2) {
        int n = word2.length();
        int m = word1.length();
        // dp[j]：word2 前 j 个字符的最小操作数；行开始前是上一行，覆盖后是当前行
        int[] dp = new int[n + 1];
        // 第 0 行（word1 为空）：变成 word2 前 j 个字符需插入 j 次
        for (int j = 0; j <= n; j++) {
            dp[j] = j;
        }
        // i 是 0 基字符下标，对应二维表第 i+1 行
        for (int i = 0; i < m; i++) {
            // dig = 左上角（上一行 j-1 的旧值）；先取旧 dp[0]，供 j=1 使用
            int dig = dp[0];
            // 本行第 0 列：word1 前 i+1 个字符全部删除
            dp[0] = i + 1;
            for (int j = 1; j <= n; j++) {
                // prev = 覆盖前的 dp[j]（上一行第 j 列）：删除候选，并用于更新 dig
                int prev = dp[j];
                if (word1.charAt(i) == word2.charAt(j - 1)) {
                    // 字符相同 → 成本等于左上角
                    dp[j] = dig;
                } else {
                    // 插入=本行左 dp[j-1]、删除=上一行 dp[j]、替换=左上 dig，取最小 +1
                    dp[j] = Math.min(dp[j - 1], Math.min(dp[j], dig)) + 1;
                }
                // dig 更新为上一行第 j 列旧值，作为下一列 j+1 的左上角
                dig = prev;
            }
        }
        return dp[n];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0072_EditDistance s = new LC0072_EditDistance();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.minDistance("horse", "ros"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.minDistance("intention", "execution"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（与一刷归档保持一致）----
        // 双空 / 一边为空（全插入、全删除）
        try { if (!TestUtil.checkEq(0, s.minDistance("", ""), "双空")) failures++; } catch (Throwable t) { failures++; System.out.println("双空 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.minDistance("", "abc"), "空->abc全插入")) failures++; } catch (Throwable t) { failures++; System.out.println("空->abc全插入 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.minDistance("abc", ""), "abc->空全删除")) failures++; } catch (Throwable t) { failures++; System.out.println("abc->空全删除 异常: " + t); }
        // 单字符：相同 / 不同（替换）
        try { if (!TestUtil.checkEq(0, s.minDistance("a", "a"), "单字符相同")) failures++; } catch (Throwable t) { failures++; System.out.println("单字符相同 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.minDistance("a", "b"), "单字符替换")) failures++; } catch (Throwable t) { failures++; System.out.println("单字符替换 异常: " + t); }
        // 完全相同 / 长度差 1（只需一次插入或删除）
        try { if (!TestUtil.checkEq(0, s.minDistance("abc", "abc"), "完全相同")) failures++; } catch (Throwable t) { failures++; System.out.println("完全相同 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.minDistance("abc", "abcd"), "插入尾字符")) failures++; } catch (Throwable t) { failures++; System.out.println("插入尾字符 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.minDistance("abcd", "abc"), "删除尾字符")) failures++; } catch (Throwable t) { failures++; System.out.println("删除尾字符 异常: " + t); }
        // 等长完全不同：全部替换
        try { if (!TestUtil.checkEq(3, s.minDistance("abc", "def"), "等长全替换")) failures++; } catch (Throwable t) { failures++; System.out.println("等长全替换 异常: " + t); }
        // 交换两个字符：需 2 次操作（替换，或删除+插入）
        try { if (!TestUtil.checkEq(2, s.minDistance("ab", "ba"), "交换两字符")) failures++; } catch (Throwable t) { failures++; System.out.println("交换两字符 异常: " + t); }
        // 经典例子（替换 + 插入/删除 组合）
        try { if (!TestUtil.checkEq(3, s.minDistance("kitten", "sitting"), "kitten->sitting")) failures++; } catch (Throwable t) { failures++; System.out.println("kitten->sitting 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.minDistance("sunday", "saturday"), "sunday->saturday")) failures++; } catch (Throwable t) { failures++; System.out.println("sunday->saturday 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
