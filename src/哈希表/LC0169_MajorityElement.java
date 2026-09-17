// ============================================================
// LeetCode 169. 多数元素 (Majority Element)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/majority-element/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-03 · 一刷一次 AC）
// 一刷/上次复习写法：Boyer-Moore 投票（候选 + 票数互相抵消），O(n)/O(1)
// 要点：候选的富余票数被抵消光（times == 0）时才换候选；多数元素出现次数 > n/2，保证最后留下的候选必是它
// 测试用例与一刷归档保持一致（示例 2 个 + 边界 5 个）
// 上次复习留下的精简项（本次直接写精简版）：变量名 a / times 改成 candidate / count；times == 0 只会在走了 else（减票）之后成立，可并进 else 里
//
// 思路：Boyer-Moore 投票——a 是当前候选，count 是它还没被抵消的富余票；遇到同值加票、异值减票，
//       减到 0 说明候选被完全抵消，用当前元素重新起头；多数元素出现次数 > n/2，抵消到最后剩下的候选必是它
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0169_MajorityElement {

    // ==== 提交代码开始 ====
    public int majorityElement(int[] nums) {
        int a = nums[0];
        int count = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == a) {
                count++;
            } else {
                count--;
            }
            if (count == 0) {
                a = nums[i];
                count = 1;
            }
        }

        return a;
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

        // ---- 边界测试 ----
        try {
            if (!TestUtil.checkEq(7, s.majorityElement(new int[]{7}), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.majorityElement(new int[]{5, 5}), "边界2-两元素全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.majorityElement(new int[]{1, 2, 1, 1}), "边界3-多数在末尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.majorityElement(new int[]{-1, -2, -1, -1}), "边界4-多数是负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1000000000, s.majorityElement(new int[]{1000000000, 1000000000, -1000000000}), "边界5-大数值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
