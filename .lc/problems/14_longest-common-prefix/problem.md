# 14. 最长公共前缀 (Longest Common Prefix)

> https://leetcode.cn/problems/longest-common-prefix/

难度：Easy ｜ 分类：字符串

标签：字典树、数组、字符串

## 题目描述

编写一个函数来查找字符串数组中的最长公共前缀。

如果不存在公共前缀，返回空字符串 ""。

示例 1：

输入：strs = ["flower","flow","flight"]
输出："fl"

示例 2：

输入：strs = ["dog","racecar","car"]
输出：""
解释：输入不存在公共前缀。

提示：

 1 
 0 
 strs[i] 如果非空，则仅由小写英文字母组成

## 示例

1. 输入：strs = ["flower","flow","flight"]
   输出："fl"

2. 输入：strs = ["dog","racecar","car"]
   输出：""
   解释：输入不存在公共前缀。

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
