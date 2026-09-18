// ============================================================
// LeetCode 139. 单词拆分 (Word Break)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/word-break/
// 二刷日期：2026-09-18 | 一刷：2026-08-10（Accepted，一次 AC，最优写法）
// 一刷思路与代码见复盘页 reviews/动态规划/…，此处不写以保留回忆空间
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归），请勿删改
// ============================================================

import java.util.*;

public class LC0139_WordBreak {

    // ==== 提交代码开始 ====
    public boolean wordBreak(String s, List<String> wordDict) {
        int n = s.length();
        boolean[] canReach = new boolean[n + 1];
        canReach[n] = true;
        Set<String> set = new HashSet<>(wordDict);
        for (int i = s.length() - 1; i >= 0; i--) {
            for (String word : set) {
                if (i + word.length() <= n && canReach[i + word.length()]) {
                    if (s.startsWith(word, i)) {
                        canReach[i] = true;
                        break;
                    }
                }
            }
        }

        return canReach[0];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0139_WordBreak s = new LC0139_WordBreak();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.wordBreak("leetcode", Arrays.asList("leet", "code")), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.wordBreak("applepenapple", Arrays.asList("apple", "pen")), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.wordBreak("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(true, s.wordBreak("a", Arrays.asList("a")), "边界-单字符命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单字符命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.wordBreak("a", Arrays.asList("b")), "边界-单字符未命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单字符未命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.wordBreak("abc", Arrays.asList("ab", "c")), "边界-两种切分")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两种切分 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.wordBreak("abc", Arrays.asList("ab", "b")), "边界-必须完整覆盖")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-必须完整覆盖 异常: " + t); }
        try {
            String longA = "a".repeat(300);
            if (!TestUtil.checkEq(true, s.wordBreak(longA, Arrays.asList("a")), "边界-长串")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长串 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.wordBreak("ab", Arrays.asList("ab", "b")), "边界-回归:短词覆盖长词")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-回归:短词覆盖长词 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
