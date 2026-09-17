// ============================================================
// LeetCode 334. 递增的三元子序列 (Increasing Triplet Subsequence)
// 难度：Medium | 分类：贪心与区间
// 链接：https://leetcode.cn/problems/increasing-triplet-subsequence/
// 复习日期：2026-09-17（第 2 次复习 · 一刷 2026-08-27 · 一刷非一次 AC）
// 一刷思路：双变量贪心（first / second 单链更新），时间 O(n) 空间 O(1)；一刷为 3 轮随机对拍才收敛（second 被写成 ≤ first 的不变量破坏）
// 上一次复习：同思路，一次 AC 且无探讨（判较强）；写法补了长度 < 3 提前返回与显式条件分支，与紧凑版等价
// 子类型：双变量贪心 —— 维护「已扫描部分的最小值」+「前面存在更小值的最小候选」这组不变量
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归用例，含随机对拍沉淀的 10~15 号回归例）
//
// 思路：双变量贪心——first 记已扫描部分的最小值，second 记「前面存在比它更小的值」的最小候选；每个数只走一条 if/else 链：
//       更小则刷新 first，落在 (first, second) 之间则刷新 second，一旦出现比 second 大的数（且 second > first）即命中递增三元组
// 复杂度：时间 O(n) 空间 O(1)
// ============================================================

public class LC0334_IncreasingTripletSubsequence {

