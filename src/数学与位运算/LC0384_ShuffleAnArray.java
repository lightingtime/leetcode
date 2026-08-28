// ============================================================
// LeetCode 384. 打乱数组 (Shuffle an Array)
// 难度：Medium | 分类：数学与位运算
// 链接：https://leetcode.cn/problems/shuffle-an-array/
// 刷题日期：2026-08-28
//
// 思路：Fisher-Yates 洗牌——从后往前，j∈[0,i] 均匀随机选并交换；reset 从 original 恢复
// 复杂度：时间 O(n) 空间 O(n)
// ============================================================

import java.util.*;

public class LC0384_ShuffleAnArray {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 Solution 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class Solution {
        int[] original;
        int[] after;
        Random random;

        public Solution(int[] nums) {
            original = new int[nums.length];
            System.arraycopy(nums, 0, original, 0, nums.length);
            after = new int[nums.length];
            System.arraycopy(original, 0, after, 0, original.length);
            random = new Random();
        }

        public int[] reset() {
            System.arraycopy(original, 0, after, 0, original.length);
            return after;
        }

        public int[] shuffle() {
            reset();
            for (int i = after.length - 1; i >= 0; i--) {
                int j = random.nextInt(i + 1);
                int temp = after[i];
                after[i] = after[j];
                after[j] = temp;
            }
            return after;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 测试1：reset 返回原始顺序 ----
        try {
            Solution s = new Solution(new int[]{1, 2, 3});
            if (!TestUtil.checkEq(new int[]{1, 2, 3}, s.reset(), "reset-原始顺序")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("reset 异常: " + t);
        }

        // ---- 测试2：shuffle 结果是原数组的排列（元素集合相同）----
        try {
            Solution s = new Solution(new int[]{1, 2, 3});
            int[] sh = s.shuffle();
            if (!TestUtil.checkEqUnordered(new int[]{1, 2, 3}, sh, "shuffle-是排列")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("shuffle 排列 异常: " + t);
        }

        // ---- 测试3：等概率统计（Fisher-Yates 核心要求）----
        // 30000 次 shuffle，统计每个元素出现在每个位置的次数；n=3 期望各 10000，允许 ±10%
        try {
            Solution s = new Solution(new int[]{1, 2, 3});
            int[][] cnt = new int[3][3]; // cnt[元素][位置]
            for (int t = 0; t < 30000; t++) {
                int[] sh = s.shuffle();
                for (int pos = 0; pos < 3; pos++) {
                    cnt[sh[pos] - 1][pos]++;
                }
            }
            boolean ok = true;
            for (int i = 0; i < 3 && ok; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cnt[i][j] < 9000 || cnt[i][j] > 11000) {
                        ok = false;
                        System.out.println("等概率偏差：元素" + (i + 1) + "在位置" + j + "出现 " + cnt[i][j] + " 次（期望 10000）");
                    }
                }
            }
            if (!ok) failures++;
            else System.out.println("等概率统计 通过 ✓");
        } catch (Throwable t) {
            failures++;
            System.out.println("等概率 异常: " + t);
        }

        // ---- 测试4：随机性 sanity——多次 shuffle 应出现至少两种不同排列 ----
        try {
            Solution s = new Solution(new int[]{1, 2, 3});
            int[] first = s.shuffle().clone();
            boolean diff = false;
            for (int t = 0; t < 50 && !diff; t++) {
                if (!Arrays.equals(first, s.shuffle())) diff = true;
            }
            if (!diff) {
                System.out.println("随机性不足：50 次 shuffle 全同");
                failures++;
            } else System.out.println("随机性 通过 ✓");
        } catch (Throwable t) {
            failures++;
            System.out.println("随机性 异常: " + t);
        }

        // ---- 测试5：单元素数组 ----
        try {
            Solution s = new Solution(new int[]{5});
            if (!TestUtil.checkEq(new int[]{5}, s.reset(), "单元素-reset")) failures++;
            if (!TestUtil.checkEq(new int[]{5}, s.shuffle(), "单元素-shuffle")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("单元素 异常: " + t);
        }

        // ---- 测试6：长度 50 大数组 shuffle 后仍是排列 ----
        try {
            int[] big = new int[50];
            for (int i = 0; i < 50; i++) big[i] = i;
            Solution s = new Solution(big);
            int[] sh = s.shuffle();
            if (!TestUtil.checkEqUnordered(big, sh, "大数组-是排列")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("大数组 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}