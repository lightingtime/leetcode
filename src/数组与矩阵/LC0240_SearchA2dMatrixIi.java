// ============================================================
// LeetCode 240. 搜索二维矩阵 II (Search a 2D Matrix II)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/search-a-2d-matrix-ii/
// 刷题日期：2026-08-06
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0240_SearchA2dMatrixIi {

    // ==== 提交代码开始 ====
    public boolean searchMatrix(int[][] matrix, int target) {
        // TODO: 在这里实现你的解法
        if (matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        int m = matrix.length;
        int n = matrix[0].length;

        int x = 0, y = n - 1;
        while (x >= 0 && x < m && y >= 0 && y < n) {
            if (matrix[x][y] == target) {
                return true;
            } else if (matrix[x][y] > target) {
                y--;
            } else {
                x++;
            }
        }
        return false;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0240_SearchA2dMatrixIi s = new LC0240_SearchA2dMatrixIi();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.searchMatrix(new int[][]{new int[]{1, 4, 7, 11, 15}, new int[]{2, 5, 8, 12, 19}, new int[]{3, 6, 9, 16, 22}, new int[]{10, 13, 14, 17, 24}, new int[]{18, 21, 23, 26, 30}}, 5), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.searchMatrix(new int[][]{new int[]{1, 4, 7, 11, 15}, new int[]{2, 5, 8, 12, 19}, new int[]{3, 6, 9, 16, 22}, new int[]{10, 13, 14, 17, 24}, new int[]{18, 21, 23, 26, 30}}, 20), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(false, s.searchMatrix(new int[0][0], 1), "空矩阵")) failures++;
        } catch (Throwable t) { failures++; System.out.println("空矩阵 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.searchMatrix(new int[][]{new int[0]}, 1), "空行")) failures++;
        } catch (Throwable t) { failures++; System.out.println("空行 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.searchMatrix(new int[][]{new int[]{5}}, 5), "单元素命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单元素命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.searchMatrix(new int[][]{new int[]{5}}, 3), "单元素未命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单元素未命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.searchMatrix(new int[][]{new int[]{1, 3, 5}}, 3), "单行命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单行命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.searchMatrix(new int[][]{new int[]{1, 3, 5}}, 4), "单行未命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单行未命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.searchMatrix(new int[][]{new int[]{1}, new int[]{3}, new int[]{5}}, 3), "单列命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单列命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.searchMatrix(new int[][]{new int[]{1, 2}, new int[]{3, 4}}, 0), "小于最小")) failures++;
        } catch (Throwable t) { failures++; System.out.println("小于最小 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.searchMatrix(new int[][]{new int[]{1, 2}, new int[]{3, 4}}, 5), "大于最大")) failures++;
        } catch (Throwable t) { failures++; System.out.println("大于最大 异常: " + t); }
        try {
            int[][] corners = new int[][]{new int[]{1, 2, 3}, new int[]{4, 5, 6}, new int[]{7, 8, 9}};
            if (!TestUtil.checkEq(true, s.searchMatrix(corners, 1), "左上角")) failures++;
            if (!TestUtil.checkEq(true, s.searchMatrix(corners, 3), "右上角")) failures++;
            if (!TestUtil.checkEq(true, s.searchMatrix(corners, 7), "左下角")) failures++;
            if (!TestUtil.checkEq(true, s.searchMatrix(corners, 9), "右下角")) failures++;
        } catch (Throwable t) { failures++; System.out.println("四角 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.searchMatrix(new int[][]{new int[]{-5, -3}, new int[]{-2, -1}}, -3), "负数命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("负数命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.searchMatrix(new int[][]{new int[]{-5, -3}, new int[]{-2, -1}}, -4), "负数未命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("负数未命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.searchMatrix(new int[][]{new int[]{1, 2, 2, 3}, new int[]{2, 3, 4, 5}}, 2), "重复值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("重复值 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
