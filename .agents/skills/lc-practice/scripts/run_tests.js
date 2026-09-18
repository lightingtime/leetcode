// 编译并运行某个 LC Java 文件，输出测试结果
// 用法: node run_tests.js [文件路径]   或   node run_tests.js --file <路径> [--full]
// 输出约定（省 token）：默认紧凑模式——通过时只回「全部测试通过（N 项）」一行；
//                       失败时自动展开失败/异常行（省略「通过 ✓」行，超过 200 行截断）。
//                       --full / --verbose 打印 Java 完整输出（调试时才用）。
const { spawnSync } = require('child_process');
const fs = require('fs');
const os = require('os');
const path = require('path');

const args = process.argv.slice(2);
let file = args.includes('--file') ? args[args.indexOf('--file') + 1] : (args[0] && !args[0].startsWith('--') ? args[0] : null);
const fullOutput = args.includes('--full') || args.includes('--verbose') || args.includes('--all');
if (!file) {
  const src = path.join(__dirname, '..', '..', '..', '..', 'src');
  file = fs.readdirSync(src).filter(f => f.endsWith('.java') && f.startsWith('LC')).sort().reverse()[0] || null;
}
if (!file) { console.error('未找到 Java 文件'); process.exit(1); }
if (!fs.existsSync(file)) { console.error(`文件不存在: ${file}`); process.exit(1); }

const tmp = fs.mkdtempSync(path.join(os.tmpdir(), 'lc-test-'));
const className = path.basename(file, '.java');

// 共享测试工具（生成的题目文件依赖它；不在 LC 前缀内，不会被自动选文件逻辑选中）
const ROOT = path.resolve(__dirname, '..', '..', '..', '..');
const SHARED_HELPER = path.join(ROOT, 'src', 'TestUtil.java');
// 公共节点类（ListNode/TreeNode 标准结构），题目文件依赖它们
const SHARED_NODES = [
  path.join(ROOT, 'src', 'ListNode.java'),
  path.join(ROOT, 'src', 'TreeNode.java')
];
const javacArgs = ['-encoding', 'UTF-8', '-d', tmp];
if (fs.existsSync(SHARED_HELPER) && path.resolve(SHARED_HELPER) !== path.resolve(file)) javacArgs.push(SHARED_HELPER);
for (const shared of SHARED_NODES) {
  if (fs.existsSync(shared) && path.resolve(shared) !== path.resolve(file)) javacArgs.push(shared);
}
javacArgs.push(file);

const jc = spawnSync('javac', javacArgs, { encoding: 'utf8' });
if (jc.status !== 0) {
  console.log('编译失败：');
  console.log((jc.stderr || jc.stdout || '').trim());
  process.exit(2);
}

const jr = spawnSync('java', ['-Dfile.encoding=UTF-8', '-Dstdout.encoding=UTF-8', '-Dstderr.encoding=UTF-8', '-cp', tmp, className], { encoding: 'utf8', timeout: 20000 });
const stdout = (jr.stdout || '').replace(/\s+$/, '');
const ok = stdout.includes('全部测试通过');
const passCount = (stdout.match(/通过 ✓/g) || []).length;
const failCount = (stdout.match(/失败 ✗/g) || []).length;

if (fullOutput) {
  if (stdout) console.log(stdout);
  if (jr.stderr) console.log('运行输出(stderr)：\n' + jr.stderr.trim());
} else if (ok) {
  console.log(`全部测试通过（${passCount} 项）`);
} else {
  // 失败：只保留有信息量的行（失败用例、异常、调试输出），省略「通过 ✓」行
  const lines = stdout.split('\n').filter(l => !/通过 ✓\s*$/.test(l));
  const kept = lines.filter(l => l.trim().length);
  const MAX = 200;
  if (kept.length > MAX) {
    console.log(kept.slice(0, MAX).join('\n'));
    console.log(`……（输出过长，已截断 ${kept.length - MAX} 行；需要完整输出用 --full）`);
  } else {
    console.log(kept.join('\n'));
  }
  if (passCount) console.log(`（另有 ${passCount} 项通过，已省略）`);
  if (jr.stderr) console.log('运行输出(stderr)：\n' + jr.stderr.trim());
  if (!failCount && !/异常/.test(stdout)) console.log('（测试未全部通过，但没有失败明细行；用 --full 查看完整输出）');
}
console.log(ok ? 'RESULT: PASS' : 'RESULT: FAIL');
process.exit(ok ? 0 : 1);
