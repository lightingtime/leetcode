// ============================================================
// LeetCode 54. 螺旋矩阵 (Spiral Matrix)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/spiral-matrix/
// 刷题日期：2026-08-23
//
// ============================================================

import java.util.*;

public class LC0054_SpiralMatrix {

    // ==== 提交代码开始 ====
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> ans = new ArrayList<>();
        int l = 0, r = matrix[0].length - 1;
        int u = 0, d = matrix.length - 1;
        while (l <= r && u <= d) {
            for (int i = l; u <= d && i <= r; i++) {
                ans.add(matrix[u][i]);
            }
            u++;
            for (int j = u; l <= r && j <= d; j++) {
                ans.add(matrix[j][r]);
            }
            r--;
            for (int i = r; u <= d && i >= l; i--) {
                ans.add(matrix[d][i]);
            }
            d--;
            for (int j = d; l<= r && j >= u; j--) {
                ans.add(matrix[j][l]);
            }
            l++;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0054_SpiralMatrix s = new LC0054_SpiralMatrix();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 3, 6, 9, 8, 7, 4, 5), s.spiralOrder(new int[][]{new int[]{1, 2, 3}, new int[]{4, 5, 6}, new int[]{7, 8, 9}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7), s.spiralOrder(new int[][]{new int[]{1, 2, 3, 4}, new int[]{5, 6, 7, 8}, new int[]{9, 10, 11, 12}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 单行：螺旋只剩向右
        try {
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 3, 4, 5), s.spiralOrder(new int[][]{new int[]{1, 2, 3, 4, 5}}), "边界1-单行")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单行 异常: " + t); }
        // 2) 单列：螺旋只剩向下
        try {
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 3, 4), s.spiralOrder(new int[][]{new int[]{1}, new int[]{2}, new int[]{3}, new int[]{4}}), "边界2-单列")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-单列 异常: " + t); }
        // 3) 单元素
        try {
            if (!TestUtil.checkEq(Arrays.asList(7), s.spiralOrder(new int[][]{new int[]{7}}), "边界3-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-单元素 异常: " + t); }
        // 4) 2x2：完整一圈后内圈为空，收尾最容易重复添加
        try {
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 4, 3), s.spiralOrder(new int[][]{new int[]{1, 2}, new int[]{3, 4}}), "边界4-2x2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-2x2 异常: " + t); }
        // 5) 3x2（宽<高）：竖列只走一次
        try {
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 4, 6, 5, 3), s.spiralOrder(new int[][]{new int[]{1, 2}, new int[]{3, 4}, new int[]{5, 6}}), "边界5-3x2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-3x2 异常: " + t); }
        // 6) 2x3（高<宽）：横列只走一次
        try {
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 3, 6, 5, 4), s.spiralOrder(new int[][]{new int[]{1, 2, 3}, new int[]{4, 5, 6}}), "边界6-2x3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-2x3 异常: " + t); }
        // 7) 负数与上下限值 -100 / 100
        try {
            if (!TestUtil.checkEq(Arrays.asList(-100, 100, -100, 100), s.spiralOrder(new int[][]{new int[]{-100, 100}, new int[]{100, -100}}), "边界7-上下限值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-上下限值 异常: " + t); }
        // 8) 全相同：值相同也必须每个位置恰好取一次
        try {
            if (!TestUtil.checkEq(Arrays.asList(5, 5, 5, 5, 5, 5, 5, 5, 5), s.spiralOrder(new int[][]{new int[]{5, 5, 5}, new int[]{5, 5, 5}, new int[]{5, 5, 5}}), "边界8-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8-全相同 异常: " + t); }
        // 9) 4x4 多圈：内圈 2x2 收尾，收缩边界时易重复/遗漏
        try {
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 3, 4, 8, 12, 16, 15, 14, 13, 9, 5, 6, 7, 11, 10), s.spiralOrder(new int[][]{new int[]{1, 2, 3, 4}, new int[]{5, 6, 7, 8}, new int[]{9, 10, 11, 12}, new int[]{13, 14, 15, 16}}), "边界9-4x4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9-4x4 异常: " + t); }
        // 10) 最大尺寸 10x10：验证不重不漏（结果长度 + 无序元素集合一致）
        try {
            int[][] big = new int[10][10];
            List<Integer> all = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    big[i][j] = i * 10 + j + 1;
                    all.add(i * 10 + j + 1);
                }
            }
            List<Integer> got = s.spiralOrder(big);
            boolean ok = got != null && got.size() == 100;
            if (ok) ok &= TestUtil.checkEqUnordered(all, got, "边界10a-10x10元素集");
            if (!ok) { failures++; System.out.println("边界10-10x10 失败（size=" + (got == null ? "null" : got.size()) + "）"); }
        } catch (Throwable t) { failures++; System.out.println("边界10-10x10 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
