// ============================================================
// LeetCode 46. 全排列 (Permutations)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/permutations/
// 刷题日期：2026-08-09
//
// ============================================================

import java.util.*;

public class LC0046_Permutations {

    // ==== 提交代码开始 ====
    boolean[] used;
    List<List<Integer>> ans;
    public List<List<Integer>> permute(int[] nums) {
        ans = new ArrayList<>();
        if (nums.length == 0) {
            return ans;
        }
        used = new boolean[nums.length];
        dfs(nums, new ArrayList<>());
        return ans;
    }

    private void dfs(int[] nums, ArrayList<Integer> list) {
        if (list.size() == nums.length) {
            ans.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            list.add(nums[i]);
            dfs(nums, list);
            list.remove(list.size() - 1);
            used[i] = false;
        }
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0046_Permutations s = new LC0046_Permutations();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(1, 2, 3), Arrays.asList(1, 3, 2), Arrays.asList(2, 1, 3), Arrays.asList(2, 3, 1), Arrays.asList(3, 1, 2), Arrays.asList(3, 2, 1)), s.permute(new int[]{1, 2, 3}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(0, 1), Arrays.asList(1, 0)), s.permute(new int[]{0, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(1)), s.permute(new int[]{1}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            List<List<Integer>> r6 = s.permute(new int[]{1, 2, 3, 4, 5, 6});
            boolean ok = r6 != null && r6.size() == 720 && r6.stream().distinct().count() == 720;
            if (!ok) {
                failures++;
                System.out.println("边界-长度6 失败 ✗ 期望720个互不相同的排列，实际=" + (r6 == null ? "null" : r6.size()));
            } else {
                System.out.println("边界-长度6 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-长度6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
