# 94. 二叉树的中序遍历 (Binary Tree Inorder Traversal)

> https://leetcode.cn/problems/binary-tree-inorder-traversal/

难度：Easy ｜ 分类：栈、队列与优先队列

标签：栈、树、深度优先搜索、二叉树

## 题目描述

给定一个二叉树的根节点 root ，返回 它的 中序 遍历 。

示例 1：

输入：root = [1,null,2,3]
输出：[1,3,2]

示例 2：

输入：root = []
输出：[]

示例 3：

输入：root = [1]
输出：[1]

提示：

 树中节点数目在范围 [0, 100] 内
 -100 

进阶: 递归算法很简单，你可以通过迭代算法完成吗？

## 示例

1. 输入：root = [1,null,2,3]
   输出：[1,3,2]

2. 输入：root = []
   输出：[]

3. 输入：root = [1]
   输出：[1]

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
