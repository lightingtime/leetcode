// ============================================================
// LeetCode 1. 两数之和 (Two Sum)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/two-sum/
// 复习日期：2026-09-16（第 1 次复习 · 一刷 2026-08-01）
// 一刷写法：单遍哈希表（边查边存，重复值场景天然正确）
// 测试用例：示例 3 个（题目自带）+ 边界 5 个（一刷归档缺边界，本次补齐）
// ============================================================

import java.util.*;

public class LC0001_TwoSum {

    // ==== 提交代码开始 ====
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (map.containsKey(target - num)) {
                return new int[] {i , map.get(target - num)};
            } else {
                map.put(num, i);
            }
        }
        return new int[] {};
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0001_TwoSum s = new LC0001_TwoSum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEqUnordered(new int[]{0, 1}, s.twoSum(new int[]{2, 7, 11, 15}, 9), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{1, 2}, s.twoSum(new int[]{3, 2, 4}, 6), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{0, 1}, s.twoSum(new int[]{3, 3}, 6), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!TestUtil.checkEqUnordered(new int[]{0, 1}, s.twoSum(new int[]{-3, 4}, 1), "边界1-最短数组含负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{0, 2}, s.twoSum(new int[]{3, 2, 3}, 6), "边界2-重复值取不同下标")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{2, 3}, s.twoSum(new int[]{1, 2, 3, 4}, 7), "边界3-答案在数组末尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{1, 2}, s.twoSum(new int[]{-1, -2, -3}, -5), "边界4-全负数与负数目标")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{0, 1}, s.twoSum(new int[]{1000000000, -1000000000, 5}, 0), "边界5-大数值防溢出")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}