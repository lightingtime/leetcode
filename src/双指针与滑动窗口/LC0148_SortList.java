// ============================================================
// LeetCode 148. 排序链表 (Sort List)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/sort-list/
// 复习日期：2026-09-06（第 4 次复习 · 一刷 2026-08-05 · 上次 2026-09-01 较强）
// 一刷思路：自底向上归并 O(1) 空间（subLen 翻倍取段 + merge）；上次踩坑：段长 i++ 非翻倍、i<<=2 误乘4
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0148_SortList {

    // ==== 提交代码开始 ====
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        int maxLen = getLen(head);
        ListNode dummy = new ListNode(0, head);
        for (int i = 1; i <= maxLen; i <<= 1) {
            ListNode pre = dummy, cur = dummy.next;
            while (cur != null) {
                ListNode head1 = cur;
                for (int j = 1; j < i && cur.next != null; j++) {
                    cur = cur.next;
                }
                ListNode head2 = cur.next;
                cur.next = null;
                cur = head2;
                for (int j = 1; j < i && cur != null && cur.next != null; j++) {
                    cur = cur.next;
                }
                ListNode next = null;
                if (cur != null) {
                    next = cur.next;
                    cur.next = null;
                }
                pre.next = merge(head1, head2);
                while (pre.next != null) {
                    pre = pre.next;
                }
                cur = next;
            }
        }
        return dummy.next;
    }

    private ListNode merge(ListNode head1, ListNode head2) {
        ListNode dummy = new ListNode();
        ListNode p1 = head1, p2 = head2, p = dummy;
        while (p1 != null && p2 != null) {
            if (p1.val < p2.val) {
                p.next = p1;
                p1 = p1.next;
            } else {
                p.next = p2;
                p2 = p2.next;
            }
            p = p.next;
        }
        if (p1 != null) {
            p.next = p1;
        }
        if (p2 != null) {
            p.next = p2;
        }
        return dummy.next;
    }

    private int getLen(ListNode head) {
        int count = 0;
        ListNode p = head;
        while (p != null) {
            count++;
            p = p.next;
        }
        return count;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0148_SortList s = new LC0148_SortList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(1, 2, 3, 4), s.sortList(listNode(4, 2, 1, 3)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(-1, 0, 3, 4, 5), s.sortList(listNode(-1, 5, 3, 4, 0)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(listNode(), s.sortList(listNode()), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（链表排序：单元素/两元素/已排序/全相同/奇数长度/大数）----
        try {
            if (!checkEq(listNode(1), s.sortList(listNode(1)), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单元素 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2), s.sortList(listNode(2, 1)), "边界2-两元素逆序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-两元素逆序 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3, 4), s.sortList(listNode(1, 2, 3, 4)), "边界3-已排序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-已排序 异常: " + t); }
        try {
            if (!checkEq(listNode(5, 5, 5, 5), s.sortList(listNode(5, 5, 5, 5)), "边界4-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-全相同 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3), s.sortList(listNode(3, 2, 1)), "边界5-奇数长度逆序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-奇数长度逆序 异常: " + t); }
        try {
            if (!checkEq(listNode(-100000, 0, 100000), s.sortList(listNode(100000, -100000, 0)), "边界6-大数混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-大数混合 异常: " + t); }

        // ---- 回归测试（力扣 WA 根因：段长未翻倍，n=9 时右段跨块无序导致 merge 错）----
        try {
            if (!checkEq(listNode(-50, -45, -45, -41, -37, 5, 37, 44, 44), s.sortList(listNode(44, 5, -37, -45, -50, 37, -45, 44, -41)), "回归-段长不翻倍")) failures++;
        } catch (Throwable t) { failures++; System.out.println("回归-段长不翻倍 异常: " + t); }

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