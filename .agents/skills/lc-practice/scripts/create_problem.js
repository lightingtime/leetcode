// 根据 problem.json 生成 IDEA 可运行的 Java 刷题文件（方法签名 + 示例测试 main + 辅助类）
// 用法: node create_problem.js --slug <slug>   或   --id <题号>   或   --json <problem.json路径>
// 可选: --out <目录> 默认 D:/Projects/leetCode/src；--force 允许覆盖与模板不一致的已有文件
const fs = require('fs');
const path = require('path');

const LC_DIR = 'D:/Projects/leetCode/.lc';
const DEFAULT_OUT = 'D:/Projects/leetCode/src';
// 这些题目的返回答案顺序无关紧要（如下标对、无序集合），示例测试用 checkEqUnordered
const UNORDERED_SLUGS = new Set(['two-sum']);

const args = process.argv.slice(2);
function arg(name) { const i = args.indexOf(name); return i >= 0 ? args[i + 1] : null; }
function todayStr() {
  const d = new Date();
  return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0');
}

function resolveProblem() {
  const jp = arg('--json');
  if (jp) return jp;
  const problemsDir = path.join(LC_DIR, 'problems');
  const dirs = fs.readdirSync(problemsDir);
  let slug = arg('--slug'), id = arg('--id');
  if (!slug && !id) { console.error('用法: --slug <slug> | --id <题号> | --json <路径>'); process.exit(1); }
  const hit = dirs.find(d => (slug && d.endsWith('_' + slug)) || (id && d.startsWith(id + '_')));
  if (!hit) { console.error('未找到已拉取的题目，请先运行 fetch_problem.js'); process.exit(1); }
  return path.join(problemsDir, hit, 'problem.json');
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

function toPascal(slug) {
  return slug.split('-').filter(Boolean).map(w => w[0].toUpperCase() + w.slice(1)).join('');
}

function extractSnippetInfo(snippet) {
  const cls = (snippet.match(/class\s+(\w+)/) || [])[1] || 'Solution';
  const body = (snippet.match(/class\s+\w+\s*\{([\s\S]*)\}/) || [])[1] || '';
  const methods = [...body.matchAll(/public\s+(?:static\s+)?[\w<>,.\[\]\s]+\s+\w+\s*\(/g)];
  const ctors = [...body.matchAll(/public\s+\w+\s*\(/g)].filter(m => !methods.includes(m));
  const total = methods.length + ctors.length;
  return { cls, body, methods, ctors, total };
}

function extractMethod(body) {
  const mm = body.match(/public\s+(?:static\s+)?([\w<>,.\[\]\s]+?)\s+([A-Za-z_]\w*)\s*\(([\s\S]*?)\)\s*\{/);
  if (!mm) return null;
  const params = [];
  const ps = splitTopLevel(mm[3]);
  for (const p of ps) {
    const t = p.trim().split(/\s+/);
    if (t.length >= 2) params.push({ type: t.slice(0, -1).join(' '), name: t[t.length - 1] });
  }
  return { ret: mm[1].trim(), name: mm[2], params };
}

function javaLiteral(value, type) {
  const v = value.trim();
  if (v === 'null' || v === '[]') {
    if (v === '[]') {
      if (type === 'ListNode') return 'listNode()';
      if (type === 'TreeNode') return 'treeNode()';
      if (type.startsWith('List')) return 'Arrays.asList()';
      if (type.endsWith('[]')) return 'new ' + type + '{}';
    }
    return 'null';
  }
  const base = type.replace(/<.*>/, '');
  if (base === 'List') {
    const inner = (type.match(/<(.+)>/) || [])[1] || 'Object';
    return 'Arrays.asList(' + splitTopLevel(v.slice(1, -1)).map(e => javaLiteral(e, inner)).join(', ') + ')';
  }
  if (base === 'ListNode') return 'listNode(' + splitTopLevel(v.slice(1, -1)).join(', ') + ')';
  if (base === 'TreeNode') return 'treeNode(' + splitTopLevel(v.slice(1, -1)).join(', ') + ')';
  if (type.endsWith('[]')) {
    const elemType = type.slice(0, -2);
    const elems = splitTopLevel(v.slice(1, -1)).map(e => javaLiteral(e, elemType));
    return 'new ' + type + '{' + elems.join(', ') + '}';
  }
  if (type === 'String') return v;
  if (type === 'char') return v.length === 3 && v[0] === '"' ? "'" + v[1] + "'" : v;
  if (type === 'long') return /L$/i.test(v) ? v : v + 'L';
  if (type === 'double') return /[.eE]/.test(v) ? v : v + '.0';
  return v;
}

function defaultReturn(type) {
  if (type === 'void') return '';
  if (type === 'int') return 'return 0;';
  if (type === 'long') return 'return 0L;';
  if (type === 'double') return 'return 0.0;';
  if (type === 'boolean') return 'return false;';
  if (type === 'char') return "return '\\0';";
  return 'return null;';
}

function designDefaultReturn(sig) {
  const m = sig.match(/^public\s+([\w<>,.\[\]\s]+?)\s+\w+\s*\(/);
  if (!m) return ''; // 构造器：不需要返回语句
  return defaultReturn(m[1].trim());
}

function buildTemplate(problem, method, snippetClsName) {
  const className = 'LC' + String(problem.id).padStart(4, '0') + '_' + toPascal(problem.slug);
  const diffCn = { EASY: '简单', MEDIUM: '中等', HARD: '困难' }[problem.difficulty] || problem.difficulty;
  const date = todayStr();
  const needListNode = (method ? method.params.some(p => p.type.includes('ListNode')) || (method.ret || '').includes('ListNode') : false);
  const needTreeNode = (method ? method.params.some(p => p.type.includes('TreeNode')) || (method.ret || '').includes('TreeNode') : false);
  const snippet = problem.javaSnippet || '';
  // 标准结构（val+next / val+left+right）用公共节点类 src/ListNode.java、src/TreeNode.java；结构不同才在题文件内生成本地类
  const standardListNode = /\bListNode\s+next\b/.test(snippet);
  const standardTreeNode = /\bTreeNode\s+left\b/.test(snippet) && /\bTreeNode\s+right\b/.test(snippet);
  // 节点类的格式化依赖本题的 ListNode/TreeNode，需要本地包装；其余题目直接用共享的 TestUtil
  const useLocalHelpers = needListNode || needTreeNode;
  const checkEqCall = useLocalHelpers ? 'checkEq' : 'TestUtil.checkEq';
  const checkEqUnorderedCall = useLocalHelpers ? 'checkEqUnordered' : 'TestUtil.checkEqUnordered';
  const paramByName = {};
  if (method) method.params.forEach(p => { paramByName[p.name] = p.type; });

  let L = [];
  L.push('// ============================================================');
  L.push(`// LeetCode ${problem.id}. ${problem.titleCn} (${problem.title})`);
  L.push(`// 难度：${diffCn} | 分类：${problem.category}`);
  L.push(`// 链接：${problem.url}`);
  L.push(`// 刷题日期：${date}`);
  L.push('//');
  L.push('// 思路：TODO 写下你的思路（先在纸面想清楚再写代码）');
  L.push('// 复杂度：TODO 时间 O(?) 空间 O(?)');
  L.push('// ============================================================');
  L.push('');
  L.push('import java.util.*;');
  L.push('');
  L.push(`public class ${className} {`);
  L.push('');
  if (!method) {
    const innerCls = snippetClsName || 'Solution';
    L.push(`    // 设计题：补全下面的成员（字段 / 构造器 / 方法体），类名 ${innerCls} 在提交时自动处理。`);
  }
  L.push('    // ==== 提交代码开始 ====');

  if (!method) {
    // 设计题：预填所有方法签名，用户补全方法体
    const body = (problem.javaSnippet.match(/class\s+\w+\s*\{([\s\S]*)\}/) || [])[1] || '';
    const sigs = [...body.matchAll(/public\s+(?:[\w<>,.\[\]\s]+\s+)?\w+\s*\([^)]*\)\s*\{/g)];
    const innerCls = snippetClsName || 'Solution';
    L.push(`    static class ${innerCls} {`);
    if (sigs.length === 0) {
      L.push('        // TODO: 按题目模板补全（构造器与方法签名见 problem.json 的 javaSnippet）');
    } else {
      sigs.forEach(s => {
        const sig = s[0].trim().replace(/\s+\{$/, '');
        L.push(`        ${sig} {`);
        const dr = designDefaultReturn(sig);
        L.push('            // TODO: 补全方法体');
        L.push(`            ${dr}`);
        L.push('        }');
      });
    }
    L.push('    }');
  } else {
    const sig = `public ${method.ret} ${method.name}(${method.params.map(p => p.type + ' ' + p.name).join(', ')})`;
    L.push(`    ${sig} {`);
    L.push('        // TODO: 在这里实现你的解法');
    L.push(`        ${defaultReturn(method.ret)}`);
    L.push('    }');
  }
  L.push('    // ==== 提交代码结束 ====');
  L.push('');
  L.push('    public static void main(String[] args) {');
  if (!method) {
    L.push('        // 设计题：按题目示例手动构造调用序列，例如：');
    L.push(`        // ${snippetClsName || 'Solution'} s = new ${snippetClsName || 'Solution'}(...);`);
    L.push('        // s.method(...);');
    if (problem.exampleTestcases) {
      L.push('        // 题目原始示例输入：');
      problem.exampleTestcases.split('\n').forEach(x => L.push(`        // ${x}`));
    }
    L.push('        int failures = 0;');
  } else {
    L.push(`        ${className} s = new ${className}();`);
    L.push('        int failures = 0;');
    L.push('');
    L.push('        // ---- 示例测试（来自题目）----');
    (problem.examples || []).forEach((e, i) => {
      const argsList = [];
      let ok = true;
      for (const inp of e.inputs) {
        const t = paramByName[inp.name];
        if (!t) { ok = false; break; }
        try { argsList.push(javaLiteral(inp.value, t)); } catch (err) { ok = false; break; }
      }
      let expLit = null;
      if (ok && e.output != null) {
        try {
          if (method.ret === 'void') {
            // void 原地修改：期望输出按被修改的数组参数类型解析
            const arrParam = method.params.find(p => p.type.endsWith('[]'));
            if (arrParam && e.inputs.length === 1) expLit = javaLiteral(e.output, arrParam.type);
          } else {
            expLit = javaLiteral(e.output, method.ret);
          }
        } catch (err) { ok = false; }
      }
      if (!ok) {
        L.push(`        // TODO 示例${i + 1} 无法自动生成：输入 ${e.inputs.map(x => x.value).join(', ')}${e.output != null ? '，输出 ' + e.output : ''}`);
        return;
      }
      L.push('        try {');
      if (method.ret === 'void') {
        const arrParam = method.params.find(p => p.type.endsWith('[]'));
        if (arrParam && expLit && argsList.length === 1 && e.inputs.length === 1) {
          L.push(`            ${arrParam.type} nums = ${argsList[0]};`);
          L.push(`            s.${method.name}(nums);`);
          L.push(`            if (!${checkEqCall}(${expLit}, nums, "示例${i + 1}")) failures++;`);
        } else {
          L.push(`            s.${method.name}(${argsList.join(', ')});`);
          L.push(`            // TODO 示例${i + 1} 为 void 原地修改，请调用后手写断言`);
        }
      } else {
        const checkFn = UNORDERED_SLUGS.has(problem.slug) ? checkEqUnorderedCall : checkEqCall;
        L.push(`            if (!${checkFn}(${expLit}, s.${method.name}(${argsList.join(', ')}), "示例${i + 1}")) failures++;`);
      }
      L.push(`        } catch (Throwable t) { failures++; System.out.println("示例${i + 1} 异常: " + t); }`);
    });
    L.push('');
    L.push('        // ---- 边界测试（自己补充）----');
    L.push('        // TODO: 补充空输入 / 单元素 / 全相同 / 大数等边界');
    L.push(`        // 例如： try { if (!${checkEqCall}(期望, s.${method.name}(边界输入), "边界1")) failures++; } catch (Throwable t) { failures++; System.out.println("边界1 异常: " + t); }`);
    L.push(`        // 若题目允许任意顺序返回（下标对 / 集合），用 ${checkEqUnorderedCall} 代替 ${checkEqCall}`);
  }
  L.push('');
  L.push('        if (failures > 0) {');
  L.push('            System.out.println("测试未全部通过，失败 " + failures + " 个");');
  L.push('            System.exit(1);');
  L.push('        }');
  L.push('        System.out.println("全部测试通过");');
  L.push('    }');
  L.push('');
  if (useLocalHelpers) {
    L.push('    // ---- 本地测试辅助（节点格式化依赖本题的 ListNode/TreeNode，其余逻辑见 TestUtil）----');
    L.push('    static boolean checkEq(Object expected, Object actual, String label) {');
    L.push('        return TestUtil.eq(label, norm(expected), norm(actual));');
    L.push('    }');
    L.push('');
    L.push('    static boolean checkEqUnordered(Object expected, Object actual, String label) {');
    L.push('        return TestUtil.eq(label, normUnordered(expected), normUnordered(actual));');
    L.push('    }');
    L.push('');
    L.push('    static String norm(Object o) {');
    L.push('        if (o == null) return "null";');
  if (needListNode) {
    L.push('        if (o instanceof ListNode) {');
    L.push('            StringBuilder sb = new StringBuilder();');
    L.push('            for (ListNode c = (ListNode) o; c != null; c = c.next) {');
    L.push('                if (sb.length() > 0) sb.append(",");');
    L.push('                sb.append(c.val);');
    L.push('            }');
    L.push('            return sb.length() == 0 ? "null" : sb.toString();');
    L.push('        }');
  }
  if (needTreeNode) {
    L.push('        if (o instanceof TreeNode) {');
    L.push('            List<String> vals = new ArrayList<>();');
    L.push('            Queue<TreeNode> q = new LinkedList<>();');
    L.push('            q.offer((TreeNode) o);');
    L.push('            while (!q.isEmpty()) {');
    L.push('                TreeNode n = q.poll();');
    L.push('                if (n == null) { vals.add("null"); continue; }');
    L.push('                vals.add(String.valueOf(n.val));');
    L.push('                q.offer(n.left);');
    L.push('                q.offer(n.right);');
    L.push('            }');
    L.push('            while (!vals.isEmpty() && vals.get(vals.size() - 1).equals("null")) vals.remove(vals.size() - 1);');
    L.push('            return String.join(",", vals);');
    L.push('        }');
  }
    L.push('        return TestUtil.norm(o);');
    L.push('    }');
    L.push('');
    L.push('    static String normUnordered(Object o) {');
    L.push('        if (o instanceof Object[]) {');
    L.push('            List<String> es = new ArrayList<>();');
    L.push('            for (Object v : (Object[]) o) es.add(norm(v));');
    L.push('            Collections.sort(es);');
    L.push('            return "[" + String.join(", ", es) + "]";');
    L.push('        }');
    L.push('        if (o instanceof List) {');
    L.push('            List<String> es = new ArrayList<>();');
    L.push('            for (Object v : (List<?>) o) es.add(norm(v));');
    L.push('            Collections.sort(es);');
    L.push('            return "[" + String.join(", ", es) + "]";');
    L.push('        }');
    L.push('        return TestUtil.normUnordered(o);');
    L.push('    }');
    L.push('');
  }
  if (needListNode) {
    L.push('    static ListNode listNode(Object... vals) {');
    L.push('        if (vals.length == 0 || vals[0] == null) return null;');
    L.push('        ListNode dummy = new ListNode(0), cur = dummy;');
    L.push('        for (Object v : vals) {');
    L.push('            if (v == null) continue;');
    L.push('            cur.next = new ListNode(((Number) v).intValue());');
    L.push('            cur = cur.next;');
    L.push('        }');
    L.push('        return dummy.next;');
    L.push('    }');
    L.push('');
  }
  if (needTreeNode) {
    L.push('    static TreeNode treeNode(Object... vals) {');
    L.push('        if (vals.length == 0 || vals[0] == null) return null;');
    L.push('        TreeNode root = new TreeNode(((Number) vals[0]).intValue());');
    L.push('        Queue<TreeNode> q = new LinkedList<>();');
    L.push('        q.offer(root);');
    L.push('        int i = 1;');
    L.push('        while (!q.isEmpty() && i < vals.length) {');
    L.push('            TreeNode node = q.poll();');
    L.push('            if (i < vals.length && vals[i] != null) { node.left = new TreeNode(((Number) vals[i]).intValue()); q.offer(node.left); }');
    L.push('            i++;');
    L.push('            if (i < vals.length && vals[i] != null) { node.right = new TreeNode(((Number) vals[i]).intValue()); q.offer(node.right); }');
    L.push('            i++;');
    L.push('        }');
    L.push('        return root;');
    L.push('    }');
    L.push('');
  }
  if (needListNode && !standardListNode) {
    L.push('    static class ListNode {');
    L.push('        int val;');
    L.push('        ListNode next;');
    L.push('        ListNode() {}');
    L.push('        ListNode(int val) { this.val = val; }');
    L.push('        ListNode(int val, ListNode next) { this.val = val; this.next = next; }');
    L.push('    }');
    L.push('');
  }
  if (needTreeNode && !standardTreeNode) {
    L.push('    static class TreeNode {');
    L.push('        int val;');
    L.push('        TreeNode left;');
    L.push('        TreeNode right;');
    L.push('        TreeNode() {}');
    L.push('        TreeNode(int val) { this.val = val; }');
    L.push('        TreeNode(int val, TreeNode left, TreeNode right) { this.val = val; this.left = left; this.right = right; }');
    L.push('    }');
    L.push('');
  }
  L.push('}');
  return { className, content: L.join('\n') };
}

function main() {
  const problem = JSON.parse(fs.readFileSync(resolveProblem(), 'utf8'));
  const info = extractSnippetInfo(problem.javaSnippet || '');
  let method = null;
  if (info.total <= 1) method = extractMethod(info.body);
  const { className, content } = buildTemplate(problem, method, info.cls);
  const outDir = arg('--out') || DEFAULT_OUT;
  fs.mkdirSync(outDir, { recursive: true });
  const file = path.join(outDir, className + '.java');
  // 安全护栏：已有文件如果和模板不一致（例如用户已在提交区内写了代码），默认拒绝覆盖
  if (fs.existsSync(file)) {
    const old = fs.readFileSync(file, 'utf8').replace(/\r\n/g, '\n');
    if (old !== content && !args.includes('--force')) {
      console.error(`已存在 ${file}，且内容与生成模板不一致（可能包含你写的代码），已跳过覆盖。`);
      console.error('确认要重新生成请加 --force，或先把已有代码另存备份。');
      process.exit(1);
    }
  }
  fs.writeFileSync(file, content, 'utf8');
  console.log(`已生成：${file}`);
  console.log(`类名：${className}${method ? ` | 方法：${method.ret} ${method.name}(...)` : ' | 设计题'}`);
  console.log('请在 IntelliJ IDEA 中打开该文件，补全方法体，运行 main 测试。');
}

main();
