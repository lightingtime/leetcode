// ============================================================
// LeetCode 200. 岛屿数量 (Number of Islands)
// 难度：Medium | 分类：图与并查集
// 链接：https://leetcode.cn/problems/number-of-islands/
// 刷题日期：2026-08-08
//
// ============================================================

public class LC0200_NumberOfIslands {

    // ==== 提交代码开始 ====
    boolean[][] visited;
    public int numIslands(char[][] grid) {
        int count = 0;
        int m = grid.length;
        if (m == 0) {
            return count;
        }
        int n = grid[0].length;
        visited = new boolean[m][n];
        if (m == 0 || n == 0) {
            return count;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (!visited[i][j] && dfs(grid, i, j)) {
                    count++;
                }
            }
        }
        return count;
    }

    int[] xd = new int[]{-1,1, 0, 0};
    int[] yd = new int[]{0, 0 , -1, 1};
    private boolean dfs(char[][] grid, int i, int j) {
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length) {
            return false;
        }
        if (visited[i][j]) {
            return false;
        }
        visited[i][j] = true;
        if (grid[i][j] == '0') {
            return false;
        }
        boolean cur = true;
        for (int k = 0; k < xd.length; k++) {
            dfs(grid, i + xd[k], j + yd[k]);
        }
        return cur;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0200_NumberOfIslands s = new LC0200_NumberOfIslands();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(1, s.numIslands(new char[][]{
                    {'1','1','1','1','0'},
                    {'1','1','0','1','0'},
                    {'1','1','0','0','0'},
                    {'0','0','0','0','0'}
            }), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.numIslands(new char[][]{
                    {'1','1','0','0','0'},
                    {'1','1','0','0','0'},
                    {'0','0','1','0','0'},
                    {'0','0','0','1','1'}
            }), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(0, s.numIslands(new char[][]{}), "边界1-空网格")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.numIslands(new char[][]{{'1'}}), "边界2-单格陆地")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.numIslands(new char[][]{{'0'}}), "边界3-单格水")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.numIslands(new char[][]{{'0','0'},{'0','0'}}), "边界4-全水")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.numIslands(new char[][]{{'1','1'},{'1','1'}}), "边界5-全陆地")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.numIslands(new char[][]{
                    {'1','0','1'},
                    {'0','1','0'},
                    {'1','0','1'}
            }), "边界6-斜对角不相邻")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.numIslands(new char[][]{{'1','0','1','1'}}), "边界7-单行")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.numIslands(new char[][]{{'1'},{'0'},{'1'},{'1'}}), "边界8-单列")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.numIslands(new char[][]{
                    {'1','1','1'},
                    {'1','0','1'},
                    {'1','1','1'}
            }), "边界9-环状岛")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
