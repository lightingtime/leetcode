// ============================================================
// LeetCode 1. 两数之和 (Two Sum)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/two-sum/
// 刷题日期：2026-08-01
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0001_TwoSum {

    // ==== 提交代码开始 ====
    public int[] twoSum(int[] nums, int target) {
        return null; // TODO: 在这里实现你的解法
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0001_TwoSum s = new LC0001_TwoSum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(new int[]{0, 1}, s.twoSum(new int[]{2, 7, 11, 15}, 9), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(new int[]{1, 2}, s.twoSum(new int[]{3, 2, 4}, 6), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(new int[]{0, 1}, s.twoSum(new int[]{3, 3}, 6), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // TODO: 补充空输入 / 单元素 / 全相同 / 大数等边界
        // 例如： try { if (!checkEq(期望, s.twoSum(边界输入), "边界1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    static boolean checkEq(Object expected, Object actual, String label) {
        String e = norm(expected);
        String a = norm(actual);
        if (e.equals(a)) { System.out.println(label + " 通过 ✓"); return true; }
        System.out.println(label + " 失败 ✗ 期望=" + e + " 实际=" + a);
        return false;
    }

    static String norm(Object o) {
        if (o == null) return "null";
        if (o instanceof int[]) return Arrays.toString((int[]) o);
        if (o instanceof long[]) return Arrays.toString((long[]) o);
        if (o instanceof double[]) return Arrays.toString((double[]) o);
        if (o instanceof boolean[]) return Arrays.toString((boolean[]) o);
        if (o instanceof char[]) return Arrays.toString((char[]) o);
        if (o instanceof Object[]) return Arrays.deepToString((Object[]) o);
        return String.valueOf(o);
    }

}