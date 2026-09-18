// ============================================================
// LeetCode 198. 打家劫舍 (House Robber)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/house-robber/
// 刷题日期：2026-09-18（二刷 · 复习轮，一刷完成于 2026-08-10）
// 说明：测试用例与一刷归档 src/动态规划/LC0198_HouseRobber.java 保持一致。
// ============================================================

import java.util.*;

public class LC0198_HouseRobber {

    // ==== 提交代码开始 ====
    public int rob(int[] nums) {
        if (nums.length < 2) return nums[0];
        int[][] dp = new int[nums.length][2];
        // 第 1 行两列要覆盖前两间的全部决策：dp[1][0]=nums[1]（偷 1 号）、dp[1][1]=nums[0]（只偷 0 号）
        dp[0][0] = dp[1][1] = nums[0];
        dp[1][0] = nums[1];
        for (int i = 2; i < nums.length; i++) {
            dp[i][0] = Math.max(dp[i - 2][1] + nums[i], dp[i - 1][1]);
            dp[i][1] = Math.max(dp[i - 2][0] + nums[i], dp[i - 1][0]);
        }
        return Math.max(dp[nums.length - 1][0], dp[nums.length - 1][1]);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0198_HouseRobber s = new LC0198_HouseRobber();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.rob(new int[]{1, 2, 3, 1}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(12, s.rob(new int[]{2, 7, 9, 3, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(5, s.rob(new int[]{5}), "边界-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.rob(new int[]{2, 1}), "边界-两元素取大")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两元素取大 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.rob(new int[]{3, 3, 3, 3}), "边界-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.rob(new int[]{2, 0, 1}), "边界-含零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-含零 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.rob(new int[]{2, 1, 1, 2}), "边界-交替选择")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-交替选择 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
