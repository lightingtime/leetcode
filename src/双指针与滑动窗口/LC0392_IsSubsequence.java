// ============================================================
// LeetCode 392. 判断子序列 (Is Subsequence)
// 难度：Easy | 分类：动态规划
// 链接：https://leetcode.cn/problems/is-subsequence/
// 刷题日期：2026-09-30
//
// ============================================================

import java.util.*;

public class LC0392_IsSubsequence {

    // ==== 提交代码开始 ====
    public boolean isSubsequence(String s, String t) {
        int i = 0, j = 0;
        while (j < t.length()) {
            if (i == s.length()) {
                return true;
            }
            if (s.charAt(i) == t.charAt(j)) {
                i++;
                j++;
            } else {
                j++;
            }
        }
        return i == s.length();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0392_IsSubsequence s = new LC0392_IsSubsequence();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.isSubsequence("abc", "ahbgdc"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isSubsequence("axc", "ahbgdc"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（覆盖空串、单字符、重复字符顺序）----
        try {
            if (!TestUtil.checkEq(true, s.isSubsequence("", "abc"), "空 s")) failures++;
        } catch (Throwable t) { failures++; System.out.println("空 s 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isSubsequence("a", ""), "空 t")) failures++;
        } catch (Throwable t) { failures++; System.out.println("空 t 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isSubsequence("b", "abc"), "单字符匹配")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单字符匹配异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isSubsequence("aab", "aaab"), "重复字符顺序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("重复字符顺序异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isSubsequence("aba", "baa"), "重复字符次序不符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("重复字符次序不符异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
