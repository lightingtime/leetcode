---
name: lc-submit
description: 力扣提交与复盘。当用户测试通过后说「提交」「提交力扣」「帮我提交」「测试过了」「提交一下」时使用：先确认本地测试通过，再通过力扣接口提交 Java 代码；Accepted 后分析写法是否最优并提示宽泛的最优思路（不替用户重写代码）；提交失败（WA/TLE/RE/编译错误）时做深度分析，指出错误位置但默认不直接给答案；同时更新进度与错误习惯。
---

# LC Submit — 提交与复盘

## 前置状态

- 项目：仓库根目录；状态目录：`仓库根目录\.lc`
- 依赖 `lc-practice` 的脚本：`.agents/skills/lc-practice/scripts/run_tests.js`、`update_state.js`

## 流程

1. 确认当前题：用户指定的文件，或 `src/` 最新的 `LC*.java`；从 `progress.json` 找到对应 seq/slug。
2. 先跑本地测试：`node ".agents/skills/lc-practice/scripts/run_tests.js" --file <文件路径>`。
   - 若未全部通过：回到 lc-analyze 流程，不提交。
   - **提交前复查边界测试（必做）**：确认 main 测试区包含针对本题具体逻辑的边界用例（lc-practice 建题时应已写入）；若缺失或只有示例测试，先按本题易错点补齐再跑测试，禁止以「只有示例测试」提交。
   - **复查用例范围（必做）**：对照 `problem.md` 的约束段逐条检查测试输入是否越界（如约束非负却有负数、约束长度下限却有空输入）；发现越界用例先修正再提交，禁止用范围外输入当测试。
3. 提交：`node ".agents/skills/lc-submit/scripts/submit.js" --file <文件路径> --slug <slug>`。
   - **提交前必做清理（用户明确要求）**：删除提交区内的 `// TODO: 在这里实现你的解法` 等占位注释，以及文件头的「思路/复杂度 TODO」占位行；只删注释、不动逻辑。清理后跑一次本地测试确认仍通过再提交。
   - 若 `config.json` 未配置 cookie：明确告诉用户需要手动到 leetcode.cn 提交并把结果贴回来，或按提示把浏览器 cookie 填入 `config.json`（`leetcode_session` 与 `csrf_token`）。
     - macOS 可一键读取：先退出 Chrome/Edge，再运行 `bash tools/setup_macos_cookie.sh`（首次弹钥匙串授权提示选「始终允许」），脚本会把登录态写入 `config.json`。
   - 提交内容 = 两个 marker 之间的方法体，自动包装为 `class Solution`（设计题按原类名，脚本自动处理；必要时附带 ListNode/TreeNode 辅助类）。
