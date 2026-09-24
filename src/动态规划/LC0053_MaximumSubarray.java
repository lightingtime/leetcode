// ============================================================
// LeetCode 53. 最大子数组和 (Maximum Subarray)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/maximum-subarray/
// 刷题日期：2026-09-24
//
// ============================================================

import java.util.*;

public class LC0053_MaximumSubarray {

    // ==== 提交代码开始 ====
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        int max = nums[0];
        dp[0] = nums[0];
        for (int i = 1; i < n; i++) {
            dp[i] = Math.max(0, dp[i - 1]) + nums[i];
            max = Math.max(max, dp[i]);
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0053_MaximumSubarray s = new LC0053_MaximumSubarray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(6, s.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.maxSubArray(new int[]{1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(23, s.maxSubArray(new int[]{5, 4, -1, 7, 8}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(-1, s.maxSubArray(new int[]{-1, -2, -3}), "边界-全负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全负数 异常: " + t); }
        try {
            if (!TestUtil.checkEq(15, s.maxSubArray(new int[]{1, 2, 3, 4, 5}), "边界-全正数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全正数 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-5, s.maxSubArray(new int[]{-5}), "边界-单负")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单负 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxSubArray(new int[]{-1, 0, -2}), "边界-含零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-含零 异常: " + t); }
        try {
            int[] allPos = new int[100000];
            java.util.Arrays.fill(allPos, 10000);
            if (!TestUtil.checkEq(1000000000, s.maxSubArray(allPos), "边界-长全正")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长全正 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
