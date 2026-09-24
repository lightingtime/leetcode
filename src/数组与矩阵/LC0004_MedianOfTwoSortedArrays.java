// ============================================================
// LeetCode 4. 寻找两个正序数组的中位数 (Median of Two Sorted Arrays)
// 难度：Hard | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/median-of-two-sorted-arrays/
// 刷题日期：2026-09-24
//
// ============================================================


public class LC0004_MedianOfTwoSortedArrays {

    // ==== 提交代码开始 ====
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;
        int l = -1, r = m;
        while (l + 1 < r) {
            int i = l + (r - l) / 2;
            int j = (m + n + 1) / 2 - i - 2;
            if (nums1[i] <= nums2[j + 1]) {
                l = i;
            } else {
                r = i;
            }
        }
        int i = l + (r - l) / 2;
        int j = (m + n + 1) / 2 - i - 2;
        int ai = i >= 0 ? nums1[i] : Integer.MIN_VALUE;
        // j < 0 表示 nums2 未进入左半，用 Math.max 的单位元避免污染左半最大值
        int bj = j >= 0 ? nums2[j] : Integer.MIN_VALUE;
        int aip1 = i + 1 < m ? nums1[i + 1] : Integer.MAX_VALUE;
        int bjp1 = j + 1 < n ? nums2[j + 1] : Integer.MAX_VALUE;
        int maxInMin = Math.max(ai, bj);
        int minInMax = Math.min(aip1, bjp1);

        // 奇数长度取左半最大，偶数长度取左半最大与右半最小的平均
        return (m + n) % 2 != 0 ? maxInMin : (maxInMin + minInMax) / 2.0;
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