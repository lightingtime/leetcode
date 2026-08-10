// ============================================================
// LeetCode 42. 接雨水 (Trapping Rain Water)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/trapping-rain-water/
// 刷题日期：2026-08-10
// ============================================================

public class LC0042_TrappingRainWater {

    // ==== 提交代码开始 ====
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int water = 0;
        while (left < right) {
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);
            if (leftMax < rightMax) {
                water += leftMax - height[left];
                left++;
            } else {
                water += rightMax - height[right];
                right--;
            }
        }
        return water;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0042_TrappingRainWater s = new LC0042_TrappingRainWater();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(6, s.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(9, s.trap(new int[]{4, 2, 0, 3, 2, 5}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(0, s.trap(new int[]{5}), "边界-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.trap(new int[]{2, 1}), "边界-两元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.trap(new int[]{2, 0, 2}), "边界-凹形")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-凹形 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.trap(new int[]{0, 5, 0, 5, 0}), "边界-山谷")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-山谷 异常: " + t); }
        try {
            if (!TestUtil.checkEq(10, s.trap(new int[]{10, 0, 10}), "边界-大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
