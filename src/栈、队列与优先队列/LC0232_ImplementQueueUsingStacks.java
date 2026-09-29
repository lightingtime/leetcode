// ============================================================
// LeetCode 232. 用栈实现队列 (Implement Queue using Stacks)
// 难度：Easy | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/implement-queue-using-stacks/
// 刷题日期：2026-09-30
//
// ============================================================

import java.util.*;

public class LC0232_ImplementQueueUsingStacks {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 MyQueue 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class MyQueue {
        Deque<Integer> stack1;
        Deque<Integer> stack2;
        public MyQueue() {
            stack1 = new ArrayDeque<>();
            stack2 = new ArrayDeque<>();
        }
        public void push(int x) {
            stack1.push(x);
        }
        public int pop() {
            if (stack2.isEmpty()) {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
            }
            return stack2.pop();
        }
        public int peek() {
            if (stack2.isEmpty()) {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
            }
            return stack2.peek();
        }
        public boolean empty() {
            return stack1.isEmpty() && stack2.isEmpty();
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        // 设计题：按题目示例手动构造调用序列，例如：
        // MyQueue s = new MyQueue(...);
        // s.method(...);
        // 题目原始示例输入：
        // ["MyQueue","push","push","peek","pop","empty"]
        // [[],[1],[2],[],[],[]]
        int failures = 0;

        // ---- 示例测试（题目示例）----
        try {
            MyQueue q = new MyQueue();
            q.push(1);
            q.push(2);
            if (q.peek() != 1) failures++;
            if (q.pop() != 1) failures++;
            if (q.empty()) failures++;
            if (q.peek() != 2) failures++;
            if (q.pop() != 2) failures++;
            if (!q.empty()) failures++;
            System.out.println("示例-FIFO顺序 通过");
        } catch (Throwable t) { failures++; System.out.println("示例-FIFO顺序 异常: " + t); }

        // ---- 边界测试：反复取队首、交错 push/pop，及题目允许的最大值 ----
        try {
            MyQueue q = new MyQueue();
            q.push(7);
            if (q.peek() != 7 || q.peek() != 7) failures++; // peek 不应移除元素
            q.push(8);
            if (q.pop() != 7) failures++;
            q.push(9);
            if (q.pop() != 8) failures++;
            if (q.pop() != 9) failures++;
            if (!q.empty()) failures++;
            System.out.println("边界-交错操作与重复peek 通过");
        } catch (Throwable t) { failures++; System.out.println("边界-交错操作与重复peek 异常: " + t); }
        try {
            MyQueue q = new MyQueue();
            q.push(1);
            q.push(9);
            if (q.pop() != 1 || q.pop() != 9 || !q.empty()) failures++;
            System.out.println("边界-值范围端点 通过");
        } catch (Throwable t) { failures++; System.out.println("边界-值范围端点 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
