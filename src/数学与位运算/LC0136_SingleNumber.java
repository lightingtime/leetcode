// ============================================================
// LeetCode 136. 只出现一次的数字 (Single Number)
// 难度：Easy | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/single-number/
// 刷题日期：2026-08-11
// ============================================================

import java.util.*;

public class LC0136_SingleNumber {

    // ==== 提交代码开始 ====
    public int singleNumber(int[] nums) {
        int ans = 0;
        for (int num : nums) {
            ans ^= num;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0136_SingleNumber s = new LC0136_SingleNumber();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try { if (!TestUtil.checkEq(1, s.singleNumber(new int[]{2,2,1}), "示例1")) failures++; } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try { if (!TestUtil.checkEq(4, s.singleNumber(new int[]{4,1,2,1,2}), "示例2")) failures++; } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.singleNumber(new int[]{1}), "示例3单元素")) failures++; } catch (Throwable t) { failures++; System.out.println("示例3单元素 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq(-7, s.singleNumber(new int[]{-7}), "单元素负数")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素负数 异常: " + t); }
        try { if (!TestUtil.checkEq(1, s.singleNumber(new int[]{1,2,2}), "目标在开头")) failures++; } catch (Throwable t) { failures++; System.out.println("目标在开头 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.singleNumber(new int[]{1,2,1,3,2}), "重复对分散")) failures++; } catch (Throwable t) { failures++; System.out.println("重复对分散 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.singleNumber(new int[]{-1,-1,3}), "负数成对")) failures++; } catch (Throwable t) { failures++; System.out.println("负数成对 异常: " + t); }
        try { if (!TestUtil.checkEq(-2, s.singleNumber(new int[]{3,3,-2}), "负数目标")) failures++; } catch (Throwable t) { failures++; System.out.println("负数目标 异常: " + t); }
        try { if (!TestUtil.checkEq(0, s.singleNumber(new int[]{0,1,1}), "包含0")) failures++; } catch (Throwable t) { failures++; System.out.println("包含0 异常: " + t); }
        try { if (!TestUtil.checkEq(30000, s.singleNumber(new int[]{-30000,-30000,30000}), "大数边界")) failures++; } catch (Throwable t) { failures++; System.out.println("大数边界 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
