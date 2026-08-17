// ============================================================
// LeetCode 438. 找到字符串中所有字母异位词 (Find All Anagrams in a String)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/find-all-anagrams-in-a-string/
// 刷题日期：2026-08-11
//
// ============================================================

import java.util.*;

public class LC0438_FindAllAnagramsInAString {

    // ==== 提交代码开始 ====
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        if (s.isEmpty() || s.length() < p.length()) {
            return ans;
        }
        int[] array = new int[26];
        int[] target = new int[26];
        for (char c : p.toCharArray()) {
            target[c - 'a']++;
        }
        int l = 0, r = 0;
        while (r < s.length()) {
            if (r - l < p.length()) {
                array[s.charAt(r) - 'a']++;
                r++;
            } else {
                array[s.charAt(l) - 'a']--;
                array[s.charAt(r) - 'a']++;
                r++;
                l++;
            }
            if (Arrays.equals(array, target)) {
                ans.add(l);
            }
        }
        return ans;
    }

    private boolean arrayEquals(int[] array, int[] target) {
        for (int i = 0; i < target.length; i++) {
            if (target[i] != array[i]) {
                return false;
            }
        }
        return true;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0438_FindAllAnagramsInAString s = new LC0438_FindAllAnagramsInAString();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList(0, 6), s.findAnagrams("cbaebabacd", "abc"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(0, 1, 2), s.findAnagrams("abab", "ab"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq(Arrays.asList(), s.findAnagrams("a", "ab"), "p比s长")) failures++; } catch (Throwable t) { failures++; System.out.println("p比s长 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(0), s.findAnagrams("a", "a"), "单字符匹配")) failures++; } catch (Throwable t) { failures++; System.out.println("单字符匹配 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(), s.findAnagrams("ab", "c"), "单字符不匹配")) failures++; } catch (Throwable t) { failures++; System.out.println("单字符不匹配 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(), s.findAnagrams("xyz", "ab"), "无匹配")) failures++; } catch (Throwable t) { failures++; System.out.println("无匹配 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(0, 1, 2), s.findAnagrams("aaaa", "aa"), "全重复窗口")) failures++; } catch (Throwable t) { failures++; System.out.println("全重复窗口 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(0), s.findAnagrams("abc", "abc"), "s等于p")) failures++; } catch (Throwable t) { failures++; System.out.println("s等于p 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(1), s.findAnagrams("baa", "aa"), "重叠窗口")) failures++; } catch (Throwable t) { failures++; System.out.println("重叠窗口 异常: " + t); }
        try {
            List<Integer> expected = new ArrayList<>();
            for (int i = 0; i < 9; i++) expected.add(i);
            if (!TestUtil.checkEq(expected, s.findAnagrams("aaaaaaaaaa", "aa"), "长串全匹配")) failures++;
        } catch (Throwable t) { failures++; System.out.println("长串全匹配 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
