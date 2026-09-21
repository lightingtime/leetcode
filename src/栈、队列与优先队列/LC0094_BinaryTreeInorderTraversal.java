// ============================================================
// LeetCode 94. 二叉树的中序遍历 (Binary Tree Inorder Traversal)
// 难度：Easy | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/binary-tree-inorder-traversal/
// 刷题日期：2026-09-21
//
// 二刷第 4 次 · 一刷 2026-08-07 · 上次复习 2026-09-05（较强，间隔 15 天）
// 一刷思路：迭代栈 + cur 指针——沿左链一路压栈，弹出访问后把 cur 移到右孩子
// 已有的两种写法：① 递归 DFS（中序，08-31）；② 迭代栈 cur 指针（09-05）。本次沿用哪种就按哪种合并
// 测试用例与一刷归档保持一致（示例 3 + 边界若干），未重新设计
// ============================================================

import java.util.*;

public class LC0094_BinaryTreeInorderTraversal {

    // ==== 提交代码开始 ====
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        TreeNode cur = root;
        // 收尾条件：栈非空或 cur 非空——弹出后 cur 可能为空，但栈里还有祖先没访问
        while (!queue.isEmpty() || cur != null) {
            while (cur != null) {
                queue.offerLast(cur);
                cur = cur.left;
            }
            cur = queue.pollLast();
            ans.add(cur.val);
            // 无论右孩子是否为空都要移过去；不移会重复压同一条左链，移了才靠外层条件决定是否收尾
            cur = cur.right;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0094_BinaryTreeInorderTraversal s = new LC0094_BinaryTreeInorderTraversal();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(Arrays.asList(1, 3, 2), s.inorderTraversal(treeNode(1, null, 2, 3)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(), s.inorderTraversal(treeNode()), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(1), s.inorderTraversal(treeNode(1)), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!checkEq(Arrays.asList(1, 2, 3), s.inorderTraversal(treeNode(3, 2, null, 1)), "边界1-全左链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(1, 2, 3), s.inorderTraversal(treeNode(1, null, 2, null, 3)), "边界2-全右链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(-50, -100, -1), s.inorderTraversal(treeNode(-100, -50, -1)), "边界3-负数值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(-100, 100), s.inorderTraversal(treeNode(100, -100)), "边界4-大数与最小值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(4, 2, 1, 5, 3), s.inorderTraversal(treeNode(1, 2, 3, 4, null, 5)), "边界5-混合非平衡树")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(1, 2, 3), s.inorderTraversal(treeNode(2, 1, 3)), "边界6-BST形态中序有序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

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
