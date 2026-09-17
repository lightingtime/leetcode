// ============================================================
// LeetCode 140. 单词拆分 II (Word Break II)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/word-break-ii/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-26）
// 子类型：线性 DP（序列分割）——dp[i] = s 的后缀 s[i..n) 能否被拆成词典里的单词
// 一刷写法：先用 dp 预判「后缀可拆」（从右往左填），再回溯枚举所有切分；dp 剪枝掉不可达分支，避免指数级搜索
// 一刷问题：回溯里共享 StringBuilder 状态不对称（子层删了末尾空格没恢复就 return，父层按下标 delete 导致错乱）；dp 方向用错——前缀可拆 dp[i] 不能当后缀可达用，差一个字符的边界 >0/>=0 也导致 dp 欠算、砍掉正确路径
// 上次复习写法：后缀可拆预判+回溯，一次通过但过程有探讨（判较弱）
// 测试用例：与一刷归档保持一致（示例 3 + 边界（含无解、单字符、长串与重复词））
//
// 思路：后缀可达 DP 预判 + 回溯枚举——canReach[i] = s[i..n) 能否拆成词典单词，从右往左填（基例 canReach[n]=true）；
//       回溯到下标 len 时先用 canReach[len] 剪掉到不了终点的分支，再枚举命中词典的词继续切
// 复杂度：时间 O(n · |dict| · L + 输出规模)，空间 O(n + 输出规模)（L 为单词长度，startsWith 比较代价）
// ============================================================

import java.util.*;

public class LC0140_WordBreakIi {

    // ==== 提交代码开始 ====
    boolean[] canReach;
    List<String> ans;
    public List<String> wordBreak(String s, List<String> wordDict) {
        ans = new ArrayList<>();
        canReach = new boolean[s.length() + 1];
        // 基例对齐后缀语义：canReach[i] 问的是 s[i..n) 能否走到终点，故唯一初始可达状态是空后缀 s[n..n)
        canReach[s.length()] = true;
        Set<String> set = new HashSet<>(wordDict);
        for (int i = s.length() - 1; i >= 0; i--) {
            for (String word : set) {

                if (i + word.length() <= s.length() && s.startsWith(word, i) && canReach[i + word.length()]) {
                    canReach[i] = true;
                    break;
                }
            }
        }
        dfs(s, 0, set, new ArrayList<>());
        return ans;
    }

    private void dfs(String s, int len, Set<String> set, List<String> path) {
        if (len == s.length()) {
            ans.add(String.join(" ", path));
            return;
        }

        if (!canReach[len]) {
            return;
        }

        for (String word : set) {
            if (len + word.length() > s.length()) {
                continue;
            }
            if (s.startsWith(word, len)) {
                path.add(word);
                dfs(s, len + word.length(), set, path);
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
            if (!TestUtil.checkEqUnordered(Arrays.asList("cats and dog", "cat sand dog"), s.wordBreak("catsanddog", Arrays.asList("cat", "cats", "and", "sand", "dog")), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("pine apple pen apple", "pineapple pen apple", "pine applepen apple"), s.wordBreak("pineapplepenapple", Arrays.asList("apple", "pen", "applepen", "pine", "pineapple")), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.wordBreak("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= s.length <= 20，1 <= wordDict.length <= 1000，词可重复使用；答案顺序任意
        // 边界1: 单字符命中
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("a"), s.wordBreak("a", Arrays.asList("a")), "边界1: 单字符命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 完全无法拆分
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.wordBreak("a", Arrays.asList("b")), "边界2: 无法拆分")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 整串即单词 + 细分并存
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("abc", "a bc"), s.wordBreak("abc", Arrays.asList("abc", "a", "bc")), "边界3: 整词+细分")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 单词重复使用（a、aa 拼出 aaa）
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("a a a", "a aa", "aa a"), s.wordBreak("aaa", Arrays.asList("a", "aa")), "边界4: 词重复用")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 词可细拆但剩余不可拼（abcd 只有 ab|cd）
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("ab cd"), s.wordBreak("abcd", Arrays.asList("ab", "cd", "abc")), "边界5: 剩余不可拼")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
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
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
