// ============================================================
// LeetCode 341. 扁平化嵌套列表迭代器 (Flatten Nested List Iterator)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/flatten-nested-list-iterator/
// 二刷复习：2026-09-24（一刷 2026-08-24）
// 测试用例与一刷归档保持一致（示例 + 边界 + 回归）
//
// ============================================================

import java.util.*;

public class LC0341_FlattenNestedListIterator {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 NestedIterator 在提交时自动处理。
    // 本地 NestedInteger 接口（判题环境自带，这里仅为本地测试镜像）
    interface NestedInteger {
        boolean isInteger();

        Integer getInteger();

        List<NestedInteger> getList();
    }

    static class NestedIntegerImpl implements NestedInteger {
        private final Integer value;
        private final List<NestedInteger> list;

        NestedIntegerImpl(Integer value) {
            this.value = value;
            this.list = null;
        }

        NestedIntegerImpl(List<NestedInteger> list) {
            this.value = null;
            this.list = list;
        }

        @Override
        public boolean isInteger() {
            return value != null;
        }

        @Override
        public Integer getInteger() {
            return value;
        }

        @Override
        public List<NestedInteger> getList() {
            return list != null ? list : Collections.emptyList();
        }
    }

    // ==== 提交代码开始 ====
    static class NestedIterator {
        Deque<NestedInteger> stack;
        public NestedIterator(List<NestedInteger> nestedList) {
            stack = new ArrayDeque<>();
            for (int i = nestedList.size() - 1; i >= 0; i--) {
                stack.push(nestedList.get(i));
            }
        }

        public Integer next() {
            return stack.pop().getInteger();
        }

        public boolean hasNext() {
            if (stack.isEmpty()) {
                return false;
            }
            while (!stack.isEmpty() && !stack.peek().isInteger()) {
                List<NestedInteger> list = stack.pop().getList();
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            }
            return !stack.isEmpty();
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 示例测试（来自题目）----
        // 示例1: [[1,1],2,[1,1]] → [1,1,2,1,1]
        try {
            List<NestedInteger> nl = Arrays.asList(ni(1), ni(1));
            List<NestedInteger> input = Arrays.asList(ni(Arrays.asList(1, 1)), ni(2), ni(Arrays.asList(1, 1)));
            if (!TestUtil.checkEq(Arrays.asList(1, 1, 2, 1, 1), collect(new NestedIterator(input)), "示例1"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        // 示例2: [1,[4,[6]]] → [1,4,6]
        try {
            List<NestedInteger> input = Arrays.asList(ni(1), ni(Arrays.asList(4, Arrays.asList(6))));
            if (!TestUtil.checkEq(Arrays.asList(1, 4, 6), collect(new NestedIterator(input)), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }

        // ---- 边界测试（自己补充）----
        // 约束：nestedList 长度 >= 1，整数值范围 [-10^6, 10^6]
        // 边界1: 单整数
        try {
            if (!TestUtil.checkEq(Arrays.asList(1), collect(new NestedIterator(Arrays.asList(ni(1)))), "边界1: 单整数"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1 异常: " + t);
        }
        // 边界2: 深层嵌套 [[[[1]]]]
        try {
            List<NestedInteger> input = Arrays.asList(ni(Arrays.asList(Arrays.asList(Arrays.asList(1)))));
            if (!TestUtil.checkEq(Arrays.asList(1), collect(new NestedIterator(input)), "边界2: 深层嵌套")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2 异常: " + t);
        }
        // 边界3: 空子列表 [[]]，无整数输出
        try {
            if (!TestUtil.checkEq(Collections.emptyList(), collect(new NestedIterator(Arrays.asList(ni(Arrays.asList())))), "边界3: 空列表"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3 异常: " + t);
        }
        // 边界4: 空列表 + 整数混合 [[],[3]]
        try {
            List<NestedInteger> input = Arrays.asList(ni(Arrays.asList()), ni(3));
            if (!TestUtil.checkEq(Arrays.asList(3), collect(new NestedIterator(input)), "边界4: 空+整数")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4 异常: " + t);
        }
        // 边界5: 整数值边界 ±10^6
        try {
            List<NestedInteger> input = Arrays.asList(ni(1000000), ni(Arrays.asList(-1000000)));
            if (!TestUtil.checkEq(Arrays.asList(1000000, -1000000), collect(new NestedIterator(input)), "边界5: 值边界"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5 异常: " + t);
        }
        // 边界6: 多元素多级嵌套 [[1,[2,[3,[4,[5]]]]]]
        try {
            List<NestedInteger> input = Arrays.asList(ni(Arrays.asList(1, Arrays.asList(2, Arrays.asList(3, Arrays.asList(4, Arrays.asList(5)))))));
            if (!TestUtil.checkEq(Arrays.asList(1, 2, 3, 4, 5), collect(new NestedIterator(input)), "边界6: 多级嵌套"))
                failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 递归构造 NestedInteger：o 为 Integer 或 List<?>
    static NestedInteger ni(Object o) {
        if (o instanceof Integer) return new NestedIntegerImpl((Integer) o);
        List<NestedInteger> l = new ArrayList<>();
        for (Object x : (List<?>) o) l.add(ni(x));
        return new NestedIntegerImpl(l);
    }

    // 迭代收集全部整数
    static List<Integer> collect(NestedIterator it) {
        List<Integer> res = new ArrayList<>();
        while (it.hasNext()) res.add(it.next());
        return res;
    }

}
