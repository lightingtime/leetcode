// ============================================================
// LeetCode 79. 单词搜索 (Word Search)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/word-search/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-09 · 一刷一次 Accepted）
// 一刷写法：网格 DFS 回溯 + 访问标记——枚举每个格子作为起点，从起点沿上下左右匹配单词的下一个字符；匹配成功就占位（改字符或 visited 标记），递归返回时撤销。O(m·n·3^L)/O(L)
// 本题易错点：① 起点要枚举（任何格子都可能是首字母），漏掉这层循环只会从 (0,0) 开始找；② 走过的格子不能复用——改字符（如置成 '\0'）或开 visited 数组，选一种并保证「选择/撤销」对称；③ 越界与字符是否相等要在进入递归前判掉；④ 走到单词末尾立刻返回 true，不必继续扩展
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 7 个：单格命中/未命中/词过长/斜向不可达/竖直可达/同格不可复用/全相同长路径）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
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
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || visited[i][j]) {
            return false;
        }
        if (board[i][j] != word.charAt(index)) {
            return false;
        }
        visited[i][j] = true;
        boolean ans;
        ans = dfs(board, i + 1, j, index + 1, word);
        ans = ans || dfs(board, i - 1, j, index + 1, word);
        ans = ans || dfs(board, i, j - 1, index + 1, word);
        ans = ans || dfs(board, i, j + 1, index + 1, word);
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
