// ============================================================
// LeetCode 190. 颠倒二进制位 (Reverse Bits)
// 难度：Easy | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/reverse-bits/
// 刷题日期：2026-08-27
//
// 思路：逐位颠倒——循环 32 次，每次取 n 最低位 (n&1) 放入结果第 31-i 位，
//       再 n>>>1 无符号右移（Java 必须用 >>> 避免符号位扩散）
// 复杂度：时间 O(32)=O(1) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0190_ReverseBits {

    // ==== 提交代码开始 ====
    public int reverseBits(int n) {
        int rev = 0;
        for (int i = 0; i < 32; i++) {
            rev |= (n & 1) << (31 - i);
            n >>>= 1;
        }
        return rev;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0190_ReverseBits s = new LC0190_ReverseBits();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(964176192, s.reverseBits(43261596), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1073741822, s.reverseBits(2147483644), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：0 <= n <= 2^31-2，n 为偶数；题面写"有符号"但实际输入为无符号范围，Java int 按位处理
        // 边界1: 0 → 0
        try {
            if (!TestUtil.checkEq(0, s.reverseBits(0), "边界1: 零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 2（最低位1，n 偶数）→ 2^30（位31为1，int 值为 0x40000000）
        try {
            if (!TestUtil.checkEq(0x40000000, s.reverseBits(2), "边界2: 最低位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 全 1 前半段（n=0x7FFFFFFC 上限内，末尾两位0为偶数）→ 反转 = 0x3FFFFFFE
        try {
            if (!TestUtil.checkEq(0x3FFFFFFE, s.reverseBits(0x7FFFFFFC), "边界3: 上限附近")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 交替位 0x55555554（偶数）→ 0x2AAAAAAA
        try {
            if (!TestUtil.checkEq(0x2AAAAAAA, s.reverseBits(0x55555554), "边界4: 交替位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 低16位全1（0x0000FFFE，末尾0为偶数）→ 高16位全1（0x7FFF0000）
        try {
            if (!TestUtil.checkEq(0x7FFF0000, s.reverseBits(0x0000FFFE), "边界5: 低半全1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 0x00000002 与 0x40000000 往返（reverse 两次还原）
        try {
            if (!TestUtil.checkEq(2, s.reverseBits(s.reverseBits(2)), "边界6: 两次还原")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 最大完全平方位模式（0x80000000 越界，用 0x40000000 最高位1）→ 位1 → 反转结果最低位1 = 1？不，反转 0x40000000 的最低位是1在bit30 → 反转后 bit1=1 → 2
        try {
            if (!TestUtil.checkEq(2, s.reverseBits(0x40000000), "边界7: 高位1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}