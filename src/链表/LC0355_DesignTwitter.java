// ============================================================
// LeetCode 355. 设计推特 (Design Twitter)
// 难度：Medium | 分类：链表
// 链接：https://leetcode.cn/problems/design-twitter/
// 刷题日期：2026-08-28
//
// 思路：每用户推文链表（头插保持最近在前）+ tweetMap 全局时间戳 + 归并取最近 10 条
// 复杂度：postTweet/follow/unfollow O(1)，getNewsFeed O(k log k)
// ============================================================

import java.util.*;

public class LC0355_DesignTwitter {

    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 Twitter 在提交时自动处理。
    // ==== 提交代码开始 ====
    static class Twitter {
        Map<Integer, List<Integer>> tweets;
        Map<Integer, Set<Integer>> followers;
        Map<Integer, Set<Integer>> followees;
        int uniId;
        Map<Integer, Integer> tweetMap;
        public Twitter() {
            tweets = new HashMap<>();
            followers = new HashMap<>();
            followees = new HashMap<>();
            uniId = 0;
            tweetMap = new HashMap<>();
        }
        public void postTweet(int userId, int tweetId) {
            uniId++;
            tweetMap.put(tweetId, uniId);
            tweets.computeIfAbsent(userId, x -> new LinkedList<>()).add(0, tweetId);
        }
        public List<Integer> getNewsFeed(int userId) {
            List<List<Integer>> allNews = new ArrayList<>();
            if (tweets.containsKey(userId)) {
                allNews.add(tweets.get(userId));
            }
            if (followers.containsKey(userId)) {
                for (Integer followeeId : followers.get(userId)) {
                    if (tweets.containsKey(followeeId)) {
                        allNews.add(tweets.get(followeeId));
                    }
                }
            }
            if (allNews.isEmpty()) {
                return new ArrayList<>();
            }
            List<Integer> sortNews = getSortNews(allNews, 0, allNews.size() - 1);
            return new ArrayList<>(sortNews.subList(0, Math.min(10, sortNews.size())));
        }

        private List<Integer> getSortNews(List<List<Integer>> allNews, int l, int r) {
            if (l >= r) {
                return allNews.get(l);
            }
            int mid = l + (r - l) / 2;
            List<Integer> first = getSortNews(allNews, l, mid);
            List<Integer> second = getSortNews(allNews, mid + 1, r);
            return mergeList(first, second);
        }

        private List<Integer> mergeList(List<Integer> first, List<Integer> second) {
            List<Integer> list = new ArrayList<>();
            int i = 0, j = 0;
            while (i < first.size() && j < second.size()) {
                if (list.size() == 10) {
                    break;
                }
                if (tweetMap.get(first.get(i)) > tweetMap.get(second.get(j))) {
                    list.add(first.get(i));
                    i++;
                } else {
                    list.add(second.get(j));
                    j++;
                }
            }
            while (i != first.size() && list.size() < 10) {
                list.add(first.get(i));
                i++;
            }
            while (j != second.size() && list.size() < 10) {
                list.add(second.get(j));
                j++;
            }
            return list;
        }

        public void follow(int followerId, int followeeId) {
            followers.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
            followees.computeIfAbsent(followeeId, k -> new HashSet<>()).add(followerId);
        }
        public void unfollow(int followerId, int followeeId) {
            if (followers.containsKey(followerId)) {
                followers.get(followerId).remove(followeeId);
            }
            if (followees.containsKey(followeeId)) {
                followees.get(followeeId).remove(followerId);
            }
        }
    }

    // ==== 提交代码结束 ====

    public static void main(String[] args) {
        int failures = 0;

        // ---- 测试1：题目示例流程 ----
        try {
            Twitter t = new Twitter();
            t.postTweet(1, 5);
            if (!TestUtil.checkEq(Arrays.asList(5), t.getNewsFeed(1), "示例-初始feed")) failures++;
            t.follow(1, 2);
            t.postTweet(2, 6);
            if (!TestUtil.checkEq(Arrays.asList(6, 5), t.getNewsFeed(1), "示例-关注后feed")) failures++;
            t.unfollow(1, 2);
            if (!TestUtil.checkEq(Arrays.asList(5), t.getNewsFeed(1), "示例-取关后feed")) failures++;
        } catch (Throwable ex) { failures++; System.out.println("示例 异常: " + ex); }

        // ---- 测试2：只返回最近 10 条，且按时间倒序 ----
        try {
            Twitter t = new Twitter();
            for (int i = 1; i <= 15; i++) t.postTweet(1, i);
            List<Integer> feed = t.getNewsFeed(1);
            List<Integer> expect = new ArrayList<>();
            for (int i = 15; i >= 6; i--) expect.add(i); // 15..6 共 10 条
            if (!TestUtil.checkEq(expect, feed, "最近10条截断+倒序")) failures++;
        } catch (Throwable ex) { failures++; System.out.println("最近10条 异常: " + ex); }

        // ---- 测试3：多关注人合并，整体按时间倒序 ----
        try {
            Twitter t = new Twitter();
            t.postTweet(2, 20);   // t=1
            t.postTweet(1, 10);   // t=2
            t.postTweet(3, 30);   // t=3
            t.follow(1, 2);
            t.follow(1, 3);
            List<Integer> expect = Arrays.asList(30, 10, 20);
            if (!TestUtil.checkEq(expect, t.getNewsFeed(1), "多关注人合并倒序")) failures++;
        } catch (Throwable ex) { failures++; System.out.println("多关注人 异常: " + ex); }

        // ---- 测试4：新用户空 feed ----
        try {
            Twitter t = new Twitter();
            if (!TestUtil.checkEq(Collections.emptyList(), t.getNewsFeed(7), "空feed")) failures++;
        } catch (Throwable ex) { failures++; System.out.println("空feed 异常: " + ex); }

        // ---- 测试5：排序按调用时间而非 tweetId 大小 ----
        try {
            Twitter t = new Twitter();
            t.postTweet(1, 100);  // 先发
            t.postTweet(1, 1);    // 后发
            List<Integer> expect = Arrays.asList(1, 100); // 后发的在前
            if (!TestUtil.checkEq(expect, t.getNewsFeed(1), "按调用时间排序")) failures++;
        } catch (Throwable ex) { failures++; System.out.println("调用时间排序 异常: " + ex); }

        // ---- 测试6：取消关注后不再出现该用户推文 ----
        try {
            Twitter t = new Twitter();
            t.follow(1, 2);
            t.postTweet(2, 99);
            t.postTweet(1, 1);
            t.unfollow(1, 2);
            if (!TestUtil.checkEq(Arrays.asList(1), t.getNewsFeed(1), "取关后排除")) failures++;
        } catch (Throwable ex) { failures++; System.out.println("取关 异常: " + ex); }

        if (failures > 0) {
            System.out.println("测试未全部通过，失败 " + failures + " 个");
            System.exit(1);
        }
        System.out.println("全部测试通过");
    }

}