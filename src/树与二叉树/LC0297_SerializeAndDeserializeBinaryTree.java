// ============================================================
// LeetCode 297. 二叉树的序列化与反序列化 (Serialize and Deserialize Binary Tree)
// 难度：Hard | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
// 复习日期：2026-09-17（第 5 次复习 · 一刷 2026-08-08）
// 一刷写法：层序 BFS + `#` 占位符（Deque FIFO 队列，O(n) 时间 / O(n) 空间）
// 测试用例：与一刷归档保持一致（示例 4 + 边界 5 + 回归，原样同步）
//
// 思路：层序遍历（BFS）+ `#` 占位 null。序列化按层出队写值、非空节点把左右孩子（含 null）入队；
//       反序列化按同一层序消费 token，每个非空队首固定吃两个 token 接左右孩子。
// 复杂度：时间 O(n) 空间 O(n)
// ============================================================

import java.util.*;

public class LC0297_SerializeAndDeserializeBinaryTree {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 Codec 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class Codec {
        public String serialize(TreeNode root) {
            if (root == null) {
                return "";
            }
            Deque<TreeNode> queue = new LinkedList<>();
            List<String> list = new ArrayList<>();
            queue.offerLast(root);
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.pollFirst();
                    if (node!= null) {
                        list.add(node.val + "");
                        queue.offerLast(node.left);
                        queue.offerLast(node.right);
                    } else {
                        list.add("#");
                    }
                }
            }
            return String.join(" ", list);
        }
        public TreeNode deserialize(String data) {
            if (data.isEmpty()) {
                return null;
            }
            String[] split = data.split(" ");
            TreeNode root = new TreeNode(Integer.parseInt(split[0]));
            int i = 1;
            Deque<TreeNode> queue = new ArrayDeque<>();
            queue.offerLast(root);
            while (i < split.length) {
                TreeNode cur = queue.pollFirst();
                if (!split[i].equals("#")) {
                    cur.left = new TreeNode(Integer.parseInt(split[i]));
                    queue.offerLast(cur.left);
                }
                i++;
                if (!split[i].equals("#")) {
                    cur.right = new TreeNode(Integer.parseInt(split[i]));
                    queue.offerLast(cur.right);
                }
                i++;
            }
            return root;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        Codec codec = new Codec();
        int failures = 0;

        // ---- 示例测试（题目示例：序列化→反序列化后结构不变）----
        try {
            TreeNode t = treeNode(1, 2, 3, null, null, 4, 5);
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            TreeNode t = treeNode();
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "示例2-空树")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            TreeNode t = treeNode(1);
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "示例3-单节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }
        try {
            TreeNode t = treeNode(1, 2);
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "示例4")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例4 异常: " + t); }

        // ---- 边界测试（往返一致）----
        try {
            TreeNode t = treeNode(1, 2, null, 3, null, 4);
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "边界1-全左链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            TreeNode t = treeNode(1, null, 2, null, 3, null, 4);
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "边界2-全右链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            TreeNode t = treeNode(3, 5, 1, null, 6);
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "边界3-不平衡混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            TreeNode t = treeNode(-1000, null, 1000);
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "边界4-极值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            TreeNode t = treeNode(1, 2, 3, 4, 5, 6, 7);
            if (!checkEq(t, codec.deserialize(codec.serialize(t)), "边界5-完全二叉树")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // ---- 本地测试辅助（节点格式化依赖 TreeNode，其余逻辑见 TestUtil）----
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
