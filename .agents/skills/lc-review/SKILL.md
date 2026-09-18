---
name: lc-review
description: 力扣二刷复习主流程。当用户说「开始复习」「二刷」「复习下一题」「开始第二轮」等，或第一遍全部完成后进入复习模式时使用：按间隔复习模型调度 170 题（较强×2.5 拉长 / 较弱重置缩短），重开题目让用户重写并重新提交力扣，按掌握度更新调度与复盘，产出可被 App 消费的结构化数据。
---

# LC Review — 二刷复习主流程

## 项目与状态文件

- 项目根目录：仓库根目录；状态目录：`仓库根目录\.lc`
- `review_state.json`：复习调度状态（每题 last/next_review_date、间隔、掌握度、是否已掌握）——由 `review.js` 维护，可由 analysis.json 的 reviews + 算法重建
- `analysis.json` 的 `reviews` 数组：每次复习的事件明细（append-only），供复盘页与后续 App 使用
- `progress.json`：只保留一刷索引，复习数据不写入；`done` 的 `optimal` 可更新为当前最优

## 上下文纪律（省 token，必守）

- 查状态一律走脚本，**禁止** `cat` / `node -e require()` 直读 `.lc/review_state.json`（66KB）、`.lc/progress.json`（129KB）、`.lc/order.json`（44KB）：队列用 `review.js next`，进度/掌握度分布用 `review.js stats`，单题信息用 `update_state.js show --seq N`。
- 抽题展示所需字段（一刷日期、是否薄弱、复杂度、下次复习日期）从 `review.js next` 与 `update_state.js show --seq N` 的输出里取，不要为拿这些字段去读 analysis.json。
- 本地测试用 `run_tests.js --file <路径>`（默认紧凑输出，失败才展开）；确需完整输出才加 `--full`。
- 复盘页与 `reviews/index.html` 只给链接、不读内容；读源码用 `rg -n` / `sed -n 'a,bp'` 取片段。

## 调度模型（写死，不要用户记数值）

- Ebbinghaus 遗忘曲线 + SM-2（EF = 2.5）
- 较强掌握（一次 AC 且全程无探讨）→ 间隔 = 上次 × 2.5（首次 1 天起），上限 60 天；第 2 次较强起每次再上浮 +2 天（尽早拉开，减少高频回访）
- 较弱掌握（探讨过 / 提示过 / 提交非一次 AC）→ 间隔重置 2 天（避免次日高频回访），连续较强计数清零
- 连续 2 次较强且间隔达上限 → 标记「已掌握」，退出复习队列（保留记录，可手动拉回）
- 第一轮二刷不走间隔：配额 = 第一轮剩余题数 ÷ 距 `2026-09-15` 剩余天数；题序 = 薄弱优先（一刷非一次 AC / 非最优）→ 一刷日期从旧到新
- 到期队列内：薄弱优先 → 到期日从早到晚
- **到期题优先（必做）**：`review.js next` 的今日推荐 = 全部到期题置前（薄弱优先 → 到期日早到晚）+ 配额补足的第一轮待刷；到期题不会被第一轮配额挤到队尾而无限搁置。

## 模式切换

- 本 skill 对应 `review` 模式（`.lc/mode.json`）。触发「开始复习 / 二刷」时先确保 `node ".agents/skills/lc-practice/scripts/update_state.js" mode review`；此后用户说「下一题」自动走本 skill 的 `review.js next`。
- 想回到一刷刷题：`update_state.js mode practice`。
- **题库新增题目时复习模式失效**：用户随时可能往 `order.json` 加新题；只要存在未完成一刷的题，`next` 会自动切回刷题模式，`review.js next/stats` 也会给出警告。复习前先跑 `next` 确认没有新题。

## 流程

1. 抽题：`node ".agents/skills/lc-review/scripts/review.js" next`（或复习模式下直接 `update_state.js next`）。
   - 读取今日推荐（含配额）与队列；同时跑 `update_state.js code-notes`，把「当时写法未达最精简」的题合并进今日清单，重写时要求达到精简写法。
     - 两者默认都只列前几题（`next` 前 6、`code-notes` 前 5）；队列/清单很长时不要加 `--all` 全量刷屏，按本次要做的题用 `--show N` 或 `show --seq N` 取需要的部分。
   - 展示时给出：题目链接、难度、分类、一刷完成日期、是否薄弱（⚠）、需要在 IDEA 打开的文件。
