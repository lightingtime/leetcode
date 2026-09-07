// ============================================================
// LeetCode 85. 最大矩形 (Maximal Rectangle)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/maximal-rectangle/
// 二刷日期：2026-09-07（第 2 次复习 · 一刷 2026-08-20 · 上次 2026-09-06 较强）
// 一刷/上次思路：逐行高度 + 单调栈直方图（heights 哨兵 +2）
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0085_MaximalRectangle {

    // ==== 提交代码开始 ====
    public int maximalRectangle(char[][] matrix) {
        int[] heights = new int[matrix[0].length + 2];
        int maxArea = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i == 0) {
                    heights[j + 1] = matrix[i][j] == '1' ? 1 : 0;
                } else {
                    heights[j + 1] = matrix[i][j] == '1' ? (1 + heights[j + 1]): 0;
                }
            }

            Deque<Integer> stack = new ArrayDeque<>();
            for (int j = 0; j < heights.length; j++) {
                while (!stack.isEmpty() && heights[stack.peekLast()] > heights[j]) {
                    int height = heights[stack.pollLast()];
                    int width = j - stack.peekLast() - 1;
                    int area = height * width;
                    maxArea = Math.max(area, maxArea);
                }
                stack.offerLast(j);
            }

        }
        return maxArea;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0085_MaximalRectangle s = new LC0085_MaximalRectangle();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(6, s.maximalRectangle(new char[][]{new char[]{'1', '0', '1', '0', '0'}, new char[]{'1', '0', '1', '1', '1'}, new char[]{'1', '1', '1', '1', '1'}, new char[]{'1', '0', '0', '1', '0'}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maximalRectangle(new char[][]{new char[]{'0'}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.maximalRectangle(new char[][]{new char[]{'1'}}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 单行全 1：矩形横跨整行
        try {
            if (!TestUtil.checkEq(3, s.maximalRectangle(from("111")), "边界-单行全1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单行全1 异常: " + t); }
        // 单行中间有 0：最大只能是单个 1
        try {
            if (!TestUtil.checkEq(1, s.maximalRectangle(from("101")), "边界-单行断开")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单行断开 异常: " + t); }
        // 单列全 1：矩形竖跨整列
        try {
            if (!TestUtil.checkEq(3, s.maximalRectangle(from("1", "1", "1")), "边界-单列全1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单列全1 异常: " + t); }
        // 全 0：没有任何 1
        try {
            if (!TestUtil.checkEq(0, s.maximalRectangle(from("00", "00")), "边界-全0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全0 异常: " + t); }
        // 全 1 小矩阵：整个矩阵就是最大矩形
        try {
            if (!TestUtil.checkEq(4, s.maximalRectangle(from("11", "11")), "边界-全1 2x2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全1 2x2 异常: " + t); }
        // L 形：最大矩形是 2x1（第二行 + 第一列）
        try {
            if (!TestUtil.checkEq(2, s.maximalRectangle(from("10", "11")), "边界-L形")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-L形 异常: " + t); }
        // 最大矩形不在最底行：底行全 0，最大矩形来自上面的全 1 两行
        try {
            if (!TestUtil.checkEq(4, s.maximalRectangle(from("11", "11", "00")), "边界-上移矩形")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-上移矩形 异常: " + t); }
        // 上下两段被全 0 行隔开：不能跨过 0 合并
        try {
            if (!TestUtil.checkEq(2, s.maximalRectangle(from("11", "00", "11")), "边界-隔开两段")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-隔开两段 异常: " + t); }
        // 宽度不一致的连续 1：取能同时覆盖的最大宽
        try {
            if (!TestUtil.checkEq(3, s.maximalRectangle(from("111", "100")), "边界-宽度不一致")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-宽度不一致 异常: " + t); }
        // 大矩阵全 1：验证大输入与面积累计
        try {
            String[] rows = new String[30];
            java.util.Arrays.fill(rows, "1".repeat(30));
            if (!TestUtil.checkEq(900, s.maximalRectangle(from(rows)), "边界-30x30全1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-30x30全1 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 本地测试辅助：字符串数组 → char[][]
    private static char[][] from(String... rows) {
        char[][] m = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) m[i] = rows[i].toCharArray();
        return m;
    }

}