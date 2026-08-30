// ============================================================
// LeetCode 4. 寻找两个正序数组的中位数 (Median of Two Sorted Arrays)
// 难度：Hard | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/median-of-two-sorted-arrays/
// 二刷 · 一刷日期：2026-08-06（一刷思路：两数组找第 k 小，O(log(m+n))）
// 测试用例与一刷归档保持一致
// 刷题日期：2026-08-30
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================


public class LC0004_MedianOfTwoSortedArrays {

    // ==== 提交代码开始 ====
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        // TODO: 在这里实现你的解法
        int len = nums1.length + nums2.length;
        if ((len & 1) == 1) {
            return getKth(nums1, nums2, len / 2 + 1);
        }
        return (getKth(nums1, nums2, len / 2 + 1) + getKth(nums1, nums2, len / 2)) / 2.0;
    }

    private int getKth(int[] nums1, int[] nums2, int k) {
        int start1 = 0, start2 = 0;
        while (true) {
            if (start1 == nums1.length) {
                return nums2[start2 + k - 1];
            }
            if (start2 == nums2.length) {
                return nums1[start1 + k - 1];
            }
            if (k == 1) {
                return Math.min(nums1[start1], nums2[start2]);
            }
            int p1 = Math.min(start1 + k / 2 - 1, nums1.length - 1);
            int p2 = Math.min(start2 + k / 2 - 1, nums2.length - 1);
            if (nums1[p1] < nums2[p2]) {
                // 排除 [start1, p1]中间所有的元素，元素个数为 p1- start1 + 1;
                k -= (p1 - start1 + 1);
                start1 = p1 + 1;
            } else {
                // 排除 [start2, p2]中间所有的元素，元素个数为 p2- start2 + 1;
                k -= (p2 - start2  + 1);
                start2 = p2 + 1;
            }
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0004_MedianOfTwoSortedArrays s = new LC0004_MedianOfTwoSortedArrays();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2.00000, s.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2.50000, s.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!TestUtil.checkEq(1.00000, s.findMedianSortedArrays(new int[]{}, new int[]{1}), "边界-一个数组为空(奇数)")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-一个数组为空(奇数) 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2.50000, s.findMedianSortedArrays(new int[]{}, new int[]{2, 3}), "边界-一个数组为空(偶数)")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-一个数组为空(偶数) 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1.00000, s.findMedianSortedArrays(new int[]{1, 1}, new int[]{1, 1}), "边界-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0.00000, s.findMedianSortedArrays(new int[]{0}, new int[]{0}), "边界-各一个元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-各一个元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-3.50000, s.findMedianSortedArrays(new int[]{-5, -3}, new int[]{-4, -2}), "边界-负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-负数 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3.50000, s.findMedianSortedArrays(new int[]{1, 2, 3, 4, 5}, new int[]{6}), "边界-大小悬殊")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-大小悬殊 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0.00000, s.findMedianSortedArrays(new int[]{-1000000}, new int[]{1000000}), "边界-极端大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-极端大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}