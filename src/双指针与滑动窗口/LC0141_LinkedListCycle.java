// ============================================================
// LeetCode 141. 环形链表 (Linked List Cycle)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/linked-list-cycle/
// 复习日期：2026-09-16（第 1 次复习 · 一刷 2026-08-04）
// 一刷写法：Floyd 快慢指针判圈 O(n)/O(1)
// 测试用例：示例 3 个（题目自带）+ 边界 7 个（一刷归档缺边界，本次补齐）
// ============================================================


import java.util.*;

public class LC0141_LinkedListCycle {

    // ==== 提交代码开始 ====
    public boolean hasCycle(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
            if (fast != null) {
                fast = fast.next;
            }
            if (fast == slow) {
                break;
            }
        }
        return fast != null;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0141_LinkedListCycle s = new LC0141_LinkedListCycle();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(true, s.hasCycle(cycleList(1, 3, 2, 0, -4)), "示例1 [3,2,0,-4] pos=1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(true, s.hasCycle(cycleList(0, 1, 2)), "示例2 [1,2] pos=0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkEq(false, s.hasCycle(cycleList(-1, 1)), "示例3 [1] pos=-1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试 ----
        try { if (!checkEq(false, s.hasCycle(null), "边界1-空链表")) failures++; }
        catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try { if (!checkEq(false, s.hasCycle(cycleList(-1, 7)), "边界2-单节点无环")) failures++; }
        catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try { if (!checkEq(true, s.hasCycle(cycleList(0, 7)), "边界3-单节点自环")) failures++; }
        catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try { if (!checkEq(true, s.hasCycle(cycleList(2, 1, 2, 3)), "边界4-尾节点自指")) failures++; }
        catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try { if (!checkEq(false, s.hasCycle(cycleList(-1, -100000, 100000)), "边界5-极值节点值无环")) failures++; }
        catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            ListNode head = new ListNode(0), cur = head;
            for (int i = 1; i < 10000; i++) { cur.next = new ListNode(i); cur = cur.next; }
            if (!checkEq(false, s.hasCycle(head), "边界6-长度 10000 无环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            ListNode head = new ListNode(0), cur = head;
            for (int i = 1; i < 10000; i++) { cur.next = new ListNode(i); cur = cur.next; }
            cur.next = head;   // 整条链成环，回到头节点
            if (!checkEq(true, s.hasCycle(head), "边界7-长度 10000 环回头节点")) failures++;
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

    // 构造链表：最后一个参数是 pos（-1 = 无环），前面的都是节点值
    static ListNode cycleList(int pos, Object... vals) {
        if (vals.length == 0 || vals[0] == null) return null;
        ListNode head = listNode(vals);
        if (pos < 0) return head;
        ListNode tail = head, target = head;
        while (tail.next != null) tail = tail.next;
        for (int i = 0; i < pos; i++) target = target.next;
        tail.next = target;
        return head;
    }

}
