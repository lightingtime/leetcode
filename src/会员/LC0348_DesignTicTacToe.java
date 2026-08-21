// ============================================================
// LeetCode 348. 设计井字棋 (Design Tic-Tac-Toe)
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/design-tic-tac-toe/
// 刷题日期：2026-08-21
//
// ============================================================

import java.util.*;

public class LC0348_DesignTicTacToe {

    // 设计题：力扣模板类名为 TicTacToe，提交脚本会把 static class 解包为顶层类。
    // ==== 提交代码开始 ====
    static class TicTacToe {
        int[][] board;
        int[][] rows;
        int[][] cols;
        int[] mainDia;
        int[] antiDia;
        public TicTacToe(int n) {
            board = new int[n][n];
            rows = new int[n][2];
            cols = new int[n][2];
            mainDia = new int[2];
            antiDia = new int[2];
        }

        public int move(int row, int col, int player) {
            board[row][col] = player;
            rows[row][player - 1]++;
            cols[col][player - 1]++;
            if (row == col) {
                mainDia[player - 1]++;
            }
            if (row + col == board.length - 1) {
                antiDia[player - 1]++;
            }
            if (rows[row][player - 1] == board.length || cols[col][player - 1] == board.length || mainDia[player - 1] == board.length
                    || antiDia[player - 1] == board.length) {
                return player;
            }
            return 0;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            TicTacToe t = new TicTacToe(3);
            int[] r = new int[7];
            r[0] = t.move(0, 0, 1);
            r[1] = t.move(0, 2, 2);
            r[2] = t.move(2, 2, 1);
            r[3] = t.move(1, 1, 2);
            r[4] = t.move(2, 0, 1);
            r[5] = t.move(1, 0, 2);
            r[6] = t.move(2, 1, 1);
            if (!TestUtil.checkEq(new int[]{0, 0, 0, 0, 0, 0, 1}, r, "示例: 玩家1行2获胜")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: n=1，唯一落子即满行/满列/满对角线
        try {
            TicTacToe t = new TicTacToe(1);
            if (!TestUtil.checkEq(1, t.move(0, 0, 1), "边界1: n=1 直接赢")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: n=2 第 0 行赢
        try {
            TicTacToe t = new TicTacToe(2);
            int a = t.move(0, 0, 1);
            int b = t.move(0, 1, 1);
            if (!TestUtil.checkEq(new int[]{0, 1}, new int[]{a, b}, "边界2: 行赢")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }

        // 边界3: n=2 第 0 列赢（玩家2）
        try {
            TicTacToe t = new TicTacToe(2);
            int a = t.move(0, 0, 2);
            int b = t.move(1, 0, 2);
            if (!TestUtil.checkEq(new int[]{0, 2}, new int[]{a, b}, "边界3: 列赢")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }

        // 边界4: n=2 主对角线赢
        try {
            TicTacToe t = new TicTacToe(2);
            int a = t.move(0, 0, 1);
            int b = t.move(1, 1, 1);
            if (!TestUtil.checkEq(new int[]{0, 1}, new int[]{a, b}, "边界4: 主对角线赢")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: n=2 副对角线赢（玩家2）
        try {
            TicTacToe t = new TicTacToe(2);
            int a = t.move(0, 1, 2);
            int b = t.move(1, 0, 2);
            if (!TestUtil.checkEq(new int[]{0, 2}, new int[]{a, b}, "边界5: 副对角线赢")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        // 边界6: 未成线时返回 0（双方各落一子）
        try {
            TicTacToe t = new TicTacToe(3);
            int a = t.move(0, 0, 1);
            int b = t.move(1, 1, 2);
            if (!TestUtil.checkEq(new int[]{0, 0}, new int[]{a, b}, "边界6: 未赢返回0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        // 边界7: 大棋盘 n=5，玩家1 第 0 行 5 连（交替落子，第 9 步获胜）
        try {
            TicTacToe t = new TicTacToe(5);
            int[] r = new int[9];
            r[0] = t.move(0, 0, 1);
            r[1] = t.move(1, 0, 2);
            r[2] = t.move(0, 1, 1);
            r[3] = t.move(1, 1, 2);
            r[4] = t.move(0, 2, 1);
            r[5] = t.move(1, 2, 2);
            r[6] = t.move(0, 3, 1);
            r[7] = t.move(1, 3, 2);
            r[8] = t.move(0, 4, 1);
            if (!TestUtil.checkEq(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 1}, r, "边界7: n=5 行赢")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}