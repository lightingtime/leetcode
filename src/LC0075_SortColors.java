// ============================================================
// LeetCode 75. 颜色分类 (Sort Colors)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/sort-colors/
// 刷题日期：2026-08-29（二刷 · 一刷 2026-08-05，一刷非一次 AC：0 分支误重置 mid 导致非严格一趟）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0075_SortColors {

    // ==== 提交代码开始 ====
    public void sortColors(int[] nums) {
        // TODO: 在这里实现你的解法
        
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0075_SortColors s = new LC0075_SortColors();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        int[] n1 = {2, 0, 2, 1, 1, 0};
        s.sortColors(n1);
        if (!TestUtil.checkEq(new int[]{0, 0, 1, 1, 2, 2}, n1, "示例1")) failures++;

        int[] n2 = {2, 0, 1};
        s.sortColors(n2);
        if (!TestUtil.checkEq(new int[]{0, 1, 2}, n2, "示例2")) failures++;

        // ---- 边界测试（自己补充）----
        // TODO: 补充空输入 / 单元素 / 全相同 / 大数等边界
        // 例如： try { if (!TestUtil.checkEq(期望, s.sortColors(边界输入), "边界1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 若题目允许任意顺序返回（下标对 / 集合），用 TestUtil.checkEqUnordered 代替 TestUtil.checkEq

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}