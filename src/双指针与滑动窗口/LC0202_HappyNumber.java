// ============================================================
// LeetCode 202. 快乐数 (Happy Number)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/happy-number/
// 刷题日期：2026-08-22
//
// 思路：Floyd 快慢指针判环 —— 快乐数过程要么到 1 要么进循环；slow 走一步、fast 走两步，若 fast 到 1 则快乐，若相遇则说明已入环。
// 复杂度：时间 O(logn * 循环长度)，空间 O(1)
// ============================================================


public class LC0202_HappyNumber {

    // ==== 提交代码开始 ====
    public boolean isHappy(int n) {
        int slow = n;
        int fast = getNext(n);
        while (fast != 1 && slow != fast) {
            slow = getNext(slow);
            fast = getNext(getNext(fast));
        }
        return fast == 1;
    }

    private int getNext(int n) {
        int sum = 0;
        while (n != 0) {
            int x = n % 10;
            sum += (x * x);
            n /= 10;
        }
        return sum;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0202_HappyNumber s = new LC0202_HappyNumber();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.isHappy(19), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.isHappy(2), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 最小 n=1（本身即快乐）
        try { if (!TestUtil.checkEq(true, s.isHappy(1), "边界1-最小1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-最小1 异常: " + t); }
        // 2) 经典快乐数 7
        try { if (!TestUtil.checkEq(true, s.isHappy(7), "边界2-快乐7")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-快乐7 异常: " + t); }
        // 3) 直接到 1 的数（10 -> 1）
        try { if (!TestUtil.checkEq(true, s.isHappy(10), "边界3-直接到1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3-直接到1 异常: " + t); }
        // 4) 三位数快乐数 100
        try { if (!TestUtil.checkEq(true, s.isHappy(100), "边界4-三位100")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-三位100 异常: " + t); }
        // 5) 循环起点 4（经典不快乐环 4->16->37->58->89->145->42->20->4）
        try { if (!TestUtil.checkEq(false, s.isHappy(4), "边界5-循环起点4")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5-循环起点4 异常: " + t); }
        // 6) 循环元素 20
        try { if (!TestUtil.checkEq(false, s.isHappy(20), "边界6-循环元素20")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6-循环元素20 异常: " + t); }
        // 7) 循环元素 145
        try { if (!TestUtil.checkEq(false, s.isHappy(145), "边界7-循环元素145")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7-循环元素145 异常: " + t); }
        // 8) 小数字非快乐 3
        try { if (!TestUtil.checkEq(false, s.isHappy(3), "边界8-非快乐3")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8-非快乐3 异常: " + t); }
        // 9) 上限 n=2^31-1（2147483647）
        try { if (!TestUtil.checkEq(false, s.isHappy(2147483647), "边界9-上限非快乐")) failures++; } catch (Throwable t) { failures++; System.out.println("边界9-上限非快乐 异常: " + t); }
        // 10) 大数为快乐数（10^9）
        try { if (!TestUtil.checkEq(true, s.isHappy(1000000000), "边界10-大数快乐")) failures++; } catch (Throwable t) { failures++; System.out.println("边界10-大数快乐 异常: " + t); }
        // 11) 上限附近非快乐
        try { if (!TestUtil.checkEq(false, s.isHappy(2147483646), "边界11-上限附近非快乐")) failures++; } catch (Throwable t) { failures++; System.out.println("边界11-上限附近非快乐 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}