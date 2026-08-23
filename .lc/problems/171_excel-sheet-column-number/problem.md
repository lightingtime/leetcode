# 171. Excel 表列序号 (Excel Sheet Column Number)

> https://leetcode.cn/problems/excel-sheet-column-number/

难度：Easy ｜ 分类：字符串

标签：数学、字符串

## 题目描述

给你一个字符串 columnTitle ，表示 Excel 表格中的列名称。返回 该列名称对应的列序号 。

例如：

A -> 1
B -> 2
C -> 3
...
Z -> 26
AA -> 27
AB -> 28 
...

示例 1:

输入: columnTitle = "A"
输出: 1

示例 2:

输入: columnTitle = "AB"
输出: 28

示例 3:

输入: columnTitle = "ZY"
输出: 701

提示：

 1 
 columnTitle 仅由大写英文组成
 columnTitle 在范围 ["A", "FXSHRXW"] 内

## 示例

1. 输入：columnTitle = "A"
   输出：1

2. 输入：columnTitle = "AB"
   输出：28

3. 输入：columnTitle = "ZY"
   输出：701

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
