// ============================================================
// LeetCode 19. 删除链表的倒数第 N 个结点 (Remove Nth Node From End of List)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
// 复习日期：2026-09-16（第 1 次复习 · 一刷 2026-08-05）
// 一刷写法：哨兵 + 快慢指针拉开 N 步（O(n)/O(1)）
// 上次遗留提醒（本次要求达到）：写法未达最精简——① slow 指针冗余（pre.next 恒等于待删节点，可直接 pre.next = pre.next.next）；
// ② 快指针走 N 步的 while + i++ 可换成 for 循环
// 测试用例：示例 3 个 + 边界 7 个（原有 5 个，本次补齐：长度 30 上限删中间 / 全零值长度 30 删头）
// ============================================================

import java.util.*;

public class LC0019_RemoveNthNodeFromEndOfList {

    // ==== 提交代码开始 ====
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);
        ListNode slow = head, p = dummy, fast = head;
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
            p = p.next;
        }
        p.next = p.next.next;
        return dummy.next;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0019_RemoveNthNodeFromEndOfList s = new LC0019_RemoveNthNodeFromEndOfList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(1, 2, 3, 5), s.removeNthFromEnd(listNode(1, 2, 3, 4, 5), 2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(), s.removeNthFromEnd(listNode(1), 1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(listNode(1), s.removeNthFromEnd(listNode(1, 2), 1), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（题型特有：删头/尾/中间、奇偶长度、全相同、长链表）----
        try {
            if (!checkEq(listNode(2, 3), s.removeNthFromEnd(listNode(1, 2, 3), 3), "边界1-删头结点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-删头结点 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 4), s.removeNthFromEnd(listNode(1, 2, 3, 4), 2), "边界2-偶数长度删中间")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-偶数长度删中间 异常: " + t); }
        try {
            if (!checkEq(listNode(5, 5, 5), s.removeNthFromEnd(listNode(5, 5, 5, 5), 2), "边界3-全相同值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-全相同值 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3, 4), s.removeNthFromEnd(listNode(1, 2, 3, 4, 5), 1), "边界4-删尾结点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-删尾结点 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3, 4, 5, 6, 7, 8, 9), s.removeNthFromEnd(listNode(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 1), "边界5-长链表删尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-长链表删尾 异常: " + t); }
        try {
            Integer[] vals = new Integer[30], expect = new Integer[29];
            for (int i = 0; i < 30; i++) vals[i] = i + 1;
            for (int i = 0, k = 0; i < 30; i++) if (i != 15) expect[k++] = i + 1;   // 倒数第 15 个 = 正数第 16 个
            if (!checkEq(listNode(expect), s.removeNthFromEnd(listNode(vals), 15), "边界6-长度 30 上限删中间")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            Integer[] vals = new Integer[30], expect = new Integer[29];
            for (int i = 0; i < 30; i++) { vals[i] = 0; if (i < 29) expect[i] = 0; }
            if (!checkEq(listNode(expect), s.removeNthFromEnd(listNode(vals), 30), "边界7-全零值长度 30 删头")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

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
