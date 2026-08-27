// ============================================================
// LeetCode 66. 加一 (Plus One)
// 难度：Easy | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/plus-one/
// 刷题日期：2026-08-27
//
// 思路：从最低位向前模拟进位——遇 9 置 0 继续；遇非 9 加 1 直接返回；
//       循环走完说明全 9，扩容为 1 后跟全 0
// 复杂度：时间 O(n) 空间 O(1)（全 9 时才 O(n) 扩容）
// ============================================================

import java.util.*;

public class LC0066_PlusOne {

    // ==== 提交代码开始 ====
    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] == 9) {
                digits[i] = 0;
            } else {
                digits[i] += 1;
                return digits;
            }
        }
        int[] ans = new int[digits.length + 1];
        ans[0] = 1;
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0066_PlusOne s = new LC0066_PlusOne();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[]{1, 2, 4}, s.plusOne(new int[]{1, 2, 3}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{4, 3, 2, 2}, s.plusOne(new int[]{4, 3, 2, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{1, 0}, s.plusOne(new int[]{9}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= digits.length <= 100，0 <= digits[i] <= 9，无前导 0
        // 边界1: 单元素非 9 → +1
        try {
            if (!TestUtil.checkEq(new int[]{5}, s.plusOne(new int[]{4}), "边界1: 单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 全 9 → 进位并扩容（[9,9,9] → [1,0,0,0]）
        try {
            if (!TestUtil.checkEq(new int[]{1, 0, 0, 0}, s.plusOne(new int[]{9, 9, 9}), "边界2: 全9进位扩容")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 末尾 9 但非全 9 → 只进位到中间（[1,9,9] → [2,0,0]）
        try {
            if (!TestUtil.checkEq(new int[]{2, 0, 0}, s.plusOne(new int[]{1, 9, 9}), "边界3: 部分9进位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 末尾非 9 → 只改末位，无进位（[9,9,8] → [9,9,9]）
        try {
            if (!TestUtil.checkEq(new int[]{9, 9, 9}, s.plusOne(new int[]{9, 9, 8}), "边界4: 末位非9")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 100 位上限全 9 → 扩容为 101 位
        try {
            int[] all9 = new int[100];
            java.util.Arrays.fill(all9, 9);
            int[] r = s.plusOne(all9);
            boolean ok = r.length == 101 && r[0] == 1;
            if (ok) for (int i = 1; i < r.length; i++) if (r[i] != 0) ok = false;
            if (!TestUtil.checkEq(true, ok, "边界5: 100上限全9")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 100 位上限，末尾 9 中段进位（99...98 → 99...99）
        try {
            int[] d = new int[100];
            java.util.Arrays.fill(d, 9);
            d[99] = 8;
            int[] r = s.plusOne(d);
            boolean ok = r.length == 100;
            if (ok) for (int i = 0; i < 99; i++) if (r[i] != 9) ok = false;
            if (ok && r[99] != 9) ok = false;
            if (!TestUtil.checkEq(true, ok, "边界6: 100上限末位9")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 首位 1 带若干 9（[1,9] → [2,0]）
        try {
            if (!TestUtil.checkEq(new int[]{2, 0}, s.plusOne(new int[]{1, 9}), "边界7: 首1尾9")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        // 边界8: 含 0 的数字（[1,0,0] → [1,0,1]）
        try {
            if (!TestUtil.checkEq(new int[]{1, 0, 1}, s.plusOne(new int[]{1, 0, 0}), "边界8: 含零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}