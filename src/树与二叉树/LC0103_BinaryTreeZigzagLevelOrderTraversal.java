// ============================================================
// LeetCode 103. 二叉树的锯齿形层序遍历 (Binary Tree Zigzag Level Order Traversal)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
// 刷题日期：2026-08-25
//
// ============================================================

import java.util.*;

public class LC0103_BinaryTreeZigzagLevelOrderTraversal {

    // ==== 提交代码开始 ====
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        boolean left = true;
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                TreeNode cur = queue.removeFirst();
                if (left) {
                    list.add(cur.val);
                } else {
                    list.add(0, cur.val);
                }
                if (cur.left != null) {
                    queue.offerLast(cur.left);
                }
                if (cur.right != null) {
                    queue.offerLast(cur.right);
                }
            }
            ans.add(list);
            left = !left;
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0103_BinaryTreeZigzagLevelOrderTraversal s = new LC0103_BinaryTreeZigzagLevelOrderTraversal();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(3), Arrays.asList(20, 9), Arrays.asList(15, 7)), s.zigzagLevelOrder(treeNode(3, 9, 20, null, null, 15, 7)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1)), s.zigzagLevelOrder(treeNode(1)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(Arrays.asList(), s.zigzagLevelOrder(treeNode()), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题：锯齿方向交替 + 空树 + 单链 + 大输入）----
        // 边界1: 左链（每层单节点，方向交替不影响）
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(2), Arrays.asList(3)), s.zigzagLevelOrder(treeNode(1, 2, null, 3)), "边界1: 左链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 右链
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(2), Arrays.asList(3)), s.zigzagLevelOrder(treeNode(1, null, 2, null, 3)), "边界2: 右链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 满二叉树 7 节点（第二层从右往左）
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(1), Arrays.asList(3, 2), Arrays.asList(4, 5, 6, 7)), s.zigzagLevelOrder(treeNode(1, 2, 3, 4, 5, 6, 7)), "边界3: 满二叉树")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 节点值含 0 和负数
        try {
            if (!checkEq(Arrays.asList(Arrays.asList(0), Arrays.asList(2, -1), Arrays.asList(-3)), s.zigzagLevelOrder(treeNode(0, -1, 2, -3)), "边界4: 0与负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 右链 2000 节点（约束上限，每层单节点，验证大输入与多轮方向切换）
        try {
            TreeNode chain = null;
            for (int v = 2000; v >= 1; v--) {
                TreeNode n = new TreeNode(v);
                n.right = chain;
                chain = n;
            }
            List<List<Integer>> exp = new ArrayList<>();
            for (int v = 1; v <= 2000; v++) exp.add(Arrays.asList(v));
            if (!checkEq(exp, s.zigzagLevelOrder(chain), "边界5: 右链2000")) failures++;
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