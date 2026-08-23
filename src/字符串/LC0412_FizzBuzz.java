// ============================================================
// LeetCode 412. Fizz Buzz (Fizz Buzz)
// 难度：Easy | 分类：字符串
// 链接：https://leetcode.cn/problems/fizz-buzz/
// 刷题日期：2026-08-23
//
// ============================================================

import java.util.*;

public class LC0412_FizzBuzz {

    // ==== 提交代码开始 ====
    public List<String> fizzBuzz(int n) {
        List<String> ans = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) {
                ans.add("FizzBuzz");
            } else if (i % 3 == 0) {
                ans.add("Fizz");
            } else if (i % 5 == 0) {
                ans.add("Buzz");
            } else {
                ans.add(String.valueOf(i));
            }
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0412_FizzBuzz s = new LC0412_FizzBuzz();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList("1", "2", "Fizz"), s.fizzBuzz(3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList("1", "2", "Fizz", "4", "Buzz"), s.fizzBuzz(5), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList("1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz", "Buzz", "11", "Fizz", "13", "14", "FizzBuzz"), s.fizzBuzz(15), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) n=1：最小输入，只有 "1"
        try {
            if (!TestUtil.checkEq(Arrays.asList("1"), s.fizzBuzz(1), "边界1-n=1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-n=1 异常: " + t); }
        // 2) n=2：全部是数字
        try {
            if (!TestUtil.checkEq(Arrays.asList("1", "2"), s.fizzBuzz(2), "边界2-n=2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-n=2 异常: " + t); }
        // 3) 15 的倍数边界：15=FizzBuzz, 30=FizzBuzz（同时是 3 和 5 的倍数）
        try {
            List<String> r = s.fizzBuzz(30);
            boolean ok = r != null && r.size() == 30;
            if (ok) ok &= TestUtil.checkEq("FizzBuzz", r.get(14), "边界3a-15FizzBuzz");
            if (ok) ok &= TestUtil.checkEq("FizzBuzz", r.get(29), "边界3b-30FizzBuzz");
            if (ok) ok &= TestUtil.checkEq("Fizz", r.get(2), "边界3c-3Fizz");
            if (ok) ok &= TestUtil.checkEq("Buzz", r.get(4), "边界3d-5Buzz");
            if (!ok) { failures++; System.out.println("边界3-30序列 失败"); }
        } catch (Throwable t) { failures++; System.out.println("边界3-30序列 异常: " + t); }
        // 4) 15 前后：14=数字, 15=FizzBuzz, 16=数字
        try {
            List<String> r = s.fizzBuzz(16);
            boolean ok = r != null && r.size() == 16;
            if (ok) ok &= TestUtil.checkEq("14", r.get(13), "边界4a-14");
            if (ok) ok &= TestUtil.checkEq("FizzBuzz", r.get(14), "边界4b-15");
            if (ok) ok &= TestUtil.checkEq("16", r.get(15), "边界4c-16");
            if (!ok) { failures++; System.out.println("边界4-15前后 失败"); }
        } catch (Throwable t) { failures++; System.out.println("边界4-15前后 异常: " + t); }
        // 5) 上限 n=10000：长度正确；9999=Fizz(3的倍数非5), 10000=Buzz(5的倍数非3)
        try {
            List<String> r = s.fizzBuzz(10000);
            boolean ok = r != null && r.size() == 10000;
            if (ok) ok &= TestUtil.checkEq("Fizz", r.get(9998), "边界5a-9999Fizz");
            if (ok) ok &= TestUtil.checkEq("Buzz", r.get(9999), "边界5b-10000Buzz");
            if (ok) ok &= TestUtil.checkEq("9997", r.get(9996), "边界5c-9997");
            if (!ok) { failures++; System.out.println("边界5-上限10000 失败"); }
        } catch (Throwable t) { failures++; System.out.println("边界5-上限10000 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
