// ============================================================
// LeetCode 217. 存在重复元素 (Contains Duplicate)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/contains-duplicate/
// 刷题日期：2026-08-21
//
// ============================================================

import java.util.*;

public class LC0217_ContainsDuplicate {

    // ==== 提交代码开始 ====
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0217_ContainsDuplicate s = new LC0217_ContainsDuplicate();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        if (!TestUtil.checkEq(true, s.containsDuplicate(new int[]{1, 2, 3, 1}), "示例1: [1,2,3,1]")) failures++;
        if (!TestUtil.checkEq(false, s.containsDuplicate(new int[]{1, 2, 3, 4}), "示例2: [1,2,3,4]")) failures++;
        if (!TestUtil.checkEq(true, s.containsDuplicate(new int[]{1, 1, 1, 3, 3, 4, 3, 2, 4, 2}), "示例3: 长重复样例")) failures++;

        // ---- 边界测试（自己补充）----
        // 边界1: 单元素（n 下限）必然无重复
        if (!TestUtil.checkEq(false, s.containsDuplicate(new int[]{7}), "边界1: 单元素")) failures++;

        // 边界2: 两个相同元素（最小重复规模）
        if (!TestUtil.checkEq(true, s.containsDuplicate(new int[]{1, 1}), "边界2: 两个相同")) failures++;

        // 边界3: 两个不同元素
        if (!TestUtil.checkEq(false, s.containsDuplicate(new int[]{-5, 5}), "边界3: 两个不同")) failures++;

        // 边界4: 重复元素取约束端点值（-10^9 与 10^9），且重复对夹在中间
        if (!TestUtil.checkEq(true, s.containsDuplicate(new int[]{1_000_000_000, -1_000_000_000, 0, 1_000_000_000}), "边界4: 端点值重复")) failures++;

        // 边界5: 负数重复
        if (!TestUtil.checkEq(true, s.containsDuplicate(new int[]{-1, -2, -1}), "边界5: 负数重复")) failures++;

        // 边界6: 全相同（n 上限 10^5，全部相同）
        int[] allSame = new int[100_000];
        java.util.Arrays.fill(allSame, 42);
        if (!TestUtil.checkEq(true, s.containsDuplicate(allSame), "边界6: 10^5 全相同")) failures++;

        // 边界7: n 上限且全不同（0..99999，均在取值范围内）
        int[] allDistinct = new int[100_000];
        for (int i = 0; i < allDistinct.length; i++) allDistinct[i] = i;
        if (!TestUtil.checkEq(false, s.containsDuplicate(allDistinct), "边界7: 10^5 全不同")) failures++;

        // 边界8: 重复只出现在末尾，其余全不同
        int[] dupAtEnd = new int[100_000];
        for (int i = 0; i < dupAtEnd.length; i++) dupAtEnd[i] = i;
        dupAtEnd[dupAtEnd.length - 1] = 42; // 42 已在前面出现过
        if (!TestUtil.checkEq(true, s.containsDuplicate(dupAtEnd), "边界8: 重复在末尾")) failures++;

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}