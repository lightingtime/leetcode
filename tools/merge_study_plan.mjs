#!/usr/bin/env node
// Merge a public LeetCode study plan into .lc/order.json by problem ID.
// Defaults to preview; pass --apply to append only problems not already present.

import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const ROOT = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '..');
const ORDER_PATH = path.join(ROOT, '.lc', 'order.json');
const PLAN_SLUG = 'selected-coding-interview';
const APPLY = process.argv.includes('--apply');

const query = `query studyPlanV2Detail($planSlug: String!) {
  studyPlanV2Detail(planSlug: $planSlug) {
    name
    planSubGroups {
      name
      questions { questionFrontendId translatedTitle title titleSlug difficulty }
    }
  }
}`;

function categoryFor(group, title) {
  if (group === '链表') return '链表';
  if (group === '栈与队列') return '栈、队列与优先队列';
  if (group === '哈希表') return '哈希表';
  if (group === '双指针') return '双指针与滑动窗口';
  if (group === '模拟') {
    if (title.includes('栈序列')) return '栈、队列与优先队列';
    if (title.includes('字符串') || title.includes('字形') || title.includes('数字') || title.includes('整数')) return '字符串';
    return '数组与矩阵';
  }
  if (group === '查找') return '数组与矩阵';
  if (group === '搜索') {
    if (title.includes('课程表')) return '图与并查集';
    if (title.includes('二叉树') || title.includes('二叉搜索树')) return '树与二叉树';
    return '数组与矩阵';
  }
  if (group === '回溯') {
    if (title.includes('二叉树') || title.includes('路径总和')) return '树与二叉树';
    return '回溯';
  }
  if (group === '分治') {
    if (title.includes('二叉树')) return '树与二叉树';
    return '数学与位运算';
  }
  if (group === '动态规划') return '动态规划';
  if (group === '贪心') return title.includes('矩阵') ? '数组与矩阵' : '贪心与区间';
  if (group === '位运算' || group === '数学') return '数学与位运算';
  throw new Error(`没有为学习计划分组配置本地分类：${group}`);
}

async function main() {
  const response = await fetch('https://leetcode.cn/graphql/', {
    method: 'POST',
    headers: { 'content-type': 'application/json', referer: `https://leetcode.cn/studyplan/${PLAN_SLUG}/` },
    body: JSON.stringify({ query, variables: { planSlug: PLAN_SLUG } }),
  });
  if (!response.ok) throw new Error(`LeetCode 请求失败：HTTP ${response.status}`);
  const payload = await response.json();
  if (payload.errors?.length) throw new Error(payload.errors.map(e => e.message).join('; '));
  const plan = payload.data?.studyPlanV2Detail;
  if (!plan?.planSubGroups?.length) throw new Error('LeetCode 未返回学习计划题目');

  const order = JSON.parse(fs.readFileSync(ORDER_PATH, 'utf8'));
  const knownIds = new Set(order.map(q => String(q.id)));
  const seenPlanIds = new Set();
  const flattened = [];
  for (const group of plan.planSubGroups) {
    for (const q of group.questions || []) {
      const id = String(q.questionFrontendId);
      if (seenPlanIds.has(id)) continue;
      seenPlanIds.add(id);
      if (knownIds.has(id)) continue;
      const title = q.translatedTitle || q.title;
      flattened.push({
        id,
        title,
        slug: q.titleSlug,
        difficulty: String(q.difficulty).toUpperCase(),
        category: categoryFor(group.name, title),
        url: `https://leetcode.cn/problems/${q.titleSlug}/`,
      });
    }
  }

  const maxSeq = Math.max(0, ...order.map(q => Number(q.seq) || 0));
  const maxPhase = Math.max(0, ...order.map(q => Number(q.phase) || 0));
  const additions = flattened.map((q, index) => ({
    seq: maxSeq + index + 1,
    phase: maxPhase + 1,
    ...q,
  }));

  console.log(`计划：${plan.name}`);
  console.log(`来源题数：${seenPlanIds.size}｜本地已有：${seenPlanIds.size - flattened.length}｜待追加：${flattened.length}`);
  console.log(`本地题库：${order.length} 题｜追加后：${order.length + additions.length} 题`);
  for (const q of additions) console.log(`  seq=${q.seq} | ${q.id}. ${q.title} [${q.category}] ${q.difficulty}`);
  if (!APPLY) {
    console.log('预览模式：确认清单无误后加 --apply 写入 order.json。');
    return;
  }

  const merged = [...order, ...additions];
  fs.writeFileSync(ORDER_PATH, `${JSON.stringify(merged, null, 2)}\n`);
  console.log(`已写入 ${path.relative(ROOT, ORDER_PATH)}；保留原有顺序与记录，新增题目追加在末尾。`);
}

main().catch(error => {
  console.error(`✗ ${error.message}`);
  process.exitCode = 1;
});
