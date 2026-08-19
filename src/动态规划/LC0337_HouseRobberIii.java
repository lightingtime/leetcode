// ============================================================
// LeetCode 337. 打家劫舍 III (House Robber III)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/house-robber-iii/
// 刷题日期：2026-08-19
//
// ============================================================

import java.util.*;

public class LC0337_HouseRobberIii {

    // ==== 提交代码开始 ====
    public int rob(TreeNode root) {
        int[] res = dfs(root);
        return Math.max(res[0], res[1]);
    }

    // 返回 int[]{偷当前节点, 不偷当前节点} 的最大金额，子问题只算一次
    private int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        int[] left = dfs(node.left);
        int[] right = dfs(node.right);
        int robThis = node.val + left[1] + right[1];                              // 偷它：孩子被迫不偷
        int skipThis = Math.max(left[0], left[1]) + Math.max(right[0], right[1]); // 不偷它：孩子自由
        return new int[]{robThis, skipThis};
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0337_HouseRobberIii s = new LC0337_HouseRobberIii();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(7, s.rob(treeNode(3, 2, 3, null, 3, null, 1)), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(9, s.rob(treeNode(3, 4, 5, 1, 3, null, 1)), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题树形 DP 设计）----
        // 空树：无房可偷
        try {
            if (!checkEq(0, s.rob(null), "边界-空树")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-空树 异常: " + t); }
        // 单节点：直接偷该房
        try {
            if (!checkEq(9, s.rob(treeNode(9)), "边界-单节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-单节点 异常: " + t); }
        // 父子两层：偷子节点更赚（2 vs 3）
        try {
            if (!checkEq(3, s.rob(treeNode(2, 3)), "边界-两层父子")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两层父子 异常: " + t); }
        // 三层右链 3->2->3：偷根+孙（3+3）胜过偷子（2）
        try {
            if (!checkEq(6, s.rob(treeNode(3, null, 2, null, 3)), "边界-右链隔代")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-右链隔代 异常: " + t); }
        // 四层右链 10->20->30->40：偷 20+40=60 胜过偷 10+40=50 / 10+30=40
        try {
            if (!checkEq(60, s.rob(treeNode(10, null, 20, null, 30, null, 40)), "边界-右链四层")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-右链四层 异常: " + t); }
        // 两层满树：偷两个子节点（2+4）胜过偷根（3）
        try {
            if (!checkEq(6, s.rob(treeNode(3, 2, 4)), "边界-两层满树")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-两层满树 异常: " + t); }
        // 混合结构 5 根(2,7) 孙(9,8)：最优是跳过根，偷 7+9+8=24（互不相邻），胜过偷根+孙 5+9+8=22 与偷子 2+7=9
        try {
            if (!checkEq(24, s.rob(treeNode(5, 2, 7, 9, 8)), "边界-根加孙组合")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-根加孙组合 异常: " + t); }
        // 全相同值 4：偷根+两孙（4+4+4=12）胜过偷两子（4+4=8）
        try {
            if (!checkEq(12, s.rob(treeNode(4, 4, 4, 4, 4)), "边界-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全相同 异常: " + t); }
        // 全零：偷不偷都 0
        try {
            if (!checkEq(0, s.rob(treeNode(0, 0, 0)), "边界-全零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-全零 异常: " + t); }
        // 大值单节点（上限 10^4）
        try {
            if (!checkEq(10000, s.rob(treeNode(10000)), "边界-大值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-大值 异常: " + t); }

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
        if (o instanceof TreeNode) {
            List<String> vals = new ArrayList<>();
            Queue<TreeNode> q = new LinkedList<>();
            q.offer((TreeNode) o);
            while (!q.isEmpty()) {
                TreeNode n = q.poll();
                if (n == null) { vals.add("null"); continue; }
                vals.add(String.valueOf(n.val));
                q.offer(n.left);
                q.offer(n.right);
            }
            while (!vals.isEmpty() && vals.get(vals.size() - 1).equals("null")) vals.remove(vals.size() - 1);
            return String.join(",", vals);
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

    static TreeNode treeNode(Object... vals) {
        if (vals.length == 0 || vals[0] == null) return null;
        TreeNode root = new TreeNode(((Number) vals[0]).intValue());
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty() && i < vals.length) {
            TreeNode node = q.poll();
            if (i < vals.length && vals[i] != null) { node.left = new TreeNode(((Number) vals[i]).intValue()); q.offer(node.left); }
            i++;
            if (i < vals.length && vals[i] != null) { node.right = new TreeNode(((Number) vals[i]).intValue()); q.offer(node.right); }
            i++;
        }
        return root;
    }

}