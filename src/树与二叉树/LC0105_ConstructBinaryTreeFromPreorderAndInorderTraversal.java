// ============================================================
// LeetCode 105. 从前序与中序遍历序列构造二叉树 (Construct Binary Tree from Preorder and Inorder Traversal)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-08 · 一刷一次 Accepted）
// 一刷写法：前序定根 + 中序哈希定位 + 递归分治——前序的第一个值就是当前子树的根；在中序里找到它的位置即可知道左子树有几个节点，据此切分两段区间递归；中序的位置用哈希表预存，避免每层线性查找。O(n)/O(n)
// 本题易错点：① 前序区间与中序区间的「长度相同」是切分的依据（左子树大小 = 中序根下标 − 中序左端点），切错会静默构造出错误的树；② 递归出口是区间为空（preL > preR 或 inL > inR），不是下标越界判断；③ 不用哈希表也能写，但每层都线性扫中序会退化到 O(n²)；④ 两个数组长度不一致或为空要能兜住
// 测试用例与一刷归档保持一致（示例 2 个 + 边界 6 个：空输入/全左链/全右链/不平衡混合/极值/完全二叉树）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
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
        TreeNode root = buildHelper(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
        return root;
    }

    private TreeNode buildHelper(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd) {
        if (preStart > preEnd) {
            return null;
        }

        TreeNode root = new TreeNode(preorder[preStart]);
        int index = map.get(root.val);
        int leftLen = index - inStart;
        root.left = buildHelper(preorder, preStart + 1, preStart + leftLen, inorder, inStart, index - 1);
        root.right = buildHelper(preorder, preStart + 1 + leftLen, preEnd, inorder, index + 1, inEnd);
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
