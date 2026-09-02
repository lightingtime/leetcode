#!/usr/bin/env node
// LC 复习调度：抽题 / 记录 / 统计（第二遍 + 间隔复习）
// 用法:
//   node review.js init               从 progress.json 初始化/补齐 review_state.json（幂等）
//   node review.js next [--count N]   今日复习队列与配额
//   node review.js done --seq N --mastery strong|weak [--firstTry true] [--approach "..."] [--time O(n)] [--space O(n)] [--optimal true] [--memory <字节>] [--notes "..."] [--dry-run]
//   node review.js stats              二刷进度 / 掌握度分布 / 今日到期
// 数据模型（面向后续 app 消费）:
//   - 事件明细（每次复习）写入 .lc/problems/{id}_{slug}/analysis.json 的 reviews 数组（append-only）
//   - 调度/派生状态（到期日期、间隔、掌握度）写入 .lc/review_state.json，可由 reviews + 算法重建
//   - progress.json 只保留一刷索引，不写入复习数据
// 间隔模型：Ebbinghaus 遗忘曲线 + SM-2（EF=2.5）
//   较强：间隔 = 上次 × 2.5（首次 1 天起），上限 60 天；
//         第 2 次较强起（连续较强 ≥ STRONG_BOOST_MIN_CONSECUTIVE）每次再上浮 STRONG_BOOST_DAYS 天（尽早拉开，减少高频回访）
//   较弱：间隔重置 WEAK_INTERVAL_DAYS 天（2 天，避免次日高频回访），连续较强清零
//   连续 2 次较强且间隔达上限 → 标记已掌握，退出队列
const fs = require('fs');
const path = require('path');

const ROOT = path.resolve(__dirname, '..', '..', '..', '..');
const LC = path.join(ROOT, '.lc');
const ORDER = path.join(LC, 'order.json');
const PROGRESS = path.join(LC, 'progress.json');
const STATE = path.join(LC, 'review_state.json');

const REVIEW_DEADLINE = '2026-09-15';       // 硬性目标：第二遍（首轮复习）完成时间
const BASE_INTERVAL_DAYS = 1;               // 首次复习后的基础间隔
const EF = 2.5;                             // SM-2 初始易度因子
const MAX_INTERVAL_DAYS = 60;               // 间隔上限
const WEAK_INTERVAL_DAYS = 2;               // 较弱掌握重置间隔（放宽为 2 天，避免次日高频回访）
const MASTER_CONSECUTIVE_STRONG = 2;        // 连续较强达到上限即掌握
const STRONG_BOOST_MIN_CONSECUTIVE = 1;     // 连续较强达到该次数后即开始上浮（1 = 第 2 次较强起）
const STRONG_BOOST_DAYS = 2;                // 达到后每次再额外拉长天数

function todayStr() {
  const d = new Date();
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}
function addDays(dateStr, n) {
  const [y, m, d] = dateStr.split('-').map(Number);
  const dt = new Date(y, m - 1, d + n);
  return `${dt.getFullYear()}-${String(dt.getMonth() + 1).padStart(2, '0')}-${String(dt.getDate()).padStart(2, '0')}`;
}
function daysBetween(a, b) {
  const pa = a.split('-').map(Number), pb = b.split('-').map(Number);
  const da = new Date(pa[0], pa[1] - 1, pa[2]);
  const db = new Date(pb[0], pb[1] - 1, pb[2]);
  return Math.round((db - da) / 86400000);
}
function readJson(p) { return JSON.parse(fs.readFileSync(p, 'utf8')); }
function writeJson(p, obj) { fs.writeFileSync(p, JSON.stringify(obj, null, 2), 'utf8'); }
function arg(name, def) { const i = process.argv.indexOf(name); return i >= 0 && process.argv[i + 1] ? process.argv[i + 1] : def; }
function has(name) { return process.argv.indexOf(name) >= 0; }

const args = process.argv.slice(2);
const cmd = args[0];
const order = JSON.parse(fs.readFileSync(ORDER, 'utf8'));
const progress = JSON.parse(fs.readFileSync(PROGRESS, 'utf8'));
const doneMap = new Map((progress.done || []).map(d => [d.seq, d]));
// 题库中未完成一刷的题（可能随时新增）——有它们时复习模式不生效
const pendingFirstPass = order.filter(o => !doneMap.has(o.seq) && !(progress.skipped || []).includes(o.seq));
function warnPending() {
  if (pendingFirstPass.length) {
    console.log(`⚠ 题库有 ${pendingFirstPass.length} 道新题未完成一刷（如 ${pendingFirstPass[0].id}. ${pendingFirstPass[0].title} 等），建议先切回刷题模式：update_state.js mode practice`);
    console.log('');
  }
}

