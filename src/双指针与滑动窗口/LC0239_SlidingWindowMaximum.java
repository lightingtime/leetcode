// ============================================================
// LeetCode 239. 滑动窗口最大值 (Sliding Window Maximum)
// 难度：Hard | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/sliding-window-maximum/
// 复习日期：2026-09-16（第 1 次复习 · 一刷 2026-08-05）
// 一刷写法：单调队列存下标——入队前弹队尾小值、读答案前弹过期队头（下标 <= i-k），O(n)/O(k)
// 测试用例：示例 2 个 + 边界 10 个（原有 7 个，本次补齐：长度 1e5 递增 k=5e4 / 长度 1e5 递减 k=5e4，
// 后者专压过期队头淘汰；边界10 为二刷发现的回归用例：队内积压多个候选后遇到更大值）
//
// 思路：单调队列存下标。入队前 while 弹掉队尾所有比新元素小的值（它们更小又更早过期，永无出头之日），
// 保持队列值单调递减、队头即窗口最大；窗口完整（i >= k-1）后先弹过期队头（下标 < i-k+1）再取队头记答案。
// 复杂度：时间 O(n)（每个下标至多进出队一次），空间 O(k)
// ============================================================

import java.util.*;

public class LC0239_SlidingWindowMaximum {

    // ==== 提交代码开始 ====
    public int[] maxSlidingWindow(int[] nums, int k) {
        Deque<Integer> queue = new ArrayDeque<>();
        List<Integer> ans = new ArrayList<>();
        for(int i = 0; i < nums.length; i++) {
            while (!queue.isEmpty() && nums[queue.peekLast()] < nums[i]) {
                queue.pollLast();
            }
            queue.offerLast(i);
            if (i - k + 1 >= 0) {
                while (queue.peekFirst() < i - k + 1) {
                    queue.pollFirst();
                }
                ans.add(nums[queue.peekFirst()]);
            }
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
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
        try {
            int n = 100000, k = 50000;
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) nums[i] = i + 1;              // 递增：靠弹队尾维持单调
            int[] got = s.maxSlidingWindow(nums, k);
            int bad = (got == null || got.length != n - k + 1) ? -2 : -1;
            for (int i = 0; bad == -1 && i < got.length; i++) if (got[i] != i + k) bad = i;
            if (bad == -1) {
                System.out.println("边界8-长度 1e5 递增 通过 ✓");
            } else {
                failures++;
                System.out.println("边界8-长度 1e5 递增 失败 ✗ 首个不同下标=" + bad + "，实际=" + (got == null ? "null" : "长度 " + got.length));
            }
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            int n = 100000, k = 50000;
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) nums[i] = n - i;              // 递减：队头不断过期，专压淘汰逻辑
            int[] got = s.maxSlidingWindow(nums, k);
            int bad = (got == null || got.length != n - k + 1) ? -2 : -1;
            for (int i = 0; bad == -1 && i < got.length; i++) if (got[i] != n - i) bad = i;
            if (bad == -1) {
                System.out.println("边界9-长度 1e5 递减 通过 ✓");
            } else {
                failures++;
                System.out.println("边界9-长度 1e5 递减 失败 ✗ 首个不同下标=" + bad + "，实际=" + (got == null ? "null" : "长度 " + got.length));
            }
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        // ---- 回归用例（二刷实测失败：队内积压多个候选下标后，新来的更大值需要连续弹掉多个队尾）----
        try {
            if (!TestUtil.checkEq(new int[]{3}, s.maxSlidingWindow(new int[]{2, 1, 3}, 3), "边界10-积压多候选后遇更大值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
