// ============================================================
// LeetCode 7. 整数反转 (Reverse Integer)
// 难度：Medium | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/reverse-integer/
// 刷题日期：2026-08-28
//
// 思路：逐位弹出，拼入 ans 前用范围上限/10 提前判断是否溢出
// 复杂度：时间 O(log10(x)) 空间 O(1)
// ============================================================


public class LC0007_ReverseInteger {

    // ==== 提交代码开始 ====
    public int reverse(int x) {
        int ans = 0;
        while (x != 0) {
            if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && x % 10 > Integer.MAX_VALUE % 10)) {
                return 0;
            } else if (ans < Integer.MIN_VALUE / 10 || (ans == Integer.MIN_VALUE / 10 && x % 10 < Integer.MIN_VALUE % 10)) {
                return 0;
            }
            ans = ans * 10 + x % 10;
            x /= 10;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0007_ReverseInteger s = new LC0007_ReverseInteger();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(321, s.reverse(123), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-321, s.reverse(-123), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(21, s.reverse(120), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.reverse(0), "示例4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例4 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(0, s.reverse(1534236469), "边界1-反转后溢出正数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.reverse(Integer.MIN_VALUE), "边界2-最小int反转溢出")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.reverse(Integer.MAX_VALUE), "边界3-最大int反转溢出")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1463847412, s.reverse(2147483641), "边界4-反转后在范围内的最值附近")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.reverse(100), "边界5-末尾0被丢弃")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-21, s.reverse(-120), "边界6-负数末尾0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.reverse(1), "边界7-单数字")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-2147483641, s.reverse(-1463847412), "边界8-负反转到范围内下限附近")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}