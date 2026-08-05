// ============================================================
// LeetCode 283. 移动零 (Move Zeroes)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/move-zeroes/
// 刷题日期：2026-08-04
//
// ============================================================

import java.util.*;

public class LC0283_MoveZeroes {

    // ==== 提交代码开始 ====
    public void moveZeroes(int[] nums) {
        int unCheck = 0, check = 0;
        while (check < nums.length) {
            if (nums[unCheck] == 0) {
                int temp = nums[check];
                nums[check] = nums[unCheck];
                nums[unCheck] = temp;
                check++;
            } else {
                unCheck++;
                check = unCheck;
            }
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0283_MoveZeroes s = new LC0283_MoveZeroes();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            int[] nums = new int[]{0, 1, 0, 3, 12};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{1, 3, 12, 0, 0}, nums, "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            int[] nums = new int[]{0};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{0}, nums, "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            int[] nums = new int[]{0, 0, 0};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{0, 0, 0}, nums, "边界1 全零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            int[] nums = new int[]{1, 2, 3};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{1, 2, 3}, nums, "边界2 无零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            int[] nums = new int[]{1, 0};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{1, 0}, nums, "边界3 首非零尾零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            int[] nums = new int[]{0, 5};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{5, 0}, nums, "边界4 首零后非零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            int[] nums = new int[]{7};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{7}, nums, "边界5 单元素非零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
