// ============================================================
// LeetCode 33. 搜索旋转排序数组 (Search in Rotated Sorted Array)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/search-in-rotated-sorted-array/
// 刷题日期：2026-08-05
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0033_SearchInRotatedSortedArray {

    // ==== 提交代码开始 ====
    public int search(int[] nums, int target) {
        // TODO: 在这里实现你的解法
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] >= nums[left]) {
                if (target >= nums[left] && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return nums[left] == target ? left : -1;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0033_SearchInRotatedSortedArray s = new LC0033_SearchInRotatedSortedArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.search(new int[]{1}, 0), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（旋转数组二分：未旋转/旋转1位/左右半段/极值/大数）----
        try {
            if (!TestUtil.checkEq(2, s.search(new int[]{1, 2, 3, 4, 5}, 3), "边界1-未旋转命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-未旋转命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.search(new int[]{1, 2, 3, 4, 5}, 6), "边界2-未旋转未命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-未旋转未命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{5, 1, 2, 3, 4}, 1), "边界3-旋转1位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-旋转1位 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{6, 7, 0, 1, 2}, 7), "边界4-目标在左半段")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-目标在左半段 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.search(new int[]{6, 7, 0, 1, 2}, 1), "边界5-目标在右半段")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-目标在右半段 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 7), "边界6-最大值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-最大值 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{10000, -10000, 0}, -10000), "边界7-大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
