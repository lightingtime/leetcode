// ============================================================
// LeetCode 41. 缺失的第一个正数 (First Missing Positive)
// 难度：Hard | 分类：哈希表
// 链接：https://leetcode.cn/problems/first-missing-positive/
// 复习日期：2026-09-06（第 1 次复习 · 一刷 2026-08-21）
// 一刷思路：原地归位（cyclic sort 值域偏移）——答案在 [1,n+1]；v 归位到 v-1，无效值占位也交换、重复值停止；扫 nums[i]!=i+1 返回 i+1
// 一刷复杂度：时间 O(n)，空间 O(1)
// 测试用例与一刷归档保持一致
// ============================================================

import java.util.*;

public class LC0041_FirstMissingPositive {

    // ==== 提交代码开始 ====
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int temp = nums[nums[i] - 1];
                nums[nums[i] - 1] = nums[i];
                nums[i] = temp;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return n + 1;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0041_FirstMissingPositive s = new LC0041_FirstMissingPositive();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.firstMissingPositive(new int[]{1, 2, 0}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.firstMissingPositive(new int[]{3, 4, -1, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.firstMissingPositive(new int[]{7, 8, 9, 11, 12}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: 单元素 [1] 缺失 2
        try { if (!TestUtil.checkEq(2, s.firstMissingPositive(new int[]{1}), "边界1: [1]")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: 单元素 [0] / [-1] / [2] 都缺失 1
        try { if (!TestUtil.checkEq(1, s.firstMissingPositive(new int[]{0}), "边界2a: [0]")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2a 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.firstMissingPositive(new int[]{-1}), "边界2b: [-1]")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2b 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.firstMissingPositive(new int[]{2}), "边界2c: [2]")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2c 异常: " + t); }

        // 边界3: 全非正数，缺失 1
        try { if (!TestUtil.checkEq(1, s.firstMissingPositive(new int[]{-1, -2, 0, -100}), "边界3: 全非正")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }

        // 边界4: 1..n 连续且有序，缺失 n+1
        try { if (!TestUtil.checkEq(6, s.firstMissingPositive(new int[]{1, 2, 3, 4, 5}), "边界4: 连续1..n")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: 1..n 连续但乱序
        try { if (!TestUtil.checkEq(4, s.firstMissingPositive(new int[]{3, 1, 2}), "边界5: 乱序1..n")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        // 边界6: 含重复值（题目不保证唯一，[1,1] 缺失 2）
        try { if (!TestUtil.checkEq(2, s.firstMissingPositive(new int[]{1, 1}), "边界6a: [1,1]")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6a 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.firstMissingPositive(new int[]{1, 2, 2}), "边界6b: [1,2,2]")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6b 异常: " + t); }

        // 边界7: 值远大于 n 或为负数，缺失 1
        try { if (!TestUtil.checkEq(1, s.firstMissingPositive(new int[]{100, 200, -5}), "边界7: 超范围值")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        // 边界8: 缺失在中间
        try { if (!TestUtil.checkEq(2, s.firstMissingPositive(new int[]{1, 3, 4}), "边界8: 缺中间")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        // 边界9: n 上限 5*10^5，数组为 1..499999 + 500000？缺 500001（连续 1..500000）
        try {
            int[] big = new int[500_000];
            for (int i = 0; i < big.length; i++) big[i] = i + 1;
            if (!TestUtil.checkEq(500_001, s.firstMissingPositive(big), "边界9: 5e5连续")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        // 边界10: n 上限，1..499999 缺 500000（把 500000 换成 500001 之外的数）
        try {
            int[] big = new int[500_000];
            for (int i = 0; i < big.length; i++) big[i] = i + 1;
            big[499_999] = 1; // 重复 1，缺失 500000
            if (!TestUtil.checkEq(500_000, s.firstMissingPositive(big), "边界10: 5e5缺500000")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }

        // 边界11: 力扣 WA 用例——无效值占着位置 0，有效值 1 在其他位置换不进去
        try { if (!TestUtil.checkEq(4, s.firstMissingPositive(new int[]{100000, 3, 4000, 2, 15, 1, 99999}), "边界11: 无效值占位")) failures++; } catch (Throwable t) { failures++; System.out.println("边界11 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}