// ============================================================
// LeetCode 11. 盛最多水的容器 (Container With Most Water)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/container-with-most-water/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-04 · 一刷一次 AC）
// 一刷/上次复习写法：相向双指针——left/right 从两端往中间收，每轮结算面积后移动较矮的一侧，O(n)/O(1)
// 要点：移动较矮侧是严格结论不是启发式——以较矮柱为界时，宽度只会更小、高度又被它封顶，所以其余组合不可能更大
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
// 上次复习留的精简提示：先比 height[left] < height[right] 再拿较矮边乘宽度，可省一次 Math.min；宽度提成 int 局部变量更易读；
// 相等时丢哪边都不影响最优（else 分支同时兜住「右边更矮」和「两边等高」两种情形），值得补一行注释
//
// 思路：相向双指针——left/right 从两端往中间收，每轮用「较矮边 × 宽度」结算面积；随后丢掉较矮的一侧：
//       以较矮柱为界时宽度只会更小、高度又被它封顶，其余组合不可能超过当前值，所以这样收不会漏掉最优
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0011_ContainerWithMostWater {

    // ==== 提交代码开始 ====
    public int maxArea(int[] height) {
        int max = 0;
        int left = 0, right = height.length - 1;
        while (left < right) {
            max = Math.max(max, Math.min(height[left], height[right]) * (right - left));
            if (height[left] > height[right]) {
                right--;
            } else {
                left++;
            }
        }
        return max;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0011_ContainerWithMostWater s = new LC0011_ContainerWithMostWater();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(49, s.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}), "示例1 [1,8,6,2,5,4,8,3,7]")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.maxArea(new int[]{1, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEq(4, s.maxArea(new int[]{1, 2, 3, 4}), "边界1 递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(4, s.maxArea(new int[]{4, 3, 2, 1}), "边界2 递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(15, s.maxArea(new int[]{5, 5, 5, 5}), "边界3 全等高")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try {
            if (!TestUtil.checkEq(2000, s.maxArea(new int[]{1000, 1, 1000}), "边界4 高边中间低")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.maxArea(new int[]{0, 0}), "边界5 全零")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        try {
            int[] height = new int[100000];
            Arrays.fill(height, 10000);
            if (!TestUtil.checkEq(10000 * 99999, s.maxArea(height), "边界6 长度 1e5 全 10000")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        try {
            if (!TestUtil.checkEq(17, s.maxArea(new int[]{2, 3, 4, 5, 18, 17, 6}), "边界7 最优不在两端")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        try {
            if (!TestUtil.checkEq(12, s.maxArea(new int[]{1, 2, 3, 4, 5, 4, 3, 2, 1}), "边界8 山峰形")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
