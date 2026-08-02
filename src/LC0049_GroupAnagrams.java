// ============================================================
// LeetCode 49. 字母异位词分组 (Group Anagrams)
// 难度：Medium | 分类：哈希表
// 链接：https://leetcode.cn/problems/group-anagrams/
// 刷题日期：2026-08-03
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0049_GroupAnagrams {

    // ==== 提交代码开始 ====
    public List<List<String>> groupAnagrams(String[] strs) {
        // TODO: 在这里实现你的解法
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            String pre = cal(str);
            if (map.containsKey(pre)) {
                map.get(pre).add(str);
            } else {
                List<String> list = new ArrayList<>();
                list.add(str);
                map.put(pre, list);
            }
        }
        return map.values().stream().toList();
    }

	private String cal(String str) {
        int[] counts = new int[26];
        for (Character c : str.toCharArray()) {
            counts[c - 'a']++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (counts[i] > 0) {
                sb.append(i + 'a');
                sb.append(counts[i]);
            }
        }
		return sb.toString();

	}
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0049_GroupAnagrams s = new LC0049_GroupAnagrams();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            List<List<String>> expected = new ArrayList<>();
            expected.add(Arrays.asList("bat"));
            expected.add(Arrays.asList("nat", "tan"));
            expected.add(Arrays.asList("ate", "eat", "tea"));
            if (!checkGroups(expected, s.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}), "示例1")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try {
            List<List<String>> expected = new ArrayList<>();
            expected.add(Arrays.asList(""));
            if (!checkGroups(expected, s.groupAnagrams(new String[]{""}), "示例2")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try {
            List<List<String>> expected = new ArrayList<>();
            expected.add(Arrays.asList("a"));
            if (!checkGroups(expected, s.groupAnagrams(new String[]{"a"}), "示例3")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        // TODO: 补充空输入 / 单元素 / 全相同 / 大数等边界
        // 例如： try { if (!TestUtil.checkEq(期望, s.groupAnagrams(边界输入), "边界1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 若题目允许任意顺序返回（下标对 / 集合），用 TestUtil.checkEqUnordered 代替 TestUtil.checkEq

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

    // 字母异位词分组：外层分组顺序与组内字符串顺序都允许任意，统一排序后比较
    private static boolean checkGroups(List<List<String>> expected, List<List<String>> actual, String label) {
        return TestUtil.checkEq(sortedGroups(expected), sortedGroups(actual), label);
    }

    private static List<List<String>> sortedGroups(List<List<String>> groups) {
        List<List<String>> out = new ArrayList<>();
        for (List<String> g : groups) {
            List<String> sorted = new ArrayList<>(g);
            Collections.sort(sorted);
            out.add(sorted);
        }
        out.sort(Comparator.comparing(g -> String.join(",", g)));
        return out;
    }

}
