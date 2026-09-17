// ============================================================
// LeetCode 227. 基本计算器 II (Basic Calculator II)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/basic-calculator-ii/
// 复习日期：2026-09-17（第 3 次复习 · 一刷 2026-08-24）
// 一刷写法：带符号栈——+ / - 把带符号的操作数压栈，* / 与栈顶结算，最后求和（O(n) 空间，写法1）
// 一刷问题：减号被绑到上一项而不是后一个操作数；扫描状态机缺「上一个运算符」状态，数字未及时清零、末尾操作数漏结算
// 上次复习写法：O(1) 空间 sum/last 两变量——* / 合并进 last、+ / - 定稿进 sum（写法2）；上次复习同样不是一次 AC
// 测试用例：与一刷归档保持一致（示例 3 + 边界 7，含 299999 字符上限与连续乘除左结合）
// ============================================================

import java.util.*;

public class LC0227_BasicCalculatorIi {

    // ==== 提交代码开始 ====
    // 参数 s：合法中缀表达式（非负整数、+ - * / 与空格，无括号）；返回其整数值
    public int calculate(String s) {
        int sum = 0, last = 0, num = 0;
        // sign：上一个运算符（初始 '+' 等价于表达式前面垫一个 0），决定 num 是定稿进 last 还是并入乘除
        char sign = '+';
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            }
            // 末尾必须单独触发一次结算，否则最后一个操作数只算进 num、没进 last/sum
            if ((!Character.isDigit(c) && c != ' ') || i == s.length() - 1) {
                switch (sign) {
                    case '+' :
                        sum += last;
                        last = num;
                        break;
                    case '-' :
                        // 减号绑到后面这个操作数上：结算旧的 last 后，把 -num 作为新的 last
                        sum += last;
                        last = -num;
                        break;
                    case '*' :
                        last *= num;
                        break;

                    case '/' :
                        last /= num;
                        break;
                }
                num = 0;
                sign = c;
            }
        }
        return sum + last;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0227_BasicCalculatorIi s = new LC0227_BasicCalculatorIi();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(7, s.calculate("3+2*2"), "示例1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(1, s.calculate(" 3/2 "), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(5, s.calculate(" 3+5 / 2 "), "示例3")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        // 约束：1 <= len <= 3*10^5，+ - * / 与空格，整数非负，无括号，除保留整数部分
        // 边界1: 单数字
        try {
            if (!TestUtil.checkEq(42, s.calculate("42"), "边界1: 单数字")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        // 边界2: 连续乘除左结合（100/10/2 = (100/10)/2 = 5）
        try {
            if (!TestUtil.checkEq(5, s.calculate("100/10/2"), "边界2: 连续除法左结合")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }
        // 边界3: 结果为负（1-2 = -1；含乘除优先级时 1-2*3+3 = -2）
        try {
            if (!TestUtil.checkEq(-1, s.calculate("1-2"), "边界3a: 负数结果")) failures++;
            if (!TestUtil.checkEq(-2, s.calculate("1-2*3+3"), "边界3b: 负数与优先级")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }
        // 边界4: 乘除优先级高于加减（1-2*3 = 1-6 = -5）
        try {
            if (!TestUtil.checkEq(-5, s.calculate("1-2*3"), "边界4: 乘除优先")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }
        // 边界5: 除法截断后参与加减（14-3/2 = 14-1 = 13）
        try {
            if (!TestUtil.checkEq(13, s.calculate("14-3/2"), "边界5: 除截断")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }
        // 边界6: 乘除在开头（2*3+4 = 10）
        try {
            if (!TestUtil.checkEq(10, s.calculate("2*3+4"), "边界6: 乘除开头")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6 异常: " + t);
        }
        // 边界7: 长度上限 3*10^5（149999 个 "1+" + "1" = 299999 字符）
        try {
            String big = "1+".repeat(149999) + "1";
            if (!TestUtil.checkEq(150000, s.calculate(big), "边界7: 3*10^5长度")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界7 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
