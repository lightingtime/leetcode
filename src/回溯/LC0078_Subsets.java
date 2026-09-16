// ============================================================
// LeetCode 78. 子集 (Subsets)
// 难度：Medium | 分类：回溯
// 链接：https://leetcode.cn/problems/subsets/
// 复习日期：2026-09-16（第 4 次复习 · 一刷 2026-08-09 · 上次 2026-09-06 较强 · 间隔 10 天到期）
// 一刷写法：回溯（选/不选）；上次写法：回溯（起点推进），每个节点都是唯一子集
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
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

    private void dfs(int[] nums, int index, List<Integer> path) {
        ans.add(new ArrayList<>(path));

        for (int i = index; i < nums.length; i++) {
            path.add(nums[i]);
            dfs(nums, i + 1, path);
            path.remove(path.size() - 1);
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0078_Subsets s = new LC0078_Subsets();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(), Arrays.asList(1), Arrays.asList(2), Arrays.asList(1, 2), Arrays.asList(3), Arrays.asList(1, 3), Arrays.asList(2, 3), Arrays.asList(1, 2, 3)), s.subsets(new int[]{1, 2, 3}), "示例1"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(), Arrays.asList(0)), s.subsets(new int[]{0}), "示例2"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（与一刷归档保持一致）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList(Arrays.asList(), Arrays.asList(1), Arrays.asList(2), Arrays.asList(1, 2)), s.subsets(new int[]{1, 2}), "边界-两元素"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-两元素 异常: " + t);
        }
        try {
            List<List<Integer>> r10 = s.subsets(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
            boolean ok = r10 != null && r10.size() == 1024 && r10.stream().distinct().count() == 1024;
            if (!ok) {
                failures++;
                System.out.println("边界-长度10 失败 ✗ 期望1024个互不相同的子集，实际=" + (r10 == null ? "null" : r10.size()));
            } else {
                System.out.println("边界-长度10 通过 ✓");
            }
        } catch (Throwable t) {
            failures++;
            System.out.println("边界-长度10 异常: " + t);
        }

        // ---- 补强边界（2026-09-06 增补：不删原归档用例）----
        try {
            List<List<Integer>> r10b = s.subsets(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
            boolean ok = r10b != null && r10b.contains(new ArrayList<>())
                    && r10b.contains(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            if (!ok) {
                failures++;
                System.out.println("边界-长度10关键成员 失败 ✗ 期望含空集与全集");
            } else {
                System.out.println("边界-长度10关键成员 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-长度10关键成员 异常: " + t); }
        try {
            List<List<Integer>> rn = s.subsets(new int[]{-1, 0, 1});
            boolean ok2 = rn != null && rn.size() == 8 && rn.stream().distinct().count() == 8
                    && rn.contains(new ArrayList<>()) && rn.contains(Arrays.asList(-1, 0, 1)) && rn.contains(Arrays.asList(-1));
            if (!ok2) {
                failures++;
                System.out.println("边界-负数 失败 ✗ 期望8个互异子集（含空集/全集/[-1]）");
            } else {
                System.out.println("边界-负数 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-负数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}