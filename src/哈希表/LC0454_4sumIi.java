// ============================================================
// LeetCode 454. 四数相加 II (4Sum II)
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/4sum-ii/
// 刷题日期：2026-08-21
//
// ============================================================

import java.util.*;

public class LC0454_4sumIi {

    // ==== 提交代码开始 ====
    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        Map<Integer, Integer> map = new HashMap<>();
        int ans = 0;
        for (int i = 0; i < nums1.length; i++) {
            for (int j = 0; j < nums2.length; j++) {
                map.merge(nums1[i] + nums2[j], 1, Integer::sum);
            }
        }
        for (int i = 0; i < nums3.length; i++) {
            for (int j = 0; j < nums4.length; j++) {
                int a = -(nums3[i] + nums4[j]);
                ans += map.getOrDefault(a, 0);
            }
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0454_4sumIi s = new LC0454_4sumIi();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2, s.fourSumCount(new int[]{1, 2}, new int[]{-2, -1}, new int[]{-1, 2}, new int[]{0, 2}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.fourSumCount(new int[]{0}, new int[]{0}, new int[]{0}, new int[]{0}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: 无解（所有和都为正）
        try { if (!TestUtil.checkEq(0, s.fourSumCount(new int[]{1, 2}, new int[]{3, 4}, new int[]{5, 6}, new int[]{7, 8}), "边界1: 无解")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: n=1 有解
        try { if (!TestUtil.checkEq(1, s.fourSumCount(new int[]{1}, new int[]{-1}, new int[]{2}, new int[]{-2}), "边界2: n=1有解")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }

        // 边界3: n=1 无解
        try { if (!TestUtil.checkEq(0, s.fourSumCount(new int[]{1}, new int[]{1}, new int[]{1}, new int[]{1}), "边界3: n=1无解")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }

        // 边界4: 重复下标组合计数（i 2 种 × j 2 种 = 4）
        try { if (!TestUtil.checkEq(4, s.fourSumCount(new int[]{1, 1}, new int[]{-1, -1}, new int[]{0}, new int[]{0}), "边界4: 重复组合")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: 全 0，n=200 上限，组合数 = 200^4 = 16 亿（验证 int 不溢出 + O(n^2) 性能）
        try {
            int[] zeros = new int[200];
            if (!TestUtil.checkEq(1_600_000_000, s.fourSumCount(zeros, zeros, zeros, zeros), "边界5: 全0 200^4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        // 边界6: 端点值 -2^28 与 2^28 抵消
        try { if (!TestUtil.checkEq(1, s.fourSumCount(new int[]{-268435456}, new int[]{268435456}, new int[]{0}, new int[]{0}), "边界6: 端点抵消")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        // 边界7: 多组解混合正负（(0,0,0,0)(1,1,0,0)(0,0,1,1)(1,1,1,1) 共 4 组）
        try { if (!TestUtil.checkEq(4, s.fourSumCount(new int[]{0, 1}, new int[]{0, -1}, new int[]{0, 2}, new int[]{0, -2}), "边界7: 多解")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}