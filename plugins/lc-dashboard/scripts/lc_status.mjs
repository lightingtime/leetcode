#!/usr/bin/env node
// LC 刷题状态面板：模式 / 一刷·二刷进度 / 今日队列 / 掌握度分布
// 用法:
//   node lc_status.mjs                    一次性显示面板
//   node lc_status.mjs --watch            每 60s 自动刷新（实时 TUI）
//   node lc_status.mjs --watch 30         每 30s 刷新
//   node lc_status.mjs --count 5          队列只显示 5 条
//   node lc_status.mjs --dir <项目根>      指定项目根目录
import fs from 'fs';
import path from 'path';

const arg = (k, d) => { const i = process.argv.indexOf(k); return i >= 0 && process.argv[i + 1] ? process.argv[i + 1] : d; };
const has = k => process.argv.indexOf(k) >= 0;
const countArg = parseInt(arg('--count', '10'), 10) || 10;
const watch = has('--watch') ? parseInt(arg('--watch', '60'), 10) || 60 : 0;

function findRoot(start) {
  let d = path.resolve(start || process.cwd());
  for (;;) {
    if (fs.existsSync(path.join(d, '.lc', 'mode.json'))) return d;
    const p = path.dirname(d);
    if (p === d) return null;
    d = p;
  }
}
const root = findRoot(arg('--dir'));
if (!root) { console.error('未找到力扣项目（向上找不到 .lc/mode.json）。请在仓库根目录运行，或加 --dir <项目根>。'); process.exit(1); }
const LC = path.join(root, '.lc');
const read = p => JSON.parse(fs.readFileSync(p, 'utf8'));
let order, progress, mode, reviewState;
function loadState() {
  order = read(path.join(LC, 'order.json'));
  progress = read(path.join(LC, 'progress.json'));
  mode = 'practice';
  try { mode = (read(path.join(LC, 'mode.json')).mode || 'practice'); } catch {}
  reviewState = null;
  try { reviewState = read(path.join(LC, 'review_state.json')); } catch {}
}
loadState();

