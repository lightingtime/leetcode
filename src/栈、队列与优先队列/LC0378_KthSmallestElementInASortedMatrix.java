// ============================================================
// LeetCode 378. 有序矩阵中第 K 小的元素 (Kth Smallest Element in a Sorted Matrix)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/kth-smallest-element-in-a-sorted-matrix/
// 刷题日期：2026-08-24
//
// ============================================================


public class LC0378_KthSmallestElementInASortedMatrix {

    // ==== 提交代码开始 ====
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int left = matrix[0][0];
        int right = matrix[n - 1][n - 1];
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (check(mid, matrix, n, k)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private boolean check(int target, int[][] matrix, int n, int k) {
        int j = 0;
        int i = n - 1;
        int count = 0;
        while (i >= 0 && j < n) {
            if (matrix[i][j] <= target) {
                count += i + 1;
                j++;
            } else {
                i--;
            }
        }
        return count >= k;
    }


    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0378_KthSmallestElementInASortedMatrix s = new LC0378_KthSmallestElementInASortedMatrix();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(13, s.kthSmallest(new int[][]{new int[]{1, 5, 9}, new int[]{10, 11, 13}, new int[]{12, 13, 15}}, 8), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-5, s.kthSmallest(new int[][]{new int[]{-5}}, 1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 约束：1 <= n <= 300，值 [-1e9,1e9]，行列非递减，1 <= k <= n^2
        // 边界1: k=1（最小）
        try {
            if (!TestUtil.checkEq(1, s.kthSmallest(new int[][]{{1, 2}, {3, 4}}, 1), "边界1: k=1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: k=n^2（最大）
        try {
            if (!TestUtil.checkEq(4, s.kthSmallest(new int[][]{{1, 2}, {3, 4}}, 4), "边界2: k=n^2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 全相同元素
        try {
            if (!TestUtil.checkEq(1, s.kthSmallest(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, 5), "边界3: 全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 负数值
        try {
            if (!TestUtil.checkEq(-5, s.kthSmallest(new int[][]{{-9, -7}, {-5, -3}}, 3), "边界4: 负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 值域边界 ±10^9
        try {
            if (!TestUtil.checkEq(-999999999, s.kthSmallest(new int[][]{{-1000000000, -999999999}, {1000000000, 1000000000}}, 2), "边界5: 值边界")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: n=300 上限，matrix[i][j]=i*300+j 严格递增，第 k 小 = k-1
        try {
            int n = 300;
            int[][] big = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) big[i][j] = i * n + j;
            }
            if (!TestUtil.checkEq(44999, s.kthSmallest(big, 45000), "边界6: 300x300中间k")) failures++;
            if (!TestUtil.checkEq(0, s.kthSmallest(big, 1), "边界6b: 300x300 k=1")) failures++;
            if (!TestUtil.checkEq(89999, s.kthSmallest(big, 90000), "边界6c: 300x300 k=n^2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}