#!/usr/bin/env node
// LC 状态栏一行输出：供 Codex 命令式状态栏 / tmux 状态栏 / 终端标题使用。
// 用法:
//   node lc_statusline.mjs                    从当前目录向上找项目
//   echo '<json>' | node lc_statusline.mjs    stdin 传会话 JSON（含 cwd）时优先用 cwd
// 非 LC 项目输出为空（调用方应回退到默认显示）；输出为单行，TTY 时带 ANSI 颜色。
import fs from 'fs';

let cwd = process.argv[2] || process.cwd();
if (!process.stdin.isTTY) {
  try {
    const buf = fs.readFileSync(0, 'utf8').trim();
    if (buf) { const j = JSON.parse(buf); if (j && j.cwd) cwd = j.cwd; }
  } catch {}
}
function findRoot(start) {
  let d = start;
  for (;;) {
    if (fs.existsSync(d + '/.lc/mode.json')) return d;
    const p = d.slice(0, d.lastIndexOf('/'));
    if (p === d || p === '') return null;
    d = p;
  }
}
const root = findRoot(cwd);
if (!root) { process.stdout.write(''); process.exit(0); }
const read = p => JSON.parse(fs.readFileSync(p, 'utf8'));
const LC = root + '/.lc';
const order = read(LC + '/order.json');
const progress = read(LC + '/progress.json');
let mode = 'practice';
try { mode = (read(LC + '/mode.json').mode || 'practice'); } catch {}
let rs = null;
try { rs = read(LC + '/review_state.json'); } catch {}
const done = progress.done || [];
const doneSeqs = new Set(done.map(d => d.seq));
const skipped = progress.skipped || [];
const pendingNew = order.filter(o => !doneSeqs.has(o.seq) && !skipped.includes(o.seq)).length;
const probs = rs ? Object.values(rs.problems) : [];
const reviewed = probs.filter(p => p.review_count > 0).length;
const strong = probs.filter(p => p.mastery === 'strong').length;
const weak = probs.filter(p => p.mastery === 'weak').length;
const today = (() => { const d = new Date(); return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`; })();
const due = probs.filter(p => p.review_count > 0 && p.next_review_date && p.next_review_date <= today && !p.mastered).length;
const tty = process.stdout.isTTY;
const C = { grn: tty ? '\x1b[32m' : '', yel: tty ? '\x1b[33m' : '', cyn: tty ? '\x1b[36m' : '', red: tty ? '\x1b[31m' : '', rst: tty ? '\x1b[0m' : '' };
const parts = [];
if (pendingNew > 0) {
  parts.push(`${C.yel}LC 刷题${C.rst} 一刷 ${done.length}/${order.length}${C.red} +新${pendingNew}${C.rst}`);
} else if (mode === 'review') {
  parts.push(`${C.cyn}LC 复习${C.rst} 一刷 ${done.length}/${order.length}${C.grn}✓${C.rst} · 二刷 ${reviewed}/${order.length} · 今日 ${due} · 强${strong} 弱${weak}`);
} else {
  parts.push(`${C.grn}LC 刷题${C.rst} ${done.length}/${order.length} · 二刷 ${reviewed}/${order.length}`);
}
process.stdout.write(parts.join(' | '));
