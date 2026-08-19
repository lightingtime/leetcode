// ============================================================
// LeetCode 494. 目标和 (Target Sum)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/target-sum/
// 刷题日期：2026-08-19
//
// ============================================================

import java.util.*;

public class LC0494_TargetSum {

    // ==== 提交代码开始 ====
    public int findTargetSumWays(int[] nums, int target) {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        if (sum < target || sum + target < 0) {
            return 0;
        }
        if (((sum + target) & 1) == 1) {
            return 0;
        }
        sum = (sum + target) / 2;
        int[] dp = new int[sum + 1];
        dp[0] = 1;

        for (int num : nums) {
            for (int i = sum; i >= num; i--) {
                dp[i] = dp[i] + dp[i - num];
            }
        }
        return dp[sum];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0494_TargetSum s = new LC0494_TargetSum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(5, s.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.findTargetSumWays(new int[]{1}, 1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对符号分配 DP 设计）----
        // 单元素达不到：|target| > sum，0 种
        try {
            if (!TestUtil.checkEq(0, s.findTargetSumWays(new int[]{1}, 2), "边界-单元素达不到")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素达不到 异常: " + t); }
        // 单元素零：+0 与 -0 是两种方式
        try {
            if (!TestUtil.checkEq(2, s.findTargetSumWays(new int[]{0}, 0), "边界-单零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单零 异常: " + t); }
        // 两个零：每个零两种符号，共 4 种
        try {
            if (!TestUtil.checkEq(4, s.findTargetSumWays(new int[]{0, 0}, 0), "边界-双零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-双零 异常: " + t); }
        // 和小于目标绝对值：必 0
        try {
            if (!TestUtil.checkEq(0, s.findTargetSumWays(new int[]{1, 1}, 3), "边界-和不够")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-和不够 异常: " + t); }
        // 奇偶性不符：sum=2 与 target=1 奇偶不同，必 0
        try {
            if (!TestUtil.checkEq(0, s.findTargetSumWays(new int[]{1, 1}, 1), "边界-奇偶不符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-奇偶不符 异常: " + t); }
        // 负 target：与正 target 对称
        try {
            if (!TestUtil.checkEq(3, s.findTargetSumWays(new int[]{1, 1, 1}, -1), "边界-负目标")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-负目标 异常: " + t); }
        // 四个 1 凑 0：C(4,2)=6 种
        try {
            if (!TestUtil.checkEq(6, s.findTargetSumWays(new int[]{1, 1, 1, 1}, 0), "边界-全一凑零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全一凑零 异常: " + t); }
        // 混合小数组：[1,2,3] 凑 2 只有 +1-2+3 一种（-1+2+3=4 不是 2）
        try {
            if (!TestUtil.checkEq(1, s.findTargetSumWays(new int[]{1, 2, 3}, 2), "边界-混合小数组")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-混合小数组 异常: " + t); }
        // 大数组 20 个 1000 凑 0：C(20,10)=184756，验证性能与计数
        {
            int[] big = new int[20];
            java.util.Arrays.fill(big, 1000);
            try {
                if (!TestUtil.checkEq(184756, s.findTargetSumWays(big, 0), "边界-大数组")) failures++;
            } catch (Throwable t) { failures++; System.out.println("边界-大数组 异常: " + t); }
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}