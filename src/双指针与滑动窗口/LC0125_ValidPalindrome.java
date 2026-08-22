// ============================================================
// LeetCode 125. 验证回文串 (Valid Palindrome)
// 难度：Easy | 分类：双指针与滑动窗口
// 链接：https://leetcode.cn/problems/valid-palindrome/
// 刷题日期：2026-08-22
//
// 思路：双指针从两端向中间 —— 只比较「字母或数字」字符，且忽略大小写；遇到非字母数字字符就移动对应指针，落点字符不同则非回文。
// 复杂度：时间 O(n)，空间 O(1)
// ============================================================


public class LC0125_ValidPalindrome {

    // ==== 提交代码开始 ====
    public boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (!Character.isLetterOrDigit(s.charAt(l))) {
                l++;
            } else if (!Character.isLetterOrDigit(s.charAt(r))) {
                r--;
            } else {
                if (Character.toLowerCase(s.charAt(l)) != Character.toLowerCase(s.charAt(r))) {
                    return false;
                } else {
                    l++;
                    r--;
                }
            }
        }
        return true;
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        LC0125_ValidPalindrome s = new LC0125_ValidPalindrome();
        int failures = 0;

        // ---- 示例测试（来自题目）----
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome("A man, a plan, a canal: Panama"), "示例1")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例1 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(false, s.isPalindrome("race a car"), "示例2")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例2 异常: " + t);
        }
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome(" "), "示例3")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("示例3 异常: " + t);
        }

        // ---- 边界测试（本题具体逻辑）----
        // 1) 单字符（字母）
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome("a"), "边界1-单字母")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界1-单字母 异常: " + t);
        }
        // 2) 单字符（数字）
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome("1"), "边界2-单数字")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界2-单数字 异常: " + t);
        }
        // 3) 只有符号/空格（过滤后为空串）
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome(".,!"), "边界3-仅符号")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界3-仅符号 异常: " + t);
        }
        // 4) 大小写不敏感
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome("Aba"), "边界4-大小写")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界4-大小写 异常: " + t);
        }
        // 5) 符号被跳过，仍构成回文
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome("ab@ba"), "边界5-符号跳过")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界5-符号跳过 异常: " + t);
        }
        // 6) 符号被跳过，但核心不是回文
        try {
            if (!TestUtil.checkEq(false, s.isPalindrome("a,b"), "边界6-符号致非回文")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界6-符号致非回文 异常: " + t);
        }
        // 7) 数字参与的字母数字回文
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome("1a2a1"), "边界7-数字回文")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界7-数字回文 异常: " + t);
        }
        // 8) 数字参与但非回文
        try {
            if (!TestUtil.checkEq(false, s.isPalindrome("1a2b1"), "边界8-数字非回文")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界8-数字非回文 异常: " + t);
        }
        // 9) 只含一个字符的非字母数字
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome("!"), "边界9-仅符号单字符")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界9-仅符号单字符 异常: " + t);
        }
        // 10) 上限长度 2*10^5：全相同字符（回文）
        try {
            if (!TestUtil.checkEq(true, s.isPalindrome("a".repeat(200000)), "边界10-上限回文")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界10-上限回文 异常: " + t);
        }
        // 12) 数字必须与字母一样参与比较（isAlphabetic 会漏掉数字导致误判）
        try {
            if (!TestUtil.checkEq(false, s.isPalindrome("0P"), "边界12-数字参与比较")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界12-数字参与比较 异常: " + t);
        }
        // 11) 上限长度 2*10^5：尾字符不同（非回文）
        try {
            if (!TestUtil.checkEq(false, s.isPalindrome("a".repeat(199999) + "b"), "边界11-上限非回文")) failures++;
        } catch (Throwable t) {
            failures++;
            System.out.println("边界11-上限非回文 异常: " + t);
        }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}