// ============================================================
// LeetCode 55. 跳跃游戏 (Jump Game)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/jump-game/
// 刷题日期：2026-09-22
//
// ============================================================

import java.util.*;

public class LC0055_JumpGame {

    // ==== 提交代码开始 ====
    public boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (maxReach < i) {
                return false;
            }
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return maxReach >= nums.length - 1;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0055_JumpGame s = new LC0055_JumpGame();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.canJump(new int[]{2, 3, 1, 1, 4}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canJump(new int[]{3, 2, 1, 0, 4}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（与一刷归档保持一致）----
        try {
            if (!TestUtil.checkEq(true, s.canJump(new int[]{0}), "边界-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canJump(new int[]{0, 1}), "边界-开头为零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-开头为零 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canJump(new int[]{1, 0, 1}), "边界-中间卡死")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-中间卡死 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.canJump(new int[]{10, 0, 0, 0}), "边界-大跳跃")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-大跳跃 异常: " + t); }
        try {
            int[] longOnes = new int[10000];
            java.util.Arrays.fill(longOnes, 1);
            if (!TestUtil.checkEq(true, s.canJump(longOnes), "边界-长数组")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长数组 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canJump(new int[]{1, 1, 1, 0, 5, 0}), "边界-不可达大跳")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-不可达大跳 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
