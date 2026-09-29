// ============================================================
// LeetCode 205. 同构字符串 (Isomorphic Strings)
// 难度：Easy | 分类：哈希表
// 链接：https://leetcode.cn/problems/isomorphic-strings/
// 刷题日期：2026-09-30
//
// 思路：双向哈希映射保证字符映射一致且一一对应
// 复杂度：时间 O(n)，空间 O(1)（ASCII 字符集大小固定）
// ============================================================

import java.util.*;

public class LC0205_IsomorphicStrings {

    // ==== 提交代码开始 ====
    public boolean isIsomorphic(String s, String t) {
        Map<Character, Character> map = new HashMap<>();
        Map<Character, Character> reverseMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (map.containsKey(s.charAt(i))) {
                if (map.get(s.charAt(i)) != t.charAt(i)) return false;
            } else {
                map.put(s.charAt(i), t.charAt(i));
            }
            if (reverseMap.containsKey(t.charAt(i))) {
                if (reverseMap.get(t.charAt(i)) != s.charAt(i)) return false;
            } else {
                reverseMap.put(t.charAt(i), s.charAt(i));
            }
        }
        return true;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0205_IsomorphicStrings s = new LC0205_IsomorphicStrings();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try { if (!TestUtil.checkEq(true, s.isIsomorphic("egg", "add"), "示例1：重复字符保持映射")) failures++; } catch (Throwable t) { failures++; System.out.println("示例1 异常: " + t); }
        try { if (!TestUtil.checkEq(false, s.isIsomorphic("f11", "b23"), "示例2：同一字符映射到不同字符")) failures++; } catch (Throwable t) { failures++; System.out.println("示例2 异常: " + t); }
        try { if (!TestUtil.checkEq(true, s.isIsomorphic("paper", "title"), "示例3：多组字符映射")) failures++; } catch (Throwable t) { failures++; System.out.println("示例3 异常: " + t); }

        // ---- 边界测试（自己补充）----
        try { if (!TestUtil.checkEq(true, s.isIsomorphic("a", "b"), "边界1：最短输入可映射")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        try { if (!TestUtil.checkEq(true, s.isIsomorphic("aaaaa", "bbbbb"), "边界2：同一字符始终映射到同一字符")) failures++; } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        try { if (!TestUtil.checkEq(false, s.isIsomorphic("ab", "cc"), "边界3：两个字符不能合并映射")) failures++; } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        try { if (!TestUtil.checkEq(true, s.isIsomorphic("ab", "12"), "边界4：不同字符可一一映射")) failures++; } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        try { if (!TestUtil.checkEq(true, s.isIsomorphic("a1!", "b2?"), "边界5：ASCII 非字母字符")) failures++; } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        String longS = "a".repeat(50_000);
        String longT = "b".repeat(50_000);
        try { if (!TestUtil.checkEq(true, s.isIsomorphic(longS, longT), "边界6：最大长度重复字符")) failures++; } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}
