// ============================================================
// LeetCode 13. 罗马数字转整数 (Roman to Integer)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/roman-to-integer/
// 刷题日期：2026-08-20
//
// 思路：哈希表映射符号值；当前符号比右边小则减，否则加（处理 6 种减法特例）。
// 复杂度：时间 O(n)，空间 O(1)（7 个固定映射）
// ============================================================

import java.util.*;

public class LC0013_RomanToInteger {
    // ==== 提交代码开始 ====
    public int romanToInt(String s) {
        Map<Character, Integer> symbolValues = new HashMap<>();
        symbolValues.put('I', 1);
        symbolValues.put('V', 5);
        symbolValues.put('X', 10);
        symbolValues.put('L', 50);
        symbolValues.put('C', 100);
        symbolValues.put('D', 500);
        symbolValues.put('M', 1000);
        int n = s.length();
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (i < n - 1 && symbolValues.get(s.charAt(i)) < symbolValues.get(s.charAt(i + 1))) {
                ans -= symbolValues.get(s.charAt(i));
            } else {
                ans += symbolValues.get(s.charAt(i));
            }
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0013_RomanToInteger s = new LC0013_RomanToInteger();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.romanToInt("III"), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.romanToInt("IV"), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(9, s.romanToInt("IX"), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(58, s.romanToInt("LVIII"), "示例4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1994, s.romanToInt("MCMXCIV"), "示例5")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例5 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 单字符：七种基础符号
        try {
            if (!TestUtil.checkEq(1, s.romanToInt("I"), "边界-I")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-I 异常: " + t); }
        try {
            if (!TestUtil.checkEq(5, s.romanToInt("V"), "边界-V")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-V 异常: " + t); }
        try {
            if (!TestUtil.checkEq(10, s.romanToInt("X"), "边界-X")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-X 异常: " + t); }
        try {
            if (!TestUtil.checkEq(50, s.romanToInt("L"), "边界-L")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-L 异常: " + t); }
        try {
            if (!TestUtil.checkEq(100, s.romanToInt("C"), "边界-C")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-C 异常: " + t); }
        try {
            if (!TestUtil.checkEq(500, s.romanToInt("D"), "边界-D")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-D 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1000, s.romanToInt("M"), "边界-M")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-M 异常: " + t); }
        // 六种减法特例
        try {
            if (!TestUtil.checkEq(4, s.romanToInt("IV"), "边界-IV")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-IV 异常: " + t); }
        try {
            if (!TestUtil.checkEq(9, s.romanToInt("IX"), "边界-IX")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-IX 异常: " + t); }
        try {
            if (!TestUtil.checkEq(40, s.romanToInt("XL"), "边界-XL")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-XL 异常: " + t); }
        try {
            if (!TestUtil.checkEq(90, s.romanToInt("XC"), "边界-XC")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-XC 异常: " + t); }
        try {
            if (!TestUtil.checkEq(400, s.romanToInt("CD"), "边界-CD")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-CD 异常: " + t); }
        try {
            if (!TestUtil.checkEq(900, s.romanToInt("CM"), "边界-CM")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-CM 异常: " + t); }
        // 范围端点：最小 1、最大 3999
        try {
            if (!TestUtil.checkEq(1, s.romanToInt("I"), "边界-最小")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-最小 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3999, s.romanToInt("MMMCMXCIX"), "边界-最大")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-最大 异常: " + t); }
        // 连续相同符号
        try {
            if (!TestUtil.checkEq(20, s.romanToInt("XX"), "边界-连续XX")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-连续XX 异常: " + t); }
        try {
            if (!TestUtil.checkEq(300, s.romanToInt("CCC"), "边界-连续CCC")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-连续CCC 异常: " + t); }
        // 相邻下降（先大后小）：纯加法
        try {
            if (!TestUtil.checkEq(6, s.romanToInt("VI"), "边界-VI")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-VI 异常: " + t); }
        try {
            if (!TestUtil.checkEq(11, s.romanToInt("XI"), "边界-XI")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-XI 异常: " + t); }
        // 多种减法混合
        try {
            if (!TestUtil.checkEq(666, s.romanToInt("DCLXVI"), "边界-DCLXVI")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-DCLXVI 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}