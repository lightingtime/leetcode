// LC 刷题状态管理：进度查询 / 标记完成 / 记录错误习惯 / 分类提示
// 用法:
//   node update_state.js next
//   node update_state.js done --seq 5 --firstPass true --optimal false --notes "..." [--verdict Accepted --testcases 65/65 --memory 47308000 --approach "单遍哈希表" --time O(n) --space O(n)]
//   node update_state.js habit add --text "..." [--problem slug] [--category cat]
//   node update_state.js habit list
//   node update_state.js analysis --slug two-sum | --seq 1
//   node update_state.js checkin --seq 1 [--date 2026-08-01]
//   node update_state.js hint --category "哈希表"
//   node update_state.js stats
// 约定: progress.json 只存精简索引；每题详细分析存 .lc/problems/{id}_{slug}/analysis.json
//       打卡表（训练打卡.md）由 checkin 命令自动追加，无需用户提醒
const fs = require('fs');
const path = require('path');

const LC_DIR = 'D:/Projects/leetCode/.lc';
const ORDER = path.join(LC_DIR, 'order.json');
const PROGRESS = path.join(LC_DIR, 'progress.json');
function todayStr() {
  const d = new Date();
  return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
}

function readJson(p) { return JSON.parse(fs.readFileSync(p, 'utf8')); }
function writeJson(p, obj) { fs.writeFileSync(p, JSON.stringify(obj, null, 2), 'utf8'); }

const args = process.argv.slice(2);
function arg(name, def) { const i = args.indexOf(name); return i >= 0 ? args[i + 1] : def; }
function has(name) { return args.indexOf(name) >= 0; }

const cmd = args[0];
const order = readJson(ORDER);
const progress = readJson(PROGRESS);
const date = todayStr();

