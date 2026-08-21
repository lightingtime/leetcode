# 166. 分数到小数 (Fraction to Recurring Decimal)

> https://leetcode.cn/problems/fraction-to-recurring-decimal/

难度：Medium ｜ 分类：哈希表

标签：哈希表、数学、字符串

## 题目描述

给定两个整数，分别表示分数的分子 numerator 和分母 denominator，以 字符串形式返回小数 。

如果小数部分为循环小数，则将循环的部分括在括号内。

如果存在多个答案，只需返回 任意一个 。

对于所有给定的输入，保证 答案字符串的长度小于 104 。

注意，如果分数可以表示为有限长度的字符串，则 必须 返回它。

示例 1：

输入：numerator = 1, denominator = 2
输出："0.5"

示例 2：

输入：numerator = 2, denominator = 1
输出："2"

示例 3：

输入：numerator = 4, denominator = 333
输出："0.(012)"

提示：

 -231 31 - 1
 denominator != 0

## 示例

1. 输入：numerator = 1，denominator = 2
   输出："0.5"

2. 输入：numerator = 2，denominator = 1
   输出："2"

3. 输入：numerator = 4，denominator = 333
   输出："0.(012)"

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
