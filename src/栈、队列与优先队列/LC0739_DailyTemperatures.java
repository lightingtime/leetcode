// ============================================================
// LeetCode 739. 每日温度 (Daily Temperatures)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/daily-temperatures/
// 刷题日期：2026-08-17
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0739_DailyTemperatures {

    // ==== 提交代码开始 ====
    public int[] dailyTemperatures(int[] temperatures) {
        // TODO: 在这里实现你的解法
        int[] ans = new int[temperatures.length];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < temperatures.length; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peekLast()]) {
                Integer topIndex = stack.removeLast();
                ans[topIndex] = i - topIndex;
            }
            if (stack.isEmpty() || temperatures[stack.peekLast()] >= temperatures[i]) {
                stack.offerLast(i);
            }
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0739_DailyTemperatures s = new LC0739_DailyTemperatures();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, s.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 1, 1, 0}, s.dailyTemperatures(new int[]{30, 40, 50, 60}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 1, 0}, s.dailyTemperatures(new int[]{30, 60, 90}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq(new int[]{2, 1, 0}, s.dailyTemperatures(new int[]{73, 73, 74}), "相等温度")) failures++; } catch (Throwable t) { failures++; System.out.println("相等温度 异常: " + t); }
        try { if (!TestUtil.checkEq(new int[]{1, 2, 1, 0}, s.dailyTemperatures(new int[]{73, 74, 74, 75}), "相等温度后更高")) failures++; } catch (Throwable t) { failures++; System.out.println("相等温度后更高 异常: " + t); }
        try { if (!TestUtil.checkEq(new int[]{0}, s.dailyTemperatures(new int[]{30}), "单元素")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素 异常: " + t); }
        try { if (!TestUtil.checkEq(new int[]{0, 0, 0, 0, 0}, s.dailyTemperatures(new int[]{5, 4, 3, 2, 1}), "递减无更高")) failures++; } catch (Throwable t) { failures++; System.out.println("递减无更高 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}