// ============================================================
// LeetCode 2. 两数相加 (Add Two Numbers)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/add-two-numbers/
// 刷题日期：2026-08-06
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0002_AddTwoNumbers {

    // ==== 提交代码开始 ====
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        // TODO: 在这里实现你的解法
        ListNode dummy = new ListNode();
        ListNode p1 = l1, p2 = l2, p = dummy;
        int next = 0;
        while (p1 != null || p2 != null || next != 0) {
            int a = p1 == null ? 0 : p1.val;
            int b = p2 == null ? 0 : p2.val;
            int sum = a + b + next;
            ListNode node = new ListNode(sum % 10);
            next = sum / 10;
            p.next = node;
            p = p.next;
            p1 = p1 == null ? p1 : p1.next;
            p2 = p2 == null ? p2 : p2.next;
        }
        return dummy.next;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0002_AddTwoNumbers s = new LC0002_AddTwoNumbers();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(7, 0, 8), s.addTwoNumbers(listNode(2, 4, 3), listNode(5, 6, 4)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(0), s.addTwoNumbers(listNode(0), listNode(0)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(listNode(8, 9, 9, 9, 0, 0, 0, 1), s.addTwoNumbers(listNode(9, 9, 9, 9, 9, 9, 9), listNode(9, 9, 9, 9)), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!checkEq(listNode(1, 1), s.addTwoNumbers(listNode(5), listNode(6)), "边界-单元素进位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单元素进位 异常: " + t); }
        try {
            if (!checkEq(listNode(0, 0, 1), s.addTwoNumbers(listNode(1), listNode(9, 9)), "边界-长度不同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-长度不同 异常: " + t); }
        try {
            if (!checkEq(listNode(8, 9, 1), s.addTwoNumbers(listNode(9, 9), listNode(9, 9)), "边界-连续进位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-连续进位 异常: " + t); }
        try {
            if (!checkEq(listNode(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1), s.addTwoNumbers(listNode(9, 9, 9, 9, 9, 9, 9, 9, 9, 9), listNode(1)), "边界-结果多一位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-结果多一位 异常: " + t); }
        try {
            if (!checkEq(listNode(0, 1, 1, 1, 1), s.addTwoNumbers(listNode(9, 9, 9, 9), listNode(1, 1, 1, 1)), "边界-大数混合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-大数混合 异常: " + t); }

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
