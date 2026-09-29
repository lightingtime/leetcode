# AGENTS.md — LeetCode 刷题项目

Java 21 刷题仓库（IntelliJ IDEA 打开），170 题一刷 + 间隔二刷。**本文件只做索引与红线；流程细节在各 skill（`.agents/skills/lc-*`）里，不在这里重复。**

## 目录

- `src/`：每题一个 Java 文件 `LC{题号}_{题名}.java`，含 `// ==== 提交代码开始 ====` / `// ==== 提交代码结束 ====` 标记与 `main` 自测；进行中放 `src/` 根目录，Accepted 后归档到 `src/{分类}/`（目录名与 `reviews/{分类}/` 一致）。`src/TestUtil.java`（本地比对工具）、`src/ListNode.java`、`src/TreeNode.java` 为公共类，不参与力扣提交。
- `reviews/`：`index.html`（脚本生成的进度/打卡/复盘主页）+ 每题复盘页，按分类存子目录。
- `tools/`：用户侧脚本。`plugins/lc-dashboard/`：状态面板插件（本地市场 `leetcode-local`，清单在 `.agents/plugins/marketplace.json`）。
- `.lc/`：刷题状态目录——`progress.json`（一刷精简索引）、`order.json`（170 题顺序）、`review_state.json`（复习调度）、`mode.json`（practice/review）、`problems/{题号}_{slug}/`（题目数据 + `analysis.json` 明细）。**只由脚本读写：不要手工改，也不要 cat 读**（progress 129KB / review_state 66KB / order 44KB）。

## 触发路由

| 用户说 | skill | 职责 |
| --- | --- | --- |
| 开始刷题 / 今天刷什么 / 拉题 / 下一题（practice 模式） | lc-practice | 选题 + 拉题建题，写 main 边界测试；**不写 done** |
| 提交 / 提交力扣 / 测试过了 | lc-submit | 本地测试通过后提交；Accepted 后复盘 + 打卡 + 归档 + commit + push 到当前分支 upstream |
| 帮我分析 / 我哪里错了 / 测试不过 / 看看我的代码 | lc-analyze | 编译运行拿报错，指出哪一步想错（不给正确代码），记录错误习惯 |
| 不懂 / 不会写 / 卡住了 / 下一步怎么做 | lc-guide | 名词 → 变量 → 分块引导，一次一块，不写完整答案 |
| 开始复习 / 二刷 / 复习下一题 | lc-review | 间隔复习抽题、重开题重提交、按掌握度更新调度 |
| 看状态 / 进度怎么样 / 今天复习什么 | lc-status | 跑状态面板脚本 `plugins/lc-dashboard/scripts/lc_status.mjs` |

「下一题」按 `.lc/mode.json` 路由：`practice` → lc-practice；`review` → lc-review。切换用 `update_state.js mode practice|review`（一刷全部完成自动切 review；题库新增未刷题自动切回 practice）。

## 硬性约定

1. 只给宽泛解题方向，不替用户写题解、不给正确代码；用户明确索要「当前思路的正确答案」时才给。选题展示后先让用户口述思路，再评估可行性与边界。
2. 讲解与复盘禁止用「隐含 / 显然 / 自然」带过关键逻辑：必须配 2~3 个元素的具体输入逐层推演状态变化（approach_detail 与复盘页同样要求）。
3. 每题「已完成」只在 lc-submit 确认 Accepted 后写入 `progress.json`；拉题失败或题目需会员时按 `order.json` 跳到下一题，不标记完成。
4. 本地测试用 `node ".agents/skills/lc-practice/scripts/run_tests.js" --file src/<文件>.java`（默认紧凑输出，失败自动展开；需完整输出加 `--full`），或用户在 IDEA 里跑 `main`。测试失败走 lc-analyze，通过走 lc-submit。
5. Accepted 后收尾走一条命令 `node ".agents/skills/lc-submit/scripts/finish.js" --seq N --approach ... --time ... --space ... --approachDetail ...`（一刷/二刷自动判别；内部完成记录 + 归档 + 复盘页 + 打卡 + 将仓库内全部非忽略改动一并 commit + push）。思路拆解必写（问题本质与解法选择理由、关键设计决策、边界细节、复杂度下限论证）；讨论中确认过的关键注释由 Codex 在跑 finish 之前补进提交区代码；用户事后又改代码用 `finish.js --code-only` 同步，不重写判题日期。推送失败时明确报告失败并保留本地提交，不把本题汇报为已推送。
6. 复习轮（二刷）走 lc-review：Accepted 后调 `review.js done` 写 `analysis.json` 的 `reviews` + 更新 `review_state.json`，**不覆盖 `progress.json` 的一刷事实**（仅 optimal 可更新）。掌握度：一次 AC 且无探讨 = 较强；探讨过/非一次 AC = 较弱。
7. 错误习惯只记录算法思路/边界/逻辑类问题；环境配置、编译错误、占位未实现不计入。
8. 新增文件按类别放进 `src/`、`reviews/`、`tools/`、`.lc/`、`.agents/`，不要堆在仓库根目录。

## 查询与输出纪律（省 token，必守）

- 状态一律走脚本：`update_state.js next|stats|plan|code-notes`、单题用 `show --seq N`、分类提示用 `hint --seq N`、复习队列用 `review.js next|stats`。**禁止** `cat` / `node -e require()` 直读 `.lc/*.json`。
- 单题明细默认 `update_state.js show --seq N`（约 0.5KB）；只有确需全量字段才 `analysis --slug S`；要某个写法的代码用 `show --seq N --code latest`。
- 读文件用 `rg -n` + `sed -n 'a,bp'` 定位片段，不整篇 cat；复盘页与 `reviews/index.html` 只给链接、不读内容、不在 CLI 整段贴出；套路库用 `lc-analyze/scripts/patterns.js find <关键词>` 查，`references/*.md` 都只读需要的小节。
- 长流程尽量一题一会话：做完一题就新开会话，避免上下文累积到几十万 token 后被反复重发。

## 环境

Java 21（`javac` / `java`）、Node.js（lc 系列 skill 脚本均为 Node）、Python 3.13（部分工具脚本）。
