// ============================================================
// LeetCode 876. 链表的中间结点 (Middle of the Linked List)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/middle-of-the-linked-list/
// 刷题日期：2026-09-30
//
// 思路：用慢指针每轮前进一步、快指针每轮最多前进两步；快指针到尾时慢指针在中间。
// 复杂度：时间 O(n)，空间 O(1)
// ============================================================

import java.util.*;

public class LC0876_MiddleOfTheLinkedList {

    // ==== 提交代码开始 ====
    public ListNode middleNode(ListNode head) {
        ListNode dummy = new ListNode(-1, head);
        ListNode slow = dummy, fast = dummy;
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
            if (fast != null) {
                fast = fast.next;
            }
        }
        return slow;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0876_MiddleOfTheLinkedList s = new LC0876_MiddleOfTheLinkedList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(3, 4, 5), s.middleNode(listNode(1, 2, 3, 4, 5)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(4, 5, 6), s.middleNode(listNode(1, 2, 3, 4, 5, 6)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（结点数约束为 [1, 100]）----
        try {
            if (!checkEq(listNode(7), s.middleNode(listNode(7)), "单节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单节点 异常: " + t); }
        try {
            if (!checkEq(listNode(20), s.middleNode(listNode(10, 20)), "偶数最短链表返回第二个节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("偶数最短链表 异常: " + t); }
        try {
            if (!checkEq(rangeListNode(50, 99), s.middleNode(rangeListNode(0, 99)), "最大长度100")) failures++;
        } catch (Throwable t) { failures++; System.out.println("最大长度100 异常: " + t); }

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

    static ListNode rangeListNode(int start, int end) {
        ListNode dummy = new ListNode(0), cur = dummy;
        for (int value = start; value <= end; value++) {
            cur.next = new ListNode(value);
            cur = cur.next;
        }
        return dummy.next;
    }

}
