// ============================================================
// LeetCode 416. 分割等和子集 (Partition Equal Subset Sum)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
// 刷题日期：2026-08-19
//
// ============================================================

import java.util.*;

public class LC0416_PartitionEqualSubsetSum {

    // ==== 提交代码开始 ====
    public boolean canPartition(int[] nums) {
        int sum =0;
        for (int num : nums) {
            sum += num;
        }
        if ((sum & 1) == 1) {
            return false;
        }
        sum /= 2;
        HashSet<Integer> set = new HashSet<>();
        set.add(0);
        for (int num : nums) {
            HashSet<Integer> copy = new HashSet<>(set);
            for (Integer i : copy) {
                set.add(i + num);
                if (set.contains(sum)) {
                    return true;
                }
            }
        }
        return false;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0416_PartitionEqualSubsetSum s = new LC0416_PartitionEqualSubsetSum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.canPartition(new int[]{1, 5, 11, 5}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canPartition(new int[]{1, 2, 3, 5}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对子集和 DP 设计）----
        // 单元素：和为奇数，必 false
        try {
            if (!TestUtil.checkEq(false, s.canPartition(new int[]{1}), "边界-单元素奇数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素奇数 异常: " + t); }
        // 单元素偶数：target=1 凑不出，false
        try {
            if (!TestUtil.checkEq(false, s.canPartition(new int[]{2}), "边界-单元素偶数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素偶数 异常: " + t); }
        // 两元素相等：各取一半
        try {
            if (!TestUtil.checkEq(true, s.canPartition(new int[]{3, 3}), "边界-两元素相等")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两元素相等 异常: " + t); }
        // 两元素不等：和为奇数，false
        try {
            if (!TestUtil.checkEq(false, s.canPartition(new int[]{3, 4}), "边界-两元素不等")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两元素不等 异常: " + t); }
        // 和偶数可拆：1+4=5
        try {
            if (!TestUtil.checkEq(true, s.canPartition(new int[]{1, 2, 3, 4}), "边界-和偶数可拆")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-和偶数可拆 异常: " + t); }
        // 全一偶数个：100 拆成两半各 50 个 1
        try {
            int[] ones = new int[100];
            java.util.Arrays.fill(ones, 1);
            if (!TestUtil.checkEq(true, s.canPartition(ones), "边界-全一偶数个")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全一偶数个 异常: " + t); }
        // 全一奇数个：和为奇数，false
        try {
            if (!TestUtil.checkEq(false, s.canPartition(new int[]{1, 1, 1}), "边界-全一奇数个")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全一奇数个 异常: " + t); }
        // 总和偶数但 target 凑不出：8/2=4，{1,2,5} 任何子集都不等于 4
        try {
            if (!TestUtil.checkEq(false, s.canPartition(new int[]{1, 2, 5}), "边界-偶数但凑不出")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-偶数但凑不出 异常: " + t); }
        // 单个大元素恰好等于 target
        try {
            if (!TestUtil.checkEq(true, s.canPartition(new int[]{5, 3, 2}), "边界-大元素即target")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-大元素即target 异常: " + t); }
        // 大数组 200 个 100：target=10000，验证性能与布尔 DP 容量
        {
            int[] big = new int[200];
            java.util.Arrays.fill(big, 100);
            try {
                if (!TestUtil.checkEq(true, s.canPartition(big), "边界-大数组")) failures++;
            } catch (Throwable t) { failures++; System.out.println("边界-大数组 异常: " + t); }
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}