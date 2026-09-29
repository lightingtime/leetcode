#!/usr/bin/env node
// Accepted 后收尾一把梭：清理占位 → 本地测试 → 记录(done / review done) → 套路 → 归档 → 复盘页 → 打卡(含 build_site) → git commit + push
//
// 用法（仓库根目录）：
//   node ".agents/skills/lc-submit/scripts/finish.js" --seq 92 \
//     --approach "树形 DP 自底向上双值" --time O(n) --space O(n) --optimal true \
//     --notes "一次 AC" --approachDetail "……" [--dpSubtype 树形DP] \
//     [--verdict Accepted] [--testcases 65/65] [--memory 47308000] \
//     [--firstPass true] [--mastery strong --firstTry true] \
//     [--pattern-title "..." --pattern-text "..."] \
//     [--skip-tests] [--no-clean] [--no-commit] [--dry-run] [--overwrite-archive]
//
//   Accepted 之后用户又改过代码（等价精简/补注释）时，只同步代码：
//   node ".agents/skills/lc-submit/scripts/finish.js" --seq 92 --code-only [--approach "原写法名"] [--no-commit]
//
// 说明：一刷/二刷自动判定（progress.json 的 done 里已有该 seq → 二刷，走 review.js done）。
//       提交区代码由本脚本从 src 文件自动提取并写入 analysis.json，调用方不用把代码贴进命令行。
//       正常收尾把仓库内全部非忽略改动一起提交，再推送当前分支 upstream。
const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');

const ROOT = path.resolve(__dirname, '..', '..', '..', '..');
const SKILLS = path.join(ROOT, '.agents', 'skills');
const LC = path.join(ROOT, '.lc');
const args = process.argv.slice(2);
function arg(name, def) { const i = args.indexOf(name); return i >= 0 && args[i + 1] ? args[i + 1] : def; }
function has(name) { return args.indexOf(name) >= 0; }
function readJson(p) { return JSON.parse(fs.readFileSync(p, 'utf8')); }
function writeJson(p, obj) { fs.writeFileSync(p, JSON.stringify(obj, null, 2), 'utf8'); }
function todayStr() {
  const d = new Date();
  return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
}
const dry = has('--dry-run');
const verbose = has('--verbose');
function step(msg, msgDry) { console.log(dry && msgDry ? `[dry-run] ${msgDry}` : msg); }
function fail(msg) { console.error(`✗ ${msg}`); process.exit(1); }

function commitAndPush(message) {
  if (has('--no-commit')) {
    step('已跳过仓库提交与推送（--no-commit）', `将提交并推送仓库内全部改动：${message}（--no-commit）`);
    return;
  }
  if (dry) {
    console.log(`[dry-run] 将提交仓库内全部非忽略改动：${message}`);
    console.log('[dry-run] 将推送当前分支到配置的 upstream');
    return;
  }

  const add = spawnSync('git', ['add', '-A'], { encoding: 'utf8', cwd: ROOT });
  if (add.status !== 0) fail('git add -A 失败：' + (add.stderr || '').trim());
  const staged = spawnSync('git', ['diff', '--cached', '--name-only', '-z'], { encoding: 'utf8', cwd: ROOT });
  if (staged.status !== 0) fail('读取暂存文件失败：' + (staged.stderr || '').trim());
  const stagedCount = staged.stdout ? staged.stdout.split('\0').filter(Boolean).length : 0;
  const hasStaged = spawnSync('git', ['diff', '--cached', '--quiet'], { cwd: ROOT });
  if (hasStaged.status === 1) {
    const c = spawnSync('git', ['commit', '-q', '-m', message], { encoding: 'utf8', cwd: ROOT });
    if (c.status !== 0) fail('git commit 失败：' + (c.stderr || c.stdout || '').trim());
    const sha = spawnSync('git', ['rev-parse', '--short', 'HEAD'], { encoding: 'utf8', cwd: ROOT }).stdout.trim();
    console.log(`  已提交 ${sha}（${stagedCount} 个仓库文件）：${message}`);
  } else if (hasStaged.status === 0) {
    console.log('  （仓库没有待提交改动，跳过提交）');
  } else {
    fail('检查暂存区失败');
  }

  const upstream = spawnSync('git', ['rev-parse', '--abbrev-ref', '--symbolic-full-name', '@{u}'], { encoding: 'utf8', cwd: ROOT });
  if (upstream.status !== 0) {
    console.error('✗ 本地提交已完成，但当前分支没有配置 upstream，无法自动推送；请配置跟踪分支后运行 git push。');
    process.exit(1);
  }
  const push = spawnSync('git', ['push'], { encoding: 'utf8', cwd: ROOT });
  if (push.status !== 0) {
    console.error(`✗ 本地提交已完成，但推送到 ${upstream.stdout.trim()} 失败：`);
    console.error((push.stderr || push.stdout || '').trim());
    process.exit(1);
  }
  console.log(`  已推送到 ${upstream.stdout.trim()}`);
}

