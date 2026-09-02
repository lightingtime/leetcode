// ============================================================
// LeetCode 49. 字母异位词分组 (Group Anagrams)
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/group-anagrams/
// 复习日期：2026-09-03（第 3 次复习 · 一刷 2026-08-03 · 上次 2026-08-31 较强）
// 一刷/历次思路：计数编码键 + 哈希表（key=26 个字母计数的编码串，同组异位词键相同）O(n·L)
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0049_GroupAnagrams {

    // ==== 提交代码开始 ====
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            String key = getKey(str);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
        }
        return map.values().stream().toList();
    }

    private String getKey(String str) {
        int[] count = new int[26];
        for (char c : str.toCharArray()) {
            count[c - 'a']++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count.length; i++) {
            sb.append(i + 'a').append(count[i]);
        }
        return sb.toString();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0049_GroupAnagrams s = new LC0049_GroupAnagrams();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        String[] t1 = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> e1 = List.of(
                List.of("bat"),
                List.of("nat", "tan"),
                List.of("ate", "eat", "tea"));
        if (!TestUtil.checkEqUnordered(e1, s.groupAnagrams(t1), "示例1")) failures++;

        String[] t2 = {""};
        if (!TestUtil.checkEqUnordered(List.of(List.of("")), s.groupAnagrams(t2), "示例2")) failures++;

        String[] t3 = {"a"};
        if (!TestUtil.checkEqUnordered(List.of(List.of("a")), s.groupAnagrams(t3), "示例3")) failures++;

        // ---- 边界测试（约束：1 <= strs.length <= 10^4，0 <= strs[i].length <= 100，仅小写字母）----
        // 边界1-全相同字符串归一组
        String[] t4 = {"abc", "abc", "abc"};
        List<List<String>> e4 = List.of(List.of("abc", "abc", "abc"));
        if (!TestUtil.checkEqUnordered(e4, s.groupAnagrams(t4), "边界1-全相同")) failures++;

        // 边界2-空字符串与普通字符串混合（长度可为 0）
        String[] t5 = {"", "a", ""};
        List<List<String>> e5 = List.of(List.of("", ""), List.of("a"));
        if (!TestUtil.checkEqUnordered(e5, s.groupAnagrams(t5), "边界2-空串混合")) failures++;

        // 边界3-长度上限 100 的互异位词（顺序不同仍应同组）
        String s1 = "a".repeat(50) + "b".repeat(50);
        String s2 = "b".repeat(50) + "a".repeat(50);
        String[] t6 = {s1, s2};
        List<List<String>> e6 = List.of(List.of(s1, s2));
        if (!TestUtil.checkEqUnordered(e6, s.groupAnagrams(t6), "边界3-长度100互异位词")) failures++;

        // 边界4-数量上限 10^4 的数组：两类各 5000 个
        String[] t7 = new String[10000];
        List<String> e7a = new ArrayList<>(), e7b = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            t7[i] = (i % 2 == 0) ? "x".repeat(100) : "y".repeat(100);
            (i % 2 == 0 ? e7a : e7b).add(t7[i]);
        }
        if (!TestUtil.checkEqUnordered(List.of(e7a, e7b), s.groupAnagrams(t7), "边界4-万级数组")) failures++;

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
