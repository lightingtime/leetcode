// ============================================================
// LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/longest-substring-without-repeating-characters/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-04 · 一刷一次 AC）
// 一刷写法：滑动窗口 + 哈希表记字符上次出现位置；上次复习已定型为 int[128] lastSeen（本题字符集是 ASCII，定长数组更快、空间固定）
// 要点：窗口 [start, i] 始终无重复；lastSeen[c] >= start 说明重复落在当前窗口内 → start 跳到 lastSeen[c] + 1；
//       每轮都能结算 i - start + 1（不必像一刷那样只在重复点延迟结算、循环外再补一次）
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 9 个，含 95 个可打印字符各不同）
// 上次复习留的三条精简已落地，本次照这个骨架写：命名 lastSeen（存的是上次下标）；去掉 != -1 的冗余判断；去掉重复分支里重复结算的 max
//
// 思路：滑动窗口 + lastSeen[c] 记字符上次出现的下标——窗口 [start, i] 始终无重复；若 lastSeen[c] >= start
//       （上次出现仍落在窗口内）就把 start 跳到 lastSeen[c] + 1，再登记 lastSeen[c] = i 并结算 i - start + 1
// 复杂度：时间 O(n) 空间 O(1)（int[128] 定长数组）
// ============================================================

import java.util.*;

public class LC0003_LongestSubstringWithoutRepeatingCharacters {

    // ==== 提交代码开始 ====
    public int lengthOfLongestSubstring(String s) {
        int[] lastSeen = new int[128];
        Arrays.fill(lastSeen, -1);
        int start = 0;
        int max = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (lastSeen[c] >= start) {
                start = lastSeen[c] + 1;
            }
            lastSeen[c] = i;
            max = Math.max(max, i - start + 1);
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
