// ============================================================
// LeetCode 86. 分隔链表 (Partition List)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/partition-list/
// 刷题日期：2026-09-28
//
// ============================================================

import java.util.*;

public class LC0086_PartitionList {

    // ==== 提交代码开始 ====
    public ListNode partition(ListNode head, int x) {
        ListNode dummy1 = new ListNode(-1);
        ListNode dummy2 = new ListNode(-1);
        ListNode p1 = dummy1, p2 = dummy2, p = head;
        while (p != null) {
            if (p.val < x) {
                p1.next = p;
                p1 = p1.next;
            } else {
                p2.next = p;
                p2 = p2.next;
            }
            p = p.next;
        }
        p2.next = null;
        p1.next = dummy2.next;
        return dummy1.next;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0086_PartitionList s = new LC0086_PartitionList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(1, 2, 2, 4, 3, 5), s.partition(listNode(1, 4, 3, 2, 5, 2), 3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2), s.partition(listNode(2, 1), 2), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（覆盖空链表、单节点、相等边界、稳定性与值域端点）----
        try {
            if (!checkEq(null, s.partition(null, 0), "空链表")) failures++;
        } catch (Throwable t) { failures++; System.out.println("空链表 异常: " + t); }
        try {
            if (!checkEq(listNode(-100), s.partition(listNode(-100), 0), "单节点小于x")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单节点小于x 异常: " + t); }
        try {
            if (!checkEq(listNode(100), s.partition(listNode(100), 100), "单节点等于x")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单节点等于x 异常: " + t); }
        try {
            if (!checkEq(listNode(100, 100, 100), s.partition(listNode(100, 100, 100), 100), "全部等于x")) failures++;
        } catch (Throwable t) { failures++; System.out.println("全部等于x 异常: " + t); }
        try {
            if (!checkEq(listNode(-100, -1, 0, 100), s.partition(listNode(0, -100, 100, -1), 0), "分区内相对顺序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("分区内相对顺序 异常: " + t); }

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
