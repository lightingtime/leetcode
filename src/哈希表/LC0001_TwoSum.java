// ============================================================
// LeetCode 1. 两数之和 (Two Sum)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/two-sum/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-01 · 一刷一次 AC）
// 一刷/上次复习写法：单遍哈希表（先查 target - num，再存 num → i），O(n)/O(n)
// 子类型：哈希表「边查边存」——顺序是本写法的命门，先存后查会让 num * 2 == target 时拿自己的下标配自己
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 5 个）
// 上次复习留下的精简项（本次重写请直接写成精简版）：if 分支里已 return、else 可去掉；末尾 return new int[]{} 是死代码；逗号前多余空格
//
// 思路：单遍哈希表——map 存「已见过的值 → 下标」；每个 num 先查 target - num 是否已在 map 里，
//       命中即刻返回 {i, map.get(target - num)}，查不到才把 num 存进去
// 复杂度：时间 O(n) 空间 O(n)
// ============================================================

import java.util.*;

public class LC0001_TwoSum {

    // ==== 提交代码开始 ====
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            // 先查后存：配对必须是更早出现的下标；若先 put，num * 2 == target 时会拿自己的下标配自己
            if (map.containsKey(target - nums[i])) {
                return new int[]{i, map.get(target - nums[i])};
            }
            map.put(nums[i], i);
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
