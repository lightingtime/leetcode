// ============================================================
// LeetCode 5. 最长回文子串 (Longest Palindromic Substring)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/longest-palindromic-substring/
// 刷题日期：2026-08-09
//
// ============================================================

import java.util.*;

public class LC0005_LongestPalindromicSubstring {

    // ==== 提交代码开始 ====
    public String longestPalindrome(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
            if (i < n - 1 && s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
            }
        }
        String ans = "";
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                if (j - i >= 2 && s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1];
                }
                if (dp[i][j] && j - i >= ans.length()) {
                    ans = s.substring(i, j + 1);
                }
            }
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0005_LongestPalindromicSubstring s = new LC0005_LongestPalindromicSubstring();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            String r1 = s.longestPalindrome("babad");
            if (!("bab".equals(r1) || "aba".equals(r1))) {
                failures++;
                System.out.println("示例1 失败 ✗ 期望=bab 或 aba，实际=" + r1);
            } else {
                System.out.println("示例1 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("bb", s.longestPalindrome("cbbd"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq("a", s.longestPalindrome("a"), "边界-单字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单字符 异常: " + t); }
        try {
            if (!TestUtil.checkEq("aa", s.longestPalindrome("aa"), "边界-双字符相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-双字符相同 异常: " + t); }
        try {
            String r2 = s.longestPalindrome("ab");
            if (!("a".equals(r2) || "b".equals(r2))) {
                failures++;
                System.out.println("边界-双字符不同 失败 ✗ 期望=a 或 b，实际=" + r2);
            } else {
                System.out.println("边界-双字符不同 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-双字符不同 异常: " + t); }
        try {
            if (!TestUtil.checkEq("aaaa", s.longestPalindrome("aaaa"), "边界-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同 异常: " + t); }
        try {
            if (!TestUtil.checkEq("abba", s.longestPalindrome("abbacdef"), "边界-偶数中心")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-偶数中心 异常: " + t); }
        try {
            if (!TestUtil.checkEq("bab", s.longestPalindrome("cbabd"), "边界-奇数中心")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-奇数中心 异常: " + t); }
        try {
            String longAll = "a".repeat(1000);
            if (!TestUtil.checkEq(longAll, s.longestPalindrome(longAll), "边界-长全同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长全同 异常: " + t); }
        try {
            String r3 = s.longestPalindrome("abca");
            if (!("a".equals(r3) || "b".equals(r3) || "c".equals(r3))) {
                failures++;
                System.out.println("边界-两端等内非回文 失败 ✗ 期望=单个字符（a/b/c），实际=" + r3);
            } else {
                System.out.println("边界-两端等内非回文 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-两端等内非回文 异常: " + t); }
        try {
            if (!TestUtil.checkEq("abccba", s.longestPalindrome("abccba"), "边界-长度6回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长度6回文 异常: " + t); }
        try {
            if (!TestUtil.checkEq("abcba", s.longestPalindrome("abcba"), "边界-长度5回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长度5回文 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
