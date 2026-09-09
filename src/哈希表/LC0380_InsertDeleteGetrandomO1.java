// ============================================================
// LeetCode 380. O(1) 时间插入、删除和获取随机元素 (Insert Delete GetRandom O(1))
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/insert-delete-getrandom-o1/
// 复习日期：2026-09-09（第 2 次复习 · 一刷 2026-08-21 · 上次复习 2026-09-06）
// 二刷重开：测试用例与一刷归档保持一致；先回忆思路再动笔，不查归档解法。
//
// 思路：HashMap<值, 下标> + ArrayList 存值；插入 append 并记下标；
//       删除把末尾元素换到被删下标再删尾（避免数组搬移）；getRandom 取随机下标。
// 复杂度：时间均摊 O(1)，空间 O(n)
// ============================================================

import java.util.*;

public class LC0380_InsertDeleteGetrandomO1 {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 RandomizedSet 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class RandomizedSet {
        Map<Integer, Integer> map;
        List<Integer> list;
        Random random;
        public RandomizedSet() {
            map = new HashMap<>();
            list = new ArrayList<>();
            random = new Random();
        }
        public boolean insert(int val) {
            if (map.containsKey(val)) {
                return false;
            }
            list.add(val);
            map.put(val, list.size() - 1);
            return true;
        }
        public boolean remove(int val) {
            if (!map.containsKey(val)) {
                return false;
            }
            int index = map.get(val);
            int endVal = list.get(list.size() - 1);
            map.put(endVal, index);
            list.set(index, endVal);
            map.remove(val);
            list.remove(list.size() - 1);
            return true;
        }
        public int getRandom() {
            return list.get(random.nextInt(list.size()));
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            RandomizedSet s = new RandomizedSet();
            boolean ok = true;
            ok &= s.insert(1) == true;
            ok &= s.remove(2) == false;
            ok &= s.insert(2) == true;
            int r = s.getRandom();                       // 集合 {1,2}，应返回 1 或 2
            ok &= (r == 1 || r == 2);
            ok &= s.remove(1) == true;
            ok &= s.insert(2) == false;
            ok &= s.getRandom() == 2;                    // 只剩 2
            if (!ok) { failures++; System.out.println("示例 失败 ✗"); }
            else System.out.println("示例 通过 ✓");
        } catch (Throwable t) { failures++; System.out.println("示例 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // 边界1: 重复插入返回 false，重复删除返回 false
        try {
            RandomizedSet s = new RandomizedSet();
            boolean ok = true;
            ok &= s.insert(1) == true;
            ok &= s.insert(1) == false;
            ok &= s.remove(1) == true;
            ok &= s.remove(1) == false;
            if (!ok) { failures++; System.out.println("边界1 失败 ✗"); }
            else System.out.println("边界1: 重复插删 通过 ✓");
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }

        // 边界2: 删除中间元素后，getRandom 只返回剩余集合元素
        try {
            RandomizedSet s = new RandomizedSet();
            s.insert(1); s.insert(2); s.insert(3);
            boolean ok = s.remove(2) == true;            // 删除中间元素
            for (int i = 0; i < 200 && ok; i++) {
                int r = s.getRandom();
                if (r != 1 && r != 3) ok = false;
            }
            if (!ok) { failures++; System.out.println("边界2 失败 ✗"); }
            else System.out.println("边界2: 删中间后随机 通过 ✓");
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }

        // 边界3: 删除末尾元素后只剩一个
        try {
            RandomizedSet s = new RandomizedSet();
            s.insert(1); s.insert(2);
            boolean ok = s.remove(2) == true && s.getRandom() == 1;
            if (!ok) { failures++; System.out.println("边界3 失败 ✗"); }
            else System.out.println("边界3: 删末尾 通过 ✓");
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }

        // 边界4: int 端点值
        try {
            RandomizedSet s = new RandomizedSet();
            boolean ok = true;
            ok &= s.insert(Integer.MIN_VALUE) == true;
            ok &= s.insert(Integer.MAX_VALUE) == true;
            int r = s.getRandom();
            ok &= (r == Integer.MIN_VALUE || r == Integer.MAX_VALUE);
            if (!ok) { failures++; System.out.println("边界4 失败 ✗"); }
            else System.out.println("边界4: int 端点 通过 ✓");
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }

        // 边界5: 2*10^5 规模操作（插 10^5 个、删一半、随机抽样）
        try {
            RandomizedSet s = new RandomizedSet();
            boolean ok = true;
            for (int i = 0; i < 100_000; i++) ok &= s.insert(i);
            for (int i = 0; i < 50_000; i++) ok &= s.remove(i);
            for (int i = 0; i < 1000 && ok; i++) {
                int r = s.getRandom();
                if (r < 50_000 || r >= 100_000) ok = false;
            }
            if (!ok) { failures++; System.out.println("边界5 失败 ✗"); }
            else System.out.println("边界5: 大操作量 通过 ✓");
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }

        // 边界6: 删除中间元素后，再删除被换到前面的末尾元素（验证 map 下标同步）
        try {
            RandomizedSet s = new RandomizedSet();
            boolean ok = true;
            ok &= s.insert(1) == true;
            ok &= s.insert(2) == true;
            ok &= s.insert(3) == true;
            ok &= s.remove(1) == true;   // 3 被换到下标 0，map 中 3 的下标必须同步
            ok &= s.remove(3) == true;   // 若 map 没同步，会按旧下标删错/越界
            ok &= s.getRandom() == 2;
            if (!ok) { failures++; System.out.println("边界6 失败 ✗"); }
            else System.out.println("边界6: 换位后删除 通过 ✓");
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }
}