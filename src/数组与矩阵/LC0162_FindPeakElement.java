// ============================================================
// LeetCode 162. 寻找峰值 (Find Peak Element)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/find-peak-element/
// 复习日期：2026-09-09（第 2 次复习 · 一刷 2026-08-23 · 上次复习 2026-09-06）
// 二刷重开：测试用例与一刷归档保持一致；先回忆思路再动笔，不查归档解法。
//
// 思路：二分，沿 nums[mid] 与 nums[mid + 1] 决定的上升方向收拢。
// 复杂度：时间 O(log n)，空间 O(1)
// ============================================================

import java.util.*;

public class LC0162_FindPeakElement {

    // ==== 提交代码开始 ====
    public int findPeakElement(int[] nums) {
        int l = -1, r = nums.length - 1;
        while (l + 1 < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] < nums[mid + 1]) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return r;
    }
    // ==== 提交代码结束 ====

    // 校验 idx 是否为合法峰值索引（nums[-1]=nums[n]=-∞）
    static boolean isPeak(int[] nums, int idx) {
        if (idx < 0 || idx >= nums.length) return false;
        boolean leftOk = idx == 0 || nums[idx] > nums[idx - 1];
        boolean rightOk = idx == nums.length - 1 || nums[idx] > nums[idx + 1];
        return leftOk && rightOk;
    }

    public static void main(String[] args) {
        LC0162_FindPeakElement s = new LC0162_FindPeakElement();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        // 示例1: nums=[1,2,3,1] -> 2（3 是峰值）
        try {
            if (!TestUtil.checkEq(2, s.findPeakElement(new int[]{1, 2, 3, 1}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        // 示例2: nums=[1,2,1,3,5,6,4] -> 返回 1 或 5 都合法（多峰任选）
        try {
            int[] nums2 = new int[]{1, 2, 1, 3, 5, 6, 4};
            if (!isPeak(nums2, s.findPeakElement(nums2))) { failures++; System.out.println("示例2 返回的不是合法峰值"); }
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 单元素：自身即峰（两侧 -∞）
        try {
            if (!TestUtil.checkEq(0, s.findPeakElement(new int[]{5}), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单元素 异常: " + t); }
        // 2) 单调递增：峰在末尾（nums[n]=-∞）
        try {
            if (!TestUtil.checkEq(4, s.findPeakElement(new int[]{1, 2, 3, 4, 5}), "边界2-单调递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-单调递增 异常: " + t); }
        // 3) 单调递减：峰在开头（nums[-1]=-∞）
        try {
            if (!TestUtil.checkEq(0, s.findPeakElement(new int[]{5, 4, 3, 2, 1}), "边界3-单调递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-单调递减 异常: " + t); }
        // 4) 中间单峰：上升后急降
        try {
            if (!TestUtil.checkEq(1, s.findPeakElement(new int[]{1, 2, 1}), "边界4-中间单峰")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-中间单峰 异常: " + t); }
        // 5) 两元素：递增 -> 末尾；递减 -> 开头
        try {
            if (!TestUtil.checkEq(1, s.findPeakElement(new int[]{1, 2}), "边界5a-两元素递增")) failures++;
            if (!TestUtil.checkEq(0, s.findPeakElement(new int[]{2, 1}), "边界5b-两元素递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-两元素 异常: " + t); }
        // 6) int 上下限值：[-2^31, 2^31-1] 递增 -> 峰在 1
        try {
            if (!TestUtil.checkEq(1, s.findPeakElement(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}), "边界6a-MIN->MAX")) failures++;
            if (!TestUtil.checkEq(0, s.findPeakElement(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}), "边界6b-MAX->MIN")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-上下限值 异常: " + t); }
        // 7) V 形谷底：两端都是峰（任选其一）
        try {
            int[] nums7 = new int[]{3, 2, 1, 2, 3};
            if (!isPeak(nums7, s.findPeakElement(nums7))) { failures++; System.out.println("边界7-V形谷底 返回的不是合法峰值"); }
        } catch (Throwable t) { failures++; System.out.println("边界7-V形谷底 异常: " + t); }
        // 8) 多峰波形：任选其一
        try {
            int[] nums8 = new int[]{1, 3, 2, 4, 3, 5, 4};
            if (!isPeak(nums8, s.findPeakElement(nums8))) { failures++; System.out.println("边界8-多峰波形 返回的不是合法峰值"); }
        } catch (Throwable t) { failures++; System.out.println("边界8-多峰波形 异常: " + t); }
        // 9) 长单调递增（规模上限附近）：峰在末尾，O(log n) 必须能返回
        try {
            int[] big = new int[1000];
            for (int i = 0; i < 1000; i++) big[i] = i;
            if (!TestUtil.checkEq(999, s.findPeakElement(big), "边界9-长递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9-长递增 异常: " + t); }
        // 10) 长波形（1000 个交替升降）：返回的必须是合法峰值
        try {
            int[] wave = new int[1000];
            for (int i = 0; i < 1000; i++) wave[i] = i % 2 == 0 ? 1000 - i : i - 1;
            if (!isPeak(wave, s.findPeakElement(wave))) { failures++; System.out.println("边界10-长波形 返回的不是合法峰值"); }
        } catch (Throwable t) { failures++; System.out.println("边界10-长波形 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
