// 编译并运行某个 LC Java 文件，输出测试结果
// 用法: node run_tests.js [文件路径]   或   node run_tests.js --file <路径>
const { spawnSync } = require('child_process');
const fs = require('fs');
const os = require('os');
const path = require('path');

const args = process.argv.slice(2);
let file = args.includes('--file') ? args[args.indexOf('--file') + 1] : (args[0] && !args[0].startsWith('--') ? args[0] : null);
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
if (jr.stdout) console.log(jr.stdout.replace(/\s+$/, ''));
if (jr.stderr) console.log('运行输出(stderr)：\n' + jr.stderr.trim());
const ok = (jr.stdout || '').includes('全部测试通过');
console.log(ok ? 'RESULT: PASS' : 'RESULT: FAIL');
process.exit(ok ? 0 : 1);
