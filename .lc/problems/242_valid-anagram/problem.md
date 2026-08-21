# 242. 有效的字母异位词 (Valid Anagram)

> https://leetcode.cn/problems/valid-anagram/

难度：Easy ｜ 分类：哈希表

标签：哈希表、字符串、排序

## 题目描述

给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的 字母异位词。

示例 1:

输入: s = "anagram", t = "nagaram"
输出: true

示例 2:

输入: s = "rat", t = "car"
输出: false

提示:

 1 4
 s 和 t 仅包含小写字母

进阶: 如果输入字符串包含 unicode 字符怎么办？你能否调整你的解法来应对这种情况？

## 示例

1. 输入：s = "anagram"，t = "nagaram"
   输出：true

2. 输入：s = "rat"，t = "car"
   输出：false

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
