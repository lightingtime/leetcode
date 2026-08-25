// ============================================================
// LeetCode 108. 将有序数组转换为二叉搜索树 (Convert Sorted Array to Binary Search Tree)
// 难度：Easy | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/
// 刷题日期：2026-08-25
//
// ============================================================

import java.util.*;

public class LC0108_ConvertSortedArrayToBinarySearchTree {

    // ==== 提交代码开始 ====
    public TreeNode sortedArrayToBST(int[] nums) {
        return buildBST(nums, 0, nums.length - 1);
    }

    private TreeNode buildBST(int[] nums, int l, int r) {
        if (l > r) {
            return null;
        }
        if (l == r) {
            return new TreeNode(nums[l]);
        }
        int mid = l + (r - l) / 2;
        TreeNode root = new TreeNode(nums[mid]);
        root.left = buildBST(nums, l, mid - 1);
        root.right = buildBST(nums, mid + 1, r);
        return root;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0108_ConvertSortedArrayToBinarySearchTree s = new LC0108_ConvertSortedArrayToBinarySearchTree();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        // 本题答案不唯一（取左中或右中都正确），示例改用「中序=原数组 + 高度平衡」两个性质校验
        try {
            if (!checkBST(new int[]{-10, -3, 0, 5, 9}, s.sortedArrayToBST(new int[]{-10, -3, 0, 5, 9}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkBST(new int[]{1, 3}, s.sortedArrayToBST(new int[]{1, 3}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题：答案不唯一，统一校验 BST 性质 + 高度平衡）----
        // 边界1: 单元素
        try {
            if (!checkBST(new int[]{7}, s.sortedArrayToBST(new int[]{7}), "边界1: 单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 偶数长度（根取中后两侧也要平衡）
        try {
            if (!checkBST(new int[]{1, 2, 3, 4}, s.sortedArrayToBST(new int[]{1, 2, 3, 4}), "边界2: 偶数长度")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 1..7 连续整数（平衡树的唯一形态）
        try {
            if (!checkBST(new int[]{1, 2, 3, 4, 5, 6, 7}, s.sortedArrayToBST(new int[]{1, 2, 3, 4, 5, 6, 7}), "边界3: 1..7")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 值域端点 ±10^4
        try {
            if (!checkBST(new int[]{-10000, -5000, 0, 5000, 10000}, s.sortedArrayToBST(new int[]{-10000, -5000, 0, 5000, 10000}), "边界4: 值域端点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 大输入（长度 10000，严格递增，防止退化成链）
        try {
            int n = 10000;
            int[] big = new int[n];
            for (int i = 0; i < n; i++) big[i] = i - 5000;
            if (!checkBST(big, s.sortedArrayToBST(big), "边界5: 长度10000")) failures++;
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

    // ---- 本题专用校验：中序遍历 = 原数组（BST 性质）且高度平衡（本题答案不唯一，不比对具体形状）----
    static boolean checkBST(int[] nums, TreeNode root, String label) {
        List<Integer> order = new ArrayList<>();
        int h = treeHeight(root, order);
        boolean ok = h >= 0 && order.size() == nums.length;
        for (int i = 0; ok && i < nums.length; i++) {
            if (order.get(i) != nums[i]) ok = false;
        }
        if (ok) {
            System.out.println(label + " 通过 ✓");
        } else {
            System.out.println(label + " 失败 ✗ 期望中序=" + Arrays.toString(nums) + " 且平衡，实际: 高=" + h + " 中序=" + order);
        }
        return ok;
    }

    // 中序遍历并收集值，同时返回子树高度；左右高度差 > 1 或子树不平衡时返回 -1
    static int treeHeight(TreeNode root, List<Integer> order) {
        if (root == null) return 0;
        int lh = treeHeight(root.left, order);
        order.add(root.val);
        int rh = treeHeight(root.right, order);
        if (lh < 0 || rh < 0 || Math.abs(lh - rh) > 1) return -1;
        return Math.max(lh, rh) + 1;
    }

}