# 5. 最长回文子串 (Longest Palindromic Substring)

> https://leetcode.cn/problems/longest-palindromic-substring/

难度：Medium ｜ 分类：动态规划

标签：双指针、字符串、动态规划、Manacher 算法

## 题目描述

给你一个字符串 s，找到 s 中最长的 回文 子串。

示例 1：

输入：s = "babad"
输出："bab"
解释："aba" 同样是符合题意的答案。

示例 2：

输入：s = "cbbd"
输出："bb"

提示：

 1 
 s 仅由数字和英文字母组成

## 示例

1. 输入：s = "babad"
   输出："bab"
   解释："aba" 同样是符合题意的答案。

2. 输入：s = "cbbd"
   输出："bb"

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
