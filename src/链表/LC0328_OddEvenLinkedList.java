// ============================================================
// LeetCode 328. 奇偶链表 (Odd Even Linked List)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/odd-even-linked-list/
// 复习日期：2026-09-17（第 3 次复习 · 一刷 2026-08-24）
// 一刷写法：先写「双哨兵拆链」（写法1）出错，改「原地交替」odd/even 各跳 next.next 重连（写法2）才 AC，O(n) / O(1)
// 一刷问题：链表拆链时边遍历边覆盖原 next、被拆节点未断开，会在奇偶交替处成环（偶数长度碰巧通过、奇数长度 OOM）
// 上次复习写法：仍用原地交替（odd/even 尾指针重连 + odd.next = evenHead），一次 AC
// 测试用例：与一刷归档保持一致（示例 2 + 边界 6，含 10000 节点）
// ============================================================

import java.util.*;

public class LC0328_OddEvenLinkedList {

    // ==== 提交代码开始 ====
    public ListNode oddEvenList(ListNode head) {
        if (head == null) {
            return head;
        }

        ListNode evenHead = head.next;
        ListNode odd = head, even = evenHead;
        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next =  odd.next;
            even = even.next;
        }
        odd.next = evenHead;
        return head;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0328_OddEvenLinkedList s = new LC0328_OddEvenLinkedList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(1, 3, 5, 2, 4), s.oddEvenList(listNode(1, 2, 3, 4, 5)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(2, 3, 6, 7, 1, 5, 4), s.oddEvenList(listNode(2, 1, 3, 5, 6, 4, 7)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 约束：0 <= n <= 10^4，-10^6 <= val <= 10^6，O(1) 空间 O(n) 时间
        // 边界1: 空链表
        try {
            if (!checkEq(null, s.oddEvenList(null), "边界1: 空链表")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 单节点
        try {
            if (!checkEq(listNode(1), s.oddEvenList(listNode(1)), "边界2: 单节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 两节点（奇数段=1个、偶数段=1个）
        try {
            if (!checkEq(listNode(1, 2), s.oddEvenList(listNode(1, 2)), "边界3: 两节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 三节点（奇数段比偶数段多 1）
        try {
            if (!checkEq(listNode(1, 3, 2), s.oddEvenList(listNode(1, 2, 3)), "边界4: 三节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 四节点（两段等长）
        try {
            if (!checkEq(listNode(1, 3, 2, 4), s.oddEvenList(listNode(1, 2, 3, 4)), "边界5: 四节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: n=10^4 上限，1..10000 → 奇数位升序接偶数位升序
        try {
            ListNode big = new ListNode(1), cur = big;
            for (int i = 2; i <= 10000; i++) { cur.next = new ListNode(i); cur = cur.next; }
            ListNode res = s.oddEvenList(big);
            boolean ok = true;
            int idx = 0;
            for (ListNode c = res; c != null; c = c.next) {
                int expect = idx < 5000 ? 2 * idx + 1 : 2 * (idx - 5000) + 2;
                if (c.val != expect) ok = false;
                idx++;
            }
            if (idx != 10000) ok = false;
            if (!checkEq(true, ok, "边界6: 10000节点奇偶分组")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

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
