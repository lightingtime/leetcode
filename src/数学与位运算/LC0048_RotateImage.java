// ============================================================
// LeetCode 48. 旋转图像 (Rotate Image)
// 难度：Medium | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/rotate-image/
// 刷题日期：2026-08-11
// ============================================================

import java.util.*;

public class LC0048_RotateImage {

    // ==== 提交代码开始 ====
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2; i++) {
            for (int j = 0; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - i - 1][j];
                matrix[n - i - 1][j] = temp;
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0048_RotateImage s = new LC0048_RotateImage();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            int[][] nums = new int[][]{new int[]{1, 2, 3}, new int[]{4, 5, 6}, new int[]{7, 8, 9}};
            s.rotate(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{7, 4, 1}, new int[]{8, 5, 2}, new int[]{9, 6, 3}}, nums, "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            int[][] nums = new int[][]{new int[]{5, 1, 9, 11}, new int[]{2, 4, 8, 10}, new int[]{13, 3, 6, 7}, new int[]{15, 14, 12, 16}};
            s.rotate(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{15, 13, 2, 5}, new int[]{14, 3, 4, 1}, new int[]{12, 6, 8, 9}, new int[]{16, 7, 10, 11}}, nums, "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            int[][] nums = new int[][]{new int[]{5}};
            s.rotate(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{5}}, nums, "单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单元素 异常: " + t); }
        try {
            int[][] nums = new int[][]{new int[]{1, 2}, new int[]{3, 4}};
            s.rotate(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{3, 1}, new int[]{4, 2}}, nums, "2x2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("2x2 异常: " + t); }
        try {
            int[][] nums = new int[][]{new int[]{-1, -2}, new int[]{-3, -4}};
            s.rotate(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{-3, -1}, new int[]{-4, -2}}, nums, "负数2x2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("负数2x2 异常: " + t); }
        try {
            int[][] nums = new int[][]{new int[]{1000, 1000}, new int[]{1000, 1000}};
            s.rotate(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{1000, 1000}, new int[]{1000, 1000}}, nums, "大数全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("大数全相同 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
