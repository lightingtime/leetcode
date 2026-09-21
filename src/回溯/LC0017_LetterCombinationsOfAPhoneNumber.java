// ============================================================
// LeetCode 17. 电话号码的字母组合 (Letter Combinations of a Phone Number)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/letter-combinations-of-a-phone-number/
// 刷题日期：2026-09-21
//
// 二刷第 6 次 · 一刷 2026-08-08 · 上次复习 2026-09-15（较强，间隔 5 天）
// 一刷思路：回溯 DFS——按下标逐位选字母，用 StringBuilder 拼接、返回前撤销；空输入要返回 [] 而不是 [""]
// 测试用例与一刷归档保持一致（示例 2 + 边界若干），未重新设计
// ============================================================

import java.util.*;

public class LC0017_LetterCombinationsOfAPhoneNumber {

    // ==== 提交代码开始 ====
    String[] numbers = new String[]{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
    List<String> ans;
    public List<String> letterCombinations(String digits) {
        ans = new ArrayList<>();
        // 空输入必须返回 []：不能指望递归自然收尾，那样会把空串当成一种组合返回 [""]
        if (digits.isEmpty()) {
            return ans;
        }
        dfs(digits, 0, new StringBuilder());
        return ans;
    }

    private void dfs(String digits, int i, StringBuilder sb) {
        if (i == digits.length()) {
            ans.add(sb.toString());
            return;
        }
        for (char c : numbers[digits.charAt(i) - '0'].toCharArray()) {
            sb.append(c);
            dfs(digits, i + 1, sb);
            // 撤销：兄弟分支必须基于干净的 sb，本层选的字母要原样摘掉
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0017_LetterCombinationsOfAPhoneNumber s = new LC0017_LetterCombinationsOfAPhoneNumber();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"), s.letterCombinations("23"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList("a", "b", "c"), s.letterCombinations("2"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（与一刷归档保持一致）----
        try {
            List<String> empty = s.letterCombinations("");
            if (empty.size() != 0) {
                StringBuilder shown = new StringBuilder("[");
                for (int i = 0; i < empty.size(); i++) {
                    if (i > 0) shown.append(", ");
                    shown.append('"').append(empty.get(i)).append('"');
                }
                shown.append(']');
                System.out.println("边界1 失败 ✗ 期望 size=0 []，实际 size=" + empty.size() + " " + shown);
                failures++;
            } else {
                System.out.println("边界1 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList("p", "q", "r", "s"), s.letterCombinations("7"), "边界2-四字母键7")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList("w", "x", "y", "z"), s.letterCombinations("9"), "边界3-四字母键9")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            List<String> expected22 = new ArrayList<>();
            for (char c1 : "abc".toCharArray()) {
                for (char c2 : "abc".toCharArray()) expected22.add("" + c1 + c2);
            }
            if (!TestUtil.checkEq(expected22, s.letterCombinations("22"), "边界4-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            List<String> expected234 = new ArrayList<>();
            for (char c1 : "abc".toCharArray()) {
                for (char c2 : "def".toCharArray()) {
                    for (char c3 : "ghi".toCharArray()) expected234.add("" + c1 + c2 + c3);
                }
            }
            if (!TestUtil.checkEq(expected234, s.letterCombinations("234"), "边界5-三个数字")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            List<String> expected27 = new ArrayList<>();
            for (char c1 : "abc".toCharArray()) {
                for (char c2 : "pqrs".toCharArray()) expected27.add("" + c1 + c2);
            }
            if (!TestUtil.checkEq(expected27, s.letterCombinations("27"), "边界6-混合四字母键")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
