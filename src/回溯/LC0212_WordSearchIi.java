// ============================================================
// LeetCode 212. 单词搜索 II (Word Search II)
// 难度：Hard | 分类：回溯
// 链接：https://leetcode.cn/problems/word-search-ii/
// 刷题日期：2026-08-26
//
// 思路：Trie + 回溯 DFS——先把全部 words 建进字典树（词尾存完整单词便于收集与去重），
//       再以每个格子为起点 DFS：守卫(越界/visited) → Trie 剪枝 → 前进命中(收词并清空 word) →
//       标记 visited → 四方向递归 → 撤销 visited
// 复杂度：时间 O(Σwords 长度 + m·n·3^L)（建树 + 每格起点的剪枝后深搜，L=单词最大长度）
//         空间 O(Σwords 长度 + m·n)（Trie 节点 + visited）
// ============================================================

import java.util.*;

public class LC0212_WordSearchIi {

    // ==== 提交代码开始 ====
    List<String> ans;
    boolean[][] visited;
    public List<String> findWords(char[][] board, String[] words) {
        ans = new ArrayList<>();
        visited = new boolean[board.length][board[0].length];
        Trie trie = new Trie();
        for (String word : words) {
            Trie p = trie;
            for (char c : word.toCharArray()) {
                if (p.children[c - 'a'] == null) {
                    p.children[c - 'a'] = new Trie();
                }
                p = p.children[c - 'a'];
            }
            p.word = word;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                dfs(board, i, j, trie);
            }
        }
        return ans;
    }

    private void dfs(char[][] board, int i, int j, Trie trie) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || visited[i][j]) {
            return;
        }
        if (trie.children[board[i][j] - 'a'] == null) {
            return;
        }
        trie = trie.children[board[i][j] - 'a'];
        if (trie.word != null) {
            ans.add(trie.word);
            trie.word = null;
        }
        visited[i][j] = true;
        dfs(board, i - 1, j, trie);
        dfs(board, i + 1, j, trie);
        dfs(board, i, j - 1, trie);
        dfs(board, i, j + 1, trie);
        visited[i][j] = false;
    }

    static class Trie {
        Trie[] children;
        String word;

        public Trie() {
            this.children = new Trie[26];
        }

        public Trie(Trie[] children, String word) {
            this.children = children;
            this.word = word;
        }
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0212_WordSearchIi s = new LC0212_WordSearchIi();
        int failures = 0;

        // 注：本题结果顺序任意，统一用 checkEqUnordered 比较
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("eat", "oath"), s.findWords(new char[][]{new char[]{'o', 'a', 'a', 'n'}, new char[]{'e', 't', 'a', 'e'}, new char[]{'i', 'h', 'k', 'r'}, new char[]{'i', 'f', 'l', 'v'}}, new String[]{"oath", "pea", "eat", "rain"}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.findWords(new char[][]{new char[]{'a', 'b'}, new char[]{'c', 'd'}}, new String[]{"abcb"}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= m,n <= 12，1 <= words.length <= 3*10^4，1 <= words[i].length <= 10
        // 边界1: 1x1 最小棋盘，单词长度 1 恰好命中
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("a"), s.findWords(new char[][]{new char[]{'a'}}, new String[]{"a"}), "边界1: 1x1单格命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 单词长度超过可达路径（2x2 只有 4 格且无 abc 路径）
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.findWords(new char[][]{new char[]{'a', 'b'}, new char[]{'c', 'd'}}, new String[]{"abc", "abcd"}), "边界2: 无路径单词")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 同一单元格不允许重复使用（'a' 只有一处却要用两次）
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.findWords(new char[][]{new char[]{'a', 'b'}}, new String[]{"aba"}), "边界3: 单元格不可复用")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 同一单词存在多条匹配路径时只输出一次
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("aa"), s.findWords(new char[][]{new char[]{'a', 'a'}}, new String[]{"aa"}), "边界4: 多路径去重")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 前缀嵌套多词（"ab" 是 "abc" 的前缀，"ac" 不相邻应排除）
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("ab", "abc"), s.findWords(new char[][]{new char[]{'a', 'b', 'c'}}, new String[]{"ab", "abc", "ac"}), "边界5: 前缀嵌套多词")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 竖条形棋盘，"bb" 因两个 b 不相邻被排除，其余命中
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("ba", "bab"), s.findWords(new char[][]{new char[]{'b'}, new char[]{'a'}, new char[]{'b'}}, new String[]{"ba", "bab", "bb"}), "边界6: 竖条棋盘部分命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}