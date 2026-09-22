// ============================================================
// LeetCode 44. 通配符匹配 (Wildcard Matching)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/wildcard-matching/
// 复习日期：2026-09-22（第 3 次复习 · 一刷 2026-08-26）
// 子类型：动态规划（线性 DP / 双序列）
// 一刷思路：先自行回忆；卡住后再对照一刷归档与复盘页
// 测试用例：与一刷归档保持一致（示例 + 边界 + 回归）
//
// ============================================================

import java.util.*;

public class LC0044_WildcardMatching {

    // ==== 提交代码开始 ====
    public boolean isMatch(String s, String p) {
        int i = 0, j = 0;
        int matchIdx = -1, start = -1;
        while (i < s.length()) {
            if (j < p.length() && p.charAt(j) == '*') {
                matchIdx = j + 1;
                start = i;
                j++;
            } else if (j < p.length() && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '?')) {
                i++;
                j++;
            } else {
                if (matchIdx > -1) {
                    start++;
                    i = start;
                    j = matchIdx;
                } else {
                    return false;
                }
            }
        }
        while (j < p.length() && p.charAt(j) == '*') {
            j++;
        }
        return j == p.length();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0044_WildcardMatching s = new LC0044_WildcardMatching();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(false, s.isMatch("aa", "a"), "示例1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(true, s.isMatch("aa", "*"), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.isMatch("cb", "?a"), "示例3")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：0 <= s.length, p.length <= 2000（均可为空）；s 仅小写，p 含 ?/*
        // 边界1: 双空串
        try {
            if (!TestUtil.checkEq(true, s.isMatch("", ""), "边界1: 双空")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        // 边界2: 空 s 配各种 p
        try {
            if (!TestUtil.checkEq(true, s.isMatch("", "*"), "边界2a: 空串配*")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2a 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.isMatch("", "?"), "边界2b: 空串配?")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2b 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.isMatch("", "a"), "边界2c: 空串配字母")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2c 异常: " + t);
        }
        // 边界3: 连续星号配空串
        try {
            if (!TestUtil.checkEq(true, s.isMatch("", "****"), "边界3: 多星号配空")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }
        // 边界4: 星号吞中间段
        try {
            if (!TestUtil.checkEq(true, s.isMatch("abc", "a*c"), "边界4: 星号吞中段")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }
        // 边界5: 星号可吞任意长度，吞完必须接得上
        try {
            if (!TestUtil.checkEq(false, s.isMatch("abcd", "a*c"), "边界5: 星号接不上")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }
        // 边界6: 问号占位
        try {
            if (!TestUtil.checkEq(true, s.isMatch("abc", "???"), "边界6: 三个问号")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6 异常: " + t);
        }
        // 边界7: 长度 2000 上限——全等长精确匹配、* 万能、全不匹配
        try {
            StringBuilder sa = new StringBuilder(), pa = new StringBuilder(), pb = new StringBuilder();
            for (int i = 0; i < 2000; i++) {
                sa.append('a');
                pa.append('a');
                pb.append('b');
            }
            if (!TestUtil.checkEq(true, s.isMatch(sa.toString(), pa.toString()), "边界7a: 2000精确匹配")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界7a 异常: " + t);
        }
        try {
            StringBuilder sa = new StringBuilder();
            for (int i = 0; i < 2000; i++) sa.append('a');
            if (!TestUtil.checkEq(true, s.isMatch(sa.toString(), "*"), "边界7b: 2000配单星")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界7b 异常: " + t);
        }
        try {
            StringBuilder sa = new StringBuilder(), pb = new StringBuilder();
            for (int i = 0; i < 2000; i++) {
                sa.append('a');
                pb.append('b');
            }
            if (!TestUtil.checkEq(false, s.isMatch(sa.toString(), pb.toString()), "边界7c: 2000全不匹配")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界7c 异常: " + t);
        }

        // 边界8: 判题失败用例回归——字面 c 不能凭空消失，"c*a*b" 无法匹配 "aab"
        try {
            if (!TestUtil.checkEq(false, s.isMatch("aab", "c*a*b"), "边界8: 字面星号回归")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界8 异常: " + t);
        }
        // 回归：模式已耗尽但字符串还有字符，仍要允许最近星号继续扩展
        try {
            if (!TestUtil.checkEq(true, s.isMatch("abc", "ab*"), "回归: 星号收尾继续扩展")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("回归: 星号收尾继续扩展 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
