// ============================================================
// LeetCode 29. 两数相除 (Divide Two Integers)
// 难度：Medium | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/divide-two-integers/
// 刷题日期：2026-08-28
//
// 思路：负数域统一 + 翻倍（二进制拆分）减法模拟，time 与 num 同步翻倍
// 复杂度：时间 O(log^2(x)) 空间 O(1)
// ============================================================


public class LC0029_DivideTwoIntegers {

    // ==== 提交代码开始 ====
    public int divide(int dividend, int divisor) {
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
        if (dividend == Integer.MAX_VALUE && divisor == 1) {
            return Integer.MAX_VALUE;
        }
        boolean negative = (dividend < 0 && divisor > 0) || (dividend > 0 && divisor < 0);
        dividend = dividend < 0 ? dividend : -dividend;
        divisor = divisor < 0 ? divisor : -divisor;
        int ans = 0;
        while (dividend <= divisor && dividend != 0) {
            int num = divisor;
            int time = 1;
            while (num >= (Integer.MIN_VALUE >> 1) && dividend < (num << 1)) {
                num <<= 1;
                time <<= 1;
            }
            ans += time;
            dividend -= num;
        }
        return negative ? -ans : ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0029_DivideTwoIntegers s = new LC0029_DivideTwoIntegers();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.divide(10, 3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-2, s.divide(7, -3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题易错点）----
        // 溢出特例：MIN / -1 = 2^31 超上限 → 返回 MAX
        try {
            if (!TestUtil.checkEq(Integer.MAX_VALUE, s.divide(Integer.MIN_VALUE, -1), "边界1-MIN/-1溢出")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // MIN 取绝对值会溢出，直接返回自身
        try {
            if (!TestUtil.checkEq(Integer.MIN_VALUE, s.divide(Integer.MIN_VALUE, 1), "边界2-MIN/1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // MIN 参与正负除法（负数取绝对值陷阱）
        try {
            if (!TestUtil.checkEq(-1073741824, s.divide(Integer.MIN_VALUE, 2), "边界3-MIN/2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1073741824, s.divide(Integer.MIN_VALUE, -2), "边界4-MIN/-2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 上界附近
        try {
            if (!TestUtil.checkEq(Integer.MAX_VALUE, s.divide(Integer.MAX_VALUE, 1), "边界5-MAX/1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-2147483647, s.divide(Integer.MAX_VALUE, -1), "边界6-MAX/-1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 被除数为 0
        try {
            if (!TestUtil.checkEq(0, s.divide(0, 5), "边界7-0/正")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.divide(0, -5), "边界8-0/负")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        // 相同数 / 互为相反数
        try {
            if (!TestUtil.checkEq(1, s.divide(5, 5), "边界9-同正数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.divide(-5, -5), "边界10-同负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }
        // |被除数| < |除数| → 0（向零截断）
        try {
            if (!TestUtil.checkEq(0, s.divide(1, 2), "边界11-小除大正")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界11 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.divide(-1, 2), "边界12-小除大负被除")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界12 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.divide(3, -5), "边界13-小除大负除")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界13 异常: " + t); }
        // 向零截断（负商去小数）
        try {
            if (!TestUtil.checkEq(-3, s.divide(-7, 2), "边界14-负商截断")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界14 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-3, s.divide(7, -2), "边界15-负除截断")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界15 异常: " + t); }
        // 大数除法（接近端点）
        try {
            if (!TestUtil.checkEq(715827882, s.divide(2147483647, 3), "边界16-MAX/3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界16 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-715827882, s.divide(Integer.MIN_VALUE, 3), "边界17-MIN/3截断")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界17 异常: " + t); }
        try {
            if (!TestUtil.checkEq(715827882, s.divide(Integer.MIN_VALUE, -3), "边界18-MIN/-3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界18 异常: " + t); }
        // 临界不溢出：-(MIN+1) / -1 = MAX
        try {
            if (!TestUtil.checkEq(2147483647, s.divide(-2147483647, -1), "边界19-MIN+1/-1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界19 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}