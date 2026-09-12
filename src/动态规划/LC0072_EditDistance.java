// ============================================================
// LeetCode 72. 编辑距离 (Edit Distance)
// 难度：Medium | 分类：动态规划（线性 DP）
// 链接：https://leetcode.cn/problems/edit-distance/
// 复习日期：2026-09-12（第 4 次复习 · 一刷 2026-08-19 · 上次复习 2026-09-06）
//
// 复写要求：已独立写出滚动数组 O(n) 空间优化，保持 leftTop 的斜对角语义。
// 思路：一维滚动 DP，按删除、替换、插入三种代价取最小值。
// 复杂度：时间 O(mn)，空间 O(n)
// ============================================================

import java.util.*;

public class LC0072_EditDistance {

    // ==== 提交代码开始 ====
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        if (m == 0) {
            return n;
        }
        if (n == 0) {
            return m;
        }
        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            dp[i] = i + 1;
        }
        for (int i = 0; i < m; i++) {
            int leftTop = i;
            for (int j = 0; j < n; j++) {
                int pre = dp[j];
                if (word1.charAt(i) == word2.charAt(j)) {
                    dp[j] = leftTop;
                } else {
                    dp[j] = Math.min(j == 0 ? i + 1 : dp[j - 1], Math.min(leftTop, pre)) + 1;
                }
                leftTop = pre;
            }
        }
        return dp[n - 1];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0072_EditDistance s = new LC0072_EditDistance();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.minDistance("horse", "ros"), "示例1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(5, s.minDistance("intention", "execution"), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（与一刷归档保持一致）----
        // 双空 / 一边为空（全插入、全删除）
        try {
            if (!TestUtil.checkEq(0, s.minDistance("", ""), "双空")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("双空 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(3, s.minDistance("", "abc"), "空->abc全插入")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("空->abc全插入 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(3, s.minDistance("abc", ""), "abc->空全删除")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("abc->空全删除 异常: " + t);
        }
        // 单字符：相同 / 不同（替换）
        try {
            if (!TestUtil.checkEq(0, s.minDistance("a", "a"), "单字符相同")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("单字符相同 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(1, s.minDistance("a", "b"), "单字符替换")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("单字符替换 异常: " + t);
        }
        // 完全相同 / 长度差 1（只需一次插入或删除）
        try {
            if (!TestUtil.checkEq(0, s.minDistance("abc", "abc"), "完全相同")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("完全相同 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(1, s.minDistance("abc", "abcd"), "插入尾字符")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("插入尾字符 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(1, s.minDistance("abcd", "abc"), "删除尾字符")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("删除尾字符 异常: " + t);
        }
        // 等长完全不同：全部替换
        try {
            if (!TestUtil.checkEq(3, s.minDistance("abc", "def"), "等长全替换")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("等长全替换 异常: " + t);
        }
        // 交换两个字符：需 2 次操作（替换，或删除+插入）
        try {
            if (!TestUtil.checkEq(2, s.minDistance("ab", "ba"), "交换两字符")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("交换两字符 异常: " + t);
        }
        // 经典例子（替换 + 插入/删除 组合）
        try {
            if (!TestUtil.checkEq(3, s.minDistance("kitten", "sitting"), "kitten->sitting")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("kitten->sitting 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(3, s.minDistance("sunday", "saturday"), "sunday->saturday")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("sunday->saturday 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}