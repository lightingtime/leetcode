// ============================================================
// LeetCode 134. 加油站 (Gas Station)
// 难度：Medium | 分类：贪心与区间
// 链接：https://leetcode.cn/problems/gas-station/
// 刷题日期：2026-08-26
//
// 思路：环形贪心——total 全程累加净油判有无解；cur 从 start 出发累积油箱，
//       一旦 cur < 0 证明 [start..i] 之间任何一站都无法作为起点，start 跳到 i+1 并清零重来
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0134_GasStation {

    // ==== 提交代码开始 ====
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int total = 0;
        int start = 0;
        int cur = 0;
        for (int i = 0; i < gas.length; i++) {
            total += gas[i] - cost[i];
            cur += gas[i] - cost[i];
            if (cur < 0) {
                start = i + 1;
                cur = 0;
            }
        }
        return total < 0 ? -1 : start;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0134_GasStation s = new LC0134_GasStation();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.canCompleteCircuit(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(-1, s.canCompleteCircuit(new int[]{2, 3, 4}, new int[]{3, 4, 3}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= n <= 10^5，0 <= gas[i], cost[i] <= 10^4（总量 ≤ 10^9，int 不溢出）；答案唯一
        // 边界1: 单站且油够
        try {
            if (!TestUtil.checkEq(0, s.canCompleteCircuit(new int[]{5}, new int[]{3}), "边界1: 单站够")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 单站油不够
        try {
            if (!TestUtil.checkEq(-1, s.canCompleteCircuit(new int[]{2}, new int[]{3}), "边界2: 单站不够")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 总油=总耗，唯一解在首站（[2,1,1]/[1,1,2]）
        try {
            if (!TestUtil.checkEq(0, s.canCompleteCircuit(new int[]{2, 1, 1}, new int[]{1, 1, 2}), "边界3: 总油总耗相等")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 大盈余在中段，首尾不足（[0,5,0]/[1,1,1] → 1）
        try {
            if (!TestUtil.checkEq(1, s.canCompleteCircuit(new int[]{0, 5, 0}, new int[]{1, 1, 1}), "边界4: 盈余中段")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 总油不足 → -1
        try {
            if (!TestUtil.checkEq(-1, s.canCompleteCircuit(new int[]{1, 1, 1}, new int[]{2, 2, 2}), "边界5: 总油不足")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 10^5 上限，唯一解在首站（首站巨大盈余）
        try {
            int n = 100000;
            int[] g = new int[n], c = new int[n];
            g[0] = n;
            c[0] = 0;
            for (int i = 1; i < n; i++) { g[i] = 0; c[i] = 1; }
            if (!TestUtil.checkEq(0, s.canCompleteCircuit(g, c), "边界6: 上限首站解")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 10^5 上限，每站都亏 → -1
        try {
            int[] g = new int[100000], c = new int[100000];
            Arrays.fill(g, 1); Arrays.fill(c, 2);
            if (!TestUtil.checkEq(-1, s.canCompleteCircuit(g, c), "边界7: 上限全亏")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}