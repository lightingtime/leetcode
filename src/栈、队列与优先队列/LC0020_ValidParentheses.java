// ============================================================
// LeetCode 20. 有效的括号 (Valid Parentheses)
// 难度：Easy | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/valid-parentheses/
// 刷题日期：2026-08-07
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
