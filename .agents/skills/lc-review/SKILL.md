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

## 调度模型（写死，不要用户记数值）

- Ebbinghaus 遗忘曲线 + SM-2（EF = 2.5）
- 较强掌握（一次 AC 且全程无探讨）→ 间隔 = 上次 × 2.5（首次 1 天起），上限 60 天
- 较弱掌握（探讨过 / 提示过 / 提交非一次 AC）→ 间隔重置 1 天，连续较强计数清零
- 连续 2 次较强且间隔达上限 → 标记「已掌握」，退出复习队列（保留记录，可手动拉回）
- 第一轮二刷不走间隔：配额 = 第一轮剩余题数 ÷ 距 `2026-09-15` 剩余天数；题序 = 薄弱优先（一刷非一次 AC / 非最优）→ 一刷日期从旧到新
- 到期队列内：薄弱优先 → 到期日从早到晚

## 流程

1. 抽题：`node ".agents/skills/lc-review/scripts/review.js" next`。
   - 读取今日推荐（含配额）与队列；同时跑 `update_state.js code-notes`，把「当时写法未达最精简」的题合并进今日清单，重写时要求达到精简写法。
   - 展示时给出：题目链接、难度、分类、一刷完成日期、是否薄弱（⚠）、需要在 IDEA 打开的文件。
2. 重开题目：复用 `lc-practice` 的拉题/建题脚本生成新的 `src/LC{题号}_{题名}.java`。
   - **示例测试为主，不预塞边界用例**（避免剧透、考回忆）；文件头注明「二刷 · 一刷日期/思路」。
   - 一刷源码已在 `src/{分类}/` 归档，不删除；git 历史保留旧版。
3. 用户重写并本地测试通过后，走 `lc-submit` 提交力扣（复习提交由 lc-submit 自动识别）。
4. 记录掌握度：Accepted 后由 lc-submit 调用 `node ".agents/skills/lc-review/scripts/review.js" done --seq <seq> --mastery strong|weak ...`（含 approach/time/space/optimal/firstTry），写入 analysis.json reviews + 更新调度。
   - **掌握度判定**：提交一次 Accepted 且全程无探讨 = strong；探讨过/提示过/非一次 AC = weak。本地自己调试改对、提交一次 AC 算 strong。
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
