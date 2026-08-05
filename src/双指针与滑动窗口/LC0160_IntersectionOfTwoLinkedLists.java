// ============================================================
// LeetCode 160. 相交链表 (Intersection of Two Linked Lists)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/intersection-of-two-linked-lists/
// 刷题日期：2026-08-04
//
// ============================================================

import java.util.*;

public class LC0160_IntersectionOfTwoLinkedLists {

    // ==== 提交代码开始 ====
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        ListNode p1 = headA, p2 = headB;
        while (p1 != p2) {
            if (p1 != null) {
                p1 = p1.next;
            } else {
                p1 = headB;
            }
            if (p2 != null) {
                p2 = p2.next;
            } else {
                p2 = headA;
            }
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
