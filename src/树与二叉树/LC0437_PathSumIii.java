// ============================================================
// LeetCode 437. 路径总和 III (Path Sum III)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/path-sum-iii/
// 刷题日期：2026-09-27
//
// ============================================================

import java.util.*;

public class LC0437_PathSumIii {

    // ==== 提交代码开始 ====
    int ans;
    public int pathSum(TreeNode root, int targetSum) {
        Map<Long, Integer> map = new HashMap<>();
        map.put(0L, 1);
        ans = 0;
        dfs(root, 0L, targetSum, map);
        return ans;
    }

    private void dfs(TreeNode root, long sum, int targetSum, Map<Long, Integer> map) {
        if (root == null) {
            return;
        }

        sum += root.val;
        ans += map.getOrDefault(sum - targetSum, 0);
        map.merge(sum, 1, Integer::sum);

        dfs(root.left, sum, targetSum, map);
        dfs(root.right, sum, targetSum, map);

        map.merge(sum, -1, Integer::sum);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0437_PathSumIii s = new LC0437_PathSumIii();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!checkEq(3, s.pathSum(treeNode(10, 5, -3, 3, 2, null, 11, 3, -2, null, 1), 8), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkEq(3, s.pathSum(treeNode(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, 5, 1), 22), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（与一刷归档保持一致）----
        // 空树：无任何路径
        try { if (!checkEq(0, s.pathSum(null, 0), "边界1-空树")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-空树 异常: " + t); }
        // 单节点命中：节点自身即一条路径
        try { if (!checkEq(1, s.pathSum(treeNode(1), 1), "边界2-单节点命中")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-单节点命中 异常: " + t); }
        // 单节点未命中
        try { if (!checkEq(0, s.pathSum(treeNode(1), 2), "边界3-单节点未命中")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3-单节点未命中 异常: " + t); }
        // 单边右链 1->2->3，target=3：路径 1+2 与 3 两条（路径无需以叶子结束，也不能跨越父节点）
        try { if (!checkEq(2, s.pathSum(treeNode(1, null, 2, null, 3), 3), "边界4-右链中间路径")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-右链中间路径 异常: " + t); }
        // 单边右链 1->2->-3，target=0：前缀和先超过目标再回落，只有 1+2+(-3) 一条
        try { if (!checkEq(1, s.pathSum(treeNode(1, null, 2, null, -3), 0), "边界5-负数回落")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5-负数回落 异常: " + t); }
        // 全零树 target=0：所有单节点与所有向下的多节点路径都算（3 单 + 2 双 = 5）
        try { if (!checkEq(5, s.pathSum(treeNode(0, 0, 0), 0), "边界6-全零多路径")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6-全零多路径 异常: " + t); }
        // 大数右链（4 个 1e9），target=1e9：只有 4 个单节点路径；前缀和累计达 2e9/3e9/4e9 超出 int 范围，验证需用 long 存前缀和
        try { if (!checkEq(4, s.pathSum(treeNode(1000000000, null, 1000000000, null, 1000000000, null, 1000000000), 1000000000), "边界7-大数防溢出")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7-大数防溢出 异常: " + t); }

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
