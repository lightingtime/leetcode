#!/usr/bin/env node
// 生成单题复盘报告：node generate_review.js --slug <slug>
// 输出：reviews/{分类}/LC{题号}_{题名}_Review.html（数据源 analysis.json / progress.json）
const fs = require('fs');
const path = require('path');

const ROOT = path.resolve(__dirname, '..', '..', '..', '..');
const LC = path.join(ROOT, '.lc');
const arg = (k, d = '') => {
  const i = process.argv.indexOf(k);
  return i >= 0 && process.argv[i + 1] ? process.argv[i + 1] : d;
};
const slug = arg('--slug');
if (!slug) { console.error('用法: node generate_review.js --slug <slug>'); process.exit(1); }

const read = p => JSON.parse(fs.readFileSync(p, 'utf8'));
const progress = read(path.join(LC, 'progress.json'));
const order = read(path.join(LC, 'order.json'));
const q = order.find(o => o.slug === slug);
if (!q) { console.error('找不到 slug'); process.exit(1); }
const a = read(path.join(LC, 'problems', `${q.id}_${slug}`, 'analysis.json'));
const done = (progress.done || []).find(d => d.slug === slug) || {};
const hint = (progress.category_hints || {})[q.category] || '';
const orderAll = read(path.join(LC, 'order.json'));

// 收集所有已生成复盘页：题号(4位) -> reviews 下的相对路径
const reviewMap = {};
(function collect(dir, base) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    if (entry.name === 'index.html') continue;
    const rel = base ? base + '/' + entry.name : entry.name;
    if (entry.isDirectory()) collect(path.join(dir, entry.name), rel);
    else if (entry.isFile() && /_Review\.html$/.test(entry.name)) {
      const m = entry.name.match(/^LC(\d{4})_/);
      if (m) reviewMap[m[1]] = rel;
    }
  }
})(path.join(ROOT, 'reviews'), '');

