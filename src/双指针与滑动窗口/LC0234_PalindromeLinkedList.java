// ============================================================
// LeetCode 234. 回文链表 (Palindrome Linked List)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/palindrome-linked-list/
// 复习日期：2026-09-18（第 2 次复习 · 一刷 2026-08-04 · 一刷一次 Accepted）
// 一刷写法：快慢指针找中点 + 原地反转后半段——slow 每轮一步、fast 每轮两步；从 slow 起把后半段反转，再与 head 逐节点比较，O(n)/O(1)
// 一刷踩过的坑（错误习惯库有记录）：比较循环的终止条件写成「两指针撞上」(p1 != p2) 只对奇数长度成立——偶数长度的真回文里反转后的后半段走完就变成 null，再取 p2.val 直接 NPE；非回文用例会在那之前 return false，把这个漏洞盖住。终止条件应由「后半段是否走完」决定
// 测试用例与一刷归档保持一致（示例 2 个 + 边界 8 个）
// ============================================================

import java.util.*;

public class LC0234_PalindromeLinkedList {

    // ==== 提交代码开始 ====
    public boolean isPalindrome(ListNode head) {
        if (head == null) return true;
        ListNode mid = getMid(head);
        ListNode newHead = reverseList(mid);
        ListNode p1 =head, p2 = newHead;
        while (p2 != null) {
            if (p1.val != p2.val) {
                return false;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        return true;
    }

    private ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode pre = null, cur = head;
        while (cur != null) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    private ListNode getMid(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast!= null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0234_PalindromeLinkedList s = new LC0234_PalindromeLinkedList();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(true, s.isPalindrome(listNode(1, 2, 2, 1)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(false, s.isPalindrome(listNode(1, 2)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!checkEq(true, s.isPalindrome(listNode(1, 2, 1)), "边界1 奇数回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!checkEq(false, s.isPalindrome(listNode(1, 2, 3)), "边界2 奇数非回文")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!checkEq(true, s.isPalindrome(listNode(7)), "边界3 单节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!checkEq(true, s.isPalindrome(listNode(1, 1, 1, 1)), "边界4 全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!checkEq(true, s.isPalindrome(listNode(1, 1)), "边界5 两节点相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!checkEq(false, s.isPalindrome(listNode(1, 2, 2, 3)), "边界6 偶数长度首尾错位")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            int half = 50000;
            int[] vals = new int[half];
            for (int i = 0; i < half; i++) vals[i] = i % 9 + 1;
            ListNode head = new ListNode(vals[0]), cur = head;
            for (int i = 1; i < half; i++) { cur.next = new ListNode(vals[i]); cur = cur.next; }
            for (int i = half - 1; i >= 0; i--) { cur.next = new ListNode(vals[i]); cur = cur.next; }
            if (!checkEq(true, s.isPalindrome(head), "边界7 长度 100000 回文")) failures++;

            ListNode head2 = new ListNode(vals[0]), cur2 = head2, last2 = head2;
            for (int i = 1; i < half; i++) { cur2.next = new ListNode(vals[i]); cur2 = cur2.next; }
            for (int i = half - 1; i >= 0; i--) { cur2.next = new ListNode(vals[i]); cur2 = cur2.next; last2 = cur2; }
            last2.val = vals[0] == 9 ? 1 : vals[0] + 1;    // 只弄坏最后一对
            if (!checkEq(false, s.isPalindrome(head2), "边界8 长度 100000 末对不匹配")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7/8 异常: " + t); }

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
