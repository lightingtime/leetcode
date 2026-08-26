// ============================================================
// LeetCode 91. 解码方法 (Decode Ways)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/decode-ways/
// 刷题日期：2026-08-26
//
// 思路：线性 DP（斐波那契式）——dp[i]=前 i 个字符的解码方案数，dp[0]=1 空串基准；
//       每个位置两个独立合法性开关相加：当前字符单独解码(非'0')→继承 dp[i-1]，
//       与前一位组成 10~26(前一位非'0' 且组合值≤26)→继承 dp[i-2]
// DP 子类型：线性 DP——状态沿字符串位置线性推进，当前位置的方案数只由前一两个位置的
//            方案数决定（单独解码 / 与前一位组成两位数），注意 0 的合法性分支
// 复杂度：时间 O(n) 空间 O(n)（可滚动变量优化到 O(1)）
// ============================================================

import java.util.*;

public class LC0091_DecodeWays {

    // ==== 提交代码开始 ====
    public int numDecodings(String s) {
        if (s.charAt(0) == '0') {
            return 0;
        }
        int[] dp = new int[s.length()];
        dp[0] = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) != '0') {
                dp[i] = dp[i - 1];
            }
            int num = Integer.parseInt(s.substring(i - 1, i + 1));
            if (num >= 10 && num <= 26) {
                dp[i] += (i > 1 ? dp[i - 2] : 1);
            }
        }
        return dp[s.length() - 1];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0091_DecodeWays s = new LC0091_DecodeWays();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2, s.numDecodings("12"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.numDecodings("226"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.numDecodings("06"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= s.length <= 100，只含数字、可能有前导零；题目保证给定输入答案在 int 内
        // 边界1: 单个 '0'，无任何合法解码
        try {
            if (!TestUtil.checkEq(0, s.numDecodings("0"), "边界1: 单零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: "27"——两位组合超 26，只能逐位拆开
        try {
            if (!TestUtil.checkEq(1, s.numDecodings("27"), "边界2: 组合越界")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: "10" 与 "100"——0 必须借前一位组 10，第二个 0 无依可归
        try {
            if (!TestUtil.checkEq(1, s.numDecodings("10"), "边界3a: 10")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3a 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.numDecodings("100"), "边界3b: 100")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3b 异常: " + t); }
        // 边界4: "111"——三种分组，斐波那契叠加的最小样例
        try {
            if (!TestUtil.checkEq(3, s.numDecodings("111"), "边界4: 斐波那契叠加")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: "301"——中途 dp 归零后即使后续字符合法整体仍为 0
        try {
            if (!TestUtil.checkEq(0, s.numDecodings("301"), "边界5: 中途断链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 长度 100 上限，全 '9' 无两位组合可行，恰好 1 种
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 100; i++) sb.append('9');
            if (!TestUtil.checkEq(1, s.numDecodings(sb.toString()), "边界6: 长度100上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}