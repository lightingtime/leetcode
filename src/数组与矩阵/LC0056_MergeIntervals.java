// ============================================================
// LeetCode 56. 合并区间 (Merge Intervals)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/merge-intervals/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-06 · 一刷一次 Accepted）
// 一刷写法：按左端点排序 + 单遍合并——排序后相邻区间只可能重叠/相接或完全分离；用一个「当前区间」依次比较，能并就更新右端点，不能并就落袋再换新的。O(n log n)/O(n)
// 本题易错点（错误习惯库里这题暂为干净）：排序键是左端点；首尾相接（[1,2] 与 [2,3]）在本题算重叠，比较要用 <= 而非 <；合并右端点取两段的 max 而不是直接取后一段；结果用 List 收集，避免预分配大小
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 7 个：空输入/单元素/全相同/大数/包含/首尾相接/互不重叠）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0056_MergeIntervals {

    // ==== 提交代码开始 ====
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> list = new ArrayList<>();
        for (int[] interval : intervals) {
            if (list.isEmpty()) {
                list.add(new int[] {interval[0], interval[1]});
            } else {
                int[] last = list.get(list.size() - 1);
                if (last[1] >= interval[0]) {
                    if (last[1] < interval[1]) {
                        last[1] = interval[1];
                    }
                } else {
                    list.add(new int[] {interval[0], interval[1]});
                }
            }
        }
        return list.toArray(new int[0][]);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0056_MergeIntervals s = new LC0056_MergeIntervals();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 6}, new int[]{8, 10}, new int[]{15, 18}}, s.merge(new int[][]{new int[]{1, 3}, new int[]{2, 6}, new int[]{8, 10}, new int[]{15, 18}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 5}}, s.merge(new int[][]{new int[]{1, 4}, new int[]{4, 5}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 7}}, s.merge(new int[][]{new int[]{4, 7}, new int[]{1, 4}}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(new int[0][0], s.merge(new int[0][0]), "空输入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("空输入 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 3}}, s.merge(new int[][]{new int[]{1, 3}}), "单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 4}}, s.merge(new int[][]{new int[]{1, 4}, new int[]{1, 4}, new int[]{1, 4}}), "全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("全相同 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{0, 10000}}, s.merge(new int[][]{new int[]{0, 10000}, new int[]{9999, 10000}}), "大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("大数 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 10}}, s.merge(new int[][]{new int[]{1, 10}, new int[]{2, 3}, new int[]{4, 5}}), "包含")) failures++;
        } catch (Throwable t) { failures++; System.out.println("包含 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 4}}, s.merge(new int[][]{new int[]{1, 2}, new int[]{2, 3}, new int[]{3, 4}}), "首尾相接")) failures++;
        } catch (Throwable t) { failures++; System.out.println("首尾相接 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1, 2}, new int[]{3, 4}, new int[]{5, 6}}, s.merge(new int[][]{new int[]{5, 6}, new int[]{1, 2}, new int[]{3, 4}}), "互不重叠")) failures++;
        } catch (Throwable t) { failures++; System.out.println("互不重叠 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
