// ============================================================
// LeetCode 96. 不同的二叉搜索树 (Unique Binary Search Trees)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/unique-binary-search-trees/
// 刷题日期：2026-08-19
//
// ============================================================


public class LC0096_UniqueBinarySearchTrees {

    // ==== 提交代码开始 ====
    public int numTrees(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int k = 0; k < i; k++) {
                dp[i] += (dp[k] * dp[i - k - 1]);
            }
        }
        return dp[n];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0096_UniqueBinarySearchTrees s = new LC0096_UniqueBinarySearchTrees();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(5, s.numTrees(3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.numTrees(1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对 BST 计数 / 卡塔兰数的具体逻辑）----
        // n=0：空树 1 种（递推根基 dp[0]，约束外但算法依赖它）
        try { if (!TestUtil.checkEq(1, s.numTrees(0), "n=0空树")) failures++; } catch (Throwable t) { failures++; System.out.println("n=0空树 异常: " + t); }
        // n=1 / n=2：单节点 / 根固定后的左右子树各 1 种
        try { if (!TestUtil.checkEq(1, s.numTrees(1), "n=1单节点")) failures++; } catch (Throwable t) { failures++; System.out.println("n=1单节点 异常: " + t); }
        try { if (!TestUtil.checkEq(2, s.numTrees(2), "n=2两种根")) failures++; } catch (Throwable t) { failures++; System.out.println("n=2两种根 异常: " + t); }
        // n=4..7：卡塔兰数递推中间值
        try { if (!TestUtil.checkEq(14, s.numTrees(4), "n=4")) failures++; } catch (Throwable t) { failures++; System.out.println("n=4 异常: " + t); }
        try { if (!TestUtil.checkEq(42, s.numTrees(5), "n=5")) failures++; } catch (Throwable t) { failures++; System.out.println("n=5 异常: " + t); }
        try { if (!TestUtil.checkEq(132, s.numTrees(6), "n=6")) failures++; } catch (Throwable t) { failures++; System.out.println("n=6 异常: " + t); }
        try { if (!TestUtil.checkEq(429, s.numTrees(7), "n=7")) failures++; } catch (Throwable t) { failures++; System.out.println("n=7 异常: " + t); }
        // n=19：int 上限附近的卡塔兰数（C19=1767263190），验证大数不溢出
        try { if (!TestUtil.checkEq(1767263190, s.numTrees(19), "n=19大数")) failures++; } catch (Throwable t) { failures++; System.out.println("n=19大数 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}