// 把 .agents/skills/*/scripts/*.js 与 SKILL.md 中硬编码的 Windows 绝对路径
// 改写为相对仓库根目录的写法，使项目可在 macOS / Linux 上直接使用。
//
// 用法（在仓库根目录运行）：
//   node tools/migrate_windows_paths.js
//
// 原理：脚本自身位于 <root>/tools/，仓库根目录 = path.resolve(__dirname, '..')；
//       skill 脚本位于 <root>/.agents/skills/<skill>/scripts/，因此
//       __dirname 上溯 4 级即仓库根目录。
// 幂等：重复运行不会重复改写（替换后目标字符串不再包含 Windows 路径）。
const fs = require('fs');
const path = require('path');

const ROOT = path.resolve(__dirname, '..');
const REPLACEMENTS = [
  // 统一把字面量路径替换为相对解析（各文件已 require('path')）
  {
    file: path.join(ROOT, '.agents/skills/lc-practice/scripts/run_tests.js'),
    pairs: [
      ["  const src = 'D:/Projects/leetCode/src';",
       "  const src = path.join(__dirname, '..', '..', '..', '..', 'src');"],
      ["const SHARED_HELPER = 'D:/Projects/leetCode/src/TestUtil.java';",
       "const ROOT = path.resolve(__dirname, '..', '..', '..', '..');\nconst SHARED_HELPER = path.join(ROOT, 'src', 'TestUtil.java');"],
      ["  'D:/Projects/leetCode/src/ListNode.java',",
       "  path.join(ROOT, 'src', 'ListNode.java'),"],
      ["  'D:/Projects/leetCode/src/TreeNode.java'",
       "  path.join(ROOT, 'src', 'TreeNode.java')"]
    ]
  },
  {
    file: path.join(ROOT, '.agents/skills/lc-practice/scripts/update_state.js'),
    pairs: [
      ["const LC_DIR = 'D:/Projects/leetCode/.lc';",
       "const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');"]
    ]
  },
  {
    file: path.join(ROOT, '.agents/skills/lc-practice/scripts/build_site.js'),
    pairs: [
      ["const ROOT = 'D:/Projects/leetCode';",
       "const ROOT = path.resolve(__dirname, '..', '..', '..', '..');"]
    ]
  },
  {
    file: path.join(ROOT, '.agents/skills/lc-practice/scripts/create_problem.js'),
    pairs: [
      ["// 可选: --out <目录> 默认 D:/Projects/leetCode/src；--force 允许覆盖与模板不一致的已有文件",
       "// 可选: --out <目录> 默认 仓库根目录/src；--force 允许覆盖与模板不一致的已有文件"],
      ["const LC_DIR = 'D:/Projects/leetCode/.lc';",
       "const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');"],
      ["const DEFAULT_OUT = 'D:/Projects/leetCode/src';",
       "const DEFAULT_OUT = path.join(__dirname, '..', '..', '..', '..', 'src');"]
    ]
  },
  {
    file: path.join(ROOT, '.agents/skills/lc-practice/scripts/fetch_problem.js'),
    pairs: [
      ["const LC_DIR = 'D:/Projects/leetCode/.lc';",
       "const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');"]
    ]
  },
  {
    file: path.join(ROOT, '.agents/skills/lc-submit/scripts/submit.js'),
    pairs: [
      ["const LC_DIR = 'D:/Projects/leetCode/.lc';",
       "const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');"]
    ]
  },
  {
    file: path.join(ROOT, '.agents/skills/lc-submit/scripts/read_cookie.js'),
    pairs: [
      ["const LC_DIR = 'D:/Projects/leetCode/.lc';",
       "const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');"]
    ]
  },
  {
    file: path.join(ROOT, '.agents/skills/lc-submit/scripts/read_cookie_cdp.js'),
    pairs: [
      ["const LC_DIR = 'D:/Projects/leetCode/.lc';",
       "const LC_DIR = path.join(__dirname, '..', '..', '..', '..', '.lc');"]
    ]
  }
];

// 文档中的路径引用统一改为「仓库根目录」（不关心 clone 到哪）
const DOC_FILES = [
  path.join(ROOT, '.agents/skills/lc-analyze/SKILL.md'),
  path.join(ROOT, '.agents/skills/lc-practice/SKILL.md'),
  path.join(ROOT, '.agents/skills/lc-submit/SKILL.md')
];

let changed = 0;

for (const { file, pairs } of REPLACEMENTS) {
  if (!fs.existsSync(file)) { console.log(`跳过（不存在）：${path.relative(ROOT, file)}`); continue; }
  let text = fs.readFileSync(file, 'utf8');
  const before = text;
  for (const [old, next] of pairs) {
    if (!text.includes(old)) continue;
    text = text.split(old).join(next);
  }
  if (text !== before) {
    fs.writeFileSync(file, text, 'utf8');
    console.log(`已改写：${path.relative(ROOT, file)}`);
    changed++;
  } else {
    console.log(`无变化：${path.relative(ROOT, file)}`);
  }
}

for (const file of DOC_FILES) {
  if (!fs.existsSync(file)) { console.log(`跳过（不存在）：${path.relative(ROOT, file)}`); continue; }
  let text = fs.readFileSync(file, 'utf8');
  const before = text;
  text = text.split(/`D:\\Projects\\leetCode`|`D:\/Projects\/leetCode`|D:\\Projects\\leetCode|D:\/Projects\/leetCode/).join('仓库根目录');
  if (text !== before) {
    fs.writeFileSync(file, text, 'utf8');
    console.log(`已改写：${path.relative(ROOT, file)}`);
    changed++;
  } else {
    console.log(`无变化：${path.relative(ROOT, file)}`);
  }
}

console.log(changed ? `完成，共改写 ${changed} 个文件。` : '所有文件均无需改写（可能已迁移过）。');
