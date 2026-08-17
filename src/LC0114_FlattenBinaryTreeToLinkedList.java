// ============================================================
// LeetCode 114. 二叉树展开为链表 (Flatten Binary Tree to Linked List)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/
// 刷题日期：2026-08-17
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0114_FlattenBinaryTreeToLinkedList {

    // ==== 提交代码开始 ====
    public void flatten(TreeNode root) {
        // TODO: 在这里实现你的解法
        
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0114_FlattenBinaryTreeToLinkedList s = new LC0114_FlattenBinaryTreeToLinkedList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            TreeNode t1 = treeNode(1, 2, 5, 3, 4, null, 6);
            s.flatten(t1);
            if (!checkEq("1,2,3,4,5,6", norm(t1), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            TreeNode t2 = treeNode();
            s.flatten(t2);
            if (!checkEq("null", norm(t2), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            TreeNode t3 = treeNode(0);
            s.flatten(t3);
            if (!checkEq("0", norm(t3), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { TreeNode t = treeNode(1, 2); s.flatten(t); if (!checkEq("1,2", norm(t), "单左链")) failures++; } catch (Throwable t) { failures++; System.out.println("单左链 异常: " + t); }
        try { TreeNode t = treeNode(1, null, 2); s.flatten(t); if (!checkEq("1,2", norm(t), "单右链")) failures++; } catch (Throwable t) { failures++; System.out.println("单右链 异常: " + t); }
        try { TreeNode t = treeNode(1, 2, 3); s.flatten(t); if (!checkEq("1,2,3", norm(t), "双孩子")) failures++; } catch (Throwable t) { failures++; System.out.println("双孩子 异常: " + t); }
        try { TreeNode t = treeNode(1, 2, null, 3); s.flatten(t); if (!checkEq("1,2,3", norm(t), "左子树带右子")) failures++; } catch (Throwable t) { failures++; System.out.println("左子树带右子 异常: " + t); }

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