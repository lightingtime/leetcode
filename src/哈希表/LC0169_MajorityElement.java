// ============================================================
// LeetCode 169. 多数元素 (Majority Element)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/majority-element/
// 复习日期：2026-09-16（第 1 次复习 · 一刷 2026-08-03）
// 一刷写法：Boyer-Moore 投票 O(n)/O(1)
// 测试用例：示例 2 个（题目自带）+ 边界 5 个（一刷归档缺边界，本次补齐）
// ============================================================


public class LC0169_MajorityElement {

    // ==== 提交代码开始 ====
    public int majorityElement(int[] nums) {
        int a = nums[0];
        int times = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == a) {
                times++;
            } else {
                times--;
            }
            if (times == 0) {
                a = nums[i];
                times = 1;
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