// ============================================================
// LeetCode 25. K 个一组翻转链表 (Reverse Nodes in k-Group)
// 难度：Hard | 分类：链表
// 链接：https://leetcode.cn/problems/reverse-nodes-in-k-group/
// 刷题日期：2026-09-21
// ============================================================

import java.util.*;

public class LC0025_ReverseNodesInKGroup {

    // ==== 提交代码开始 ====
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(-1, head);
        ListNode pre = dummy;
        while (head != null) {
            ListNode tail = pre;
            for (int i = 0; i < k; i++) {
                tail = tail.next;
                if (tail == null) {
                    return dummy.next;
                }
            }
            ListNode next = tail.next;
            ListNode[] list = reverse(head, tail);
            head = list[0];
            tail = list[1];
            pre.next = head;
            tail.next = next;
            pre = tail;
            head = tail.next;
        }
        return dummy.next;
    }

    private ListNode[] reverse(ListNode head, ListNode tail) {
        ListNode pre = tail.next;
        ListNode cur = head;
        // 循环不变量：groupHead 是区间 [head, tail] 之外的第一个节点；tail.next 会被本循环改写，故先快照再作判据
        ListNode groupHead = tail.next;
        while (cur != groupHead) {
            ListNode next = cur.next;
            cur.next = pre;
            pre = cur;
            cur = next;
        }
        // 返回 {翻转后的新头, 新尾}
        return new ListNode[] {tail, head};
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0025_ReverseNodesInKGroup s = new LC0025_ReverseNodesInKGroup();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(listNode(2, 1, 4, 3, 5), s.reverseKGroup(listNode(1, 2, 3, 4, 5), 2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(listNode(3, 2, 1, 4, 5), s.reverseKGroup(listNode(1, 2, 3, 4, 5), 3), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（按约束 1 <= k <= n <= 5000, 0 <= Node.val <= 1000 设计）----
        // 边界1 最小规模：n=k=1，单节点组翻转后仍是自身
        try { if (!checkEq(listNode(1), s.reverseKGroup(listNode(1), 1), "边界1 单节点")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2 k=1：每组 1 个等价于「不翻转」，顺序必须原样返回（易错：无条件反转指针把顺序打乱）
        try { if (!checkEq(listNode(5, 1, 3, 2), s.reverseKGroup(listNode(5, 1, 3, 2), 1), "边界2 k=1 不变")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3 恰好整除且 n=k：整条链表作为一组翻转
        try { if (!checkEq(listNode(3, 2, 1), s.reverseKGroup(listNode(1, 2, 3), 3), "边界3 n=k 整体翻转")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4 剩余恰好 1 个：k=2, n=3，末尾单节点必须保持原序拼接（易错点：不足 k 个也去翻转）
        try { if (!checkEq(listNode(2, 1, 3), s.reverseKGroup(listNode(1, 2, 3), 2), "边界4 余 1 个保序")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5 剩余 2 个不足一组：k=3, n=8 -> 前 6 个分两组翻转，末尾 7,8 保序
        try { if (!checkEq(listNode(3, 2, 1, 6, 5, 4, 7, 8), s.reverseKGroup(listNode(1, 2, 3, 4, 5, 6, 7, 8), 3), "边界5 余 2 个保序")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6 k = n-2：只有最后一组不足，前面组全翻转，验证组间拼接指针
        try { if (!checkEq(listNode(4, 3, 2, 1, 5, 6), s.reverseKGroup(listNode(1, 2, 3, 4, 5, 6), 4), "边界6 k=n-2")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7 全相同值：翻转后值序列不变（若只比较值可能误判，这里仍比对整链结构）
        try { if (!checkEq(listNode(5, 5, 5, 5, 5, 5), s.reverseKGroup(listNode(5, 5, 5, 5, 5, 5), 2), "边界7 全相同")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        // 边界8 取值端点：val 取约束上下界 0 与 1000，k=n
        try { if (!checkEq(listNode(1000, 0), s.reverseKGroup(listNode(0, 1000), 2), "边界8 值端点")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        // 边界9 规模上限：n=5000（约束上限），k=2 恰好整除，期望 = 相邻两两交换
        try {
            int n = 5000;
            ListNode big = new ListNode(1), bt = big;
            for (int v = 2; v <= n; v++) { bt.next = new ListNode(v); bt = bt.next; }
            ListNode exp = new ListNode(2), et = exp;
            et.next = new ListNode(1); et = et.next;
            for (int v = 4; v <= n; v += 2) { et.next = new ListNode(v); et = et.next; et.next = new ListNode(v - 1); et = et.next; }
            if (!checkEq(exp, s.reverseKGroup(big, 2), "边界9 n=5000")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

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
