// ============================================================
// LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/longest-substring-without-repeating-characters/
// 刷题日期：2026-08-04
//
// ============================================================

import java.util.*;

public class LC0003_LongestSubstringWithoutRepeatingCharacters {

    // ==== 提交代码开始 ====
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int max = 0;
        int start = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!map.containsKey(c) || map.get(c) < start) {
                map.put(c, i);
            } else {
                max = Math.max(max, i - start);
                start = map.get(c) + 1;
                map.put(c, i);
            }
        }
        max = Math.max(max, s.length() - start);
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

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
