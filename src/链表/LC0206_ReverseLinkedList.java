// ============================================================
// LeetCode 206. 反转链表 (Reverse Linked List)
// 难度：Easy | 分类：链表
// 链接：https://leetcode.cn/problems/reverse-linked-list/
// 复习日期：2026-08-31（复习 · 一刷 2026-08-06，上次复习 2026-08-30）
// 一刷思路：迭代三指针 pre/cur/next，O(n)/O(1)
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0206_ReverseLinkedList {

    // ==== 提交代码开始 ====
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = null, cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0206_ReverseLinkedList s = new LC0206_ReverseLinkedList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(5, 4, 3, 2, 1), s.reverseList(listNode(1, 2, 3, 4, 5)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(2, 1), s.reverseList(listNode(1, 2)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(listNode(), s.reverseList(listNode()), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!checkEq(listNode(1), s.reverseList(listNode(1)), "边界-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素 异常: " + t); }
        try {
            if (!checkEq(listNode(4, 3, 2, 1), s.reverseList(listNode(1, 2, 3, 4)), "边界-偶数长度")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-偶数长度 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 1, 1, 1), s.reverseList(listNode(1, 1, 1, 1)), "边界-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同 异常: " + t); }
        try {
            if (!checkEq(listNode(-3, -2, -1), s.reverseList(listNode(-1, -2, -3)), "边界-负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-负数 异常: " + t); }
        try {
            if (!checkEq(listNode(1000000, -1000000), s.reverseList(listNode(-1000000, 1000000)), "边界-极端大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-极端大数 异常: " + t); }

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

}
