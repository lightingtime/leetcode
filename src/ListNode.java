// ============================================================
// 公共链表节点类：链表题本地测试共用（val + next 标准结构）
// 若某题节点结构不同（如带 random / prev），在该题文件内定义本地节点类
// 不参与力扣提交：判题环境自带 ListNode，提交脚本不会再附带
// ============================================================

public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}
