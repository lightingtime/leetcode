// ============================================================
// LeetCode 28. 找出字符串中第一个匹配项的下标 (Find the Index of the First Occurrence in a String)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
// 刷题日期：2026-08-22
//
// 思路：KMP —— 先对 needle 构建前缀函数 pi（最长相等前后缀），匹配时不匹配就按 pi 回退，避免像暴力法那样每次都从头重比。
// 复杂度：时间 O(n+m)，空间 O(m)
// ============================================================


public class LC0028_FindTheIndexOfTheFirstOccurrenceInAString {

    // ==== 提交代码开始 ====
    public int strStr(String haystack, String needle) {
        int[] pi = new int[needle.length()];
        int j = 0;
        for (int i = 1; i < needle.length(); i++) {
            while (j > 0 && needle.charAt(j) != needle.charAt(i)) {
                j = pi[j - 1];
            }
            if (needle.charAt(i) == needle.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        j = 0;
        for (int i = 0; i < haystack.length(); i++) {
            while (j > 0 && haystack.charAt(i) != needle.charAt(j)) {
                j = pi[j - 1];
            }
            if (haystack.charAt(i) == needle.charAt(j)) {
                j++;
            }
            if (j == needle.length()) {
                return i - j + 1;
            }
        }
        return -1;
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0028_FindTheIndexOfTheFirstOccurrenceInAString s = new LC0028_FindTheIndexOfTheFirstOccurrenceInAString();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(0, s.strStr("sadbutsad", "sad"), "示例1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(-1, s.strStr("leetcode", "leeto"), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（本题具体逻辑）----
        // 1) needle 匹配在 haystack 末尾
        try {
            if (!TestUtil.checkEq(3, s.strStr("hello", "lo"), "边界1-末尾匹配")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1-末尾匹配 异常: " + t);
        }
        // 2) 单字符且相等（长度都为 1）
        try {
            if (!TestUtil.checkEq(0, s.strStr("a", "a"), "边界2-单字符相等")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2-单字符相等 异常: " + t);
        }
        // 3) 单字符不相等
        try {
            if (!TestUtil.checkEq(-1, s.strStr("a", "b"), "边界3-单字符不等")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3-单字符不等 异常: " + t);
        }
        // 4) needle 比 haystack 长，必然不存在
        try {
            if (!TestUtil.checkEq(-1, s.strStr("abc", "abcd"), "边界4-needle更长")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4-needle更长 异常: " + t);
        }
        // 5) 全相同字符、允许重叠，返回第一次出现的位置 0
        try {
            if (!TestUtil.checkEq(0, s.strStr("aaaaa", "aa"), "边界5-全相同重叠")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5-全相同重叠 异常: " + t);
        }
        // 6) 经典易错样本：多个候选起点，只有一处真正匹配
        try {
            if (!TestUtil.checkEq(4, s.strStr("mississippi", "issip"), "边界6-经典错位")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6-经典错位 异常: " + t);
        }
        // 7) 模式串特征字母不在 haystack 中
        try {
            if (!TestUtil.checkEq(-1, s.strStr("aaaaa", "b"), "边界7-特征字母缺失")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界7-特征字母缺失 异常: " + t);
        }
        // 8) 约束上限：长度 10^4，完全相等返回 0
        try {
            if (!TestUtil.checkEq(0, s.strStr("a".repeat(10000), "a".repeat(10000)), "边界8-上限相等")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界8-上限相等 异常: " + t);
        }
        // 9) 约束上限：长度均为 10^4，needle 不存在
        try {
            if (!TestUtil.checkEq(-1, s.strStr("a".repeat(10000), "b".repeat(10000)), "边界9-上限不存在")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界9-上限不存在 异常: " + t);
        }
        // 10) 约束上限：needle=单字符且出现在末尾
        try {
            if (!TestUtil.checkEq(9999, s.strStr("a".repeat(9999) + "z", "z"), "边界10-上限末尾")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界10-上限末尾 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}