// ============================================================
// LeetCode 33. 搜索旋转排序数组 (Search in Rotated Sorted Array)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/search-in-rotated-sorted-array/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-05 · 一刷多次踩坑后才过）
// 一刷写法：旋转数组二分——每轮先判 mid 落在哪一段（nums[mid] >= nums[left] 说明左段递增），再用「有序半段的值域」判断 target 归属，O(log n)/O(1)
// 这题一刷踩过的四个坑（错误习惯库里都有记录）：中点写成 (right-left)/2 落到区间外；不用有序半段的值域判断 target 归属；丢掉 nums[mid] == target 的显式命中；左段有序判定写成 > 而不是 >=（剩两个元素时 mid == left，> 恒 false 会错入右段）
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 9 个）
// ============================================================

public class LC0033_SearchInRotatedSortedArray {

    // ==== 提交代码开始 ====
    public int search(int[] nums, int target) {
        // 待查下标是开区间 (left, right) 内的 left+1 .. right-1，left/right 本身不在待查范围内
        int left = -1, right = nums.length;
        // left+1 < right 表示待查范围内还有元素；退出时待查为空
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            // 端点移动会把 mid 甩出待查范围，命中必须当场返回
            if (nums[mid] == target) {
                return mid;
            }
            // 段位参照物必须是固定的 nums[0]：右段的值全部小于 nums[0]
            if (nums[mid] >= nums[0]) {
                // 左段：mid 左边那一半的值域是 [nums[0], nums[mid]]
                if (target >= nums[0] && target < nums[mid]) {
                    right = mid;
                } else {
                    left = mid;
                }
            } else {
                // 右段：mid 右边那一半的值域是 [nums[mid], nums[n-1]]
                if (target < nums[0] && target > nums[mid]) {
                    left = mid;
                } else {
                    right = mid;
                }
            }
        }
        // 待查区间已空，target 不在数组中
        return -1;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0033_SearchInRotatedSortedArray s = new LC0033_SearchInRotatedSortedArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.search(new int[]{1}, 0), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（旋转数组二分：未旋转/旋转1位/左右半段/极值/大数）----
        try {
            if (!TestUtil.checkEq(2, s.search(new int[]{1, 2, 3, 4, 5}, 3), "边界1-未旋转命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-未旋转命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.search(new int[]{1, 2, 3, 4, 5}, 6), "边界2-未旋转未命中")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-未旋转未命中 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{5, 1, 2, 3, 4}, 1), "边界3-旋转1位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-旋转1位 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{6, 7, 0, 1, 2}, 7), "边界4-目标在左半段")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-目标在左半段 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.search(new int[]{6, 7, 0, 1, 2}, 1), "边界5-目标在右半段")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-目标在右半段 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 7), "边界6-最大值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-最大值 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{10000, -10000, 0}, -10000), "边界7-大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-大数 异常: " + t); }

        // ---- 二刷分析补入：死循环最小复现（mid 落到下标 0 时两个 if 都不成立，left/right 都不动）----
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{1, 2}, 2), "边界8-未旋转两元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8-未旋转两元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.search(new int[]{2, 1}, 1), "边界9-旋转一位两元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9-旋转一位两元素 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
