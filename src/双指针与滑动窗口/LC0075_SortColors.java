// ============================================================
// LeetCode 75. 颜色分类 (Sort Colors)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/sort-colors/
// 刷题日期：2026-08-29（测试用例与一刷归档保持一致）（二刷 · 一刷 2026-08-05，一刷非一次 AC：0 分支误重置 mid 导致非严格一趟）
//
// 思路：三指针分区（荷兰国旗）——0 归左、2 归右、1 居中
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0075_SortColors {

    // ==== 提交代码开始 ====
    public void sortColors(int[] nums) {
        // 不变量（分区含义）：
        //   [0, lt)    已处理，全是 0
        //   [lt, i)    已处理，全是 1
        //   [i, gt]    未处理区（待扫描）
        //   (gt, n-1]  已处理，全是 2
        // lt：下一个 0 应放的位置（0 区右边界，不含）
        // i ：当前扫描指针（未处理区左端点）
        // gt：下一个 2 应放的位置（2 区左边界，不含）——gt 位置本身未处理，不代表已完成
        int lt = 0, i = 0, gt = nums.length - 1;
        // 未处理区是 [i, gt] 闭区间：i == gt 时还剩最后一个元素未处理；
        // 交换 2 后 gt--，新 gt 处放着换回的未知值（可能 0/1），必须由 i 再扫一次，故条件写 i <= gt
        while (i <= gt) {
            if (nums[i] == 2) {
                swap(nums, i, gt);   // 2 归位到 gt；换回的值未知，i 不前进，下一轮继续检查
                gt--;
            } else if (nums[i] == 0) {
                swap(nums, lt, i);   // 0 归位到 lt；换回的值来自已处理区（必为 1），可放心 i++
                lt++;
                i++;
            } else {                 // nums[i] == 1
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
        LC0075_SortColors s = new LC0075_SortColors();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            int[] nums = new int[]{2, 0, 2, 1, 1, 0};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{0, 0, 1, 1, 2, 2}, nums, "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            int[] nums = new int[]{2, 0, 1};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{0, 1, 2}, nums, "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（原地修改，每个用例都断言排序后的数组）----
        try {
            int[] nums = new int[]{};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{}, nums, "边界1-空数组")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-空数组 异常: " + t); }
        try {
            int[] nums = new int[]{1};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{1}, nums, "边界2-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-单元素 异常: " + t); }
        try {
            int[] nums = new int[]{0, 0, 0};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{0, 0, 0}, nums, "边界3-全相同0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-全相同0 异常: " + t); }
        try {
            int[] nums = new int[]{2, 2, 2};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{2, 2, 2}, nums, "边界4-全相同2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-全相同2 异常: " + t); }
        try {
            int[] nums = new int[]{0, 1, 1, 2};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{0, 1, 1, 2}, nums, "边界5-已排序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-已排序 异常: " + t); }
        try {
            int[] nums = new int[]{2, 1, 0};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{0, 1, 2}, nums, "边界6-逆序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-逆序 异常: " + t); }
        try {
            int[] nums = new int[]{1, 1, 2, 2};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{1, 1, 2, 2}, nums, "边界7-只有两种颜色")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-只有两种颜色 异常: " + t); }
        try {
            int[] nums = new int[]{0, 2, 1};
            s.sortColors(nums);
            if (!TestUtil.checkEq(new int[]{0, 1, 2}, nums, "回归-WA用例")) failures++;
        } catch (Throwable t) { failures++; System.out.println("回归-WA用例 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}