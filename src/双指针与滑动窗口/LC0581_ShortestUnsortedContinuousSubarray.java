// ============================================================
// LeetCode 581. 最短无序连续子数组 (Shortest Unsorted Continuous Subarray)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/
// 刷题日期：2026-08-17
//
// ============================================================


public class LC0581_ShortestUnsortedContinuousSubarray {

    // ==== 提交代码开始 ====
    public int findUnsortedSubarray(int[] nums) {
        int r = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] < max) {
                r = i;
            }
            max = Math.max(max, nums[i]);
        }
        if (r == 0) {
            return 0;
        }
        int l = nums.length - 1;
        int min = Integer.MAX_VALUE;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] > min) {
                l = i;
            }
            min = Math.min(min, nums[i]);
        }
        return r - l + 1;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0581_ShortestUnsortedContinuousSubarray s = new LC0581_ShortestUnsortedContinuousSubarray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(5, s.findUnsortedSubarray(new int[]{2, 6, 4, 8, 10, 9, 15}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.findUnsortedSubarray(new int[]{1, 2, 3, 4}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.findUnsortedSubarray(new int[]{1}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq(0, s.findUnsortedSubarray(new int[]{1, 1, 1, 1}), "全相同")) failures++; } catch (Throwable t) { failures++; System.out.println("全相同 异常: " + t); }
        try { if (!TestUtil.checkEq(4, s.findUnsortedSubarray(new int[]{4, 3, 2, 1}), "严格降序")) failures++; } catch (Throwable t) { failures++; System.out.println("严格降序 异常: " + t); }
        try { if (!TestUtil.checkEq(2, s.findUnsortedSubarray(new int[]{2, 1, 3, 4}), "前缀乱序")) failures++; } catch (Throwable t) { failures++; System.out.println("前缀乱序 异常: " + t); }
        try { if (!TestUtil.checkEq(2, s.findUnsortedSubarray(new int[]{1, 3, 2, 4}), "后缀乱序")) failures++; } catch (Throwable t) { failures++; System.out.println("后缀乱序 异常: " + t); }
        try { if (!TestUtil.checkEq(4, s.findUnsortedSubarray(new int[]{1, 3, 2, 2, 2}), "中间连续相等")) failures++; } catch (Throwable t) { failures++; System.out.println("中间连续相等 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.findUnsortedSubarray(new int[]{3, 1, 2}), "全部重排")) failures++; } catch (Throwable t) { failures++; System.out.println("全部重排 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}