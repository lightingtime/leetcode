// ============================================================
// LeetCode 76. 最小覆盖子串 (Minimum Window Substring)
// 难度：Hard | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/minimum-window-substring/
// 复习日期：2026-09-16（第 1 次复习 · 一刷 2026-08-05）
// 一刷写法：滑动窗口 + 128 计数（扩展时先判后减、收缩只删多余）
// 上次遗留提醒（本次要求达到）：写法微优化——`for (Character c : ...)` 逐个装箱，改用 `for (char c : ...)`
// 或按 charAt 下标访问，避免装箱（复杂度不变）
// 测试用例：示例 3 个 + 边界 9 个（原有 5 个 + 本次补齐：长度 1e5 末尾命中 / 大小写混合 / 重复字符精确计数 /
// 短窗口不一定是最后一次记录）
// ============================================================

import java.util.*;

public class LC0076_MinimumWindowSubstring {

    // ==== 提交代码开始 ====
    public String minWindow(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray()) {
            need[c]++;
        }
        int count = t.length();
        String ans = "";
        int start = 0;
        int i = 0;
        int min = Integer.MAX_VALUE;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (need[c] > 0) {
                count--;
            }
            need[c]--;
            if (count == 0) {
                while (need[s.charAt(start)] < 0) {
                    need[s.charAt(start)]++;
                    start++;
                }
                if (i - start + 1 < min) {
                    min = i - start + 1;
                    ans = s.substring(start, i + 1);
                }
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
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 99999; i++) sb.append('a');
            sb.append('b');                                    // 目标字符只出现在末尾
            if (!TestUtil.checkEq("b", s.minWindow(sb.toString(), "b"), "边界6-长度 1e5 末尾命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq("aA", s.minWindow("aA", "aA"), "边界7-大小写混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq("abbbc", s.minWindow("aaabbbccc", "abc"), "边界8-重复字符精确计数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            // 更短的窗口出现在更长的窗口之后被记录的场景
            if (!TestUtil.checkEq("ab", s.minWindow("xxabxxa", "ab"), "边界9-短窗口不一定是最后一次记录")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
