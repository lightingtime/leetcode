// ============================================================
// LeetCode 138. 随机链表的复制 (Copy List with Random Pointer)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/copy-list-with-random-pointer/
// 刷题日期：2026-08-24
//
// ============================================================

import java.util.*;

public class LC0138_CopyListWithRandomPointer {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 Node 在提交时自动处理。
    static class Node {
        int val;
        Node next;
        Node random;

        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
    }

    // ==== 提交代码开始 ====
    public Node copyRandomList(Node head) {
        if (head == null) {
            return head;
        }
        for (Node node = head; node != null; node = node.next.next) {
            Node nodeNew = new Node(node.val);
            nodeNew.next = node.next;
            node.next = nodeNew;
        }
        for (Node node = head; node != null; node = node.next.next) {
            Node nodeNew = node.next;
            // 这里注意是链接 原来 node 的 随机节点的next
            // 因为此时的随机节点的next是我们创建出来的随机节点的copy
            nodeNew.random = node.random != null ? node.random.next : null;
        }
        Node newHead = head.next;
        // 注意这里 node = node.next
        for (Node node = head; node != null; node = node.next) {
            // 断开拷贝节点
            Node nodeNew = node.next;
            node.next = nodeNew.next;
            // 同上面的用法，需要链接下一个节点的拷贝节点，就是 nodeNew.next(原节点的下一个节点).next(原节点下一个节点的拷贝节点)
            nodeNew.next = nodeNew.next != null ? nodeNew.next.next : null;
        }
        return newHead;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 示例测试（来自题目）----
        // 示例1: [[7,null],[13,0],[11,4],[10,2],[1,0]]
        try {
            Node h = buildList(new Integer[][]{{7, null}, {13, 0}, {11, 4}, {10, 2}, {1, 0}});
            if (!TestUtil.checkEq(true, verify(h, new LC0138_CopyListWithRandomPointer().copyRandomList(h)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        // 示例2: [[1,1],[2,1]]
        try {
            Node h = buildList(new Integer[][]{{1, 1}, {2, 1}});
            if (!TestUtil.checkEq(true, verify(h, new LC0138_CopyListWithRandomPointer().copyRandomList(h)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        // 示例3: [[3,null],[3,0],[3,null]]（重复值）
        try {
            Node h = buildList(new Integer[][]{{3, null}, {3, 0}, {3, null}});
            if (!TestUtil.checkEq(true, verify(h, new LC0138_CopyListWithRandomPointer().copyRandomList(h)), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 约束：0 <= n <= 1000，-10^4 <= val <= 10^4，random 为 null 或指向链表中节点
        // 边界1: 空链表
        try {
            if (!TestUtil.checkEq(null, new LC0138_CopyListWithRandomPointer().copyRandomList(null), "边界1: 空链表")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 单节点，random 为 null
        try {
            Node h = buildList(new Integer[][]{{-1000, null}});
            if (!TestUtil.checkEq(true, verify(h, new LC0138_CopyListWithRandomPointer().copyRandomList(h)), "边界2: 单节点无random")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 单节点，random 指向自己（自环）
        try {
            Node h = buildList(new Integer[][]{{1000, 0}});
            if (!TestUtil.checkEq(true, verify(h, new LC0138_CopyListWithRandomPointer().copyRandomList(h)), "边界3: 自环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: n=1000 上限，random 混合指向（含 null 与多种索引），val 取边界附近
        try {
            int N = 1000;
            Integer[][] big = new Integer[N][2];
            for (int i = 0; i < N; i++) {
                big[i][0] = i % 2 == 0 ? -10000 : 10000;
                big[i][1] = i % 3 == 0 ? null : (i * 7) % N;
            }
            Node h = buildList(big);
            if (!TestUtil.checkEq(true, verify(h, new LC0138_CopyListWithRandomPointer().copyRandomList(h)), "边界4: 1000节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // data[i] = {val, random_index|null}，按数组构造链表
    static Node buildList(Integer[][] data) {
        if (data.length == 0) return null;
        Node[] nodes = new Node[data.length];
        for (int i = 0; i < data.length; i++) nodes[i] = new Node(data[i][0]);
        for (int i = 0; i < data.length; i++) {
            if (i + 1 < data.length) nodes[i].next = nodes[i + 1];
            if (data[i][1] != null) nodes[i].random = nodes[data[i][1]];
        }
        return nodes[0];
    }

    // 验证深拷贝：长度一致、节点全新、val 相等、next/random 映射关系与原链表一致
    static boolean verify(Node orig, Node copy) {
        if (orig == null) return copy == null;
        Map<Node, Integer> idx = new HashMap<>();
        List<Node> origNodes = new ArrayList<>();
        Node p = orig;
        while (p != null) { idx.put(p, origNodes.size()); origNodes.add(p); p = p.next; }
        List<Node> copyNodes = new ArrayList<>();
        p = copy;
        while (p != null) { copyNodes.add(p); p = p.next; }
        if (copyNodes.size() != origNodes.size()) return false;
        for (int i = 0; i < origNodes.size(); i++) {
            Node o = origNodes.get(i), c = copyNodes.get(i);
            if (c == o) return false;              // 深拷贝：不能复用原节点
            if (c.val != o.val) return false;
            Node or = o.random;
            Node cr = c.random;
            if (or == null) {
                if (cr != null) return false;
            } else {
                if (cr != copyNodes.get(idx.get(or))) return false;  // random 必须指向复制链表对应位置
            }
        }
        return true;
    }

}