// ============================================================
// LeetCode 149. 直线上最多的点数 (Max Points on a Line)
// 难度：Hard | 分类：哈希表
// 链接：https://leetcode.cn/problems/max-points-on-a-line/
// 复习日期：2026-09-06（第 1 次复习 · 一刷 2026-08-21）
// 一刷思路：三点共线叉积（O(n³)，非最优）；code-notes 要求二刷改写为 O(n²) 最优解：
// 固定每个基准点 i，HashMap<归一化斜率, 计数> 统计与 i 同斜率的点数，答案取 max(count)+1
// 斜率 key = 差值 (dx,dy) 除以 gcd 约分 + 符号统一（dx<0 或 dx==0&&dy<0 时取反）
// 测试用例与一刷归档保持一致
// ============================================================

import java.util.*;

public class LC0149_MaxPointsOnALine {

    // ==== 提交代码开始 ====
    public int maxPoints(int[][] points) {
        int max = 0;
        for (int i = 0; i < points.length; i++) {
            Map<String, Integer> map = new HashMap<>();
            int[] a = points[i];
            int curMax = 0;
            int same = 0;
            for (int j = 0; j < points.length; j++) {
                if (i == j) {
                    continue;
                }
                int[] b = points[j];
                int dx = a[0] - b[0];
                int dy = a[1] - b[1];
                if (dx == 0 && dy == 0) {
                    same++;
                    continue;
                }
                if (dx < 0 || (dx == 0 && dy < 0)) {
                    dx = -dx;
                    dy = -dy;
                }
                int g = gcd(Math.abs(dx), Math.abs(dy));
                dx = dx / g;
                dy = dy / g;
                String key = dx + "," + dy;
                map.merge(key, 1, Integer::sum);
                curMax = Math.max(curMax, map.get(key));
            }
            max = Math.max(max, curMax + 1 + same);
        }
        return max;
    }

    private int gcd(int dx, int dy) {
        while (dy != 0) {
            int n = dx % dy;
            dx = dy;
            dy = n;
        }
        return dx;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0149_MaxPointsOnALine s = new LC0149_MaxPointsOnALine();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(3, s.maxPoints(new int[][]{new int[]{1, 1}, new int[]{2, 2}, new int[]{3, 3}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.maxPoints(new int[][]{new int[]{1, 1}, new int[]{3, 2}, new int[]{5, 3}, new int[]{4, 1}, new int[]{2, 3}, new int[]{1, 4}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: 单点
        try { if (!TestUtil.checkEq(1, s.maxPoints(new int[][]{{0, 0}}), "边界1: 单点")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: 两个点必然共线
        try { if (!TestUtil.checkEq(2, s.maxPoints(new int[][]{{0, 0}, {1, 1}}), "边界2: 两点")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }

        // 边界3: 水平线（同 y）
        try { if (!TestUtil.checkEq(5, s.maxPoints(new int[][]{{0, 3}, {1, 3}, {2, 3}, {3, 3}, {4, 3}}), "边界3: 水平线")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }

        // 边界4: 垂直线（同 x）
        try { if (!TestUtil.checkEq(4, s.maxPoints(new int[][]{{-2, 0}, {-2, 1}, {-2, 2}, {-2, 3}}), "边界4: 垂直线")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: 斜率归一化（2/4 与 1/2 是同一条线，不能用 double 比较）
        try { if (!TestUtil.checkEq(4, s.maxPoints(new int[][]{{0, 0}, {2, 4}, {4, 8}, {1, 2}}), "边界5: 斜率归一化")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        // 边界6: 负坐标斜线
        try { if (!TestUtil.checkEq(4, s.maxPoints(new int[][]{{-1, -1}, {1, 1}, {2, 2}, {0, 0}}), "边界6: 负坐标")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        // 边界7: 多条平行线，取最多的那条
        try { if (!TestUtil.checkEq(3, s.maxPoints(new int[][]{{0, 0}, {1, 1}, {0, 1}, {1, 2}, {2, 3}}), "边界7: 平行线取大")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        // 边界8: 坐标端点 ±10^4
        try { if (!TestUtil.checkEq(3, s.maxPoints(new int[][]{{-10000, -10000}, {10000, 10000}, {0, 0}}), "边界8: 端点坐标")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        // 边界9: 300 点上限全在 y=x 上
        try {
            int[][] big = new int[300][2];
            for (int i = 0; i < 300; i++) { big[i][0] = i; big[i][1] = i; }
            if (!TestUtil.checkEq(300, s.maxPoints(big), "边界9: 300点共线")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        // 边界10: 水平/垂直/对角混合，取最多一条
        try { if (!TestUtil.checkEq(3, s.maxPoints(new int[][]{{0, 0}, {1, 0}, {0, 1}, {1, 1}, {2, 0}, {2, 2}}), "边界10: 混合线")) failures++; } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}