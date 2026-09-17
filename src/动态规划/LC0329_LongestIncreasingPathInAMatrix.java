// ============================================================
// LeetCode 329. 矩阵中的最长递增路径 (Longest Increasing Path in a Matrix)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-26 · 一刷非一次 AC）
// 一刷思路：记忆化 DFS（DAG 最长路径），dfs(i,j) = 1 + max(更大邻居的 dfs)，memo 每格只算一次
// 上一次复习：记忆化 DFS（DAG 最长路径），一次 AC 但有探讨（判较弱）；上次踩坑：DFS 未记忆化 → 指数爆炸、守卫写成 < pre 放行等值 → 2-环 StackOverflow
// 子类型：DAG 最长路径（记忆化 DFS / 网格上按值定向的 DP）
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
//
// 思路：记忆化 DFS（DAG 最长路径）——dfs(i,j) 返回「从 (i,j) 出发的最长递增路径长度」= 1 + 四个邻居中的最大值，
//       memo 每格只算一次；守卫 matrix[i][j] <= pre 挡住等值（无环保证）与越界，外层枚举每个起点取最大值
// 复杂度：时间 O(m·n)（每格只算一次），空间 O(m·n)（memo + 递归栈最深 m·n）
// ============================================================

public class LC0329_LongestIncreasingPathInAMatrix {

    // ==== 提交代码开始 ====
    int ans;
    int[][] memo;

    public int longestIncreasingPath(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        ans = 0;
        memo = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ans = Math.max(ans, dfs(matrix, i, j, -1));
            }
        }
        return ans;
    }

    private int dfs(int[][] matrix, int i, int j, int pre) {
        // 守卫兼防环：等值不通过（只有严格递增才继续展开），状态图必然无环；此分支直接 return 0 且不写 memo，
        // 所以 memo[i][j] 存的值与调用方传入的 pre 无关，缓存才成立
        if (i < 0 || j < 0 || i > matrix.length - 1 || j > matrix[0].length - 1 || matrix[i][j] <= pre) {
            return 0;
        }
        // 0 作「未计算」哨兵：能走到这里说明守卫已通过，合法答案至少为 1
        if (memo[i][j] > 0) {
            return memo[i][j];
        }
        int count = 0;

        count = Math.max(count, dfs(matrix, i + 1, j, matrix[i][j]));
        count = Math.max(count, dfs(matrix, i - 1, j, matrix[i][j]));
        count = Math.max(count, dfs(matrix, i, j + 1, matrix[i][j]));
        count = Math.max(count, dfs(matrix, i, j - 1, matrix[i][j]));
        memo[i][j] = count + 1;
        return memo[i][j];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0329_LongestIncreasingPathInAMatrix s = new LC0329_LongestIncreasingPathInAMatrix();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.longestIncreasingPath(new int[][]{new int[]{9, 9, 4}, new int[]{6, 6, 8}, new int[]{2, 1, 1}}), "示例1"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(4, s.longestIncreasingPath(new int[][]{new int[]{3, 4, 5}, new int[]{3, 2, 6}, new int[]{2, 2, 1}}), "示例2"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(1, s.longestIncreasingPath(new int[][]{new int[]{1}}), "示例3")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= m,n <= 200，0 <= matrix[i][j] <= 2^31-1（严格递增，相等不能走）
        // 边界1: 单行矩阵，只能左右
        try {
            if (!TestUtil.checkEq(3, s.longestIncreasingPath(new int[][]{new int[]{1, 2, 3}}), "边界1: 单行"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        // 边界2: 单列矩阵，只能上下
        try {
            if (!TestUtil.checkEq(3, s.longestIncreasingPath(new int[][]{new int[]{1}, new int[]{2}, new int[]{3}}), "边界2: 单列"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }
        // 边界3: 全相同——必须严格递增，相等不能走 → 1
        try {
            if (!TestUtil.checkEq(1, s.longestIncreasingPath(new int[][]{new int[]{5, 5}, new int[]{5, 5}}), "边界3: 全相同"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }
        // 边界4: 2x2 螺旋递增 → 1→2→3→4 长度 4
        try {
            if (!TestUtil.checkEq(4, s.longestIncreasingPath(new int[][]{new int[]{1, 2}, new int[]{4, 3}}), "边界4: 螺旋路径"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }
        // 边界5: 200x200 上限，matrix[i][j]=i+j → 任一单调右/下路径长度 m+n-1=399
        try {
            int[][] big = new int[200][200];
            for (int i = 0; i < 200; i++) for (int j = 0; j < 200; j++) big[i][j] = i + j;
            if (!TestUtil.checkEq(399, s.longestIncreasingPath(big), "边界5: 200x200上限")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }
        // 边界6: 取值上限 2^31-1 附近，只比较不运算，不应溢出
        try {
            if (!TestUtil.checkEq(2, s.longestIncreasingPath(new int[][]{new int[]{0, 2147483647}}), "边界6: 上限值"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
