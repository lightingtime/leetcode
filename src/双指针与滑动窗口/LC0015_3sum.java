// ============================================================
// LeetCode 15. 三数之和 (3Sum)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/3sum/
// 复习日期：2026-09-16（第 1 次复习 · 一刷 2026-08-04）
// 一刷写法：排序 + 固定 i 后双指针夹逼（3 数和 <0 左指针右移，>0 右指针左移），O(n²)/O(1)（忽略排序栈）
// 测试用例：示例 3 个 + 边界 8 个（原有 7 个，本次补齐长度 3000 混合重复；示例1 换用无序比较，与力扣判题口径一致）
// ============================================================

import java.util.*;

public class LC0015_3sum {

    // ==== 提交代码开始 ====
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int j = nums.length - 1;
            int k = i + 1;
            while (k < j) {
                if (k > i + 1 && nums[k] == nums[k - 1]) {
                    k++;
                    continue;
                }
                if (nums[i] + nums[j] + nums[k] == 0) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[k]);
                    list.add(nums[j]);
                    ans.add(list);
                    k++;
                } else if (nums[i] + nums[j] + nums[k] < 0){
                    k++;
                } else {
                    j--;
                }
            }
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0015_3sum s = new LC0015_3sum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(-1, -1, 2), Arrays.asList(-1, 0, 1)), s.threeSum(new int[]{-1, 0, 1, 2, -1, -4}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(), s.threeSum(new int[]{0, 1, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(0, 0, 0)), s.threeSum(new int[]{0, 0, 0}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（题目允许任意顺序返回，统一用 checkEqUnordered）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.threeSum(new int[]{}), "边界1-空输入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-空输入 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.threeSum(new int[]{1, 2}), "边界2-不足三个元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-不足三个元素 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.threeSum(new int[]{1, 1, 1, 1}), "边界3-全相同但不为0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-全相同但不为0 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(), s.threeSum(new int[]{-2, -1, 1}), "边界4-无解")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-无解 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(-100000, 0, 100000)), s.threeSum(new int[]{-100000, 0, 100000}), "边界5-大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-大数 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(-2, 0, 2)), s.threeSum(new int[]{-2, 0, 0, 2, 2}), "边界6-重复元素去重")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-重复元素去重 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(0, 0, 0)), s.threeSum(new int[]{0, 0, 0, 0}), "边界7-多个零去重")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-多个零去重 异常: " + t); }
        try {
            int[] nums = new int[3000];
            for (int i = 0; i < 3000; i++) nums[i] = i % 3 - 1;      // 1000 个 -1、1000 个 0、1000 个 1
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(-1, 0, 1), Arrays.asList(0, 0, 0)), s.threeSum(nums), "边界8-长度 3000 大量重复")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
