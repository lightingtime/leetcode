# 104. 二叉树的最大深度 (Maximum Depth of Binary Tree)

> https://leetcode.cn/problems/maximum-depth-of-binary-tree/

难度：Easy ｜ 分类：树与二叉树

标签：树、深度优先搜索、广度优先搜索、二叉树

## 题目描述

给定一个二叉树 root ，返回其最大深度。

二叉树的 最大深度 是指从根节点到最远叶子节点的最长路径上的节点数。

示例 1：

输入：root = [3,9,20,null,null,15,7]
输出：3

示例 2：

输入：root = [1,null,2]
输出：2

提示：

 树中节点的数量在 [0, 104] 区间内。
 -100

## 示例

1. 输入：root = [3,9,20,null,null,15,7]
   输出：3

2. 输入：root = [1,null,2]
   输出：2

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
