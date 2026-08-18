// ============================================================
// LeetCode 338. 比特位计数 (Counting Bits)
// 难度：Easy | 分类：动态规划
// 链接：https://leetcode.cn/problems/counting-bits/
// 刷题日期：2026-08-18
//
// ============================================================

import java.util.*;

public class LC0338_CountingBits {

    // ==== 提交代码开始 ====
    public int[] countBits(int n) {
        int[] ans = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            ans[i] = ans[i >> 1] + (i & 1);
        }
        return ans;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0338_CountingBits s = new LC0338_CountingBits();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new int[]{0, 1, 1}, s.countBits(2), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new int[]{0, 1, 1, 2, 1, 2}, s.countBits(5), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1-n=0：只有一个元素，0 的 1 位数是 0
        try { if (!TestUtil.checkEq(new int[]{0}, s.countBits(0), "边界1-n=0")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-n=0 异常: " + t); }
        // 边界2-n=1：0 -> 0，1 -> 1
        try { if (!TestUtil.checkEq(new int[]{0, 1}, s.countBits(1), "边界2-n=1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-n=1 异常: " + t); }
        // 边界3-2 的幂与全 1 临界：n=16，验证 15(1111)=4、16(10000)=1 等整段
        try { if (!TestUtil.checkEq(new int[]{0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1}, s.countBits(16), "边界3-幂与全1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3-幂与全1 异常: " + t); }
        // 边界4-n=10 整段：与手算逐位核对
        try { if (!TestUtil.checkEq(new int[]{0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2}, s.countBits(10), "边界4-n=10")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-n=10 异常: " + t); }
        // 边界5-大数抽查：n=100000，抽查长度与 2^16-1=16 个 1、2^16=1 个 1、100000=6 个 1
        try {
            int[] big = s.countBits(100000);
            boolean ok = big.length == 100001 && big[0] == 0 && big[65535] == 16 && big[65536] == 1 && big[100000] == 6;
            if (!ok) { failures++; System.out.println("边界5-大数抽查 失败 ✗ 期望 len=100001, [0]=0, [65535]=16, [65536]=1, [100000]=6 实际 len=" + big.length + " [65535]=" + big[65535] + " [65536]=" + big[65536] + " [100000]=" + big[100000]); }
            else System.out.println("边界5-大数抽查 通过 ✓");
        } catch (Throwable t) { failures++; System.out.println("边界5-大数抽查 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}