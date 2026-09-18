// ============================================================
// LeetCode 155. 最小栈 (Min Stack)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/min-stack/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-07 · 一刷一次 Accepted）
// 一刷写法：单栈 + 节点自带「入栈时的最小値」——push 时把 min(value, 栈顶节点的 min) 一起存进新节点，getMin 直接读栈顶节点的 min 字段；push/pop/top/getMin 全部 O(1)，空间 O(n)
// 本题易错点：① 只用 Stack/Deque 存值的话 getMin 得 O(n) 扫描，不满足「每个操作 O(1)」的要求；② 用「主栈 + 辅助最小栈」两栈写法时，弹出必须同步维护辅助栈（只有弹出的正是当前最小值才弹它），否则最小值提前丢失；③ 重复最小值要一起处理（边界3 专门测这个）；④ 空栈时 pop/top/getMin 的行为
// 测试用例与一刷归档保持一致（示例 1 个 + 边界 6 个：单元素与后压更大值/新最小后 pop 回退/重复最小值/降序压栈逐层回退/升序压栈最小在底/int 极值）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
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
                stack.push(new Node(value, Math.min(value, stack.peek().min)));
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
    }

    static class Node {
        int cur;
        int min;

        public Node() {
        }

        public Node(int cur, int min) {
            this.cur = cur;
            this.min = min;
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
