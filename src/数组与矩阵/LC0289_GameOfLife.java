// ============================================================
// LeetCode 289. 生命游戏 (Game of Life)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/game-of-life/
// 刷题日期：2026-08-23
//
// ============================================================

import java.util.*;

public class LC0289_GameOfLife {

    // ==== 提交代码开始 ====
    public void gameOfLife(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                judge(board, i, j);
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == -1) {
                    board[i][j] = 0;
                } else if (board[i][j] == -2) {
                    board[i][j] = 1;
                }
            }
        }
    }

    private void judge(int[][] board, int i, int j) {
        int[][] dirs = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        int live = 0;
        for (int[] dir : dirs) {
            int x = i + dir[0];
            int y = j + dir[1];
            if (x >= 0 && x < board.length && y >= 0 && y < board[0].length) {
                if (board[x][y] == 1 || board[x][y] == -1) {
                    live++;
                }
            }
        }
        if (board[i][j] == 1) {
            if (live < 2 || live > 3) {
                board[i][j] = -1;
            }
        } else if (board[i][j] == 0) {
            if (live == 3) {
                board[i][j] = -2;
            }
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0289_GameOfLife s = new LC0289_GameOfLife();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            int[][] nums = new int[][]{new int[]{0, 1, 0}, new int[]{0, 0, 1}, new int[]{1, 1, 1}, new int[]{0, 0, 0}};
            s.gameOfLife(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{0, 0, 0}, new int[]{1, 0, 1}, new int[]{0, 1, 1}, new int[]{0, 1, 0}}, nums, "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            int[][] nums = new int[][]{new int[]{1, 1}, new int[]{1, 0}};
            s.gameOfLife(nums);
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 1}, new int[]{1, 1}}, nums, "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 单细胞活：周围 0 个活邻居 (<2) -> 死
        try {
            int[][] b = new int[][]{new int[]{1}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{0}}, b, "边界1-单活")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单活 异常: " + t); }
        // 2) 单细胞死：死细胞周围 0 个 (≠3) -> 仍死
        try {
            int[][] b = new int[][]{new int[]{0}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{0}}, b, "边界2-单死")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-单死 异常: " + t); }
        // 3) 2x2 全活：每个活细胞周围 3 个 -> 存活（稳定）
        try {
            int[][] b = new int[][]{new int[]{1, 1}, new int[]{1, 1}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 1}, new int[]{1, 1}}, b, "边界3-2x2全活")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-2x2全活 异常: " + t); }
        // 4) 2x2 对角：每个活细胞周围 1 个 -> 全死
        try {
            int[][] b = new int[][]{new int[]{1, 0}, new int[]{0, 1}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{0, 0}, new int[]{0, 0}}, b, "边界4-对角")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-对角 异常: " + t); }
        // 5) 死细胞复活：中心 (1,1) 周围正好 3 个活 -> 变活
        try {
            int[][] b = new int[][]{new int[]{1, 1, 0}, new int[]{1, 0, 0}, new int[]{0, 0, 0}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 1, 0}, new int[]{1, 1, 0}, new int[]{0, 0, 0}}, b, "边界5-复活")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-复活 异常: " + t); }
        // 6) 3x3 全活：角细胞只有 3 个活邻居(存活)，边 5 个(死)，中心 8 个(死)
        //    -> [[1,0,1],[0,0,0],[1,0,1]]，验证边界邻居数统计正确
        try {
            int[][] b = new int[][]{new int[]{1, 1, 1}, new int[]{1, 1, 1}, new int[]{1, 1, 1}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 0, 1}, new int[]{0, 0, 0}, new int[]{1, 0, 1}}, b, "边界6-3x3全活")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-3x3全活 异常: " + t); }
        // 7) 1x2 全活：每个活细胞周围 1 个 -> 全死
        try {
            int[][] b = new int[][]{new int[]{1, 1}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{0, 0}}, b, "边界7-1x2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-1x2 异常: " + t); }
        // 8) 2x1 全活：同上 -> 全死
        try {
            int[][] b = new int[][]{new int[]{1}, new int[]{1}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{0}, new int[]{0}}, b, "边界8-2x1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8-2x1 异常: " + t); }
        // 9) 闪烁器 blinker：水平三连 -> 垂直三连（经典同时更新 case）
        try {
            int[][] b = new int[][]{new int[]{0, 0, 0}, new int[]{1, 1, 1}, new int[]{0, 0, 0}};
            s.gameOfLife(b);
            if (!TestUtil.checkEq(new int[][]{new int[]{0, 1, 0}, new int[]{0, 1, 0}, new int[]{0, 1, 0}}, b, "边界9-闪烁器")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9-闪烁器 异常: " + t); }
        // 10) 最大尺寸 25x25 全活：四角各 3 个活邻居存活，其余过密死亡，验证大输入
        try {
            int[][] b = new int[25][25];
            for (int i = 0; i < 25; i++) Arrays.fill(b[i], 1);
            s.gameOfLife(b);
            boolean ok = true;
            for (int i = 0; i < 25 && ok; i++) {
                for (int j = 0; j < 25 && ok; j++) {
                    boolean corner = (i == 0 || i == 24) && (j == 0 || j == 24);
                    if (corner ? b[i][j] != 1 : b[i][j] != 0) ok = false;
                }
            }
            if (!ok) { failures++; System.out.println("边界10-25x25全活 角落/其余状态不符"); }
        } catch (Throwable t) { failures++; System.out.println("边界10-25x25全活 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
