// ============================================================
// LeetCode 5. 最长回文子串 (Longest Palindromic Substring)
// 难度：Medium | 分类：动态规划（子类型：区间 DP；最优常写中心扩散）
// 链接：https://leetcode.cn/problems/longest-palindromic-substring/
// 复习日期：2026-09-02（第 2 次复习 · 一刷 2026-08-09 · 上次 2026-09-01 较强）
// 一刷写法：区间 DP（boolean 表，按长度枚举）；上次写法：中心扩散（2n-1 个中心，O(1) 空间）
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0005_LongestPalindromicSubstring {

    // ==== 提交代码开始 ====
    public String longestPalindrome(String s) {
        int start = -1;
        int max = 0;
        for (int j = 0; j < s.length(); j++) {
            int len1 = getLen(s, j, j);
            int len2 = getLen(s, j, j + 1);
            int maxLen = Math.max(len1, len2);
            if (maxLen > max) {
                max = maxLen;
                start = j - (maxLen - 1) / 2;
            }
        }
        return s.substring(start, start + max);
    }

    private int getLen(String s, int i, int j) {
        while (i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)) {
            i--;
            j++;
        }
        return j - i - 1;
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
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq("bb", s.longestPalindrome("cbbd"), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（与一刷归档保持一致）----
        try {
            if (!TestUtil.checkEq("a", s.longestPalindrome("a"), "边界-单字符")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-单字符 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq("aa", s.longestPalindrome("aa"), "边界-双字符相同")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-双字符相同 异常: " + t);
        }
        try {
            String r2 = s.longestPalindrome("ab");
            if (!("a".equals(r2) || "b".equals(r2))) {
                failures++;
                System.out.println("边界-双字符不同 失败 ✗ 期望=a 或 b，实际=" + r2);
            } else {
                System.out.println("边界-双字符不同 通过 ✓");
            }
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-双字符不同 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq("aaaa", s.longestPalindrome("aaaa"), "边界-全相同")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-全相同 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq("abba", s.longestPalindrome("abbacdef"), "边界-偶数中心")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-偶数中心 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq("bab", s.longestPalindrome("cbabd"), "边界-奇数中心")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-奇数中心 异常: " + t);
        }
        try {
            String longAll = "a".repeat(1000);
            if (!TestUtil.checkEq(longAll, s.longestPalindrome(longAll), "边界-长全同")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-长全同 异常: " + t);
        }
        // TLE 回归（力扣 93/144 失败：约 1000 个全 0，期望整串；O(n³) 冗余循环超时，本地只验正确性）
        try {
            String zeros = "0".repeat(1000);
            if (!TestUtil.checkEq(zeros, s.longestPalindrome(zeros), "回归-全0长串TLE")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("回归-全0长串TLE 异常: " + t);
        }
        try {
            String r3 = s.longestPalindrome("abca");
            if (!("a".equals(r3) || "b".equals(r3) || "c".equals(r3))) {
                failures++;
                System.out.println("边界-两端等内非回文 失败 ✗ 期望=单个字符（a/b/c），实际=" + r3);
            } else {
                System.out.println("边界-两端等内非回文 通过 ✓");
            }
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-两端等内非回文 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq("abccba", s.longestPalindrome("abccba"), "边界-长度6回文")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-长度6回文 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq("abcba", s.longestPalindrome("abcba"), "边界-长度5回文")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-长度5回文 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
