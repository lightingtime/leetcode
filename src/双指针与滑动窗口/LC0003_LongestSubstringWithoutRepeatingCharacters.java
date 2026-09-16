// ============================================================
// LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/longest-substring-without-repeating-characters/
// 复习日期：2026-09-16（第 1 次复习 · 一刷 2026-08-04）
// 一刷写法：滑动窗口 + 哈希表记位置（O(n) 时间，字符集大小的额外空间）
// 测试用例：示例 3 个 + 边界 9 个（原有 5 个，本次补齐：空格与标点 / 长度 5e4 周期串 / 重复在末尾 / 95 个可打印字符各不同）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0003_LongestSubstringWithoutRepeatingCharacters {

    // ==== 提交代码开始 ====
    public int lengthOfLongestSubstring(String s) {
        int[] lastSeen = new int[128];
        Arrays.fill(lastSeen, -1);
        int start = 0, max = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (lastSeen[c] >= start) {     // 上次出现还在当前窗口内 → 窗口左界跳到它后面
                start = lastSeen[c] + 1;
            }
            lastSeen[c] = i;
            max = Math.max(max, i - start + 1);   // [start, i] 此时一定合法，每轮都能结算
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0003_LongestSubstringWithoutRepeatingCharacters s = new LC0003_LongestSubstringWithoutRepeatingCharacters();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.lengthOfLongestSubstring("abcabcbb"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.lengthOfLongestSubstring("bbbbb"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.lengthOfLongestSubstring("pwwkew"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(0, s.lengthOfLongestSubstring(""), "边界1 空串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.lengthOfLongestSubstring("a"), "边界2 单字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.lengthOfLongestSubstring("aaaa"), "边界3 全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.lengthOfLongestSubstring("abba"), "边界4 重复后跳跃")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.lengthOfLongestSubstring("dvdf"), "边界5 跳跃式最长")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.lengthOfLongestSubstring("a b!a"), "边界6 空格与标点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            StringBuilder sb = new StringBuilder();
            String cycle = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            for (int i = 0; i < 50000; i++) sb.append(cycle.charAt(i % cycle.length()));
            if (!TestUtil.checkEq(cycle.length(), s.lengthOfLongestSubstring(sb.toString()), "边界7 长度 5e4 周期串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.lengthOfLongestSubstring("abcdefa"), "边界8 重复落在末尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            StringBuilder sb = new StringBuilder();
            for (char c = 32; c < 127; c++) sb.append(c);     // 95 个可打印字符，互不相同
            if (!TestUtil.checkEq(95, s.lengthOfLongestSubstring(sb.toString()), "边界9 95 个可打印字符各不同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
