// ============================================================
// LeetCode 124. 二叉树中的最大路径和 (Binary Tree Maximum Path Sum)
// 难度：Hard | 分类：动态规划
// 链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
// 刷题日期：2026-08-11
// ============================================================

import java.util.*;

public class LC0124_BinaryTreeMaximumPathSum {

    // ==== 提交代码开始 ====
    int max;
    public int maxPathSum(TreeNode root) {
        max = Integer.MIN_VALUE;
        dfs(root);
        return max;
    }

    private int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int maxLeft = Math.max(0, dfs(root.left));
        int maxRight = Math.max(0, dfs(root.right));
        max = Math.max(max, root.val + maxLeft + maxRight);
        return Math.max(maxLeft, maxRight) + root.val;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0124_BinaryTreeMaximumPathSum s = new LC0124_BinaryTreeMaximumPathSum();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(6, s.maxPathSum(treeNode(1, 2, 3)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(42, s.maxPathSum(treeNode(-10, 9, 20, null, null, 15, 7)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!checkEq(5, s.maxPathSum(treeNode(5)), "单元素正数")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素正数 异常: " + t); }
        try { if (!checkEq(-3, s.maxPathSum(treeNode(-3)), "单元素负数")) failures++; } catch (Throwable t) { failures++; System.out.println("单元素负数 异常: " + t); }
        try { if (!checkEq(-1, s.maxPathSum(treeNode(-2, -1, -3)), "全负数取单点")) failures++; } catch (Throwable t) { failures++; System.out.println("全负数取单点 异常: " + t); }
        try { if (!checkEq(3000, s.maxPathSum(treeNode(1000, 1000, 1000)), "大数全正过根")) failures++; } catch (Throwable t) { failures++; System.out.println("大数全正过根 异常: " + t); }
        try { if (!checkEq(4, s.maxPathSum(treeNode(0, -1, -1, 2, 3)), "最优路径不经过根")) failures++; } catch (Throwable t) { failures++; System.out.println("最优路径不经过根 异常: " + t); }
        try { if (!checkEq(48, s.maxPathSum(treeNode(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1)), "经典混合树")) failures++; } catch (Throwable t) { failures++; System.out.println("经典混合树 异常: " + t); }

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
