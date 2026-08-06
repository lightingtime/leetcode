// ============================================================
// LeetCode 84. 柱状图中最大的矩形 (Largest Rectangle in Histogram)
// 难度：Hard | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/largest-rectangle-in-histogram/
// 刷题日期：2026-08-07
// ============================================================

import java.util.*;

public class LC0084_LargestRectangleInHistogram {

    // ==== 提交代码开始 ====
    public int largestRectangleArea(int[] heights) {
        int[] newHeights = new int[heights.length + 2];
        System.arraycopy(heights, 0, newHeights, 1, heights.length);
        heights = newHeights;
        int max = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[stack.peekLast()] > heights[i]) {
                int height = heights[stack.pollLast()];
                int width = i - stack.peekLast() - 1;
                max = Math.max(max, height * width);
            }
            stack.offerLast(i);
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0084_LargestRectangleInHistogram s = new LC0084_LargestRectangleInHistogram();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(10, s.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.largestRectangleArea(new int[]{2, 4}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!TestUtil.checkEq(5, s.largestRectangleArea(new int[]{5}), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(9, s.largestRectangleArea(new int[]{1, 2, 3, 4, 5}), "边界2-递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(9, s.largestRectangleArea(new int[]{5, 4, 3, 2, 1}), "边界3-递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(12, s.largestRectangleArea(new int[]{3, 3, 3, 3}), "边界4-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.largestRectangleArea(new int[]{2, 0, 2}), "边界5-含零分隔")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.largestRectangleArea(new int[]{0, 0, 0}), "边界6-全零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(12, s.largestRectangleArea(new int[]{6, 4, 5, 2, 7}), "边界7-V形")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(20000, s.largestRectangleArea(new int[]{10000, 10000}), "边界8-极值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
