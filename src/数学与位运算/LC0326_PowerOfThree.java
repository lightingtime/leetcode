// ============================================================
// LeetCode 326. 3 的幂 (Power of Three)
// 难度：Easy | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/power-of-three/
// 刷题日期：2026-08-27
//
// 思路：数学法——int 内最大 3 的幂是 3^19=1162261467（3^20 溢出），
//       任意 3^k(k≤19) 都能整除它；n>0 且 max3%n==0 ⟺ n 是 3 的幂
// 复杂度：时间 O(1) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0326_PowerOfThree {

    // ==== 提交代码开始 ====
    public boolean isPowerOfThree(int n) {
        if (n <= 0) {
            return false;
        }
        int max3 = 1162261467;
        return max3 % n == 0;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0326_PowerOfThree s = new LC0326_PowerOfThree();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.isPowerOfThree(27), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isPowerOfThree(0), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.isPowerOfThree(9), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isPowerOfThree(45), "示例4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例4 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：-2^31 <= n <= 2^31-1；判断 n 是否为 3 的幂次方（n == 3^x，x 为整数）
        // 边界1: n=1 → true（3^0=1）
        try {
            if (!TestUtil.checkEq(true, s.isPowerOfThree(1), "边界1: 3的0次")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: n=3 → true
        try {
            if (!TestUtil.checkEq(true, s.isPowerOfThree(3), "边界2: 3的一次")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: n=-3 → false（幂次方要求非负，3^x 恒正）
        try {
            if (!TestUtil.checkEq(false, s.isPowerOfThree(-3), "边界3: 负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 上限内最大 3 的幂 3^19 = 1162261467 → true
        try {
            if (!TestUtil.checkEq(true, s.isPowerOfThree(1162261467), "边界4: 3^19上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 3^19 * 2 = 2324522934 溢出，用 3^19 内最大非幂（1162261466）→ false
        try {
            if (!TestUtil.checkEq(false, s.isPowerOfThree(1162261466), "边界5: 上限内非幂")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: n=2 → false
        try {
            if (!TestUtil.checkEq(false, s.isPowerOfThree(2), "边界6: 二")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: n=81 → true（3^4）
        try {
            if (!TestUtil.checkEq(true, s.isPowerOfThree(81), "边界7: 3^4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        // 边界8: n=Integer.MIN_VALUE → false
        try {
            if (!TestUtil.checkEq(false, s.isPowerOfThree(Integer.MIN_VALUE), "边界8: 最小负值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}