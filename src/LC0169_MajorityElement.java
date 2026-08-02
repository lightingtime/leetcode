// ============================================================
// LeetCode 169. 多数元素 (Majority Element)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/majority-element/
// 刷题日期：2026-08-03
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0169_MajorityElement {

    // ==== 提交代码开始 ====
    public int majorityElement(int[] nums) {
        // TODO: 在这里实现你的解法

        int cnt = 1;
        int cur = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (cur != nums[i]) {
                cnt--;
                if (cnt <= 0) {
                    cur = nums[i];
                    cnt = 1;
                }
            } else {
                cnt++;
            }
        }
        return cur;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0169_MajorityElement s = new LC0169_MajorityElement();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.majorityElement(new int[]{3, 2, 3}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // TODO: 补充空输入 / 单元素 / 全相同 / 大数等边界
        // 例如： try { if (!TestUtil.checkEq(期望, s.majorityElement(边界输入), "边界1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 若题目允许任意顺序返回（下标对 / 集合），用 TestUtil.checkEqUnordered 代替 TestUtil.checkEq

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}