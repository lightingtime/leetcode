// ============================================================
// LeetCode 189. 轮转数组 (Rotate Array)
// 难度：Medium | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/rotate-array/
// 刷题日期：2026-08-22
//
// 思路：三段反转 —— k%=n 后，先整体反转，再反转前 k 段，再反转剩余段，即可得到右移 k 位的结果；原地、O(1) 额外空间。
// 复杂度：时间 O(n)，空间 O(1)
// ============================================================


public class LC0189_RotateArray {

    // ==== 提交代码开始 ====
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k %= n;
        swap(nums, 0, n - 1);
        swap(nums, 0, k - 1);
        swap(nums, k, n - 1);
    }

    private void swap(int[] nums, int l, int r) {
        while (l < r) {
            int temp = nums[r];
            nums[r] = nums[l];
            nums[l] = temp;
            l++;
            r--;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0189_RotateArray s = new LC0189_RotateArray();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        {
            int[] a = new int[]{1, 2, 3, 4, 5, 6, 7};
            s.rotate(a, 3);
            if (!TestUtil.checkEq(new int[]{5, 6, 7, 1, 2, 3, 4}, a, "示例1")) failures++;
        }
        {
            int[] a = new int[]{-1, -100, 3, 99};
            s.rotate(a, 2);
            if (!TestUtil.checkEq(new int[]{3, 99, -1, -100}, a, "示例2")) failures++;
        }

        // ---- 边界测试（本题具体逻辑）----
        // 1) k=0：不轮转
        {
            int[] a = new int[]{1, 2, 3, 4, 5};
            s.rotate(a, 0);
            if (!TestUtil.checkEq(new int[]{1, 2, 3, 4, 5}, a, "边界1-k0")) failures++;
        }
        // 2) k=n：轮转一整圈，数组不变
        {
            int[] a = new int[]{1, 2, 3};
            s.rotate(a, 3);
            if (!TestUtil.checkEq(new int[]{1, 2, 3}, a, "边界2-k等于n")) failures++;
        }
        // 3) k>n：需取模（k=5 % 3 = 2）
        {
            int[] a = new int[]{1, 2, 3};
            s.rotate(a, 5);
            if (!TestUtil.checkEq(new int[]{2, 3, 1}, a, "边界3-k大于n")) failures++;
        }
        // 4) k=n+1：等价于右移 1 位
        {
            int[] a = new int[]{1, 2, 3};
            s.rotate(a, 4);
            if (!TestUtil.checkEq(new int[]{3, 1, 2}, a, "边界4-k等于n+1")) failures++;
        }
        // 5) 单元素：任意 k 不变
        {
            int[] a = new int[]{7};
            s.rotate(a, 100);
            if (!TestUtil.checkEq(new int[]{7}, a, "边界5-单元素")) failures++;
        }
        // 6) 两元素右移 1 位
        {
            int[] a = new int[]{1, 2};
            s.rotate(a, 1);
            if (!TestUtil.checkEq(new int[]{2, 1}, a, "边界6-两元素")) failures++;
        }
        // 7) 负数参与轮转
        {
            int[] a = new int[]{-5, -4, -3, -2, -1};
            s.rotate(a, 2);
            if (!TestUtil.checkEq(new int[]{-2, -1, -5, -4, -3}, a, "边界7-负数")) failures++;
        }
        // 8) 全相同：轮转后不变
        {
            int[] a = new int[]{5, 5, 5};
            s.rotate(a, 2);
            if (!TestUtil.checkEq(new int[]{5, 5, 5}, a, "边界8-全相同")) failures++;
        }
        // 9) k 上限 10^5：k=100001 % 5 = 1
        {
            int[] a = new int[]{1, 2, 3, 4, 5};
            s.rotate(a, 100001);
            if (!TestUtil.checkEq(new int[]{5, 1, 2, 3, 4}, a, "边界9-k上限")) failures++;
        }
        // 10) 长度上限 10^5、k=1：末元素移到最前
        {
            int[] a = new int[100000];
            for (int i = 0; i < a.length; i++) a[i] = i;
            int[] expected = new int[100000];
            expected[0] = 99999;
            for (int i = 1; i < expected.length; i++) expected[i] = i - 1;
            s.rotate(a, 1);
            if (!TestUtil.checkEq(expected, a, "边界10-上限k1")) failures++;
        }
        // 11) 长度上限 10^5、k=10^5：整圈不变
        {
            int[] a = new int[100000];
            for (int i = 0; i < a.length; i++) a[i] = i;
            int[] expected = new int[100000];
            for (int i = 0; i < expected.length; i++) expected[i] = i;
            s.rotate(a, 100000);
            if (!TestUtil.checkEq(expected, a, "边界11-上限整圈")) failures++;
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}