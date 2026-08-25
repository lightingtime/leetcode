// ============================================================
// LeetCode 218. 天际线问题 (The Skyline Problem)
// 难度：Hard | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/the-skyline-problem/
// 刷题日期：2026-08-25
//
// 思路：扫描线 + 最大堆（惰性删除）。每栋建筑生成“开始/结束”两个事件，按 x 排序（同 x 先结束再开始）；
//       扫描时用最大堆维护当前活跃建筑的最高高度：开始事件入堆，结束事件靠惰性删除（堆顶 right<=x 即过期）清掉；
//       当前最高高度变化处输出关键点 (x, cur)。
// 复杂度：时间 O(n log n)（事件排序 + 每个事件入/出堆一次）；空间 O(n)
// ============================================================

import java.util.*;

public class LC0218_TheSkylineProblem {

    // ==== 提交代码开始 ====
    public List<List<Integer>> getSkyline(int[][] buildings) {
        // 1. 生成事件：每栋建筑 = 左边缘“开始” + 右边缘“结束”，按 x 升序（同 x 时结束排在开始前）
        PriorityQueue<Event> queue = new PriorityQueue<>((a, b) -> {
            if (a.x == b.x) {
                return a.isStart - b.isStart; // isStart 0=结束 先于 1=开始，保证同 x 先移除再加入
            }
            return a.x - b.x;
        });
        for (int[] building : buildings) {
            queue.offer(new Event(building[0], building[1], building[2], 1)); // 左边缘：开始
            queue.offer(new Event(building[1], building[1], building[2], 0)); // 右边缘：结束
        }

        List<List<Integer>> ans = new ArrayList<>();
        // 2. 活跃建筑最大堆（惰性删除）：元素 = {right, height}，按 height 降序
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        int h = 0; // 上一个已记录的天际线高度，初始为地面高度 0

        // 3. 按 x 分组扫描：同一 x 的所有事件处理完，再查询一次当前最高高度
        while (!queue.isEmpty()) {
            int x = queue.peek().x; // 本组事件共同的 x
            while (!queue.isEmpty() && queue.peek().x == x) {
                Event top = queue.poll();
                if (top.isStart == 1) {
                    heap.offer(new int[]{top.right, top.h}); // 开始事件：加入活跃集合
                }
                // 结束事件无需显式处理，由下面的惰性删除统一清理
            }
            // 惰性删除：right <= x 的建筑已经结束，从堆顶弹出
            while (!heap.isEmpty() && heap.peek()[0] <= x) {
                heap.poll();
            }
            // 当前最高高度变化处输出关键点 (x, cur)
            int cur = heap.isEmpty() ? 0 : heap.peek()[1];
            if (cur != h) {
                List<Integer> node = new ArrayList<>();
                node.add(x);
                node.add(cur);
                ans.add(node);
                h = cur;
            }
        }
        return ans;
    }

    static class Event {
        int x;       // 事件发生的横坐标（左边缘或右边缘）
        int right;   // 该建筑的右边缘（开始事件用它判断何时过期；结束事件时等于 x）
        int h;       // 该建筑高度
        int isStart; // 1=开始（左边缘，入堆），0=结束（右边缘）

        public Event(int x, int right, int h, int isStart) {
            this.x = x;
            this.right = right;
            this.h = h;
            this.isStart = isStart;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0218_TheSkylineProblem s = new LC0218_TheSkylineProblem();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(2, 10), Arrays.asList(3, 15), Arrays.asList(7, 12), Arrays.asList(12, 0), Arrays.asList(15, 10), Arrays.asList(20, 8), Arrays.asList(24, 0)), s.getSkyline(new int[][]{new int[]{2, 9, 10}, new int[]{3, 7, 15}, new int[]{5, 12, 12}, new int[]{15, 20, 10}, new int[]{19, 24, 8}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(0, 3), Arrays.asList(5, 0)), s.getSkyline(new int[][]{new int[]{0, 2, 3}, new int[]{2, 5, 3}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题具体逻辑与约束设计，buildings.length>=1，left<right<=2^31-1，height>=1）----
        // 边界1: 单栋建筑
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(1, 3), Arrays.asList(5, 0)), s.getSkyline(new int[][]{new int[]{1, 5, 3}}), "边界1: 单栋")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 两栋等高紧贴（应合并为一条水平线，无中间关键点）
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(0, 4), Arrays.asList(6, 0)), s.getSkyline(new int[][]{new int[]{0, 3, 4}, new int[]{3, 6, 4}}), "边界2: 等高紧贴")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 矮建筑完全被高建筑覆盖（被覆盖者不产生关键点）
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(2, 10), Arrays.asList(9, 0)), s.getSkyline(new int[][]{new int[]{2, 9, 10}, new int[]{3, 7, 8}}), "边界3: 完全覆盖")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 高建筑先结束、矮建筑后继续（高度回落处出新点）
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(0, 10), Arrays.asList(5, 6), Arrays.asList(8, 0)), s.getSkyline(new int[][]{new int[]{0, 5, 10}, new int[]{3, 8, 6}}), "边界4: 高先结束矮后继续")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 前一栋右缘与后一栋左缘相同 x，高度变化（x 边界处理）
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(0, 3), Arrays.asList(4, 5), Arrays.asList(8, 0)), s.getSkyline(new int[][]{new int[]{0, 4, 3}, new int[]{4, 8, 5}}), "边界5: 相接不同高")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 两栋等高但中间有空隙（空隙处必须落回 0）
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(0, 3), Arrays.asList(2, 0), Arrays.asList(4, 3), Arrays.asList(6, 0)), s.getSkyline(new int[][]{new int[]{0, 2, 3}, new int[]{4, 6, 3}}), "边界6: 等高有空隙")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 大数端点 right=2^31-1
        try {
            if (!TestUtil.checkEq(Arrays.asList(Arrays.asList(0, 1), Arrays.asList(2147483647, 0)), s.getSkyline(new int[][]{new int[]{0, 2147483647, 1}}), "边界7: 大数端点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}