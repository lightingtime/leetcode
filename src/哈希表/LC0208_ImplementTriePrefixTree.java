// ============================================================
// LeetCode 208. 实现 Trie (前缀树) (Implement Trie (Prefix Tree))
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/implement-trie-prefix-tree/
// 刷题日期：2026-08-03
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0208_ImplementTriePrefixTree {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 Trie 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class Trie {
        Trie[] sub;
        boolean end = false;
        public Trie() {
            // TODO: 补全方法体
            sub = new Trie[26];
        }
        public void insert(String word) {
            Trie cur = this;
            // TODO: 补全方法体
            for (Character c : word.toCharArray()) {
                if (cur.sub[c - 'a'] != null) {
                    cur = cur.sub[c - 'a'];
                } else {
                    cur.sub[c - 'a'] = new Trie();
                    cur = cur.sub[c - 'a'];
                }
            }
            cur.end = true;
        }
        public boolean search(String word) {
            // TODO: 补全方法体
            Trie cur = getCur(word);
            if (cur == null) return false;
            return cur.end;
        }

        private Trie getCur(String word) {
            Trie cur = this;
            for (Character c : word.toCharArray()) {
                if (cur.sub[c - 'a'] == null) {
                    return null;
                } else {
                    cur = cur.sub[c - 'a'];
                }
            }
            return cur;
        }

        public boolean startsWith(String prefix) {
            // TODO: 补全方法体
            Trie cur = getCur(prefix);
            return cur != null;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        // 设计题：按题目示例手动构造调用序列，例如：
        // Trie s = new Trie(...);
        // s.method(...);
        // 题目原始示例输入：
        // ["Trie","insert","search","search","startsWith","insert","search"]
        // [[],["apple"],["apple"],["app"],["app"],["app"],["app"]]
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            Trie trie = new Trie();
            trie.insert("apple");
            if (!TestUtil.checkEq(true, trie.search("apple"), "示例1 search(apple)")) failures++;
            if (!TestUtil.checkEq(false, trie.search("app"), "示例2 search(app)")) failures++;
            if (!TestUtil.checkEq(true, trie.startsWith("app"), "示例3 startsWith(app)")) failures++;
            trie.insert("app");
            if (!TestUtil.checkEq(true, trie.search("app"), "示例4 insert(app) 后 search(app)")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // TODO: 补充单字符、相同前缀、未插入前缀等边界

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
