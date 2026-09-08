// ============================================================
// LeetCode 150. 逆波兰表达式求值 (Evaluate Reverse Polish Notation)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/evaluate-reverse-polish-notation/
// 刷题日期：2026-09-07
//
// ============================================================

import java.util.*;

public class LC0150_EvaluateReversePolishNotation {

    // ==== 提交代码开始 ====
    public int evalRPN(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : tokens) {
            if (token.equals("+")) {
                int a = stack.pollLast();
                int b = stack.pollLast();
                stack.offerLast(a + b);
            } else if (token.equals("-")) {
                int b = stack.pollLast();
                int a = stack.pollLast();
                stack.offerLast(a - b);
            } else if (token.equals("*")) {
                int a = stack.pollLast();
                int b = stack.pollLast();
                stack.offerLast(a * b);
            } else if (token.equals("/")) {
                int b = stack.pollLast();
                int a = stack.pollLast();
                stack.offerLast(a / b);
            } else {
                stack.offerLast(Integer.parseInt(token));
            }
        }
        return stack.peek();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0150_EvaluateReversePolishNotation s = new LC0150_EvaluateReversePolishNotation();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(9, s.evalRPN(new String[]{"2", "1", "+", "3", "*"}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.evalRPN(new String[]{"4", "13", "5", "/", "+"}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(22, s.evalRPN(new String[]{"10", "6", "9", "3", "+", "-11", "*", "/", "*", "17", "+", "5", "+"}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 约束：1 <= tokens.length <= 10^4，token 为算符或 [-200,200] 整数，除法向零截断
        // 边界1: 单元素（只有数字）
        try {
            if (!TestUtil.checkEq(42, s.evalRPN(new String[]{"42"}), "边界1: 单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 负数除法向零截断（-7/2 = -3.5 → -3）
        try {
            if (!TestUtil.checkEq(-3, s.evalRPN(new String[]{"-7", "2", "/"}), "边界2: 负除正向零截断")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 正数除负数向零截断（7/-2 = -3.5 → -3）
        try {
            if (!TestUtil.checkEq(-3, s.evalRPN(new String[]{"7", "-2", "/"}), "边界3: 正除负向零截断")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 负数乘法
        try {
            if (!TestUtil.checkEq(22, s.evalRPN(new String[]{"-11", "-2", "*"}), "边界4: 负数乘法")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 大中间结果（200^4 = 1.6e9，接近 32 位上限，题目保证不溢出）
        try {
            if (!TestUtil.checkEq(1600000000, s.evalRPN(new String[]{"200", "200", "*", "200", "*", "200", "*"}), "边界5: 大中间结果")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: tokens 长度上限 10^4（5000 个 1 + 4999 个 + = 9999 tokens）
        try {
            String[] big = new String[9999];
            for (int i = 0; i < 5000; i++) big[i] = "1";
            for (int i = 5000; i < 9999; i++) big[i] = "+";
            if (!TestUtil.checkEq(5000, s.evalRPN(big), "边界6: 9999 tokens")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}