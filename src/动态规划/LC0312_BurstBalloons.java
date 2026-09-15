// ============================================================
// LeetCode 312. 戳气球 (Burst Balloons)
// 难度：Hard | 分类：动态规划（区间 DP）
// 链接：https://leetcode.cn/problems/burst-balloons/
// 复习日期：2026-09-15（第 3 次复习 · 一刷 2026-08-20 · 上次复习 2026-09-09）
//
// 思路：补左右哨兵，按区间长度枚举最后戳破的气球 k。
// 复杂度：时间 O(n^3)，空间 O(n^2)
// ============================================================

import java.util.*;

public class LC0312_BurstBalloons {

    // ==== 提交代码开始 ====
    public int maxCoins(int[] nums) {
        int[] array = new int[nums.length + 2];
        System.arraycopy(nums, 0, array, 1, nums.length);
        array[0] = array[array.length - 1] = 1;
        int[][] dp = new int[array.length][array.length];
        for (int len = 2; len < array.length; len++) {
            for (int i = 0; i + len < array.length; i++) {
                int j = i + len;
                for (int k = i + 1; k < j; k++) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][k] + array[i] * array[k] * array[j] + dp[k][j]);
                }
            }
        }
        return dp[0][dp.length - 1];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0312_BurstBalloons s = new LC0312_BurstBalloons();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(167, s.maxCoins(new int[]{3, 1, 5, 8}), "示例1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(10, s.maxCoins(new int[]{1, 5}), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        // 单元素：左右边界都视为 1
        try {
            if (!TestUtil.checkEq(5, s.maxCoins(new int[]{5}), "边界-单元素")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-单元素 异常: " + t);
        }
        // 单元素为 0
        try {
            if (!TestUtil.checkEq(0, s.maxCoins(new int[]{0}), "边界-单元素0")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-单元素0 异常: " + t);
        }
        // 全 0：任何乘积都是 0
        try {
            if (!TestUtil.checkEq(0, s.maxCoins(new int[]{0, 0, 0}), "边界-全0")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-全0 异常: " + t);
        }
        // 全 1：每个气球贡献 1*1*1=1
        try {
            if (!TestUtil.checkEq(3, s.maxCoins(new int[]{1, 1, 1}), "边界-全1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-全1 异常: " + t);
        }
        // 递增数组：最优需要先戳中间
        try {
            if (!TestUtil.checkEq(12, s.maxCoins(new int[]{1, 2, 3}), "边界-递增")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-递增 异常: " + t);
        }
        // 递减数组：与递增对称
        try {
            if (!TestUtil.checkEq(12, s.maxCoins(new int[]{3, 2, 1}), "边界-递减")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-递减 异常: " + t);
        }
        // 0 在中间：应最后戳 0 或绕开它
        try {
            if (!TestUtil.checkEq(2, s.maxCoins(new int[]{1, 0, 1}), "边界-中间0")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-中间0 异常: " + t);
        }
        // 较长全 1：验证大输入与 int 范围
        try {
            int[] ones = new int[300];
            java.util.Arrays.fill(ones, 1);
            if (!TestUtil.checkEq(300, s.maxCoins(ones), "边界-300全1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-300全1 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}