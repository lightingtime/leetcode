// ============================================================
// LeetCode 236. 二叉树的最近公共祖先 (Lowest Common Ancestor of a Binary Tree)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
// 刷题日期：2026-09-28
//
// 思路：递归搜索左右子树，合并两侧命中结果；当前节点是 p 或 q 时直接向上返回。
// 复杂度：时间 O(n)，递归栈空间 O(h)
// ============================================================

import java.util.*;

public class LC0236_LowestCommonAncestorOfABinaryTree {

    // ==== 提交代码开始 ====
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        if (p == root || q == root) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) {
            return root;
        }
        return left == null ? right : left;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0236_LowestCommonAncestorOfABinaryTree s = new LC0236_LowestCommonAncestorOfABinaryTree();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        TreeNode t1 = treeNode(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
        try {
            if (!checkEq(treeNode(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4), s.lowestCommonAncestor(t1, find(t1, 5), find(t1, 1)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(treeNode(5, 6, 2, null, null, 7, 4), s.lowestCommonAncestor(t1, find(t1, 5), find(t1, 4)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        TreeNode t3 = treeNode(1, 2);
        try {
            if (!checkEq(treeNode(1, 2), s.lowestCommonAncestor(t3, find(t3, 1), find(t3, 2)), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        TreeNode lc = treeNode(1, 2, null, 3, null, 4);
        try {
            if (!checkEq(treeNode(2, 3, null, 4), s.lowestCommonAncestor(lc, find(lc, 4), find(lc, 2)), "边界1-左链祖先")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        TreeNode rc = treeNode(1, null, 2, null, 3, null, 4);
        try {
            if (!checkEq(treeNode(3, null, 4), s.lowestCommonAncestor(rc, find(rc, 3), find(rc, 4)), "边界2-右链自身")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        TreeNode ub = treeNode(3, 5, 1, null, 6);
        try {
            if (!checkEq(treeNode(3, 5, 1, null, 6), s.lowestCommonAncestor(ub, find(ub, 6), find(ub, 1)), "边界3-不平衡")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        TreeNode ext = treeNode(-1000000000, null, 1000000000);
        try {
            if (!checkEq(treeNode(-1000000000, null, 1000000000), s.lowestCommonAncestor(ext, find(ext, -1000000000), find(ext, 1000000000)), "边界4-极值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

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

    static TreeNode find(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val == val) return root;
        TreeNode l = find(root.left, val);
        if (l != null) return l;
        return find(root.right, val);
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
