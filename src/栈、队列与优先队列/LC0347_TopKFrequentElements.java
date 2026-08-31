// ============================================================
// LeetCode 347. 前 K 个高频元素 (Top K Frequent Elements)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/top-k-frequent-elements/
// 复习日期：2026-09-01（复习 · 一刷 2026-08-07）
// 一刷思路：哈希计数 + 快速选择，O(n) 平均；code-notes：复习可试桶排序 O(n) 线性做法
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// ============================================================

import java.util.*;

public class LC0347_TopKFrequentElements {

    // ==== 提交代码开始 ====
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : nums) {
            map.merge(num, 1, Integer::sum);
        }
        List<Node> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> en : map.entrySet()) {
            list.add(new Node(en.getKey(), en.getValue()));
        }
        quickSort(list, 0, list.size() - 1, list.size() - k);
        return list.subList(list.size() - k, list.size()).stream().map(Node::getNum).mapToInt(Integer::intValue).toArray();
    }

    private void quickSort(List<Node> list, int l, int r, int k) {
        if (l == r) {
            return;
        }
        int p = list.get(r).count;
        int i = l, j = l;
        while (j < r) {
            if (list.get(j).count < p) {
                swap(list, i, j);
                i++;
            }
            j++;
        }
        swap(list, i, r);
        if (k == i) {
            return;
        } else if (k < i) {
            quickSort(list, l, i - 1, k);
        } else {
            quickSort(list, i + 1, r, k);
        }
    }

    private void swap(List<Node> list, int i, int j) {
        Node node = list.get(i);
        list.set(i, list.get(j));
        list.set(j, node);
    }

    static class Node {
        int num;
        int count;

        public Node(int num, int count) {
            this.num = num;
            this.count = count;
        }

        public int getNum() {
            return num;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0347_TopKFrequentElements s = new LC0347_TopKFrequentElements();
        int failures = 0;

        // ---- 示例测试（来自题目，答案顺序任意，用无序比较）----
        try {
            if (!TestUtil.checkEqUnordered(new int[]{1, 2}, s.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2), "示例1"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{1}, s.topKFrequent(new int[]{1}, 1), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{1, 2}, s.topKFrequent(new int[]{1, 2, 1, 2, 1, 2, 3, 1, 3, 2}, 2), "示例3"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }

        // ---- 边界测试 ----
        try {
            if (!TestUtil.checkEqUnordered(new int[]{7}, s.topKFrequent(new int[]{7, 7, 7, 7}, 1), "边界1-全相同"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{1, 2, 3}, s.topKFrequent(new int[]{1, 2, 3, 1, 2, 1}, 3), "边界2-k等于不同元素个数"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{-1, -3}, s.topKFrequent(new int[]{-1, -1, -2, -3, -3}, 2), "边界3-负数"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{-10000, 10000}, s.topKFrequent(new int[]{-10000, 10000, -10000, 10000, 0}, 2), "边界4-极值范围"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{3}, s.topKFrequent(new int[]{1, 2, 2, 3, 3, 3}, 1), "边界5-单最高频"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }
        try {
            if (!TestUtil.checkEqUnordered(new int[]{4, 1}, s.topKFrequent(new int[]{4, 4, 1, 1, 1, 2, 2, 3, 4, 4, 4}, 2), "边界6-长数组频率交错"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
