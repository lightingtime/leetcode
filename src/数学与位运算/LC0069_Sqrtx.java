// ============================================================
// LeetCode 69. x 的平方根  (Sqrt(x))
// 难度：Easy | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/sqrtx/
// 刷题日期：2026-08-27
//
// 思路：二分查找最右的 mid 使 mid*mid <= x——l=-1、r=46341(哨兵)，
//       mid*mid<=x 时右移 l，否则左移 r；r 哨兵保证 mid 最大 46340，int 内不溢出
// 复杂度：时间 O(log x) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0069_Sqrtx {

    // ==== 提交代码开始 ====
    public int mySqrt(int x) {
        int l = -1, r = 46341;
        while (l + 1 < r) {
            int mid = l + (r - l) / 2;
            if (mid * mid <= x) {
                // 左边一定满足
                l = mid;
            } else {
                r = mid;
            }
        }
        // 返回谁，上面的 if 里就更新谁
        return l;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0069_Sqrtx s = new LC0069_Sqrtx();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2, s.mySqrt(4), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.mySqrt(8), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：0 <= x <= 2^31-1；返回整数部分，小数舍去；禁止内置 sqrt/pow
        // 边界1: x=0 → 0
        try {
            if (!TestUtil.checkEq(0, s.mySqrt(0), "边界1: 零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: x=1 → 1
        try {
            if (!TestUtil.checkEq(1, s.mySqrt(1), "边界2: 一")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 完全平方 16 → 4
        try {
            if (!TestUtil.checkEq(4, s.mySqrt(16), "边界3: 完全平方")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 完全平方+1（17 → 4）
        try {
            if (!TestUtil.checkEq(4, s.mySqrt(17), "边界4: 平方+1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 完全平方-1（15 → 3）
        try {
            if (!TestUtil.checkEq(3, s.mySqrt(15), "边界5: 平方-1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 上限 2^31-1，sqrt≈46340.95 → 46340
        try {
            if (!TestUtil.checkEq(46340, s.mySqrt(Integer.MAX_VALUE), "边界6: 上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 46340^2 = 2147395600（上限内最大完全平方）→ 46340
        try {
            if (!TestUtil.checkEq(46340, s.mySqrt(2147395600), "边界7: 上限内最大平方")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        // 边界8: 46341^2 = 2147488281 溢出 int（需防溢出写法验证：46340^2+1 → 46340）
        try {
            if (!TestUtil.checkEq(46340, s.mySqrt(2147395601), "边界8: 溢出边界")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}