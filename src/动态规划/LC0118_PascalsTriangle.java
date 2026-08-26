// ============================================================
// LeetCode 118. 杨辉三角 (Pascal's Triangle)
// 难度：Easy | 分类：动态规划
// 链接：https://leetcode.cn/problems/pascals-triangle/
// 刷题日期：2026-08-26
//
// 思路：线性 DP 逐行递推——第 1 行固定 [1]；第 i 行长 i，首尾置 1，
//       内部 cur[j] = prev[j-1] + prev[j]，由上一行直接推下一行
// DP 子类型：线性 DP——状态沿「行」线性推进，每个数只由上一行的固定几个相邻位置决定，
//            不涉及区间/背包/树形结构
// 复杂度：时间 O(n²)（总元素 n(n+1)/2 每个算一次）
//         空间 O(n²) 输出不计则辅助 O(行长)（helper 的临时数组）
// ============================================================

import java.util.*;

public class LC0118_PascalsTriangle {

    // ==== 提交代码开始 ====
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        ans.add(list);
        for (int i = 2; i <= numRows; i++) {
            List<Integer> temp = generateHelper(ans.get(ans.size() - 1));
            ans.add(temp);
        }
        return ans;
    }

    private List<Integer> generateHelper(List<Integer> list) {
        int[] cur = new int[list.size() + 1];
        cur[0] = 1;
        for (int i = 1; i < cur.length - 1; i++) {
            cur[i] = list.get(i - 1) + list.get(i);
        }
        cur[cur.length - 1] = 1;
        return Arrays.stream(cur).boxed().toList();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0118_PascalsTriangle s = new LC0118_PascalsTriangle();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(1, 1), Arrays.asList(1, 2, 1), Arrays.asList(1, 3, 3, 1), Arrays.asList(1, 4, 6, 4, 1)), s.generate(5), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(1)), s.generate(1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= numRows <= 30；第 30 行最大值 C(29,14)=77558760 < 2^31-1，int 不会溢出
        // 边界1: numRows=2，最小非平凡（两行）
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(1, 1)), s.generate(2), "边界1: 两行")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: numRows=3，第一次真正用到「上一行相邻两数之和」
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(1, 1), Arrays.asList(1, 2, 1)), s.generate(3), "边界2: 三行首个递推")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 第 10 行精确值（二项式系数 C(9,k)），验证递推数值正确性
        try {
            List<List<Integer>> r10 = s.generate(10);
            if (!TestUtil.checkEq(Arrays.asList(1, 9, 36, 84, 126, 126, 84, 36, 9, 1), r10.get(9), "边界3: 第10行精确值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: numRows=30 上限——行数、行长、首尾为 1、内部严格满足递推、总元素数 465
        try {
            List<List<Integer>> big = s.generate(30);
            boolean ok = big.size() == 30;
            int total = 0;
            for (int i = 0; i < 30 && ok; i++) {
                List<Integer> row = big.get(i);
                total += row.size();
                if (row.size() != i + 1 || row.get(0) != 1 || row.get(row.size() - 1) != 1) ok = false;
                for (int j = 1; j + 1 < row.size() && ok; j++) {
                    if (row.get(j).intValue() != big.get(i - 1).get(j - 1) + big.get(i - 1).get(j)) ok = false;
                }
            }
            if (total != 465) ok = false;
            if (!TestUtil.checkEq(true, ok, "边界4: 30行结构自洽")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}