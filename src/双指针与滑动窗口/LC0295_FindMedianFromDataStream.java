// ============================================================
// LeetCode 295. 数据流的中位数 (Find Median from Data Stream)
// 难度：Hard | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/find-median-from-data-stream/
// 刷题日期：2026-08-22
//
// ============================================================

import java.util.*;

public class LC0295_FindMedianFromDataStream {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 MedianFinder 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class MedianFinder {
        PriorityQueue<Integer> firstHeap;
        PriorityQueue<Integer> secondHeap;
        public MedianFinder() {
            secondHeap = new PriorityQueue<>();
            firstHeap = new PriorityQueue<>(Comparator.reverseOrder());
        }
        public void addNum(int num) {
            if (firstHeap.isEmpty() || num < firstHeap.peek()) {
                firstHeap.offer(num);
                if (secondHeap.size() + 1 < firstHeap.size()) {
                    secondHeap.offer(firstHeap.poll());
                }
            } else {
                secondHeap.offer(num);
                if (firstHeap.size() < secondHeap.size()) {
                    firstHeap.offer(secondHeap.poll());
                }
            }
        }
        public double findMedian() {
            if (firstHeap.size() > secondHeap.size()) {
                return firstHeap.peek();
            }
            return (firstHeap.peek() + secondHeap.peek()) / 2.0;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 官方调用序列（题目示例）----
        // ["MedianFinder","addNum","addNum","findMedian","addNum","findMedian"]
        // [[],[1],[2],[],[3],[]]  -> [null,null,null,1.5,null,2.0]
        try {
            MedianFinder mf = new MedianFinder();
            boolean ok = true;
            mf.addNum(1);
            mf.addNum(2);
            ok &= TestUtil.checkEq(1.5, mf.findMedian(), "官序-中位1.5");
            mf.addNum(3);
            ok &= TestUtil.checkEq(2.0, mf.findMedian(), "官序-中位2.0");
            if (!ok) failures++;
        } catch (Throwable t) { failures++; System.out.println("官序 异常: " + t); }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 单元素
        try {
            MedianFinder mf = new MedianFinder();
            mf.addNum(5);
            if (!TestUtil.checkEq(5.0, mf.findMedian(), "边界1-单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-单元素 异常: " + t); }
        // 2) 两元素（偶数取平均）
        try {
            MedianFinder mf = new MedianFinder();
            mf.addNum(1);
            mf.addNum(3);
            if (!TestUtil.checkEq(2.0, mf.findMedian(), "边界2-两元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-两元素 异常: " + t); }
        // 3) 负数参与
        try {
            MedianFinder mf = new MedianFinder();
            mf.addNum(-5);
            mf.addNum(-1);
            mf.addNum(-3);
            if (!TestUtil.checkEq(-3.0, mf.findMedian(), "边界3-负数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-负数 异常: " + t); }
        // 4) 取值上下限 -10^5 与 10^5
        try {
            MedianFinder mf = new MedianFinder();
            mf.addNum(-100000);
            mf.addNum(100000);
            if (!TestUtil.checkEq(0.0, mf.findMedian(), "边界4-上下限值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-上下限值 异常: " + t); }
        // 5) 偶数个数：1,2,3,4 -> (2+3)/2 = 2.5
        try {
            MedianFinder mf = new MedianFinder();
            mf.addNum(1); mf.addNum(2); mf.addNum(3); mf.addNum(4);
            if (!TestUtil.checkEq(2.5, mf.findMedian(), "边界5-偶数平均")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5-偶数平均 异常: " + t); }
        // 6) 全相同
        try {
            MedianFinder mf = new MedianFinder();
            mf.addNum(7); mf.addNum(7); mf.addNum(7); mf.addNum(7);
            if (!TestUtil.checkEq(7.0, mf.findMedian(), "边界6-全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6-全相同 异常: " + t); }
        // 7) 乱序插入（先大后小）
        try {
            MedianFinder mf = new MedianFinder();
            mf.addNum(30); mf.addNum(20); mf.addNum(10);
            if (!TestUtil.checkEq(20.0, mf.findMedian(), "边界7-乱序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7-乱序 异常: " + t); }
        // 8) 大流量：插入 0..9999（偶数个，中位 4999.5），再插入 10000（奇数个，中位 5000）
        try {
            MedianFinder mf = new MedianFinder();
            for (int i = 0; i < 10000; i++) mf.addNum(i);
            boolean ok = TestUtil.checkEq(4999.5, mf.findMedian(), "边界8a-大流量偶数");
            mf.addNum(10000);
            ok &= TestUtil.checkEq(5000.0, mf.findMedian(), "边界8b-大流量奇数");
            if (!ok) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8-大流量 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}