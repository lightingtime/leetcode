// ============================================================
// LeetCode 79. 单词搜索 (Word Search)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/word-search/
// 刷题日期：2026-08-09
//
// ============================================================

import java.util.*;

public class LC0079_WordSearch {

    // ==== 提交代码开始 ====
    boolean[][] visited;

    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        visited = new boolean[m][n];
        boolean ans = false;
        int index = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ans = ans || dfs(board, i, j, index, word);
            }
        }
        return ans;
    }

    private boolean dfs(char[][] board, int i, int j, int index, String word) {
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return false;
        }
        if (visited[i][j]) {
            return false;
        }
        if (board[i][j] != word.charAt(index)) {
            return false;
        }
        visited[i][j] = true;
        int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        boolean ans = false;
        for (int[] dir : dirs) {
            int x = i + dir[0];
            int y = j + dir[1];
            ans = ans || dfs(board, x, y, index + 1, word);
        }
        visited[i][j] = false;
        return ans;
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0079_WordSearch s = new LC0079_WordSearch();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.exist(new char[][]{new char[]{'A', 'B', 'C', 'E'}, new char[]{'S', 'F', 'C', 'S'}, new char[]{'A', 'D', 'E', 'E'}}, "ABCCED"), "示例1"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(true, s.exist(new char[][]{new char[]{'A', 'B', 'C', 'E'}, new char[]{'S', 'F', 'C', 'S'}, new char[]{'A', 'D', 'E', 'E'}}, "SEE"), "示例2"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.exist(new char[][]{new char[]{'A', 'B', 'C', 'E'}, new char[]{'S', 'F', 'C', 'S'}, new char[]{'A', 'D', 'E', 'E'}}, "ABCB"), "示例3"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(true, s.exist(new char[][]{new char[]{'A'}}, "A"), "边界-单格命中")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-单格命中 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.exist(new char[][]{new char[]{'A'}}, "B"), "边界-单格未命中")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-单格未命中 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.exist(new char[][]{new char[]{'A'}}, "AB"), "边界-词过长")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-词过长 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.exist(new char[][]{new char[]{'A', 'B'}, new char[]{'C', 'D'}}, "AD"), "边界-斜向不可达"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-斜向不可达 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(true, s.exist(new char[][]{new char[]{'A', 'B'}, new char[]{'C', 'D'}}, "AC"), "边界-竖直可达"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-竖直可达 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.exist(new char[][]{new char[]{'A', 'A'}}, "AAA"), "边界-同格不可复用"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-同格不可复用 异常: " + t);
        }
        try {
            char[][] allA = new char[][]{new char[]{'A', 'A', 'A'}, new char[]{'A', 'A', 'A'}, new char[]{'A', 'A', 'A'}};
            if (!TestUtil.checkEq(true, s.exist(allA, "AAAAAA"), "边界-全相同长路径")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-全相同长路径 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
