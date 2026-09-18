// ============================================================
// LeetCode 23. 合并 K 个升序链表 (Merge k Sorted Lists)
// 难度：Hard | 分类：链表
// 链接：https://leetcode.cn/problems/merge-k-sorted-lists/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-07 · 一刷一次 Accepted）
// 一刷写法：分治两两合并——每轮把相邻两条链两两合并，链表数量减半，重复到只剩一条；每层总共只走 N 个节点，共 log k 层。O(N log k)/O(log k)（递归栈）
// 本题易错点：① 不用堆也能做到 O(N log k)，关键在「每轮两两合并」而不是「每次从头扫 k 条链取最小」（后者是 O(N·k)）；② 递归出口要同时管住 start > end（空区间）与 start == end（单链直接返回）；③ 本轮合并结果要写回 lists[i]，下一轮读的是合并后的链；④ 一刷的合并子过程每次 new 节点复制值，可改成原地复用节点改 next，省掉全部分配
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 6 个：单条单节点链/空链混入/全相同/负数与大数/单条长链夹短链/多条单节点链）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0023_MergeKSortedLists {

    // ==== 提交代码开始 ====
    public ListNode mergeKLists(ListNode[] lists) {
        return mergeKLists(lists, 0, lists.length - 1);
    }

    private ListNode mergeKLists(ListNode[] lists, int start, int end) {
        if (start >= lists.length) {
            return null;
        }
        if (start == end) {
            return lists[start];
        }
        int mid = start + (end - start) / 2;
        ListNode first = mergeKLists(lists, start, mid);
        ListNode second = mergeKLists(lists, mid + 1, end);
        return mergeTowList(first, second);
    }

    private ListNode mergeTowList(ListNode first, ListNode second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        }
        ListNode dummy = new ListNode();
        ListNode p1 = first, p2 = second, p = dummy;
        while (p1 != null && p2 != null) {
            ListNode node = new ListNode();
            if (p1.val > p2.val) {
                node.val = p2.val;
                p2 = p2.next;
            } else {
                node.val = p1.val;
                p1 = p1.next;
            }
            p.next = node;
            p = p.next;
        }
        if (p1 == null) {
            p.next = p2;
        }
        if (p2 == null) {
            p.next = p1;
        }
        return dummy.next;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0023_MergeKSortedLists s = new LC0023_MergeKSortedLists();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(1, 1, 2, 3, 4, 4, 5, 6), s.mergeKLists(new ListNode[]{listNode(1, 4, 5), listNode(1, 3, 4), listNode(2, 6)}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(), s.mergeKLists(new ListNode[0]), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(listNode(), s.mergeKLists(new ListNode[]{listNode()}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试 ----
        try {
            if (!checkEq(listNode(5), s.mergeKLists(new ListNode[]{listNode(5)}), "边界1-单条单节点链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3), s.mergeKLists(new ListNode[]{listNode(1, 2, 3), null}), "边界2-空链混入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 1, 1, 1, 1), s.mergeKLists(new ListNode[]{listNode(1, 1), listNode(1, 1), listNode(1)}), "边界3-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!checkEq(listNode(-10000, -1, 0, 10000), s.mergeKLists(new ListNode[]{listNode(-10000, 0), listNode(-1, 10000)}), "边界4-负数与大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3, 4, 5, 6, 7, 8, 9), s.mergeKLists(new ListNode[]{listNode(2, 3, 4, 5, 6, 7, 8), listNode(1), listNode(9)}), "边界5-单条长链夹短链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!checkEq(listNode(1, 2, 3, 4, 5, 6, 7, 8), s.mergeKLists(new ListNode[]{listNode(8), listNode(3), listNode(1), listNode(6), listNode(2), listNode(7), listNode(4), listNode(5)}), "边界6-多条单节点链")) failures++;
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
