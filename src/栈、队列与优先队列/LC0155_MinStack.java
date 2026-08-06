// ============================================================
// LeetCode 155. 最小栈 (Min Stack)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/min-stack/
// 刷题日期：2026-08-07
// ============================================================

import java.util.*;

public class LC0155_MinStack {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 MinStack 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class MinStack {
        Deque<Node> stack;
        public MinStack() {
            stack = new ArrayDeque<>();
        }
        public void push(int value) {
            if (stack.isEmpty()) {
                stack.push(new Node(value, value));
            } else {
                int min = Math.min(value, getMin());
                stack.push(new Node(value, min));
            }
        }
        public void pop() {
            stack.pop();
        }
        public int top() {
            return stack.peek().cur;
        }
        public int getMin() {
            return stack.peek().min;
        }

        class Node {
            int cur;
            int min;

            public Node() {
            }

            public Node(int cur, int min) {
                this.cur = cur;
                this.min = min;
            }
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 示例测试（题目示例）----
        try {
            MinStack s = new MinStack();
            s.push(-2);
            s.push(0);
            s.push(-3);
            if (s.getMin() != -3) failures++;
            s.pop();
            if (s.top() != 0) failures++;
            if (s.getMin() != -2) failures++;
            System.out.println("示例-标准操作序列 通过");
        } catch (Throwable t) { failures++; System.out.println("示例 异常: " + t); }

        // ---- 边界测试 ----
        try {
            MinStack s = new MinStack();
            s.push(5);
            if (s.top() != 5) failures++;
            if (s.getMin() != 5) failures++;
            s.push(6);
            if (s.getMin() != 5) failures++;
            s.pop();
            if (s.getMin() != 5) failures++;
            s.pop();
            System.out.println("边界1-单元素与后压更大值 通过");
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            MinStack s = new MinStack();
            s.push(3);
            s.push(1);
            s.push(2);
            if (s.getMin() != 1) failures++;
            s.pop();
            if (s.getMin() != 1) failures++;
            s.pop();
            if (s.getMin() != 3) failures++;
            s.pop();
            System.out.println("边界2-新最小后 pop 回退 通过");
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            MinStack s = new MinStack();
            s.push(1);
            s.push(1);
            s.push(1);
            if (s.getMin() != 1) failures++;
            s.pop();
            if (s.getMin() != 1) failures++;
            s.pop();
            if (s.getMin() != 1) failures++;
            s.pop();
            System.out.println("边界3-重复最小值 通过");
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            MinStack s = new MinStack();
            s.push(7);
            s.push(6);
            s.push(5);
            if (s.getMin() != 5) failures++;
            s.pop();
            if (s.getMin() != 6) failures++;
            s.pop();
            if (s.getMin() != 7) failures++;
            s.pop();
            System.out.println("边界4-降序压栈逐层回退 通过");
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            MinStack s = new MinStack();
            s.push(5);
            s.push(6);
            s.push(7);
            if (s.getMin() != 5) failures++;
            s.pop();
            if (s.getMin() != 5) failures++;
            s.pop();
            if (s.getMin() != 5) failures++;
            s.pop();
            System.out.println("边界5-升序压栈最小在底 通过");
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            MinStack s = new MinStack();
            s.push(Integer.MIN_VALUE);
            s.push(Integer.MAX_VALUE);
            if (s.getMin() != Integer.MIN_VALUE) failures++;
            if (s.top() != Integer.MAX_VALUE) failures++;
            s.pop();
            if (s.getMin() != Integer.MIN_VALUE) failures++;
            s.pop();
            System.out.println("边界6-int 极值 通过");
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