4. 按判题结果分支：

   **Accepted（通过）**
   - 顺序约定：先确认判题结果正确（Accepted），再提醒最优性；不要在提交前因「未达最优」打断用户，保证正确优先。
   - **完整思路拆解（每题必做，一次 AC 也不能省略）**：无论是否一次通过、是否与用户讨论过，Accepted 后都必须写完整思路拆解并写入 `--approachDetail`：① 问题本质与解法选择理由（为什么用这个思路）；② 关键设计决策（状态定义、终止条件、选择/撤销、去重/剪枝逻辑）；③ 边界与细节（典型边界是否覆盖、约束外情况如何）；④ 复杂度分析与「为什么是最优/可接受」的下限论证。复盘页「思路拆解」区块由 `generate_review.js` 自动渲染。
   - **子类型记录（分类细分必做）**：分类是「动态规划」时，`done` 命令必须加 `--dpSubtype <子类型>`（线性/区间/树形/背包/状态机/数位/状压），存入 analysis.json 并在复盘页展示；approachDetail 的状态定义、转移、填表顺序要与该子类型套路对应（速查表见 lc-practice 的 `references/dp-subtypes.md`）。
   - **`--approachDetail` 写法规范**：使用轻量 Markdown 结构（空行分段；`- ` 开头成无序列表；`1. ` 开头成编号列表；反引号包行内代码；`**加粗**`），复盘页按此渲染。具体 case 推演要单独成段、用列表逐层展开，禁止拍成无层级的整段长文本。
   - **丰富表达**：凡是存在「编号/下标 → 变量 → 语义」或「状态 → 转移结果」的映射，优先使用 Markdown 表格逐行展示，并配一个具体的小输入；公式不能只写结论，必须说明每个变量的含义。适合时补充 ASCII 图示、分步列表和逐轮状态变化，让复盘读者能直接对照代码复现过程。
   - **可复用套路提醒**：Accepted 后若解法包含可迁移的套路（如中心扩散、双指针、单调栈），用 `update_state.js pattern add` 记录到本题分析，使复盘页的「套路沉淀」区块明确提醒后续复习重点。
   - 分析写法是否最优：时间复杂度/空间复杂度是否达最优、边界是否覆盖、代码是否清晰、有无明显冗余。
   - **优化空间询问（必做）**：分析写法时若发现当前写法**有优化空间**（未达最优复杂度、或存在更优思路/更简写法），必须主动询问用户「是否尝试最优解」；用户回答「尝试」→ 按下方「多写法等待约定」暂缓归档 / 复盘 / 打卡 / 收尾，等所有写法写完并各自提交后再统一整理；用户回答「不尝试」→ 正常归档总结。不得在未询问的情况下直接归档。
   - 若不够最优：只提示宽泛的最优思路（如「这题可以用双指针把 O(n²) 降到 O(n)」「应该想到单调栈」），让用户自己重写后再次提交；不替用户写。
   - 若已最优：点评优点，简要说明为什么这个复杂度已是下限。
  - 思路已最优但代码写法可微调时（如冗余变量、可合并/简化的循环、重复表达式、残留 TODO 注释、赋值写法、字段/变量命名），在复盘与汇报中具体指出可优化点（只提示，不替用户重写）。**每次 Accepted 汇报前必做「精简检查」**：合并循环/三元简化、去冗余变量、清残留 TODO 注释，发现任何一项都要在汇报中指出。赋值/命名类优化提示统一在 Accepted 汇报时给出，提交前不因写法问题打断用户。
   - 精简检查后的处理约定（用户明确要求）：用户按提示改动后，只需本地 `main` 测试通过即视为完成，**不要求再次提交力扣**；无用 import 等文件卫生问题由 Codex 在提交收尾时直接清理，不写进复盘报告。
   - **Accepted 后代码变更同步分析（必做）**：用户在任何时候重写/微调已 Accepted 的解法（等价精简、换写法等），只要本地测试通过对拍确认正确，就**必须把最新代码同步进该题 `.lc/problems/{题号}_{slug}/analysis.json` 的 `submissions` 记录（用 `--code` 保存最新提交区代码）**，并重新生成复盘页让代码块展示最新写法；若仅代码微调不换思路，不追加新 submission，直接覆盖旧代码字段；同时向用户确认「本地通过即视为完成、无需再提交力扣」。
   - 记录完成：`node ".agents/skills/lc-practice/scripts/update_state.js" done --seq <seq> --firstPass <是否一次通过> --optimal <是否最优> --notes "<一句话复盘>" --verdict Accepted --testcases 65/65 --memory <内存字节> --approach "解法名" --time O(n) --space O(n)`。
   - 存储约定：`progress.json` 只留精简完成索引；判题结果、复杂度、错误习惯等明细写入 `.lc/problems/{题号}_{slug}/analysis.json`（`done` 命令自动生成/合并，`update_state.js analysis --slug <slug>` 可查看）。
  - 提交历史规则：解法不同才追加到 analysis.json 的 `submissions`；相似解法（仅代码微调）只保留最优解（按 optimal 与内存比较，`done` 自动处理）；复盘报告体现不同解法的提交。
  - 多写法等待约定（用户明确要求）：用户说「先提交 X 写法」等表述表明后续还要尝试其他写法时，Accepted 后只记录判题结果与最优性，**暂缓归档 / 复盘 / 打卡 / 收尾**；等用户所有写法写完并各自提交后，再统一整理——analysis.json 的 `submissions` 记录每种不同解法，**每个解法都要有独立的思路/复杂度/代码分析（`done` 加 `--code "<解法代码>"` 保存，旧写法代码被覆盖前先从 git 历史取回）**，复盘页按「写法 1 / 写法 2 …」逐一呈现（`generate_review.js` 已支持），最后才归档 + build_site + commit。
  - 自动打卡（无需用户提醒）：`node ".agents/skills/lc-practice/scripts/update_state.js" checkin --seq <seq>`，重新生成训练主页「reviews/index.html」（打卡表 + 进度 + 复盘列表，数据源 progress.json）。
  - 每次打卡自动核对硬性进度：checkin 会输出当前完成题数 X/170、打卡天数、剩余天数与需日均量（目标：8.30 前刷完第一遍 170 题，9.15 前完成第二遍），并评估当天训练量是否达标，把这部分汇报给用户。
   - 生成复盘报告（每次 Accepted 必做，无需用户提醒）：按 `reviews/{分类}/LC{题号}_{题名}_Review.html` 的既有格式（参考 `reviews/哈希表/LC0001_TwoSum_Review.html` 等），基于 `analysis.json` 与 `progress.json` 生成本期复盘页，内容包含「我的解法 / 解题过程 / 分析结果（判题指标、最优性、分类提醒、完成记录）」；报告存入当前题分类对应的 `reviews/{分类}/` 子目录（分类名中的 `/` 等非法字符替换为 `-`），页内相对链接按子目录层级写（`../../src/...`、`../../index.html`）。
     解题过程时间线只记录真实环节（如 独立实现 → 本地测试 → 提交 Accepted），**不包含「选题建题」**。
   - 报告内容只记录与算法学习相关的东西：解题思路、踩坑与修复、判题结果、错误习惯、复杂度分析。工具/环境问题（提交脚本 bug、cookie、IDE 配置、判题包装、编译包装等）**一律不写进报告**，也不出现在 analysis.json / progress.json 的笔记文字里。
   - 收尾（每次 Accepted 必做）：先把本题源码归档到分类目录 `git mv src/LC{题号}_{题名}.java src/{分类}/LC{题号}_{题名}.java`（与 `reviews/{分类}/` 一致），确认 `main` 测试在归档路径仍通过；提交前清理文件内无用 import（只删 import 行，不动逻辑）；再重新运行 `node ".agents/skills/lc-practice/scripts/build_site.js"` 让主页复盘列表指向新报告；随后用 git add + commit 提交当前分支的全部改动，至少包含：归档后的本题源码 `src/{分类}/LC{题号}_{题名}.java`、`.lc/progress.json`、`.lc/problems/{题号}_{slug}/analysis.json`、`reviews/index.html` 与本期复盘报告，其余相关状态文件一并提交，不留未提交的工作区改动；汇报时把复盘报告链接主动发给用户。
   - 类似题型提醒：分类是「动态规划」时，读取本题 `analysis.json` 的 `dp_subtype`，用 `references/dp-subtypes.md` 对应子类型的宽泛提示强调（如「区间 DP 先想最后一步处理谁、按区间长度填表」）；其他分类读 `progress.json` 的 `category_hints`。

   **Wrong Answer / Time Limit Exceeded / Runtime Error / Compile Error**
   - 拿到失败用例（必做）：判题结果里的 `last_testcase`（输入）与 `expected_output`（期望输出）就是最可靠的回归用例；若脚本没打印出来，通过判题接口重新查询取回。
   - 补充到本地测试（必做）：把该失败用例转成 Java 测试，加进本题文件的 `main` 测试区（示例或边界区），让本地先能稳定复现这个错误，再进入分析。
   - 深度分析：引用该具体用例、期望 vs 实际输出、出错行/异常栈，指出逻辑上具体哪里错了。
   - 默认不直接给正确答案；只给宽泛提示与「错误出在哪一步」。用户明确索要当前思路的正确写法时才给出。
   - 记录错误习惯：`node ".agents/skills/lc-practice/scripts/update_state.js" habit add --text "..." --problem <slug> --category <分类>`。
   - 仅算法/逻辑类问题记录习惯；Compile Error 属于环境或代码格式问题，不记录。

