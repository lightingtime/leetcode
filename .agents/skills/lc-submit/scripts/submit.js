// 把本地通过测试的 Java 解法提交到力扣中国站
// 用法: node submit.js --file <LC*.java路径> --slug <titleSlug>
// 需要先在 .lc/config.json 配置 leetcode_session 与 csrf_token（浏览器登录力扣后从 cookie 中复制）
const fs = require('fs');
const path = require('path');

const LC_DIR = 'D:/Projects/leetCode/.lc';
const GRAPHQL = 'https://leetcode.cn/graphql/';

const args = process.argv.slice(2);
function arg(name) { const i = args.indexOf(name); return i >= 0 ? args[i + 1] : null; }
const sleep = ms => new Promise(r => setTimeout(r, ms));

function readConfig() {
  try {
    return JSON.parse(fs.readFileSync(path.join(LC_DIR, 'config.json'), 'utf8'));
  } catch { return {}; }
}

function helperClasses(fragment, provided) {
  const parts = [];
  if (/\bListNode\b/.test(fragment) && !(provided && provided.ListNode)) {
    parts.push(`class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}`);
  }
  if (/\bTreeNode\b/.test(fragment) && !(provided && provided.TreeNode)) {
    parts.push(`class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) { this.val = val; this.left = left; this.right = right; }
}`);
  }
  return parts.join('\n\n');
}

async function gql(query, variables) {
  const resp = await fetch(GRAPHQL, {
    method: 'POST',
    headers: { 'content-type': 'application/json', 'origin': 'https://leetcode.cn', 'referer': 'https://leetcode.cn/' },
    body: JSON.stringify({ query, variables, operationName: 'questionData' })
  });
  return resp.json();
}

function printVerdict(ck) {
  console.log(`判题结果：${ck.status_msg || '未知'}（code=${ck.status_code}）`);
  if (ck.runtime != null) console.log(`运行时间：${ck.runtime}`);
  if (ck.memory != null) console.log(`内存占用：${ck.memory}`);
  if (ck.total_testcases != null) console.log(`通过用例：${ck.total_correct}/${ck.total_testcases}`);
  if (ck.status_code === 10) return; // Accepted
  // 非 Accepted（WA/MLE/OLE/TLE/RE/CE）：一律尽量打印失败用例与期望输出，供补充到本地测试
  if (ck.last_testcase != null) {
    console.log('失败用例输入：');
    console.log((String(ck.last_testcase) || '').trim());
  }
  if (ck.expected_output != null) console.log(`期望输出：${ck.expected_output}`);
  if (ck.full_runtime_error) { console.log('运行时错误：'); console.log(String(ck.full_runtime_error).trim()); }
  if (ck.full_compile_error) { console.log('编译错误：'); console.log(String(ck.full_compile_error).trim()); }
}

