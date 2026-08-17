// ============================================================
// LeetCode 543. 二叉树的直径 (Diameter of Binary Tree)
// 难度：Easy | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/diameter-of-binary-tree/
// 刷题日期：2026-08-17
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0543_DiameterOfBinaryTree {

    // ==== 提交代码开始 ====
    int max = 0;
    public int diameterOfBinaryTree(TreeNode root) {
        max = 0; // 重置：成员变量在多次调用间会残留上一次的答案
        dfs(root);
        return max;
    }

    private int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = dfs(root.left);
        int right = dfs(root.right);
        max = Math.max(max, left + right);
        return 1 + Math.max(left, right);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0543_DiameterOfBinaryTree s = new LC0543_DiameterOfBinaryTree();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(3, s.diameterOfBinaryTree(treeNode(1, 2, 3, 4, 5)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(1, s.diameterOfBinaryTree(treeNode(1, 2)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!checkEq(0, s.diameterOfBinaryTree(treeNode(1)), "单节点")) failures++; } catch (Throwable t) { failures++; System.out.println("单节点 异常: " + t); }
        try { if (!checkEq(3, s.diameterOfBinaryTree(treeNode(1, 2, null, 3, null, 4)), "链状4节点")) failures++; } catch (Throwable t) { failures++; System.out.println("链状4节点 异常: " + t); }
        try { if (!checkEq(2, s.diameterOfBinaryTree(treeNode(1, 2, 3)), "根左右各一")) failures++; } catch (Throwable t) { failures++; System.out.println("根左右各一 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // ---- 本地测试辅助（节点格式化依赖本题的 ListNode/TreeNode，其余逻辑见 TestUtil）----
    static boolean checkEq(Object expected, Object actual, String label) {
        return TestUtil.eq(label, norm(expected), norm(actual));
    }

    static boolean checkEqUnordered(Object expected, Object actual, String label) {
        return TestUtil.eq(label, normUnordered(expected), normUnordered(actual));
    }

    static String norm(Object o) {
        if (o == null) return "null";
        if (o instanceof TreeNode) {
            List<String> vals = new ArrayList<>();
            Queue<TreeNode> q = new LinkedList<>();
            q.offer((TreeNode) o);
            while (!q.isEmpty()) {
                TreeNode n = q.poll();
                if (n == null) { vals.add("null"); continue; }
                vals.add(String.valueOf(n.val));
                q.offer(n.left);
                q.offer(n.right);
            }
            while (!vals.isEmpty() && vals.get(vals.size() - 1).equals("null")) vals.remove(vals.size() - 1);
            return String.join(",", vals);
        }
        return TestUtil.norm(o);
    }

    static String normUnordered(Object o) {
        if (o instanceof Object[]) {
            List<String> es = new ArrayList<>();
            for (Object v : (Object[]) o) es.add(norm(v));
            Collections.sort(es);
            return "[" + String.join(", ", es) + "]";
        }
        if (o instanceof List) {
            List<String> es = new ArrayList<>();
            for (Object v : (List<?>) o) es.add(norm(v));
            Collections.sort(es);
            return "[" + String.join(", ", es) + "]";
        }
        return TestUtil.normUnordered(o);
    }

    static TreeNode treeNode(Object... vals) {
        if (vals.length == 0 || vals[0] == null) return null;
        TreeNode root = new TreeNode(((Number) vals[0]).intValue());
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode node = q.poll();
            if (i < vals.length && vals[i] != null) { node.left = new TreeNode(((Number) vals[i]).intValue()); q.offer(node.left); }
            i++;
            if (i < vals.length && vals[i] != null) { node.right = new TreeNode(((Number) vals[i]).intValue()); q.offer(node.right); }
            i++;
        }
        return root;
    }

}