---
name: lc-submit
description: 力扣提交与复盘。当用户测试通过后说「提交」「提交力扣」「帮我提交」「测试过了」「提交一下」时使用：先确认本地测试通过，再通过力扣接口提交 Java 代码；Accepted 后分析写法是否最优并提示宽泛的最优思路（不替用户重写代码）；提交失败（WA/TLE/RE/编译错误）时做深度分析，指出错误位置但默认不直接给答案；同时更新进度与错误习惯。
---

# LC Submit — 提交与复盘

## 前置状态

- 项目：仓库根目录；状态目录：`仓库根目录\.lc`
- 依赖 `lc-practice` 的脚本：`.agents/skills/lc-practice/scripts/run_tests.js`、`update_state.js`
- **Accepted 后的收尾一条命令**：`scripts/finish.js`（记录 + 套路 + 归档 + 复盘页 + 打卡 + commit + push 到当前分支 upstream；`--code-only` 只同步代码）

## 上下文纪律（省 token，必守）

- 查状态一律走脚本，**禁止** `cat` / `node -e require()` 直读 `.lc/progress.json`（129KB）、`.lc/order.json`（44KB）、`.lc/review_state.json`（66KB）：本题信息用 `update_state.js show --seq N`，判题/复习调度都在脚本输出里。
- 单题明细默认 `update_state.js show --seq N`（约 0.5KB，含一刷结论、每种写法摘要、二刷记录、下次复习日期）；需要某写法代码时 `show --seq N --code latest`；**只有确需全量字段**（如 approach_detail 原文）才 `analysis --slug S`。
- 本地测试用 `run_tests.js --file <路径>`（默认紧凑：通过 1 行、失败只回失败行）；确需 Java 完整输出才加 `--full`。
- 生成复盘页/主页后**不要读回 HTML 内容**（`reviews/**/*.html`、`index.html` 只给链接）；核对用 `rg -n` 查关键词即可。
- 读源码用 `sed -n 'a,bp'` / `rg -n` 定位片段；git 历史取旧代码用 `git show <commit>:<path>` 只取需要的片段，不要整篇 diff 刷屏。

## 流程

