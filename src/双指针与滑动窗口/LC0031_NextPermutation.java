// ============================================================
// LeetCode 31. 下一个排列 (Next Permutation)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/next-permutation/
// 刷题日期：2026-08-11
// ============================================================

import java.util.*;

public class LC0031_NextPermutation {

    // ==== 提交代码开始 ====
    public void nextPermutation(int[] nums) {
        int firstIndex = nums.length - 2;
        while (firstIndex >= 0) {
            if (nums[firstIndex] < nums[firstIndex + 1]) {
                break;
            } else {
                firstIndex--;
            }
        }
        if (firstIndex == -1) {
            reverse(nums, 0, nums.length - 1);
            return;
        }
        int secondIndex = nums.length - 1;
        while (secondIndex > firstIndex) {
            if (nums[firstIndex] < nums[secondIndex]) {
                break;
            } else {
                secondIndex--;
            }
        }
        swap(nums, firstIndex, secondIndex);

        reverse(nums, firstIndex + 1, nums.length - 1);
    }

    private void swap(int[] nums, int firstIndex, int secondIndex) {
        int temp = nums[firstIndex];
        nums[firstIndex] = nums[secondIndex];
        nums[secondIndex] = temp;
    }

    private void reverse(int[] nums, int begin, int end) {
        while (begin < end) {
            swap(nums, begin, end);
            begin++;
            end--;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0031_NextPermutation s = new LC0031_NextPermutation();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            int[] nums = new int[]{1, 2, 3};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{1, 3, 2}, nums, "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            int[] nums = new int[]{3, 2, 1};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{1, 2, 3}, nums, "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            int[] nums = new int[]{1, 1, 5};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{1, 5, 1}, nums, "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            int[] nums = new int[]{1};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{1}, nums, "单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单元素 异常: " + t); }
        try {
            int[] nums = new int[]{2, 2, 2};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{2, 2, 2}, nums, "全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("全相同 异常: " + t); }
        try {
            int[] nums = new int[]{1, 2, 3, 4};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{1, 2, 4, 3}, nums, "升序四元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("升序四元素 异常: " + t); }
        try {
            int[] nums = new int[]{9, 5, 4, 3, 1};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{1, 3, 4, 5, 9}, nums, "降序大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("降序大数 异常: " + t); }
        try {
            int[] nums = new int[]{1, 3, 2, 2};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{2, 1, 2, 3}, nums, "重复值带pivot")) failures++;
        } catch (Throwable t) { failures++; System.out.println("重复值带pivot 异常: " + t); }
        try {
            int[] nums = new int[]{0, 1, 2};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{0, 2, 1}, nums, "含0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("含0 异常: " + t); }
        try {
            int[] nums = new int[]{1, 2, 3, 5, 4};
            s.nextPermutation(nums);
            if (!TestUtil.checkEq(new int[]{1, 2, 4, 3, 5}, nums, "只动尾部")) failures++;
        } catch (Throwable t) { failures++; System.out.println("只动尾部 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
