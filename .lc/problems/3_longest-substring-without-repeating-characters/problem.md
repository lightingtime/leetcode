# 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)

> https://leetcode.cn/problems/longest-substring-without-repeating-characters/

难度：Medium ｜ 分类：双指针与滑动窗口

标签：哈希表、字符串、滑动窗口

## 题目描述

给定一个字符串 s ，请你找出其中不含有重复字符的 最长 子串 的长度。

示例 1:

输入: s = "abcabcbb"
输出: 3 
解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。注意 "bca" 和 "cab" 也是正确答案。

示例 2:

输入: s = "bbbbb"
输出: 1
解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。

示例 3:

输入: s = "pwwkew"
输出: 3
解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。

提示：

 0 5
 s 由英文字母、数字、符号和空格组成

## 示例

1. 输入：s = "abcabcbb"
   输出：3
   解释：因为无重复字符的最长子串是 "abc"，所以其长度为 3。注意 "bca" 和 "cab" 也是正确答案。

2. 输入：s = "bbbbb"
   输出：1
   解释：因为无重复字符的最长子串是 "b"，所以其长度为 1。

3. 输入：s = "pwwkew"
   输出：3
   解释：因为无重复字符的最长子串是 "wke"，所以其长度为 3。

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
