// ============================================================
// LeetCode 101. 对称二叉树 (Symmetric Tree)
// 难度：Easy | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/symmetric-tree/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-07 · 一刷一次 Accepted）
// 一刷写法：把「自己和自己比」拆成「左子树与右子树镜像比」——helper(left, right)：双 null 返回 true、只有一个 null 返回 false、值不同返回 false，再递归比 (left.left, right.right) 与 (left.right, right.left)。O(n)/O(h)
// 本题易错点：① 镜像配对关系是 左.左 ↔ 右.右、左.右 ↔ 右.左，写成「左.左 ↔ 右.左」就不是镜像了；② 递归里的空值判断顺序必须是「先双 null、再单 null、最后比 val」，顺序反了会空指针；③ 迭代版（栈/队列）注意 java 的 ArrayDeque 不接受 null 元素（一刷就是踩了这个换成 LinkedList），要么换集合、要么入队前就把双 null 过滤掉；④ 空树与单节点都算对称
// 测试用例与一刷归档保持一致（示例 2 个 + 边界 6 个：单节点/左右值不同/结构对称值错位/四层全对称/负值对称/极值对称）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0101_SymmetricTree {

    // ==== 提交代码开始 ====
    public boolean isSymmetric(TreeNode root) {
        return isSymmetricHelper(root, root);
    }

    private boolean isSymmetricHelper(TreeNode left, TreeNode right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        if (left.val != right.val) {
            return false;
        }
        return isSymmetricHelper(left.left, right.right) && isSymmetricHelper(left.right, right.left);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0101_SymmetricTree s = new LC0101_SymmetricTree();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(true, s.isSymmetric(treeNode(1, 2, 2, 3, 4, 4, 3)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(false, s.isSymmetric(treeNode(1, 2, 2, null, 3, null, 3)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!checkEq(true, s.isSymmetric(treeNode(1)), "边界1-单节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!checkEq(false, s.isSymmetric(treeNode(1, 2, 3)), "边界2-左右值不同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!checkEq(false, s.isSymmetric(treeNode(1, 2, 2, 3, null, null, 4)), "边界3-结构对称值错位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!checkEq(true, s.isSymmetric(treeNode(1, 2, 2, 3, 4, 4, 3, 5, 6, 7, 8, 8, 7, 6, 5)), "边界4-四层全对称")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!checkEq(true, s.isSymmetric(treeNode(-1, -2, -2, -3, -4, -4, -3)), "边界5-负值对称")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!checkEq(true, s.isSymmetric(treeNode(100, 100, 100)), "边界6-极值对称")) failures++;
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
