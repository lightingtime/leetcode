// ============================================================
// LeetCode 239. 滑动窗口最大值 (Sliding Window Maximum)
// 难度：Hard | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/sliding-window-maximum/
// 刷题日期：2026-08-05
//
// ============================================================

import java.util.*;

public class LC0239_SlidingWindowMaximum {

    // ==== 提交代码开始 ====
    public int[] maxSlidingWindow(int[] nums, int k) {
        LinkedList<Integer> queue = new LinkedList<>();
        int[] ans = new int[nums.length - k + 1];
        int start = 0;
        int i = 0;
        while (i < nums.length) {
            // 入队前弹掉队尾比新元素小的：新元素更大且更晚，被弹者永远当不了最大值
            while (!queue.isEmpty() && nums[queue.peekLast()] < nums[i]) {
                queue.pollLast();
            }
            queue.offerLast(i);
            // 弹出过期队头：窗口是闭区间 [i-k+1, i]，队头下标 <= i-k 说明已滑出窗口
            while (!queue.isEmpty() && queue.peekFirst() <= i - k) {
                queue.pollFirst();
            }
            if (i - start + 1 == k) {
                ans[start] = nums[queue.peekFirst()];
                start++;
            }
            i++;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0239_SlidingWindowMaximum s = new LC0239_SlidingWindowMaximum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[]{3, 3, 5, 5, 6, 7}, s.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1}, s.maxSlidingWindow(new int[]{1}, 1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（滑动窗口：k 取值/单调性/全相同/负数/大数）----
        try {
            if (!TestUtil.checkEq(new int[]{1, 2, 3}, s.maxSlidingWindow(new int[]{1, 2, 3}, 1), "边界1-k=1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-k=1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{4}, s.maxSlidingWindow(new int[]{1, 2, 3, 4}, 4), "边界2-k等于全长")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-k等于全长 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{5, 5, 5}, s.maxSlidingWindow(new int[]{5, 5, 5, 5}, 2), "边界3-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-全相同 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{4, 3, 2}, s.maxSlidingWindow(new int[]{4, 3, 2, 1}, 2), "边界4-递减数组")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-递减数组 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{2, 3, 4}, s.maxSlidingWindow(new int[]{1, 2, 3, 4}, 2), "边界5-递增数组")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-递增数组 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{-1, -3, -5}, s.maxSlidingWindow(new int[]{-1, -3, -5, -7}, 2), "边界6-全负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-全负数 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{10000, 10000}, s.maxSlidingWindow(new int[]{-10000, 10000, 0}, 2), "边界7-大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
