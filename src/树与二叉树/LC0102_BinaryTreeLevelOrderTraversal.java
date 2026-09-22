// ============================================================
// LeetCode 102. 二叉树的层序遍历 (Binary Tree Level Order Traversal)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/binary-tree-level-order-traversal/
// 复习日期：2026-09-22（二刷 · 一刷 2026-08-08 · 一刷思路：层序 BFS）
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 6 个：全左链/全右链/不平衡混合/负值/极值/完全二叉树）
//
// 思路：BFS 队列按层处理；每轮先记录当前队列长度，只弹出本层节点，并同步加入非空孩子。
// 复杂度：时间 O(n)，空间 O(w)（w 为树的最大宽度）
// ============================================================

import java.util.*;

public class LC0102_BinaryTreeLevelOrderTraversal {

    // ==== 提交代码开始 ====
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        Deque<TreeNode> queue = new LinkedList<>();
        queue.offerLast(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.pollFirst();
                list.add(cur.val);
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                }
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                }
            }
            ans.add(list);
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0102_BinaryTreeLevelOrderTraversal s = new LC0102_BinaryTreeLevelOrderTraversal();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(3), Arrays.asList(9, 20), Arrays.asList(15, 7)), s.levelOrder(treeNode(3, 9, 20, null, null, 15, 7)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1)), s.levelOrder(treeNode(1)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(), s.levelOrder(treeNode()), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(2), Arrays.asList(3)), s.levelOrder(treeNode(1, 2, null, 3)), "边界1-全左链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(2), Arrays.asList(3)), s.levelOrder(treeNode(1, null, 2, null, 3)), "边界2-全右链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5)), s.levelOrder(treeNode(1, 2, 3, 4, null, null, 5)), "边界3-不平衡混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(-1), Arrays.asList(-2, -3)), s.levelOrder(treeNode(-1, -2, -3)), "边界4-负值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1000), Arrays.asList(-1000)), s.levelOrder(treeNode(1000, -1000)), "边界5-极值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(2, 3), Arrays.asList(4, 5, 6, 7)), s.levelOrder(treeNode(1, 2, 3, 4, 5, 6, 7)), "边界6-完全二叉树")) failures++;
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
