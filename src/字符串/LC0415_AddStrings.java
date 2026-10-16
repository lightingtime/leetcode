// ============================================================
// LeetCode 415. 字符串相加 (Add Strings)
// 难度：Easy | 分类：字符串
// 链接：https://leetcode.cn/problems/add-strings/
// 刷题日期：2026-10-16
//
// ============================================================

public class LC0415_AddStrings {

    // ==== 提交代码开始 ====
    public String addStrings(String num1, String num2) {
        int next = 0;
        StringBuilder ans = new StringBuilder();
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        while (i >= 0 || j >= 0 || next > 0) {
            int a = i >= 0 ? num1.charAt(i) - '0' : 0;
            int b = j >= 0 ? num2.charAt(j) - '0' : 0;
            int sum = a + b + next;
            ans.append(sum % 10);
            next = sum / 10;
            i--;
            j--;
        }
        ans.reverse();
        return ans.toString();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0415_AddStrings s = new LC0415_AddStrings();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("134", s.addStrings("11", "123"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("533", s.addStrings("456", "77"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq("0", s.addStrings("0", "0"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（长度 1～10000，无前导零）----
        try {
            if (!TestUtil.checkEq("5", s.addStrings("2", "3"), "单字符无进位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单字符无进位 异常: " + t); }
        try {
            if (!TestUtil.checkEq("18", s.addStrings("9", "9"), "单字符产生新位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单字符产生新位 异常: " + t); }
        try {
            if (!TestUtil.checkEq("1000", s.addStrings("999", "1"), "连续进位及末尾新位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("连续进位及末尾新位 异常: " + t); }
        try {
            if (!TestUtil.checkEq("1000", s.addStrings("1", "999"), "交换长短输入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("交换长短输入 异常: " + t); }
        try {
            if (!TestUtil.checkEq("12345", s.addStrings("12345", "0"), "右侧为零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("右侧为零 异常: " + t); }
        try {
            if (!TestUtil.checkEq("98765", s.addStrings("0", "98765"), "左侧为零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("左侧为零 异常: " + t); }
        try {
            if (!TestUtil.checkEq("1100", s.addStrings("1001", "99"), "内部零与局部进位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("内部零与局部进位 异常: " + t); }
        try {
            if (!TestUtil.checkEq("1000", s.addStrings("500", "500"), "同长度进位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("同长度进位 异常: " + t); }
        try {
            if (!TestUtil.checkEq("2147483648", s.addStrings("2147483647", "1"), "超出 int 范围")) failures++;
        } catch (Throwable t) { failures++; System.out.println("超出 int 范围 异常: " + t); }
        try {
            if (!TestUtil.checkEq("9223372036854775808", s.addStrings("9223372036854775807", "1"), "超出 long 范围")) failures++;
        } catch (Throwable t) { failures++; System.out.println("超出 long 范围 异常: " + t); }
        try {
            String actual = s.addStrings("9".repeat(10000), "1");
            if (!TestUtil.checkEq(true, ("1" + "0".repeat(10000)).equals(actual), "长度上限连续进位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("长度上限连续进位 异常: " + t); }
        try {
            String actual = s.addStrings("1" + "0".repeat(9999), "1" + "0".repeat(9999));
            if (!TestUtil.checkEq(true, ("2" + "0".repeat(9999)).equals(actual), "两个长度上限输入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("两个长度上限输入 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}