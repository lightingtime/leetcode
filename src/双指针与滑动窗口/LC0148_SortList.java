// ============================================================
// LeetCode 148. 排序链表 (Sort List)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/sort-list/
// 刷题日期：2026-08-30（二刷 · 一刷 2026-08-05，一刷一次 AC 但空间 O(log n) 未达进阶；测试用例与一刷归档保持一致）
//
// 思路：自顶向下归并——快慢指针找中点切分，两半递归排序后合并
// 复杂度：时间 O(n log n) 空间 O(log n)（递归栈，进阶自底向上可 O(1)）
// ============================================================

import java.util.*;

public class LC0148_SortList {

    // ==== 提交代码开始 ====
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        // 两半必须各自递归排序后再合并（此前漏掉分治，直接 merge 两个乱序序列）
        ListNode mid = getMid(head);
        ListNode second = mid.next;
        mid.next = null;
        ListNode newHead = sortList(head);
        second = sortList(second);
        return mergeList(newHead, second);
    }

    private ListNode mergeList(ListNode head, ListNode second) {
        ListNode newHead = new ListNode();
        ListNode p1 = head, p2 = second, p = newHead;
        // 只把 p 指向较小节点、p 前移，不断任何链（断链会拆碎链表并让 p 变 null 触发 NPE）
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
        if (p1 != null) p.next = p1;
        if (p2 != null) p.next = p2;
        return newHead.next;
    }

    private ListNode getMid(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        // fast 每轮完整走 2 步、slow 走 1 步，fast 到尾时 slow 恰在中点
        // 条件 fast.next.next != null 保证「slow 前进 ⇔ fast 走满 2 步」（此前偶数长度 slow 多走，[1,2] 返回节点 2 致无限递归）
        ListNode fast = head, slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
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