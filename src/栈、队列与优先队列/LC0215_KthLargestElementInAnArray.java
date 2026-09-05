// ============================================================
// LeetCode 215. 数组中的第K个最大元素 (Kth Largest Element in an Array)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/kth-largest-element-in-an-array/
// 复习日期：2026-09-05（二刷复习 · 一刷 2026-08-07，上次复习 2026-09-01，较弱）
// 一刷思路：快速选择（Hoare 分区），O(n) 平均 / O(log n)
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0215_KthLargestElementInAnArray {

    // ==== 提交代码开始 ====
    public int findKthLargest(int[] nums, int k) {
        return quickSort(nums, 0, nums.length - 1, nums.length - k);
    }

    private int quickSort(int[] nums, int l, int r, int k) {
        if (l == r) {
            return nums[l];
        }
        int p = nums[r];
        int i = l, j = l;
        while (j < r) {
            if (nums[j] < p) {
                swap(nums, i, j);
                i++;
            }
            j++;
        }
        swap(nums, i, r);
        if (i == k) {
            return nums[i];
        } else if (i < k) {
            return quickSort(nums, i + 1, r, k);
        } else {
            return quickSort(nums, l, i - 1, k);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0215_KthLargestElementInAnArray s = new LC0215_KthLargestElementInAnArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(5, s.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!TestUtil.checkEq(5, s.findKthLargest(new int[]{5}, 1), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.findKthLargest(new int[]{3, 2, 1}, 3), "边界2-k等于长度取最小")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.findKthLargest(new int[]{2, 2, 2, 2}, 2), "边界3-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-2, s.findKthLargest(new int[]{-1, -2, -3, -4}, 2), "边界4-全负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(10000, s.findKthLargest(new int[]{-10000, 0, 10000}, 1), "边界5-极值范围")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.findKthLargest(new int[]{5, 4, 3, 2, 1}, 5), "边界6-降序取最小")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 2), "边界7-重复值干扰")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
