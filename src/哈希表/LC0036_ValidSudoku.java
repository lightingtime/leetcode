// ============================================================
// LeetCode 36. 有效的数独 (Valid Sudoku)
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/valid-sudoku/
// 刷题日期：2026-08-21
//
// ============================================================

import java.util.*;

public class LC0036_ValidSudoku {

    // ==== 提交代码开始 ====
    public boolean isValidSudoku(char[][] board) {
        int[][] rows = new int[9][9];
        int[][] cols = new int[9][9];
        int[][][] sub = new int[3][3][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    Integer num = board[i][j] - '0' - 1;
                    rows[i][num]++;
                    cols[j][num]++;
                    sub[i / 3][j / 3][num]++;
                    if (rows[i][num] > 1 || cols[j][num] > 1 || sub[i / 3][j / 3][num] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0036_ValidSudoku s = new LC0036_ValidSudoku();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        String[] ex1 = {
            "53..7....",
            "6..195...",
            ".98....6.",
            "8...6...3",
            "4..8.3..1",
            "7...2...6",
            ".6....28.",
            "...419..5",
            "....8..79"
        };
        String[] ex2 = {
            "83..7....",
            "6..195...",
            ".98....6.",
            "8...6...3",
            "4..8.3..1",
            "7...2...6",
            ".6....28.",
            "...419..5",
            "....8..79"
        };
        try { if (!TestUtil.checkEq(true, s.isValidSudoku(board(ex1)), "示例1: 有效棋盘")) failures++; } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try { if (!TestUtil.checkEq(false, s.isValidSudoku(board(ex2)), "示例2: 左上宫两个8")) failures++; } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: 全空白棋盘，没有任何冲突
        try { if (!TestUtil.checkEq(true, s.isValidSudoku(board(new String[]{".........", ".........", ".........", ".........", ".........", ".........", ".........", ".........", "........."})), "边界1: 全空白")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: 同一行重复数字 1（第 0 行两个 '1'）
        try { if (!TestUtil.checkEq(false, s.isValidSudoku(board(new String[]{"1.......1", ".........", ".........", ".........", ".........", ".........", ".........", ".........", "........."})), "边界2: 行重复")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }

        // 边界3: 同一列重复数字 9（第 8 列两个 '9'，不同行）
        try { if (!TestUtil.checkEq(false, s.isValidSudoku(board(new String[]{"........9", ".........", ".........", ".........", ".........", ".........", ".........", ".........", "........9"})), "边界3: 列重复")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }

        // 边界4: 同一 3x3 宫重复数字 3（(0,6) 与 (2,8)，不同行不同列，只撞右上宫）
        try { if (!TestUtil.checkEq(false, s.isValidSudoku(board(new String[]{"......3..", ".........", "........3", ".........", ".........", ".........", ".........", ".........", "........."})), "边界4: 宫重复")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: 行、列、宫同时各有重复（三处冲突）
        try { if (!TestUtil.checkEq(false, s.isValidSudoku(board(new String[]{"11...3...", "1........", "........3", ".........", ".........", ".........", ".........", ".........", "........."})), "边界5: 行+列+宫多重冲突")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        // 边界6: 完整合法数独终盘（每行/列/宫都是 1-9）
        String[] full = {
            "534678912",
            "672195348",
            "198342567",
            "859761423",
            "426853791",
            "713924856",
            "961537284",
            "287419635",
            "345286179"
        };
        try { if (!TestUtil.checkEq(true, s.isValidSudoku(board(full)), "边界6: 合法终盘")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        // 边界7: 合法终盘只改一格为 '.', 仍然有效（未填完不等于无效）
        String[] fullWithBlank = {
            "534678912",
            "672195348",
            "198342567",
            "859761423",
            "426853791",
            "713924856",
            "961537284",
            "287419635",
            ".45286179"
        };
        try { if (!TestUtil.checkEq(true, s.isValidSudoku(board(fullWithBlank)), "边界7: 终盘挖一格仍有效")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        // 边界8: 左上宫两个 '1'（(0,0) 与 (1,2)，不同行不同列，只撞同一宫）
        try { if (!TestUtil.checkEq(false, s.isValidSudoku(board(new String[]{"1........", "..1......", ".........", ".........", ".........", ".........", ".........", ".........", "........."})), "边界8: 宫端点重复")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 辅助：String[]（每行 9 个字符，'.' 表示空白）转 char[][]
    private static char[][] board(String[] rows) {
        char[][] b = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) b[i] = rows[i].toCharArray();
        return b;
    }

}