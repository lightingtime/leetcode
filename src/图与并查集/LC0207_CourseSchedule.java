// ============================================================
// LeetCode 207. 课程表 (Course Schedule)
// 难度：Medium | 分类：图与并查集
// 链接：https://leetcode.cn/problems/course-schedule/
// 刷题日期：2026-08-08
//
// ============================================================

import java.util.*;

public class LC0207_CourseSchedule {

    // ==== 提交代码开始 ====
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }

        for (int[] prereq : prerequisites) {
            graph.get(prereq[1]).add(prereq[0]);
            inDegree[prereq[0]]++;
        }
        int processed = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int course = queue.poll();
            processed++;
            for (int nextCourse : graph.get(course)) {
                inDegree[nextCourse]--;
                if (inDegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }
        return processed == numCourses;
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

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
