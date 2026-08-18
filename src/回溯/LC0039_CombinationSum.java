// ============================================================
// LeetCode 39. 组合总和 (Combination Sum)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/combination-sum/
// 刷题日期：2026-08-18
//
// ============================================================

import java.util.*;

public class LC0039_CombinationSum {

    // ==== 提交代码开始 ====
    List<List<Integer>> ans;
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        ans = new ArrayList<>();
        Arrays.sort(candidates);
        dfs(candidates, 0, target, new ArrayList<>());
        return ans;
    }

    private void dfs(int[] candidates, int index, int remain, List<Integer> path) {
        if (remain == 0) {
            ans.add(new ArrayList<>(path));
            return;
        }

        for (int i = index; i < candidates.length; i++) {
            if (candidates[i] > remain) {
                break;
            }
            path.add(candidates[i]);
            dfs(candidates, i, remain - candidates[i], path);
            path.remove(path.size() - 1);
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0039_CombinationSum s = new LC0039_CombinationSum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(2, 2, 3), Arrays.asList(7)), s.combinationSum(new int[]{2, 3, 6, 7}, 7), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(2, 2, 2, 2), Arrays.asList(2, 3, 3), Arrays.asList(3, 5)), s.combinationSum(new int[]{2, 3, 5}, 8), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(), s.combinationSum(new int[]{2}, 1), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1-无限次重复（全 1）：[1] 凑 3 -> [[1,1,1]]，验证同一数可无限选取
        try { if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(1, 1, 1)), s.combinationSum(new int[]{1}, 3), "边界1-无限次重复")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-无限次重复 异常: " + t); }
        // 边界2-无解：全偶数凑奇数 5 -> 空列表
        try { if (!TestUtil.checkEqUnordered(Arrays.asList(), s.combinationSum(new int[]{2, 4, 6}, 5), "边界2-无解")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-无解 异常: " + t); }
        // 边界3-混合重复：1 和 2 凑 4 -> [1,1,1,1]、[1,1,2]、[2,2]，验证不同重复次数组合不重不漏
        try { if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(1, 1, 1, 1), Arrays.asList(1, 1, 2), Arrays.asList(2, 2)), s.combinationSum(new int[]{1, 2}, 4), "边界3-混合重复")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3-混合重复 异常: " + t); }
        // 边界4-候选恰等于目标：1/2/3 凑 3 -> [1,1,1]、[1,2]、[3] 三种都要
        try { if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(1, 1, 1), Arrays.asList(1, 2), Arrays.asList(3)), s.combinationSum(new int[]{1, 2, 3}, 3), "边界4-候选恰为目标")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-候选恰为目标 异常: " + t); }
        // 边界5-单元素整除：5 凑 10 -> [5,5]，整除时才有一个组合
        try { if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(5, 5)), s.combinationSum(new int[]{5}, 10), "边界5-单元素整除")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5-单元素整除 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}