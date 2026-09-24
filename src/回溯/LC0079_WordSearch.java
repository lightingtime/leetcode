// ============================================================
// LeetCode 79. 单词搜索 (Word Search)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/word-search/
// 刷题日期：2026-09-22
//
// ============================================================

import java.util.*;

public class LC0079_WordSearch {

    // ==== 提交代码开始 ====
    boolean[][] visited;
    public boolean exist(char[][] board, String word) {
        visited = new boolean[board.length][board[0].length];
        boolean ans = false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (!visited[i][j]) {
                    ans = dfs(board, i, j, word, 0);
                }
                if (ans) {
                    return true;
                }
            }
        }
        return ans;
    }

    private boolean dfs(char[][] board, int i, int j, String word, int len) {
        if (len == word.length()) {
            return true;
        }
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j]) {
            return false;
        }
        if (word.charAt(len) != board[i][j]) {
            return false;
        }
        visited[i][j] = true;
        boolean search;
        search = dfs(board, i + 1, j, word, len + 1);
        search = search || dfs(board, i - 1, j, word, len + 1);
        search = search || dfs(board, i, j + 1, word, len + 1);
        search = search || dfs(board, i, j - 1, word, len + 1);
        // visited 表示当前递归路径占用的格子，回退时必须释放。
        visited[i][j] = false;
        return search;
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
            if (!TestUtil.checkEq(true, s.exist(new char[][]{new char[]{'A', 'B'}, new char[]{'A', 'C'}}, "AAB"), "边界-失败尝试需撤销访问标记"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-失败尝试需撤销访问标记 异常: " + t);
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
