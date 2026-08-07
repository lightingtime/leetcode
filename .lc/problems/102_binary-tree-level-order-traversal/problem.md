# 102. 二叉树的层序遍历 (Binary Tree Level Order Traversal)

> https://leetcode.cn/problems/binary-tree-level-order-traversal/

难度：Medium ｜ 分类：树与二叉树

标签：树、广度优先搜索、二叉树

## 题目描述

给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。

示例 1：

输入：root = [3,9,20,null,null,15,7]
输出：[[3],[9,20],[15,7]]

示例 2：

输入：root = [1]
输出：[[1]]

示例 3：

输入：root = []
输出：[]

提示：

 树中节点数目在范围 [0, 2000] 内
 -1000

## 示例

1. 输入：root = [3,9,20,null,null,15,7]
   输出：[[3],[9,20],[15,7]]

2. 输入：root = [1]
   输出：[[1]]

3. 输入：root = []
   输出：[]

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
