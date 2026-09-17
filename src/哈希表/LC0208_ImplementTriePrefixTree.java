// ============================================================
// LeetCode 208. 实现 Trie (前缀树) (Implement Trie (Prefix Tree))
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/implement-trie-prefix-tree/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-03 · 一刷一次 AC）
// 一刷/上次复习写法：Trie（26 叉数组子节点 + 词尾结束标记），insert/search/startsWith 都是逐字符下行 O(len)
// 要点：end 只标在词尾节点（不是沿途每个节点）；search 要额外看 cur.end，startsWith 只看能否走通
// 测试用例与一刷归档保持一致（示例 4 个 + 边界 6 个）
// 上次复习留的精简提示：一刷把「下行、走不通返回 null」抽成私有 getCur 给 search/startsWith 共用，本轮内联成了两遍同样 7 行循环——想清楚要不要抽回去
//
// 思路：每个节点放 26 叉子节点数组 + 词尾标记 end；insert 从根 this 出发逐字符下行（缺则建），最后在词尾节点标 end；
//       search 同样下行，走不通返回 false，走通后还要看 cur.end；startsWith 只看能否走通，不看 end
// 复杂度：单次操作时间 O(len)；空间 O(已插入字符总数 × 26) 个指针 + 每节点一个 end
// ============================================================

import java.util.*;

public class LC0208_ImplementTriePrefixTree {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 Trie 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class Trie {
        Trie[] sub;
        boolean end = false;
        public Trie() {
            sub = new Trie[26];
        }
        public void insert(String word) {
            Trie it = this;
            for (char c : word.toCharArray()) {
                if (it.sub[c - 'a'] == null) {
                    it.sub[c - 'a'] = new Trie();
                }
                it = it.sub[c - 'a'];
            }
            it.end = true;
        }
        public boolean search(String word) {
            Trie it = this;
            for (char c : word.toCharArray()) {
                if (it.sub[c - 'a'] == null) {
                    return false;
                }
                it = it.sub[c - 'a'];
            }
            return it.end;
        }
        public boolean startsWith(String prefix) {
            Trie it = this;
            for (char c : prefix.toCharArray()) {
                if (it.sub[c - 'a'] == null) {
                    return false;
                }
                it = it.sub[c - 'a'];
            }
            return it != null;
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

        // ---- 边界测试 ----
        try {
            Trie t = new Trie();
            t.insert("a");
            if (!TestUtil.checkEq(true, t.search("a"), "边界1-单字符 word")) failures++;
            if (!TestUtil.checkEq(true, t.startsWith("a"), "边界1-单字符 prefix")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            Trie t = new Trie();
            t.insert("apple");
            if (!TestUtil.checkEq(false, t.search("appl"), "边界2-只是前缀不算单词")) failures++;
            if (!TestUtil.checkEq(true, t.startsWith("appl"), "边界2-前缀查询应为 true")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            Trie t = new Trie();
            t.insert("app");
            if (!TestUtil.checkEq(false, t.search("apple"), "边界3-反向不存在（词比插入的长）")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            Trie t = new Trie();
            t.insert("ab");
            t.insert("a");
            if (!TestUtil.checkEq(true, t.search("ab"), "边界4-先插 ab 再插 a，ab 仍须在")) failures++;
            if (!TestUtil.checkEq(true, t.search("a"), "边界4-短词是长词前缀")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            Trie t = new Trie();
            t.insert("word");
            t.insert("word");
            if (!TestUtil.checkEq(true, t.search("word"), "边界5-重复插入同一词")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            StringBuilder sb = new StringBuilder();
            StringBuilder other = new StringBuilder();
            for (int i = 0; i < 2000; i++) { sb.append('a'); other.append(i == 1999 ? 'b' : 'a'); }
            String longWord = sb.toString(), nearMiss = other.toString();
            Trie t = new Trie();
            t.insert(longWord);
            if (!TestUtil.checkEq(true, t.search(longWord), "边界6-长度 2000 上限命中")) failures++;
            if (!TestUtil.checkEq(true, t.startsWith(longWord), "边界6-长度 2000 前缀命中")) failures++;
            if (!TestUtil.checkEq(false, t.search(nearMiss), "边界6-最后一位不同应未命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
