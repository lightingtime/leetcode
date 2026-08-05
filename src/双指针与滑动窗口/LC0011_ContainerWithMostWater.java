// ============================================================
// LeetCode 11. 盛最多水的容器 (Container With Most Water)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/container-with-most-water/
// 刷题日期：2026-08-04
//
// ============================================================

import java.util.*;

public class LC0011_ContainerWithMostWater {

    // ==== 提交代码开始 ====
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int max = 0;
        while (left < right) {
            max = Math.max(max, Math.min(height[left], height[right]) * (right - left));
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0011_ContainerWithMostWater s = new LC0011_ContainerWithMostWater();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(49, s.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}), "示例1 [1,8,6,2,5,4,8,3,7]")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.maxArea(new int[]{1, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(4, s.maxArea(new int[]{1, 2, 3, 4}), "边界1 递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.maxArea(new int[]{4, 3, 2, 1}), "边界2 递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(15, s.maxArea(new int[]{5, 5, 5, 5}), "边界3 全等高")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2000, s.maxArea(new int[]{1000, 1, 1000}), "边界4 高边中间低")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxArea(new int[]{0, 0}), "边界5 全零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
