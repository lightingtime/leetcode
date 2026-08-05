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
const esc = s => String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
const diff = String(a.difficulty || q.difficulty || 'MEDIUM').toUpperCase();
const diffCls = diff === 'EASY' ? 'easy' : diff === 'HARD' ? 'hard' : 'medium';
const date = a.date || done.date || '';
const testcases = a.testcases || '';
const memory = a.memory_bytes != null ? `≈ ${(a.memory_bytes / 1048576).toFixed(1)} MB` : '—';
const timeSpace = [a.time_complexity, a.space_complexity].filter(Boolean).join(' · ');
const mistakes = Array.isArray(a.mistakes) ? a.mistakes : [];
const codeNotes = Array.isArray(a.code_notes) ? a.code_notes : [];

const tlItems = [];
mistakes.forEach((m, i) => tlItems.push(
  `<div class="tl-item"><div class="tl-marker"></div><div class="tl-body"><span class="tl-title">0${i + 1} · 踩坑<span class="tl-badge bug">已记录</span></span><p>${esc(m.habit)}</p></div></div>`));
tlItems.push(
  `<div class="tl-item"><div class="tl-marker"></div><div class="tl-body"><span class="tl-title">0${mistakes.length + 1} · 本地测试<span class="tl-badge ok">通过</span></span><p>示例 + 边界用例本地全部通过后提交。</p></div></div>`,
  `<div class="tl-item"><div class="tl-marker"></div><div class="tl-body"><span class="tl-title">0${mistakes.length + 2} · 提交<span class="tl-badge ok">${a.verdict || 'Accepted'}</span></span><p>${esc(testcases)} 用例${a.firstPass ? '一次通过' : ''}。</p></div></div>`);

