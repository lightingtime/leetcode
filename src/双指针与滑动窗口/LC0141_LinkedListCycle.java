// ============================================================
// LeetCode 141. 环形链表 (Linked List Cycle)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/linked-list-cycle/
// 刷题日期：2026-08-03
//
// ============================================================

import java.time.chrono.HijrahEra;
import java.util.*;

public class LC0141_LinkedListCycle {

    // ==== 提交代码开始 ====
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                return true;
            }
        }
        return false;
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

        // ---- 边界测试（自己补充）----
        // 例如： try { if (!checkEq(期望, s.hasCycle(边界输入), "边界1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 若题目允许任意顺序返回（下标对 / 集合），用 checkEqUnordered 代替 checkEq

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
