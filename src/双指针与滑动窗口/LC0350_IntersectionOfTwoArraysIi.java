// ============================================================
// LeetCode 350. 两个数组的交集 II (Intersection of Two Arrays II)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/intersection-of-two-arrays-ii/
// 刷题日期：2026-08-22
//
// 思路：值域 0..1000 固定，用两个长度 1001 的计数数组分别统计两数组出现次数，再对每个值取 min 次数输出。
// 复杂度：时间 O(m+n+1001)，空间 O(1001)（常数）
// ============================================================

import java.util.*;

public class LC0350_IntersectionOfTwoArraysIi {

    // ==== 提交代码开始 ====
    public int[] intersect(int[] nums1, int[] nums2) {
        List<Integer> ans = new ArrayList<>();
        int[] a = new int[1001];
        int[] b = new int[1001];
        for (int num : nums1) {
            a[num]++;
        }
        for (int num : nums2) {
            b[num]++;
        }
        for (int i = 0; i < 1001; i++) {
            if (a[i] > 0 && b[i] > 0) {
                int min = Math.min(a[i], b[i]);
                for (int j = 0; j < min; j++) {
                    ans.add(i);
                }
            }
        }
        return ans.stream().mapToInt(Integer::intValue).toArray();
    }
    // ==== 提交代码结束 ====

    // ---- 测试辅助 ----
    static int[] range(int from, int toExclusive) {
        int[] a = new int[toExclusive - from];
        for (int i = 0; i < a.length; i++) a[i] = from + i;
        return a;
    }

    public static void main(String[] args) {
        LC0350_IntersectionOfTwoArraysIi s = new LC0350_IntersectionOfTwoArraysIi();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[]{2, 2}, s.intersect(new int[]{1, 2, 2, 1}, new int[]{2, 2}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{4, 9}, s.intersect(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 完全无交集 -> 空数组
        try { if (!TestUtil.checkEqUnordered(new int[]{}, s.intersect(new int[]{1, 2, 3}, new int[]{4, 5, 6}), "边界1-无交集")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-无交集 异常: " + t); }
        // 2) 单元素且重合
        try { if (!TestUtil.checkEqUnordered(new int[]{1}, s.intersect(new int[]{1}, new int[]{1}), "边界2-单元素重合")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-单元素重合 异常: " + t); }
        // 3) 单元素不重合 -> 空
        try { if (!TestUtil.checkEqUnordered(new int[]{}, s.intersect(new int[]{1}, new int[]{2}), "边界3-单元素不重合")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3-单元素不重合 异常: " + t); }
        // 4) 两数组完全相同
        try { if (!TestUtil.checkEqUnordered(new int[]{1, 1, 1}, s.intersect(new int[]{1, 1, 1}, new int[]{1, 1, 1}), "边界4-完全相同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-完全相同 异常: " + t); }
        // 5) 出现次数取较小值（重复次数不等）
        try { if (!TestUtil.checkEqUnordered(new int[]{1, 1}, s.intersect(new int[]{1, 1, 1, 1}, new int[]{1, 1}), "边界5-次数取小")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5-次数取小 异常: " + t); }
        // 6) nums1 更短但整体是 nums2 的子集
        try { if (!TestUtil.checkEqUnordered(new int[]{2, 2}, s.intersect(new int[]{2, 2}, new int[]{1, 2, 2, 1}), "边界6-子集")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6-子集 异常: " + t); }
        // 7) 取值下界 0 参与交集
        try { if (!TestUtil.checkEqUnordered(new int[]{0, 0}, s.intersect(new int[]{0, 0, 1}, new int[]{0, 0, 0, 2}), "边界7-含0")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7-含0 异常: " + t); }
        // 8) 取值上限 1000 参与交集
        try { if (!TestUtil.checkEqUnordered(new int[]{999, 1000}, s.intersect(new int[]{1000, 999}, new int[]{999, 1000}), "边界8-上限值")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8-上限值 异常: " + t); }
        // 9) 长度上限 1000：nums1=0..999，nums2=1..1000，交集 1..999
        try {
            int[] r = new int[999];
            for (int i = 0; i < r.length; i++) r[i] = i + 1;
            if (!TestUtil.checkEqUnordered(r, s.intersect(range(0, 1000), range(1, 1001)), "边界9-上限长度")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9-上限长度 异常: " + t); }
        // 10) 长度上限 1000：两数组完全相同 0..999
        try { if (!TestUtil.checkEqUnordered(range(0, 1000), s.intersect(range(0, 1000), range(0, 1000)), "边界10-上限全重合")) failures++; } catch (Throwable t) { failures++; System.out.println("边界10-上限全重合 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}