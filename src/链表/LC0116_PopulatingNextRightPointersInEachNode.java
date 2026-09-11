// ============================================================
// LeetCode 116. 填充每个节点的下一个右侧节点指针 (Populating Next Right Pointers in Each Node)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/
// 复习日期：2026-09-12（第 2 次复习 · 一刷 2026-08-24 · 上次复习 2026-09-06）
// 一刷思路：递归（前序 DFS）——先连 root.left.next=root.right、root.right.next=root.next.left，再递归左右子树
// 一刷复杂度：时间 O(n)，空间 O(log n) 递归栈（完美二叉树高 log n）
// 测试用例与一刷归档保持一致
//
// ============================================================

import java.util.*;

public class LC0116_PopulatingNextRightPointersInEachNode {

    static class Node {
        public int val;
        public Node left, right, next;

        public Node() {
        }

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }

    }

    // ==== 提交代码开始 ====
    public Node connect(Node root) {
        if (root == null) {
            return null;
        }
        Node mostLeft = root;
        // mostLeft.left 是下一层存在的充要条件，是一切循环的开始
        while (mostLeft.left != null) {
            // node 拿到 当前层的起点，通过遍历当前层来连接下一层的next
            Node node = mostLeft;
            while (node != null) {
                node.left.next = node.right;
                if (node.next != null) {
                    node.right.next = node.next.left;
                }
                node = node.next;
            }
            // 当前层链接完下一层的next后，可以切换到下一层继续链接下下一层
            mostLeft = mostLeft.left;
        }
        return root;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 示例测试（来自题目）----
        // 示例1: 7 节点完美二叉树 [1,2,3,4,5,6,7]
        try {
            Node root = buildTree(new int[]{1, 2, 3, 4, 5, 6, 7});
            List<String> got = levelStrings(new LC0116_PopulatingNextRightPointersInEachNode().connect(root));
            if (!TestUtil.checkEq(Arrays.asList("1", "2 3", "4 5 6 7"), got, "示例1: 三层树按层连接")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        // 示例2: 空树
        try {
            if (!TestUtil.checkEq(null, new LC0116_PopulatingNextRightPointersInEachNode().connect(null), "示例2: 空树返回null"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        // 约束：节点数 [0, 2^12-1]=[0,4095]，完美二叉树（无单边/链状情况）
        // 边界1: 单节点，next 应为 null
        try {
            Node root = buildTree(new int[]{1});
            List<String> got = levelStrings(new LC0116_PopulatingNextRightPointersInEachNode().connect(root));
            if (!TestUtil.checkEq(Arrays.asList("1"), got, "边界1: 单节点")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        // 边界2: 两层树，根的左右子通过 next 相连
        try {
            Node root = buildTree(new int[]{1, 2, 3});
            List<String> got = levelStrings(new LC0116_PopulatingNextRightPointersInEachNode().connect(root));
            if (!TestUtil.checkEq(Arrays.asList("1", "2 3"), got, "边界2: 两层树")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }
        // 边界3: 12 层满树（2^12-1=4095 节点，约束上限），逐层验证节点数 2^(k-1) 且层尾 next 为 null
        try {
            int[] big = new int[4095];
            for (int i = 0; i < big.length; i++) big[i] = i + 1;
            List<String> got = levelStrings(new LC0116_PopulatingNextRightPointersInEachNode().connect(buildTree(big)));
            boolean ok = got.size() == 12;
            for (int k = 0; ok && k < 12; k++) {
                if (got.get(k).split(" ").length != (1 << k)) ok = false;
            }
            if (!TestUtil.checkEq(true, ok, "边界3: 4095节点满树12层各层节点数正确")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 层序构造完全二叉树（完美二叉树为其特例），vals 长度必须为 2^k - 1 且不含空位
    static Node buildTree(int[] vals) {
        if (vals.length == 0) return null;
        Node[] nodes = new Node[vals.length];
        for (int i = 0; i < vals.length; i++) nodes[i] = new Node(vals[i]);
        for (int i = 0; i < vals.length; i++) {
            int l = 2 * i + 1, r = 2 * i + 2;
            if (l < vals.length) nodes[i].left = nodes[l];
            if (r < vals.length) nodes[i].right = nodes[r];
        }
        return nodes[0];
    }

    // 按层输出 next 连接结果：每层一个字符串（层内用空格分隔），null 根返回空列表
    static List<String> levelStrings(Node root) {
        List<String> res = new ArrayList<>();
        Node levelStart = root;
        while (levelStart != null) {
            StringBuilder sb = new StringBuilder();
            Node cur = levelStart;
            while (cur != null) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(cur.val);
                cur = cur.next;
            }
            res.add(sb.toString());
            levelStart = levelStart.left;
        }
        return res;
    }

}