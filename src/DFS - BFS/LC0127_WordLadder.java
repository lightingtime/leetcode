// ============================================================
// LeetCode 127. 单词接龙 (Word Ladder)
// 难度：Hard | 分类：DFS / BFS
// 链接：https://leetcode.cn/problems/word-ladder/
// 刷题日期：2026-08-25
//
// ============================================================

import java.util.*;

public class LC0127_WordLadder {

    // ==== 提交代码开始 ====
    Map<String, Integer> map;
    List<List<Integer>> edge;
    int nodeNum;
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) {
            return 0;
        }
        map = new HashMap<>();
        edge = new ArrayList<>();
        nodeNum = 0;
        for (String word : wordList) {
            addEdge(word);
        }
        addEdge(beginWord);
        Set<Integer> beginSet = new HashSet<>();
        Set<Integer> endSet = new HashSet<>();

        Set<Integer> visitedBegin = new HashSet<>();  // begin 侧已访问
        Set<Integer> visitedEnd = new HashSet<>();    // end 侧已访问
        int levelA = 0;                               // begin 侧当前层的虚拟边数
        int levelB = 0;                               // end 侧当前层的虚拟边数
        beginSet.add(map.get(beginWord));
        endSet.add(map.get(endWord));
        visitedBegin.add(map.get(beginWord));
        visitedEnd.add(map.get(endWord));
        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            if (beginSet.size() > endSet.size()) {
                Set<Integer> temp = new HashSet<>(beginSet);
                beginSet = endSet;
                endSet = temp;
                temp = new HashSet<>(visitedBegin);
                visitedBegin = visitedEnd;
                visitedEnd = temp;
                int c = levelB;
                levelB = levelA;
                levelA = c;
            }
            Set<Integer> next = new HashSet<>();
            for (Integer u : beginSet) {
                for (Integer v : edge.get(u)) {
                    if (visitedEnd.contains(v)) {
                        return ((levelA + 1) + levelB) / 2 + 1;
                    }
                    if (!visitedBegin.contains(v)) {
                        next.add(v);
                        visitedBegin.add(v);
                    }
                }
            }
            beginSet = next;
            levelA++;
        }
        return 0;
    }

    private void addEdge(String word) {
        addWord(word);
        int id1 = map.get(word);
        char[] charArray = word.toCharArray();
        int len = charArray.length;
        for (int i = 0; i < len; i++) {
            char temp = charArray[i];
            charArray[i] = '*';
            String newWord = new String(charArray);
            addWord(newWord);
            int id2 = map.get(newWord);
            edge.get(id1).add(id2);
            edge.get(id2).add(id1);
            charArray[i] = temp;
        }
    }

    private void addWord(String newWord) {
        if (!map.containsKey(newWord)) {
            map.put(newWord, nodeNum++);
            edge.add(new ArrayList<>());
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0127_WordLadder s = new LC0127_WordLadder();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(5, s.ladderLength("hit", "cog", Arrays.asList("hot", "dot", "dog", "lot", "log", "cog")), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            if (!TestUtil.checkEq(0, s.ladderLength("hit", "cog", Arrays.asList("hot", "dot", "dog", "lot", "log")), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }

        // ---- 边界测试（针对本题：一步直达、长链、end 不在字典、无通路、begin 在字典、多步路径）----
        // 边界1: 一步直达（单字母词，任意两字母只差 1 个字符）
        try {
            if (!TestUtil.checkEq(2, s.ladderLength("a", "c", Arrays.asList("c")), "边界1: 一步直达")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 2 字母长链（8 步）
        try {
            if (!TestUtil.checkEq(9, s.ladderLength("aa", "ee", Arrays.asList("ab", "bb", "bc", "cc", "cd", "dd", "de", "ee")), "边界2: 长链")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: endWord 不在字典 -> 0
        try {
            if (!TestUtil.checkEq(0, s.ladderLength("hit", "cog", Arrays.asList("hot", "dot")), "边界3: end不在字典")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 无通路（begin 到 end 差 3 个字符且无中间词）-> 0
        try {
            if (!TestUtil.checkEq(0, s.ladderLength("abc", "xyz", Arrays.asList("xyz")), "边界4: 无通路")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: beginWord 本身在字典中（不应造成重复计数）
        try {
            if (!TestUtil.checkEq(2, s.ladderLength("b", "c", Arrays.asList("b", "c")), "边界5: begin在字典")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 2 字母多步路径
        try {
            if (!TestUtil.checkEq(5, s.ladderLength("aa", "cc", Arrays.asList("ab", "bb", "bc", "cc")), "边界6: 多步路径")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}