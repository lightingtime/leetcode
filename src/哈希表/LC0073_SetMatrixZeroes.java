// ============================================================
// LeetCode 73. 矩阵置零 (Set Matrix Zeroes)
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/set-matrix-zeroes/
// 刷题日期：2026-08-21
//
// ============================================================

import java.util.*;

public class LC0073_SetMatrixZeroes {

    // ==== 提交代码开始 ====
    public void setZeroes(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        boolean flagCol0 = false, flagRow0 = false;
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] == 0) {
                flagCol0 = true;
                break;
            }
        }
        for (int j = 0; j < n; j++) {
            if (matrix[0][j] == 0) {
                flagRow0 = true;
                break;
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 0) {
                    matrix[0][j] = 0;
                    matrix[i][0] = 0;
                }
            }
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (matrix[i][0] == 0 || matrix[0][j] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        if (flagCol0) {
            for (int i = 0; i < m; i++) {
                matrix[i][0] = 0;
            }

        }
        if (flagRow0) {
            for (int j = 0; j < n; j++) {
                matrix[0][j] = 0;
            }
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0073_SetMatrixZeroes s = new LC0073_SetMatrixZeroes();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            int[][] nums = new int[][]{new int[]{1, 1, 1}, new int[]{1, 0, 1}, new int[]{1, 1, 1}};
            s.setZeroes(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 0, 1}, new int[]{0, 0, 0}, new int[]{1, 0, 1}}, nums, "示例1"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            int[][] nums = new int[][]{new int[]{0, 1, 2, 0}, new int[]{3, 4, 5, 2}, new int[]{1, 3, 1, 5}};
            s.setZeroes(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{0, 0, 0, 0}, new int[]{0, 4, 5, 0}, new int[]{0, 3, 1, 0}}, nums, "示例2"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        // 边界1: 单元素矩阵 [0]，0 的行列都是它自己
        try {
            int[][] m1 = new int[][]{{0}};
            s.setZeroes(m1);
            if (!TestUtil.checkEq(new int[][]{{0}}, m1, "边界1: 单元素0")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }

        // 边界2: 单元素矩阵非 0，不变
        try {
            int[][] m2 = new int[][]{{5}};
            s.setZeroes(m2);
            if (!TestUtil.checkEq(new int[][]{{5}}, m2, "边界2: 单元素非0")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }

        // 边界3: 单行，0 使整行变 0
        try {
            int[][] m3 = new int[][]{{0, 1, 2}};
            s.setZeroes(m3);
            if (!TestUtil.checkEq(new int[][]{{0, 0, 0}}, m3, "边界3: 单行")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }

        // 边界4: 单列，0 使整列变 0
        try {
            int[][] m4 = new int[][]{{1}, {0}, {2}};
            s.setZeroes(m4);
            if (!TestUtil.checkEq(new int[][]{{0}, {0}, {0}}, m4, "边界4: 单列")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }

        // 边界5: 0 在 (0,0)，只有第 0 行和第 0 列被清，角落 (1,1) 保留
        try {
            int[][] m5 = new int[][]{{0, 1}, {2, 3}};
            s.setZeroes(m5);
            if (!TestUtil.checkEq(new int[][]{{0, 0}, {0, 3}}, m5, "边界5: 角0")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }

        // 边界6: 两个 0 分别在 (0,1) 与 (2,2)：行0/行2/列1/列2 全清，(1,0)=4 不在任何 0 行/列故保留
        try {
            int[][] m6 = new int[][]{{1, 0, 3}, {4, 5, 6}, {7, 8, 0}};
            s.setZeroes(m6);
            if (!TestUtil.checkEq(new int[][]{{0, 0, 0}, {4, 0, 0}, {0, 0, 0}}, m6, "边界6: 双0十字")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6 异常: " + t);
        }

        // 边界7: 输入含负数（-1），0 检测不能误伤负数
        try {
            int[][] m7 = new int[][]{{-1, 0}, {-2, -3}};
            s.setZeroes(m7);
            if (!TestUtil.checkEq(new int[][]{{0, 0}, {-2, 0}}, m7, "边界7: 含负数")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界7 异常: " + t);
        }

        // 边界8: 全 0 矩阵，结果仍全 0
        try {
            int[][] m8 = new int[][]{{0, 0}, {0, 0}};
            s.setZeroes(m8);
            if (!TestUtil.checkEq(new int[][]{{0, 0}, {0, 0}}, m8, "边界8: 全0")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界8 异常: " + t);
        }

        // 边界9: 无 0 矩阵，完全不变
        try {
            int[][] m9 = new int[][]{{1, 2}, {3, 4}};
            s.setZeroes(m9);
            if (!TestUtil.checkEq(new int[][]{{1, 2}, {3, 4}}, m9, "边界9: 无0")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界9 异常: " + t);
        }

        // 边界10: 200x200 上限，唯一 0 在 (0,0)，第 0 行/列全 0，其余保留 1
        try {
            int[][] big = new int[200][200];
            for (int i = 0; i < 200; i++) for (int j = 0; j < 200; j++) big[i][j] = 1;
            big[0][0] = 0;
            s.setZeroes(big);
            boolean bigOk = true;
            for (int j = 0; j < 200; j++) if (big[0][j] != 0) bigOk = false;
            for (int i = 0; i < 200; i++) if (big[i][0] != 0) bigOk = false;
            for (int i = 1; i < 200 && bigOk; i++) for (int j = 1; j < 200; j++) if (big[i][j] != 1) bigOk = false;
            if (!bigOk) {
                failures++;
                System.out.println("边界10: 200x200 角0 失败 ✗");
            } else System.out.println("边界10: 200x200 角0 通过 ✓");
        } catch (Throwable t) {
            failures++;
            System.out.println("边界10 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}