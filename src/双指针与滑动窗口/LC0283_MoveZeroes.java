// ============================================================
// LeetCode 283. 移动零 (Move Zeroes)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/move-zeroes/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-04 · 一刷一次 AC）
// 一刷/上次复习写法：双指针原地交换（慢指针 l 记「已排好的非零区」右边界，快指针 i 扫描），O(n)/O(1)
// 要点：不变量 [0, l) 全是已就位的非零元素且保持原相对顺序；i 扫到非零就换到 l 处，扫完 0 自然全被挤到尾部
// 测试用例与一刷归档保持一致（示例 2 个 + 边界 9 个）
// 上次复习留的精简提示：l == i 时 swap 是自交换，可加 if (l != i) 跳过；两个分支都在 i++，可改成 for 循环 + 单个 if；swap 签名里逗号前多了空格
//
// 思路：双指针原地分区——lt 是「已就位的非零区」右边界（[0, lt) 全非零且保序），i 逐个扫描；
//       遇到非零就与 nums[lt] 交换并让 lt 前进，零则原地跳过；扫完非零全在头部且保序，零自然被挤到尾部
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0283_MoveZeroes {

    // ==== 提交代码开始 ====
    public void moveZeroes(int[] nums) {
        int lt = 0, i = 0;
        while (i < nums.length) {
           if (nums[i] != 0) {
               swap(nums, lt, i);
               lt++;
               i++;
           } else {
               i++;
           }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
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
        try {
            int[] nums = new int[]{0, -1, 0, -2};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{-1, -2, 0, 0}, nums, "边界6 负数与零混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            int[] nums = new int[]{0, Integer.MIN_VALUE, 0, Integer.MAX_VALUE};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0}, nums, "边界7 极值数值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            int[] nums = new int[]{0, 3, 0, 4, 0};
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(new int[]{3, 4, 0, 0, 0}, nums, "边界8 首尾皆零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            int n = 10000, k = 0;
            int[] nums = new int[n], expect = new int[n];
            for (int i = 0; i < n; i++) {
                if (i % 2 == 0) { nums[i] = 0; }
                else { nums[i] = i; expect[k++] = i; }   // 非零元素按原顺序留在前面
            }
            s.moveZeroes(nums);
            if (!TestUtil.checkEq(expect, nums, "边界9 长度 1e4 保序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
