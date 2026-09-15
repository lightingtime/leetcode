// ============================================================
// LeetCode 395. 至少有 K 个重复字符的最长子串 (Longest Substring with At Least K Repeating Characters)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/longest-substring-with-at-least-k-repeating-characters/
// 复习日期：2026-09-15（第 3 次复习 · 一刷 2026-08-22 · 上次 2026-09-12 较弱）
// 一刷/上次思路：分治（<k 的字符当墙切左右，跳过连续墙），O(n²) 最坏 —— 非最优
// 复习目标：独立写出比一刷 O(n²) 更好的写法（能到 O(n)/O(26n) 更好），并说清正确性依据
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0395_LongestSubstringWithAtLeastKRepeatingCharacters {

    // ==== 提交代码开始 ====
    public int longestSubstring(String s, int k) {
        return dfs(s, 0, s.length() - 1, k);
    }

    private int dfs(String s, int l, int r, int k) {
        if (l > r) {
            return 0;
        }
        int[] counts = new int[26];
        for (int i = l; i <= r; i++) {
            counts[s.charAt(i) - 'a']++;
        }
        int p = -1;
        for (int i = l; i <= r; i++) {
            if (counts[s.charAt(i) - 'a'] < k) {
                p = i;
                break;
            }
        }
        if (p == -1) {
            return r - l + 1;
        }
        int q = p;
        while (q <= r && counts[s.charAt(q) - 'a'] < k) {
            q++;
        }

        return Math.max(dfs(s, l, p - 1, k), dfs(s, q, r, k));
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0395_LongestSubstringWithAtLeastKRepeatingCharacters s = new LC0395_LongestSubstringWithAtLeastKRepeatingCharacters();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.longestSubstring("aaabb", 3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.longestSubstring("ababbc", 2), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) k=1：任意子串都合法，整个串最长
        try { if (!TestUtil.checkEq(3, s.longestSubstring("abc", 1), "边界1-k1全串")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-k1全串 异常: " + t); }
        // 2) k > s.length：不存在合法子串
        try { if (!TestUtil.checkEq(0, s.longestSubstring("ab", 3), "边界2-k大于串长")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-k大于串长 异常: " + t); }
        // 3) 单字符：k=1 合法，k=2 不合法
        try {
            boolean ok = true;
            ok &= TestUtil.checkEq(1, s.longestSubstring("a", 1), "边界3a-单字符k1");
            ok &= TestUtil.checkEq(0, s.longestSubstring("a", 2), "边界3b-单字符k2");
            if (!ok) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-单字符 异常: " + t); }
        // 4) 全相同且 k 满足：整个串合法
        try { if (!TestUtil.checkEq(4, s.longestSubstring("aaaa", 2), "边界4-全相同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-全相同 异常: " + t); }
        // 5) 全相同但 k 超过出现次数：返回 0
        try { if (!TestUtil.checkEq(0, s.longestSubstring("aaaa", 5), "边界5-全相同k过大")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5-全相同k过大 异常: " + t); }
        // 6) 存在出现次数不足 k 的字符，需切分取合法段
        try { if (!TestUtil.checkEq(3, s.longestSubstring("aaabbc", 3), "边界6-切分")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6-切分 异常: " + t); }
        // 7) 两字符出现次数恰好都 >= k：整串合法
        try { if (!TestUtil.checkEq(4, s.longestSubstring("aabb", 2), "边界7-恰好满足")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7-恰好满足 异常: " + t); }
        // 8) 两字符都出现不足 k 次：返回 0
        try { if (!TestUtil.checkEq(0, s.longestSubstring("ab", 2), "边界8-均不足")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8-均不足 异常: " + t); }
        // 9) 上限长度 10^4：一半字符恰好不足 k，只能取前一半
        try { if (!TestUtil.checkEq(5000, s.longestSubstring("a".repeat(5000) + "b".repeat(4999), 5000), "边界9-上限切分")) failures++; } catch (Throwable t) { failures++; System.out.println("边界9-上限切分 异常: " + t); }
        // 10) 上限长度 10^4 且全相同、k=10^4：整串合法
        try { if (!TestUtil.checkEq(10000, s.longestSubstring("a".repeat(10000), 10000), "边界10-上限全同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界10-上限全同 异常: " + t); }
        // 11) 答案在最后一个墙字符之后的段（baaac：b、c 为墙，合法段 aaa 在末尾）
        try { if (!TestUtil.checkEq(3, s.longestSubstring("baaac", 3), "边界11-尾段答案")) failures++; } catch (Throwable t) { failures++; System.out.println("边界11-尾段答案 异常: " + t); }
        // 12) 答案在末尾两墙之后（cbabb：c、a 为墙，合法段 bb 在末尾）
        try { if (!TestUtil.checkEq(2, s.longestSubstring("cbabb", 2), "边界12-尾段答案2")) failures++; } catch (Throwable t) { failures++; System.out.println("边界12-尾段答案2 异常: " + t); }

        // 回归：全局频率满足 k，不代表切分后的当前区间内部满足 k
        // "aabacb" 全局 a=3、b=2，但 "aaba" 内部 b 只有 1 次，最长合法段实际是 "aa"
        try { if (!TestUtil.checkEq(2, s.longestSubstring("aabacb", 2), "回归-窗口内频率不足")) failures++; } catch (Throwable t) { failures++; System.out.println("回归-窗口内频率不足 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}