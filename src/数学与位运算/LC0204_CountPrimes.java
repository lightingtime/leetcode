// ============================================================
// LeetCode 204. 计数质数 (Count Primes)
// 难度：Medium | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/count-primes/
// 刷题日期：2026-08-28
//
// 思路：静态埃氏筛一次筛满上限 5e6 + 前缀和 pre[i]=小于 i 的质数个数，查询 O(1)
// 复杂度：预处理 O(N log log N) 空间 O(N)，单次查询 O(1)
// ============================================================

public class LC0204_CountPrimes {

    // ==== 提交代码开始 ====
    static boolean[] isComposite = new boolean[5000001];
    static int[] pre = new int[5000001];
    static {
        for (int i = 2; i < 5000001; i++) {
            if (isComposite[i]) {
                continue;
            }
            if ((long) i * i < 5000001) {
                for (int j = i * i; j < 5000001; j += i) {
                    isComposite[j] = true;
                }
            }
        }
        for (int i = 2; i < 5000001; i++) {
            pre[i] = pre[i - 1] + (i - 1 >= 2 && !isComposite[i - 1] ? 1 : 0);
        }
    }
    public int countPrimes(int n) {
        return pre[n];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0204_CountPrimes s = new LC0204_CountPrimes();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(4, s.countPrimes(10), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.countPrimes(0), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.countPrimes(1), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题易错点，全部在约束 0<=n<=5*10^6 内）----
        // n<=2 没有质数（最小质数是 2，需严格小于 n）
        try {
            if (!TestUtil.checkEq(0, s.countPrimes(2), "边界1-n=2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.countPrimes(3), "边界2-n=3(含2)")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 质数边界：n 恰好是质数时不含它本身（小于 n）
        try {
            if (!TestUtil.checkEq(3, s.countPrimes(7), "边界3-n=7(质数本身不含)")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.countPrimes(8), "边界4-n=8")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 小值对照
        try {
            if (!TestUtil.checkEq(2, s.countPrimes(5), "边界5-n=5")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(10, s.countPrimes(30), "边界6-n=30")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 已知 π(n) 标准值：π(100)=25、π(1000)=168、π(10000)=1229、π(100000)=9592
        try {
            if (!TestUtil.checkEq(25, s.countPrimes(100), "边界7-n=100")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(168, s.countPrimes(1000), "边界8-n=1000")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1229, s.countPrimes(10000), "边界9-n=10000")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }
        try {
            if (!TestUtil.checkEq(9592, s.countPrimes(100000), "边界10-n=100000")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }
        // 约束上限附近：π(5*10^6)=348513；4999999 是质数，countPrimes 不含其本身 → π(4999998)=348512
        try {
            if (!TestUtil.checkEq(348513, s.countPrimes(5000000), "边界11-n=5000000上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界11 异常: " + t); }
        try {
            if (!TestUtil.checkEq(348512, s.countPrimes(4999999), "边界12-n=4999999")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界12 异常: " + t); }
        // 力扣 TLE 失败用例回归：n=3683568 → 262295
        try {
            if (!TestUtil.checkEq(262295, s.countPrimes(3683568), "边界13-力扣TLE用例")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界13 异常: " + t); }
        // 第二次 TLE 失败用例回归：n=4240833 → 298979
        try {
            if (!TestUtil.checkEq(298979, s.countPrimes(4240833), "边界14-力扣TLE用例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界14 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}