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

// 补充知识：扫描 reviews 下的通用文档（.html/.md，排除 index.html 与 LC*_Review.html 复盘页），按所在目录标注分类
function collectKnowledgeDocs(dir, base) {
  const out = [];
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    if (entry.name === 'index.html') continue;
    if (entry.isFile() && /^LC\d{4}_.*_Review\.html$/.test(entry.name)) continue;
    const rel = base ? base + '/' + entry.name : entry.name;
    if (entry.isDirectory()) out.push(...collectKnowledgeDocs(path.join(dir, entry.name), rel));
    else if (entry.isFile() && (entry.name.endsWith('.html') || entry.name.endsWith('.md'))) out.push(rel);
  }
  return out;
}
const knowledgeDocs = fs.existsSync(REVIEWS) ? collectKnowledgeDocs(REVIEWS, '') : [];
const knowledgeItems = knowledgeDocs.map(rel => {
  const dir = path.dirname(rel);
  const name = path.basename(rel, '.md');
  const cat = dir === '.' ? '通用' : dir;
  return `      <div class="card">
        <p><a href="${esc(rel)}">${esc(name)}</a> <span class="badge plain">分类：${esc(cat)}</span></p>
      </div>`;
}).join('\n');

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

// 二刷复习统计（数据源 .lc/review_state.json，由 lc-review 维护）
let reviewState = null;
const reviewStatePath = path.join(LC_DIR, 'review_state.json');
if (fs.existsSync(reviewStatePath)) {
  try { reviewState = JSON.parse(fs.readFileSync(reviewStatePath, 'utf8')); } catch {}
}
let curMode = 'practice';
const modePath = path.join(LC_DIR, 'mode.json');
if (fs.existsSync(modePath)) { try { curMode = (JSON.parse(fs.readFileSync(modePath, 'utf8')).mode || 'practice'); } catch {} }
const reviewProblems = reviewState ? Object.values(reviewState.problems || {}) : [];
const reviewedCnt = reviewProblems.filter(p => p.review_count > 0).length;
const masteredCnt = reviewProblems.filter(p => p.mastered).length;
const strongCnt = reviewProblems.filter(p => p.mastery === 'strong').length;
const weakCnt = reviewProblems.filter(p => p.mastery === 'weak').length;
const dueToday = reviewProblems.filter(p => p.review_count > 0 && p.next_review_date && p.next_review_date <= todayStr && !p.mastered).length;
const pendingCnt = Math.max(0, total - reviewedCnt);
const reviewPct = total ? Math.min(100, Math.round((reviewedCnt / total) * 100)) : 0;

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
  `    <details class="rv-group">
      <summary>${esc(g.category)} · ${g.cards.length} 题 <span class="muted">（点击展开 / 收起）</span></summary>
${g.cards.join('\n')}
    </details>`
).join('\n');

const html = `<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>力扣训练主页</title>
<link rel="stylesheet" href="review.css">
</head>
<body>

<div class="markdown-body">
  <div class="page-head">
    <div class="kicker">LEETCODE 训练主页</div>
    <h1>力扣刷题 · 打卡与复盘</h1>
    <div class="sub">第一遍 ${total} 题 ${FIRST_PASS_DEADLINE} 前刷完 · 第二遍 ${SECOND_PASS_DEADLINE} 前完成</div>
    <div class="badges">
      <span class="badge ok">已打卡 ${checkinDays} 天</span>
      <span class="badge">完成 ${done.length}/${total} 题</span>
      <span class="badge">距 ${FIRST_PASS_DEADLINE} 还有 ${daysLeft} 天</span>
      <span class="badge">开始：${checkinDates.length ? checkinDates[checkinDates.length - 1] : '—'}</span>
      <span class="badge ${curMode === 'review' ? 'ok' : 'plain'}">模式：${curMode === 'review' ? '复习' : '刷题'}</span>
    </div>
</div>

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
    <h2>二刷复习</h2>
    <div class="metrics">
      <div class="metric ok"><div class="v">${reviewedCnt}/${total}</div><div class="l">二刷进度（${SECOND_PASS_DEADLINE} 前）</div></div>
      <div class="metric"><div class="v">${pendingCnt}</div><div class="l">第一轮待刷</div></div>
      <div class="metric"><div class="v">${dueToday}</div><div class="l">今日到期</div></div>
      <div class="metric ok"><div class="v">${strongCnt}</div><div class="l">较强掌握</div></div>
      <div class="metric"><div class="v">${weakCnt}</div><div class="l">较弱掌握</div></div>
      <div class="metric"><div class="v">${masteredCnt}</div><div class="l">已掌握</div></div>
    </div>
    <div class="bar"><i style="width:${reviewPct}%"></i></div>
    <p class="muted">调度：Ebbinghaus + SM-2（较强 ×2.5 拉长、较弱重置 1 天，上限 60 天）｜ 数据源 .lc/review_state.json，由 lc-review 维护。</p>
  </section>

  <section>
    <h2>打卡表</h2>
    ${done.length === 0
      ? '<div class="card muted">还没有打卡记录，完成第一题后会显示在这里。</div>'
      : `<table>
      <tr><th>日期</th><th>题数</th><th>完成题目（点击看复盘）</th><th>一次 AC</th><th>最优</th><th>复习日期</th></tr>
${rows}
    </table>`}
    <p class="muted">「是否最优」由 lc-submit 在 Accepted 后分析得出；二刷间隔由 lc-review 按掌握度调度。</p>
  </section>

  <section>
    <h2>题目复盘</h2>
    ${done.length === 0
      ? '<div class="card muted">暂无复盘。</div>'
      : `<p class="muted">按分类分组，默认折叠；点击分类可展开 / 收起，或点右上「全部展开 / 收起」。</p>
      <div class="rv-toolbar"><button id="rvToggle" type="button" onclick="toggleReviewGroups()">全部展开</button></div>
${reviewSections}`}
  </section>

  <section>
    <h2>补充知识</h2>
    ${knowledgeItems.length ? knowledgeItems : '<div class="card muted">暂无补充知识文档。</div>'}
  </section>

  <section>
    <h2>相关文档</h2>
    <div class="card">
      <p><a href="../训练方案.md">训练方案.md</a> —— 四周主题计划、题单与训练原则。</p>
      <p><a href="../README.md">README.md</a> —— 项目说明。</p>
      <p class="muted">本页由 build_site.js 自动生成：数据来自 .lc/progress.json 与 .lc/problems/*/analysis.json，每题 Accepted 后自动刷新，无需手动维护。</p>
    </div>
  </section>

  <div class="foot">生成于 ${todayStr} ｜ 数据源：.lc/progress.json · .lc/review_state.json · .lc/problems/*/analysis.json</div>

<script>
function toggleReviewGroups() {
  const groups = document.querySelectorAll('details.rv-group');
  const openAll = Array.from(groups).some(d => !d.open);
  groups.forEach(d => { d.open = openAll; });
  document.getElementById('rvToggle').textContent = openAll ? '全部收起' : '全部展开';
}
</script>
</div>

</body>
</html>
`;

fs.mkdirSync(REVIEWS, { recursive: true });
fs.writeFileSync(path.join(REVIEWS, 'index.html'), html, 'utf8');
console.log(`已生成 reviews/index.html（打卡 ${checkinDays} 天，完成 ${done.length}/${total} 题）`);
