#!/usr/bin/env node
// 套路沉淀库工具：按需查询/追加，避免把 68KB 的 patterns.md 整篇读进上下文
//
// 数据布局：references/patterns/index.md（索引）+ references/patterns/{分类}.md（正文）
//
// 用法（仓库根目录）：
//   node ".agents/skills/lc-analyze/scripts/patterns.js" list [--category 链表] [--grep 单调] [--max N]
//   node ".agents/skills/lc-analyze/scripts/patterns.js" find <关键词...> [--titles] [--max N]   ← 查套路默认用这个
//   node ".agents/skills/lc-analyze/scripts/patterns.js" show <编号|关键词>
//   node ".agents/skills/lc-analyze/scripts/patterns.js" add --title "..." --text "..." [--category 链表] [--source LC0141|slug] [--dry-run]
//   node ".agents/skills/lc-analyze/scripts/patterns.js" check
//   node ".agents/skills/lc-analyze/scripts/patterns.js" split            （一次性迁移：把单文件 patterns.md 拆成上面的布局）
const fs = require('fs');
const path = require('path');

const ROOT = path.resolve(__dirname, '..', '..', '..', '..');
const REFS = path.join(ROOT, '.agents', 'skills', 'lc-analyze', 'references');
const DIR = path.join(REFS, 'patterns');
const INDEX = path.join(DIR, 'index.md');
const LEGACY = path.join(REFS, 'patterns.md');

const args = process.argv.slice(2);
const cmd = args[0];
function arg(name, def) { const i = args.indexOf(name); return i >= 0 && args[i + 1] ? args[i + 1] : def; }
function has(name) { return args.indexOf(name) >= 0; }
function readJson(p) { return JSON.parse(fs.readFileSync(p, 'utf8')); }

function orderIndex() {
  const order = readJson(path.join(ROOT, '.lc', 'order.json'));
  const byId = new Map(order.map(o => [String(parseInt(o.id, 10)), o]));
  return { order, byId };
}

// 从「出处」行推断分类：取引用题号中出现最多的分类
function categoryOf(sectionText, byId) {
  const ids = [...sectionText.matchAll(/LC(\d+)/g)].map(m => String(parseInt(m[1], 10)));
  const counts = {};
  for (const id of ids) {
    const o = byId.get(id);
    if (!o) continue;
    counts[o.category] = (counts[o.category] || 0) + 1;
  }
  const best = Object.entries(counts).sort((a, b) => b[1] - a[1])[0];
  return best ? best[0] : '其他';
}

function fileFor(category) {
  return path.join(DIR, category.replace(/[/\\]/g, '-') + '.md');
}

function parseSections(text) {
  const re = /^## (\d+)\. (.+)$/gm;
  const marks = [];
  let m;
  while ((m = re.exec(text)) !== null) marks.push({ n: parseInt(m[1], 10), title: m[2].trim(), start: m.index });
  const sections = [];
  for (let i = 0; i < marks.length; i++) {
    const end = i + 1 < marks.length ? marks[i + 1].start : text.length;
    sections.push({ n: marks[i].n, title: marks[i].title, body: text.slice(marks[i].start, end).replace(/\s+$/, '') + '\n' });
  }
  return sections;
}

function loadPatterns() {
  if (!fs.existsSync(DIR)) return [];
  const out = [];
  for (const f of fs.readdirSync(DIR).filter(f => f.endsWith('.md') && f !== 'index.md').sort()) {
    const text = fs.readFileSync(path.join(DIR, f), 'utf8');
    for (const s of parseSections(text)) out.push({ ...s, file: f });
  }
  return out;
}

function categoryFromFile(file) { return file.replace(/\.md$/, ''); }

function writeIndex(sections) {
  const lines = [
    '# 套路沉淀索引（Patterns Index）',
    '',
    '> 正文按分类拆在 `references/patterns/{分类}.md`。查套路用',
    '> `node ".agents/skills/lc-analyze/scripts/patterns.js" find <关键词>`（或 `list` / `show <编号>`），不要整目录通读。',
    '',
    `共 ${sections.length} 条。`,
    '',
    '| # | 套路 | 分类 | 文件 |',
    '| --- | --- | --- | --- |'
  ];
  for (const s of sections.slice().sort((a, b) => a.n - b.n || a.title.localeCompare(b.title))) {
    lines.push(`| ${s.n} | ${s.title.replace(/\|/g, '\\|')} | ${categoryFromFile(s.file)} | [${s.file}](./${encodeURI(s.file)}) |`);
  }
  lines.push('');
  fs.writeFileSync(INDEX, lines.join('\n'), 'utf8');
}