// 题目原文（含描述 / 示例 / 提示），渲染进「题目回顾」区块；剔除流程性「约定」段
const probMdPath = path.join(LC, 'problems', `${q.id}_${slug}`, 'problem.md');
const probMd = (fs.existsSync(probMdPath) ? fs.readFileSync(probMdPath, 'utf8') : '')
  .replace(/\n## 示例\n+## 约定/, '\n## 约定') // 拉题未返回示例时去掉空的示例段
  .replace(/\n## 约定[\s\S]*$/, '');            // 去掉与题目无关的流程提示

// 定位源码文件并提取提交区
const walk = dir => fs.existsSync(dir)
  ? fs.readdirSync(dir, { withFileTypes: true }).flatMap(e =>
      e.isDirectory() ? walk(path.join(dir, e.name)) : [path.join(dir, e.name)])
  : [];
const srcFile = walk(path.join(ROOT, 'src'))
  .find(f => path.basename(f).startsWith(`LC${String(q.id).padStart(4, '0')}_`));
let code = '';
if (srcFile) {
  const t = fs.readFileSync(srcFile, 'utf8');
  const m = t.match(/\/\/ ==== 提交代码开始 ====\n([\s\S]*?)\n    \/\/ ==== 提交代码结束 ====/);
  if (m) code = m[1].trim();
}
const fileBase = srcFile ? path.basename(srcFile, '.java') : `LC${String(q.id).padStart(4, '0')}`;
const safeCat = s => String(s).replace(/[\/\\]+/g, '-');
const esc = s => String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
// 轻量 Markdown 渲染（逐行状态机）：列表、简单表格、段落；支持 `行内代码`、**加粗**
const md = raw => {
  const lines = esc(String(raw)).replace(/\\n/g, '\n').split('\n').map(l => l.replace(/\s+$/, ''));
  // 先保护行内代码，再处理加粗与链接（[text](url) 与裸 URL 均可点击），最后还原代码
  const fmt = s => {
    const codes = [];
    let t = s.replace(/`([^`]+)`/g, (m, c) => { codes.push(c); return '\u0000' + (codes.length - 1) + '\u0000'; });
    t = t.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
      .replace(/\[([^\]]+)\]\((https?:\/\/[^)\s]+)\)|(https?:\/\/[^\s<]+)/g,
        (m, tx, u, bare) => { const url = u || bare; const text = tx || url; return `<a href="${url}" rel="noopener">${text}</a>`; });
    return t.replace(/\u0000(\d+)\u0000/g, (m, i) => `<code>${codes[+i]}</code>`);
  };
  const splitCells = line => line.replace(/^\||\|$/g, '').split('|').map(cell => fmt(cell.trim()));
  const isTableSeparator = line => /^\|?\s*:?-+:?\s*(\|\s*:?-+:?\s*)+\|?$/.test(line);
  const out = [];
  let listTag = null;
  const close = () => { if (listTag) { out.push('</' + listTag + '>'); listTag = null; } };
  for (let i = 0; i < lines.length; i++) {
    const rawLine = lines[i];
    const line = rawLine.trim();
    if (!line) { close(); continue; }
    if (line.startsWith('```')) {
      close();
      const buf = [];
      i++;
      while (i < lines.length && !lines[i].trim().startsWith('```')) { buf.push(lines[i]); i++; }
      out.push(`<div class="md-code"><pre>${buf.join('\n')}</pre></div>`);
      continue;
    }
    const hm = line.match(/^(#{1,3})\s+(.*)$/);
    if (hm) { close(); out.push(`<h${hm[1].length}>${fmt(hm[2])}</h${hm[1].length}>`); continue; }
    if (i + 1 < lines.length && line.includes('|') && isTableSeparator(lines[i + 1].trim())) {
      close();
      const header = splitCells(line);
      const tableRows = [];
      i += 2;
      while (i < lines.length && lines[i].trim() && lines[i].includes('|')) {
        tableRows.push(splitCells(lines[i].trim()));
        i++;
      }
      i--;
      out.push('<div class="table-wrap"><table><thead><tr>' +
        header.map(cell => '<th>' + cell + '</th>').join('') +
        '</tr></thead>' +
        (tableRows.length ? '<tbody>' + tableRows.map(row => '<tr>' + row.map(cell => '<td>' + cell + '</td>').join('') + '</tr>').join('') + '</tbody>' : '') +
        '</table></div>');
      continue;
    }
    const bm = line.match(/^[-*]\s+(.*)$/);
    const nm = line.match(/^\d+[.、]\s+(.*)$/);
    if (bm) {
      if (listTag !== 'ul') { close(); out.push('<ul>'); listTag = 'ul'; }
      out.push('<li>' + fmt(bm[1]) + '</li>');
    } else if (nm) {
      if (listTag !== 'ol') { close(); out.push('<ol>'); listTag = 'ol'; }
      out.push('<li>' + fmt(nm[1]) + '</li>');
    } else {
      close();
      out.push('<p>' + fmt(line) + '</p>');
    }
  }
  close();
  return out.join('');
};
const diff = String(a.difficulty || q.difficulty || 'MEDIUM').toUpperCase();
const diffCls = diff === 'EASY' ? 'easy' : diff === 'HARD' ? 'hard' : 'medium';
const date = a.date || done.date || '';
const testcases = a.testcases || '';
const memory = a.memory_bytes != null ? `≈ ${(a.memory_bytes / 1048576).toFixed(1)} MB` : '—';
const timeSpace = [a.time_complexity, a.space_complexity].filter(Boolean).join(' · ');
const mistakes = Array.isArray(a.mistakes) ? a.mistakes : [];
const codeNotes = Array.isArray(a.code_notes) ? a.code_notes : [];
const patterns = Array.isArray(a.patterns) ? a.patterns : [];
const approachDetail = a.approach_detail || '';
const reviews = Array.isArray(a.reviews) ? a.reviews : [];

const tlItems = [];
mistakes.forEach((m, i) => tlItems.push(
  `<div class="tl-item"><div class="tl-marker"></div><div class="tl-body"><span class="tl-title">0${i + 1} · 踩坑<span class="tl-badge bug">已记录</span></span><p>${esc(m.habit)}</p></div></div>`));
tlItems.push(
  `<div class="tl-item"><div class="tl-marker"></div><div class="tl-body"><span class="tl-title">0${mistakes.length + 1} · 本地测试<span class="tl-badge ok">通过</span></span><p>示例 + 边界用例本地全部通过后提交。</p></div></div>`,
  `<div class="tl-item"><div class="tl-marker"></div><div class="tl-body"><span class="tl-title">0${mistakes.length + 2} · 提交<span class="tl-badge ok">${a.verdict || 'Accepted'}</span></span><p>${esc(testcases)} 用例${a.firstPass ? '一次通过' : ''}。</p></div></div>`);

const fmtMem = b => b != null ? `≈ ${(b / 1048576).toFixed(1)} MB` : '—';
const codeBlock = (title, c) => c ? `<div class="code-wrap"><div class="code-head"><span>${esc(title)}</span><span class="code-actions"><span class="code-tag">提交区</span><button type="button" class="copy-btn" onclick="copyCode(this)">复制代码</button></span></div><pre>${esc(c)}</pre></div>` : '';
function renderReviews() {
  if (!reviews.length) return '';
  const items = reviews.slice().reverse().map(r => {
    const m = r.mastery === 'strong' ? '<span class="tl-badge ok">较强</span>' : '<span class="tl-badge bug">较弱</span>';
    const fp = r.firstTry ? '一次 AC' : '多次提交';
    const ts = [r.time_complexity, r.space_complexity].filter(Boolean).join(' · ');
    const verdict = r.verdict || 'Accepted';
    return `<div class="tl-item"><div class="tl-marker"></div><div class="tl-body">
      <span class="tl-title">第 ${esc(r.pass)} 次复习 · ${esc(r.date)} ${m}</span>
      <p class="muted">${verdict}（${esc(r.testcases || '—')}）｜ ${fp} ｜ ${esc(r.approach || '—')}${ts ? ' ｜ ' + esc(ts) : ''}</p>
      ${r.notes ? `<div class="review-note md">${md(r.notes)}</div>` : ''}
    </div></div>`;
  }).join('');
  return `<section><h2>复习记录</h2><div class="tl">${items}</div></section>`;
}
function renderSolutions() {
  const subs = Array.isArray(a.submissions) && a.submissions.length ? a.submissions : null;
  if (!subs) {
    return `<div class="card">
    <p><b>思路：</b>${esc(a.approach || '')}</p>
    <p><span class="chip">时间 ${esc(a.time_complexity || '—')}</span><span class="chip">空间 ${esc(a.space_complexity || '—')}</span></p>
    ${codeBlock(a.approach || fileBase, code)}
  </div>`;
  }
  return subs.map((s, i) => {
    const sCode = s.code || (i === subs.length - 1 ? code : '');
    const sLabel = s.code ? `${fileBase}.java · ${s.approach}` : path.relative(ROOT, srcFile).replace(/\\/g, '/');
    const codeHtml = sCode ? codeBlock(sLabel, sCode) : '<p class="muted">（该写法为中间版本，未单独留存代码，思路与指标见上；历史代码可在 git 中追溯）</p>';
    return `<div class="card" style="margin-top:12px">
    <h3 style="margin:0 0 8px">写法 ${i + 1} · ${esc(s.approach || '')}</h3>
    <p><span class="chip">时间 ${esc(s.time_complexity || '—')}</span><span class="chip">空间 ${esc(s.space_complexity || '—')}</span><span class="chip">内存 ${fmtMem(s.memory_bytes)}</span><span class="chip">用例 ${esc(s.testcases || '—')}</span></p>
    ${codeHtml}
  </div>`;
  }).join('');
}

// 题目回顾：渲染 problem.md（描述 / 示例 / 提示）
function renderProblem() {
  if (!probMd) return '';
  return `<details class="review-problem"><summary>题目回顾 <span class="muted">点击展开 / 收起</span></summary><div class="card md">${md(probMd)}</div></details>`;
}

// 历轮表现：每轮一张卡片并排（一轮=一刷，之后每轮一次复习）
function renderRounds() {
  const rounds = [];
  rounds.push({
    label: '第一轮（一刷）',
    approach: a.approach || '—',
    ts: [a.time_complexity, a.space_complexity].filter(Boolean).join(' · ') || '—'
  });
  if (reviews.length) {
    const last = reviews[reviews.length - 1];
    rounds.push({
      label: `第 ${esc(last.pass)} 轮（二刷）`,
      approach: last.approach || '—',
      ts: [last.time_complexity, last.space_complexity].filter(Boolean).join(' · ') || '—',
      mastery: last.mastery
    });
  }
  const cards = rounds.map((r, i) => `<div class="round-card${r.mastery === 'strong' ? ' strong' : r.mastery === 'weak' ? ' weak' : ''}">
      <h3>${i + 1}. ${esc(r.label)}</h3>
      <ul class="round-meta">
        <li><span>思路</span><b>${esc(r.approach)}</b></li>
        <li><span>复杂度</span><b>${esc(r.ts)}</b></li>
        ${r.mastery ? `<li><span>掌握度</span><b><span class="badge ${r.mastery === 'strong' ? 'ok' : 'hard'}">${r.mastery === 'strong' ? '较强 · 间隔 ×2.5' : '较弱 · 间隔重置'}</span></b></li>` : ''}
      </ul>
    </div>`).join('');
  return `<section><h2>历轮表现</h2><div class="rounds">${cards}</div></section>`;
}

// 同类题导航：同分类下其他已复盘题目
function renderSimilar() {
  const list = orderAll
    .filter(o => o.category === q.category && String(o.id) !== String(q.id) && reviewMap[String(o.id).padStart(4, '0')])
    .sort((x, y) => x.seq - y.seq)
    .slice(0, 8);
  if (!list.length) return '';
  const items = list.map(o => {
    const f = reviewMap[String(o.id).padStart(4, '0')];
    const dir = path.dirname(f);
    const base = path.basename(f, '.html');
    const href = '../' + (dir === '.' ? base + '.html' : dir + '/' + base + '.html');
    return `<li><a href="${href}">LC${o.id} · ${esc(o.title)}</a><span class="chip">${esc(o.difficulty)}</span></li>`;
  }).join('');
  return `<section><h2>同类题 · ${esc(q.category)}</h2><ul class="similar-list">${items}</ul></section>`;
}

const html = `<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>LC${q.id} ${q.title} · 刷题复盘</title>
<link rel="stylesheet" href="../review.css">
</head>
<body>
<div class="markdown-body">
  <div class="page-head">
  <div class="kicker">LEETCODE 刷题复盘 · SEQ ${q.seq}</div>
  <h1>LC${q.id} · ${esc(q.title)}</h1>
  <div class="sub">${esc(a.approach || '')} · ${a.verdict || ''}</div>
  <div class="badges">
    <span class="badge ok">✓ ${a.verdict || 'Accepted'} ${testcases}</span>
    <span class="badge ${diffCls}">难度 ${diff}</span>
    <span class="badge tag">分类 ${esc(q.category)}</span>
    ${a.dp_subtype ? `<span class="badge tag">子类型 ${esc(a.dp_subtype)}</span>` : ''}
    <span class="badge plain">${done.firstPass ? '一次通过' : '多次通过'}</span>
    <span class="badge ${done.optimal ? 'ok' : 'plain'}">${done.optimal ? '最优解' : '已满足题意·可进阶'}</span>
    ${codeNotes.length ? '<span class="badge hard">重点复习</span>' : ''}
    ${reviews.length ? `<span class="badge plain">二刷 ×${reviews.length}</span>` : ''}
    <span class="badge">${date}</span>
  </div>
</div>
  ${renderProblem()}
  ${renderReviews()}
  ${renderRounds()}
  <section><h2>我的解法</h2>${renderSolutions()}</section>
  ${approachDetail ? `<section><h2>思路拆解</h2><div class="card md">${md(approachDetail)}</div></section>` : ''}
  <section><h2>解题过程</h2><div class="tl">${tlItems.join('')}</div></section>
  ${patterns.length ? `<section><h2>套路沉淀</h2>${patterns.map(p => `<div class="card md"><h3>${esc(p.title)}</h3>${md(p.text)}</div>`).join('')}</section>` : ''}
  <section><h2>分析结果</h2>
    <div class="metrics">
      <div class="metric ok"><div class="v">${a.verdict || 'Accepted'}</div><div class="l">判题状态</div></div>
      <div class="metric ok"><div class="v">${testcases || '—'}</div><div class="l">通过用例</div></div>
      <div class="metric"><div class="v">${memory}</div><div class="l">内存占用</div></div>
      <div class="metric ok"><div class="v">${timeSpace || '—'}</div><div class="l">时间 · 空间</div></div>
      <div class="metric ok"><div class="v">${done.firstPass ? '一次' : '多次'}</div><div class="l">提交次数</div></div>
    </div>
    <div class="card">
      <h3>算法判定</h3>
      <p>${done.optimal ? '思路已达该题最优复杂度。' : '思路满足题意，但存在进阶最优写法（见复盘备注），可在复习轮重写。'}</p>
      ${hint ? `<h3>分类提醒 · ${esc(q.category)}</h3><div class="quote">${esc(hint)}</div>` : ''}
      ${mistakes.length ? `<h3>错误习惯复盘</h3>${mistakes.map(m => `<p><b>${esc(m.habit)}</b>（累计 ${m.count} 次）</p>`).join('')}` : '<h3>错误习惯复盘</h3><p>本轮无新增算法/逻辑级错误习惯。</p>'}
      ${codeNotes.length ? `<h3>复习提醒</h3>${codeNotes.map(n => `<div class="quote">★ ${esc(n)}</div>`).join('')}` : ''}
      <h3>完成记录</h3>
      <p><span class="chip">seq ${q.seq} · ${slug}</span><span class="chip">firstPass: ${done.firstPass}</span><span class="chip">optimal: ${done.optimal}</span></p>
      <div class="quote">${esc(a.notes || '')}</div>
    </div>
  </section>
  ${renderSimilar()}
  <div class="foot">
    数据来源：力扣判题接口 · <code>.lc/problems/${q.id}_${slug}/analysis.json</code> · <code>.lc/progress.json</code> ｜ 环境：IntelliJ IDEA + Java 21 + 力扣中国站<br>
    相关文件：<a href="../../src/${encodeURIComponent(safeCat(q.category))}/${encodeURIComponent(fileBase)}.java">src/${esc(q.category)}/${fileBase}.java</a> ·
    <a href="../../index.html">训练主页</a><br>
    生成于 ${date}
  </div>
</div>
<script>
function copyCode(btn) {
  var pre = btn.closest('.code-wrap').querySelector('pre');
  var text = pre.innerText;
  var done = function(){ flash(btn); };
  if (navigator.clipboard && navigator.clipboard.writeText) {
    navigator.clipboard.writeText(text).then(done, function(){ fallback(btn, text, done); });
  } else {
    fallback(btn, text, done);
  }
}
function fallback(btn, text, done) {
  var ta = document.createElement('textarea');
  ta.value = text;
  ta.style.position = 'fixed';
  ta.style.opacity = '0';
  document.body.appendChild(ta);
  ta.select();
  try { document.execCommand('copy'); done(); } catch (e) { alert('复制失败，请手动选择代码复制'); }
  document.body.removeChild(ta);
}
function flash(btn) {
  var old = btn.textContent;
  btn.textContent = '已复制 ✓';
  btn.classList.add('copied');
  setTimeout(function(){ btn.textContent = old; btn.classList.remove('copied'); }, 1200);
}
</script>
</body>
</html>`;

const outDir = path.join(ROOT, 'reviews', safeCat(q.category));
fs.mkdirSync(outDir, { recursive: true });
const outFile = path.join(outDir, `${fileBase}_Review.html`);
fs.writeFileSync(outFile, html);
console.log(`已生成：${outFile}`);
