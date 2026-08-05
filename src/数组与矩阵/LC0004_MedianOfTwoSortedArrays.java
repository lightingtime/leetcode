// ============================================================
// LeetCode 4. 寻找两个正序数组的中位数 (Median of Two Sorted Arrays)
// 难度：Hard | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/median-of-two-sorted-arrays/
// 刷题日期：2026-08-06
//
// 思路：两数组找第 k 小——每轮各取有效部分第 k/2 个候选比较，排除较小一侧
//       （候选及其前面的元素），k 减去实际排除数量；出口：一边排空或 k==1。
// 复杂度：时间 O(log(m+n))，空间 O(1)
// ============================================================

import java.util.*;

public class LC0004_MedianOfTwoSortedArrays {

    // ==== 提交代码开始 ====
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len1 = nums1.length, len2 = nums2.length;
        int totLen = len1 + len2;
        // 中位数换算成「第 k 小」（1 基排名，不是数组下标）：
        // 奇数总长 → 第 totLen/2 + 1 小；偶数总长 → 第 totLen/2 小与第 totLen/2 + 1 小取平均。
        if (totLen % 2 == 1) {
            return kthSmallest(nums1, nums2, totLen / 2 + 1);
        }
        int k1 = totLen / 2, k2 = totLen / 2 + 1;
        return (kthSmallest(nums1, nums2, k1) + kthSmallest(nums1, nums2, k2)) / 2.0;
    }

    /**
     * 返回两数组「剩余有效部分」合并后第 k 小的元素值。
     * 约定：k 是 1 基排名（k=1 表示最小元素），不是数组下标。
     * p1/p2 是两个数组当前的有效起点（0 基下标），下标小于 p1/p2 的元素已被排除。
     */
    private int kthSmallest(int[] nums1, int[] nums2, int k) {
        int len1 = nums1.length, len2 = nums2.length;
        int p1 = 0, p2 = 0; // 有效起点，只会向右移动，表示「前面有多少元素已被跳过」
        while (true) {
            // 出口：某一边已排空，答案在另一边的当前有效部分里
            if (p1 >= len1) {
                return nums2[p2 + k - 1];
            } else if (p2 >= len2) {
                return nums1[p1 + k - 1];
            } else if (k == 1) {
                return Math.min(nums1[p1], nums2[p2]);
            }
            // 候选下标 = 各自有效部分里第 k/2 个元素的下标；数组不够长时封顶到最后一个
            int cand1 = Math.min(len1 - 1, p1 + k / 2 - 1);
            int cand2 = Math.min(len2 - 1, p2 + k / 2 - 1);
            if (nums1[cand1] <= nums2[cand2]) {
                // 排除 nums1 从 p1 到 cand1（含候选），数量 = 候选下标 - 旧起点 + 1
                int removed = cand1 - p1 + 1;
                p1 = cand1 + 1;
                k -= removed;
            } else {
                int removed = cand2 - p2 + 1;
                p2 = cand2 + 1;
                k -= removed;
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
