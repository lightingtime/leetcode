// ============================================================
// LeetCode 21. 合并两个有序链表 (Merge Two Sorted Lists)
// 难度：Easy | 分类：链表
// 链接：https://leetcode.cn/problems/merge-two-sorted-lists/
// 刷题日期：2026-08-06
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0021_MergeTwoSortedLists {

    // ==== 提交代码开始 ====
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // TODO: 在这里实现你的解法
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        ListNode dummy = new ListNode();
        ListNode p1 = list1, p2 = list2, p = dummy;
        while (p1 != null && p2 != null) {
            if (p1.val < p2.val) {
                ListNode node = new ListNode(p1.val);
                p.next = node;
                p1 = p1.next;
            } else {
                ListNode node = new ListNode(p2.val);
                p.next = node;
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
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0021_MergeTwoSortedLists s = new LC0021_MergeTwoSortedLists();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(1, 1, 2, 3, 4, 4), s.mergeTwoLists(listNode(1, 2, 4), listNode(1, 3, 4)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(), s.mergeTwoLists(listNode(), listNode()), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(listNode(0), s.mergeTwoLists(listNode(), listNode(0)), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!checkEq(listNode(1, 2), s.mergeTwoLists(listNode(1), listNode(2)), "边界-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3), s.mergeTwoLists(listNode(1, 2, 3), listNode()), "边界-一边为空(左长)")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-一边为空(左长) 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 1, 1, 1, 1), s.mergeTwoLists(listNode(1, 1, 1), listNode(1, 1)), "边界-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3, 4, 5), s.mergeTwoLists(listNode(1), listNode(2, 3, 4, 5)), "边界-长度悬殊")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长度悬殊 异常: " + t); }
        try {
            if (!checkEq(listNode(-5, -4, -3, -2), s.mergeTwoLists(listNode(-5, -3), listNode(-4, -2)), "边界-负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-负数 异常: " + t); }
        try {
            if (!checkEq(listNode(-100, -1, 1, 100), s.mergeTwoLists(listNode(-100, 100), listNode(-1, 1)), "边界-极端大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-极端大数 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3, 4, 5, 6), s.mergeTwoLists(listNode(1, 3, 5), listNode(2, 4, 6)), "边界-交错升序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-交错升序 异常: " + t); }

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
