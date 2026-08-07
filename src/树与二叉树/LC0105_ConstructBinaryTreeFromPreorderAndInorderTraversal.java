// ============================================================
// LeetCode 105. 从前序与中序遍历序列构造二叉树 (Construct Binary Tree from Preorder and Inorder Traversal)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
// 刷题日期：2026-08-08
//
// ============================================================

import java.util.*;

public class LC0105_ConstructBinaryTreeFromPreorderAndInorderTraversal {

    // ==== 提交代码开始 ====
    Map<Integer, Integer> map;
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        map = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            map.put(inorder[i], i);
        }
        return buildTreeHelper(preorder, 0, preorder.length - 1, inorder, 0, inorder.length);
    }

    private TreeNode buildTreeHelper(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd) {
        if (preStart > preEnd) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[preStart]);
        int index = map.get(preorder[preStart]);
        int leftLength = index - inStart;
        root.left = buildTreeHelper(preorder, preStart + 1, preStart + leftLength, inorder, inStart, index - 1);
        root.right = buildTreeHelper(preorder, preStart + leftLength + 1, preEnd, inorder, index + 1, inEnd);
        return root;
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0105_ConstructBinaryTreeFromPreorderAndInorderTraversal s = new LC0105_ConstructBinaryTreeFromPreorderAndInorderTraversal();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(treeNode(3, 9, 20, null, null, 15, 7), s.buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(treeNode(-1), s.buildTree(new int[]{-1}, new int[]{-1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!checkEq(null, s.buildTree(new int[]{}, new int[]{}), "边界1-空输入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!checkEq(treeNode(1, 2, null, 3), s.buildTree(new int[]{1, 2, 3}, new int[]{3, 2, 1}), "边界2-全左链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!checkEq(treeNode(1, null, 2, null, 3), s.buildTree(new int[]{1, 2, 3}, new int[]{1, 2, 3}), "边界3-全右链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!checkEq(treeNode(1, 2, 3, 4), s.buildTree(new int[]{1, 2, 4, 3}, new int[]{4, 2, 1, 3}), "边界4-不平衡混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!checkEq(treeNode(-3000, null, 3000), s.buildTree(new int[]{-3000, 3000}, new int[]{-3000, 3000}), "边界5-极值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!checkEq(treeNode(1, 2, 3, 4, 5, 6, 7), s.buildTree(new int[]{1, 2, 4, 5, 3, 6, 7}, new int[]{4, 2, 5, 1, 6, 3, 7}), "边界6-完全二叉树")) failures++;
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
