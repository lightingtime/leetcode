// ============================================================
// LeetCode 22. 括号生成 (Generate Parentheses)
// 难度：Medium | 分类：动态规划
// 链接：https://leetcode.cn/problems/generate-parentheses/
// 复习日期：2026-09-18（第 1 次复习 · 一刷 2026-08-09 · 一刷一次 AC）
// 一刷/本次写法：回溯（剩余计数剪枝）——dfs(剩余左, 剩余右, StringBuilder)，剩余左 > 剩余右 即前缀非法直接剪枝，O(C_n·n)/O(n)
// 测试用例与一刷归档保持一致（示例 n=3/1 + 边界 n=2/4/8）
//
// 思路：l/r 是「还剩几个左/右括号」，每一步只有放 '(' 或 ')' 两种选择；剩余数走负说明这条分支已经走过头，直接返回；
//       剩余左 > 剩余右 说明已放下的 ')' 多于 '('、前缀已经非法，剪掉；l==r==0 时产生一个合法串，StringBuilder 追加后立即撤销
// 复杂度：时间 O(C_n·n) 空间 O(n)
// ============================================================

import java.util.*;

public class LC0022_GenerateParentheses {

    // ==== 提交代码开始 ====
    List<String> ans;
    public List<String> generateParenthesis(int n) {
        ans = new ArrayList<>();
        dfs(n, n, new StringBuilder());
        return ans;
    }

    // l/r：还剩几个左/右括号
    private void dfs(int l, int r, StringBuilder sb) {
        if (l < 0 || r < 0) {
            return;
        }
        if (l == r && l == 0) {
            ans.add(sb.toString());
            return;
        }
        // 剩余左括号比右括号多 ⇒ 已放下的 ')' 已经超过 '('，前缀非法
        if (l > r) {
            return;
        }
        sb.append("(");
        dfs(l - 1, r, sb);
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");
        dfs(l, r - 1, sb);
        sb.deleteCharAt(sb.length() - 1);
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0022_GenerateParentheses s = new LC0022_GenerateParentheses();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("((()))", "(()())", "(())()", "()(())", "()()()"), s.generateParenthesis(3), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(Arrays.asList("()"), s.generateParenthesis(1), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try {
            if (!TestUtil.checkEqUnordered(Arrays.asList("(())", "()()"), s.generateParenthesis(2), "边界-n=2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界-n=2 异常: " + t); }
        try {
            List<String> r4 = s.generateParenthesis(4);
            boolean ok = r4 != null && r4.size() == 14 && r4.stream().distinct().count() == 14;
            if (ok) {
                for (String str : r4) {
                    int bal = 0;
                    for (char c : str.toCharArray()) {
                        if (c == '(') bal++; else bal--;
                        if (bal < 0) { ok = false; break; }
                    }
                    if (bal != 0) ok = false;
                }
            }
            if (!ok) {
                failures++;
                System.out.println("边界-n=4 失败 ✗ 期望14个互不相同的合法括号组合，实际=" + (r4 == null ? "null" : r4.size()));
            } else {
                System.out.println("边界-n=4 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-n=4 异常: " + t); }
        try {
            List<String> r8 = s.generateParenthesis(8);
            boolean ok = r8 != null && r8.size() == 1430 && r8.stream().distinct().count() == 1430;
            if (!ok) {
                failures++;
                System.out.println("边界-n=8 失败 ✗ 期望1430个互不相同的组合，实际=" + (r8 == null ? "null" : r8.size()));
            } else {
                System.out.println("边界-n=8 通过 ✓");
            }
        } catch (Throwable t) { failures++; System.out.println("边界-n=8 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
