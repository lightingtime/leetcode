// ============================================================
// LeetCode 406. 根据身高重建队列 (Queue Reconstruction by Height)
// 难度：Medium | 分类：树与二叉树
// 链接：https://leetcode.cn/problems/queue-reconstruction-by-height/
// 刷题日期：2026-08-18
//
// ============================================================

import java.util.*;

public class LC0406_QueueReconstructionByHeight {

    // ==== 提交代码开始 ====
    public int[][] reconstructQueue(int[][] people) {
        List<int[]> ans = new ArrayList<>();
        Arrays.sort(people, (a, b) -> {
            if (a[0] == b[0]) {
                return a[1] - b[1];
            }
            return b[0] - a[0];
        });
        for (int[] p : people) {
            ans.add(p[1], p);
        }
        return ans.toArray(new int[0][]);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0406_QueueReconstructionByHeight s = new LC0406_QueueReconstructionByHeight();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{5, 0}, new int[]{7, 0}, new int[]{5, 2}, new int[]{6, 1}, new int[]{4, 4}, new int[]{7, 1}}, s.reconstructQueue(new int[][]{new int[]{7, 0}, new int[]{4, 4}, new int[]{7, 1}, new int[]{5, 0}, new int[]{6, 1}, new int[]{5, 2}}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{4, 0}, new int[]{5, 0}, new int[]{2, 2}, new int[]{3, 2}, new int[]{1, 4}, new int[]{6, 0}}, s.reconstructQueue(new int[][]{new int[]{6, 0}, new int[]{5, 0}, new int[]{4, 0}, new int[]{3, 2}, new int[]{2, 2}, new int[]{1, 4}}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充，针对本题易错点设计）----
        // 边界1 单元素
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{5, 0}}, s.reconstructQueue(new int[][]{new int[]{5, 0}}), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单元素 异常: " + t); }
        // 边界2 全相同身高：同身高也互相计入 k，k 必须为 0,1,2 递增
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{7, 0}, new int[]{7, 1}, new int[]{7, 2}}, s.reconstructQueue(new int[][]{new int[]{7, 0}, new int[]{7, 1}, new int[]{7, 2}}), "边界2-全相同身高")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-全相同身高 异常: " + t); }
        // 边界3 两人不同身高：矮个子必须排在高个子前面（k 均为 0）
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{5, 0}, new int[]{6, 0}}, s.reconstructQueue(new int[][]{new int[]{6, 0}, new int[]{5, 0}}), "边界3-两人不同身高")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-两人不同身高 异常: " + t); }
        // 边界4 全部 k=0：队列应严格按身高升序
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{3, 0}, new int[]{4, 0}, new int[]{5, 0}}, s.reconstructQueue(new int[][]{new int[]{5, 0}, new int[]{3, 0}, new int[]{4, 0}}), "边界4-全k0")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-全k0 异常: " + t); }
        // 边界5 相同身高穿插不同身高：验证 >= 语义（同身高者也要计入 k）
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{6, 0}, new int[]{6, 1}, new int[]{7, 0}, new int[]{7, 1}}, s.reconstructQueue(new int[][]{new int[]{7, 0}, new int[]{7, 1}, new int[]{6, 1}, new int[]{6, 0}}), "边界5-同高穿插")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-同高穿插 异常: " + t); }
        // 边界6 大数身高（约束上界 10^6）单元素
        try {
            if (!TestUtil.checkEq(new int[][]{new int[]{1000000, 0}}, s.reconstructQueue(new int[][]{new int[]{1000000, 0}}), "边界6-大数身高")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-大数身高 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}