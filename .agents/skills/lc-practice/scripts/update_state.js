// LC 刷题状态管理：进度查询 / 标记完成 / 记录错误习惯 / 分类提示
// 用法:
//   node update_state.js next
//   node update_state.js done --seq 5 --firstPass true --optimal false --notes "..." [--verdict Accepted --testcases 65/65 --memory 47308000 --approach "单遍哈希表" --time O(n) --space O(n)]
//   node update_state.js habit add --text "..." [--problem slug] [--category cat]
//   node update_state.js habit list
//   node update_state.js pattern add --slug S --title "套路名" --text "套路总结" [--category cat]
//   node update_state.js pattern list [--slug S]
//   node update_state.js analysis --slug two-sum | --seq 1
//   node update_state.js checkin --seq 1
//   node update_state.js hint --category "哈希表"
//   node update_state.js stats
// 约定: progress.json 只存精简索引；每题详细分析存 .lc/problems/{id}_{slug}/analysis.json
//       打卡表并入训练主页 reviews/index.html，由 checkin 命令自动重新生成，无需用户提醒
//       可复用套路（如指针判空取舍）用 pattern add 记录到 analysis.json 的 patterns 字段，并同步沉淀到 lc-analyze skill 的 references/patterns.md
//       done 标记 Accepted 时自动维护 analysis.json 的 submissions 历史：解法不同则追加；相似解法（仅代码微调）只保留最优解
//       每次打卡（checkin）自动核对 30 天计划：目标 30 天打卡，训练方案节奏每天 4-5 道新题
const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');

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
const TARGET_CHECKINS = 30; // 30 天计划：30 天打卡

function daysBetween(a, b) {
  const pa = a.split('-').map(Number), pb = b.split('-').map(Number);
  const da = new Date(pa[0], pa[1] - 1, pa[2]);
  const db = new Date(pb[0], pb[1] - 1, pb[2]);
  return Math.round((db - da) / 86400000);
}

