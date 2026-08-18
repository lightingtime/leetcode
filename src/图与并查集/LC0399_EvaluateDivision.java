// ============================================================
// LeetCode 399. 除法求值 (Evaluate Division)
// 难度：Medium | 分类：图与并查集
// 链接：https://leetcode.cn/problems/evaluate-division/
// 刷题日期：2026-08-18
//
// ============================================================

import java.util.*;

public class LC0399_EvaluateDivision {

    // ==== 提交代码开始 ====
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0);
            String b = equations.get(i).get(1);
            double value = values[i];

            graph.computeIfAbsent(a, k -> new HashMap<>()).merge(b, value, (oldVal, newVal) -> newVal);
            graph.computeIfAbsent(b, k -> new HashMap<>()).merge(a, 1 / value, (oldVal, newVal) -> newVal);
        }
        double[] ans = new double[queries.size()];
        int index = 0;
        for (List<String> query : queries) {
            String a = query.get(0);
            String b = query.get(1);
            if (graph.containsKey(a)) {
                if (a.equals(b)) {
                    ans[index] = 1.0;
                } else {
                    ans[index] = findAns(a, b, 1.0, graph, new HashSet<String>());
                }
            } else {
                ans[index] = -1.0;
            }
            index++;
        }
        return ans;
    }

    private double findAns(String a, String b, double cur, Map<String, Map<String, Double>> graph, HashSet<String> visited) {
        if (a.equals(b)) {
            return cur;
        }
        visited.add(a);
        for (Map.Entry<String, Double> entry : graph.get(a).entrySet()) {
            String next = entry.getKey();
            if (visited.contains(next)) {
                continue;
            }
            double r = findAns(next, b, cur * entry.getValue(), graph, visited);
            if (r != -1.0) return r;
        }
        return -1.0;
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0399_EvaluateDivision s = new LC0399_EvaluateDivision();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(new double[]{6.00000, 0.50000, -1.00000, 1.00000, -1.00000}, s.calcEquation(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "c")), new double[]{2.0, 3.0}, Arrays.asList(Arrays.asList("a", "c"), Arrays.asList("b", "a"), Arrays.asList("a", "e"), Arrays.asList("a", "a"), Arrays.asList("x", "x"))), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new double[]{3.75000, 0.40000, 5.00000, 0.20000}, s.calcEquation(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "c"), Arrays.asList("bc", "cd")), new double[]{1.5, 2.5, 5.0}, Arrays.asList(Arrays.asList("a", "c"), Arrays.asList("c", "b"), Arrays.asList("bc", "cd"), Arrays.asList("cd", "bc"))), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            if (!TestUtil.checkEq(new double[]{0.50000, 2.00000, -1.00000, -1.00000}, s.calcEquation(Arrays.asList(Arrays.asList("a", "b")), new double[]{0.5}, Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "a"), Arrays.asList("a", "c"), Arrays.asList("x", "y"))), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1-空等式：没有任何已知条件，所有查询（含 a/a）都未定义 -> -1.0
        try { if (!TestUtil.checkEq(new double[]{-1.0, -1.0}, s.calcEquation(new ArrayList<>(), new double[]{}, Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("a", "a"))), "边界1-空等式")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1-空等式 异常: " + t); }
        // 边界2-已定义但不连通：a/b、c/d 是两个独立分量，跨分量查询 -> -1.0（不是 0，也不是某个值）
        try { if (!TestUtil.checkEq(new double[]{-1.0, -1.0, -1.0}, s.calcEquation(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("c", "d")), new double[]{2.0, 3.0}, Arrays.asList(Arrays.asList("a", "d"), Arrays.asList("d", "a"), Arrays.asList("b", "c"))), "边界2-不连通")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2-不连通 异常: " + t); }
        // 边界3-长链乘法：a/b=2, b/c=3, c/d=4, d/e=5 -> a/e = 2*3*4*5 = 120.0
        try { if (!TestUtil.checkEq(new double[]{120.0}, s.calcEquation(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "c"), Arrays.asList("c", "d"), Arrays.asList("d", "e")), new double[]{2.0, 3.0, 4.0, 5.0}, Arrays.asList(Arrays.asList("a", "e"))), "边界3-长链乘法")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3-长链乘法 异常: " + t); }
        // 边界4-自环与重复查询：图内 a/a=1、b/b=1；同一查询出现多次结果一致
        try { if (!TestUtil.checkEq(new double[]{1.0, 1.0, 6.0, 6.0}, s.calcEquation(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("b", "c")), new double[]{2.0, 3.0}, Arrays.asList(Arrays.asList("a", "a"), Arrays.asList("b", "b"), Arrays.asList("a", "c"), Arrays.asList("a", "c"))), "边界4-自环与重复查询")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4-自环与重复查询 异常: " + t); }
        // 边界5-含数字变量名 + 反向精确值：a1/b2=4，b2/a1=0.25
        try { if (!TestUtil.checkEq(new double[]{4.0, 0.25}, s.calcEquation(Arrays.asList(Arrays.asList("a1", "b2")), new double[]{4.0}, Arrays.asList(Arrays.asList("a1", "b2"), Arrays.asList("b2", "a1"))), "边界5-数字变量名与反向")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5-数字变量名与反向 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}