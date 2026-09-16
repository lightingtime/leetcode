// ============================================================
// LeetCode 200. 岛屿数量 (Number of Islands)
// 难度：Medium | 分类：图与并查集
// 链接：https://leetcode.cn/problems/number-of-islands/
// 复习日期：2026-09-16（第 4 次复习 · 一刷 2026-08-08 · 上次 2026-09-06 较强 · 间隔 10 天到期）
// 一刷思路：DFS 洪水填充 + visited；本轮改为原地淹没（访问过的 1 置 2），额外空间 O(1)
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

public class LC0200_NumberOfIslands {

    // ==== 提交代码开始 ====
    int ans;
    public int numIslands(char[][] grid) {
        ans = 0;
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    dfs(grid, i, j);
                    ans++;
                }
            }
        }
        return ans;
    }

    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] != '1') {
            return;
        }
        grid[i][j] = '2';
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
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