1. 确认当前题：用户指定的文件，或 `src/` 最新的 `LC*.java`；用 `node ".agents/skills/lc-practice/scripts/update_state.js" show --seq <seq>`（或 `--slug`）确认 seq/slug 与一刷情况，不要直读 progress.json。
2. 先跑本地测试：`node ".agents/skills/lc-practice/scripts/run_tests.js" --file <文件路径>`（默认紧凑输出；失败时会自动展开失败行）。
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
   - **收尾一条命令（记录 + 套路 + 归档 + 复盘页 + 打卡 + commit + push 全在里面）**：

     ```bash
     node ".agents/skills/lc-submit/scripts/finish.js" --seq <seq> \
       --approach "<解法名>" --time "O(n)" --space "O(1)" --optimal true \
       --verdict Accepted --testcases "65/65" [--memory <字节>] \
       --notes "<一句话复盘>" --approachDetail "<完整思路拆解>" [--dpSubtype 区间DP] \
       [--firstPass true] [--mastery strong|weak --firstTry true] \
       [--pattern-title "<套路名>" --pattern-text "<对照/取舍>"] [--dry-run]
     ```

     - 脚本自动判别一刷/二刷（`progress.json` 已有该 seq → 走 `review.js done`，此时 `--mastery` 必填）；提交区代码由脚本从文件自行提取，**不要把代码贴进命令行**。
     - 内部顺序：清理占位注释 → 本地测试（不通过就中止且不写任何状态）→ 记录（含代码）→ 套路入库 → 归档 → 复盘页 → 打卡（含 `build_site.js`）→ 只提交本题相关文件 → 推送当前分支的配置 upstream；归档位已有旧版会先报错，确认覆盖再加 `--overwrite-archive`。
     - 推送成功以脚本明确输出「已推送到 <upstream>」为准。提交成功但推送失败时，脚本报错并保留本地提交，修复连接或权限后再 push；没有 upstream 时按报错配置跟踪分支。`--dry-run` 显示提交/推送计划；`--no-commit` 跳过提交与推送，仅用于多写法暂存；用时/内存/用例数从 `submit.js` 的输出里取，不要编。
   - **完整思路拆解（每题必做，一次 AC 也不能省略）**——`--approachDetail` 必写四块：① 问题本质与解法选择理由；② 关键设计决策（状态定义、终止条件、选择/撤销、去重/剪枝）；③ 边界与细节（典型边界、约束外情况）；④ 复杂度与「为什么是最优/可接受」的下限论证。只写「解法名 + 复杂度」视为偷懒。
     - 写法：轻量 Markdown（空行分段；`- ` / `1. ` 列表；行内代码；`**加粗**`）；具体 case 推演单独成段、用列表逐层展开；存在「编号/下标 → 变量 → 语义」「状态 → 转移结果」映射时用 Markdown 表格逐行展示并配一个小输入；公式要说明每个变量的含义。
   - **子类型记录**：分类是「动态规划」时，收尾必须加 `--dpSubtype <子类型>`（线性/区间/树形/背包/状态机/数位/状压），且 approachDetail 的状态定义、转移、填表顺序要与该子类型套路对应（速查表见 lc-practice 的 `references/dp-subtypes.md`）。
   - **可复用套路**：解法含可迁移套路（中心扩散、双指针、单调栈…）时，收尾加 `--pattern-title/--pattern-text`（脚本同时写套路库与本题 analysis.json，复盘页「套路沉淀」区块呈现）。
   - 分析写法是否最优：复杂度是否达最优、边界是否覆盖、代码是否清晰、有无明显冗余。
   - **优化空间询问（必做）**：发现优化空间（未达最优复杂度、有更优/更简写法）先问用户「是否尝试最优解」；「尝试」→ 用 `finish.js --no-commit` 只记录，等所有写法写完再统一收尾；「不尝试」→ 正常收尾。不得在未询问的情况下直接归档。
   - 若不够最优：只提示宽泛的最优思路（如「这题可以用双指针把 O(n²) 降到 O(n)」「应该想到单调栈」），让用户自己重写后再次提交；不替用户写。
   - 若已最优：点评优点，简要说明为什么这个复杂度已是下限。
  - 思路已最优但代码写法可微调时（如冗余变量、可合并/简化的循环、重复表达式、残留 TODO 注释、赋值写法、字段/变量命名），在复盘与汇报中具体指出可优化点（只提示，不替用户重写）。**每次 Accepted 汇报前必做「精简检查」**：合并循环/三元简化、去冗余变量、清残留 TODO 注释，发现任何一项都要在汇报中指出。赋值/命名类优化提示统一在 Accepted 汇报时给出，提交前不因写法问题打断用户。
  - 精简检查后的处理约定（用户明确要求）：用户按提示改动后，只需本地 `main` 测试通过即视为完成，**不要求再次提交力扣**；无用 import 等文件卫生问题由 Codex 在提交收尾时直接清理，不写进复盘报告。
   - **讨论中形成的关键注释由 Codex 补进代码（用户明确要求）**：讲解/探讨（lc-guide、lc-analyze）确认过的参数语义、循环与边界不变量、易错点理由，收尾前由 Codex 直接补进提交区代码——一行一条「精确定义」式注释，只加在易错与曾错点，正确直白的代码保持无注释，禁止行尾注释与成段解释。注释必须在跑 `finish.js` **之前**补完（它保存的代码就是归档版与复盘页展示的版本）。
   - **Accepted 后用户又改代码（必做同步）**：本地测试通过即视为完成，**不要求重新提交力扣**；用
     `node ".agents/skills/lc-submit/scripts/finish.js" --seq <seq> --code-only [--approach "<原写法名>"]`
     同步最新提交区代码（旧代码自动进 `prev_code`，判题日期不动）并重生成复盘页 + commit。
   - 存储约定：`progress.json` 只留精简完成索引；判题结果、复杂度、错误习惯等明细写入 `.lc/problems/{题号}_{slug}/analysis.json`（查看用 `update_state.js show --seq <seq>`，不要整篇 dump）；不同解法才追加 `submissions`，同解法的代码微调只覆盖代码字段。
   - 多写法等待约定（用户明确要求）：用户说「先提交 X 写法」时，`finish.js --no-commit` 只记录判题结果与最优性，**暂缓归档 / 复盘 / 打卡 / 收尾**；等所有写法写完，最后统一收尾一次，复盘页按「写法 1 / 写法 2 …」逐一呈现（`generate_review.js` 已支持）。
   - **收尾前核对 submissions 不缺代码（必做）**：任何写法缺 `code` 都用 `git log --follow -- <旧路径>` + `git show <commit>:<路径>` 从历史找回写入（旧版存 `prev_code`）；禁止复盘页出现「写法 N 无代码」，禁止留空、禁止让用户自己翻 git。
   - 打卡与进度：`finish.js` 内部已跑 `checkin`（重新生成 `reviews/index.html`），输出含完成题数 X/170、打卡天数、剩余天数与需日均量（目标：8.30 前刷完一刷、9.15 前完成二刷），把这部分汇报给用户。
   - **报告范围**：只写算法相关（思路、踩坑与修复、判题、错误习惯、复杂度）；工具/环境问题（提交脚本、cookie、IDE、判题包装、编译包装等）**一律不写进报告**，也不进 analysis.json / progress.json 的笔记。解题过程时间线只记真实环节（独立实现 → 本地测试 → 提交 Accepted），不含「选题建题」。
   - **归档与提交**：`finish.js` 负责 `git mv` 归档（冲突时先报错）、清理占位注释、重生成复盘页与主页，并只提交本题相关文件（源码、`.lc/progress.json` 或 `review_state.json`、analysis.json、复盘页、`reviews/index.html`）；汇报时把复盘报告链接主动发给用户。
   - 类似题型提醒：分类是「动态规划」时，从 `update_state.js show --seq <seq>` 的输出拿 `DP子类型`，用 `references/dp-subtypes.md` 对应子类型的宽泛提示强调（如「区间 DP 先想最后一步处理谁、按区间长度填表」）；其他分类用 `update_state.js hint --seq <seq>` 取分类提示。

   **Wrong Answer / Time Limit Exceeded / Runtime Error / Compile Error**
   - 拿到失败用例（必做）：判题结果里的 `last_testcase`（输入）与 `expected_output`（期望输出）就是最可靠的回归用例；若脚本没打印出来，通过判题接口重新查询取回。
   - 补充到本地测试（必做）：把该失败用例转成 Java 测试，加进本题文件的 `main` 测试区（示例或边界区），让本地先能稳定复现这个错误，再进入分析。
   - 深度分析：引用该具体用例、期望 vs 实际输出、出错行/异常栈，指出逻辑上具体哪里错了。
   - 默认不直接给正确答案；只给宽泛提示与「错误出在哪一步」。用户明确索要当前思路的正确写法时才给出。
   - 记录错误习惯：`node ".agents/skills/lc-practice/scripts/update_state.js" habit add --text "..." --problem <slug> --category <分类>`。
   - 仅算法/逻辑类问题记录习惯；Compile Error 属于环境或代码格式问题，不记录。

