// ============================================================
// LeetCode 33. 搜索旋转排序数组 (Search in Rotated Sorted Array)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/search-in-rotated-sorted-array/
// 二刷 · 一刷日期：2026-08-05｜测试用例与一刷归档保持一致
// 上次复习：2026-09-18（较弱）
// ============================================================

import java.util.*;

public class LC0033_SearchInRotatedSortedArray {

    // ==== 提交代码开始 ====
    public int search(int[] nums, int target) {
        int l = -1, r = nums.length;
        while (l + 1 < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] >= nums[0]) {
                if (target >= nums[0] && target < nums[mid]) {
                    r = mid;
                } else {
                    l = mid;
                }
            } else {
                // target == nums[0] 属于左段（含下标 0），不能划进右段
                if (target < nums[0] && target >= nums[mid]) {
                    l = mid;
                } else {
                    r = mid;
                }
            }
        }
        return -1;
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

        // ---- 二刷分析补入：死循环最小复现（mid 落到下标 0 时两个 if 都不成立，left/right 都不动）----
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{1, 2}, 2), "边界8-未旋转两元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8-未旋转两元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{2, 1}, 1), "边界9-旋转一位两元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9-旋转一位两元素 异常: " + t); }

        // ---- 力扣 WA 回归用例：target 恰好等于 nums[0] ----
        try {
            if (!TestUtil.checkEq(0, s.search(new int[]{5, 1, 3}, 5), "边界10-target等于nums[0]")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10-target等于nums[0] 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
