// ============================================================
// LeetCode 10. 正则表达式匹配 (Regular Expression Matching)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/regular-expression-matching/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-10）
//
// 思路：前缀长度语义的二维 DP，滚动匹配 `*` 的零次/多次两种分支。
// 复杂度：时间 O(|s|·|p|)，空间 O(|s|·|p|)
// ============================================================

import java.util.*;

public class LC0010_RegularExpressionMatching {

    // ==== 提交代码开始 ====
    public boolean isMatch(String s, String p) {
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        dp[0][0] = true;
        for (int i = 0; i <= s.length(); i++) {
            for (int j = 1; j <= p.length(); j++) {
                char c = p.charAt(j - 1);
                if (i > 0 && (s.charAt(i - 1) == c || c == '.')) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (c == '*') {
                    dp[i][j] = dp[i][j - 2] || (i > 0 && dp[i - 1][j] && (s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.'));
                }
            }
        }
        return dp[s.length()][p.length()];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0010_RegularExpressionMatching s = new LC0010_RegularExpressionMatching();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(false, s.isMatch("aa", "a"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isMatch("aa", "a*"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isMatch("ab", ".*"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(true, s.isMatch("a", "."), "边界-点单字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-点单字符 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isMatch("a", "ab*"), "边界-星号零次")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-星号零次 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isMatch("aab", "c*a*b"), "边界-前缀星号")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-前缀星号 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isMatch("aaa", "a*a"), "边界-点星组合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-点星组合 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isMatch("mississippi", "mis*is*p*."), "边界-经典不匹配")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-经典不匹配 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isMatch("ab", ".*c"), "边界-后缀不匹配")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-后缀不匹配 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
