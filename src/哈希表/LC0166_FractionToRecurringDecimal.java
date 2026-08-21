// ============================================================
// LeetCode 166. 分数到小数 (Fraction to Recurring Decimal)
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/fraction-to-recurring-decimal/
// 刷题日期：2026-08-21
//
// ============================================================

import java.util.*;

public class LC0166_FractionToRecurringDecimal {

    // ==== 提交代码开始 ====
    public String fractionToDecimal(int numerator, int denominator) {
        long a = numerator, b = denominator;
        if (a % b == 0) {
            return String.valueOf(a / b);
        }
        StringBuilder sb = new StringBuilder();
        if (a * b < 0) {
            sb.append("-");
        }
        a = Math.abs(a);
        b = Math.abs(b);
        sb.append(a / b).append(".");
        a %= b;
        Map<Long, Integer> map = new HashMap<>();
        while (a != 0) {
            map.put(a, sb.length());
            a *= 10;
            sb.append(a / b);
            a %= b;
            if (map.containsKey(a)) {
                int u = map.get(a);
                return String.format("%s(%s)", sb.substring(0, u), sb.substring(u));
            }
        }
        return sb.toString();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0166_FractionToRecurringDecimal s = new LC0166_FractionToRecurringDecimal();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("0.5", s.fractionToDecimal(1, 2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("2", s.fractionToDecimal(2, 1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq("0.(012)", s.fractionToDecimal(4, 333), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: 整数结果（分子整除分母）
        try { if (!TestUtil.checkEq("1", s.fractionToDecimal(1, 1), "边界1: 1/1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: 分子为 0
        try { if (!TestUtil.checkEq("0", s.fractionToDecimal(0, 5), "边界2: 0/5")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }

        // 边界3: 负数符号只出现一次（分子或分母为负）
        try { if (!TestUtil.checkEq("-0.5", s.fractionToDecimal(1, -2), "边界3: 1/-2")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try { if (!TestUtil.checkEq("-0.5", s.fractionToDecimal(-1, 2), "边界3b: -1/2")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3b 异常: " + t); }

        // 边界4: 两个负数相除得正
        try { if (!TestUtil.checkEq("0.5", s.fractionToDecimal(-1, -2), "边界4: -1/-2")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: 有限小数（分母只含 2/5 因子）
        try { if (!TestUtil.checkEq("0.125", s.fractionToDecimal(1, 8), "边界5: 1/8")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try { if (!TestUtil.checkEq("0.0625", s.fractionToDecimal(1, 16), "边界5b: 1/16")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5b 异常: " + t); }

        // 边界6: 纯循环小数（循环节从第一位开始）
        try { if (!TestUtil.checkEq("0.(3)", s.fractionToDecimal(1, 3), "边界6: 1/3")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try { if (!TestUtil.checkEq("0.(142857)", s.fractionToDecimal(1, 7), "边界6b: 1/7")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6b 异常: " + t); }

        // 边界7: 混循环小数（循环节不从第一位开始）
        try { if (!TestUtil.checkEq("0.1(6)", s.fractionToDecimal(1, 6), "边界7: 1/6")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try { if (!TestUtil.checkEq("0.08(3)", s.fractionToDecimal(1, 12), "边界7b: 1/12")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7b 异常: " + t); }

        // 边界8: 整数部分非 0 的循环小数
        try { if (!TestUtil.checkEq("3.(142857)", s.fractionToDecimal(22, 7), "边界8: 22/7")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        // 边界9: int 溢出经典坑 -2^31 / -1 = 2^31，必须用 long
        try { if (!TestUtil.checkEq("2147483648", s.fractionToDecimal(-2147483648, -1), "边界9: MIN/-1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        // 边界10: 分母是 int 最小值（-2^31），自身相除为 1
        try { if (!TestUtil.checkEq("1", s.fractionToDecimal(-2147483648, -2147483648), "边界10: MIN/MIN")) failures++; } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }

        // 边界11: 负数循环小数
        try { if (!TestUtil.checkEq("-0.(012)", s.fractionToDecimal(-4, 333), "边界11: -4/333")) failures++; } catch (Throwable t) { failures++; System.out.println("边界11 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}