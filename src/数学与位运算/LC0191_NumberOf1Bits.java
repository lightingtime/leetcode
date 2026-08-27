// ============================================================
// LeetCode 191. 位1的个数 (Number of 1 Bits)
// 难度：Easy | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/number-of-1-bits/
// 刷题日期：2026-08-27
//
// 思路：每次清除最低位的 1（n &= n-1）并计数，循环次数 = 1 的个数而非 32；
//       比逐位 (n&1)+右移 的恒 32 轮更优
// 复杂度：时间 O(k)（k 为 1 的个数，≤32）空间 O(1)
// ============================================================

import java.util.*;

public class LC0191_NumberOf1Bits {

    // ==== 提交代码开始 ====
    public int hammingWeight(int n) {
        int ans = 0;
        while (n != 0) {
            ans++;
            n &= n - 1;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0191_NumberOf1Bits s = new LC0191_NumberOf1Bits();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.hammingWeight(11), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.hammingWeight(128), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(30, s.hammingWeight(2147483645), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= n <= 2^31-1（正整数）；返回二进制中 1 的个数（汉明重量）
        // 边界1: n=1 → 1
        try {
            if (!TestUtil.checkEq(1, s.hammingWeight(1), "边界1: 最小正整数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 2 的幂（16 → 10000 → 1 个 1）
        try {
            if (!TestUtil.checkEq(1, s.hammingWeight(16), "边界2: 2的幂")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 2 的幂 -1（15 → 1111 → 4 个 1）
        try {
            if (!TestUtil.checkEq(4, s.hammingWeight(15), "边界3: 全1段")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 上限 2^31-1（31 个 1）
        try {
            if (!TestUtil.checkEq(31, s.hammingWeight(Integer.MAX_VALUE), "边界4: 上限全1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 0x55555555 交替位（奇数位 1，16 个）
        try {
            if (!TestUtil.checkEq(16, s.hammingWeight(0x55555555), "边界5: 交替位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 中间单 1（0x10000000 → 1）
        try {
            if (!TestUtil.checkEq(1, s.hammingWeight(0x10000000), "边界6: 中位1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 大数 0x40000001（最高位与最低位各 1 → 2 个）
        try {
            if (!TestUtil.checkEq(2, s.hammingWeight(0x40000001), "边界7: 两端1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}