function run(argsArr, { label, silent = true } = {}) {
  if (dry) { console.log(`[dry-run] node ${argsArr.join(' ').slice(0, 160)}`); return ''; }
  const r = spawnSync('node', argsArr, { encoding: 'utf8', cwd: ROOT });
  if (r.status !== 0) {
    console.error(`✗ ${label || argsArr[1]} 失败：`);
    console.error((r.stderr || r.stdout || '').trim());
    process.exit(1);
  }
  const out = (r.stdout || '').trim();
  if (!silent && out) console.log(out);
  else if (out) out.split('\n').slice(-2).forEach(l => console.log('  ' + l));
  return out;
}

const seq = parseInt(arg('--seq', '0'), 10);
if (!seq) fail('请传 --seq <seq>（题目在 order.json 中的顺序号）');
const order = readJson(path.join(LC, 'order.json'));
const progress = readJson(path.join(LC, 'progress.json'));
const q = order.find(o => o.seq === seq);
if (!q) fail(`order.json 里找不到 seq=${seq}`);
const id4 = String(q.id).padStart(4, '0');
const isReview = (progress.done || []).some(d => d.seq === seq);

const approach = arg('--approach', '') || fail('请传 --approach <解法名>');
const timeC = arg('--time', '');
const spaceC = arg('--space', '');
const optimal = arg('--optimal', 'false') === 'true';
const verdict = arg('--verdict', 'Accepted');
const testcases = arg('--testcases', '');
const memory = arg('--memory', '');
const notes = arg('--notes', '');
const approachDetail = arg('--approachDetail', '');
const dpSubtype = arg('--dpSubtype', '');
const mastery = arg('--mastery', '');
const firstTry = arg('--firstTry', 'true') === 'true';

// 1) 定位源码：优先 src 根目录（进行中），其次分类目录（已归档）
const srcRoot = path.join(ROOT, 'src');
const catDir = path.join(srcRoot, q.category.replace(/[/\\]+/g, '-'));
const rootCand = fs.readdirSync(srcRoot).filter(f => f.endsWith('.java') && new RegExp(`^LC0*${q.id}_`, 'i').test(f));
let srcFile = rootCand.length ? path.join(srcRoot, rootCand[0]) : null;
let alreadyArchived = false;
if (!srcFile) {
  const archCand = fs.existsSync(catDir) ? fs.readdirSync(catDir).filter(f => f.endsWith('.java') && new RegExp(`^LC0*${q.id}_`, 'i').test(f)) : [];
  if (!archCand.length) fail(`src/ 与 src/${q.category}/ 下都没找到 LC${id4}_*.java`);
  srcFile = path.join(catDir, archCand[0]);
  alreadyArchived = true;
}
const fileBase = path.basename(srcFile, '.java');
const archivedFile = path.join(catDir, `${fileBase}.java`);
const reviewHtml = path.join(ROOT, 'reviews', q.category.replace(/[/\\]+/g, '-'), `${fileBase}_Review.html`);
console.log(`题目：${q.id}. ${q.title}（${q.category}）｜ ${isReview ? '二刷' : '一刷'} ｜ 源码 ${path.relative(ROOT, srcFile) || srcFile}`);

// 2) 取提交区代码（marker 之间）
function submitCodeOf(file) {
  const text = fs.readFileSync(file, 'utf8');
  const m = text.match(/\/\/\s*====\s*提交代码开始\s*====([\s\S]*?)\/\/\s*====\s*提交代码结束\s*====/);
  return m ? m[1].trim() : '';
}
const code = submitCodeOf(srcFile);
if (!code) fail('没能从文件里提取到提交区代码（检查 marker 是否完整）');

// 2.5) 预检：归档冲突（归档已有旧版）必须在任何写入之前确定处理方式
const overwriteArchive = has('--overwrite-archive');
const archiveConflict = !alreadyArchived && fs.existsSync(archivedFile) && path.resolve(archivedFile) !== path.resolve(srcFile);
if (archiveConflict && !overwriteArchive) {
  fail(`归档位置已有旧版本：${path.relative(ROOT, archivedFile)}\n` +
    '  一刷收尾默认不覆盖既有归档。确认要用当前文件覆盖（旧版仍保留在 git 历史与 analysis.json 的 submissions 里）再加 --overwrite-archive。');
}