    // ==== 提交代码开始 ====
    public boolean increasingTriplet(int[] nums) {
        if (nums.length < 3) return false;
        int first = nums[0];
        int second = Integer.MAX_VALUE;
        for (int i = 1; i < nums.length; i++) {
            // 不变量：first 是已扫描部分的最小值；second 只在「num > first」时刷新，所以恒有 second > first，
            // 且 second 的前驱 first 必定出现在它之前 —— 每个数只进一条分支，等号不落任何分支（严格递增）
            if (nums[i] < first) {
                first = nums[i];
            } else if (nums[i] > first && nums[i] < second) {
                second = nums[i];
            } else if (nums[i] > second && second > first) {
                return true;
            }
        }
        return false;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0334_IncreasingTripletSubsequence s = new LC0334_IncreasingTripletSubsequence();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{1, 2, 3, 4, 5}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{5, 4, 3, 2, 1}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{2, 1, 5, 0, 4, 6}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（针对本题逻辑与约束设计）----
        // 约束：1 <= nums.length <= 5*10^5，-2^31 <= nums[i] <= 2^31-1；找长度为3的递增子序列
        // 边界1: 单元素 → false
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{1}), "边界1: 单元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 两元素递增 → false
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{1, 2}), "边界2: 两元素")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 全部相同 → false
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{7, 7, 7}), "边界3: 全相同")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 严格递增三元素 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{1, 2, 3}), "边界4: 递增三元")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 先降后升但三元成立（[2,1,5,0,4] → 1,4 后缺第三个？[1,5]之后无更大，实际 false）
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{2, 1, 5, 0, 4}), "边界5: 中段无第三个")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 需要前两个都足够小（[0,5,1,2] → 1,2 有但 0 在前，0<1<2 成立 → true）
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{0, 5, 1, 2}), "边界6: 小元素在前")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }
        // 边界7: 大数范围边界（-2^31 与 2^31-1），[min,0,max] → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}), "边界7: 极值范围")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界7 异常: " + t); }
        // 边界8: 5*10^5 上限，整体递增 → true
        try {
            int[] big = new int[500000];
            for (int i = 0; i < big.length; i++) big[i] = i;
            if (!TestUtil.checkEq(true, s.increasingTriplet(big), "边界8: 5万上限递增")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界8 异常: " + t); }
        // 边界9: 5*10^5 上限，整体递减 → false
        try {
            int[] big = new int[500000];
            for (int i = 0; i < big.length; i++) big[i] = big.length - i;
            if (!TestUtil.checkEq(false, s.increasingTriplet(big), "边界9: 5万上限递减")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界9 异常: " + t); }
        // 边界10-15: 随机对拍发现的 bug 例（numB 被写成等于 numA 导致误判）
        // 边界10: [0,-8,-8,-7] → 无严格递增三元（-8,-8 相等不成）
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{0, -8, -8, -7}), "边界10: 相等中间值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界10 异常: " + t); }
        // 边界11: [3,3,3,-2,-8,10,-2] → 无严格递增三元（-8<-2<10 但 -2 在 -8 前，需 -8 索引在 -2 前）
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{3, 3, 3, -2, -8, 10, -2}), "边界11: 重复前缀")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界11 异常: " + t); }
        // 边界12: [MAX,-7,-7,-2] → 无严格递增三元
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{Integer.MAX_VALUE, -7, -7, -2}), "边界12: MAX前缀")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界12 异常: " + t); }
        // 边界13: [5,2,0,0,MAX] → 无严格递增三元
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{5, 2, 0, 0, Integer.MAX_VALUE}), "边界13: 相等谷值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界13 异常: " + t); }
        // 边界14: [4,-1,-1,1,-6] → 无严格递增三元
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{4, -1, -1, 1, -6}), "边界14: 重复负值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界14 异常: " + t); }
        // 边界15-20: 对拍发现的漏判例（numA 更新被 numB 状态误拦截 → 应 true 返回 false）
        // 边界15: [2,0,-7,4,-3,4,-5,-5] → -7<-3<4 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{2, 0, -7, 4, -3, 4, -5, -5}), "边界15: 前段更小")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界15 异常: " + t); }
        // 边界16: [3,-7,3,MAX-1,10,2,0,3] → -7<3<10 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{3, -7, 3, Integer.MAX_VALUE - 1, 10, 2, 0, 3}), "边界16: 中段插入")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界16 异常: " + t); }
        // 边界17: [4,MIN,-3,9] → MIN<-3<9 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{4, Integer.MIN_VALUE, -3, 9}), "边界17: MIN前缀")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界17 异常: " + t); }
        // 边界18: [5,0,3,4,MAX,-1] → 0<3<4 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{5, 0, 3, 4, Integer.MAX_VALUE, -1}), "边界18: 尾部MAX")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界18 异常: " + t); }
        // 边界19: [MAX-1,-1,5,7] → -1<5<7 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{Integer.MAX_VALUE - 1, -1, 5, 7}), "边界19: 前缀大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界19 异常: " + t); }
        // 边界20: [2,-4,-4,-2,MIN,-10,MAX,-8] → -4<-2<MAX 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{2, -4, -4, -2, Integer.MIN_VALUE, -10, Integer.MAX_VALUE, -8}), "边界20: 交错前缀")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界20 异常: " + t); }
        // 边界21-26: 对拍发现的误判例（numA 变小后旧 numB 下标失效 → 应 false 返回 true）
        // 边界21: [MAX,3,3,-1,10,-1,-7,8] → 无严格递增三元（-7<8 但中间无数介于两者之间且下标靠后）
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{Integer.MAX_VALUE, 3, 3, -1, 10, -1, -7, 8}), "边界21: 旧B失效")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界21 异常: " + t); }
        // 边界22: [3,9,-9,-9,4,0] → 无严格递增三元
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{3, 9, -9, -9, 4, 0}), "边界22: 相等负值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界22 异常: " + t); }
        // 边界23: [MAX-1,-1,MAX-1,-1,-5,5,-1] → 无严格递增三元
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{Integer.MAX_VALUE - 1, -1, Integer.MAX_VALUE - 1, -1, -5, 5, -1}), "边界23: 交替重复")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界23 异常: " + t); }
        // 边界24: [MAX,-2,3,-2,-10,0,0,-10] → 无严格递增三元
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{Integer.MAX_VALUE, -2, 3, -2, -10, 0, 0, -10}), "边界24: 重复中值")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界24 异常: " + t); }
        // 边界25: [9,6,-1,8,-1,-7,MIN,1] → 无严格递增三元
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{9, 6, -1, 8, -1, -7, Integer.MIN_VALUE, 1}), "边界25: 前缀大数")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界25 异常: " + t); }
        // 边界26: [-4,-5,8,-5,0] → 无严格递增三元
        try {
            if (!TestUtil.checkEq(false, s.increasingTriplet(new int[]{-4, -5, 8, -5, 0}), "边界26: 负数重复")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界26 异常: " + t); }
        // 边界27-32: 对拍发现的漏判例（first 被更大值无条件覆盖 → 应 true 返回 false）
        // 边界27: [-10,MAX,-7,4,-1] → -10<-7<4 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{-10, Integer.MAX_VALUE, -7, 4, -1}), "边界27: first被覆盖")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界27 异常: " + t); }
        // 边界28: [-1,MAX,8,-6,MAX-1,7] → -1<7 后…实际 -1<8? 需三元：-1,7? -1<8<... MAX-1? -1<8<MAX-1 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{-1, Integer.MAX_VALUE, 8, -6, Integer.MAX_VALUE - 1, 7}), "边界28: 大数交替")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界28 异常: " + t); }
        // 边界29: [4,5,-8,1,-1,-9,-1,-5,-8,-2] → -8<1? 需三元：-9<-5<-2 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{4, 5, -8, 1, -1, -9, -1, -5, -8, -2}), "边界29: 尾部三元")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界29 异常: " + t); }
        // 边界30: [4,-1,MAX,5,5,1,MAX,1,MAX] → -1<5<MAX 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{4, -1, Integer.MAX_VALUE, 5, 5, 1, Integer.MAX_VALUE, 1, Integer.MAX_VALUE}), "边界30: MAX重复")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界30 异常: " + t); }
        // 边界31: [7,-1,MAX,7,9,3,-1,-5,1] → -1<7<9 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{7, -1, Integer.MAX_VALUE, 7, 9, 3, -1, -5, 1}), "边界31: 中段三元")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界31 异常: " + t); }
        // 边界32: [MAX-1,4,MIN,MAX,-6,-8,9,7] → 4<9? 需三元：MIN<-8<9 存在 → true
        try {
            if (!TestUtil.checkEq(true, s.increasingTriplet(new int[]{Integer.MAX_VALUE - 1, 4, Integer.MIN_VALUE, Integer.MAX_VALUE, -6, -8, 9, 7}), "边界32: 大数前缀")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界32 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
