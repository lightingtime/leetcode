// ============================================================
// LeetCode 387. 字符串中的第一个唯一字符 (First Unique Character in a String)
// 难度：Easy | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/first-unique-character-in-a-string/
// 刷题日期：2026-08-24
//
// ============================================================

import java.util.*;

public class LC0387_FirstUniqueCharacterInAString {

    // ==== 提交代码开始 ====
    public int firstUniqChar(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        for (int i = 0; i < s.length(); i++) {
            if (count[s.charAt(i) - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0387_FirstUniqueCharacterInAString s = new LC0387_FirstUniqueCharacterInAString();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(0, s.firstUniqChar("leetcode"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.firstUniqChar("loveleetcode"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.firstUniqChar("aabb"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 约束：1 <= s.length <= 10^5，仅小写字母（无空串）
        // 边界1: 单字符（最小长度）
        try {
            if (!TestUtil.checkEq(0, s.firstUniqChar("a"), "边界1: 单字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 全相同字符，无唯一
        try {
            if (!TestUtil.checkEq(-1, s.firstUniqChar("aaaaa"), "边界2: 全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 唯一字符在末尾
        try {
            if (!TestUtil.checkEq(4, s.firstUniqChar("aabbc"), "边界3: 唯一在末尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 唯一字符在开头
        try {
            if (!TestUtil.checkEq(0, s.firstUniqChar("zabcabc"), "边界4: 唯一在开头")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 长度 10^5 上限，唯一字符在末尾（a*99999 + z）
        try {
            String big = "a".repeat(99999) + "z";
            if (!TestUtil.checkEq(99999, s.firstUniqChar(big), "边界5: 10^5上限唯一在末尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 长度 10^5 上限，唯一字符在开头（z + a*99999）
        try {
            String big = "z" + "a".repeat(99999);
            if (!TestUtil.checkEq(0, s.firstUniqChar(big), "边界6: 10^5上限唯一在开头")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}