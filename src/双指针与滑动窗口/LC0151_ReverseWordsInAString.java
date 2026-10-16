// ============================================================
// LeetCode 151. 反转字符串中的单词 (Reverse Words in a String)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/reverse-words-in-a-string/
// 刷题日期：2026-10-16
//
// ============================================================

public class LC0151_ReverseWordsInAString {

    // ==== 提交代码开始 ====
    public String reverseWords(String s) {
        s = new StringBuilder(s.trim()).reverse().toString();
        int n = s.length();
        StringBuilder ans = new StringBuilder();
        int index = 0;
        while (index < n) {
            if (s.charAt(index) == ' ') {
                ans.append(' ');
            }
            while (index < n && s.charAt(index) == ' ') {
                index++;
            }
            int start = index;
            while (index < n && Character.isLetterOrDigit(s.charAt(index))) {
                index++;
            }
            for (int i = index - 1; i >= start; i--) {
                ans.append(s.charAt(i));
            }
        }
        return ans.toString();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0151_ReverseWordsInAString s = new LC0151_ReverseWordsInAString();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("blue is sky the", s.reverseWords("the sky is blue"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("world hello", s.reverseWords("  hello world  "), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq("example good a", s.reverseWords("a good   example"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（至少一个单词，长度不超过 10000）----
        try {
            if (!TestUtil.checkEq("a", s.reverseWords("a"), "长度下限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("长度下限 异常: " + t); }
        try {
            if (!TestUtil.checkEq("Solo", s.reverseWords("  Solo  "), "单个单词带首尾空格")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单个单词带首尾空格 异常: " + t); }
        try {
            if (!TestUtil.checkEq("b a", s.reverseWords("a b"), "两个单字符单词")) failures++;
        } catch (Throwable t) { failures++; System.out.println("两个单字符单词 异常: " + t); }
        try {
            if (!TestUtil.checkEq("c b a", s.reverseWords("  a   b  c   "), "首尾及连续空格")) failures++;
        } catch (Throwable t) { failures++; System.out.println("首尾及连续空格 异常: " + t); }
        try {
            if (!TestUtil.checkEq("007 cD3 Ab12", s.reverseWords("Ab12 cD3 007"), "保留大小写和数字")) failures++;
        } catch (Throwable t) { failures++; System.out.println("保留大小写和数字 异常: " + t); }
        try {
            if (!TestUtil.checkEq("same same", s.reverseWords("same same"), "重复单词")) failures++;
        } catch (Throwable t) { failures++; System.out.println("重复单词 异常: " + t); }
        try {
            if (!TestUtil.checkEq("cd ab", s.reverseWords(" ab cd "), "只反转单词顺序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("只反转单词顺序 异常: " + t); }
        try {
            if (!TestUtil.checkEq("x".repeat(10000), s.reverseWords("x".repeat(10000)), "长度上限单个长单词")) failures++;
        } catch (Throwable t) { failures++; System.out.println("长度上限单个长单词 异常: " + t); }
        try {
            if (!TestUtil.checkEq("z", s.reverseWords(" ".repeat(9999) + "z"), "长度上限前导空格")) failures++;
        } catch (Throwable t) { failures++; System.out.println("长度上限前导空格 异常: " + t); }
        try {
            if (!TestUtil.checkEq("b" + " a".repeat(4999), s.reverseWords("a ".repeat(4999) + "b "), "长度上限多个单词")) failures++;
        } catch (Throwable t) { failures++; System.out.println("长度上限多个单词 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}