const html = `<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>LC${q.id} ${q.title} · 刷题复盘</title>
<style>
  :root{--bg:#f6f7f9;--card:#fff;--ink:#1f2329;--muted:#6b7280;--line:#e5e7eb;--accent:#2563eb;--good:#16a34a;--bad:#dc2626;--code-bg:#0f172a;--code-ink:#e2e8f0}
  *{box-sizing:border-box}body{margin:0;background:var(--bg);color:var(--ink);font-family:"Segoe UI","Microsoft YaHei","PingFang SC",system-ui,sans-serif;line-height:1.7}
  .wrap{max-width:920px;margin:0 auto;padding:0 22px 64px}
  .hero{background:linear-gradient(135deg,#0f172a 0%,#1e3a8a 100%);color:#fff;padding:52px 22px 46px;margin-bottom:34px}
  .hero .wrap{padding:0}.hero .kicker{font-size:13px;letter-spacing:2px;color:#93c5fd;font-weight:600}
  .hero h1{margin:8px 0 6px;font-size:34px;line-height:1.25}.hero .sub{color:#cbd5e1;font-size:15px}
  .hero .badges{margin-top:18px;display:flex;flex-wrap:wrap;gap:8px}
  .badge{display:inline-flex;align-items:center;gap:6px;padding:4px 12px;border-radius:999px;font-size:12.5px;font-weight:600;background:rgba(255,255,255,.14);color:#e2e8f0}
  .badge.ok,.badge.easy{background:#dcfce7;color:#166534}.badge.hard{background:#fee2e2;color:#991b1b}
  .badge.medium{background:#fef3c7;color:#92400e}.badge.tag{background:#e0e7ff;color:#3730a3}.badge.plain{background:#e5e7eb;color:#374151}
  section{margin-top:40px}h2{font-size:21px;margin:0 0 14px;padding-left:12px;border-left:4px solid var(--accent)}h3{font-size:16px;margin:22px 0 8px}p{margin:8px 0}
  .muted{color:var(--muted);font-size:13.5px}a{color:var(--accent);text-decoration:none}a:hover{text-decoration:underline}
  .card{background:var(--card);border:1px solid var(--line);border-radius:14px;padding:20px 22px;margin-top:14px;box-shadow:0 1px 3px rgba(15,23,42,.05)}
  .metrics{display:grid;grid-template-columns:repeat(auto-fit,minmax(150px,1fr));gap:14px}
  .metric{background:var(--card);border:1px solid var(--line);border-radius:12px;padding:16px 12px;text-align:center}
  .metric .v{font-size:25px;font-weight:800}.metric .l{font-size:12.5px;color:var(--muted);margin-top:4px}.metric.ok .v{color:var(--good)}
  .chip{display:inline-block;padding:2px 10px;border-radius:6px;background:#eef2ff;color:#3730a3;font-size:12.5px;font-weight:600;margin:2px 4px 2px 0}
  .tl{display:flex;flex-direction:column}.tl-item{display:grid;grid-template-columns:34px 1fr;gap:16px}
  .tl-marker{position:relative}.tl-marker::before{content:"";position:absolute;top:8px;left:50%;transform:translateX(-50%);width:13px;height:13px;border-radius:50%;background:var(--accent);box-shadow:0 0 0 3px #dbeafe}
  .tl-marker::after{content:"";position:absolute;top:25px;bottom:-12px;left:50%;width:2px;transform:translateX(-50%);background:var(--line)}
  .tl-item:last-child .tl-marker::after{display:none}.tl-body{padding-bottom:26px}.tl-title{font-weight:700;font-size:15.5px}
  .tl-badge{display:inline-block;font-size:11.5px;font-weight:700;border-radius:5px;padding:1px 8px;margin-left:8px;vertical-align:2px}
  .tl-badge.ok{background:#dcfce7;color:#166534}.tl-badge.bug{background:#fee2e2;color:#991b1b}
  .ok-list{margin:10px 0;padding:0;list-style:none}.ok-list li{padding:6px 0 6px 26px;position:relative}
  .ok-list li::before{content:"✓";position:absolute;left:4px;color:var(--good);font-weight:800}
  .quote{border-left:3px solid var(--accent);background:#f8fafc;padding:10px 14px;border-radius:0 8px 8px 0;margin:12px 0;font-size:14.5px}
  .code-wrap{background:var(--code-bg);border-radius:12px;overflow:hidden;margin:12px 0}
  .code-head{display:flex;justify-content:space-between;align-items:center;padding:8px 16px;background:rgba(255,255,255,.06);color:#94a3b8;font-size:12px;font-family:Consolas,"Cascadia Code",monospace}
  .code-wrap pre{margin:0;padding:16px 18px;overflow-x:auto;color:var(--code-ink);font:13px/1.75 Consolas,"Cascadia Code",monospace;tab-size:4}
  .foot{margin-top:56px;padding-top:18px;border-top:1px solid var(--line);color:var(--muted);font-size:12.5px;text-align:center}
  @media(max-width:600px){.hero h1{font-size:26px}.metrics{grid-template-columns:repeat(2,1fr)}}
</style>
</head>
<body>
<div class="hero"><div class="wrap">
  <div class="kicker">LEETCODE 刷题复盘 · SEQ ${q.seq}</div>
  <h1>LC${q.id} · ${esc(q.title)}</h1>
  <div class="sub">${esc(a.approach || '')} · ${a.verdict || ''}</div>
  <div class="badges">
    <span class="badge ok">✓ ${a.verdict || 'Accepted'} ${testcases}</span>
    <span class="badge ${diffCls}">难度 ${diff}</span>
    <span class="badge tag">分类 ${esc(q.category)}</span>
    <span class="badge plain">${done.firstPass ? '一次通过' : '多次通过'}</span>
    <span class="badge ${done.optimal ? 'ok' : 'plain'}">${done.optimal ? '最优解' : '已满足题意·可进阶'}</span>
    ${codeNotes.length ? '<span class="badge hard">重点复习</span>' : ''}
    <span class="badge">${date}</span>
  </div>
</div></div>
<div class="wrap">
  <section><h2>我的解法</h2><div class="card">
    <p><b>思路：</b>${esc(a.approach || '')}</p>
    <p><span class="chip">时间 ${esc(a.time_complexity || '—')}</span><span class="chip">空间 ${esc(a.space_complexity || '—')}</span></p>
    ${code ? `<div class="code-wrap"><div class="code-head"><span>${esc(path.relative(ROOT, srcFile).replace(/\\/g, '/'))}</span><span>提交区</span></div><pre>${esc(code)}</pre></div>` : ''}
  </div></section>
  <section><h2>解题过程</h2><div class="tl">${tlItems.join('')}</div></section>
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
  <div class="foot">
    数据来源：力扣判题接口 · <code>.lc/problems/${q.id}_${slug}/analysis.json</code> · <code>.lc/progress.json</code> ｜ 环境：IntelliJ IDEA + Java 21 + 力扣中国站<br>
    相关文件：<a href="../../src/${encodeURIComponent(q.category)}/${encodeURIComponent(fileBase)}.java">src/${esc(q.category)}/${fileBase}.java</a> ·
    <a href="../../index.html">训练主页</a><br>
    生成于 ${date}
  </div>
</div>
</body>
</html>`;

const outDir = path.join(ROOT, 'reviews', q.category);
fs.mkdirSync(outDir, { recursive: true });
const outFile = path.join(outDir, `${fileBase}_Review.html`);
fs.writeFileSync(outFile, html);
console.log(`已生成：${outFile}`);
