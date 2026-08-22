// ============================================================
// LeetCode 395. 至少有 K 个重复字符的最长子串 (Longest Substring with At Least K Repeating Characters)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/longest-substring-with-at-least-k-repeating-characters/
// 刷题日期：2026-08-22
//
// 思路：分治 —— 统计当前区间频次，找到第一个出现次数 < k 的字符（墙），它不可能在合法子串中；跳过连续墙后对墙左边与右边分别递归，取最大。
// 复杂度：时间 O(n^2) 最坏（每层最多 2 个互不重叠子问题），空间 O(n) 递归栈
// ============================================================


public class LC0395_LongestSubstringWithAtLeastKRepeatingCharacters {

    // ==== 提交代码开始 ====
    public int longestSubstring(String s, int k) {
        return dfs(s.toCharArray(), 0, s.length(), k);
    }

    private int dfs(char[] ch, int start, int end, int k) {
        if (end - start < k) {
            return 0;
        }
        int[] count = new int[26];
        for (int i = start; i < end; i++) {
            count[ch[i] - 'a']++;
        }
        for (int i = start; i < end; i++) {
            if (count[ch[i] - 'a'] < k) {
                int j = i + 1;
                while (j < end && count[ch[j] - 'a'] < k) {
                    j++;
                }
                return Math.max(dfs(ch, start, i, k), dfs(ch, j, end, k));
            }
        }
        return end - start;
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

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}