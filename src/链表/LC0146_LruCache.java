// ============================================================
// LeetCode 146. LRU 缓存 (LRU Cache)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/lru-cache/
// 刷题日期：2026-09-21
//
// 二刷第 4 次 · 一刷 2026-08-07 · 上次复习 2026-09-05（较强，间隔 15 天）
// 一刷思路：HashMap 存 key -> 节点 + 双向链表维护访问顺序，get/put 都是 O(1)
// 已有的两种写法：① 哈希表+双向链表（08-07）；② HashMap+双向链表（哨兵头尾，09-05）。本次沿用哪种就按哪种合并
// 测试用例与一刷归档保持一致（示例调用序列 1 + 边界 3），未重新设计
// ============================================================

import java.util.*;

public class LC0146_LruCache {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 LRUCache 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class LRUCache {
        Map<Integer, Node> map;
        int capacity;
        Node head;
        Node tail;
        public LRUCache(int capacity) {
            map = new HashMap<>();
            this.capacity = capacity;
            head = new Node();
            tail = new Node();
            head.next = tail;
            tail.pre = head;
        }
        public int get(int key) {
            if (!map.containsKey(key)) {
                return -1;
            }
            Node node = map.get(key);
            unLinkNoe(node);
            move2Head(node);
            return node.val;
        }

        private void move2Head(Node node) {
            head.next.pre = node;
            node.next = head.next;
            node.pre = head;
            head.next = node;
        }

        private void unLinkNoe(Node node) {
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }

        public void put(int key, int value) {
            Node node;
            if (map.containsKey(key)) {
                node = map.get(key);
                node.val = value;
                unLinkNoe(node);
            } else {
                node = new Node(key, value);
                map.put(key, node);
            }
            move2Head(node);
            if (map.size() > capacity) {
                Node last = tail.pre;
                unLinkNoe(last);
                // 两份结构必须同步增删：断链的同时要从 map 里移除，否则 map 与链表会失去一致性
                map.remove(last.key);
            }
        }
    }

    private static class Node {
        int key;
        int val;
        Node next;
        Node pre;
        public Node(){

        }

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        // 设计题：按题目示例手动构造调用序列
        int failures = 0;

        // ---- 示例测试（题目示例）----
        try {
            LRUCache cache = new LRUCache(2);
            cache.put(1, 1);
            cache.put(2, 2);
            if (cache.get(1) != 1) failures++;
            cache.put(3, 3);
            if (cache.get(2) != -1) failures++;
            cache.put(4, 4);
            if (cache.get(1) != -1) failures++;
            if (cache.get(3) != 3) failures++;
            if (cache.get(4) != 4) failures++;
            System.out.println("示例-标准LRU序列 通过");
        } catch (Throwable t) { failures++; System.out.println("示例-标准LRU序列 异常: " + t); }

        // ---- 边界测试 ----
        try {
            LRUCache c = new LRUCache(1);
            c.put(1, 1);
            if (c.get(1) != 1) failures++;
            c.put(2, 2); // 容量 1，逐出 key=1
            if (c.get(1) != -1) failures++;
            if (c.get(2) != 2) failures++;
            System.out.println("边界-capacity=1 通过");
        } catch (Throwable t) { failures++; System.out.println("边界-capacity=1 异常: " + t); }
        try {
            LRUCache c = new LRUCache(2);
            if (c.get(9) != -1) failures++; // 空缓存 get
            c.put(1, 1);
            c.put(2, 2);
            c.get(1); // get 刷新：1 变成最近使用
            c.put(3, 3); // 应逐出 2（1 刚被刷新）
            if (c.get(2) != -1) failures++;
            if (c.get(1) != 1) failures++;
            System.out.println("边界-get刷新顺序 通过");
        } catch (Throwable t) { failures++; System.out.println("边界-get刷新顺序 异常: " + t); }
        try {
            LRUCache c = new LRUCache(2);
            c.put(1, 1);
            c.put(2, 2);
            c.put(1, 2); // 更新已存在 key，视为使用（1 变最近）
            if (c.get(1) != 2) failures++;
            c.put(3, 3); // 应逐出 2（1 刚被更新）
            if (c.get(1) != 2) failures++;
            if (c.get(2) != -1) failures++;
            System.out.println("边界-更新已存在key 通过");
        } catch (Throwable t) { failures++; System.out.println("边界-更新已存在key 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
