// ============================================================
// LeetCode 207. 课程表 (Course Schedule)
// 难度：Medium | 分类：图与并查集
// 链接：https://leetcode.cn/problems/course-schedule/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-08 · 一刷一次 Accepted）
// 一刷写法：Kahn 拓扑排序（BFS 出度法）——邻接表存「先修 → 后继」，统计每个节点的入度；入度为 0 的课先入队，出队时把它指向的后继入度减 1，减到 0 就入队；最后统计出队数量是否等于课程总数，够则无环。O(V+E)/O(V+E)
// 本题易错点：① 依赖方向不能反——prerequisites[i] = [a, b] 表示「修 a 前要先修 b」，所以边是 b → a（入度记在 a 上）；② 判无环用「出队数量 == numCourses」而不是「队列空」；③ 自环（a 依赖 a）与局部环都要能识别；④ 也可以用 DFS 三色标记判环，但要注意「访问中」状态不能漏（只记 visited 会把环当合法）
// 测试用例与一刷归档保持一致（示例 2 个 + 边界 8 个：无先修/单课自环/三角环/线性依赖/局部环/共享先修/深链/带环深链）
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0207_CourseSchedule {

    // ==== 提交代码开始 ====
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<Integer>[] graph = new List[numCourses];
        int[] inDegree = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] prereq :prerequisites) {
            int before = prereq[1];
            int after = prereq[0];
            graph[before].add(after);
            inDegree[after]++;
        }
        int solve = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int course = queue.poll();
            solve++;
            if (!graph[course].isEmpty()) {
                for (Integer after : graph[course]) {
                    inDegree[after]--;
                    if (inDegree[after] == 0) {
                        queue.offer(after);
                    }
                }
            }
        }
        return solve == numCourses;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0207_CourseSchedule s = new LC0207_CourseSchedule();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.canFinish(2, new int[][]{new int[]{1, 0}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canFinish(2, new int[][]{new int[]{1, 0}, new int[]{0, 1}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(true, s.canFinish(3, new int[][]{}), "边界1-无先修")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canFinish(1, new int[][]{{0, 0}}), "边界2-单课自环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canFinish(3, new int[][]{{0, 1}, {1, 2}, {2, 0}}), "边界3-三角环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.canFinish(4, new int[][]{{1, 0}, {2, 1}, {3, 2}}), "边界4-线性依赖")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canFinish(3, new int[][]{{1, 2}, {2, 1}}), "边界5-局部环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.canFinish(3, new int[][]{{1, 0}, {2, 0}}), "边界6-共享先修")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.canFinish(5, new int[][]{{1, 0}, {2, 1}, {3, 2}, {4, 3}}), "边界7-深链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.canFinish(4, new int[][]{{3, 2}, {2, 1}, {1, 0}, {0, 3}}), "边界8-带环深链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        try {
            // 二刷分析补入：环外还有孤立节点（入度为 0 的点不止一个，且不一定在下标 0）
            if (!TestUtil.checkEq(false, s.canFinish(3, new int[][]{{1, 0}, {0, 1}}), "边界9-环外孤立节点")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }
        try {
            // 二刷分析补入：零入度点在别的下标（2 → 0 → 1 是合法顺序）
            if (!TestUtil.checkEq(true, s.canFinish(3, new int[][]{{1, 0}, {0, 2}}), "边界10-零入度点不在下标0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
