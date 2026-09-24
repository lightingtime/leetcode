// ============================================================
// LeetCode 70. 爬楼梯 (Climbing Stairs)
// 难度：Easy | 分类：动态规划
// 链接：https://leetcode.cn/problems/climbing-stairs/
// 刷题日期：2026-09-24
//
// 思路：滚动变量维护前两阶的方法数
// 复杂度：时间 O(n)，空间 O(1)
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