5. 每次提交后向用户汇报：判题结果、用时/内存（如有）、复盘报告链接、下一步建议。
   - **汇报用 Markdown 表格（必做）**：判题 / 二刷对比（距上次天数、较强较弱、一次AC/多次）/ 复杂度最优性 / 下次复习日期 / **复盘报告链接**（`reviews/{分类}/LC{题号}_{题名}_Review.html` 相对链接）逐行列出；禁止退化成纯文字流水账。
   - **TUI 不重复贴分析全文（必做）**：复盘分析（notes / approach_detail）只存在于 analysis.json 与复盘页，汇报里禁止整段重复贴出；只给结论性摘要（较强/较弱、一次 AC/多次、一句话改进点），完整分析指向复盘页。

## 复习模式（二刷，lc-review 联动）

- **自动检测（必做）**：提交前若 `progress.json` 的 `done` 已存在该 slug（一刷已完成、源码已归档），本次即为复习提交，走本分支；否则按一刷流程。
- **Accepted 后掌握度判定**：一次提交即 Accepted 且全程无探讨 = `strong`（较强）；探讨过/提示过/非一次 AC = `weak`（较弱）。拿不准时向用户确认一句「这次有没有找过提示/探讨」再定。
- **记录（必做）**：用 `finish.js --seq <seq> --mastery strong|weak --firstTry <true|false> --approach "<解法名>" --time "<复杂度>" --space "<复杂度>" --optimal <true|false> [--memory <字节>] [--notes "<一句话>"]` 收尾（内部调 `review.js done`）：写 analysis.json 的 `reviews` + 更新 `review_state.json` 调度（较强 ×2.5 拉长 / 较弱重置）；**不要再跑一刷的 `done`**，否则会覆盖一刷事实。
- **同写法禁止拆成两条（必做）**：判定本次写法与一刷/最近 submission 是同一思路时，`--approach` 必须**复用该 submission 原有的 approach 字符串**（不要自造新措辞），`review.js` 才会走合并分支（保留最优 + 旧 code 存 prev_code）；确为不同写法（换思路/换数据结构）才用新 approach 追加。submissions 每一条 = 一种**写法**，不是一次提交。
- **汇报（必体现二刷对比）**：输出「二刷第 N 次 · 距上次 X 天 · 较强/较弱 · 一次 AC/多次」，并对比一刷：思路是否更换、复杂度/内存是否更优、是否命中已沉淀套路（`pattern` 记录）；据此更新 `optimal`/`code_notes`。
- **复盘页**：生成复盘页时复习记录区块自动置前（`generate_review.js` 已支持），submissions 由 review.js 合并（新思路追加、同思路保留最优）。
- **归档覆盖（必做）**：复习文件在 `src/` 根，Accepted 后用其内容覆盖 `src/{分类}/LC{题号}_{题名}.java`（git 历史保留一刷版），删除根目录新文件与建题时产生的 `src/{分类}/LC{题号}_{题名}.java.bak`（若有）；analysis.json / 复盘页里指向旧位置结构的链接同步更新。
- **原写法强制保留（必做，删 .bak 前核对）**：删除 `.java.bak` / 覆盖归档前，先核对 `analysis.json` 的 `submissions`——所有既有写法条目的 `code` 已保存（建题时已自动固化；若仍缺 code，用 `git show <归档commit>:src/{分类}/LC{题号}_{题名}.java` 找回提交区代码补上），同思路被覆盖的旧版存 `prev_code`。任何情况下不得让原写法/中间版本代码只存在于 git 历史而 analysis.json 缺失。
- **打卡与收尾**：由 `finish.js` 一并完成（`checkin` + `build_site.js` + commit，含 `review_state.json`）。
- **复习提交失败（WA/TLE/RE）**：按下方失败分支分析，但提示标准更高——先用 `update_state.js show --seq <seq>` 看一刷 `解法`/写法摘要（需要代码再 `--code`），指出「一刷用的 XX，这次为什么没想起来」，再给宽泛提示；错误习惯照常记录。

## 红线

- 未通过本地测试不提交。
- 提交失败时默认不给正确答案；除非用户明确说「给我当前思路的正确答案」。
- 不给用户重写整个解决方案；提示最优思路，让用户自己动手。
