// ============================================================
// LeetCode 171. Excel 表列序号 (Excel Sheet Column Number)
// 难度：Easy | 分类：字符串
// 链接：https://leetcode.cn/problems/excel-sheet-column-number/
// 刷题日期：2026-08-23
//
// ============================================================

import java.util.*;

public class LC0171_ExcelSheetColumnNumber {

    // ==== 提交代码开始 ====
    public int titleToNumber(String columnTitle) {
        int ans = 0;
        int mul = 1;
        for (int i = columnTitle.length() - 1; i >= 0; i--) {
            int k = columnTitle.charAt(i) - 'A' + 1;
            ans += k * mul;
            mul *= 26;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0171_ExcelSheetColumnNumber s = new LC0171_ExcelSheetColumnNumber();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(1, s.titleToNumber("A"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(28, s.titleToNumber("AB"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(701, s.titleToNumber("ZY"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 单字符最小与最大：A=1, Z=26
        try {
            if (!TestUtil.checkEq(1, s.titleToNumber("A"), "边界1a-A=1")) failures++;
            if (!TestUtil.checkEq(26, s.titleToNumber("Z"), "边界1b-Z=26")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单字符 异常: " + t); }
        // 2) 进位边界：Z=26 -> AA=27（下一位进位）
        try {
            if (!TestUtil.checkEq(27, s.titleToNumber("AA"), "边界2-AA=27")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-AA=27 异常: " + t); }
        // 3) 双字符中间值：AZ=52, BA=53, ZZ=702
        try {
            if (!TestUtil.checkEq(52, s.titleToNumber("AZ"), "边界3a-AZ=52")) failures++;
            if (!TestUtil.checkEq(53, s.titleToNumber("BA"), "边界3b-BA=53")) failures++;
            if (!TestUtil.checkEq(702, s.titleToNumber("ZZ"), "边界3c-ZZ=702")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-双字符 异常: " + t); }
        // 4) 三字符：AAA=703（26^2+26+1），ABC=731
        try {
            if (!TestUtil.checkEq(703, s.titleToNumber("AAA"), "边界4a-AAA=703")) failures++;
            if (!TestUtil.checkEq(731, s.titleToNumber("ABC"), "边界4b-ABC=731")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-三字符 异常: " + t); }
        // 5) 题目范围上限 FXSHRXW = 2^31-1（int 最大值），验证无溢出
        try {
            if (!TestUtil.checkEq(Integer.MAX_VALUE, s.titleToNumber("FXSHRXW"), "边界5-FXSHRXW上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-FXSHRXW上限 异常: " + t); }
        // 6) 接近上限的长串：FXSHRXV = FXSHRXW - 1
        try {
            if (!TestUtil.checkEq(Integer.MAX_VALUE - 1, s.titleToNumber("FXSHRXV"), "边界6-上限前一位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-上限前一位 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
