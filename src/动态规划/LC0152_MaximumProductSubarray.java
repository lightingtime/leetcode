// ============================================================
// LeetCode 152. 乘积最大子数组 (Maximum Product Subarray)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/maximum-product-subarray/
// 刷题日期：2026-08-10
// ============================================================

public class LC0152_MaximumProductSubarray {

    // ==== 提交代码开始 ====
    public int maxProduct(int[] nums) {
        int[][] dp = new int[nums.length][2];
        dp[0][0] = dp[0][1] = nums[0];
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            dp[i][0] = Math.max(nums[i], Math.max(dp[i - 1][0] * nums[i], dp[i - 1][1] * nums[i]));
            dp[i][1] = Math.min(nums[i], Math.min(dp[i - 1][0] * nums[i], dp[i - 1][1] * nums[i]));
            max = Math.max(dp[i][0], max);
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0152_MaximumProductSubarray s = new LC0152_MaximumProductSubarray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(6, s.maxProduct(new int[]{2, 3, -2, 4}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxProduct(new int[]{-2, 0, -1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(5, s.maxProduct(new int[]{5}), "边界-单元素正")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素正 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-7, s.maxProduct(new int[]{-7}), "边界-单元素负")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素负 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.maxProduct(new int[]{-2, -3, -1}), "边界-全负")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全负 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.maxProduct(new int[]{0, 2, 3}), "边界-零断开")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-零断开 异常: " + t); }
        try {
            if (!TestUtil.checkEq(24, s.maxProduct(new int[]{2, -5, -2, -4, 3}), "边界-正负翻转")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-正负翻转 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.maxProduct(new int[]{1, 2, 3}), "边界-全正")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全正 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
