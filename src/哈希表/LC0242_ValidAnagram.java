// ============================================================
// LeetCode 242. 有效的字母异位词 (Valid Anagram)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/valid-anagram/
// 刷题日期：2026-08-21
//
// ============================================================

import java.util.*;

public class LC0242_ValidAnagram {

    // ==== 提交代码开始 ====
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            map.merge(s.charAt(i), 1, Integer::sum);
            map.merge(t.charAt(i), -1, Integer::sum);
        }
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            if (entry.getValue() != 0) {
                return false;
            }
        }
        return true;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0242_ValidAnagram s = new LC0242_ValidAnagram();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.isAnagram("anagram", "nagaram"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isAnagram("rat", "car"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: 单字符相同（n 下限）
        try { if (!TestUtil.checkEq(true, s.isAnagram("a", "a"), "边界1: 单字符相同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: 单字符不同
        try { if (!TestUtil.checkEq(false, s.isAnagram("a", "b"), "边界2: 单字符不同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }

        // 边界3: 长度不同（即使字符集合相同也必然不是异位词）
        try { if (!TestUtil.checkEq(false, s.isAnagram("ab", "a"), "边界3: 长度不同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }

        // 边界4: 字母集合相同但出现次数不同（'a' 多一个，漏次数会误判）
        try { if (!TestUtil.checkEq(false, s.isAnagram("aaab", "aab"), "边界4: 次数不同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: 同字符不同排列（真正的异位词）
        try { if (!TestUtil.checkEq(true, s.isAnagram("aab", "aba"), "边界5: 同字符异序")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        // 边界6: 两个完全相同的字符串
        try { if (!TestUtil.checkEq(true, s.isAnagram("hello", "hello"), "边界6: 完全相同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        // 边界7: 26 个小写字母全出场，顺序整体反转
        try { if (!TestUtil.checkEq(true, s.isAnagram("abcdefghijklmnopqrstuvwxyz", "zyxwvutsrqponmlkjihgfedcba"), "边界7: 全字母表反转")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        // 边界8: n 上限 5*10^4 全相同
        String bigA = "a".repeat(50_000);
        try { if (!TestUtil.checkEq(true, s.isAnagram(bigA, bigA), "边界8: 5万全相同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        // 边界9: n 上限且长度相同但字母完全不同
        String bigB = "b".repeat(50_000);
        try { if (!TestUtil.checkEq(false, s.isAnagram(bigA, bigB), "边界9: 5万全不同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}