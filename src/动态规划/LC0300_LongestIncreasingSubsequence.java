// ============================================================
// LeetCode 300. 最长递增子序列 (Longest Increasing Subsequence)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/longest-increasing-subsequence/
// 刷题日期：2026-08-10
// ============================================================

public class LC0300_LongestIncreasingSubsequence {

    // ==== 提交代码开始 ====
    public int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int tail = 0;
        for (int i = 0; i < nums.length; i++) {
            int index = findNumIndex(tails, tail, nums[i]);
            if (index < tail) {
                tails[index] = nums[i];
            } else {
                tails[tail] = nums[i];
                tail++;
            }
        }
        return tail;
    }

    private int findNumIndex(int[] tails, int end, int num) {
        int l = -1, r = end + 1;
        while (l + 1 < r) {
            int mid = l + (r - l) / 2;
            if (tails[mid] < num) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return r;
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0300_LongestIncreasingSubsequence s = new LC0300_LongestIncreasingSubsequence();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.lengthOfLIS(new int[]{7, 7, 7, 7, 7, 7, 7}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(1, s.lengthOfLIS(new int[]{5}), "边界-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.lengthOfLIS(new int[]{1, 2, 3, 4}), "边界-严格递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-严格递增 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.lengthOfLIS(new int[]{4, 3, 2, 1}), "边界-严格递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-严格递减 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.lengthOfLIS(new int[]{-5, -3, -1}), "边界-负数递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-负数递增 异常: " + t); }
        try {
            if (!TestUtil.checkEq(6, s.lengthOfLIS(new int[]{1, 3, 6, 7, 9, 4, 10, 5, 6}), "边界-经典混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-经典混合 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
