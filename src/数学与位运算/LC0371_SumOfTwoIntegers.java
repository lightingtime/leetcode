// ============================================================
// LeetCode 371. 两整数之和 (Sum of Two Integers)
// 难度：Medium | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/sum-of-two-integers/
// 刷题日期：2026-08-28
//
// 思路：XOR 无进位加 + AND 进位左移，迭代到进位为 0；先算进位再用旧值更新 a
// 复杂度：时间 O(1)（最多 32 轮） 空间 O(1)
// ============================================================


public class LC0371_SumOfTwoIntegers {

    // ==== 提交代码开始 ====
    public int getSum(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;
            a = a ^ b;
            b = carry;
        }
        return a;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0371_SumOfTwoIntegers s = new LC0371_SumOfTwoIntegers();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.getSum(1, 2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.getSum(2, 3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题易错点，全部在约束 -1000<=a,b<=1000 内）----
        // 负数组合
        try {
            if (!TestUtil.checkEq(-3, s.getSum(-1, -2), "边界1-两个负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.getSum(-1, 1), "边界2-相反数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 零参与
        try {
            if (!TestUtil.checkEq(0, s.getSum(0, 0), "边界3-双零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.getSum(0, 5), "边界4-零加正")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-5, s.getSum(-5, 0), "边界5-负加零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 约束端点
        try {
            if (!TestUtil.checkEq(2000, s.getSum(1000, 1000), "边界6-上限相加")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-2000, s.getSum(-1000, -1000), "边界7-下限相加")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.getSum(1000, -1000), "边界8-端点抵消")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        // 进位链：多位连续进位
        try {
            if (!TestUtil.checkEq(16, s.getSum(7, 9), "边界9-进位链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-16, s.getSum(-7, -9), "边界10-负数进位链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }
        // 一正一负（减法语义，靠位运算自然完成）
        try {
            if (!TestUtil.checkEq(2, s.getSum(5, -3), "边界11-正负混合1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界11 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-2, s.getSum(-5, 3), "边界12-正负混合2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界12 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}