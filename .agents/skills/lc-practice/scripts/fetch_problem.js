// 从力扣官方接口拉取题目详情（问题描述、示例、Java 代码模板），保存到 .lc/problems/{id}_{slug}/
// 用法: node fetch_problem.js --slug <slug>   或   node fetch_problem.js --id <题号>
const fs = require('fs');
const path = require('path');

const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');
const GRAPHQL = 'https://leetcode.cn/graphql/';

const CATS = [
  { name: '动态规划', keys: ['Dynamic Programming', 'Memoization'] },
  { name: '回溯', keys: ['Backtracking'] },
  { name: '双指针与滑动窗口', keys: ['Two Pointers', 'Sliding Window'] },
  { name: '链表', keys: ['Linked List'] },
  { name: '栈、队列与优先队列', keys: ['Stack', 'Queue', 'Monotonic Stack', 'Monotonic Queue', 'Priority Queue', 'Heap (Priority Queue)'] },
  { name: '树与二叉树', keys: ['Tree', 'Binary Tree', 'Binary Search Tree', 'Binary Indexed Tree', 'Segment Tree', 'N-ary Tree'] },
  { name: '图与并查集', keys: ['Graph', 'Topological Sort', 'Union Find'] },
  { name: 'DFS / BFS', keys: ['Depth-First Search', 'Breadth-First Search'] },
  { name: '贪心与区间', keys: ['Greedy', 'Interval'] },
  { name: '哈希表', keys: ['Hash Table'] },
  { name: '字符串', keys: ['String', 'String Matching'] },
  { name: '数学与位运算', keys: ['Math', 'Bit Manipulation', 'Number Theory', 'Combinatorics', 'Geometry', 'Randomized'] },
  { name: '数组与矩阵', keys: ['Array', 'Matrix'] },
  { name: '其他', keys: ['Design', 'Trie', 'Recursion', 'Divide and Conquer', 'Shortest Path', 'Game Theory', 'Enumeration', 'Simulation', 'Bucket Sort', 'Radix Sort', 'Counting', 'Counting Sort', 'Ordered Set', 'Data Stream', 'Iterator', 'Rolling Hash', 'Suffix Array', 'Strongly Connected Component', 'Minimum Spanning Tree', 'Reservoir Sampling'] }
];

function getCategory(tags) {
  for (const c of CATS) for (const t of tags) if (c.keys.includes(t)) return c.name;
  return '其他';
}