if (cmd === 'next') {
  const doneSeqs = new Set((progress.done || []).map(d => d.seq));
  const next = order.find(o => !doneSeqs.has(o.seq));
  console.log(`进度：已完成 ${doneSeqs.size}/${order.length}`);
  if (!next) {
    console.log('全部完成，进入复习模式（随机抽已完成题重写）。');
    process.exit(0);
  }
  console.log(`下一题：seq=${next.seq} | ${next.id}. ${next.title}（${next.difficulty}）分类=${next.category}`);
  console.log(`链接：${next.url}`);
  console.log('之后几题：');
  order.filter(o => o.seq >= next.seq && o.seq < next.seq + 5).forEach(o =>
    console.log(`  ${o.seq}. ${o.id}. ${o.title} [${o.category}]`));
  const habits = progress.error_habits || [];
  if (habits.length) {
    console.log('历史错误习惯提醒：');
    habits.slice().sort((a, b) => b.count - a.count).slice(0, 5).forEach(h =>
      console.log(`  [x${h.count}] ${h.habit} | 最近 ${h.last_seen} | 示例题: ${(h.examples || []).slice(-3).join(', ')}`));
  }
} else if (cmd === 'done') {
  const seq = parseInt(arg('--seq', '0'), 10);
  const q = order.find(o => o.seq === seq);
  if (!q) { console.error(`找不到 seq=${seq}`); process.exit(1); }
  const rec = {
    seq,
    id: q.id,
    title: q.title,
    slug: q.slug,
    category: q.category,
    date,
    firstPass: arg('--firstPass', 'true') === 'true',
    optimal: arg('--optimal', 'false') === 'true',
    notes: arg('--notes', '')
  };
  progress.done = (progress.done || []).filter(d => d.seq !== seq);
  progress.done.push(rec);
  progress.current_seq = Math.max(progress.current_seq || 0, seq);
  progress.updated = date;
  writeJson(PROGRESS, progress);

  // 详细分析按题写入 analysis.json，progress.json 不膨胀
  const dir = path.join(LC_DIR, 'problems', `${q.id}_${q.slug}`);
  fs.mkdirSync(dir, { recursive: true });
  const ap = path.join(dir, 'analysis.json');
  let prev = {};
  if (fs.existsSync(ap)) { try { prev = readJson(ap); } catch {} }
  const rt = arg('--runtime', '');
  const mem = arg('--memory', '');
  const analysis = {
    seq,
    id: q.id,
    slug: q.slug,
    title: q.title,
    category: q.category,
    date,
    verdict: arg('--verdict', '') || prev.verdict || '',
    testcases: arg('--testcases', '') || prev.testcases || '',
    runtime_ms: rt ? parseInt(rt, 10) : (prev.runtime_ms != null ? prev.runtime_ms : null),
    memory_bytes: mem ? parseInt(mem, 10) : (prev.memory_bytes != null ? prev.memory_bytes : null),
    firstPass: rec.firstPass,
    optimal: rec.optimal,
    approach: arg('--approach', '') || prev.approach || '',
    time_complexity: arg('--time', '') || prev.time_complexity || '',
    space_complexity: arg('--space', '') || prev.space_complexity || '',
    notes: rec.notes || prev.notes || '',
    mistakes: Array.isArray(prev.mistakes) ? prev.mistakes : [],
    hints: (progress.category_hints && progress.category_hints[q.category]) ? [progress.category_hints[q.category]] : []
  };
  writeJson(ap, analysis);
  console.log(`已记录完成：${q.id}. ${q.title}`);
  console.log(`详细分析：${ap}`);
} else if (cmd === 'habit' && has('add')) {
  const text = arg('--text', '');
  const slug = arg('--problem', '');
  const category = arg('--category', '');
  if (!text) { console.error('--text 必填'); process.exit(1); }
  let h = (progress.error_habits || []).find(x => x.habit === text);
  if (!h) {
    h = { habit: text, count: 0, examples: [], categories: [], last_seen: '' };
    progress.error_habits.push(h);
  }
  h.count++;
  if (slug && !h.examples.includes(slug)) h.examples.push(slug);
  if (category && !h.categories.includes(category)) h.categories.push(category);
  h.last_seen = date;
  progress.updated = date;
  writeJson(PROGRESS, progress);

  // 同时把明细记入对应题目的 analysis.json（按题存储，progress 只留聚合索引）
  if (slug) {
    const q = order.find(o => o.slug === slug);
    if (q) {
      const dir = path.join(LC_DIR, 'problems', `${q.id}_${q.slug}`);
      fs.mkdirSync(dir, { recursive: true });
      const ap = path.join(dir, 'analysis.json');
      let a = {
        seq: q.seq, id: q.id, slug: q.slug, title: q.title, category: q.category, date,
        verdict: '', testcases: '', runtime_ms: null, memory_bytes: null,
        firstPass: false, optimal: false, approach: '', time_complexity: '', space_complexity: '',
        notes: '', mistakes: [], hints: []
      };
      if (fs.existsSync(ap)) { try { a = Object.assign(a, readJson(ap)); } catch {} }
      if (!Array.isArray(a.mistakes)) a.mistakes = [];
      const m = a.mistakes.find(x => x.habit === text);
      if (m) m.count = (m.count || 0) + 1;
      else a.mistakes.push({ habit: text, count: 1 });
      writeJson(ap, a);
    }
  }
  console.log(`已记录错误习惯：${text}（累计 ${h.count} 次）`);
} else if (cmd === 'habit' && has('list')) {
  (progress.error_habits || []).slice().sort((a, b) => b.count - a.count)
    .forEach(h => console.log(`[x${h.count}] ${h.habit} | ${(h.categories || []).join(',')} | 最近 ${h.last_seen}`));
} else if (cmd === 'analysis') {
  const slug = arg('--slug', '');
  const seqRaw = arg('--seq', '');
  let q = null;
  if (seqRaw) q = order.find(o => o.seq === parseInt(seqRaw, 10));
  else if (slug) q = order.find(o => o.slug === slug);
  if (!q) { console.error('请用 --slug <slug> 或 --seq <seq> 指定题目'); process.exit(1); }
  const ap = path.join(LC_DIR, 'problems', `${q.id}_${q.slug}`, 'analysis.json');
  if (!fs.existsSync(ap)) { console.log(`暂无分析记录：${q.id}. ${q.title}`); process.exit(0); }
  console.log(JSON.stringify(readJson(ap), null, 2));
} else if (cmd === 'checkin') {
  const seq = parseInt(arg('--seq', '0'), 10);
  const q = order.find(o => o.seq === seq);
  if (!q) { console.error(`找不到 seq=${seq}`); process.exit(1); }
  const doneRec = (progress.done || []).find(d => d.seq === seq);
  if (!doneRec) { console.error(`seq=${seq} 尚未标记完成，请先执行 done`); process.exit(1); }
  const date = arg('--date', todayStr());
  const review = new Date(date + 'T00:00:00');
  review.setDate(review.getDate() + 1);
  const reviewStr = review.toISOString().slice(0, 10);
  const file = 'D:/Projects/leetCode/训练打卡.md';
  if (!fs.existsSync(file)) { console.error(`找不到打卡表：${file}`); process.exit(1); }
  let text = fs.readFileSync(file, 'utf8');
  const lines = text.split(/\r?\n/);
  let hdr = -1;
  for (let i = 0; i < lines.length; i++) {
    if (lines[i].includes('| 日期 | 题号 |')) { hdr = i; break; }
  }
  if (hdr < 0) { console.error('打卡表里没有找到表头（| 日期 | 题号 | ...）'); process.exit(1); }
  const row = `| ${date} | ${q.id} | ${q.category} | ${doneRec.firstPass ? '是' : '否'} | ${doneRec.optimal ? '是' : '否'} | ${reviewStr} |`;
  const already = lines.some(l => /^\s*\|/.test(l) && !l.includes('---') && l.includes(`| ${q.id} |`));
  if (already) {
    console.log(`打卡表中已记录题号 ${q.id}（${q.title}），跳过。`);
  } else {
    let at = hdr + 2;
    while (at < lines.length && /^\s*\|/.test(lines[at]) && !lines[at].includes('---')) at++;
    lines.splice(at, 0, row);
    let end = at + 1;
    while (end < lines.length && lines[end].trim() !== '' && !lines[end].trim().startsWith('>')) end++;
    const cnt = end - (hdr + 2);
    text = lines.join('\n');
    text = text.replace(/已完成：\d+ 次/, `已完成：${cnt} 次`);
    fs.writeFileSync(file, text, 'utf8');
    console.log(`已追加打卡：${date} | ${q.id}. ${q.title}（${q.category}，一次AC=${doneRec.firstPass ? '是' : '否'}，最优=${doneRec.optimal ? '是' : '否'}，复习 ${reviewStr}）`);
    console.log(`当前进度：${cnt}/30 次`);
  }
} else if (cmd === 'hint') {
  const cat = arg('--category', '');
  console.log((progress.category_hints || {})[cat] || '暂无该分类提示');
} else if (cmd === 'stats') {
  const done = progress.done || [];
  console.log(`已完成：${done.length}/${order.length}`);
  const byCat = {};
  done.forEach(d => { byCat[d.category] = (byCat[d.category] || 0) + 1; });
  Object.entries(byCat).sort((a, b) => b[1] - a[1]).forEach(([c, n]) => console.log(`  ${c}: ${n}`));
  console.log(`错误习惯：${(progress.error_habits || []).length} 条`);
  console.log(`最近完成：${(done[done.length - 1] || {}).title || '无'}`);
} else {
  console.log('用法: next | done --seq N [--firstPass true] [--optimal true] [--notes "..."] [--verdict ... --testcases ... --memory ... --approach ... --time ... --space ...] | habit add|list | analysis --slug <slug> | checkin --seq N [--date ...] | hint --category <分类> | stats');
}
