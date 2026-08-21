// ============================================================
// LeetCode 268. 丢失的数字 (Missing Number)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/missing-number/
// 刷题日期：2026-08-21
//
// ============================================================

import java.util.*;

public class LC0268_MissingNumber {

    // ==== 提交代码开始 ====
    public int missingNumber(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == nums.length) {
                continue;
            }
            while (nums[i] != i) {
                int temp = nums[nums[i]];
                nums[nums[i]] = nums[i];
                nums[i] = temp;
                if (nums[i] == nums.length) {
                    break;
                }
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i) {
                return i;
            }
        }
        return nums.length;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0268_MissingNumber s = new LC0268_MissingNumber();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try { if (!TestUtil.checkEq(2, s.missingNumber(new int[]{3, 0, 1}), "示例1: [3,0,1]")) failures++; } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try { if (!TestUtil.checkEq(2, s.missingNumber(new int[]{0, 1}), "示例2: [0,1]")) failures++; } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try { if (!TestUtil.checkEq(8, s.missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}), "示例3: 长乱序")) failures++; } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: n=1 且缺的是 1（只有 [0]）
        try { if (!TestUtil.checkEq(1, s.missingNumber(new int[]{0}), "边界1: [0] 缺1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: n=1 且缺的是 0（只有 [1]）
        try { if (!TestUtil.checkEq(0, s.missingNumber(new int[]{1}), "边界2: [1] 缺0")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }

        // 边界3: 缺的是最大值 n（[0,1] 缺 2）
        try { if (!TestUtil.checkEq(3, s.missingNumber(new int[]{0, 1, 2}), "边界3: 缺n")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }

        // 边界4: 缺的是最小值 0（[1,2] 缺 0）
        try { if (!TestUtil.checkEq(0, s.missingNumber(new int[]{1, 2}), "边界4: 缺0")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: 缺中间值
        try { if (!TestUtil.checkEq(3, s.missingNumber(new int[]{0, 1, 2, 4, 5}), "边界5: 缺中间")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        // 边界6: n 上限 10^4，缺最大值 10000（数组为 0..9999）
        int[] bigMissMax = new int[10_000];
        for (int i = 0; i < bigMissMax.length; i++) bigMissMax[i] = i;
        try { if (!TestUtil.checkEq(10000, s.missingNumber(bigMissMax), "边界6: 上限缺n")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        // 边界7: n 上限 10^4，缺最小值 0（数组为 1..10000）
        int[] bigMissZero = new int[10_000];
        for (int i = 0; i < bigMissZero.length; i++) bigMissZero[i] = i + 1;
        try { if (!TestUtil.checkEq(0, s.missingNumber(bigMissZero), "边界7: 上限缺0")) failures++; } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }

        // 边界8: n 上限 10^4，缺中间值 5000（数组为 0..9999 去掉 5000）
        int[] bigMissMid = new int[10_000];
        int idx = 0;
        for (int v = 0; v <= 10_000; v++) {
            if (v != 5000) bigMissMid[idx++] = v;
        }
        try { if (!TestUtil.checkEq(5000, s.missingNumber(bigMissMid), "边界8: 上限缺中间")) failures++; } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        // 边界9: 缺失的数 m 恰好占着值 0（nums[m] == 0），0 无法被取负成标记
        try { if (!TestUtil.checkEq(2, s.missingNumber(new int[]{4, 3, 0, 1}), "边界9: 缺失位存0")) failures++; } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}