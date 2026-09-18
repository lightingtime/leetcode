// ============================================================
// LeetCode 76. 最小覆盖子串 (Minimum Window Substring)
// 难度：Hard | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/minimum-window-substring/
// 复习日期：2026-09-18（第 2 次复习 · 一刷 2026-08-05 · 一刷一次 Accepted）
// 一刷写法：滑动窗口 + 计数数组——右指针扩展时「先判后减」更新已满足个数，窗口覆盖 t 后收缩左指针（只删多余的字符），每次收缩前/后记录最短窗口。O(m+n)/O(1)
// 一刷踩过的坑（错误习惯库有 4 条）：① 扩展时先减后判，首次遇到的必需字符判不出来，count 永不更新；② 收缩条件用「计数 ≤ 0」，把恰好满足的必需字符也还回去，覆盖状态与 count 脱节；③ 字符映射用 c - 'a' 遇大写直接负数越界；④ 最短窗口的 min 变量只比较不赋值，最后留下的是最后一个合法窗口而不是最短的（反例 s=xxabxxa, t=ab 会返回 bxxa）
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 9 个）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0076_MinimumWindowSubstring {

    // ==== 提交代码开始 ====
    public String minWindow(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray()) {
            need[c]++;
        }
        // counts：还差几个必需字符没被满足；0 表示当前窗口已覆盖 t
        int counts = t.length();
        String ans = "";
        int min = Integer.MAX_VALUE;
        int start = 0;
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            // 判断只决定计数器：只有「还没被满足的必需字符」才让 counts 减 1
            if (need[c] > 0) {
                counts--;
            }
            // 减法无条件执行：多余字符与不在 t 里的字符被减成负数，收缩时才有「可删」的凭证
            need[c]--;
            if (counts == 0) {
                // 收缩只删多余（need < 0），恰好满足的字符留着
                while (need[s.charAt(start)] < 0) {
                    need[s.charAt(start)]++;
                    start++;
                }
                if (i - start + 1 < min) {
                    min = i - start + 1;
                    ans = s.substring(start, i + 1);
                }
                // 记录后把左端必需字符还回去，窗口整体前移继续找
                need[s.charAt(start)]++;
                counts++;
                start++;
            }
            i++;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0076_MinimumWindowSubstring s = new LC0076_MinimumWindowSubstring();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("BANC", s.minWindow("ADOBECODEBANC", "ABC"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("a", s.minWindow("a", "a"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq("", s.minWindow("a", "aa"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（滑动窗口：长度/位置/重复字符/大小写）----
        try {
            if (!TestUtil.checkEq("", s.minWindow("abc", "abcd"), "边界1-s比t短")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-s比t短 异常: " + t); }
        try {
            if (!TestUtil.checkEq("ABC", s.minWindow("ABCXYZ", "ABC"), "边界2-窗口在开头")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-窗口在开头 异常: " + t); }
        try {
            if (!TestUtil.checkEq("ABC", s.minWindow("XYZABC", "ABC"), "边界3-窗口在结尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-窗口在结尾 异常: " + t); }
        try {
            if (!TestUtil.checkEq("AAB", s.minWindow("AAAB", "AAB"), "边界4-t含重复字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-t含重复字符 异常: " + t); }
        try {
            if (!TestUtil.checkEq("AB", s.minWindow("abAB", "AB"), "边界5-大小写敏感")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-大小写敏感 异常: " + t); }
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 99999; i++) sb.append('a');
            sb.append('b');                                    // 目标字符只出现在末尾
            if (!TestUtil.checkEq("b", s.minWindow(sb.toString(), "b"), "边界6-长度 1e5 末尾命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq("aA", s.minWindow("aA", "aA"), "边界7-大小写混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq("abbbc", s.minWindow("aaabbbccc", "abc"), "边界8-重复字符精确计数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            // 更短的窗口出现在更长的窗口之后被记录的场景
            if (!TestUtil.checkEq("ab", s.minWindow("xxabxxa", "ab"), "边界9-短窗口不一定是最后一次记录")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }
        try {
            // 二刷分析补入：count 无条件自减的最小复现（扫满 t.length() 个字符就误判覆盖）
            if (!TestUtil.checkEq("a", s.minWindow("xa", "a"), "边界10-无关字符开头最小复现")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
