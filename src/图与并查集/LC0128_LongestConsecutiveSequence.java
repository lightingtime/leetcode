// ============================================================
// LeetCode 128. 最长连续序列 (Longest Consecutive Sequence)
// 难度：Medium | 分类：图与并查集
// 链接：https://leetcode.cn/problems/longest-consecutive-sequence/
// 复习日期：2026-09-15（第 4 次复习 · 一刷 2026-08-08 · 上次 2026-09-05 较强）
// 一刷/上次思路：HashSet 去重，只从「段起点」（不存在 x-1）向上延展计数，每段只数一次 O(n)
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0128_LongestConsecutiveSequence {

    // ==== 提交代码开始 ====
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for(int num : nums) set.add(num);
        int max = 0;
        for (int num : set) {
            int x = num;
            if (set.contains(x - 1)) {
                continue;
            }
            while (set.contains(x + 1)) {
                x++;
            }
            max = Math.max(max, x - num + 1);
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0128_LongestConsecutiveSequence s = new LC0128_LongestConsecutiveSequence();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(9, s.longestConsecutive(new int[]{0, 3, 7, 2, 5, 8, 4, 6, 0, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.longestConsecutive(new int[]{1, 0, 1, 2}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(0, s.longestConsecutive(new int[]{}), "边界1-空输入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.longestConsecutive(new int[]{7}), "边界2-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.longestConsecutive(new int[]{2, 2, 2, 2, 2}), "边界3-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.longestConsecutive(new int[]{1, 1, 2, 2, 3, 3}), "边界4-重复值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.longestConsecutive(new int[]{-3, -2, -1, 0, 1}), "边界5-负数区间")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.longestConsecutive(new int[]{1000000000, 999999999}), "边界6-极值相邻")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.longestConsecutive(new int[]{-1000000000, 1000000000}), "边界7-极值分离")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.longestConsecutive(new int[]{5, 3, 1}), "边界8-断开序列")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        // ---- 回归测试（力扣 TLE 根因：遍历 nums 含大量重复起点 0，每个 0 都完整扩展一次 → O(n²)；须遍历去重后的 set）----
        try {
            int[] big = new int[10001];
            for (int i = 0; i < 5000; i++) big[i] = 0;          // 5000 个重复起点
            for (int i = 0; i < 5001; i++) big[5000 + i] = i + 1; // 1..5001 连续
            if (!TestUtil.checkEq(5002, s.longestConsecutive(big), "回归-TLE重复起点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("回归-TLE重复起点 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}