const today = (() => { const d = new Date(); return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`; })();
const REVIEW_DEADLINE = '2026-09-15';
const daysBetween = (a, b) => { const p = a.split('-').map(Number), q = b.split('-').map(Number); return Math.round((new Date(q[0], q[1] - 1, q[2]) - new Date(p[0], p[1] - 1, p[2])) / 86400000); };
const daysLeft = Math.max(0, daysBetween(today, REVIEW_DEADLINE));

let eligibleOrder, done, doneMap, skipped, checkinDays, pendingFirst;
let problems, reviewed, pendingReview, mastered, strong, weak, dueToday;
function refreshDerivedState() {
  eligibleOrder = order.filter(o => o.isPaidOnly === false);
  done = progress.done || [];
  doneMap = new Map(done.map(d => [d.seq, d]));
  skipped = progress.skipped || [];
  checkinDays = new Set(done.map(d => d.date)).size;
  pendingFirst = eligibleOrder.filter(o => !doneMap.has(o.seq) && !skipped.includes(o.seq));

  const eligibleSeqs = new Set(eligibleOrder.map(o => o.seq));
  problems = reviewState ? Object.values(reviewState.problems || {}).filter(p => eligibleSeqs.has(p.seq)) : [];
  reviewed = problems.filter(p => p.review_count > 0);
  pendingReview = problems.filter(p => p.review_count === 0);
  mastered = problems.filter(p => p.mastered);
  strong = reviewed.filter(p => p.mastery === 'strong');
  weak = reviewed.filter(p => p.mastery === 'weak');
  dueToday = reviewed.filter(p => p.next_review_date && p.next_review_date <= today && !p.mastered);
}
refreshDerivedState();

const isWeak = st => { const d = doneMap.get(st.seq) || {}; return !d.firstPass || !d.optimal; };
const queue = [
  ...pendingReview.sort((a, b) => (isWeak(b) - isWeak(a)) || String(a.first_pass_date || '').localeCompare(String(b.first_pass_date || ''))),
  ...dueToday.sort((a, b) => (isWeak(b) - isWeak(a)) || String(a.next_review_date).localeCompare(String(b.next_review_date)))
];
const quota = pendingReview.length && pendingFirst.length === 0 ? Math.ceil(pendingReview.length / daysLeft) : 0;

const tty = process.stdout.isTTY;
const C = { b: tty ? '\x1b[1m' : '', dim: tty ? '\x1b[2m' : '', grn: tty ? '\x1b[32m' : '', yel: tty ? '\x1b[33m' : '', red: tty ? '\x1b[31m' : '', cyn: tty ? '\x1b[36m' : '', rst: tty ? '\x1b[0m' : '' };
const W = 58;
const line = c => '│' + (c || '').padEnd(W) + '│';
const hline = '├' + '─'.repeat(W) + '┤';
const tline = '┌' + '─'.repeat(W) + '┐';
const bline = '└' + '─'.repeat(W) + '┘';
const badge = (label, ok) => (ok ? `${C.grn}${label}${C.rst}` : `${C.yel}${label}${C.rst}`);

function render() {
  const modeLabel = mode === 'review' ? `${C.cyn}复习（二刷）${C.rst}` : `${C.grn}刷题（一刷）${C.rst}`;
  const first = `${done.length}/${eligibleOrder.length}` + (pendingFirst.length === 0 ? ` ${C.grn}✓ 一刷完成${C.rst}` : ` ${C.yel}还有 ${pendingFirst.length} 道新题${C.rst}`);
  const rv = reviewState ? `${reviewed.length}/${eligibleOrder.length} ｜ 待刷 ${pendingReview.length} ｜ 今日到期 ${dueToday.length}` : `${C.dim}未开始二刷${C.rst}`;
  const master = reviewState ? `较强 ${strong.length} ｜ 较弱 ${weak.length} ｜ 已掌握 ${mastered.length}` : '—';
  const out = [];
  out.push(tline);
  out.push(line(`${C.b}LC 刷题状态${C.rst}  ${C.dim}${root}${C.rst}`.slice(0, W)));
  out.push(hline);
  out.push(line(`模式：${modeLabel}`));
  out.push(line(`一刷：${first} ｜ 打卡 ${checkinDays} 天`));
  out.push(line(`二刷：${rv}`));
  out.push(line(`掌握：${master}`));
  if (mode === 'review' && pendingFirst.length === 0) {
    out.push(line(`配额：今日建议 ${quota} 题（${REVIEW_DEADLINE} 前过完第一轮）`));
  }
  if (pendingFirst.length > 0) {
    out.push(line(`${C.red}⚠ 题库新增 ${pendingFirst.length} 题未刷，复习模式已失效${C.rst}`));
  }
  out.push(hline);
  const title = mode === 'review' ? '今日复习队列（薄弱优先）' : `下一题（刷题）${pendingFirst.length ? '：' + pendingFirst[0].id + '. ' + pendingFirst[0].title : '：一刷已完成'}`;
  out.push(line(`${C.b}${title}${C.rst}`.slice(0, W)));
  if (mode === 'review') {
    const items = queue.slice(0, countArg);
    if (!items.length) out.push(line(`${C.dim}今日队列为空，进入间隔等待${C.rst}`));
    items.forEach((st, i) => {
      const w = isWeak(st) ? ` ${C.yel}⚠薄弱${C.rst}` : '';
      const when = st.review_count > 0 ? `（${st.next_review_date} 到期）` : `（一刷 ${st.first_pass_date || '—'}）`;
      out.push(line(`${String(i + 1).padStart(2)}. ${st.id}. ${st.title} [${st.category}]${w}`.slice(0, W)));
      out.push(line(`${C.dim}   ${when}${C.rst}`.slice(0, W)));
    });
    if (queue.length > items.length) out.push(line(`${C.dim}  …… 队列还有 ${queue.length - items.length} 题${C.rst}`));
  } else if (pendingFirst.length) {
    out.push(line(`${pendingFirst[0].seq}. ${pendingFirst[0].id}. ${pendingFirst[0].title} [${pendingFirst[0].category}] ${pendingFirst[0].difficulty}`));
    pendingFirst.slice(1, 4).forEach((q, i) => out.push(line(`${C.dim}  ${q.id}. ${q.title}${C.rst}`.slice(0, W))));
    if (pendingFirst.length > 4) out.push(line(`${C.dim}  …… 还有 ${pendingFirst.length - 4} 题${C.rst}`));
  }
  out.push(bline);
  out.push(`${C.dim}提示：说「下一题」继续 ｜ 说「开始复习 / 开始刷题」切模式 ｜ Ctrl+C 退出${C.rst}`);
  return out.join('\n');
}

function run() {
  loadState();
  refreshDerivedState();
  const s = render();
  if (watch) process.stdout.write('\x1b[2J\x1b[H');
  console.log(s);
}
run();
if (watch) setInterval(run, watch * 1000);
