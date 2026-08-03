// ============================================================
// 公共二叉树节点类：树题本地测试共用（val + left + right 标准结构）
// 若某题节点结构不同，在该题文件内定义本地节点类
// 不参与力扣提交：判题环境自带 TreeNode
// ============================================================

public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) { this.val = val; this.left = left; this.right = right; }
}
