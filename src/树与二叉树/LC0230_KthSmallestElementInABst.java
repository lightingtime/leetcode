// ============================================================
// LeetCode 230. 二叉搜索树中第 K 小的元素 (Kth Smallest Element in a BST)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/kth-smallest-element-in-a-bst/
// 刷题日期：2026-08-25
//
// ============================================================

import java.util.*;

public class LC0230_KthSmallestElementInABst {

    // ==== 提交代码开始 ====
    TreeNode cur;
    int count;
    public int kthSmallest(TreeNode root, int k) {
        count = 0;
        cur = null;
        dfs(root, k);
        return cur.val;
    }

    private void dfs(TreeNode node, int k) {
        if (node == null) {
            return;
        }
        dfs(node.left, k);
        count++;
        if (count == k) {
            cur = node;
        }
        dfs(node.right, k);
    }


    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0230_KthSmallestElementInABst s = new LC0230_KthSmallestElementInABst();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(1, s.kthSmallest(treeNode(3, 1, 4, null, 2), 1), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(3, s.kthSmallest(treeNode(5, 3, 6, 2, 4, null, null, 1), 3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题：k 的端点、退化链、值域端点、大输入）----
        // 边界1: k=1（最小值）
        try {
            if (!checkEq(1, s.kthSmallest(treeNode(2, 1, 3), 1), "边界1: k=1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: k=n（最大值）
        try {
            if (!checkEq(3, s.kthSmallest(treeNode(2, 1, 3), 3), "边界2: k=n")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 单节点
        try {
            if (!checkEq(7, s.kthSmallest(treeNode(7), 1), "边界3: 单节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 左链退化（中序 1,2,3,4）
        try {
            if (!checkEq(3, s.kthSmallest(treeNode(4, 3, null, 2, null, 1), 3), "边界4: 左链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 右链退化（中序 1,2,3）
        try {
            if (!checkEq(2, s.kthSmallest(treeNode(1, null, 2, null, 3), 2), "边界5: 右链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 值域端点 0 和 10^4
        try {
            if (!checkEq(0, s.kthSmallest(treeNode(0, null, 10000), 1), "边界6a: 最小端点")) failures++;
            if (!checkEq(10000, s.kthSmallest(treeNode(0, null, 10000), 2), "边界6b: 最大端点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 右链 2000 节点（值 1..2000，验证深链 + 大输入）
        try {
            TreeNode chain = null;
            for (int v = 2000; v >= 1; v--) {
                TreeNode n = new TreeNode(v);
                n.right = chain;
                chain = n;
            }
            if (!checkEq(1000, s.kthSmallest(chain, 1000), "边界7a: 链中值")) failures++;
            if (!checkEq(2000, s.kthSmallest(chain, 2000), "边界7b: 链最大")) failures++;
            if (!checkEq(1, s.kthSmallest(chain, 1), "边界7c: 链最小")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

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