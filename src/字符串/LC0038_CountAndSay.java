// ============================================================
// LeetCode 38. 外观数列 (Count and Say)
// 难度：Medium | 分类：字符串
// 链接：https://leetcode.cn/problems/count-and-say/
// 刷题日期：2026-08-24
//
// ============================================================

public class LC0038_CountAndSay {

    // ==== 提交代码开始 ====
    public String countAndSay(int n) {
        String pre = "1";
        if (n == 1) {
            return pre;
        }
        for (int i = 2; i <= n; i++) {
            pre = getNext(pre);
        }
        return pre;
    }

    private String getNext(String pre) {
        int count = 1;
        char c = pre.charAt(0);
        int l = 1;
        StringBuilder sb = new StringBuilder();
        while (l < pre.length()) {
            if (pre.charAt(l) == c) {
                count++;
            } else {
                sb.append(count).append(c);
                c = pre.charAt(l);
                count = 1;
            }
            l++;
        }
        sb.append(count).append(c);
        return sb.toString();
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0038_CountAndSay s = new LC0038_CountAndSay();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq("1", s.countAndSay(1), "示例1: n=1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq("1211", s.countAndSay(4), "示例2: n=4")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        // 约束：1 <= n <= 30
        // 边界1: n=2，最小编码输出（"1" 的 RLE = "11"）
        try {
            if (!TestUtil.checkEq("11", s.countAndSay(2), "边界1: n=2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        // 边界2: n=3，验证 RLE 从 "11" 正确过渡到 "21"
        try {
            if (!TestUtil.checkEq("21", s.countAndSay(3), "边界2: n=3")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }
        // 边界3: n=5，含连续 3 个 '1'（111/22/1），验证组计数能处理 3 位以上
        try {
            if (!TestUtil.checkEq("111221", s.countAndSay(5), "边界3: n=5 连续三个1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }
        // 边界4: n=10，多轮迭代累积后的中等规模结果
        try {
            if (!TestUtil.checkEq("13211311123113112211", s.countAndSay(10), "边界4: n=10")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }
        // 边界5: n=30 上限，验证无溢出/性能问题（长度应为 4462）
        try {
            if (!TestUtil.checkEq(4462, s.countAndSay(30).length(), "边界5: n=30 长度")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}