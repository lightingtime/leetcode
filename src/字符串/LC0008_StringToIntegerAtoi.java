// ============================================================
// LeetCode 8. 字符串转换整数 (atoi) (String to Integer (atoi))
// 难度：Medium | 分类：字符串
// 链接：https://leetcode.cn/problems/string-to-integer-atoi/
// 复习日期：2026-09-06（第 1 次复习 · 一刷 2026-08-23）
// 一刷思路：long 绝对值累加 + 累加前预判溢出 + 按符号截断（先跳空白，再读可选符号，再累数字）
// 一刷复杂度：时间 O(n)，空间 O(1)
// 测试用例与一刷归档保持一致
// ============================================================

import java.util.*;

public class LC0008_StringToIntegerAtoi {

    // ==== 提交代码开始 ====
    public int myAtoi(String s) {
        int index = 0;
        int ans = 0;
        while (index < s.length() && s.charAt(index) == ' ') {
            index++;
        }
        int sign = 1;
        if (index < s.length() && (s.charAt(index) == '+' || s.charAt(index) == '-')) {
            sign = s.charAt(index) == '+' ? 1 : -1;
            index++;
        }
        while (index < s.length() && s.charAt(index) == '0') {
            index++;
        }
        if (index >= s.length()) {
            return 0;
        }
        s = s.substring(index);
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                break;
            } else {
                if (ans > Integer.MAX_VALUE / 10 || (ans == Integer.MAX_VALUE / 10 && (c - '0') >= Integer.MAX_VALUE % 10)) {
                    return Integer.MAX_VALUE;
                }
                if (ans < (Integer.MIN_VALUE / 10) || (ans == Integer.MIN_VALUE / 10 && (-1) *(c - '0') <= Integer.MIN_VALUE % 10)) {
                    return Integer.MIN_VALUE;
                }
                ans = ans * 10 + sign * (c - '0');
            }
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0008_StringToIntegerAtoi s = new LC0008_StringToIntegerAtoi();
        int failures = 0;

        // ---- 官方示例 ----
        try {
            if (!TestUtil.checkEq(42, s.myAtoi("42"), "示例1-42")) failures++;
            if (!TestUtil.checkEq(-42, s.myAtoi(" -042"), "示例2--042")) failures++;
            if (!TestUtil.checkEq(1337, s.myAtoi("1337c0d3"), "示例3-1337c0d3")) failures++;
            if (!TestUtil.checkEq(0, s.myAtoi("0-1"), "示例4-0-1")) failures++;
            if (!TestUtil.checkEq(0, s.myAtoi("words and 987"), "示例5-words")) failures++;
        } catch (Throwable t) { failures++; System.out.println("官方示例 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 空串 / 纯空格 / 纯符号：无数字 -> 0
        try {
            if (!TestUtil.checkEq(0, s.myAtoi(""), "边界1a-空串")) failures++;
            if (!TestUtil.checkEq(0, s.myAtoi("   "), "边界1b-纯空格")) failures++;
            if (!TestUtil.checkEq(0, s.myAtoi("+"), "边界1c-只有+")) failures++;
            if (!TestUtil.checkEq(0, s.myAtoi("-"), "边界1d-只有-")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-空/空格/符号 异常: " + t); }
        // 2) 符号后跟非数字 / 连续符号
        try {
            if (!TestUtil.checkEq(0, s.myAtoi("+abc"), "边界2a-+abc")) failures++;
            if (!TestUtil.checkEq(0, s.myAtoi("+-12"), "边界2b-+-12")) failures++;
            if (!TestUtil.checkEq(0, s.myAtoi("-+12"), "边界2c--+12")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-符号后非法 异常: " + t); }
        // 3) 正号与前导零
        try {
            if (!TestUtil.checkEq(42, s.myAtoi("+42"), "边界3a-+42")) failures++;
            if (!TestUtil.checkEq(7, s.myAtoi("007"), "边界3b-007")) failures++;
            if (!TestUtil.checkEq(-12, s.myAtoi("  -0012a42"), "边界3c--0012a42")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-正号/前导零 异常: " + t); }
        // 4) 数字后遇非数字停止（点 / 字母 / 符号）
        try {
            if (!TestUtil.checkEq(3, s.myAtoi("3.14159"), "边界4a-3.14159")) failures++;
            if (!TestUtil.checkEq(12, s.myAtoi("12a34"), "边界4b-12a34")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-中途停止 异常: " + t); }
        // 5) 恰好边界值
        try {
            if (!TestUtil.checkEq(Integer.MAX_VALUE, s.myAtoi("2147483647"), "边界5a-最大正")) failures++;
            if (!TestUtil.checkEq(Integer.MIN_VALUE, s.myAtoi("-2147483648"), "边界5b-最小负")) failures++;
            if (!TestUtil.checkEq(2147483646, s.myAtoi("2147483646"), "边界5c-最大正-1")) failures++;
            if (!TestUtil.checkEq(-2147483647, s.myAtoi("-2147483647"), "边界5d-最小负+1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-边界值 异常: " + t); }
        // 6) 溢出截断
        try {
            if (!TestUtil.checkEq(Integer.MAX_VALUE, s.myAtoi("2147483648"), "边界6a-正溢出1")) failures++;
            if (!TestUtil.checkEq(Integer.MIN_VALUE, s.myAtoi("-2147483649"), "边界6b-负溢出1")) failures++;
            if (!TestUtil.checkEq(Integer.MAX_VALUE, s.myAtoi("99999999999999999999"), "边界6c-超大正")) failures++;
            if (!TestUtil.checkEq(Integer.MIN_VALUE, s.myAtoi("-99999999999999999999"), "边界6d-超大负")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-溢出 异常: " + t); }
        // 7) 长度上限 200：200 位数字 -> 溢出截断
        try {
            StringBuilder sb = new StringBuilder("1");
            for (int i = 0; i < 199; i++) sb.append('0');
            if (!TestUtil.checkEq(Integer.MAX_VALUE, s.myAtoi(sb.toString()), "边界7-200位溢出")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-200位溢出 异常: " + t); }

        // 回归: 力扣 WA 用例 -21474836482（负号侧先到 2^31 再续位，int 装不下正 2^31）
        try {
            if (!TestUtil.checkEq(Integer.MIN_VALUE, s.myAtoi("-21474836482"), "回归-负溢出续位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("回归-负溢出续位 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
