// ============================================================
// LeetCode 301. 删除无效的括号 (Remove Invalid Parentheses)
// 难度：Hard | 分类：回溯
// 链接：https://leetcode.cn/problems/remove-invalid-parentheses/
// 刷题日期：2026-08-18
//
// ============================================================

import java.util.*;

public class LC0301_RemoveInvalidParentheses {

    // ==== 提交代码开始 ====
    List<String> ans;
    public List<String> removeInvalidParentheses(String s) {
        ans = new ArrayList<>();
        int balance = 0;
        int needR = 0, needL;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                if (balance == 0) {
                    needR++;
                } else {
                    balance--;
                }
            }
        }
        needL = balance;
        dfs(s, 0, needL, needR);
        return ans;
    }

    private void dfs(String cur, int index, int needL, int needR) {
        if (needL == 0 && needR == 0) {
            if (isValid(cur)) {
                ans.add(cur);
            }
            return;
        }

        for (int i = index; i < cur.length(); i++) {
            if (i > index && cur.charAt(i) == cur.charAt(i - 1)) {
                continue;
            }
            String newStr = cur.substring(0, i) + cur.substring(i + 1);
            if (cur.charAt(i) == '(' && needL > 0) {
                dfs(newStr, i, needL - 1, needR);
            }
            if (cur.charAt(i) == ')' && needR > 0) {
                dfs(newStr, i, needL, needR - 1);
            }
        }
    }

    private boolean isValid(String cur) {
        int remain = 0;
        for (char c : cur.toCharArray()) {
            if (c == '(') {
                remain++;
            } else if (c == ')') {
                if (remain == 0) {
                    return false;
                }
                remain--;
            }
        }
        return remain == 0;
    }


    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0301_RemoveInvalidParentheses s = new LC0301_RemoveInvalidParentheses();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList("(())()", "()()()"), s.removeInvalidParentheses("()())()"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList("(a())()", "(a)()()"), s.removeInvalidParentheses("(a)())()"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(""), s.removeInvalidParentheses(")("), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1-无括号字母串：本身已合法，无需删除
        try { if (!TestUtil.checkEqUnordered(Arrays.asList("abc"), s.removeInvalidParentheses("abc"), "边界1-无括号")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-无括号 异常: " + t); }
        // 边界2-已合法括号串：删除 0 个，原样返回
        try { if (!TestUtil.checkEqUnordered(Arrays.asList("()"), s.removeInvalidParentheses("()"), "边界2-已合法")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-已合法 异常: " + t); }
        // 边界3-全左括号：只能全部删光 -> [""]
        try { if (!TestUtil.checkEqUnordered(Arrays.asList(""), s.removeInvalidParentheses("((("), "边界3-全左括号")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3-全左括号 异常: " + t); }
        // 边界4-全右括号：只能全部删光 -> [""]
        try { if (!TestUtil.checkEqUnordered(Arrays.asList(""), s.removeInvalidParentheses(")))"), "边界4-全右括号")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-全右括号 异常: " + t); }
        // 边界5-两种删法得到同一结果：()(() 删第 3 或第 4 个 '(' 都是 "()()"，必须去重只留一个
        try { if (!TestUtil.checkEqUnordered(Arrays.asList("()()"), s.removeInvalidParentheses("()(()"), "边界5-删法去重")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5-删法去重 异常: " + t); }
        // 边界6-字母+两种不同删法：(a() 删第 1 个 '(' 得 "a()"，删第 3 个 '(' 得 "(a)"，两个都要
        try { if (!TestUtil.checkEqUnordered(Arrays.asList("(a)", "a()"), s.removeInvalidParentheses("(a()"), "边界6-字母与两种删法")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6-字母与两种删法 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}