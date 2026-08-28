// ============================================================
// LeetCode 50. Pow(x, n) (Pow(x, n))
// 难度：Medium | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/powx-n/
// 刷题日期：2026-08-28
//
// 思路：递归分治快速幂，long 承接 n 避免 MIN 取负溢出，half 只算一次（写法 1）
// 复杂度：时间 O(log n) 空间 O(log n)
// ============================================================


public class LC0050_PowxN {

    // ==== 提交代码开始 ====
    public double myPow(double x, int n) {
        long N = n;
        return helper(x, N);
    }

    private double helper(double x, long n) {
        if (n == 0) {
            return 1.0;
        }
        if (n < 0) {
            return 1 / helper(x, -n);
        }
        boolean odd = (n & 1) == 1;
        double half = helper(x, n / 2);
        double ans = half * half;
        ans = odd ? ans * x : ans;
        return ans;
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0050_PowxN s = new LC0050_PowxN();
        int failures = 0;

        // ---- 示例测试（来自题目，double 用容差比较）----
        try {
            if (!checkDouble(1024.0, s.myPow(2.0, 10), 1e-9, "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkDouble(9.261, s.myPow(2.1, 3), 1e-9, "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkDouble(0.25, s.myPow(2.0, -2), 1e-9, "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题易错点，全部在题目约束内）----
        // n=0：非零底数的 0 次幂 = 1
        try {
            if (!checkDouble(1.0, s.myPow(3.0, 0), 1e-12, "边界1-正底数0次幂")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!checkDouble(1.0, s.myPow(-3.0, 0), 1e-12, "边界2-负底数0次幂")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // n=MIN：取负会溢出的核心坑；2^MIN 下溢为 0
        try {
            if (!checkDouble(0.0, s.myPow(2.0, Integer.MIN_VALUE), 1e-12, "边界3-n=MIN")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!checkDouble(1.0, s.myPow(1.0, Integer.MIN_VALUE), 1e-12, "边界4-1的MIN次幂")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // x=-1：奇偶决定符号（MIN 是偶数）
        try {
            if (!checkDouble(1.0, s.myPow(-1.0, Integer.MIN_VALUE), 1e-12, "边界5--1的MIN次幂(偶)")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!checkDouble(-1.0, s.myPow(-1.0, Integer.MIN_VALUE + 1), 1e-12, "边界6--1的MIN+1次幂(奇)")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // x=0 且 n>0（约束：x=0 时 n 必为正）
        try {
            if (!checkDouble(0.0, s.myPow(0.0, 5), 1e-12, "边界7-0的正次幂")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        // 负数底数 + 负数指数
        try {
            if (!checkDouble(-0.125, s.myPow(-2.0, -3), 1e-12, "边界8-负底负指")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            if (!checkDouble(0.25, s.myPow(-2.0, -2), 1e-12, "边界9-负底负指偶")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }
        // 小数底数
        try {
            if (!checkDouble(0.25, s.myPow(0.5, 2), 1e-12, "边界10-小数底")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }
        try {
            if (!checkDouble(0.001, s.myPow(0.1, 3), 1e-12, "边界11-小数底3次")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界11 异常: " + t); }
        // 结果接近上限 10^4
        try {
            if (!checkDouble(10000.0, s.myPow(10.0, 4), 1e-9, "边界12-结果上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界12 异常: " + t); }
        try {
            if (!checkDouble(100.0, s.myPow(100.0, 1), 1e-12, "边界13-底数上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界13 异常: " + t); }
        // 负底数正指数
        try {
            if (!checkDouble(-8.0, s.myPow(-2.0, 3), 1e-12, "边界14-负底正指")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界14 异常: " + t); }
        // 大指数但结果在范围内（快速幂必须 O(log n)，n 大时不超时且精度可用）
        try {
            if (!checkDouble(59049.0, s.myPow(3.0, 10), 1e-9, "边界15-3的10次")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界15 异常: " + t); }
        try {
            if (!checkDouble(1.0 / 1024.0, s.myPow(0.5, 10), 1e-12, "边界16-0.5的10次")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界16 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 本地 double 容差比较（不参与力扣提交）
    static boolean checkDouble(double expected, double actual, double tol, String label) {
        if (Math.abs(expected - actual) <= tol) { System.out.println(label + " 通过 ✓"); return true; }
        System.out.println(label + " 失败 ✗ 期望=" + expected + " 实际=" + actual);
        return false;
    }

}