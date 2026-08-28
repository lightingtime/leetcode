// ============================================================
// LeetCode 172. 阶乘后的零 (Factorial Trailing Zeroes)
// 难度：Medium | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/factorial-trailing-zeroes/
// 刷题日期：2026-08-28
//
// 思路：尾随零=因子5个数，分层数 5/25/125…的倍数个数之和（进阶 O(log n)）
// 复杂度：时间 O(log_5 n) 空间 O(1)
// ============================================================


public class LC0172_FactorialTrailingZeroes {

    // ==== 提交代码开始 ====
    public int trailingZeroes(int n) {
        int ans = 0;
        int i = 5;
        while (i <= n) {
            ans += n / i;
            i *= 5;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0172_FactorialTrailingZeroes s = new LC0172_FactorialTrailingZeroes();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(0, s.trailingZeroes(3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.trailingZeroes(5), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.trailingZeroes(0), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题易错点，全部在约束 0<=n<=10^4 内）----
        // 25 的倍数：多贡献一个 5（25=5*5）
        try {
            if (!TestUtil.checkEq(6, s.trailingZeroes(25), "边界1-25的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 125 的倍数：多贡献两个 5
        try {
            if (!TestUtil.checkEq(31, s.trailingZeroes(125), "边界2-125的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 50 = 5*5*2，25 的倍数
        try {
            if (!TestUtil.checkEq(12, s.trailingZeroes(50), "边界3-50的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 100 = 25*4
        try {
            if (!TestUtil.checkEq(24, s.trailingZeroes(100), "边界4-100的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 临界：25 前一个（24! 只有 5 的贡献）
        try {
            if (!TestUtil.checkEq(4, s.trailingZeroes(24), "边界5-24的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 越过 25 后（26! 多一个 5）
        try {
            if (!TestUtil.checkEq(6, s.trailingZeroes(26), "边界6-26的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 5 的倍数但非 25 倍数
        try {
            if (!TestUtil.checkEq(1, s.trailingZeroes(5), "边界7-5的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2, s.trailingZeroes(10), "边界8-10的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        // 不足 5：无尾随 0
        try {
            if (!TestUtil.checkEq(0, s.trailingZeroes(1), "边界9-n=1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.trailingZeroes(4), "边界10-n=4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }
        // n=0 与 5 附近
        try {
            if (!TestUtil.checkEq(0, s.trailingZeroes(0), "边界11-n=0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界11 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.trailingZeroes(3), "边界12-n=3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界12 异常: " + t); }
        // 625 的倍数（5^4）
        try {
            if (!TestUtil.checkEq(156, s.trailingZeroes(625), "边界13-625的阶乘")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界13 异常: " + t); }
        // 约束上限附近
        try {
            if (!TestUtil.checkEq(2499, s.trailingZeroes(10000), "边界14-n=10000")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界14 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2495, s.trailingZeroes(9999), "边界15-n=9999")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界15 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}