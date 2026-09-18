// ============================================================
// LeetCode 46. 全排列 (Permutations)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/permutations/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-09 · 一刷一次 Accepted）
// 一刷写法：回溯 + used 标记——每层从「还没用过的数」里挑一个追加到路径尾部，递归到底收集答案，返回前撤销这次选择（used 归位、路径去掉末尾）。O(n·n!)/O(n)
// 本题易错点：① 选择与撤销必须一一对应、严格对称；② 收集答案要放路径的**拷贝**（new ArrayList<>(path)），直接放引用会被后续回溯改坏；③ 本题元素互不相同，不需要去重（对比 LC47 有重复元素时才要在同层跳过重复值）；④ 题目约束 nums.length ≤ 6，别用越界规模的输入当测试
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 1 个：长度 6 的 720 个互不相同排列）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0046_Permutations {

    // ==== 提交代码开始 ====
    List<List<Integer>> ans;
    boolean[] visited;
    public List<List<Integer>> permute(int[] nums) {
        ans = new ArrayList<>();
        visited = new boolean[nums.length];
        Arrays.sort(nums);
        dfs(nums, new ArrayList<>());
        return ans;
    }

    private void dfs(int[] nums, ArrayList<Integer> list) {
        if (list.size() == nums.length) {
            ans.add(new ArrayList<>(list));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) {
                continue;
            }
            visited[i] = true;
            list.add(nums[i]);
            dfs(nums, list);
            list.remove(list.size() - 1);
            visited[i] = false;
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

        try {
            // 二刷复查补充：负数元素（约束允许 -10..10），逐条校验元素集合与重复情况
            List<List<Integer>> rn = s.permute(new int[]{-1, -2, -3});
            boolean okn = rn != null && rn.size() == 6 && rn.stream().distinct().count() == 6
                    && rn.stream().allMatch(p -> p.size() == 3
                            && new HashSet<>(p).equals(new HashSet<>(Arrays.asList(-1, -2, -3))));
            if (!okn) {
                failures++;
                System.out.println("边界-负数元素 失败 ✗ 期望 6 个互不相同且元素集合正确的排列，实际=" + (rn == null ? "null" : rn.size()));
            } else {
                System.out.println("边界-负数元素 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-负数元素 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
