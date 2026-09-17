// ============================================================
// LeetCode 44. 通配符匹配 (Wildcard Matching)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/wildcard-matching/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-26）
// 子类型：线性 DP（双序列）——状态是「s 前 i 个字符 / p 前 j 个模式」的前缀能否匹配
// 一刷写法：二维 DP，dp[i][j] = s 前 i 个字符与 p 前 j 个模式是否匹配；`?`/字母一对一，`*` 取「不吃字符」或「吃一个字符」两种转移
// 一刷问题：空前缀行把任意位置的 `*` 都判 true（忽略它前面压着的字面字符），"c*a*b" 误匹配 "aab"；空串行的 `*` 转移方向短接写错
// 上次复习写法：二维 DP（O(nm)/O(nm)）与贪心双指针+星号回溯（最坏 O(nm)/O(1)）两条都提交 Accepted；贪心首版收尾比较写反、循环以模式长度为界、&&/|| 漏括号越界
// 测试用例：与一刷归档保持一致（示例 3 + 边界 8 组 + 回归，含 2000 长度上限三档与星号收尾继续扩展）
// ============================================================

import java.util.*;

public class LC0044_WildcardMatching {

    // ==== 提交代码开始 ====
    // 参数 s：待匹配字符串；参数 p：模式串（`?` 配任意单字符、`*` 配任意长度含空串）；返回整体是否匹配
    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();
        // start：最近那个 `*` 目前吃到 s 的下标（回溯时 ++ 表示多吃一个字符）；matchIdx：该 `*` 之后第一个待匹配的模式下标
        int start = -1, matchIdx = -1;
        int i = 0, j = 0;
        while (i < m) {
            if (j < n && p.charAt(j) == '*') {
                start = i;
                matchIdx = j + 1;
                j++;
            } else if (j < n && (s.charAt(i) == p.charAt(j) || p.charAt(j) == '?')) {
                i++;
                j++;
            } else {
                if (start > -1) {
                    start++;
                    i = start;
                    j = matchIdx;
                } else {
                    return false;
                }
            }
        }
        // 收尾：模式可能还剩尾部的 `*`（匹配空串），所以比较方向是「模式是否走到头」而不是「s 是否走到头」
        while (j < n && p.charAt(j) == '*') {
            j++;
        }
        return j == n;
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
