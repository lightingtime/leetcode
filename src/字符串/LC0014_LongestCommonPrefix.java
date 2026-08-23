// ============================================================
// LeetCode 14. 最长公共前缀 (Longest Common Prefix)
// 难度：Easy | 分类：字符串
// 链接：https://leetcode.cn/problems/longest-common-prefix/
// 刷题日期：2026-08-23
//
// ============================================================

import java.util.*;

public class LC0014_LongestCommonPrefix {

    // ==== 提交代码开始 ====
    public String longestCommonPrefix(String[] strs) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index < strs[0].length()) {
            boolean allEquals = true;
            char c = strs[0].charAt(index);
            for (String str : strs) {
                if (index >= str.length()) {
                    allEquals = false;
                    break;
                }
                if (str.charAt(index) != c) {
                    allEquals = false;
                    break;
                }
            }
            index++;
            if (!allEquals) {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0014_LongestCommonPrefix s = new LC0014_LongestCommonPrefix();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("fl", s.longestCommonPrefix(new String[]{"flower", "flow", "flight"}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("", s.longestCommonPrefix(new String[]{"dog", "racecar", "car"}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 单元素：公共前缀就是它自身
        try {
            if (!TestUtil.checkEq("abc", s.longestCommonPrefix(new String[]{"abc"}), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单元素 异常: " + t); }
        // 2) 含空串：空串与任何串公共前缀为空
        try {
            if (!TestUtil.checkEq("", s.longestCommonPrefix(new String[]{"", "abc", "a"}), "边界2-含空串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-含空串 异常: " + t); }
        // 3) 仅一个空串
        try {
            if (!TestUtil.checkEq("", s.longestCommonPrefix(new String[]{""}), "边界3-单空串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-单空串 异常: " + t); }
        // 4) 全相同
        try {
            if (!TestUtil.checkEq("abc", s.longestCommonPrefix(new String[]{"abc", "abc", "abc"}), "边界4-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-全相同 异常: " + t); }
        // 5) 较短串是较长串的前缀：公共前缀被最短串限制
        try {
            if (!TestUtil.checkEq("ab", s.longestCommonPrefix(new String[]{"abc", "ab", "abd"}), "边界5-前缀包含")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-前缀包含 异常: " + t); }
        // 6) 首字符即不同 -> 空串
        try {
            if (!TestUtil.checkEq("", s.longestCommonPrefix(new String[]{"ab", "cd"}), "边界6-首字符不同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-首字符不同 异常: " + t); }
        // 7) 单字符公共前缀
        try {
            if (!TestUtil.checkEq("a", s.longestCommonPrefix(new String[]{"a", "ab", "ac"}), "边界7-单字符前缀")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-单字符前缀 异常: " + t); }
        // 8) 长串（长度上限 200 内）：前 150 相同后不同
        try {
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < 200; i++) sb1.append('a');
            for (int i = 0; i < 150; i++) sb2.append('a');
            for (int i = 0; i < 50; i++) sb2.append('b');
            String exp = "a".repeat(150);
            if (!TestUtil.checkEq(exp, s.longestCommonPrefix(new String[]{sb1.toString(), sb2.toString()}), "边界8-长串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8-长串 异常: " + t); }
        // 9) 数组长度上限附近（200 个相同串）
        try {
            String[] arr = new String[200];
            Arrays.fill(arr, "abc");
            if (!TestUtil.checkEq("abc", s.longestCommonPrefix(arr), "边界9-200个串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9-200个串 异常: " + t); }
        // 10) 长公共前缀被某个短串截断：公共前缀恰为中间某串整体
        try {
            if (!TestUtil.checkEq("abcd", s.longestCommonPrefix(new String[]{"abcdef", "abcdxyz", "abcd", "abcdpqr"}), "边界10-短串截断")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10-短串截断 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
