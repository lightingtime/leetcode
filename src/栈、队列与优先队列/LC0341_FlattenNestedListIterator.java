// ============================================================
// LeetCode 341. 扁平化嵌套列表迭代器 (Flatten Nested List Iterator)
// 难度：Medium | 分类：栈、队列与优先队列
// 链接：https://leetcode.cn/problems/flatten-nested-list-iterator/
// 复习日期：2026-09-17（第 3 次复习 · 一刷 2026-08-24）
// 一刷写法：惰性栈——构造时逆序压栈，hasNext 循环展开栈顶列表直到露出整数或栈空，next 弹栈顶
// 一刷问题：展开循环起点写成 size()（越界）；hasNext 只展开一次就 return true（深层嵌套时栈顶仍是列表）；展开成空栈后仍 return true、继续 peek 栈顶 NPE；Deque 混端操作丢掉栈语义
// 上次复习写法：同样是惰性栈，一次 AC 但有探讨（判较弱）
// 测试用例：与一刷归档保持一致（示例 2 + 边界 6：单整数、深层嵌套、空子列表、空列表混整数、±10^6、多级嵌套）
// ============================================================

import java.util.*;

public class LC0341_FlattenNestedListIterator {

    // 本地 NestedInteger 接口（判题环境自带，这里仅为本地测试镜像）
    interface NestedInteger {
        boolean isInteger();

        Integer getInteger();

        List<NestedInteger> getList();
    }

    static class NestedIntegerImpl implements NestedInteger {
        private final Integer value;
        private final List<NestedInteger> list;

        NestedIntegerImpl(Integer v) {
            value = v;
            list = null;
        }

        NestedIntegerImpl(List<NestedInteger> l) {
            value = null;
            list = l;
        }

        public boolean isInteger() {
            return value != null;
        }

        public Integer getInteger() {
            return value;
        }

        public List<NestedInteger> getList() {
            return list != null ? list : Collections.emptyList();
        }
    }

    // ==== 提交代码开始 ====
    // 参数 nestedList：外层嵌套列表；NestedIterator 按深度优先顺序惰性吐出其中所有整数
    static class NestedIterator implements Iterator<Integer> {
        Deque<NestedInteger> stack;

        public NestedIterator(List<NestedInteger> nestedList) {
            // 逆序压栈：栈顶是外层列表最左边的元素，pop 顺序即从左到右
            stack = new ArrayDeque<>();
            for (int i = nestedList.size() - 1; i >= 0; i--) {
                stack.push(nestedList.get(i));
            }
        }

        @Override
        public Integer next() {
            return stack.pop().getInteger();
        }

        @Override
        public boolean hasNext() {
            if (stack.isEmpty()) {
                return false;
            }
            // 必须 while 一直展开到栈顶是整数：只展开一层的话深层嵌套时 next 会取到列表
            while (!stack.isEmpty() && !stack.peek().isInteger()) {
                List<NestedInteger> list = stack.pop().getList();
                for (int i = list.size() - 1; i >= 0; i--) {
                    stack.push(list.get(i));
                }
            }
            // 展开后栈可能已经空了（整层都是空列表），所以返回值要看栈状态而不是无条件 true
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
