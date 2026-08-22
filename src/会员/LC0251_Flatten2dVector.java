// ============================================================
// LeetCode 251. 展开二维向量 (Flatten 2D Vector)
// 难度：Medium | 分类：双指针与滑动窗口（设计题）
// 链接：https://leetcode.cn/problems/flatten-2d-vector/
// 刷题日期：2026-08-22
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0251_Flatten2dVector {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体）。判题要求类名 Vector2D，提交脚本会自动解包。
    // ==== 提交代码开始 ====
    static class Vector2D {
        // TODO: 补全字段
        public Vector2D(int[][] vec) {
            // TODO
        }

        public int next() {
            // TODO
            return 0;
        }

        public boolean hasNext() {
            // TODO
            return false;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 官方调用序列（题目示例）----
        // ["Vector2D","next","next","next","hasNext","hasNext","next","hasNext"]
        // [[[[1,2],[3],[4]]],[],[],[],[],[],[],[]]
        try {
            Vector2D v = new Vector2D(new int[][]{{1, 2}, {3}, {4}});
            boolean ok = true;
            ok &= TestUtil.checkEq(1, v.next(), "官序-next1");
            ok &= TestUtil.checkEq(2, v.next(), "官序-next2");
            ok &= TestUtil.checkEq(3, v.next(), "官序-next3");
            ok &= TestUtil.checkEq(true, v.hasNext(), "官序-hasNext4");
            ok &= TestUtil.checkEq(true, v.hasNext(), "官序-hasNext5");
            ok &= TestUtil.checkEq(4, v.next(), "官序-next6");
            ok &= TestUtil.checkEq(false, v.hasNext(), "官序-hasNext7");
            if (!ok) failures++;
        } catch (Throwable t) { failures++; System.out.println("官序 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 空外层：new int[0][0]，hasNext 直接为 false
        try {
            Vector2D v = new Vector2D(new int[0][0]);
            if (!TestUtil.checkEq(false, v.hasNext(), "边界1-空外层")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-空外层 异常: " + t); }
        // 2) 只有空内层数组
        try {
            Vector2D v = new Vector2D(new int[][]{{}});
            if (!TestUtil.checkEq(false, v.hasNext(), "边界2-全空内层")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-全空内层 异常: " + t); }
        // 3) 空内层夹在中间，需跳过
        try {
            Vector2D v = new Vector2D(new int[][]{{}, {1}, {}, {2, 3}, {}});
            boolean ok = true;
            ok &= TestUtil.checkEq(true, v.hasNext(), "边界3-先true");
            ok &= TestUtil.checkEq(1, v.next(), "边界3-next1");
            ok &= TestUtil.checkEq(2, v.next(), "边界3-next2");
            ok &= TestUtil.checkEq(3, v.next(), "边界3-next3");
            ok &= TestUtil.checkEq(false, v.hasNext(), "边界3-后false");
            if (!ok) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-空内层 异常: " + t); }
        // 4) 单元素
        try {
            Vector2D v = new Vector2D(new int[][]{{7}});
            boolean ok = true;
            ok &= TestUtil.checkEq(7, v.next(), "边界4-next");
            ok &= TestUtil.checkEq(false, v.hasNext(), "边界4-hasNext");
            if (!ok) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-单元素 异常: " + t); }
        // 5) 多行变长数组全遍历
        try {
            Vector2D v = new Vector2D(new int[][]{{1, 2, 3}, {4}, {5, 6}});
            boolean ok = true;
            int[] got = new int[6];
            int idx = 0;
            while (v.hasNext()) got[idx++] = v.next();
            ok &= TestUtil.checkEq(new int[]{1, 2, 3, 4, 5, 6}, got, "边界5-全遍历");
            ok &= TestUtil.checkEq(false, v.hasNext(), "边界5-结束");
            if (!ok) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-变长 异常: " + t); }
        // 6) 连续多个空内层后再有数据
        try {
            Vector2D v = new Vector2D(new int[][]{{}, {}, {}, {5}});
            boolean ok = true;
            ok &= TestUtil.checkEq(true, v.hasNext(), "边界6-先true");
            ok &= TestUtil.checkEq(5, v.next(), "边界6-next");
            ok &= TestUtil.checkEq(false, v.hasNext(), "边界6-后false");
            if (!ok) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-连续空 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
