# 230. 二叉搜索树中第 K 小的元素 (Kth Smallest Element in a BST)

> https://leetcode.cn/problems/kth-smallest-element-in-a-bst/

难度：Medium ｜ 分类：树与二叉树

标签：树、深度优先搜索、二叉搜索树、二叉树

## 题目描述

给定一个二叉搜索树的根节点 root ，和一个整数 k ，请你设计一个算法查找其中第 k 小的元素（k 从 1 开始计数）。

示例 1：

输入：root = [3,1,4,null,2], k = 1
输出：1

示例 2：

输入：root = [5,3,6,2,4,null,null,1], k = 3
输出：3

提示：

 树中的节点数为 n 。
 1 4
 0 4

进阶：如果二叉搜索树经常被修改（插入/删除操作）并且你需要频繁地查找第 k 小的值，你将如何优化算法？

## 示例

1. 输入：root = [3,1,4,null,2]，k = 1
   输出：1

2. 输入：root = [5,3,6,2,4,null,null,1]，k = 3
   输出：3

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
