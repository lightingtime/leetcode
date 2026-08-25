# 103. 二叉树的锯齿形层序遍历 (Binary Tree Zigzag Level Order Traversal)

> https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/

难度：Medium ｜ 分类：树与二叉树

标签：树、广度优先搜索、二叉树

## 题目描述

给你二叉树的根节点 root ，返回其节点值的 锯齿形层序遍历 。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。

示例 1：

输入：root = [3,9,20,null,null,15,7]
输出：[[3],[20,9],[15,7]]

示例 2：

输入：root = [1]
输出：[[1]]

示例 3：

输入：root = []
输出：[]

提示：

 树中节点数目在范围 [0, 2000] 内
 -100

## 示例

1. 输入：root = [3,9,20,null,null,15,7]
   输出：[[3],[20,9],[15,7]]

2. 输入：root = [1]
   输出：[[1]]

3. 输入：root = []
   输出：[]

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
