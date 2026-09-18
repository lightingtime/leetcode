// ============================================================
// LeetCode 160. 相交链表 (Intersection of Two Linked Lists)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/intersection-of-two-linked-lists/
// 复习日期：2026-09-18（第 2 次复习 · 一刷 2026-08-04 · 一刷一次 Accepted）
// 一刷写法：双指针换链——两个指针各走完自己那条链后切到对方链头，走满 m+n 步后同相位；有交点必相遇，无交点同时落到 null。O(m+n)/O(1)
// 这题的历史坑（错误习惯库有记录，上一轮复习还犯过一次）：① 换链要按「指针自己走到 null」切到另一条链头，写成「next == null 时切」会相位错乱甚至死循环；② 每轮先判 p1 == p2 再移动，先走一步再比会漏掉「交点正好在链头」；③ 判空之前别解引用 p.next
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 7 个）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0160_IntersectionOfTwoLinkedLists {

    // ==== 提交代码开始 ====
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode p1 = headA, p2 = headB;
        while (p1 != p2) {
            p1 = p1 != null ? p1.next : headB;
            p2 = p2 != null ? p2.next : headA;
        }
        return p1;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0160_IntersectionOfTwoLinkedLists s = new LC0160_IntersectionOfTwoLinkedLists();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            ListNode[] p1 = intersectList(new Object[]{4,1,8,4,5}, 2, new Object[]{5,6,1,8,4,5}, 3);
            if (!checkEq(p1[0].next.next, s.getIntersectionNode(p1[0], p1[1]), "示例1 相交于 8")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            ListNode[] p2 = intersectList(new Object[]{1,9,1,2,4}, 3, new Object[]{3,2,4}, 1);
            if (!checkEq(p2[0].next.next.next, s.getIntersectionNode(p2[0], p2[1]), "示例2 相交于 2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            ListNode[] p3 = intersectList(new Object[]{2,6,4}, 3, new Object[]{1,5}, 2);
            if (!checkEq(null, s.getIntersectionNode(p3[0], p3[1]), "示例3 无交点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!checkEq(null, s.getIntersectionNode(null, null), "边界1 双空")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!checkEq(null, s.getIntersectionNode(listNode(1, 2, 3), null), "边界2 单空")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            ListNode[] p = intersectList(new Object[]{1, 2}, 2, new Object[]{3, 4}, 2);
            if (!checkEq(null, s.getIntersectionNode(p[0], p[1]), "边界3 等长不相交")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            ListNode[] p = intersectList(new Object[]{1, 2, 3}, 0, new Object[]{1, 2, 3}, 0);
            if (!checkEq(p[0], s.getIntersectionNode(p[0], p[1]), "边界4 整条链共享，交于两链头")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            ListNode[] p = intersectList(new Object[]{9, 1, 2}, 1, new Object[]{1, 2}, 0);
            if (!checkEq(p[1], s.getIntersectionNode(p[0], p[1]), "边界5 交于短链头（A 是 B 的延伸）")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            Object[] longA = new Object[60];
            for (int i = 0; i < longA.length; i++) longA[i] = i;
            longA[59] = 7;                       // 最后一个节点与 B 共享
            ListNode[] p = intersectList(longA, 59, new Object[]{7}, 0);
            if (!checkEq(p[1], s.getIntersectionNode(p[0], p[1]), "边界6 长度差 59，共享尾节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            Object[] longA = new Object[60];
            for (int i = 0; i < longA.length; i++) longA[i] = i;
            ListNode[] p = intersectList(longA, 60, new Object[]{1, 2, 3}, 3);
            if (!checkEq(null, s.getIntersectionNode(p[0], p[1]), "边界7 长度差 57 且不相交")) failures++;
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

    // 构造两条可能相交的链表（与力扣输入格式一致）：
    // listA 前 skipA 个节点之后接入公共尾部，listB 前 skipB 个节点之后接入同一公共尾部
    static ListNode[] intersectList(Object[] aVals, int skipA, Object[] bVals, int skipB) {
        ListNode common = listNode(Arrays.copyOfRange(aVals, skipA, aVals.length));
        ListNode headA = listNode(Arrays.copyOfRange(aVals, 0, skipA));
        ListNode headB = listNode(Arrays.copyOfRange(bVals, 0, skipB));
        if (headA == null) {
            headA = common;
        } else {
            ListNode cur = headA;
            while (cur.next != null) cur = cur.next;
            cur.next = common;
        }
        if (headB == null) {
            headB = common;
        } else {
            ListNode cur = headB;
            while (cur.next != null) cur = cur.next;
            cur.next = common;
        }
        return new ListNode[]{ headA, headB };
    }

}
