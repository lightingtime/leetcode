// ============================================================
// LeetCode 88. 合并两个有序数组 (Merge Sorted Array)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/merge-sorted-array/
// 刷题日期：2026-08-22
//
// 思路：三指针从后往前原地合并 —— p1/p2 分别指向 nums1、nums2 的有效末尾，p 指向 nums1 尾部写入位，每次取较大者放到 p，避免覆盖 nums1 未合并的前段。
// 复杂度：时间 O(m+n)，空间 O(1)
// ============================================================


public class LC0088_MergeSortedArray {

    // ==== 提交代码开始 ====
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1, p2 = n - 1;
        int p = m + n - 1;
        while (p1 >= 0 || p2 >= 0) {
            if (p1 < 0) {
                nums1[p--] = nums2[p2];
                p2--;
            } else if (p2 < 0) {
                nums1[p--] = nums1[p1--];
            } else {
                if (nums1[p1] > nums2[p2]) {
                    nums1[p] = nums1[p1];
                    p1--;
                } else {
                    nums1[p] = nums2[p2];
                    p2--;
                }
                p--;
            }
        }

    }
    // ==== 提交代码结束 ====

    // ---- 测试辅助 ----
    static int[] range(int from, int toExclusive) {
        int[] a = new int[toExclusive - from];
        for (int i = 0; i < a.length; i++) a[i] = from + i;
        return a;
    }

    public static void main(String[] args) {
        LC0088_MergeSortedArray s = new LC0088_MergeSortedArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        {
            int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
            s.merge(nums1, 3, new int[]{2, 5, 6}, 3);
            if (!TestUtil.checkEq(new int[]{1, 2, 2, 3, 5, 6}, nums1, "示例1")) failures++;
        }
        {
            int[] nums1 = new int[]{1};
            s.merge(nums1, 1, new int[]{}, 0);
            if (!TestUtil.checkEq(new int[]{1}, nums1, "示例2")) failures++;
        }
        {
            int[] nums1 = new int[]{0};
            s.merge(nums1, 0, new int[]{1}, 1);
            if (!TestUtil.checkEq(new int[]{1}, nums1, "示例3")) failures++;
        }

        // ---- 边界测试（本题具体逻辑）----
        // 1) n=0：nums2 空，nums1 原样保留
        {
            int[] nums1 = new int[]{1, 2, 3};
            s.merge(nums1, 3, new int[]{}, 0);
            if (!TestUtil.checkEq(new int[]{1, 2, 3}, nums1, "边界1-n2为空")) failures++;
        }
        // 2) m=0：nums1 全是占位 0，直接拷入 nums2
        {
            int[] nums1 = new int[]{0, 0};
            s.merge(nums1, 0, new int[]{1, 2}, 2);
            if (!TestUtil.checkEq(new int[]{1, 2}, nums1, "边界2-m为0")) failures++;
        }
        // 3) 两数组元素完全相同（考验相等元素时的稳定合并）
        {
            int[] nums1 = new int[]{1, 1, 1, 0, 0, 0};
            s.merge(nums1, 3, new int[]{1, 1, 1}, 3);
            if (!TestUtil.checkEq(new int[]{1, 1, 1, 1, 1, 1}, nums1, "边界3-全相同")) failures++;
        }
        // 4) nums2 全部更小
        {
            int[] nums1 = new int[]{5, 6, 7, 0, 0, 0};
            s.merge(nums1, 3, new int[]{1, 2, 3}, 3);
            if (!TestUtil.checkEq(new int[]{1, 2, 3, 5, 6, 7}, nums1, "边界4-nums2全小")) failures++;
        }
        // 5) nums2 全部更大
        {
            int[] nums1 = new int[]{1, 2, 3, 0, 0, 0};
            s.merge(nums1, 3, new int[]{4, 5, 6}, 3);
            if (!TestUtil.checkEq(new int[]{1, 2, 3, 4, 5, 6}, nums1, "边界5-nums2全大")) failures++;
        }
        // 6) 交错合并
        {
            int[] nums1 = new int[]{1, 3, 5, 0, 0, 0};
            s.merge(nums1, 3, new int[]{2, 4, 6}, 3);
            if (!TestUtil.checkEq(new int[]{1, 2, 3, 4, 5, 6}, nums1, "边界6-交错")) failures++;
        }
        // 7) 负数值（元素可为负）
        {
            int[] nums1 = new int[]{-1, 0, 0};
            s.merge(nums1, 1, new int[]{-2, -1}, 2);
            if (!TestUtil.checkEq(new int[]{-2, -1, -1}, nums1, "边界7-负数")) failures++;
        }
        // 8) 简单 m=1,n=1
        {
            int[] nums1 = new int[]{2, 0};
            s.merge(nums1, 1, new int[]{1}, 1);
            if (!TestUtil.checkEq(new int[]{1, 2}, nums1, "边界8-单元素")) failures++;
        }
        // 9) 约束上限：m=199,n=1，nums1 总长 200
        {
            int[] nums1 = new int[200];
            for (int i = 0; i < 199; i++) nums1[i] = i; // 0..198，最后一个占位 0
            s.merge(nums1, 199, new int[]{199}, 1);
            if (!TestUtil.checkEq(range(0, 200), nums1, "边界9-上限")) failures++;
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}