2. 重开题目：复用 `lc-practice` 的拉题/建题脚本生成新的 `src/LC{题号}_{题名}.java`。
   - **重复类自动处理（必做）**：建题脚本 `create_problem.js` 检测到 `src/{分类}/` 下已有同名一刷归档时，会把它临时改名为 `.java.bak`（IDEA 不编译），避免根目录二刷文件与归档类重复导致编译失败；提交收尾（lc-submit 归档覆盖）时恢复为归档文件。
   - **原写法强制保留（必做，禁止任何一步丢失中间版本代码）**：改名 `.java.bak` 前，`create_problem.js` 自动把归档源码的提交区代码固化进该题 `analysis.json` 的 `submissions`（补到缺 code 的既有条目，无条目则追加「一刷原写法」记录）；此后 `.bak` 被删除、归档被覆盖，一刷/中间版本代码都在 analysis.json 可查。任何流程都不得让 `submissions` 出现缺 `code` 的条目。
   - **测试用例与一刷保持一致（必做）**：把一刷归档 `main` 测试区（示例 + 边界 + 回归用例）原样同步到二刷文件，禁止重新设计或留 TODO；文件头注明「二刷 · 一刷日期/思路」和「测试用例与一刷归档保持一致」。
   - 一刷源码已在 `src/{分类}/` 归档，不删除；git 历史保留旧版。
3. 用户重写并本地测试通过后，走 `lc-submit` 提交力扣（复习提交由 lc-submit 自动识别）。
4. 记录掌握度：Accepted 后由 lc-submit 调用 `node ".agents/skills/lc-review/scripts/review.js" done --seq <seq> --mastery strong|weak ...`（含 approach/time/space/optimal/firstTry），写入 analysis.json reviews + 更新调度。
   - **同思路合并不丢旧代码（必做）**：`review.js done` 合并同 approach 的 submissions 时，若新代码覆盖旧 code，先把旧 code 存入 `prev_code` 字段（脚本已内置，禁止手工绕过）；复盘页「写法卡片」会以可折叠「上一版写法」展示。
   - **同写法只留一条（必做）**：submissions 每一条 = 一种**写法**，不是一次提交。同一思路的复习（措辞微调、命名优化、变量更清晰）必须复用原 approach 字符串走合并，禁止拆成多条；确为不同思路才追加新条。
   - **掌握度判定**：提交一次 Accepted 且全程无探讨 = strong；探讨过/提示过/非一次 AC = weak。本地自己调试改对、提交一次 AC 算 strong。
   - **notes 必写深度分析（禁止一句话敷衍）**：`--notes` 用多行 Markdown，至少含 ① 与上一轮对比（思路/写法/复杂度差异）；② 这次为什么想起来/没想起来（一刷犯的错这次如何规避）；③ 掌握度判定依据（是否一次 AC、有无探讨）；④ 下一步提醒（未尝试的更优写法、可顺带复习的相关题）。复盘页「复习记录 / 历轮表现」按 md 渲染，写浅了等于没复盘。
   - **notes 宁缺毋滥（禁止凑字数）**：只写有实质信息的点；「下一步提醒」没有实质内容就整节省略，不要用「已达最优不必尝试」这类废话填充。
   - **代码展示统一在「我的解法」**：复盘页代码只出现一份（当前源码 / 最优写法），复习 notes 不重复贴代码；改进只需一句话点明（如「从双查精简为 computeIfAbsent」），代码读者自会去「我的解法」看。
   - **进阶/多写法不另计复习次数**：同一轮复习内尝试多种写法（如自顶向下 → 自底向上 O(1)），只算**一次**复习事件（review_count +1、reviews 只追加一条）；`submissions` 可记录多种写法，notes 里合并说明各写法与取舍。
5. 复盘与收尾（lc-submit 复习分支）：生成复盘页（复习记录区块置前）→ checkin 打卡 → build_site → 归档覆盖一刷源码 → commit。

## 与现有 skill 的分工

- `lc-practice`：`update_state.js next` 全部完成后直接指向本 skill 的 `review.js next`。
- `lc-submit`：自动检测复习提交（progress.done 已有该 slug 即复习），汇报输出「二刷第 N 次 · 距上次 X 天 · 强/弱」+ 与一刷对比；Accepted 后调用 `review.js done`。
- `lc-analyze` / `lc-guide`：复习轮失败/卡住时先对比一刷解法（为什么这次没想起来），再走既有分析/引导流程。
- `build_site.js` / `generate_review.js`：主页展示二刷进度/今日到期/掌握度分布；复盘页渲染 reviews 区块。

## 红线

- 默认不替用户写题解；复习轮标准高于一刷：先让用户回忆，失败再分析（对比一刷）。
- 不修改 progress.json 的一刷事实（date/firstPass/notes）；只允许更新 optimal 为当前最优。
- 复习数据只写 analysis.json reviews + review_state.json，保持 progress.json 精简。
