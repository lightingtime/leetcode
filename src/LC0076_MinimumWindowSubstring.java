// ============================================================
// LeetCode 76. 最小覆盖子串 (Minimum Window Substring)
// 难度：Hard | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/minimum-window-substring/
// 刷题日期：2026-08-05
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0076_MinimumWindowSubstring {

    // ==== 提交代码开始 ====
    public String minWindow(String s, String t) {
        // TODO: 在这里实现你的解法
        int[] need = new int[128];
        for (Character c : t.toCharArray()) {
            need[c]++;
        }
        int count = t.length();
        int start = 0;
        int i = 0;
        int min = Integer.MAX_VALUE;
        String ans = "";
        while (i < s.length()) {
            char c = s.charAt(i);
            // 该字符进入窗口前若仍缺，则未满足数 -1（先判后减）
            if (need[c] > 0) {
                count--;
            }
            need[c]--;
            if (count == 0) {
                // 先收缩：删掉左边多余字符，记录以 i 结尾的最短覆盖窗口
                while (need[s.charAt(start)] < 0) {
                    need[s.charAt(start)]++;
                    start++;
                }
                // 记录答案
                if (i - start + 1 < min) {
                    min = i - start + 1;
                    ans = s.substring(start, i + 1);
                }
                // 还回左边界关键字符（need+1、count+1、start+1），覆盖被破坏，窗口继续右扩
                need[s.charAt(start)]++;
                count++;
                start++;
            }
            i++;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0076_MinimumWindowSubstring s = new LC0076_MinimumWindowSubstring();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("BANC", s.minWindow("ADOBECODEBANC", "ABC"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("a", s.minWindow("a", "a"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq("", s.minWindow("a", "aa"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（滑动窗口：长度/位置/重复字符/大小写）----
        try {
            if (!TestUtil.checkEq("", s.minWindow("abc", "abcd"), "边界1-s比t短")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-s比t短 异常: " + t); }
        try {
            if (!TestUtil.checkEq("ABC", s.minWindow("ABCXYZ", "ABC"), "边界2-窗口在开头")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-窗口在开头 异常: " + t); }
        try {
            if (!TestUtil.checkEq("ABC", s.minWindow("XYZABC", "ABC"), "边界3-窗口在结尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-窗口在结尾 异常: " + t); }
        try {
            if (!TestUtil.checkEq("AAB", s.minWindow("AAAB", "AAB"), "边界4-t含重复字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-t含重复字符 异常: " + t); }
        try {
            if (!TestUtil.checkEq("AB", s.minWindow("abAB", "AB"), "边界5-大小写敏感")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-大小写敏感 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