// 2.6) --code-only：Accepted 之后用户又改过代码时，同步代码 + 重生成复盘页 + 提交并推送仓库改动
//      （不重写完成日期、不追加复习记录；旧代码存进该写法的 prev_code）
if (has('--code-only')) {
  const ap = path.join(LC, 'problems', `${q.id}_${q.slug}`, 'analysis.json');
  if (!fs.existsSync(ap)) fail(`没有 analysis.json，无法只同步代码：${path.relative(ROOT, ap)}`);
  const a = readJson(ap);
  a.submissions = Array.isArray(a.submissions) ? a.submissions : [];
  const idx = approach ? a.submissions.findIndex(s => (s.approach || '') === approach) : a.submissions.length - 1;
  if (dry) {
    console.log(`[dry-run] 将把当前提交区代码（${code.length} 字符）写入 submissions[${idx}]${approach ? '（' + approach + '）' : '（最新写法）'}`);
  } else {
    if (idx >= 0) {
      const s = a.submissions[idx];
      if (s.code && s.code !== code && !s.prev_code) s.prev_code = s.code;
      s.code = code;
    } else {
      a.submissions.push({ date: todayStr(), verdict: verdict, approach: approach || fileBase, optimal, code });
    }
    writeJson(ap, a);
  }
  step(`已同步代码：${path.relative(ROOT, ap)}`, `将同步代码到 ${path.relative(ROOT, ap)}`);
  run([path.join(SKILLS, 'lc-submit/scripts/generate_review.js'), '--slug', q.slug], { label: '生成复盘页' });
  run([path.join(SKILLS, 'lc-practice/scripts/build_site.js')], { label: '重建主页' });
  const msg = `chore(LC${id4}): 同步最新代码到复盘（${approach || fileBase}）`;
  commitAndPush(msg);
  console.log('');
  console.log(`✅ 代码同步完成：${q.id}. ${q.title}（${path.relative(ROOT, ap)}，复盘页已重生成）`);
  process.exit(0);
}

// 3) 清理占位注释（只删注释行，不动逻辑）
if (!has('--no-clean')) {
  const placeholders = [
    /^\s*\/\/\s*TODO:\s*在这里实现你的解法\s*$/,
    /^\s*\/\/\s*TODO:\s*补全方法体\s*$/,
    /^\s*\/\/\s*思路：TODO.*$/,
    /^\s*\/\/\s*复杂度：TODO.*$/
  ];
  const lines = fs.readFileSync(srcFile, 'utf8').split('\n');
  const kept = lines.filter(l => !placeholders.some(re => re.test(l)));
  const removed = lines.length - kept.length;
  if (removed) {
    if (!dry) fs.writeFileSync(srcFile, kept.join('\n'), 'utf8');
    step(`清理占位注释 ${removed} 行`, `将清理占位注释 ${removed} 行（${path.relative(ROOT, srcFile)}）`);
  } else {
    step('占位注释：无残留');
  }
  if (kept.some(l => /TODO:\s*补充空输入/.test(l))) {
    console.log('⚠ 测试区仍有 `TODO: 补充空输入 / 单元素…`：本次会照常收尾，但下次建题要补齐边界用例');
  }
}

// 4) 本地测试（必须通过）
if (!has('--skip-tests')) {
  const out = run([path.join(SKILLS, 'lc-practice/scripts/run_tests.js'), '--file', srcFile], { label: '本地测试', silent: false });
  if (!dry && !/RESULT: PASS/.test(out)) fail('本地测试未通过，停止收尾（用 run_tests.js --full 看全量输出）');
} else {
  step('本地测试：已跳过（--skip-tests）');
}

// 5) 记录进度（一刷 done / 二刷 review done）
if (!isReview) {
  const doneArgs = [path.join(SKILLS, 'lc-practice/scripts/update_state.js'), 'done', '--seq', String(seq),
    '--firstPass', arg('--firstPass', 'true'), '--optimal', String(optimal),
    '--notes', notes, '--verdict', verdict, '--testcases', testcases,
    '--approach', approach, '--time', timeC, '--space', spaceC, '--code', code];
  if (memory) doneArgs.push('--memory', memory);
  if (approachDetail) doneArgs.push('--approachDetail', approachDetail);
  if (dpSubtype) doneArgs.push('--dpSubtype', dpSubtype);
  run(doneArgs, { label: '记录完成' });
  step(`已记录一刷完成：${q.title}`);
} else {
  if (mastery !== 'strong' && mastery !== 'weak') fail('二刷收尾必须传 --mastery strong|weak');
  const revArgs = [path.join(SKILLS, 'lc-review/scripts/review.js'), 'done', '--seq', String(seq),
    '--mastery', mastery, '--firstTry', String(firstTry), '--approach', approach,
    '--time', timeC, '--space', spaceC, '--optimal', String(optimal), '--notes', notes, '--code', code];
  if (memory) revArgs.push('--memory', memory);
  if (testcases) revArgs.push('--testcases', testcases);
  run(revArgs, { label: '记录二刷' });
  const st = (readJson(path.join(LC, 'review_state.json')).problems || {})[q.slug] || {};
  console.log(`  下次复习 ${st.next_review_date || '—'}｜间隔 ${st.interval_days ?? '—'} 天｜已掌握=${st.mastered ? '是' : '否'}`);
}

