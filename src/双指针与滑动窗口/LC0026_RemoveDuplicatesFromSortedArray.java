// ============================================================
// LeetCode 26. 删除有序数组中的重复项 (Remove Duplicates from Sorted Array)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/remove-duplicates-from-sorted-array/
// 刷题日期：2026-08-22
//
// ============================================================

import java.util.*;

public class LC0026_RemoveDuplicatesFromSortedArray {

    // ==== 提交代码开始 ====
    public int removeDuplicates(int[] nums) {
        int l = 0;
        for (int num : nums) {
            if (l == 0 || nums[l - 1] != num) nums[l++] = num;
        }
        return l;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0026_RemoveDuplicatesFromSortedArray s = new LC0026_RemoveDuplicatesFromSortedArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        if (!checkRemove(s, "示例1", new int[]{1, 1, 2}, new int[]{1, 2})) failures++;
        if (!checkRemove(s, "示例2", new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}, new int[]{0, 1, 2, 3, 4})) failures++;

        // ---- 边界测试（自己补充）----
        // 单元素（约束 nums.length >= 1 的最小情况，无重复可删）
        if (!checkRemove(s, "边界1-单元素", new int[]{7}, new int[]{7})) failures++;
        // 两元素全相同
        if (!checkRemove(s, "边界2-两元素全相同", new int[]{5, 5}, new int[]{5})) failures++;
        // 全相同（同值大量重复，只留 1 个）
        if (!checkRemove(s, "边界3-全相同", new int[]{2, 2, 2, 2, 2}, new int[]{2})) failures++;
        // 无重复（严格递增，覆盖值域两端 -100 / 100）
        if (!checkRemove(s, "边界4-无重复", new int[]{-100, -99, -98, 0, 99, 100}, new int[]{-100, -99, -98, 0, 99, 100})) failures++;
        // 头部与尾部重复交错
        if (!checkRemove(s, "边界5-头尾重复", new int[]{1, 1, 2, 3, 3}, new int[]{1, 2, 3})) failures++;
        // 连续多段重复（值域内负值重复多段）
        if (!checkRemove(s, "边界6-连续多段重复", new int[]{-3, -3, -3, -2, -1, -1, 0, 0, 0, 1}, new int[]{-3, -2, -1, 0, 1})) failures++;
        // 大输入（长度贴近约束上限 3e4，值仍限制在 [-100,100] 内）
        {
            int n = 30000;
            int[] big = new int[n];
            int idx = 0;
            for (int v = -100; v <= 100 && idx < n; v++) {
                for (int c = 0; c < 149 && idx < n; c++) big[idx++] = v;
            }
            while (idx < n) big[idx++] = 100; // 剩余补 100，保持非递减
            int[] expected = new int[201];
            for (int v = -100; v <= 100; v++) expected[v + 100] = v;
            if (!checkRemove(s, "边界7-大输入", big, expected)) failures++;
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 校验：返回值 k == expectedUnique.length，且 nums 前 k 个元素等于 expectedUnique（其后元素判题忽略）
    private static boolean checkRemove(LC0026_RemoveDuplicatesFromSortedArray s, String label, int[] input, int[] expectedUnique) {
        int[] nums = input.clone();
        int k;
        try {
            k = s.removeDuplicates(nums);
        } catch (Throwable t) {
            System.out.println(label + " 异常: " + t);
            return false;
        }
        boolean ok = true;
        if (k != expectedUnique.length) {
            System.out.println(label + " 失败 ✗ 长度 期望=" + expectedUnique.length + " 实际=" + k);
            ok = false;
        }
        int[] prefix = Arrays.copyOf(nums, k);
        if (!Arrays.equals(prefix, expectedUnique)) {
            System.out.println(label + " 失败 ✗ 前缀 期望=" + Arrays.toString(expectedUnique) + " 实际=" + Arrays.toString(prefix));
            ok = false;
        }
        if (ok) System.out.println(label + " 通过 ✓");
        return ok;
    }

}