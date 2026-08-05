// 生成训练主页 reviews/index.html（进度 + 打卡表 + 题目复盘列表，复盘按分类分组）
// 复盘报告按分类存放：reviews/{分类}/LC{题号}_{题名}_Review.html（分类名中的 / 等非法字符替换为 -）
// 用法: node build_site.js
// 数据源: .lc/progress.json（done 列表）、.lc/problems/*/analysis.json（每题判题明细）
// 约定: 主页由脚本自动生成，不要手改；每题 Accepted 后由 update_state.js checkin 触发重新生成。
const fs = require('fs');
const path = require('path');

const ROOT = path.resolve(__dirname, '..', '..', '..', '..');
const LC_DIR = path.join(ROOT, '.lc');
const REVIEWS = path.join(ROOT, 'reviews');
const FIRST_PASS_DEADLINE = '2026-08-30';  // 硬性目标：第一遍刷完所有题
const SECOND_PASS_DEADLINE = '2026-09-15'; // 硬性目标：第二遍完成时间
const todayStr = (() => { const d = new Date(); return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`; })();

const progress = JSON.parse(fs.readFileSync(path.join(LC_DIR, 'progress.json'), 'utf8'));
const order = JSON.parse(fs.readFileSync(path.join(LC_DIR, 'order.json'), 'utf8'));
const done = (progress.done || []).slice().sort((a, b) => a.seq - b.seq);
const checkinDates = [...new Set(done.map(d => d.date))].sort((a, b) => b.localeCompare(a)); // 打卡按天去重，最新在前
const checkinDays = checkinDates.length;
const total = order.length;
function daysBetween(a, b) {
  const pa = a.split('-').map(Number), pb = b.split('-').map(Number);
  const da = new Date(pa[0], pa[1] - 1, pa[2]);
  const db = new Date(pb[0], pb[1] - 1, pb[2]);
  return Math.round((db - da) / 86400000);
}
const daysLeft = Math.max(0, daysBetween(todayStr, FIRST_PASS_DEADLINE));
const needDaily = daysLeft > 0 ? (total - done.length) / daysLeft : (total - done.length);

// 加载每题分析明细
const analyses = {};
const problemsDir = path.join(LC_DIR, 'problems');
if (fs.existsSync(problemsDir)) {
  for (const dir of fs.readdirSync(problemsDir)) {
    const ap = path.join(problemsDir, dir, 'analysis.json');
    if (fs.existsSync(ap)) {
      try {
        const a = JSON.parse(fs.readFileSync(ap, 'utf8'));
        analyses[a.slug || dir] = a;
      } catch {}
    }
  }
}

function collectReviewFiles(dir, base) {
  const out = [];
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    if (entry.name === 'index.html') continue;
    const rel = base ? base + '/' + entry.name : entry.name;
    if (entry.isDirectory()) out.push(...collectReviewFiles(path.join(dir, entry.name), rel));
    else if (entry.isFile() && entry.name.endsWith('.html')) out.push(rel);
  }
  return out;
}
const reviewFiles = fs.existsSync(REVIEWS) ? collectReviewFiles(REVIEWS, '') : [];

function padId(id) { return 'LC' + String(id).padStart(4, '0') + '_'; }
function reviewLink(id) {
  const hit = reviewFiles.find(f => path.basename(f).startsWith(padId(id)));
  return hit || '';
}
function reviewDate(d) {
  const r = new Date(d + 'T00:00:00');
  r.setDate(r.getDate() + 1);
  return r.toISOString().slice(0, 10);
}
function esc(s) {
  return String(s == null ? '' : s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

const firstPassCnt = done.filter(d => d.firstPass).length;
const optimalCnt = done.filter(d => d.optimal).length;
const pct = Math.min(100, Math.round((done.length / total) * 100));

const rows = checkinDates.map(date => {
  const items = done.filter(d => d.date === date).sort((a, b) => a.seq - b.seq);
  const idLinks = items.map(d => {
    const link = reviewLink(d.id);
    const label = `LC${String(d.id).padStart(4, '0')} · ${d.title}`;
    return link ? `<a href="${link}">${esc(label)}</a>` : esc(label);
  }).join('<br>');
  const fp = items.filter(d => d.firstPass).length;
  const op = items.filter(d => d.optimal).length;
  return `      <tr>
        <td>${esc(date)}</td>
        <td>${items.length}</td>
        <td>${idLinks}</td>
        <td>${fp}/${items.length}</td>
        <td>${op}/${items.length}</td>
        <td>${esc(reviewDate(date))}</td>
      </tr>`;
}).join('\n');

// 题目复盘卡片按分类分组（保持题目完成顺序）
const reviewGroups = [];
const groupIndexOf = new Map();
done.forEach(d => {
  const a = analyses[d.slug] || {};
  const verdict = a.verdict || '';
  const approach = a.approach || '';
  const link = reviewLink(d.id);
  const card = `    <div class="card">
      <div class="rv-head">
        <b>${esc(padId(d.id).replace(/_$/, ''))} · ${esc(d.title)}</b>
        <span class="badge ${verdict === 'Accepted' ? 'ok' : 'plain'}">${verdict || '已记录'}</span>
      </div>
      <p class="muted">分类：${esc(d.category)} ｜ 日期：${esc(d.date)} ｜ 一次 AC：${d.firstPass ? '是' : '否'} ｜ 最优：${d.optimal ? '是' : '否'}</p>
      ${approach ? `<p>解法：${esc(approach)}</p>` : ''}
      ${link ? `<p><a href="${link}">打开完整复盘页 →</a></p>` : ''}
    </div>`;
  if (!groupIndexOf.has(d.category)) {
    groupIndexOf.set(d.category, reviewGroups.length);
    reviewGroups.push({ category: d.category, cards: [] });
  }
  reviewGroups[groupIndexOf.get(d.category)].cards.push(card);
});
const reviewSections = reviewGroups.map(g =>
  `    <div class="cat-head">${esc(g.category)}</div>\n${g.cards.join('\n')}`
).join('\n');

const html = `<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>力扣训练主页</title>
<style>
  :root { --bg:#f6f7f9; --card:#fff; --ink:#1f2329; --muted:#6b7280; --line:#e5e7eb; --accent:#2563eb; --good:#16a34a; }
  * { box-sizing:border-box; }
  body { margin:0; background:var(--bg); color:var(--ink); font-family:"Segoe UI","Microsoft YaHei","PingFang SC",system-ui,sans-serif; line-height:1.7; }
  .wrap { max-width:920px; margin:0 auto; padding:0 22px 60px; }
  .hero { background:linear-gradient(135deg,#0f172a 0%,#1e3a8a 100%); color:#fff; padding:48px 22px 42px; margin-bottom:30px; }
  .hero .wrap { padding:0; }
  .hero .kicker { font-size:13px; letter-spacing:2px; color:#93c5fd; font-weight:600; }
  .hero h1 { margin:8px 0 6px; font-size:32px; }
  .hero .sub { color:#cbd5e1; font-size:15px; }
  .hero .badges { margin-top:16px; display:flex; flex-wrap:wrap; gap:8px; }
  .badge { display:inline-flex; align-items:center; gap:6px; padding:4px 12px; border-radius:999px; font-size:12.5px; font-weight:600; background:rgba(255,255,255,.14); color:#e2e8f0; }
  .badge.ok { background:#dcfce7; color:#166534; }
  .badge.plain { background:#e5e7eb; color:#374151; }
  section { margin-top:40px; }
  h2 { font-size:21px; margin:0 0 14px; padding-left:12px; border-left:4px solid var(--accent); }
  .metrics { display:grid; grid-template-columns:repeat(auto-fit,minmax(150px,1fr)); gap:14px; }
  .metric { background:var(--card); border:1px solid var(--line); border-radius:12px; padding:16px 12px; text-align:center; }
  .metric .v { font-size:25px; font-weight:800; }
  .metric.ok .v { color:var(--good); }
  .metric .l { font-size:12.5px; color:var(--muted); margin-top:4px; }
  .bar { height:10px; background:#e5e7eb; border-radius:999px; overflow:hidden; margin-top:10px; }
  .bar i { display:block; height:100%; background:linear-gradient(90deg,#2563eb,#22c55e); width:${pct}%; }
  .card { background:var(--card); border:1px solid var(--line); border-radius:14px; padding:18px 20px; margin-top:14px; box-shadow:0 1px 3px rgba(15,23,42,.05); }
  .rv-head { display:flex; align-items:center; justify-content:space-between; gap:10px; flex-wrap:wrap; }
  .cat-head { font-size:17px; font-weight:700; margin:28px 0 2px; padding-left:10px; border-left:3px solid var(--accent); }
  .muted { color:var(--muted); font-size:13.5px; }
  a { color:var(--accent); text-decoration:none; }
  a:hover { text-decoration:underline; }
  table { width:100%; border-collapse:collapse; background:var(--card); border:1px solid var(--line); border-radius:12px; overflow:hidden; margin-top:14px; }
  th,td { padding:10px 12px; text-align:left; font-size:14px; border-bottom:1px solid var(--line); }
  th { background:#f1f5f9; font-weight:600; }
  tr:last-child td { border-bottom:none; }
  .foot { margin-top:56px; padding-top:18px; border-top:1px solid var(--line); color:var(--muted); font-size:12.5px; text-align:center; }
  @media (max-width:600px) { .metrics { grid-template-columns:repeat(2,1fr); } }
</style>
</head>
<body>

<div class="hero">
  <div class="wrap">
    <div class="kicker">LEETCODE 训练主页</div>
    <h1>力扣刷题 · 打卡与复盘</h1>
    <div class="sub">第一遍 ${total} 题 ${FIRST_PASS_DEADLINE} 前刷完 · 第二遍 ${SECOND_PASS_DEADLINE} 前完成</div>
    <div class="badges">
      <span class="badge ok">已打卡 ${checkinDays} 天</span>
      <span class="badge">完成 ${done.length}/${total} 题</span>
      <span class="badge">距 ${FIRST_PASS_DEADLINE} 还有 ${daysLeft} 天</span>
      <span class="badge">开始：${checkinDates.length ? checkinDates[checkinDates.length - 1] : '—'}</span>
    </div>
  </div>
</div>

<div class="wrap">

  <section>
    <h2>训练进度</h2>
    <div class="metrics">
      <div class="metric ok"><div class="v">${done.length}/${total}</div><div class="l">完成题数（第一遍 ${FIRST_PASS_DEADLINE} 前）</div></div>
      <div class="metric"><div class="v">${checkinDays}</div><div class="l">打卡天数</div></div>
      <div class="metric ok"><div class="v">${firstPassCnt}</div><div class="l">一次 AC</div></div>
      <div class="metric"><div class="v">${optimalCnt}</div><div class="l">最优解</div></div>
      <div class="metric"><div class="v">${needDaily.toFixed(1)}</div><div class="l">需日均（剩余 ${daysLeft} 天）</div></div>
    </div>
    <div class="bar"><i></i></div>
  </section>

  <section>
    <h2>打卡表</h2>
    ${done.length === 0
      ? '<div class="card muted">还没有打卡记录，完成第一题后会显示在这里。</div>'
      : `<table>
      <tr><th>日期</th><th>题数</th><th>完成题目（点击看复盘）</th><th>一次 AC</th><th>最优</th><th>复习日期</th></tr>
${rows}
    </table>`}
    <p class="muted">复习节奏：1 / 3 / 7 / 14 天；「是否最优」由 lc-submit 在 Accepted 后分析得出。</p>
  </section>

  <section>
    <h2>题目复盘</h2>
    ${done.length === 0
      ? '<div class="card muted">暂无复盘。</div>'
      : `<p class="muted">按分类分组展示，点击「打开完整复盘页」查看每题报告。</p>
${reviewSections}`}
  </section>

  <section>
    <h2>相关文档</h2>
    <div class="card">
      <p><a href="../训练方案.md">训练方案.md</a> —— 四周主题计划、题单与训练原则。</p>
      <p><a href="../README.md">README.md</a> —— 项目说明。</p>
      <p class="muted">本页由 build_site.js 自动生成：数据来自 .lc/progress.json 与 .lc/problems/*/analysis.json，每题 Accepted 后自动刷新，无需手动维护。</p>
    </div>
  </section>

  <div class="foot">生成于 ${todayStr} ｜ 数据源：.lc/progress.json · .lc/problems/*/analysis.json</div>
</div>

</body>
</html>
`;

fs.mkdirSync(REVIEWS, { recursive: true });
fs.writeFileSync(path.join(REVIEWS, 'index.html'), html, 'utf8');
console.log(`已生成 reviews/index.html（打卡 ${checkinDays} 天，完成 ${done.length}/${total} 题）`);