function seedEntry(q) {
  const d = doneMap.get(q.seq) || {};
  return {
    seq: q.seq,
    id: q.id,
    title: q.title,
    category: q.category,
    first_pass_date: d.date || null,   // 一刷完成日期
    last_review_date: null,
    next_review_date: null,
    interval_days: null,               // 当前间隔（天）
    review_count: 0,
    mastery: null,                     // strong | weak | null
    consecutive_strong: 0,
    mastered: false
  };
}

// 初始化 / 幂等补齐
function ensureState() {
  if (!fs.existsSync(STATE)) {
    const problems = {};
    for (const q of order) problems[q.slug] = seedEntry(q);
    const state = { version: 1, updated: todayStr(), deadline: REVIEW_DEADLINE, problems };
    writeJson(STATE, state);
    return state;
  }
  const state = readJson(STATE);
  let changed = false;
  for (const q of order) {
    if (!state.problems[q.slug]) { state.problems[q.slug] = seedEntry(q); changed = true; }
  }
  // 清理已不在题库中的旧条目（题库可能删除/替换题目）
  for (const slug of Object.keys(state.problems)) {
    if (!order.some(o => o.slug === slug)) { delete state.problems[slug]; changed = true; }
  }
  if (changed) { state.updated = todayStr(); writeJson(STATE, state); }
  return state;
}

// 薄弱判定（一刷记录）：非一次 AC / 非最优 → 薄弱
function isWeak(st) {
  const d = doneMap.get(st.seq) || {};
  return !d.firstPass || !d.optimal;
}
function fmt(st, d) {
  const weak = isWeak(st);
  return `seq=${st.seq} | ${st.id}. ${st.title} [${st.category}]${weak ? ' ⚠薄弱' : ''} | 一刷 ${st.first_pass_date || '—'}` +
    (st.review_count > 0 ? ` | 上次复习 ${st.last_review_date} | 间隔 ${st.interval_days} 天` : '');
}

