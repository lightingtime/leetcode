// ============================================================
// LeetCode 560. 和为 K 的子数组 (Subarray Sum Equals K)
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/subarray-sum-equals-k/
// 刷题日期：2026-08-11
// ============================================================

import java.util.*;

public class LC0560_SubarraySumEqualsK {

    // ==== 提交代码开始 ====
    public int subarraySum(int[] nums, int k) {
        int[] prefix = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            if (i == 0) {
                prefix[i] = nums[i];
            } else {
                prefix[i] = nums[i] + prefix[i - 1];
            }
        }
        int ans = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(prefix[i] - k)) {
                ans += map.get(prefix[i] - k);
            }
            map.put(prefix[i], map.getOrDefault(prefix[i], 0) + 1);
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0560_SubarraySumEqualsK s = new LC0560_SubarraySumEqualsK();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2, s.subarraySum(new int[]{1, 1, 1}, 2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.subarraySum(new int[]{1, 2, 3}, 3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq(1, s.subarraySum(new int[]{5}, 5), "单元素等于k")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素等于k 异常: " + t); }
        try { if (!TestUtil.checkEq(0, s.subarraySum(new int[]{5}, 3), "单元素不等于k")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素不等于k 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.subarraySum(new int[]{1, 1, 1}, 1), "全1求和1")) failures++; } catch (Throwable t) { failures++; System.out.println("全1求和1 异常: " + t); }
        try { if (!TestUtil.checkEq(6, s.subarraySum(new int[]{0, 0, 0}, 0), "全0求和0")) failures++; } catch (Throwable t) { failures++; System.out.println("全0求和0 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.subarraySum(new int[]{1, -1}, 0), "正负抵消")) failures++; } catch (Throwable t) { failures++; System.out.println("正负抵消 异常: " + t); }
        try { if (!TestUtil.checkEq(4, s.subarraySum(new int[]{1, -1, 1, -1}, 0), "多个抵消段")) failures++; } catch (Throwable t) { failures++; System.out.println("多个抵消段 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.subarraySum(new int[]{1000, 1000, 1000}, 3000), "大数全段")) failures++; } catch (Throwable t) { failures++; System.out.println("大数全段 异常: " + t); }
        try { if (!TestUtil.checkEq(0, s.subarraySum(new int[]{1, 2, 3}, 7), "无匹配")) failures++; } catch (Throwable t) { failures++; System.out.println("无匹配 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
