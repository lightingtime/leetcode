// ============================================================
// LeetCode 210. 课程表 II (Course Schedule II)
// 难度：Medium | 分类：图与并查集
// 链接：https://leetcode.cn/problems/course-schedule-ii/
// 刷题日期：2026-08-25
//
// ============================================================

import java.util.*;

public class LC0210_CourseScheduleIi {

    // ==== 提交代码开始 ====
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        int[] inDegree = new int[numCourses];
        for (int[] prerequisite : prerequisites) {
            int before = prerequisite[1];
            int after = prerequisite[0];
            graph.get(before).add(after);
            inDegree[after]++;
        }
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                deque.offerLast(i);
            }
        }
        int[] ans = new int[numCourses];
        int index = 0;
        while (!deque.isEmpty()) {
            int cur = deque.removeFirst();
            ans[index++] = cur;
            for (Integer course : graph.get(cur)) {
                inDegree[course]--;
                if (inDegree[course] == 0) {
                    deque.offerLast(course);
                }
            }
        }
        if (index == numCourses) {
            return ans;
        }
        return new int[0];
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0210_CourseScheduleIi s = new LC0210_CourseScheduleIi();
        int failures = 0;

        // ---- 示例测试（来自题目；答案不唯一，改用拓扑序合法性校验 checkOrder）----
        try {
            if (!checkOrder(s.findOrder(2, new int[][]{new int[]{1, 0}}), 2, new int[][]{new int[]{1, 0}})) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!checkOrder(s.findOrder(4, new int[][]{new int[]{1, 0}, new int[]{2, 0}, new int[]{3, 1}, new int[]{3, 2}}), 4, new int[][]{new int[]{1, 0}, new int[]{2, 0}, new int[]{3, 1}, new int[]{3, 2}})) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!checkOrder(s.findOrder(1, new int[][]{}), 1, new int[][]{})) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题：无先修、环、链式依赖、大输入）----
        // 边界1: 无先修（任意顺序都合法）
        try {
            if (!checkOrder(s.findOrder(3, new int[][]{}), 3, new int[][]{})) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 2 节点成环 -> 空数组
        try {
            if (!TestUtil.checkEq(new int[]{}, s.findOrder(2, new int[][]{new int[]{0, 1}, new int[]{1, 0}}), "边界2: 成环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 链式依赖（唯一顺序）
        try {
            if (!checkOrder(s.findOrder(4, new int[][]{new int[]{1, 0}, new int[]{2, 1}, new int[]{3, 2}}), 4, new int[][]{new int[]{1, 0}, new int[]{2, 1}, new int[]{3, 2}})) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 大输入 2000 门课链式依赖
        try {
            int n = 2000;
            int[][] pre = new int[n - 1][2];
            for (int i = 1; i < n; i++) pre[i - 1] = new int[]{i, i - 1};
            if (!checkOrder(s.findOrder(n, pre), n, pre)) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 大输入 2000 门课成环 -> 空数组
        try {
            int n = 2000;
            int[][] pre = new int[n][2];
            for (int i = 0; i < n; i++) pre[i] = new int[]{i, (i + 1) % n};
            if (!TestUtil.checkEq(new int[]{}, s.findOrder(n, pre), "边界5: 2000环")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // ---- 本题专用校验：order 是否为一个合法拓扑序（答案不唯一，不比对具体顺序）----
    static boolean checkOrder(int[] order, int numCourses, int[][] prerequisites) {
        if (order == null || order.length != numCourses) {
            System.out.println(" 失败 ✗ 长度=" + (order == null ? "null" : order.length) + " 期望=" + numCourses);
            return false;
        }
        int[] pos = new int[numCourses];
        Arrays.fill(pos, -1);
        for (int i = 0; i < order.length; i++) {
            if (order[i] < 0 || order[i] >= numCourses || pos[order[i]] != -1) {
                System.out.println(" 失败 ✗ 课程缺失或重复: " + Arrays.toString(order));
                return false;
            }
            pos[order[i]] = i;
        }
        for (int[] p : prerequisites) {
            if (pos[p[1]] >= pos[p[0]]) { // 先修 p[1] 必须在 p[0] 之前
                System.out.println(" 失败 ✗ 先修顺序错误: " + p[0] + " 需要先修 " + p[1]);
                return false;
            }
        }
        System.out.println(" 通过 ✓");
        return true;
    }

}