if (cmd === 'init') {
  const state = ensureState();
  console.log(`review_state.json 就绪：${Object.keys(state.problems).length} 题（二刷 ${Object.values(state.problems).filter(p => p.review_count > 0).length} 题）`);
} else if (cmd === 'next') {
  warnPending();
  const state = ensureState();
  const today = todayStr();
  const daysLeft = Math.max(0, daysBetween(today, REVIEW_DEADLINE));
  const problems = state.problems;
  const pending = [], due = [], mastered = [];
  for (const q of order) {
    const st = problems[q.slug];
    if (!st) continue;
    if (st.mastered) { mastered.push(st); continue; }
    if (st.skipped) continue; // 用户跳过的题：不推荐，完成复习或 --undo 后恢复
    if (st.review_count === 0) pending.push(st);
    else if (st.next_review_date && st.next_review_date <= today) due.push(st);
  }
  // 第一轮待刷：薄弱优先 → 一刷日期从旧到新
  pending.sort((a, b) => (isWeak(b) - isWeak(a)) || String(a.first_pass_date || '').localeCompare(String(b.first_pass_date || '')));
  // 到期（间隔）：薄弱优先 → 到期日从早到晚
  due.sort((a, b) => (isWeak(b) - isWeak(a)) || String(a.next_review_date).localeCompare(String(b.next_review_date)));
  // 到期题优先：due 全部置前（薄弱优先 → 到期日早到晚），再按配额补第一轮待刷
  const queue = [...due, ...pending];
  const countArg = parseInt(arg('--count', '0'), 10) || 0;
  const quota = countArg > 0 ? countArg : (daysLeft > 0 ? Math.ceil(pending.length / daysLeft) : pending.length);
  const base = quota > 0 ? quota : Math.min(10, queue.length);
  const n = base + due.length; // 今日推荐 = 全部到期 + 配额补足
  const todayList = queue.slice(0, n);
  console.log(`二刷进度：${order.length - pending.length}/${order.length} 题 ｜ 已掌握 ${mastered.length} 题`);
  console.log(`第一轮待刷 ${pending.length} 题，距 ${REVIEW_DEADLINE} 还有 ${daysLeft} 天 → 建议今日 ${quota || '—'} 题` +
    (due.length ? `（到期 ${due.length} 题优先，与配额一起推荐）` : ''));
  if (!todayList.length) { console.log('今日队列为空：全部已完成或未到期，进入间隔复习等待。'); process.exit(0); }
  console.log('今日推荐（按优先级）：');
  todayList.forEach((st, i) => console.log(`  ${i + 1}. ${fmt(st)}`));
  if (queue.length > todayList.length) console.log(`  …… 队列中还有 ${queue.length - todayList.length} 题（今日做不完顺延）`);
} else if (cmd === 'skip') {
  const seq = parseInt(arg('--seq', ''), 10);
  const undo = arg('--undo', '') === 'true';
  if (!seq) { console.error('用法: review.js skip --seq N [--undo true]'); process.exit(1); }
  const state = ensureState();
  const q = order.find(o => o.seq === seq);
  if (!q) { console.error(`找不到 seq=${seq}`); process.exit(1); }
  const st = state.problems[q.slug];
  if (undo) { delete st.skipped; console.log(`已取消跳过：${q.id}. ${q.title}（重新进入复习队列）`); }
  else { st.skipped = true; console.log(`已跳过：${q.id}. ${q.title}（next 不再推荐；完成复习或 --undo true 可恢复）`); }
  state.updated = todayStr();
  writeJson(STATE, state);
} else if (cmd === 'done') {
  const seq = parseInt(arg('--seq', '0'), 10);
  const q = order.find(o => o.seq === seq);
  if (!q) { console.error(`找不到 seq=${seq}`); process.exit(1); }
  const mastery = arg('--mastery', '');
  if (mastery !== 'strong' && mastery !== 'weak') { console.error('--mastery 必填 strong|weak'); process.exit(1); }
  const state = ensureState();
  const st = state.problems[q.slug];
  const today = todayStr();
  const firstTry = arg('--firstTry', 'true') === 'true';
  const optimal = arg('--optimal', 'false') === 'true';
  const review = {
    pass: st.review_count + 1,
    date: today,
    mastery,
    firstTry,
    verdict: arg('--verdict', 'Accepted'),
    testcases: arg('--testcases', ''),
    approach: arg('--approach', ''),
    time_complexity: arg('--time', ''),
    space_complexity: arg('--space', ''),
    optimal,
    notes: arg('--notes', '')
  };
  // 间隔模型
  let interval;
  if (mastery === 'strong') {
    interval = st.review_count === 0 ? BASE_INTERVAL_DAYS : Math.min(MAX_INTERVAL_DAYS, Math.round((st.interval_days || BASE_INTERVAL_DAYS) * EF));
    // 第 2 次较强起（连续较强 ≥ 阈值）：在 ×2.5 基础上再上浮，尽早拉开间隔（减少高频回访）
    if ((st.consecutive_strong || 0) >= STRONG_BOOST_MIN_CONSECUTIVE) {
      interval = Math.min(MAX_INTERVAL_DAYS, interval + STRONG_BOOST_DAYS);
    }
    st.consecutive_strong = (st.consecutive_strong || 0) + 1;
  } else {
    interval = WEAK_INTERVAL_DAYS;
    st.consecutive_strong = 0;
  }
  st.interval_days = interval;
  st.last_review_date = today;
  st.next_review_date = addDays(today, interval);
  st.review_count += 1;
  st.mastery = mastery;
  delete st.skipped; // 完成复习即取消跳过标记
  if (mastery === 'strong' && interval >= MAX_INTERVAL_DAYS && st.consecutive_strong >= MASTER_CONSECUTIVE_STRONG) st.mastered = true;
  state.updated = today;

  // analysis.json：reviews 追加 + submissions 合并
  const dir = path.join(LC, 'problems', `${q.id}_${q.slug}`);
  fs.mkdirSync(dir, { recursive: true });
  const ap = path.join(dir, 'analysis.json');
  let a = {};
  if (fs.existsSync(ap)) { try { a = readJson(ap); } catch {} }
  a.reviews = Array.isArray(a.reviews) ? a.reviews : [];
  a.reviews.push(review);
  if (review.verdict === 'Accepted') {
    const sub = { date: today, verdict: review.verdict, testcases: review.testcases, approach: review.approach, time_complexity: review.time_complexity, space_complexity: review.space_complexity, optimal: review.optimal, notes: review.notes, mastery, pass: review.pass };
    const mem = arg('--memory', '');
    if (mem) sub.memory_bytes = parseInt(mem, 10);
    const codeNote = arg('--code', '');
    if (codeNote) sub.code = codeNote;
    a.submissions = Array.isArray(a.submissions) ? a.submissions : [];
    // 同写法合并判断：approach 忽略大小写/标点/空格后比较（措辞微调也能命中）；
    // 调用方（lc-submit）对同一写法必须复用原 approach 字符串，从根上避免同写法拆成两条。
    const normApproach = x => (x || '').toLowerCase().replace(/[^a-z0-9\u4e00-\u9fff]/g, '');
    const same = a.submissions.find(s => normApproach(s.approach) === normApproach(sub.approach));
    if (same) {
      const better = (sub.optimal && !same.optimal) ||
        (sub.optimal === same.optimal && sub.memory_bytes != null && (same.memory_bytes == null || sub.memory_bytes < same.memory_bytes));
      if (better || codeNote) {
        // 强制保留原写法（必做）：合并覆盖前把旧 code 存入 prev_code，绝不静默丢弃中间版本代码
        const oldCode = same.code;
        if (codeNote && oldCode && oldCode !== codeNote && !same.prev_code) same.prev_code = oldCode;
        Object.assign(same, sub);
      } else if (same.approach !== sub.approach) {
        same.approach = sub.approach; // 同写法措辞微调：只更新命名，不重复追加条目
      }
    } else {
      a.submissions.push(sub);
    }
  }
  // progress.done：一刷事实不动，仅最优性更新为当前最优
  const doneRec = doneMap.get(seq);
  if (doneRec && review.optimal && !doneRec.optimal) doneRec.optimal = true;

  if (has('--dry-run')) {
    console.log('[dry-run] 将写入：');
    console.log('  analysis.json reviews+1:', JSON.stringify(review, null, 2).split('\n').map(l => '  ' + l).join('\n'));
    console.log(`  review_state[${q.slug}]: next=${st.next_review_date} interval=${interval} mastery=${mastery} consecutive_strong=${st.consecutive_strong} mastered=${st.mastered}`);
    return;
  }
  writeJson(ap, a);
  writeJson(PROGRESS, progress);
  writeJson(STATE, state);
  console.log(`已记录二刷（第 ${review.pass} 次）：${q.id}. ${q.title} ｜ 掌握：${mastery === 'strong' ? '较强' : '较弱'} ｜ 下次复习 ${st.next_review_date}（间隔 ${interval} 天）`);
  if (st.mastered) console.log('该题连续较强且间隔达上限 → 已标记「掌握」，退出复习队列。');
} else if (cmd === 'stats') {
  warnPending();
  const state = ensureState();
  const problems = Object.values(state.problems);
  const today = todayStr();
  const reviewed = problems.filter(p => p.review_count > 0);
  const pending = problems.filter(p => p.review_count === 0);
  const mastered = problems.filter(p => p.mastered);
  const strong = reviewed.filter(p => p.mastery === 'strong');
  const weak = reviewed.filter(p => p.mastery === 'weak');
  const dueToday = reviewed.filter(p => p.next_review_date && p.next_review_date <= today && !p.mastered).length;
  console.log(`二刷进度：${reviewed.length}/${order.length} 题（已掌握 ${mastered.length} 题）`);
  console.log(`第一轮待刷：${pending.length} 题 ｜ 今日到期（间隔）：${dueToday} 题`);
  console.log(`掌握度分布：较强 ${strong.length} ｜ 较弱 ${weak.length} ｜ 未复习 ${pending.length}`);
  const weakCat = {};
  [...weak, ...pending].forEach(p => { weakCat[p.category] = (weakCat[p.category] || 0) + 1; });
  console.log('薄弱分类分布（较弱 + 待刷，需重点照顾）：');
  Object.entries(weakCat).sort((a, b) => b[1] - a[1]).forEach(([c, n]) => console.log(`  ${c}: ${n}`));
} else {
  console.log('用法: init | next [--count N] | done --seq N --mastery strong|weak [--firstTry ...] [--approach ...] [--time ...] [--space ...] [--optimal ...] [--memory <字节>] [--notes ...] [--dry-run] | stats');
}
