// ============================================================
// LeetCode 621. 任务调度器 (Task Scheduler)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/task-scheduler/
// 刷题日期：2026-08-17
//
// 思路：统计每个任务出现次数，答案只由「出现次数最多的任务」决定。
//   公式：max(tasks.length, (maxFreq - 1) * (n + 1) + maxFreqTasks)
//   - maxFreq     ：出现次数最多的任务的次数
//   - maxFreqTasks：出现次数恰好等于 maxFreq 的任务个数（并列最多频任务数）
//   - (maxFreq-1)*(n+1)：前 maxFreq-1 轮骨架，每轮 n+1 格 = 1 个最多频任务 + n 个冷却/填充位
//   - +maxFreqTasks：最后一轮把并列最多频的任务紧凑收尾（它们后面没有同类任务，无需再隔 n）
//   当任务种类足够多、空位能被填满时，最短就是 tasks.length，故取二者较大值。
// 复杂度：时间 O(n)（一遍统计频率 + 固定 26 次循环）| 空间 O(1)（count[26]）
// ============================================================

import java.util.*;

public class LC0621_TaskScheduler {

    // ==== 提交代码开始 ====
    public int leastInterval(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }
        // maxFreq     ：出现次数最多的任务的次数（如 A×3、B×3 时 maxFreq = 3）
        // maxFreqTasks：并列达到 maxFreq 的任务个数（如 A×3、B×3 时 = 2）
        int maxFreq = 0, maxFreqTasks = 0;
        for (int i = 0; i < 26; i++) {
            maxFreq = Math.max(maxFreq, count[i]);
        }
        for (int i = 0; i < 26; i++) {
            maxFreqTasks += count[i] == maxFreq ? 1 : 0;
        }
        // 公式：(maxFreq-1) 轮骨架，每轮 n+1 格；最后一轮 maxFreqTasks 个并列最多频任务收尾。
        // 若任务种类足够多、空位能被填满，最短就是 tasks.length，故取二者较大值。
        return Math.max(tasks.length, (maxFreq - 1) * (n + 1) + maxFreqTasks);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0621_TaskScheduler s = new LC0621_TaskScheduler();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try { if (!TestUtil.checkEq(8, s.leastInterval(new char[]{'A','A','A','B','B','B'}, 2), "示例1")) failures++; } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try { if (!TestUtil.checkEq(6, s.leastInterval(new char[]{'A','C','A','B','D','B'}, 1), "示例2")) failures++; } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try { if (!TestUtil.checkEq(10, s.leastInterval(new char[]{'A','A','A','B','B','B'}, 3), "示例3")) failures++; } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq(1, s.leastInterval(new char[]{'A'}, 2), "单任务")) failures++; } catch (Throwable t) { failures++; System.out.println("单任务 异常: " + t); }
        try { if (!TestUtil.checkEq(3, s.leastInterval(new char[]{'A','A','B'}, 0), "n=0无冷却")) failures++; } catch (Throwable t) { failures++; System.out.println("n=0无冷却 异常: " + t); }
        try { if (!TestUtil.checkEq(7, s.leastInterval(new char[]{'A','A','A'}, 2), "全相同")) failures++; } catch (Throwable t) { failures++; System.out.println("全相同 异常: " + t); }
        try { if (!TestUtil.checkEq(16, s.leastInterval(new char[]{'A','A','A','A','A','A','B','C','D','E','F','G'}, 2), "最多频+多样")) failures++; } catch (Throwable t) { failures++; System.out.println("最多频+多样 异常: " + t); }
        try { if (!TestUtil.checkEq(7, s.leastInterval(new char[]{'A','B','C','D','E','F','G'}, 2), "无需idle")) failures++; } catch (Throwable t) { failures++; System.out.println("无需idle 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}