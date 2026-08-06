// ============================================================
// LeetCode 215. 数组中的第K个最大元素 (Kth Largest Element in an Array)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/kth-largest-element-in-an-array/
// 刷题日期：2026-08-07
// ============================================================

public class LC0215_KthLargestElementInAnArray {

    // ==== 提交代码开始 ====
    public int findKthLargest(int[] nums, int k) {
        int n = nums.length;
        return quickSort(nums, 0, n - 1, n - k);
    }

    private int quickSort(int[] nums, int l, int r, int k) {
        if (l == r) {
            return nums[k];
        }
        int x = nums[l], i = l - 1, j = r + 1;
        while (i < j) {
            do {
                i++;
            } while (nums[i] < x);
            do {
                j--;
            } while ( nums[j] > x);
            if (i < j) {
                int temp = nums[j];
                nums[j] = nums[i];
                nums[i] = temp;
            }
        }
        if (k <= j) {
            return quickSort(nums, l, j, k);
        }
        return quickSort(nums, j + 1, r, k);
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0215_KthLargestElementInAnArray s = new LC0215_KthLargestElementInAnArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(5, s.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!TestUtil.checkEq(5, s.findKthLargest(new int[]{5}, 1), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.findKthLargest(new int[]{3, 2, 1}, 3), "边界2-k等于长度取最小")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.findKthLargest(new int[]{2, 2, 2, 2}, 2), "边界3-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-2, s.findKthLargest(new int[]{-1, -2, -3, -4}, 2), "边界4-全负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(10000, s.findKthLargest(new int[]{-10000, 0, 10000}, 1), "边界5-极值范围")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.findKthLargest(new int[]{5, 4, 3, 2, 1}, 5), "边界6-降序取最小")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 2), "边界7-重复值干扰")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
