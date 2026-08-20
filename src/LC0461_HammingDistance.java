// ============================================================
// LeetCode 461. 汉明距离 (Hamming Distance)
// 难度：Easy | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/hamming-distance/
// 刷题日期：2026-08-20
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0461_HammingDistance {

    // ==== 提交代码开始 ====
    public int hammingDistance(int x, int y) {
        // TODO: 在这里实现你的解法
        int t = x ^ y;
        int ans = 0;
        while (t != 0) {
            if ((t & 1) == 1) {
                ans++;
            }
            t >>>= 1;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0461_HammingDistance s = new LC0461_HammingDistance();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2, s.hammingDistance(1, 4), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.hammingDistance(3, 1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 相同数字：没有位不同
        try {
            if (!TestUtil.checkEq(0, s.hammingDistance(5, 5), "边界-相同数字")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-相同数字 异常: " + t); }
        // 两个 0
        try {
            if (!TestUtil.checkEq(0, s.hammingDistance(0, 0), "边界-两个0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两个0 异常: " + t); }
        // 0 与 1：只有最低位不同
        try {
            if (!TestUtil.checkEq(1, s.hammingDistance(0, 1), "边界-0和1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-0和1 异常: " + t); }
        // 0 与最大正整数：31 个 1
        try {
            if (!TestUtil.checkEq(31, s.hammingDistance(0, Integer.MAX_VALUE), "边界-0和MAX")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-0和MAX 异常: " + t); }
        // 最大正整数与次大值：只有最低位不同（都在题目约束 0..2^31-1 内）
        try {
            if (!TestUtil.checkEq(1, s.hammingDistance(Integer.MAX_VALUE, Integer.MAX_VALUE - 1), "边界-MAX和MAX-1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-MAX和MAX-1 异常: " + t); }
        // 2^30 与 0：单个高位不同
        try {
            if (!TestUtil.checkEq(1, s.hammingDistance(0, 1 << 30), "边界-最高位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-最高位 异常: " + t); }
        // 0 与 2 的幂：单个位不同
        try {
            if (!TestUtil.checkEq(1, s.hammingDistance(0, 1024), "边界-单个位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单个位 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}