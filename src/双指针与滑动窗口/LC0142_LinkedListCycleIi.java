// ============================================================
// LeetCode 142. 环形链表 II (Linked List Cycle II)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/linked-list-cycle-ii/
// 刷题日期：2026-08-11
// ============================================================

import java.util.*;

public class LC0142_LinkedListCycleIi {

    // ==== 提交代码开始 ====
    public ListNode detectCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
            if (fast != null) {
                fast = fast.next;
            }
            if (fast == slow) {
                break;
            }
        }
        if (fast == null) {
            return fast;
        }
        slow = head;
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        return fast;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0142_LinkedListCycleIi s = new LC0142_LinkedListCycleIi();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            ListNode[] a = cycleList(new int[]{3, 2, 0, -4}, 1);
            if (!checkNodeEq(a[1], s.detectCycle(a[0]), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            ListNode[] a = cycleList(new int[]{1, 2}, 0);
            if (!checkNodeEq(a[1], s.detectCycle(a[0]), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            ListNode[] a = cycleList(new int[]{1}, -1);
            if (!checkNodeEq(null, s.detectCycle(a[0]), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            ListNode[] a = cycleList(new int[]{1}, 0);
            if (!checkNodeEq(a[1], s.detectCycle(a[0]), "单节点自环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("单节点自环 异常: " + t); }
        try {
            ListNode[] a = cycleList(new int[]{1, 2, 3}, 2);
            if (!checkNodeEq(a[1], s.detectCycle(a[0]), "尾节点自环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("尾节点自环 异常: " + t); }
        try {
            ListNode[] a = cycleList(new int[]{1, 2}, 1);
            if (!checkNodeEq(a[1], s.detectCycle(a[0]), "两节点环入口在尾")) failures++;
        } catch (Throwable t) { failures++; System.out.println("两节点环入口在尾 异常: " + t); }
        try {
            ListNode[] a = cycleList(new int[]{1, 2, 3, 4}, -1);
            if (!checkNodeEq(null, s.detectCycle(a[0]), "无环多节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("无环多节点 异常: " + t); }
        try {
            ListNode[] a = cycleList(new int[]{}, -1);
            if (!checkNodeEq(null, s.detectCycle(a[0]), "空链表")) failures++;
        } catch (Throwable t) { failures++; System.out.println("空链表 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 构造带环链表，返回 [head, 环入口节点]（pos=-1 表示无环）
    static ListNode[] cycleList(int[] vals, int pos) {
        if (vals.length == 0) return new ListNode[]{null, null};
        ListNode dummy = new ListNode(0), cur = dummy;
        ListNode entry = null;
        for (int i = 0; i < vals.length; i++) {
            cur.next = new ListNode(vals[i]);
            cur = cur.next;
            if (i == pos) entry = cur;
        }
        if (pos >= 0) cur.next = entry;
        return new ListNode[]{dummy.next, entry};
    }

    // 节点引用比较（带环链表不能走 norm 序列化，会死循环）
    static boolean checkNodeEq(ListNode expected, ListNode actual, String label) {
        if (expected != actual) {
            System.out.println(label + " 失败 ✗ 期望=" + (expected == null ? "null" : String.valueOf(expected.val))
                    + " 实际=" + (actual == null ? "null" : String.valueOf(actual.val)));
            return false;
        }
        System.out.println(label + " 通过 ✓");
        return true;
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
