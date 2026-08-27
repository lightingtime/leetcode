// ============================================================
// LeetCode 179. 最大数 (Largest Number)
// 难度：Medium | 分类：贪心与区间
// 链接：https://leetcode.cn/problems/largest-number/
// 刷题日期：2026-08-27
//
// 思路：自定义比较器——按拼接结果排序，(b+a).compareTo(a+b) 使拼接大的排前面；
//       全零输入需去重为单个 "0"
// 复杂度：时间 O(n·k log n)（k 为拼接串长，数字 ≤10 位）空间 O(n)
// ============================================================

import java.util.*;

public class LC0179_LargestNumber {

    // ==== 提交代码开始 ====
    public String largestNumber(int[] nums) {
        String[] array = Arrays.stream(nums).mapToObj(String::valueOf).toArray(String[]::new);
        Arrays.sort(array, (a, b) -> (b + a).compareTo(a + b));
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            if (s.equals("0") && sb.toString().equals("0")) {
                continue;
            }
            sb.append(s);
        }
        return sb.toString();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0179_LargestNumber s = new LC0179_LargestNumber();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("210", s.largestNumber(new int[]{10, 2}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq("9534330", s.largestNumber(new int[]{3, 30, 34, 5, 9}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= nums.length <= 100，0 <= nums[i] <= 10^9；返回字符串而非整数
        // 边界1: 单元素
        try {
            if (!TestUtil.checkEq("10", s.largestNumber(new int[]{10}), "边界1: 单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 全零 → 只返回一个 "0" 而不是 "000..."
        try {
            if (!TestUtil.checkEq("0", s.largestNumber(new int[]{0, 0, 0}), "边界2: 全零去重")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 前缀相同长度不同（30 vs 3，3 应在 30 前）
        try {
            if (!TestUtil.checkEq("330", s.largestNumber(new int[]{30, 3}), "边界3: 30 vs 3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 经典陷阱 121 vs 12（比较需拼接而非逐位）
        try {
            if (!TestUtil.checkEq("12121", s.largestNumber(new int[]{121, 12}), "边界4: 121 vs 12")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 重复相等元素（[9,9] → "99"）
        try {
            if (!TestUtil.checkEq("99", s.largestNumber(new int[]{9, 9}), "边界5: 相等重复")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 100 个元素上限（全 9，结果 100 个 9）
        try {
            int[] big = new int[100];
            java.util.Arrays.fill(big, 9);
            String r = s.largestNumber(big);
            boolean ok = r.length() == 100;
            if (ok) for (char c : r.toCharArray()) if (c != '9') ok = false;
            if (!TestUtil.checkEq(true, ok, "边界6: 100上限")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 大数值（10^9 与相邻数），含首位 0 但不全零
        try {
            if (!TestUtil.checkEq("991000000000", s.largestNumber(new int[]{99, 1000000000}), "边界7: 大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        // 边界8: 含 0 的混合（[0,9,8] → "980"）
        try {
            if (!TestUtil.checkEq("980", s.largestNumber(new int[]{0, 9, 8}), "边界8: 含零混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}