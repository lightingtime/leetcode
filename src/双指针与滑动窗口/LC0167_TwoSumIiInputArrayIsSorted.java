// ============================================================
// LeetCode 167. 两数之和 II - 输入有序数组 (Two Sum II - Input Array Is Sorted)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/two-sum-ii-input-array-is-sorted/
// 刷题日期：2026-10-16
//
// ============================================================

import java.util.*;

public class LC0167_TwoSumIiInputArrayIsSorted {

    // ==== 提交代码开始 ====
    public int[] twoSum(int[] numbers, int target) {
        int i = 0;
        int j = numbers.length - 1;
        while (i < j) {
            if (i > 0 && numbers[i] == numbers[i - 1]) {
                i++;
                continue;
            }
                int sum  = numbers[i] + numbers[j];
                if (sum == target) {
                    return new int[]{i + 1, j + 1};
                } else if (sum < target) {
                    i++;
                } else {
                    j--;
                }
        }
        return new int[] {-1, -1};
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0167_TwoSumIiInputArrayIsSorted s = new LC0167_TwoSumIiInputArrayIsSorted();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[]{1, 2}, s.twoSum(new int[]{2, 7, 11, 15}, 9), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 3}, s.twoSum(new int[]{2, 3, 4}, 6), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 2}, s.twoSum(new int[]{-1, 0}, -1), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（均满足唯一答案约束）----
        try {
            if (!TestUtil.checkEq(new int[]{1, 2}, s.twoSum(new int[]{0, 0}, 0), "最短数组与相同数值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("最短数组与相同数值 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 3}, s.twoSum(new int[]{1, 2, 3}, 4), "不能重复使用同一下标")) failures++;
        } catch (Throwable t) { failures++; System.out.println("不能重复使用同一下标 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{3, 4}, s.twoSum(new int[]{1, 2, 3, 7}, 10), "答案位于末尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("答案位于末尾 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{2, 3}, s.twoSum(new int[]{-8, -5, -3}, -8), "全负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("全负数 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 4}, s.twoSum(new int[]{-4, -1, 0, 3}, -1), "负数与正数配对")) failures++;
        } catch (Throwable t) { failures++; System.out.println("负数与正数配对 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{2, 3}, s.twoSum(new int[]{1, 2, 2, 4}, 4), "重复值位于不同下标")) failures++;
        } catch (Throwable t) { failures++; System.out.println("重复值位于不同下标 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 3}, s.twoSum(new int[]{-1000, -999, 0, 999, 1000}, -1000), "数值及目标下界")) failures++;
        } catch (Throwable t) { failures++; System.out.println("数值及目标下界 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{3, 5}, s.twoSum(new int[]{-1000, -999, 0, 999, 1000}, 1000), "数值及目标上界")) failures++;
        } catch (Throwable t) { failures++; System.out.println("数值及目标上界 异常: " + t); }
        try {
            int[] large = new int[30000];
            Arrays.fill(large, -1000);
            large[29998] = 499;
            large[29999] = 501;
            if (!TestUtil.checkEq(new int[]{29999, 30000}, s.twoSum(large, 1000), "长度上限与长段重复值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("长度上限 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}