function writeCategoryFile(category, sections) {
  const header = [
    `# 套路沉淀 · ${category}`,
    '',
    '> 本文件由 `patterns.js` 维护（`add` 追加、`split` 迁移）；索引见 `index.md`。',
    '> 新增套路务必用 `patterns.js add`，不要手改编号。',
    ''
  ].join('\n');
  const body = sections.slice().sort((a, b) => a.n - b.n).map(s => s.body).join('\n');
  fs.writeFileSync(fileFor(category), header + body, 'utf8');
}

if (cmd === 'split') {
  if (!fs.existsSync(LEGACY)) { console.error('找不到 references/patterns.md，无需拆分'); process.exit(1); }
  const raw = fs.readFileSync(LEGACY, 'utf8');
  const sections = parseSections(raw);
  if (!sections.length) { console.error('patterns.md 中没解析到 `## N. 标题` 小节'); process.exit(1); }
  const { byId } = orderIndex();
  const byCat = new Map();
  for (const s of sections) {
    const cat = categoryOf(s.body, byId);
    if (!byCat.has(cat)) byCat.set(cat, []);
    byCat.get(cat).push(s);
  }
  if (!fs.existsSync(DIR)) fs.mkdirSync(DIR, { recursive: true });
  for (const [cat, list] of byCat) writeCategoryFile(cat, list.map(s => ({ ...s, file: cat.replace(/[/\\]/g, '-') + '.md' })));
  writeIndex([...byCat].flatMap(([cat, list]) => list.map(s => ({ ...s, file: cat.replace(/[/\\]/g, '-') + '.md' }))));

  // 回读校验：拆分前后小节数与正文逐条一致
  const back = loadPatterns();
  const norm = t => t.replace(/\s+/g, ' ').trim();
  const before = sections.map(s => s.n + '::' + norm(s.body)).sort();
  const after = back.map(s => s.n + '::' + norm(s.body)).sort();
  const same = before.length === after.length && before.every((v, i) => v === after[i]);
  console.log(`拆分完成：${sections.length} 条 → ${byCat.size} 个分类文件 + index.md（回读校验：${same ? '一致 ✓' : '不一致 ✗'}）`);
  for (const [cat, list] of [...byCat].sort((a, b) => b[1].length - a[1].length)) {
    console.log(`  ${cat}: ${list.length} 条（#{${list.map(s => s.n).join(',')}}）`);
  }
  if (!same) process.exit(2);
} else if (cmd === 'list' || cmd === 'find' || cmd === 'show') {
  const sections = loadPatterns();
  if (!sections.length) { console.error(`套路库为空（${DIR} 下没有分类文件）`); process.exit(1); }
  const catFilter = arg('--category', '');
  const max = parseInt(arg('--max', '0'), 10) || 0;
  if (cmd === 'list') {
    const grep = arg('--grep', '');
    let list = sections.slice().sort((a, b) => a.n - b.n);
    if (catFilter) list = list.filter(s => categoryFromFile(s.file) === catFilter);
    if (grep) list = list.filter(s => s.title.includes(grep) || s.body.includes(grep));
    if (max) list = list.slice(0, max);
    for (const s of list) console.log(`${s.n}. ${s.title} [${categoryFromFile(s.file)}]`);
    console.log(`（共 ${sections.length} 条；正文见 references/patterns/{分类}.md，单条用 show <编号>）`);
  } else if (cmd === 'show') {
    const key = args[1];
    if (!key) { console.error('用法：patterns.js show <编号|关键词>'); process.exit(1); }
    const num = parseInt(key, 10);
    let hit = !Number.isNaN(num) && String(num) === key ? sections.filter(s => s.n === num) : [];
    if (!hit.length) hit = sections.filter(s => s.title.includes(key));
    if (!hit.length) hit = sections.filter(s => s.body.includes(key));
    if (!hit.length) { console.log(`未找到：${key}`); process.exit(1); }
    for (const s of hit.slice(0, max || 3)) console.log(`<!-- ${s.file} -->\n${s.body}`);
  } else {
    const terms = args.slice(1).filter(a => !a.startsWith('--'));
    if (!terms.length) { console.error('用法：patterns.js find <关键词...> [--titles]'); process.exit(1); }
    let hit = sections.filter(s => terms.some(t => s.title.includes(t) || s.body.includes(t)));
    if (catFilter) hit = hit.filter(s => categoryFromFile(s.file) === catFilter);
    if (!hit.length) { console.log(`套路库暂无匹配（${terms.join(' / ')}）——可考虑新增，用 patterns.js add`); process.exit(1); }
    const shown = max ? hit.slice(0, max) : hit;
    for (const s of shown) {
      if (has('--titles')) console.log(`${s.n}. ${s.title} [${categoryFromFile(s.file)}]`);
      else console.log(`<!-- ${s.file} -->\n${s.body}`);
    }
    if (shown.length < hit.length) console.log(`（另有 ${hit.length - shown.length} 条匹配，用 --max N 或 list --grep 缩小）`);
  }
} else if (cmd === 'add') {
  const title = arg('--title', '');
  const text = arg('--text', '');
  const source = arg('--source', '');
  if (!title || !text) { console.error('用法：patterns.js add --title "..." --text "..." [--category 分类] [--source LC0141|slug]'); process.exit(1); }
  const { byId, order } = orderIndex();
  let category = arg('--category', '');
  let origin = source ? `出处：${source}` : '出处：（待补）';
  if (source) {
    const m = source.match(/(\d+)/);
    const o = m ? byId.get(String(parseInt(m[1], 10))) : order.find(x => x.slug === source);
    if (o) {
      if (!category) category = o.category;
      origin = `出处：LC${String(o.id).padStart(4, '0')} ${o.title}`;
    } else if (!category) {
      console.error(`无法从 --source "${source}" 定位题目，请显式传 --category`); process.exit(1);
    }
  }
  if (!category) { console.error('请传 --category 或 --source'); process.exit(1); }
  const sections = loadPatterns();
  const nextN = sections.reduce((mx, s) => Math.max(mx, s.n), 0) + 1;
  const body = `## ${nextN}. ${title}\n\n${origin}\n\n${text.replace(/\s+$/, '')}\n\n`;
  if (has('--dry-run')) {
    console.log(`[dry-run] 将追加到 references/patterns/${category}.md：\n${body}`);
    process.exit(0);
  }
  const file = fileFor(category);
  const exists = fs.existsSync(file);
  if (!exists) fs.writeFileSync(file, `# 套路沉淀 · ${category}\n\n> 本文件由 \`patterns.js\` 维护（\`add\` 追加、\`split\` 迁移）；索引见 \`index.md\`。\n> 新增套路务必用 \`patterns.js add\`，不要手改编号。\n\n`, 'utf8');
  fs.appendFileSync(file, body, 'utf8');
  writeIndex(loadPatterns());
  console.log(`已记录套路 #${nextN}：${title}（references/patterns/${category.replace(/[/\\]/g, '-')}.md，共 ${sections.length + 1} 条）`);
} else if (cmd === 'check') {
  const sections = loadPatterns();
  if (!sections.length) { console.error('套路库为空'); process.exit(1); }
  const nums = sections.map(s => s.n);
  const dup = nums.filter((n, i) => nums.indexOf(n) !== i);
  const maxN = Math.max(...nums);
  const missing = [];
  for (let i = 1; i <= maxN; i++) if (!nums.includes(i)) missing.push(i);
  const indexText = fs.existsSync(INDEX) ? fs.readFileSync(INDEX, 'utf8') : '';
  const indexRows = (indexText.match(/^\| \d+ \|/gm) || []).length;
  const files = fs.readdirSync(DIR).filter(f => f.endsWith('.md') && f !== 'index.md');
  console.log(`分类文件 ${files.length} 个 | 正文小节 ${sections.length} 条 | index 行 ${indexRows} 行`);
  console.log(`编号 ${Math.min(...nums)}..${maxN}${missing.length ? '，缺号 ' + missing.join(',') : ''}${dup.length ? '，重复编号 ' + [...new Set(dup)].join(',') : ''}`);
  if (indexRows !== sections.length) { console.error(`index.md 与正文条数不一致（${indexRows} vs ${sections.length}）：跑 patterns.js add/split 重生成`); process.exit(2); }
  console.log('一致性检查通过 ✓');
} else {
  console.log('用法: patterns.js list|find|show|add|check|split  （详见文件头注释）');
  process.exit(cmd ? 1 : 0);
}
