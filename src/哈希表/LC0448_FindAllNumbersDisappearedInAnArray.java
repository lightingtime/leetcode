// ============================================================
// LeetCode 448. 找到所有数组中消失的数字 (Find All Numbers Disappeared in an Array)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/find-all-numbers-disappeared-in-an-array/
// 刷题日期：2026-08-11
// ============================================================

import java.util.*;

public class LC0448_FindAllNumbersDisappearedInAnArray {

    // ==== 提交代码开始 ====
    public List<Integer> findDisappearedNumbers(int[] nums) {
        int index = 0;
        while (index < nums.length) {
            if (nums[index] != index + 1) {
                if (nums[index] == nums[nums[index] - 1]) {
                    index++;
                    continue;
                }
                int temp = nums[nums[index] - 1];
                nums[nums[index] - 1] = nums[index];
                nums[index] = temp;
            } else {
                index++;
            }
        }
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                list.add(i + 1);
            }
        }
        return list;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0448_FindAllNumbersDisappearedInAnArray s = new LC0448_FindAllNumbersDisappearedInAnArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList(5, 6), s.findDisappearedNumbers(new int[]{4, 3, 2, 7, 8, 2, 3, 1}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(2), s.findDisappearedNumbers(new int[]{1, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq(Arrays.asList(), s.findDisappearedNumbers(new int[]{1}), "单元素无缺失")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素无缺失 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(2, 3), s.findDisappearedNumbers(new int[]{1, 1, 1}), "重复占位多缺失")) failures++; } catch (Throwable t) { failures++; System.out.println("重复占位多缺失 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(), s.findDisappearedNumbers(new int[]{1, 2, 3}), "完全无缺失")) failures++; } catch (Throwable t) { failures++; System.out.println("完全无缺失 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(1, 2, 3, 4), s.findDisappearedNumbers(new int[]{5, 5, 5, 5, 5}), "全部相同大数")) failures++; } catch (Throwable t) { failures++; System.out.println("全部相同大数 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(1), s.findDisappearedNumbers(new int[]{2, 2, 3}), "缺失在开头")) failures++; } catch (Throwable t) { failures++; System.out.println("缺失在开头 异常: " + t); }
        try { if (!TestUtil.checkEq(Arrays.asList(3), s.findDisappearedNumbers(new int[]{1, 2, 2}), "缺失在结尾")) failures++; } catch (Throwable t) { failures++; System.out.println("缺失在结尾 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
