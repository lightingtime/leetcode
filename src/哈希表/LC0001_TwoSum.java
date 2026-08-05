// ============================================================
// LeetCode 1. 两数之和 (Two Sum)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/two-sum/
// 刷题日期：2026-08-01
//
// ============================================================

import java.util.*;

public class LC0001_TwoSum {

    // ==== 提交代码开始 ====
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            } else {
                map.put(nums[i], i);
            }
        }
        return null;
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

        // ---- 边界测试（自己补充）----
        // 例如： try { if (!TestUtil.checkEq(期望, s.twoSum(边界输入), "边界1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 若题目允许任意顺序返回（下标对 / 集合），用 TestUtil.checkEqUnordered 代替 TestUtil.checkEq

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}