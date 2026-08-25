// ============================================================
// LeetCode 130. 被围绕的区域 (Surrounded Regions)
// 难度：Medium | 分类：图与并查集
// 链接：https://leetcode.cn/problems/surrounded-regions/
// 刷题日期：2026-08-25
//
// ============================================================

import java.util.*;

public class LC0130_SurroundedRegions {

    // ==== 提交代码开始 ====
    public void solve(char[][] board) {
        int m = board.length;
        if (m == 0) {
            return;
        }
        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            dfs(board, i, 0);
            dfs(board, i, n - 1);
        }

        for (int j = 0; j < n; j++) {
            dfs(board, 0, j);
            dfs(board, m - 1, j);
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'A') {
                    board[i][j] = 'O';
                } else if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
            }
        }
    }

    private void dfs(char[][] board, int i, int j) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != 'O') {
            return;
        }
        board[i][j] = 'A';
        dfs(board, i - 1, j);
        dfs(board, i + 1, j);
        dfs(board, i, j - 1);
        dfs(board, i, j + 1);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0130_SurroundedRegions s = new LC0130_SurroundedRegions();
        int failures = 0;

        // ---- 示例测试（来自题目，void 原地修改：调用后断言 board）----
        try {
            char[][] b1 = new char[][]{{'X','X','X','X'},{'X','O','O','X'},{'X','X','O','X'},{'X','O','X','X'}};
            s.solve(b1);
            if (!TestUtil.checkEq(new char[][]{{'X','X','X','X'},{'X','X','X','X'},{'X','X','X','X'},{'X','O','X','X'}}, b1, "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            char[][] b2 = new char[][]{{'X'}};
            s.solve(b2);
            if (!TestUtil.checkEq(new char[][]{{'X'}}, b2, "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题：边缘 O 不可捕获、连通到边缘不捕获、被围中心变 X、单行/单列、大输入）----
        // 边界1: 全 O 且都在边缘（2x2）-> 不捕获
        try {
            char[][] b = new char[][]{{'O','O'},{'O','O'}};
            s.solve(b);
            if (!TestUtil.checkEq(new char[][]{{'O','O'},{'O','O'}}, b, "边界1: 全O边缘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 全 X -> 不变
        try {
            char[][] b = new char[][]{{'X','X','X'},{'X','X','X'},{'X','X','X'}};
            s.solve(b);
            if (!TestUtil.checkEq(new char[][]{{'X','X','X'},{'X','X','X'},{'X','X','X'}}, b, "边界2: 全X")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 单行（所有 O 都在边缘）-> 不捕获
        try {
            char[][] b = new char[][]{{'O','X','O'}};
            s.solve(b);
            if (!TestUtil.checkEq(new char[][]{{'O','X','O'}}, b, "边界3: 单行")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 单列 -> 不捕获
        try {
            char[][] b = new char[][]{{'O'},{'X'},{'O'}};
            s.solve(b);
            if (!TestUtil.checkEq(new char[][]{{'O'},{'X'},{'O'}}, b, "边界4: 单列")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 中心 O 完全被 X 包围 -> 变成 X
        try {
            char[][] b = new char[][]{{'X','X','X'},{'X','O','X'},{'X','X','X'}};
            s.solve(b);
            if (!TestUtil.checkEq(new char[][]{{'X','X','X'},{'X','X','X'},{'X','X','X'}}, b, "边界5: 中心被围")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 内部区域连通到边缘 O -> 整个区域不捕获
        try {
            char[][] b = new char[][]{{'X','X','X'},{'X','O','X'},{'O','O','X'}};
            s.solve(b);
            if (!TestUtil.checkEq(new char[][]{{'X','X','X'},{'X','O','X'},{'O','O','X'}}, b, "边界6: 连通边缘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 大输入（递归深度有界，规避栈溢出）——交替列 200x200 / 全O 50x50 / 全X 200x200
        try {
            char[][] b = new char[200][200];
            for (int i = 0; i < 200; i++)
                for (int j = 0; j < 200; j++)
                    b[i][j] = (j % 2 == 0) ? 'O' : 'X'; // O 只在偶数列，纵向连通到上下边缘
            char[][] exp = new char[200][200];
            for (int i = 0; i < 200; i++) exp[i] = b[i].clone();
            s.solve(b);
            boolean ok = true;
            for (int i = 0; i < 200 && ok; i++) for (int j = 0; j < 200 && ok; j++) if (b[i][j] != exp[i][j]) ok = false;
            if (!ok) { System.out.println("边界7a 失败 ✗"); failures++; } else { System.out.println("边界7a: 200x200交替列 通过 ✓"); }
        } catch (Throwable t) { failures++; System.out.println("边界7a 异常: " + t); }
        try {
            char[][] b = new char[50][50];
            for (char[] row : b) Arrays.fill(row, 'O');
            s.solve(b);
            boolean ok = true;
            for (char[] row : b) for (char c : row) if (c != 'O') ok = false;
            if (!ok) { System.out.println("边界7b 失败 ✗"); failures++; } else { System.out.println("边界7b: 50x50全O 通过 ✓"); }
        } catch (Throwable t) { failures++; System.out.println("边界7b 异常: " + t); }
        try {
            char[][] b = new char[200][200];
            for (char[] row : b) Arrays.fill(row, 'X');
            s.solve(b);
            boolean ok = true;
            for (char[] row : b) for (char c : row) if (c != 'X') ok = false;
            if (!ok) { System.out.println("边界7c 失败 ✗"); failures++; } else { System.out.println("边界7c: 200x200全X 通过 ✓"); }
        } catch (Throwable t) { failures++; System.out.println("边界7c 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}