// 6) 套路沉淀（可选）
const patTitle = arg('--pattern-title', '');
if (patTitle) {
  const patText = arg('--pattern-text', '') || fail('给了 --pattern-title 就要给 --pattern-text');
  const patArgs = [path.join(SKILLS, 'lc-analyze/scripts/patterns.js'), 'add', '--title', patTitle, '--text', patText, '--source', `LC${id4}`];
  const patCat = arg('--pattern-category', '');
  if (patCat) patArgs.push('--category', patCat);
  run(patArgs, { label: '套路入库' });
  run([path.join(SKILLS, 'lc-practice/scripts/update_state.js'), 'pattern', 'add', '--slug', q.slug, '--title', patTitle, '--text', patText], { label: '套路写题' });
}

// 7) 归档（一刷 git mv；二刷覆盖归档 + 删根目录副本与 .bak）
const touched = [];
if (!alreadyArchived) {
  if (!fs.existsSync(catDir)) { if (!dry) fs.mkdirSync(catDir, { recursive: true }); }
  if (!dry) {
    if (isReview || archiveConflict) {
      fs.writeFileSync(archivedFile, fs.readFileSync(srcFile, 'utf8'));
      fs.unlinkSync(srcFile);
      const bak = archivedFile + '.bak';
      if (fs.existsSync(bak)) fs.unlinkSync(bak);
    } else {
      const r = spawnSync('git', ['mv', path.relative(ROOT, srcFile), path.relative(ROOT, archivedFile)], { encoding: 'utf8', cwd: ROOT });
      if (r.status !== 0) {
        fs.renameSync(srcFile, archivedFile);
        console.log('  （文件未被 git 跟踪，已用普通移动归档）');
      }
    }
  }
  step(`已归档：${path.relative(ROOT, archivedFile)}`, `将归档到 ${path.relative(ROOT, archivedFile)}`);
  touched.push(archivedFile, srcFile, archivedFile + '.bak');
} else {
  step(`源码已在分类目录：${path.relative(ROOT, srcFile)}`);
  touched.push(srcFile);
}

// 8) 复盘页 + 打卡（checkin 内部会重新生成 reviews/index.html）
run([path.join(SKILLS, 'lc-submit/scripts/generate_review.js'), '--slug', q.slug], { label: '生成复盘页' });
run([path.join(SKILLS, 'lc-practice/scripts/update_state.js'), 'checkin', '--seq', String(seq)], { label: '打卡' });
touched.push(reviewHtml, path.join(ROOT, 'reviews', 'index.html'));

// 9) 提交并推送仓库内全部非忽略改动
touched.push(
  path.join(LC, 'problems', `${q.id}_${q.slug}`, 'analysis.json'),
  path.join(LC, isReview ? 'review_state.json' : 'progress.json')
);
let message;
if (isReview) {
  const st = (readJson(path.join(LC, 'review_state.json')).problems || {})[q.slug] || {};
  const pass = ((st.review_count || 1));
  message = `review(LC${id4}): 二刷第 ${pass} 次 Accepted（${mastery === 'strong' ? '较强' : '较弱'}，${approach}，下次 ${st.next_review_date || '—'}）`;
} else {
  message = `完成力扣${q.id} ${q.title}（${approach}）并打卡复盘`;
}
commitAndPush(message);

// 10) 收尾摘要
console.log('');
console.log(`✅ ${isReview ? '二刷' : '一刷'}收尾完成：${q.id}. ${q.title}`);
console.log(`- 判题：${verdict}${testcases ? ' ' + testcases : ''}｜${approach}｜${timeC || '—'}/${spaceC || '—'}${optimal ? '｜已达最优' : ''}`);
console.log(`- 源码：${path.relative(ROOT, archivedFile)}`);
console.log(`- 复盘页：${path.relative(ROOT, reviewHtml)}`);
console.log('- 主页：reviews/index.html');
if (verbose) console.log(`- 变更文件：${touched.filter(Boolean).map(p => path.relative(ROOT, p)).join('、')}`);
