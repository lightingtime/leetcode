// ============================================================
// LeetCode 324. 摆动排序 II (Wiggle Sort II)
// 难度：Medium | 分类：贪心与区间
// 链接：https://leetcode.cn/problems/wiggle-sort-ii/
// 复习日期：2026-09-15（第 1 次复习 · 一刷 2026-08-27 · 一刷非一次 AC）
// 一刷思路：快速选择找中位数 + 虚拟下标映射 + 三路划分，O(n)/O(1)
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
//
// 思路：快速选择求第 n/2 小（只要中位数，不必全排序）+ 虚拟下标 f(i)=(1+2i)%(n|1) 把数组折成
//       「虚拟前段=峰位、后段=谷位」的顺序，再在虚拟空间上三路划分：>mid 进前段(峰)、
//       <mid 进后段(谷)、=mid 留中；lt: [0,lt) 全 >mid；i: [lt,i) 全 ==mid；(gt,n) 全 <mid
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

public class LC0324_WiggleSortIi {

    // ==== 提交代码开始 ====
    public void wiggleSort(int[] nums) {
        int n = nums.length;
        int mid = quickSort(nums, 0, n - 1, n / 2);
        // 虚拟下标上：lt: [0,lt) 全 >mid；i: [lt,i) 全 ==mid；(gt,n) 全 <mid；[i,gt] 未处理
        int lt = 0, i = 0, gt = n - 1;
        // 未处理区是闭区间 [i, gt]，i == gt 时还剩最后一个元素，所以条件取 i <= gt
        while (i <= gt) {
            if (nums[f(n, i)] > mid) {
                swap(nums, f(n, i), f(n, lt));
                lt++;
                i++;
            } else if (nums[f(n, i)] < mid) {
                swap(nums, f(n, i), f(n, gt));
                gt--;
            } else {
                i++;
            }
        }
    }

    // 虚拟下标 i → 真实下标；前 n/2 个虚拟下标是全部峰位，其余是全部谷位
    private int f(int n, int i) {
        return (1 + 2 * i) % (n | 1);
    }

    // 参数：nums[l..r] 为候选区间，k 为目标下标(0 基)；返回的是第 k+1 小的值
    private int quickSort(int[] nums, int l, int r, int k) {
        if (l == r) {
            return nums[l];
        }
        int p = nums[r];
        int i = l, j = l;
        while (j < r) {
            if (nums[j] < p) {
                swap(nums, i, j);
                i++;
            }
            j++;
        }
        swap(nums, i, r);
        if (i == k) {
            return nums[k];
        } else if (i < k) {
            return quickSort(nums, i + 1, r, k);
        } else {
            return quickSort(nums, l, i - 1, k);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0324_WiggleSortIi s = new LC0324_WiggleSortIi();
        int failures = 0;

        // ---- 示例测试（来自题目，允许多种合法答案 → 校验摆动性质）----
        try {
            int[] nums = new int[]{1, 5, 1, 1, 6, 4};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums) && sameMultiset(nums, 1, 5, 1, 1, 6, 4), "示例1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            int[] nums = new int[]{1, 3, 2, 2, 3, 1};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums) && sameMultiset(nums, 1, 3, 2, 2, 3, 1), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= n <= 5*10^4，0 <= nums[i] <= 5000；题目保证输入总能满足要求；答案唯一性无要求
        // 边界1: 单元素（摆动性质空满足）
        try {
            int[] nums = new int[]{7};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums), "边界1: 单元素")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        // 边界2: 两元素升序
        try {
            int[] nums = new int[]{1, 2};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums), "边界2: 两元素")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }
        // 边界3: 两元素降序
        try {
            int[] nums = new int[]{2, 1};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums), "边界3: 两元素降序")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }
        // 边界4: 大量重复 1 与 2（经典难例 [1,1,2,2]）
        try {
            int[] nums = new int[]{1, 1, 2, 2};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums) && sameMultiset(nums, 1, 1, 2, 2), "边界4: 重复元素"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }
        // 边界5: 全部相等元素不可能满足，但题目保证输入有效，此处测 [1,2,2,3,3,4] 升序排列
        try {
            int[] nums = new int[]{1, 2, 2, 3, 3, 4};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums) && sameMultiset(nums, 1, 2, 2, 3, 3, 4), "边界5: 连续升序"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }
        // 边界6: 奇数长度
        try {
            int[] nums = new int[]{1, 3, 2, 2, 3};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums) && sameMultiset(nums, 1, 3, 2, 2, 3), "边界6: 奇数长度"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6 异常: " + t);
        }
        // 边界7: 极值范围 0 与 5000
        try {
            int[] nums = new int[]{5000, 0, 5000, 0};
            s.wiggleSort(nums);
            if (!TestUtil.checkEq(true, isWiggle(nums) && sameMultiset(nums, 5000, 0, 5000, 0), "边界7: 极值"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界7 异常: " + t);
        }
        // 边界8: 大输入上限（5*10^4，交替 0/1 最坏重复场景）
        try {
            int[] nums = new int[50000];
            for (int i = 0; i < 50000; i++) nums[i] = (i % 2 == 0) ? 1 : 0;
            int[] orig = nums.clone();
            s.wiggleSort(nums);
            boolean ok = isWiggle(nums) && sameMultiset(nums, orig);
            if (!TestUtil.checkEq(true, ok, "边界8: 5万上限")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界8 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 摆动性质：nums[0] < nums[1] > nums[2] < nums[3] > ...
    static boolean isWiggle(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            boolean up = (i % 2 == 0);
            if (up ? a[i] >= a[i + 1] : a[i] <= a[i + 1]) return false;
        }
        return true;
    }

    // 多重集不变：a 排序后与 b 相同
    static boolean sameMultiset(int[] a, int[] b) {
        int[] x = a.clone(), y = b.clone();
        java.util.Arrays.sort(x);
        java.util.Arrays.sort(y);
        return java.util.Arrays.equals(x, y);
    }

    static boolean sameMultiset(int[] a, int first, int... rest) {
        return sameMultiset(a, toArray(first, rest));
    }

    private static int[] toArray(int first, int... rest) {
        int[] r = new int[rest.length + 1];
        r[0] = first;
        System.arraycopy(rest, 0, r, 1, rest.length);
        return r;
    }

}