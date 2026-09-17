// ============================================================
// LeetCode 324. 摆动排序 II (Wiggle Sort II)
// 难度：Medium | 分类：贪心与区间
// 链接：https://leetcode.cn/problems/wiggle-sort-ii/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-27 · 一刷非一次 AC）
// 一刷思路：快速选择找中位数 + 虚拟下标映射 + 三路划分，O(n)/O(1)
// 上一次复习：快速选择中位数 + 虚拟下标三路划分，一次 AC 但有探讨（判较弱）；上次踩坑：三路划分方向写反（小数被送进峰位）→ 除单元素外全部失败、分区细节从 Hoare 改成 Lomuto（pivot 取末尾）
// 子类型：中位数定位 + 虚拟下标映射（把「峰位/谷位」折成虚拟前段/后段）
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例）
//
// 思路：quickselect 定位第 n/2 小（只要中位数，不必全排序；Lomuto 分区、pivot 取右端）+ 虚拟下标
//       f(i)=(1+2i)%(n|1) 把数组折成「虚拟前段=峰位(奇数下标)、后段=谷位(偶数下标)」，再在虚拟空间上三路划分：
//       >mid 送虚拟左侧(峰)、<mid 送虚拟右侧(谷)、=mid 留中，最后按 f 映回真实下标即得小大小大
// 复杂度：时间 平均 O(n) 空间 O(1)
// ============================================================

public class LC0324_WiggleSortIi {

    // ==== 提交代码开始 ====
    public void wiggleSort(int[] nums) {
        int n = nums.length;
        int mid = quickSort(nums, 0, n - 1, n / 2);
        int lt = 0, i = 0, gt = n - 1;
        while (i <= gt) {
            // 虚拟左侧 [0,lt) 映到奇数下标（峰位）→ 收 >mid；虚拟右侧 (gt,n) 映到偶数下标（谷位）→ 收 <mid。
            // 方向与真实下标的「小 大 小 大」相反，写反会除单元素外全部失败
            if (nums[f(i, n)] < mid) {
                // (gt,n) 已确定为 <mid，i..gt 是待处理闭区间：换进来的是 gt 侧未检查的值，故 i 原地重看
                swap(nums, f(i, n), f(gt, n));
                gt--;
            } else if (nums[f(i, n)] > mid) {
                // 换进来的是 [lt,i) 上已知 ==mid 的值，落位即定，可以直接 i++
                swap(nums, f(i, n), f(lt, n));
                i++;
                lt++;
            } else {
                i++;
            }
        }
    }

    private int f(int i, int n) {
        return (1 + 2 * i) % (n | 1);
    }

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
        if (i == k){
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
        nums[j] =temp;
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
