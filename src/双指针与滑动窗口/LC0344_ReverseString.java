// ============================================================
// LeetCode 344. 反转字符串 (Reverse String)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/reverse-string/
// 刷题日期：2026-08-22
//
// 思路：首尾双下标向中间推进，每次交换一对字符；循环到中点（s.length/2）即完成，无需新数组。
// 复杂度：时间 O(n)，空间 O(1)
// ============================================================


public class LC0344_ReverseString {

    // ==== 提交代码开始 ====
    public void reverseString(char[] s) {
        for (int i = 0; i < s.length /2; i++) {
            char c = s[s.length - i - 1];
            s[s.length - i - 1] = s[i];
            s[i] = c;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0344_ReverseString s = new LC0344_ReverseString();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            char[] nums = new char[]{'h', 'e', 'l', 'l', 'o'};
            s.reverseString(nums);
            if (!TestUtil.checkEq(new char[]{'o', 'l', 'l', 'e', 'h'}, nums, "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            char[] nums = new char[]{'H', 'a', 'n', 'n', 'a', 'h'};
            s.reverseString(nums);
            if (!TestUtil.checkEq(new char[]{'h', 'a', 'n', 'n', 'a', 'H'}, nums, "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 单字符
        try {
            char[] a = new char[]{'a'};
            s.reverseString(a);
            if (!TestUtil.checkEq(new char[]{'a'}, a, "边界1-单字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单字符 异常: " + t); }
        // 2) 两字符
        try {
            char[] a = new char[]{'a', 'b'};
            s.reverseString(a);
            if (!TestUtil.checkEq(new char[]{'b', 'a'}, a, "边界2-两字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-两字符 异常: " + t); }
        // 3) 回文串反转后不变
        try {
            char[] a = new char[]{'a', 'b', 'c', 'b', 'a'};
            s.reverseString(a);
            if (!TestUtil.checkEq(new char[]{'a', 'b', 'c', 'b', 'a'}, a, "边界3-回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-回文 异常: " + t); }
        // 4) 全相同字符
        try {
            char[] a = new char[]{'a', 'a', 'a', 'a'};
            s.reverseString(a);
            if (!TestUtil.checkEq(new char[]{'a', 'a', 'a', 'a'}, a, "边界4-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-全相同 异常: " + t); }
        // 5) 空格与标点
        try {
            char[] a = new char[]{' ', 'a', '!'};
            s.reverseString(a);
            if (!TestUtil.checkEq(new char[]{'!', 'a', ' '}, a, "边界5-特殊字符")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-特殊字符 异常: " + t); }
        // 6) 数字字符
        try {
            char[] a = new char[]{'1', '2', '3', '4'};
            s.reverseString(a);
            if (!TestUtil.checkEq(new char[]{'4', '3', '2', '1'}, a, "边界6-数字")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-数字 异常: " + t); }
        // 7) 大小写混合
        try {
            char[] a = new char[]{'A', 'b', 'C'};
            s.reverseString(a);
            if (!TestUtil.checkEq(new char[]{'C', 'b', 'A'}, a, "边界7-大小写")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-大小写 异常: " + t); }
        // 8) 上限长度 10^5：循环模式字符
        try {
            char[] a = new char[100000];
            for (int i = 0; i < a.length; i++) a[i] = (char) ('a' + (i % 26));
            char[] expected = new char[a.length];
            for (int i = 0; i < expected.length; i++) expected[i] = a[a.length - 1 - i];
            s.reverseString(a);
            if (!TestUtil.checkEq(expected, a, "边界8-上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8-上限 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}