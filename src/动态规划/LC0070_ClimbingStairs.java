// ============================================================
// LeetCode 70. 爬楼梯 (Climbing Stairs)
// 难度：Easy | 分类：动态规划
// 链接：https://leetcode.cn/problems/climbing-stairs/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-09 · 一刷一次 Accepted）
// 一刷写法：滚动变量的斐波那契——dp[i] = dp[i-1] + dp[i-2]，只保留前两项滚动更新。O(n)/O(1)。子类型：线性 DP
// DP 五件事（线性 DP）：① dp[i] = 走到第 i 阶的方法数；② 下标范围 i = 1..n，dp[0] 当哨兵 = 1（站在地面，算 1 种）；③ dp 值存方案计数；④ 转移 dp[i] = dp[i-1] + dp[i-2]（最后一步跨 1 阶或跨 2 阶）；⑤ 答案是 dp[n]
// 本题易错点：① dp[0] = 1 不能丢，否则 n=1 时算成 0；② 用两个滚动变量时要先更新「上一行」再更新「上上行」，顺序反了会算废；③ n ≤ 45，结果在 int 范围内，不需要 long
// 测试用例与一刷归档保持一致（示例 2 个 + 边界 3 个：n=1/n=4/n=45）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0070_ClimbingStairs {

    // ==== 提交代码开始 ====
    public int climbStairs(int n) {
        if (n < 2) {
            return 1;
        }
        int a = 1, b = 1;
        for (int i = 2; i <= n; i++) {
            int c = a + b;
            b = a;
            a = c;
        }
        return a;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0070_ClimbingStairs s = new LC0070_ClimbingStairs();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2, s.climbStairs(2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.climbStairs(3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(1, s.climbStairs(1), "边界-n=1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-n=1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.climbStairs(4), "边界-n=4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-n=4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1836311903, s.climbStairs(45), "边界-n=45")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-n=45 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
