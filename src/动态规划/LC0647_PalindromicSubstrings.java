// ============================================================
// LeetCode 647. 回文子串 (Palindromic Substrings)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/palindromic-substrings/
// 刷题日期：2026-08-20
//
// 思路：中心扩散：枚举 2n-1 个奇数/偶数回文中心，向两侧扩展并计数。
// 复杂度：时间 O(n^2)，空间 O(1)
// ============================================================

public class LC0647_PalindromicSubstrings {

    // ==== 提交代码开始 ====
    public int countSubstrings(String s) {
        int ans = 0;
        int n = s.length();
        for (int i = 0; i < 2 * n - 1; i++) {
            int l = i / 2, r = (i + 1) / 2;
            while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
                l--;
                r++;
                ans++;
            }
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0647_PalindromicSubstrings s = new LC0647_PalindromicSubstrings();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.countSubstrings("abc"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.countSubstrings("aaa"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对回文子串计数设计）----
        // 空串：0 个子串
        try {
            if (!TestUtil.checkEq(0, s.countSubstrings(""), "边界-空串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-空串 异常: " + t); }
        // 单字符：只有它自己
        try {
            if (!TestUtil.checkEq(1, s.countSubstrings("a"), "边界-单字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单字符 异常: " + t); }
        // 两字符不同：无长度为 2 的回文，只数 2 个单字符
        try {
            if (!TestUtil.checkEq(2, s.countSubstrings("ab"), "边界-两字符不同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两字符不同 异常: " + t); }
        // 两字符相同：a、a、aa 共 3 个
        try {
            if (!TestUtil.checkEq(3, s.countSubstrings("aa"), "边界-两字符相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两字符相同 异常: " + t); }
        // 奇数中心：aba = 3 个单字符 + aba = 4
        try {
            if (!TestUtil.checkEq(4, s.countSubstrings("aba"), "边界-奇数中心")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-奇数中心 异常: " + t); }
        // 偶数中心：abba = 4 个单字符 + bb + abba = 6
        try {
            if (!TestUtil.checkEq(6, s.countSubstrings("abba"), "边界-偶数中心")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-偶数中心 异常: " + t); }
        // 混合多个中心：abab = 4 个单字符 + aba + bab = 6（无长度 2 回文）
        try {
            if (!TestUtil.checkEq(6, s.countSubstrings("abab"), "边界-交替无长2回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-交替无长2回文 异常: " + t); }
        // 嵌套回文：abcba = 5 个单字符 + bcb + abcba = 7
        try {
            if (!TestUtil.checkEq(7, s.countSubstrings("abcba"), "边界-嵌套回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-嵌套回文 异常: " + t); }
        // 非全中心回文：aabaa = 5 单 + aa、aa + aba + aabaa = 9
        try {
            if (!TestUtil.checkEq(9, s.countSubstrings("aabaa"), "边界-非全中心回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-非全中心回文 异常: " + t); }
        // 全相同 4 个：n(n+1)/2 = 10，验证中心扩散不重复计数
        try {
            if (!TestUtil.checkEq(10, s.countSubstrings("aaaa"), "边界-全相同4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同4 异常: " + t); }
        // 长度上限 1000 全相同：1000*1001/2 = 500500，验证性能与不溢出
        {
            char[] big = new char[1000];
            java.util.Arrays.fill(big, 'a');
            try {
                if (!TestUtil.checkEq(500500, s.countSubstrings(new String(big)), "边界-长度1000全相同")) failures++;
            } catch (Throwable t) { failures++; System.out.println("边界-长度1000全相同 异常: " + t); }
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}