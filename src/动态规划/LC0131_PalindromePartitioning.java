// ============================================================
// LeetCode 131. 分割回文串 (Palindrome Partitioning)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/palindrome-partitioning/
// 刷题日期：2026-08-26
//
// 思路：区间 DP 预判回文 + 回溯枚举——isPal[i][j] 按区间长度递增填充（len==1 单独判 s[i]==s[j]，
//       更长区间查 isPal[i+1][j-1]）；回溯从 start 起枚举每段终点，查表判回文后切段递归、返回撤销
// DP 子类型：区间 DP（回文预判）——isPal[i][j] 由 isPal[i+1][j-1] 转移，
//            枚举主体是回溯：在每个可切位置选「当前段是否回文」，回文则切下去
// 复杂度：时间 O(n² 预判 + 方案数×n 回溯)；空间 O(n²)（isPal 表）+ O(n) 递归栈
// ============================================================

import java.util.*;

public class LC0131_PalindromePartitioning {

    // ==== 提交代码开始 ====
    List<List<String>> ans;
    boolean[][] isPal;
    public List<List<String>> partition(String s) {
        ans = new ArrayList<>();
        if (s.isEmpty()) {
            return ans;
        }
        isPal = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            isPal[i][i] = true;
        }
        for (int len = 1; len < s.length(); len++) {
            for (int i = 0; i < s.length() - len; i++) {
                int j = i + len;
                if (len == 1) {
                    isPal[i][j] = s.charAt(i) == s.charAt(j);
                } else {
                    isPal[i][j] = s.charAt(i) == s.charAt(j) && isPal[i + 1][j - 1];
                }
            }
        }
        dfs(s, 0, new ArrayList<>());
        return ans;
    }

    private void dfs(String s, int start, ArrayList<String> list) {
        if (start == isPal.length) {
            ans.add(new ArrayList<>(list));
            return;
        }
        for (int i = start; i < s.length(); i++) {
            if (!isPal[start][i]) {
                continue;
            }
            list.add(s.substring(start, i + 1));
            dfs(s, i + 1, list);
            list.remove(list.size() - 1);
        }
    }


    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0131_PalindromePartitioning s = new LC0131_PalindromePartitioning();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList("a", "a", "b"), Arrays.asList("aa", "b")), s.partition("aab"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList("a")), s.partition("a"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= s.length <= 16，仅小写字母；方案顺序任意 → 外层用无序比较
        // 边界1: 双字符回文 "aa" → 两种切法
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList("a", "a"), Arrays.asList("aa")), s.partition("aa"), "边界1: 双字符回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: "aba" → 全拆 + 整串两种
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList("a", "b", "a"), Arrays.asList("aba")), s.partition("aba"), "边界2: 奇数回文整串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: "abba" → [a|b|b|a, a|bb|a, abba] 三种
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(
                    Arrays.asList("a", "b", "b", "a"),
                    Arrays.asList("a", "bb", "a"),
                    Arrays.asList("abba")), s.partition("abba"), "边界3: 嵌套回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 长度 16 上限全同字符 'a'——任意分割都回文，方案数 = 2^15 = 32768
        try {
            String allA = "aaaaaaaaaaaaaaaa";
            List<List<String>> r = s.partition(allA);
            boolean ok = r.size() == 32768;
            if (ok) for (List<String> seg : r) if (seg.isEmpty() || !String.join("", seg).equals(allA)) ok = false;
            if (!TestUtil.checkEq(true, ok, "边界4: 16全同上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界5: 长度 16 上限全不同字符——只有全拆一种方案
        try {
            String allDiff = "abcdefghijklmnop";
            List<List<String>> r = s.partition(allDiff);
            boolean ok = r.size() == 1 && r.get(0).size() == 16 && String.join("", r.get(0)).equals(allDiff);
            if (!TestUtil.checkEq(true, ok, "边界5: 16全不同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}