async function main() {
  const file = arg('--file');
  const slug = arg('--slug');
  if (!file || !slug) { console.error('用法: --file <路径> --slug <titleSlug>'); process.exitCode = 1; return; }
  if (!fs.existsSync(file)) { console.error(`文件不存在: ${file}`); process.exitCode = 1; return; }

  const config = readConfig();
  const session = config.leetcode_session || '';
  const csrf = config.csrf_token || '';
  if (!session) {
    console.log('未配置力扣 cookie，无法自动提交。');
    console.log('方式 A：在浏览器登录 leetcode.cn，打开 F12 → Application → Cookies，把 LEETCODE_SESSION 和 csrftoken 的值填入 .lc/config.json 的 leetcode_session / csrf_token 字段。');
    console.log('方式 B：手动在 leetcode.cn 提交代码，把判题结果贴回来，我继续帮你复盘。');
    process.exitCode = 2;
    return;
  }

  const code = fs.readFileSync(file, 'utf8');
  const marker = code.match(/\/\/ ==== 提交代码开始 ====\s*([\s\S]*?)\/\/ ==== 提交代码结束 ====/);
  let fragment = marker ? marker[1].trim() : code.trim();
  // 去掉提交区开头的纯注释行（模板提示语），避免影响设计题类解包判断
  fragment = fragment.replace(/^(?:[ \t]*\/\/[^\n]*\n?)+/, '');

  // 读取题目数据：判断判题环境是否已自带 ListNode/TreeNode，避免重复附带辅助类
  const probDirs = fs.readdirSync(path.join(LC_DIR, 'problems'));
  const dir = probDirs.find(d => d.endsWith('_' + slug));
  let snippet = '';
  if (dir) {
    try {
      const prob = JSON.parse(fs.readFileSync(path.join(LC_DIR, 'problems', dir, 'problem.json'), 'utf8'));
      snippet = prob.javaSnippet || '';
    } catch {}
  }
  const providedHelpers = {
    ListNode: /\bclass\s+ListNode\b/.test(snippet),
    TreeNode: /\bclass\s+TreeNode\b/.test(snippet)
  };

  let submission;
  const staticClass = fragment.match(/^static\s+class\s+(\w+)\s*\{([\s\S]*)\}$/);
  if (staticClass) {
    // 设计题：解包嵌套类，恢复为顶层类
    submission = `class ${staticClass[1]} {\n${staticClass[2].trim()}\n}\n${helperClasses(fragment, providedHelpers)}`;
  } else if (/^\s*(public\s+)?class\s+\w+/.test(fragment)) {
    submission = fragment;
  } else {
    // 保留 snippet 里的类声明（含 public 修饰），避免判题按类名/修饰符映射文件失败
    const snippetClass = snippet.match(/^\s*(public\s+)?class\s+(\w+)/);
    const clsName = snippetClass ? snippetClass[2] : 'Solution';
    const clsModifier = snippetClass && snippetClass[1] ? snippetClass[1] : '';
    submission = `${clsModifier}class ${clsName} {\n${fragment}\n}\n${helperClasses(fragment, providedHelpers)}`;
  }

  // 获取 question_id（数据库 ID）
  const q = await gql(`query questionData($titleSlug: String!) { question(titleSlug: $titleSlug) { questionId } }`, { titleSlug: slug });
  const qid = q.data && q.data.question && q.data.question.questionId;
  if (!qid) { console.error('获取 questionId 失败'); process.exitCode = 3; return; }

  const headers = {
    'content-type': 'application/json',
    'x-csrf-token': csrf,
    'origin': 'https://leetcode.cn',
    'referer': `https://leetcode.cn/problems/${slug}/`,
    'cookie': `csrftoken=${csrf}; LEETCODE_SESSION=${session}`
  };
  const sub = await fetch(`https://leetcode.cn/problems/${slug}/submit/`, {
    method: 'POST',
    headers,
    body: JSON.stringify({ lang: 'java', question_id: qid, typed_code: submission, judge_type: 'large' })
  });
  const subJson = await sub.json();
  if (!subJson.submission_id) {
    console.log('提交失败：' + JSON.stringify(subJson).slice(0, 500));
    console.log('常见原因：cookie 过期 / csrf 无效 / 未登录。请重新复制 cookie 后重试。');
    process.exitCode = 3;
    return;
  }
  console.log('已提交，等待判题……');

  for (let i = 0; i < 60; i++) {
    await sleep(1000);
    const ck = await fetch(`https://leetcode.cn/submissions/detail/${subJson.submission_id}/check/`, {
      headers: { 'referer': `https://leetcode.cn/problems/${slug}/`, 'cookie': headers.cookie }
    });
    const ckj = await ck.json();
    if (ckj.state === 'SUCCESS') {
      printVerdict(ckj);
      process.exitCode = ckj.status_msg === 'Accepted' ? 0 : 1;
      return;
    }
    if (ckj.state === 'FAILED') {
      console.log('判题异常：' + JSON.stringify(ckj).slice(0, 500));
      process.exitCode = 3;
      return;
    }
  }
  console.log('判题超时（60 秒），请稍后到力扣「提交记录」查看结果并告诉我。');
  process.exitCode = 3;
}

main().catch(e => { console.error('错误：' + e.message); process.exitCode = 1; });
