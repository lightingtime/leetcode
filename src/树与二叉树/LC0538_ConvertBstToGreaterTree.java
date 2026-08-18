// ============================================================
// LeetCode 538. 把二叉搜索树转换为累加树 (Convert BST to Greater Tree)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/convert-bst-to-greater-tree/
// 刷题日期：2026-08-18
//
// ============================================================

import java.util.*;

public class LC0538_ConvertBstToGreaterTree {

    // ==== 提交代码开始 ====
    long sum;
    public TreeNode convertBST(TreeNode root) {
        // TODO: 在这里实现你的解法
        sum = 0;
        dfs(root);
        return root;
    }

    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.right);
        root.val += sum;
        sum = root.val;
        dfs(root.left);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0538_ConvertBstToGreaterTree s = new LC0538_ConvertBstToGreaterTree();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(treeNode(30, 36, 21, 36, 35, 26, 15, null, null, null, 33, null, null, null, 8),
                    s.convertBST(treeNode(4, 1, 6, 0, 2, 5, 7, null, null, null, 3, null, null, null, 8)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(treeNode(1, null, 1), s.convertBST(treeNode(0, null, 1)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(treeNode(3, 3, 2), s.convertBST(treeNode(1, 0, 2)), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }
        try {
            if (!checkEq(treeNode(7, 9, 4, 10), s.convertBST(treeNode(3, 2, 4, 1)), "示例4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例4 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 空树：直接返回 null
        try { if (!checkEq(null, s.convertBST(null), "边界1-空树")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-空树 异常: " + t); }
        // 单节点：没有比它大的节点，值不变
        try { if (!checkEq(treeNode(5), s.convertBST(treeNode(5)), "边界2-单节点")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-单节点 异常: " + t); }
        // 右链 1->2->3（升序链）：3 不变，2+3=5，1+2+3=6
        try { if (!checkEq(treeNode(6, null, 5, null, 3), s.convertBST(treeNode(1, null, 2, null, 3)), "边界3-右链")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3-右链 异常: " + t); }
        // 左链 3->2->1（降序链）：1 累加最多=6，2=5，3 不变=3
        try { if (!checkEq(treeNode(3, 5, null, 6), s.convertBST(treeNode(3, 2, null, 1)), "边界4-左链")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-左链 异常: " + t); }
        // 负数节点：-3 加上所有比它大的 1，得 -2；1 不变
        try { if (!checkEq(treeNode(-2, null, 1), s.convertBST(treeNode(-3, null, 1)), "边界5-负数")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5-负数 异常: " + t); }

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