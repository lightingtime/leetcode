// ============================================================
// LeetCode 20. 有效的括号 (Valid Parentheses)
// 难度：Easy | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/valid-parentheses/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-07 · 一刷一次 Accepted）
// 一刷写法：栈匹配（Deque 当栈）——遇左括号把「对应的右括号」压栈，遇右括号就弹栈比对，相等继续、不等直接 false；扫描完栈空才算有效。O(n)/O(n)
// 本题易错点：① 提前返回的两处：右括号来时栈已空（多余右括号）、弹栈元素与当前右括号不匹配；② 扫描结束后必须判栈空（还有左括号没闭合），漏掉会把 "(((" 判成有效；③ 压栈时压的是「配对的右括号」，这样弹出的比较就是一次相等判断，不用写两套映射分支
// 测试用例与一刷归档保持一致（示例 5 个 + 边界 7 个：单个左/右括号、全左未闭合、多余右括号、深层嵌套、正确嵌套、交叉错配）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0020_ValidParentheses {

    // ==== 提交代码开始 ====
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.offer(')');
            } else if (c == '{') {
                stack.offer('}');
            } else if (c == '[') {
                stack.offer(']');
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                if (stack.pollLast() != c) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0020_ValidParentheses s = new LC0020_ValidParentheses();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.isValid("()"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isValid("()[]{}"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isValid("(]"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isValid("([])"), "示例4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isValid("([)]"), "示例5")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例5 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!TestUtil.checkEq(false, s.isValid("("), "边界1-单个左括号")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isValid(")"), "边界2-单个右括号")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isValid("((("), "边界3-全左括号未闭合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isValid("())"), "边界4-多余右括号")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isValid("(((())))"), "边界5-深层嵌套")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isValid("{[]}"), "边界6-正确嵌套")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isValid("{[}]"), "边界7-交叉错配")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
