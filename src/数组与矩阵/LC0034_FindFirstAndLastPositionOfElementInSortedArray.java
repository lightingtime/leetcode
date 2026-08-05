// ============================================================
// LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置 (Find First and Last Position of Element in Sorted Array)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/
// 刷题日期：2026-08-05
//
// ============================================================

import java.util.*;

public class LC0034_FindFirstAndLastPositionOfElementInSortedArray {

    // ==== 提交代码开始 ====
    public int[] searchRange(int[] nums, int target) {
        if (nums.length == 0) {
            return new int[]{-1, -1};
        }
        int left = findFirstIndex(nums, target);
        int right = findFirstIndex(nums, target + 1);
        if (left >= nums.length || right < left || nums[left] != target ) {
            return new int[]{-1, -1};
        }
        return new int[] {left, right - 1};
    }

	private int findFirstIndex(int[] nums, int target) {
        int left = -1, right = nums.length;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid;
            } else {
                right = mid;
            }
        }
		return right;
	}
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0034_FindFirstAndLastPositionOfElementInSortedArray s = new LC0034_FindFirstAndLastPositionOfElementInSortedArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[]{3, 4}, s.searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{-1, -1}, s.searchRange(new int[]{5, 7, 7, 8, 8, 10}, 6), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{-1, -1}, s.searchRange(new int[]{}, 0), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（二分找首尾：单元素/全相同/开头结尾/越界/大数）----
        try {
            if (!TestUtil.checkEq(new int[]{0, 0}, s.searchRange(new int[]{1}, 1), "边界1-单元素命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单元素命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{-1, -1}, s.searchRange(new int[]{1}, 0), "边界2-单元素未命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-单元素未命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{0, 3}, s.searchRange(new int[]{8, 8, 8, 8}, 8), "边界3-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-全相同 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{0, 1}, s.searchRange(new int[]{8, 8, 9, 10}, 8), "边界4-目标在开头")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-目标在开头 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{2, 3}, s.searchRange(new int[]{1, 2, 8, 8}, 8), "边界5-目标在结尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-目标在结尾 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{-1, -1}, s.searchRange(new int[]{2, 3, 4}, 1), "边界6-小于最小值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-小于最小值 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{-1, -1}, s.searchRange(new int[]{2, 3, 4}, 5), "边界7-大于最大值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-大于最大值 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{2, 2}, s.searchRange(new int[]{-1000000000, 0, 1000000000}, 1000000000), "边界8-大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8-大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
