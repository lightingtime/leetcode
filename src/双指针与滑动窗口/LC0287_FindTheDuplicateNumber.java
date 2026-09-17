// ============================================================
// LeetCode 287. 寻找重复数 (Find the Duplicate Number)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/find-the-duplicate-number/
// 复习日期：2026-09-17（第 1 次复习 · 一刷 2026-08-05 · 一刷一次 AC）
// 一刷写法：Floyd 判圈——把数组当下标链表（i → nums[i]），重复值就是「有多个前驱」的节点、也就是环入口；
//       快慢指针在值上跳，相遇后 slow 复位到 0 再同速前进，再次相遇处即重复值，O(n)/O(1)
// 要点：相遇阶段必须先移动再比较（do-while 或让两指针初始错开），同起点时 fast != slow 一开始就不成立，循环体会一次都不执行
// 测试用例与一刷归档保持一致（示例 3 个 + 边界 4 个：n=1、重复最小值、重复在中间、n=10^5 大数组）
//
// 思路：Floyd 判圈（把值域当下标链）——slow 走一格 nums[slow]，fast 走两格 nums[nums[fast]]，do-while 先走再比；
//       相遇后把 slow 复位到 0，两指针改同速前进，第二次相遇处就是环入口（即重复值）。不改数组、额外空间 O(1)
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

import java.util.*;

public class LC0287_FindTheDuplicateNumber {

    // ==== 提交代码开始 ====
    public int findDuplicate(int[] nums) {
        int fast = 0;
        int slow = 0;
        do {
            fast = nums[nums[fast]];
            slow = nums[slow];
        } while (fast != slow);
        slow = 0;
        while (fast != slow) {
            fast = nums[fast];
            slow = nums[slow];
        }
        return fast;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0287_FindTheDuplicateNumber s = new LC0287_FindTheDuplicateNumber();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(2, s.findDuplicate(new int[]{1, 3, 4, 2, 2}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.findDuplicate(new int[]{3, 1, 3, 4, 2}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.findDuplicate(new int[]{3, 3, 3, 3, 3}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（n+1 个数都在 [1,n]，仅一个重复）----
        try {
            if (!TestUtil.checkEq(1, s.findDuplicate(new int[]{1, 1}), "边界1-最小规模n=1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1-最小规模n=1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(1, s.findDuplicate(new int[]{1, 1, 2, 3}), "边界2-重复最小值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2-重复最小值 异常: " + t); }
        try {
            if (!TestUtil.checkEq(3, s.findDuplicate(new int[]{1, 2, 3, 3, 4}), "边界3-重复值在中间")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3-重复值在中间 异常: " + t); }
        try {
            int[] nums = new int[100001];
            for (int i = 0; i < 100000; i++) nums[i] = i + 1;
            nums[100000] = 50000;
            if (!TestUtil.checkEq(50000, s.findDuplicate(nums), "边界4-大数组n=10^5")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4-大数组n=10^5 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
