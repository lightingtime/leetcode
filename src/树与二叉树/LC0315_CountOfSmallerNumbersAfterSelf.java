// ============================================================
// LeetCode 315. 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)
// 难度：Hard | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
// 刷题日期：2026-08-25
//
// ============================================================

import java.util.*;

public class LC0315_CountOfSmallerNumbersAfterSelf {

    // ==== 提交代码开始 ====
    int[] ans;
    int[] temp;
    int[] tempIndex;
    int[] index;
    public List<Integer> countSmaller(int[] nums) {
        ans = new int[nums.length];
        temp = new int[nums.length];
        tempIndex = new int[nums.length];
        index = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            index[i] = i;
        }
        mergeSort(nums, 0, nums.length - 1);
        return Arrays.stream(ans).boxed().toList();
    }

    private void mergeSort(int[] nums, int l, int r) {
        if (l >= r) {
            return;
        }
        int mid = l + (r - l) / 2;
        mergeSort(nums, l, mid);
        mergeSort(nums, mid + 1, r);
        merge(nums, l, mid, r);
    }

    private void merge(int[] nums, int l, int mid, int r) {
        int i = l;          // 左半指针
        int j = mid + 1;    // 右半指针
        int k = l;          // temp 写入位置
        int rightTaken = 0; // 已取走的右半元素个数

        while (i <= mid && j <= r) {
            if (nums[j] < nums[i]) {
                // 右半元素更小：先取走，它比当前左半元素小且在它右侧
                temp[k] = nums[j];
                tempIndex[k] = index[j];
                k++;
                j++;
                rightTaken++;
            } else {
                // 取走左半元素：之前取走的右半元素都是它的“右侧更小”
                ans[index[i]] += rightTaken;
                temp[k] = nums[i];
                tempIndex[k] = index[i];
                k++;
                i++;
            }
        }
        // 剩余左半元素：所有右半元素都比它们小
        while (i <= mid) {
            ans[index[i]] += rightTaken;
            temp[k] = nums[i];
            tempIndex[k] = index[i];
            k++;
            i++;
        }
        // 剩余右半元素：直接搬走，不加计数
        while (j <= r) {
            temp[k] = nums[j];
            tempIndex[k] = index[j];
            k++;
            j++;
        }
        // 合并结果拷回 nums 与 index
        for (int p = l; p <= r; p++) {
            nums[p] = temp[p];
            index[p] = tempIndex[p];
        }
    }


    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0315_CountOfSmallerNumbersAfterSelf s = new LC0315_CountOfSmallerNumbersAfterSelf();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList(2, 1, 1, 0), s.countSmaller(new int[]{5, 2, 6, 1}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(0), s.countSmaller(new int[]{-1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(0, 0), s.countSmaller(new int[]{-1, -1}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题：单调性、重复元素、值域端点、大输入）----
        // 边界1: 递增（右侧无更小 -> 全 0）
        try {
            if (!TestUtil.checkEq(Arrays.asList(0, 0, 0, 0), s.countSmaller(new int[]{1, 2, 3, 4}), "边界1: 递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 递减（每个元素右侧都比它小）
        try {
            if (!TestUtil.checkEq(Arrays.asList(3, 2, 1, 0), s.countSmaller(new int[]{4, 3, 2, 1}), "边界2: 递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 全相同（严格小于，相等不算）
        try {
            if (!TestUtil.checkEq(Arrays.asList(0, 0, 0), s.countSmaller(new int[]{5, 5, 5}), "边界3: 全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 值域端点 -10^4 与 10^4
        try {
            if (!TestUtil.checkEq(Arrays.asList(0, 1, 0), s.countSmaller(new int[]{-10000, 10000, 0}), "边界4: 值域端点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 高低交替（大值多次出现）
        try {
            if (!TestUtil.checkEq(Arrays.asList(2, 0, 1, 0), s.countSmaller(new int[]{10000, -10000, 10000, -10000}), "边界5: 交替")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 大输入 10^5 全相同（验证大 n + 重复元素）
        try {
            int n = 100000;
            int[] big = new int[n];
            Arrays.fill(big, 5);
            List<Integer> exp = new ArrayList<>(n);
            for (int i = 0; i < n; i++) exp.add(0);
            if (!TestUtil.checkEq(exp, s.countSmaller(big), "边界6: 大输入全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}