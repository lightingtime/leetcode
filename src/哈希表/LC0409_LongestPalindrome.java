// ============================================================
// LeetCode 409. 最长回文串 (Longest Palindrome)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/longest-palindrome/
// 刷题日期：2026-09-30
//
// ============================================================

import java.util.*;

public class LC0409_LongestPalindrome {

    // ==== 提交代码开始 ====
    public int longestPalindrome(String s) {
        int[] count = new int[128];
        boolean hasSingle = false;
        for (char c : s.toCharArray()) {
            count[c]++;
        }
        int len = 0;
        for (int j : count) {
            if (j % 2 == 0) {
                len += j;
            } else {
                len += j - 1;
                hasSingle = true;
            }
        }
        return hasSingle ? len + 1 : len;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0409_LongestPalindrome s = new LC0409_LongestPalindrome();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(7, s.longestPalindrome("abccccdd"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.longestPalindrome("a"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 题目约束内的补充边界用例（1 <= s.length <= 2000，只含英文字母）----
        try {
            if (!TestUtil.checkEq(4, s.longestPalindrome("zzzz"), "边界1 全部字符相同且次数为偶数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.longestPalindrome("abc"), "边界2 多个字符都只出现一次")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.longestPalindrome("aaabbc"), "边界3 奇数频次字符的成对部分也可使用")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.longestPalindrome("Aa"), "边界4 大小写字符区分")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.longestPalindrome("AZaz"), "边界5 大小写字母范围端点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
