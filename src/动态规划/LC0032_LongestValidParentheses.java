// ============================================================
// LeetCode 32. 最长有效括号 (Longest Valid Parentheses)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/longest-valid-parentheses/
// 刷题日期：2026-08-20
//
// 思路：一维 DP，dp[i] 表示以 i 结尾的最长有效括号长度；遇 ')' 时按前一个字符分类，配对后尝试拼接更前面的有效段。
// 复杂度：时间 O(n)，空间 O(n)
// ============================================================

public class LC0032_LongestValidParentheses {

    // ==== 提交代码开始 ====
    public int longestValidParentheses(String s) {
        int n = s.length();
        int[] dp = new int[n];
        int max = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == ')') {
                if (i > 0 && s.charAt(i - 1) == '(') {
                    if (i - 2 >= 0) {
                        dp[i] = dp[i - 2] + 2;
                    } else {
                        dp[i] = 2;
                    }
                } else if (i > 0 && s.charAt(i - 1) == ')') {
                    int j = i - dp[i-1] - 1;
                    if (j >= 0) {
                        if (s.charAt(j) == '(') {
                            if (j - 1 >= 0) {
                                dp[i] = dp[i - 1] + 2 + dp[j - 1];
                            } else {
                                dp[i] = dp[i - 1] + 2;
                            }
                        }
                    }
                }
                max = Math.max(max, dp[i]);
            }
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0032_LongestValidParentheses s = new LC0032_LongestValidParentheses();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2, s.longestValidParentheses("(()"), "示例1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(4, s.longestValidParentheses(")()())"), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(0, s.longestValidParentheses(""), "示例3")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        // 最短合法结构：一对括号
        try {
            if (!TestUtil.checkEq(2, s.longestValidParentheses("()"), "边界-最短合法")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-最短合法 异常: " + t);
        }
        // 同长度但方向相反：没有任何合法子串
        try {
            if (!TestUtil.checkEq(0, s.longestValidParentheses(")("), "边界-方向相反")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-方向相反 异常: " + t);
        }
        // 全部为同一种括号：无法匹配
        try {
            if (!TestUtil.checkEq(0, s.longestValidParentheses("((("), "边界-全左括号")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-全左括号 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(0, s.longestValidParentheses(")))"), "边界-全右括号")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-全右括号 异常: " + t);
        }
        // 嵌套结构：验证连续匹配的累计
        try {
            if (!TestUtil.checkEq(4, s.longestValidParentheses("(())"), "边界-嵌套")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-嵌套 异常: " + t);
        }
        // 包裹后再拼接：验证配对后还要接更前面一段有效串
        try {
            if (!TestUtil.checkEq(6, s.longestValidParentheses("()(())"), "边界-包裹后拼接")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-包裹后拼接 异常: " + t); }
        // 两段合法结构相邻：验证拼接计数
        try {
            if (!TestUtil.checkEq(4, s.longestValidParentheses("()()"), "边界-连续两段")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-连续两段 异常: " + t);
        }
        // 中间断开后，不能把左右两段错误合并
        try {
            if (!TestUtil.checkEq(2, s.longestValidParentheses("())(()"), "边界-中间断开")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-中间断开 异常: " + t);
        }
        // 长度较大的连续合法输入：验证累计长度和循环边界
        try {
            if (!TestUtil.checkEq(1000, s.longestValidParentheses("()".repeat(500)), "边界-长连续合法")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-长连续合法 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}