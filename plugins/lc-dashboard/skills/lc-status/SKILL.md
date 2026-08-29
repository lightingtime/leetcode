---
name: lc-status
description: 显示力扣刷题/复习状态面板。当用户在力扣刷题项目（仓库含 .lc/ 目录）里说「看状态」「刷题状态」「复习状态」「今天刷什么」「今天复习什么」「当前什么模式」「进度怎么样」「状态面板」等时使用：运行状态脚本，把模式、一刷/二刷进度、今日队列、掌握度分布以面板形式展示，并说明当前可用动作。
---

# LC Status — 刷题状态面板

## 流程

1. 运行状态脚本（在仓库根目录）：
   - 首选 `node "plugins/lc-dashboard/scripts/lc_status.mjs"`（本插件脚本，路径可能随安装位置变化）
   - 若该路径不存在，改用仓库自带的命令组合：`node ".agents/skills/lc-practice/scripts/update_state.js" mode` + `node ".agents/skills/lc-review/scripts/review.js" stats` + `review.js next`，把结果拼成同样的面板。
2. 把脚本输出的面板原样展示给用户（终端是 TTY 时自带颜色；对话里保留方框结构即可）。
3. 根据面板内容补充一句「下一步」：
   - 模式=复习且队列非空 → 「说『下一题』开始复习，或说『开始刷题』切回一刷」。
   - 模式=刷题且有新题 → 「说『下一题』刷下一道新题」。
   - 面板出现「题库新增 N 题未刷」警告 → 提醒用户复习模式已失效，自动切回刷题。

## 注意

- 只读操作，不修改任何状态文件。
- 用户想看实时面板（如单独终端）时，提示可运行 `node plugins/lc-dashboard/scripts/lc_status.mjs --watch`。