function decodeEntities(s) {
  return s.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&')
    .replace(/&quot;/g, '"').replace(/&#39;/g, "'").replace(/&nbsp;/g, ' ');
}

function stripTags(s) { return decodeEntities(s).replace(/<[^>]+>/g, ''); }

function htmlToText(s) {
  return stripTags(s.replace(/<br\s*\/?>/gi, '\n').replace(/<\/p>/gi, '\n\n'))
    .replace(/\r/g, '').replace(/[ \t]+/g, ' ').replace(/\n\s*\n\s*\n+/g, '\n\n').trim();
}

function splitTopLevel(s) {
  const parts = [];
  let depth = 0, cur = '', inQ = false;
  for (const ch of s) {
    if (ch === '"') { inQ = !inQ; cur += ch; continue; }
    if (!inQ && (ch === '[' || ch === '{' || ch === '<')) depth++;
    if (!inQ && (ch === ']' || ch === '}' || ch === '>')) depth--;
    if (ch === ',' && depth === 0 && !inQ) { parts.push(cur.trim()); cur = ''; }
    else cur += ch;
  }
  if (cur.trim()) parts.push(cur.trim());
  return parts;
}

function parseExamples(content) {
  const examples = [];
  const blocks = [...content.matchAll(/<pre>([\s\S]*?)<\/pre>/gi)];
  for (const m of blocks) {
    const lines = stripTags(m[1]).replace(/\r/g, '').split('\n').map(l => l.trim()).filter(Boolean);
    let inputLine = null, outputLine = null, explain = '';
    for (const line of lines) {
      if (/^输入[:：]/.test(line)) inputLine = line;
      else if (/^输出[:：]/.test(line)) outputLine = line;
      else if (/^解释[:：]/.test(line)) explain = line.replace(/^解释[:：]\s*/, '').trim();
    }
    if (!inputLine) continue;
    const inputs = [];
    for (const kv of splitTopLevel(inputLine.replace(/^输入[:：]\s*/, ''))) {
      const eq = kv.indexOf('=');
      if (eq < 0) continue;
      inputs.push({ name: kv.slice(0, eq).trim(), value: kv.slice(eq + 1).trim() });
    }
    examples.push({
      inputs,
      output: outputLine ? outputLine.replace(/^输出[:：]\s*/, '').trim() : null,
      explain
    });
  }
  return examples;
}

const args = process.argv.slice(2);
function arg(name) { const i = args.indexOf(name); return i >= 0 ? args[i + 1] : null; }

async function main() {
  let slug = arg('--slug');
  if (!slug && arg('--id')) {
    const id = arg('--id');
    const order = JSON.parse(fs.readFileSync(path.join(LC_DIR, 'order.json'), 'utf8'));
    const q = order.find(o => o.id === id);
    if (!q) { console.error(`order.json 中找不到题号 ${id}`); process.exitCode = 1; return; }
    slug = q.slug;
  }
  if (!slug) { console.error('用法: --slug <slug> 或 --id <题号>'); process.exitCode = 1; return; }

  const query = `query questionData($titleSlug: String!) {
    question(titleSlug: $titleSlug) {
      questionId questionFrontendId title translatedTitle titleSlug difficulty
      translatedContent content
      topicTags { name translatedName }
      exampleTestcases
      codeSnippets { lang langSlug code }
    }
  }`;
  const resp = await fetch(GRAPHQL, {
    method: 'POST',
    headers: { 'content-type': 'application/json', 'origin': 'https://leetcode.cn', 'referer': 'https://leetcode.cn/' },
    body: JSON.stringify({ query, variables: { titleSlug: slug }, operationName: 'questionData' })
  });
  const json = await resp.json();
  const data = json.data && json.data.question;
  if (!data) {
    console.error('拉取失败：' + JSON.stringify(json.errors || json).slice(0, 500));
    process.exitCode = 1;
    return;
  }

  const id = data.questionFrontendId;
  const titleCn = data.translatedTitle || data.title;
  const tags = (data.topicTags || []).map(t => t.name);
  const tagsCn = (data.topicTags || []).map(t => t.translatedName || t.name);
  const snippet = (data.codeSnippets || []).find(c => c.lang === 'Java');
  const content = data.translatedContent || data.content || '';
  const problem = {
    id,
    slug,
    title: data.title,
    titleCn,
    difficulty: data.difficulty,
    url: `https://leetcode.cn/problems/${slug}/`,
    tags,
    tagsCn,
    category: getCategory(tags),
    examples: parseExamples(content),
    exampleTestcases: data.exampleTestcases || '',
    javaSnippet: snippet ? snippet.code : '',
    fetchedAt: new Date().toISOString()
  };

  const dir = path.join(LC_DIR, 'problems', `${id}_${slug}`);
  fs.mkdirSync(dir, { recursive: true });
  fs.writeFileSync(path.join(dir, 'problem.json'), JSON.stringify(problem, null, 2), 'utf8');

  let md = `# ${id}. ${titleCn} (${data.title})\n\n`;
  md += `> ${problem.url}\n\n`;
  md += `难度：${data.difficulty} ｜ 分类：${problem.category}\n\n`;
  md += `标签：${tagsCn.join('、')}\n\n`;
  md += `## 题目描述\n\n${htmlToText(content)}\n\n`;
  md += `## 示例\n\n`;
  problem.examples.forEach((e, i) => {
    md += `${i + 1}. 输入：${e.inputs.map(x => `${x.name} = ${x.value}`).join('，')}\n`;
    md += `   输出：${e.output}\n`;
    if (e.explain) md += `   解释：${e.explain}\n`;
    md += `\n`;
  });
  md += `## 约定\n\n- 先独立思考，别急着看题解。\n- 测试失败后可以让 lc-analyze 帮你分析思路。\n`;
  fs.writeFileSync(path.join(dir, 'problem.md'), md, 'utf8');

  console.log(`已拉取：${id}. ${titleCn}（${data.difficulty}）分类=${problem.category} 示例=${problem.examples.length}`);
  console.log(`题目数据：${path.join(dir, 'problem.json')}`);
  console.log(`题目描述：${path.join(dir, 'problem.md')}`);
}

main().catch(e => { console.error('错误：' + e.message); process.exitCode = 1; });
