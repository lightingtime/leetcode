// ============================================================
// LeetCode 238. 除了自身以外数组的乘积 (Product of Array Except Self)
// 难度：Medium | 分类：数组与矩阵
// 链接：https://leetcode.cn/problems/product-of-array-except-self/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-06 · 一刷一次 Accepted）
// 一刷写法：答案数组当前缀积容器 + 滚动后缀变量——第一遍从左往右把「左侧所有元素的积」写进 ans[i]；第二遍从右往左用滚动变量累乘右侧积，与 ans[i] 相乘即得答案。O(n)/O(1)（不把输出数组算作额外空间）
// 本题易错点：题目禁用除法，所以不能用「总乘积 ÷ 自身」（有 0 就崩）；第二遍必须从右往左、滚动变量要在相乘之后再更新；边界的核心是 0 的个数（一个 0 / 多个 0）
// 测试用例与一刷归档保持一致（示例 2 个 + 边界 8 个：空输入/单元素/最短长度2/全相同/单个0/多个0/负数/大数）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0238_ProductOfArrayExceptSelf {

    // ==== 提交代码开始 ====
    public int[] productExceptSelf(int[] nums) {
        if (nums.length < 1) return nums;
        int [] leftMul = new int[nums.length];
        leftMul[0] = 1;
        for (int i = 1; i < leftMul.length; i++) {
            leftMul[i] = leftMul[i - 1] * nums[i - 1];
        }
        int right = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            leftMul[i] = leftMul[i] * right;
            right *= nums[i];
        }
        return leftMul;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0238_ProductOfArrayExceptSelf s = new LC0238_ProductOfArrayExceptSelf();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[]{24, 12, 8, 6}, s.productExceptSelf(new int[]{1, 2, 3, 4}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{0, 0, 9, 0, 0}, s.productExceptSelf(new int[]{-1, 1, 0, -3, 3}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(new int[0], s.productExceptSelf(new int[0]), "空输入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("空输入 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1}, s.productExceptSelf(new int[]{7}), "单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单元素 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{3, 2}, s.productExceptSelf(new int[]{2, 3}), "最短长度2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("最短长度2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 1, 1, 1}, s.productExceptSelf(new int[]{1, 1, 1, 1}), "全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("全相同 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{6, 0, 0, 0}, s.productExceptSelf(new int[]{0, 1, 2, 3}), "单个0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单个0 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{0, 0, 0, 0}, s.productExceptSelf(new int[]{1, 0, 2, 0}), "多个0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("多个0 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{-24, -12, -8, -6}, s.productExceptSelf(new int[]{-1, -2, -3, -4}), "负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("负数 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{99999, 99999}, s.productExceptSelf(new int[]{99999, 99999}), "大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
