// ============================================================
// LeetCode 394. 字符串解码 (Decode String)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/decode-string/
// 刷题日期：2026-08-17
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0394_DecodeString {

    // ==== 提交代码开始 ====
    public String decodeString(String s) {
        StringBuilder res = new StringBuilder();
        Deque<Integer> num = new LinkedList<>();
        Deque<String> subStr = new LinkedList<>();
        int multi = 0;
        for (char c : s.toCharArray()) {
            if (c == '[') {
                num.addLast(multi);
                subStr.addLast(res.toString());
                multi = 0;
                res = new StringBuilder();
            } else if (c == ']') {
                StringBuilder temp = new StringBuilder();
                int mul = num.removeLast();
                for (int i = 0; i < mul; i++) {
                    temp.append(res);
                }
                res = new StringBuilder(subStr.removeLast() + temp);
            } else if (Character.isDigit(c)) {
                multi = multi * 10 + Integer.parseInt(c + "");
            } else {
                res.append(c);
            }
        }
        return res.toString();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0394_DecodeString s = new LC0394_DecodeString();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("aaabcbc", s.decodeString("3[a]2[bc]"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("accaccacc", s.decodeString("3[a2[c]]"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq("abcabccdcdcdef", s.decodeString("2[abc]3[cd]ef"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }
        try {
            if (!TestUtil.checkEq("abccdcdcdxyz", s.decodeString("abc3[cd]xyz"), "示例4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例4 异常: " + t); }

                // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq("abc", s.decodeString("abc"), "纯字母无括号")) failures++; } catch (Throwable t) { failures++; System.out.println("纯字母无括号 异常: " + t); }
        try { if (!TestUtil.checkEq("a", s.decodeString("a"), "单字符")) failures++; } catch (Throwable t) { failures++; System.out.println("单字符 异常: " + t); }
        try { if (!TestUtil.checkEq("a", s.decodeString("1[a]"), "k=1最简")) failures++; } catch (Throwable t) { failures++; System.out.println("k=1最简 异常: " + t); }
        try { if (!TestUtil.checkEq("aaaa", s.decodeString("2[2[a]]"), "连续嵌套")) failures++; } catch (Throwable t) { failures++; System.out.println("连续嵌套 异常: " + t); }
        try { if (!TestUtil.checkEq("aaaaaaaaaa", s.decodeString("10[a]"), "多位数k")) failures++; } catch (Throwable t) { failures++; System.out.println("多位数k 异常: " + t); }
        try { if (!TestUtil.checkEq("aabb", s.decodeString("2[a]2[b]"), "相邻块")) failures++; } catch (Throwable t) { failures++; System.out.println("相邻块 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}