5. 每次提交后向用户汇报：判题结果、用时/内存（如有）、复盘报告链接、下一步建议。
   - **汇报用 Markdown 表格（必做）**：判题 / 二刷对比（距上次天数、较强较弱、一次AC/多次）/ 复杂度最优性 / 下次复习日期 / **复盘报告链接**（`reviews/{分类}/LC{题号}_{题名}_Review.html` 相对链接）逐行列出；禁止退化成纯文字流水账。

## 复习模式（二刷，lc-review 联动）

- **自动检测（必做）**：提交前若 `progress.json` 的 `done` 已存在该 slug（一刷已完成、源码已归档），本次即为复习提交，走本分支；否则按一刷流程。
- **Accepted 后掌握度判定**：一次提交即 Accepted 且全程无探讨 = `strong`（较强）；探讨过/提示过/非一次 AC = `weak`（较弱）。拿不准时向用户确认一句「这次有没有找过提示/探讨」再定。
- **记录（必做）**：调用 `node ".agents/skills/lc-review/scripts/review.js" done --seq <seq> --mastery strong|weak --firstTry <true|false> --approach "<解法名>" --time <复杂度> --space <复杂度> --optimal <true|false> [--memory <字节>] [--notes "<一句话>"]`，写入 analysis.json 的 `reviews` 并更新 `review_state.json` 调度（较强 ×2.5 拉长 / 较弱重置 1 天）；不要再跑一刷的 `done` 覆盖一刷事实。
- **汇报（必体现二刷对比）**：输出「二刷第 N 次 · 距上次 X 天 · 较强/较弱 · 一次 AC/多次」，并对比一刷：思路是否更换、复杂度/内存是否更优、是否命中已沉淀套路（`pattern` 记录）；据此更新 `optimal`/`code_notes`。
- **复盘页**：生成复盘页时复习记录区块自动置前（`generate_review.js` 已支持），submissions 由 review.js 合并（新思路追加、同思路保留最优）。
- **归档覆盖（必做）**：复习文件在 `src/` 根，Accepted 后用其内容覆盖 `src/{分类}/LC{题号}_{题名}.java`（git 历史保留一刷版），删除根目录新文件；analysis.json / 复盘页里指向旧位置结构的链接同步更新。
- **打卡与收尾**：照常 `checkin --seq` + `build_site.js` + commit（含 review_state.json）。
- **复习提交失败（WA/TLE/RE）**：按下方失败分支分析，但提示标准更高——先读 `analysis.json` 的一刷 `approach`/`submissions`，指出「一刷用的 XX，这次为什么没想起来」，再给宽泛提示；错误习惯照常记录。

## 红线

- 未通过本地测试不提交。
- 提交失败时默认不给正确答案；除非用户明确说「给我当前思路的正确答案」。
- 不给用户重写整个解决方案；提示最优思路，让用户自己动手。
