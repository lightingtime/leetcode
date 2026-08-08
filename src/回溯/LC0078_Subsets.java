// ============================================================
// LeetCode 78. 子集 (Subsets)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/subsets/
// 刷题日期：2026-08-09
//
// ============================================================

import java.util.*;

public class LC0078_Subsets {

    // ==== 提交代码开始 ====
    List<List<Integer>> ans;
    public List<List<Integer>> subsets(int[] nums) {
        ans = new ArrayList<>();
        dfs(nums, 0, new ArrayList<>());
        return ans;
    }

    private void dfs(int[] nums, int index, List<Integer> list) {
        if (index >= nums.length) {
            ans.add(new ArrayList<>(list));
            return;
        }

            list.add(nums[index]);
            dfs(nums, index + 1, list);

            list.remove(list.size() - 1);
            dfs(nums, index + 1, list);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0078_Subsets s = new LC0078_Subsets();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(), Arrays.asList(1), Arrays.asList(2), Arrays.asList(1, 2), Arrays.asList(3), Arrays.asList(1, 3), Arrays.asList(2, 3), Arrays.asList(1, 2, 3)), s.subsets(new int[]{1, 2, 3}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(), Arrays.asList(0)), s.subsets(new int[]{0}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(), Arrays.asList(1), Arrays.asList(2), Arrays.asList(1, 2)), s.subsets(new int[]{1, 2}), "边界-两元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两元素 异常: " + t); }
        try {
            List<List<Integer>> r10 = s.subsets(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
            boolean ok = r10 != null && r10.size() == 1024 && r10.stream().distinct().count() == 1024;
            if (!ok) {
                failures++;
                System.out.println("边界-长度10 失败 ✗ 期望1024个互不相同的子集，实际=" + (r10 == null ? "null" : r10.size()));
            } else {
                System.out.println("边界-长度10 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-长度10 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
