# 538. 把二叉搜索树转换为累加树 (Convert BST to Greater Tree)

> https://leetcode.cn/problems/convert-bst-to-greater-tree/

难度：Medium ｜ 分类：树与二叉树

标签：树、深度优先搜索、二叉搜索树、二叉树

## 题目描述

给出二叉 搜索 树的根节点 root，该树的节点值各不相同，请你将其转换为累加树（Greater Sum Tree），将其转换为一个更大的树，使得原始二叉搜索树中的每个节点值都变为原本值加上原本二叉搜索树中所有比该节点值大的节点值的总和。

提醒一下，二叉搜索树满足下列约束条件：

 节点的左子树仅包含键 小于 节点键的节点。
 节点的右子树仅包含键 大于 节点键的节点。
 左右子树也必须是二叉搜索树。

注意：本题和 1038: https://leetcode.cn/problems/binary-search-tree-to-greater-sum-tree/ 相同

示例 1：

输入：[4,1,6,0,2,5,7,null,null,null,3,null,null,null,8]
输出：[30,36,21,36,35,26,15,null,null,null,33,null,null,null,8]

示例 2：

输入：root = [0,null,1]
输出：[1,null,1]

示例 3：

输入：root = [1,0,2]
输出：[3,3,2]

示例 4：

输入：root = [3,2,4,1]
输出：[7,9,4,10]

提示：

 树中的节点数介于 0 和 104 之间。
 每个节点的值介于 -104 和 104 之间。
 树中的所有值 互不相同 。
 给定的树为二叉搜索树。

## 示例

1. 输入：
   输出：[30,36,21,36,35,26,15,null,null,null,33,null,null,null,8]

2. 输入：root = [0,null,1]
   输出：[1,null,1]

3. 输入：root = [1,0,2]
   输出：[3,3,2]

4. 输入：root = [3,2,4,1]
   输出：[7,9,4,10]

## 约定

- 先独立思考，别急着看题解。
- 测试失败后可以让 lc-analyze 帮你分析思路。
