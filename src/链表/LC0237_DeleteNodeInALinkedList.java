// ============================================================
// LeetCode 237. 删除链表中的节点 (Delete Node in a Linked List)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/delete-node-in-a-linked-list/
// 刷题日期：2026-08-24
//
// ============================================================

import java.util.*;

public class LC0237_DeleteNodeInALinkedList {

    // ==== 提交代码开始 ====
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0237_DeleteNodeInALinkedList s = new LC0237_DeleteNodeInALinkedList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        // 示例1: [4,5,1,9] 删 5 → [4,1,9]
        try {
            ListNode h = listNode(4, 5, 1, 9);
            s.deleteNode(findNode(h, 5));
            if (!checkEq(listNode(4, 1, 9), h, "示例1: 删第二个节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        // 示例2: [4,5,1,9] 删 1 → [4,5,9]
        try {
            ListNode h = listNode(4, 5, 1, 9);
            s.deleteNode(findNode(h, 1));
            if (!checkEq(listNode(4, 5, 9), h, "示例2: 删倒数第二个节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 约束：2 <= n <= 1000，值唯一，node 非末尾
        // 边界1: 两节点删头节点 [1,2] → [2]（node 是 head 且是倒数第二个）
        try {
            ListNode h = listNode(1, 2);
            s.deleteNode(findNode(h, 1));
            if (!checkEq(listNode(2), h, "边界1: 两节点删头")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 删中间节点 [1,2,3] 删 2 → [1,3]
        try {
            ListNode h = listNode(1, 2, 3);
            s.deleteNode(findNode(h, 2));
            if (!checkEq(listNode(1, 3), h, "边界2: 删中间节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 删值取边界 -1000 / 1000，[−1000,7,1000] 删 -1000 → [7,1000]
        try {
            ListNode h = listNode(-1000, 7, 1000);
            s.deleteNode(findNode(h, -1000));
            if (!checkEq(listNode(7, 1000), h, "边界3: 删边界值节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: n=1000 上限删中间，验证长度 999、被删值消失、前后顺序保持
        try {
            ListNode big = new ListNode(1), cur = big;
            for (int i = 2; i <= 1000; i++) { cur.next = new ListNode(i); cur = cur.next; }
            s.deleteNode(findNode(big, 500));
            boolean ok = true;
            int len = 0, prev = 0;
            for (ListNode c = big; c != null; c = c.next) {
                if (c.val == 500) ok = false;
                if (c.val < 1 || c.val > 1000) ok = false;
                if (c.val <= prev) ok = false;  // 严格递增即顺序保持（删除后允许跳值）
                prev = c.val;
                len++;
            }
            if (len != 999) ok = false;
            if (!checkEq(true, ok, "边界4: 1000节点删500")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

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
        if (o instanceof ListNode) {
            StringBuilder sb = new StringBuilder();
            for (ListNode c = (ListNode) o; c != null; c = c.next) {
                if (sb.length() > 0) sb.append(",");
                sb.append(c.val);
            }
            return sb.length() == 0 ? "null" : sb.toString();
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

    static ListNode listNode(Object... vals) {
        if (vals.length == 0 || vals[0] == null) return null;
        ListNode dummy = new ListNode(0), cur = dummy;
        for (Object v : vals) {
            if (v == null) continue;
            cur.next = new ListNode(((Number) v).intValue());
            cur = cur.next;
        }
        return dummy.next;
    }

    // 值唯一，按值定位节点
    static ListNode findNode(ListNode head, int val) {
        for (ListNode c = head; c != null; c = c.next) {
            if (c.val == val) return c;
        }
        return null;
    }

}