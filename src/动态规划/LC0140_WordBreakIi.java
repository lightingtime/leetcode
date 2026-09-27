// ============================================================
// LeetCode 140. 单词拆分 II (Word Break II)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/word-break-ii/
// 二刷 · 一刷完成日期：2026-08-26（一刷思路先自己回忆，提交后再对照复盘页）
// 测试用例与一刷归档保持一致（示例 3 组 + 边界 6 组）
//
// ============================================================

import java.util.*;

public class LC0140_WordBreakIi {

    // ==== 提交代码开始 ====
    List<String> ans;
    boolean[] canReach;
    public List<String> wordBreak(String s, List<String> wordDict) {
        ans = new ArrayList<>();
        canReach = new boolean[s.length() + 1];
        canReach[s.length()] = true;
        Set<String> set = new HashSet<>(wordDict);
        for (int i = s.length(); i >= 0; i--) {
            for (String word : set) {
                if (i + word.length() <= s.length() && canReach[i + word.length()] && s.startsWith(word, i)) {
                    canReach[i] = true;
                    break;
                }
            }
        }
        dfs(s, 0, set, new ArrayList<String>());
        return ans;
    }

    private void dfs(String s, int i, Set<String> set, ArrayList<String> path) {
        if (i == s.length()) {
            ans.add(String.join(" ", path));
            return;
        }

        if (!canReach[i]) {
            return;
        }

        for (String word : set) {
            if (i + word.length() > s.length()) {
                continue;
            }
            if (s.startsWith(word, i)) {
                path.add(word);
                dfs(s, i + word.length(), set, path);
                path.remove(path.size() - 1);
            }
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0140_WordBreakIi s = new LC0140_WordBreakIi();
        int failures = 0;

        // 注：结果顺序任意 → 统一用无序比较
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("cats and dog", "cat sand dog"), s.wordBreak("catsanddog", Arrays.asList("cat", "cats", "and", "sand", "dog")), "示例1"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("pine apple pen apple", "pineapple pen apple", "pine applepen apple"), s.wordBreak("pineapplepenapple", Arrays.asList("apple", "pen", "applepen", "pine", "pineapple")), "示例2"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.wordBreak("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")), "示例3"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= s.length <= 20，1 <= wordDict.length <= 1000，词可重复使用；答案顺序任意
        // 边界1: 单字符命中
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("a"), s.wordBreak("a", Arrays.asList("a")), "边界1: 单字符命中"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        // 边界2: 完全无法拆分
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.wordBreak("a", Arrays.asList("b")), "边界2: 无法拆分"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }
        // 边界3: 整串即单词 + 细分并存
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("abc", "a bc"), s.wordBreak("abc", Arrays.asList("abc", "a", "bc")), "边界3: 整词+细分"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }
        // 边界4: 单词重复使用（a、aa 拼出 aaa）
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("a a a", "a aa", "aa a"), s.wordBreak("aaa", Arrays.asList("a", "aa")), "边界4: 词重复用"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }
        // 边界5: 词可细拆但剩余不可拼（abcd 只有 ab|cd）
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("ab cd"), s.wordBreak("abcd", Arrays.asList("ab", "cd", "abc")), "边界5: 剩余不可拼"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }
        // 边界6: 长度 20 上限，20 个 a 用 {a,aa} 拼 → 方案数 F(21)=10946
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 20; i++) sb.append('a');
            List<String> r = s.wordBreak(sb.toString(), Arrays.asList("a", "aa"));
            boolean ok = r.size() == 10946;
            if (ok) for (String sentence : r) {
                String joined = sentence.replace(" ", "");
                if (!joined.equals(sb.toString())) ok = false;
                for (String w : sentence.split(" ")) if (!w.equals("a") && !w.equals("aa")) ok = false;
            }
            if (!TestUtil.checkEq(true, ok, "边界6: 20长度方案数")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
