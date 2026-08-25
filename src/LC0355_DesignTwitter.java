// ============================================================
// LeetCode 355. 设计推特 (Design Twitter)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/design-twitter/
// 刷题日期：2026-08-25
//
// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）
// 复杂度：TODO 时间 O(?) 空间 O(?)
// ============================================================

import java.util.*;

public class LC0355_DesignTwitter {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 Twitter 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class Twitter {
        public Twitter() {
            // TODO: 补全方法体
            
        }
        public void postTweet(int userId, int tweetId) {
            // TODO: 补全方法体
            
        }
        public List<Integer> getNewsFeed(int userId) {
            // TODO: 补全方法体
            return null;
        }
        public void follow(int followerId, int followeeId) {
            // TODO: 补全方法体
            
        }
        public void unfollow(int followerId, int followeeId) {
            // TODO: 补全方法体
            
        }
    }
    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 示例测试（来自题目，命令序列见文件头）----
        try {
            Twitter s = new Twitter();
            s.postTweet(1, 5);
            if (!TestUtil.checkEq(Arrays.asList(5), s.getNewsFeed(1), "示例: 自己推文")) failures++;
            s.follow(1, 2);
            s.postTweet(2, 6);
            if (!TestUtil.checkEq(Arrays.asList(6, 5), s.getNewsFeed(1), "示例: 关注后合并")) failures++;
            s.unfollow(1, 2);
            if (!TestUtil.checkEq(Arrays.asList(5), s.getNewsFeed(1), "示例: 取关后")) failures++;
        } catch (Throwable t) { failures++; System.out.println("示例 异常: " + t); }

        // ---- 边界测试（针对本题：最近 10 条、按时间非 id、关注合并、空 feed、取关 no-op、交叉关注）----
        // 边界1: 同用户发 15 条，只保留最近 10 条且按发布时间倒序
        try {
            Twitter t = new Twitter();
            for (int id = 1; id <= 15; id++) t.postTweet(1, id);
            List<Integer> exp = new ArrayList<>();
            for (int id = 15; id >= 6; id--) exp.add(id);
            if (!TestUtil.checkEq(exp, t.getNewsFeed(1), "边界1: 最近10条倒序")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }
        // 边界2: 按发布时间排序而不是 tweetId 大小（先发 100 再发 1，最新在前应是 1）
        try {
            Twitter t = new Twitter();
            t.postTweet(1, 100);
            t.postTweet(1, 1);
            if (!TestUtil.checkEq(Arrays.asList(1, 100), t.getNewsFeed(1), "边界2: 按时间非id")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界2 异常: " + t); }
        // 边界3: 关注多个用户合并 feed，按时间倒序
        try {
            Twitter t = new Twitter();
            t.postTweet(1, 10);
            t.postTweet(2, 20);
            t.follow(1, 2);
            t.postTweet(2, 21);
            if (!TestUtil.checkEq(Arrays.asList(21, 20, 10), t.getNewsFeed(1), "边界3: 关注合并")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界3 异常: " + t); }
        // 边界4: 新用户无推文、无关注 -> 空列表
        try {
            Twitter t = new Twitter();
            if (!TestUtil.checkEq(Collections.emptyList(), t.getNewsFeed(99), "边界4: 空feed")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界4 异常: " + t); }
        // 边界5: 取消关注一个未关注的人 -> no-op 不抛异常
        try {
            Twitter t = new Twitter();
            t.postTweet(1, 7);
            t.unfollow(1, 999);
            if (!TestUtil.checkEq(Arrays.asList(7), t.getNewsFeed(1), "边界5: 取关未关注no-op")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界5 异常: " + t); }
        // 边界6: 交叉关注，各自 feed 按全局时间倒序
        try {
            Twitter t = new Twitter();
            t.postTweet(1, 10);  // t1
            t.postTweet(2, 20);  // t2
            t.postTweet(2, 21);  // t3
            t.postTweet(1, 11);  // t4
            t.follow(1, 2);
            t.follow(2, 1);
            if (!TestUtil.checkEq(Arrays.asList(11, 21, 20, 10), t.getNewsFeed(1), "边界6a: 1的feed")) failures++;
            if (!TestUtil.checkEq(Arrays.asList(11, 21, 20, 10), t.getNewsFeed(2), "边界6b: 2的feed")) failures++;
        } catch (Throwable t) { failures++; System.out.println("边界6 异常: " + t); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}