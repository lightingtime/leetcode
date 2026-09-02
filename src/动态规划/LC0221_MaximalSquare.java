// ============================================================
// LeetCode 221. 最大正方形 (Maximal Square)
// 难度：Medium | 分类：动态规划（子类型：线性/网格 DP）
// 链接：https://leetcode.cn/problems/maximal-square/
// 复习日期：2026-09-01（复习 · 一刷 2026-08-19）
// 一刷思路：DP 三邻居 min+1（dp[i][j] = 以 (i,j) 为右下角的最大正方形边长），O(mn)/O(mn)
// 二刷提示：空间可滚动到 O(n)（只依赖上一行 + 本行左 + 左上，同 LC62/LC72 套路）
// 测试用例与一刷归档保持一致（示例 + WA 回归 + 边界）
// ============================================================


public class LC0221_MaximalSquare {

    // ==== 提交代码开始 ====
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[] dp = new int[n];
        int max = 0;
        for (int i = 0; i < m; i++) {
            int dig = dp[0];
            for (int j = 0; j < n; j++) {
                int pre = dp[j];
                if (matrix[i][j] == '0') {
                    dp[j] = 0;
                } else {
                    if (i > 0 && j > 0) {
                        dp[j] = Math.min(dp[j - 1], Math.min(pre, dig)) + 1;
                    } else {
                        dp[j] = 1;
                    }
                }
                dig = pre;
                max = Math.max(max, dp[j]);
            }
        }
        return max * max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0221_MaximalSquare s = new LC0221_MaximalSquare();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{new char[]{'1', '0', '1', '0', '0'}, new char[]{'1', '0', '1', '1', '1'}, new char[]{'1', '1', '1', '1', '1'}, new char[]{'1', '0', '0', '1', '0'}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'0', '1'}, new char[]{'1', '0'}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maximalSquare(new char[][]{new char[]{'0'}}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- WA 回归用例（力扣失败输入）----
        try { if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{
                new char[]{'1','0','1','1','0','1'}, new char[]{'1','1','1','1','1','1'},
                new char[]{'0','1','1','0','1','1'}, new char[]{'1','1','1','0','1','0'},
                new char[]{'0','1','1','1','1','1'}, new char[]{'1','1','0','1','1','1'}}), "WA回归6x6")) failures++; } catch (Throwable t) { failures++; System.out.println("WA回归6x6 异常: " + t); }

        // ---- 边界测试（与一刷归档保持一致）----
        // 单元素：'1' -> 1，'0' -> 0
        try { if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'1'}}), "单元素1")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素1 异常: " + t); }
        // 全零：无 1，面积为 0
        try { if (!TestUtil.checkEq(0, s.maximalSquare(new char[][]{new char[]{'0', '0'}, new char[]{'0', '0'}}), "全零2x2")) failures++; } catch (Throwable t) { failures++; System.out.println("全零2x2 异常: " + t); }
        // 单行 / 单列：最多只能形成 1x1
        try { if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'1', '1', '1'}}), "单行全1")) failures++; } catch (Throwable t) { failures++; System.out.println("单行全1 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'1'}, new char[]{'1'}, new char[]{'1'}}), "单列全1")) failures++; } catch (Throwable t) { failures++; System.out.println("单列全1 异常: " + t); }
        // 全一：正方形边长 = min(行,列)
        try { if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{new char[]{'1', '1'}, new char[]{'1', '1'}}), "全一2x2")) failures++; } catch (Throwable t) { failures++; System.out.println("全一2x2 异常: " + t); }
        try { if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{new char[]{'1', '1', '1'}, new char[]{'1', '1', '1'}}), "全一2x3长条")) failures++; } catch (Throwable t) { failures++; System.out.println("全一2x3长条 异常: " + t); }
        try { if (!TestUtil.checkEq(9, s.maximalSquare(new char[][]{new char[]{'1', '1', '1'}, new char[]{'1', '1', '1'}, new char[]{'1', '1', '1'}}), "全一3x3")) failures++; } catch (Throwable t) { failures++; System.out.println("全一3x3 异常: " + t); }
        // 零矩阵中嵌入 2x2 全一正方形
        try { if (!TestUtil.checkEq(4, s.maximalSquare(new char[][]{new char[]{'0', '0', '0'}, new char[]{'0', '1', '1'}, new char[]{'0', '1', '1'}}), "嵌入2x2")) failures++; } catch (Throwable t) { failures++; System.out.println("嵌入2x2 异常: " + t); }
        // 对角线上有 0，破坏 2x2：最多 1x1
        try { if (!TestUtil.checkEq(1, s.maximalSquare(new char[][]{new char[]{'1', '1'}, new char[]{'1', '0'}}), "对角0破坏")) failures++; } catch (Throwable t) { failures++; System.out.println("对角0破坏 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