function planReport(p) {
  const done = p.done || [];
  const dates = [...new Set(done.map(d => d.date))].sort();
  const checkins = dates.length; // 打卡按天去重
  const problems = done.length;
  const lines = [`30 天计划打卡天数：${checkins}/${TARGET_CHECKINS}（目标 ${TARGET_CHECKINS} 天打卡，已完成 ${problems} 题）`];
  if (checkins >= TARGET_CHECKINS) {
    lines.push(`已达成 ${TARGET_CHECKINS} 天打卡目标（完成 ${problems} 题），进入复习 / 二刷阶段`);
    return lines;
  }
  const first = dates[0];
  const elapsed = first ? daysBetween(first, date) + 1 : 1;
  const remaining = TARGET_CHECKINS - checkins;
  const daily = problems / elapsed;
  lines.push(`训练已进行 ${elapsed} 天（自 ${first}），打卡 ${checkins} 天，完成 ${problems} 题（日均 ${daily.toFixed(1)} 题），还差 ${remaining} 天打卡`);
  const rate = checkins / elapsed; // 打卡节奏：多少天里实际打卡
  if (rate > 0) {
    const needDays = Math.ceil(remaining / rate);
    const d = new Date();
    d.setDate(d.getDate() + needDays);
    const ymd = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    lines.push(`按当前打卡节奏（${elapsed} 天里打卡 ${checkins} 天），预计 ${ymd} 前后达成目标`);
  }
  const expLow = 4 * elapsed, expHigh = 5 * elapsed;
  if (problems < expLow) lines.push(`训练方案节奏为每天 4-5 道新题：当前期望 ${expLow}-${expHigh} 题，落后约 ${expLow - problems} 题`);
  else if (problems > expHigh) lines.push(`训练方案节奏为每天 4-5 道新题：当前期望 ${expLow}-${expHigh} 题，已超前`);
  else lines.push(`训练方案节奏为每天 4-5 道新题：当前期望 ${expLow}-${expHigh} 题，进度正常`);
  return lines;
}

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
    patterns: Array.isArray(prev.patterns) ? prev.patterns : [],
    submissions: Array.isArray(prev.submissions) ? prev.submissions : [],
    hints: (progress.category_hints && progress.category_hints[q.category]) ? [progress.category_hints[q.category]] : []
  };
  // 提交历史：解法不同则追加；相似解法（仅代码微调）只保留最优解
  if (analysis.verdict === 'Accepted') {
    const sub = {
      date,
      verdict: analysis.verdict,
      testcases: analysis.testcases,
      runtime_ms: analysis.runtime_ms,
      memory_bytes: analysis.memory_bytes,
      approach: analysis.approach,
      time_complexity: analysis.time_complexity,
      space_complexity: analysis.space_complexity,
      optimal: analysis.optimal,
      notes: analysis.notes
    };
    const sameApproach = analysis.submissions.find(s => s.approach === sub.approach);
    if (sameApproach) {
      const better = (sub.optimal && !sameApproach.optimal) ||
        (sub.optimal === sameApproach.optimal && sub.memory_bytes != null &&
          (sameApproach.memory_bytes == null || sub.memory_bytes < sameApproach.memory_bytes));
      if (better) Object.assign(sameApproach, sub);
    } else {
      analysis.submissions.push(sub);
    }
  }
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
} else if (cmd === 'pattern' && has('add')) {
  const slug = arg('--slug', '');
  const title = arg('--title', '');
  const text = arg('--text', '');
  const category = arg('--category', '');
  const q = order.find(o => o.slug === slug);
  if (!q || !title || !text) { console.error('--slug/--title/--text 必填'); process.exit(1); }
  const ap = path.join(LC_DIR, 'problems', `${q.id}_${q.slug}`, 'analysis.json');
  let a = {};
  if (fs.existsSync(ap)) { try { a = readJson(ap); } catch {} }
  if (!Array.isArray(a.patterns)) a.patterns = [];
  if (a.patterns.some(p => p.title === title && p.text === text)) {
    console.log(`套路已存在（${q.id}. ${q.title}）：${title}`);
    process.exit(0);
  }
  a.patterns.push({ title, text, category: category || q.category, problem: slug, date });
  writeJson(ap, a);
  console.log(`已记录套路：${q.id}. ${q.title} → ${title}`);
} else if (cmd === 'pattern' && has('list')) {
  const slug = arg('--slug', '');
  const problemsDir = path.join(LC_DIR, 'problems');
  const dirs = fs.existsSync(problemsDir) ? fs.readdirSync(problemsDir) : [];
  const seen = new Map();
  for (const d of dirs) {
    const ap = path.join(problemsDir, d, 'analysis.json');
    if (!fs.existsSync(ap)) continue;
    let a = {};
    try { a = readJson(ap); } catch { continue; }
    if (!Array.isArray(a.patterns)) continue;
    for (const p of a.patterns) {
      if (slug && p.problem !== slug) continue;
      if (!seen.has(p.title)) seen.set(p.title, { ...p, count: 0 });
      seen.get(p.title).count++;
    }
  }
  if (!seen.size) { console.log('暂无套路记录'); process.exit(0); }
  for (const [title, p] of seen) {
    console.log(`[x${p.count}] ${title}（${p.category || ''}）: ${p.text}`);
  }
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
  // 打卡数据以 progress.json 为唯一来源，重新生成训练主页（打卡表 + 进度 + 复盘列表）
  const r = spawnSync('node', [path.join(__dirname, 'build_site.js')], { encoding: 'utf8' });
  if (r.status !== 0) {
    console.error('生成主页失败：' + (r.stderr || r.stdout || '').trim());
    process.exit(1);
  }
  console.log(`已打卡并更新主页：${q.id}. ${q.title}（一次AC=${doneRec.firstPass ? '是' : '否'}，最优=${doneRec.optimal ? '是' : '否'}）`);
  planReport(progress).forEach(l => console.log(l));
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
} else if (cmd === 'plan') {
  planReport(progress).forEach(l => console.log(l));
} else {
  console.log('用法: next | done --seq N [--firstPass true] [--optimal true] [--notes "..."] [--verdict ... --testcases ... --memory ... --approach ... --time ... --space ...] | habit add|list | pattern add|list | analysis --slug <slug> | checkin --seq N | plan | hint --category <分类> | stats');
}
