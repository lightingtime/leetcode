# AGENTS.md — LeetCode 刷题项目

本项目是 LeetCode 算法刷题练习仓库（Java 21，建议用 IntelliJ IDEA 打开）。Codex 在本仓库内应遵循下面的约定。

## 目录结构

- `src/`：每道题一个 Java 文件，命名 `LC{题号}_{题名}.java`，如 `LC0001_TwoSum.java`。文件内用 `// ==== 提交代码开始 ====` / `// ==== 提交代码结束 ====` 标记要提交的方法体，并自带 `main` 示例测试。进行中（未提交）的题目放 `src/` 根目录；已提交（Accepted）的题目按分类归档到 `src/{分类}/`，目录名与 `reviews/{分类}/` 一致，如 `src/哈希表/LC0001_TwoSum.java`。
  - `src/TestUtil.java`：本地测试共用的比较工具（`checkEq` / `checkEqUnordered` 等），只服务 `main` 自测，不参与力扣提交。
  - `src/ListNode.java` / `src/TreeNode.java`：公共节点类（标准 val+next / val+left+right 结构），链表/树题本地测试共用；节点结构不同的题目才在题文件内定义本地节点类。
- `reviews/`：训练主页 `reviews/index.html`（由 `build_site.js` 自动生成：进度、打卡表、题目复盘）+ 每道题的复盘网页/报告，按分类存到子目录，如 `reviews/哈希表/LC0001_TwoSum_Review.html`（分类目录名中的 `/` 等非法字符替换为 `-`）。
- `tools/`：用户侧辅助脚本，如 `tools/setup_cookie.cmd`（一键读取浏览器 cookie）。
- `.lc/`：刷题状态目录，不要手工改动
  - `progress.json`：精简索引（完成列表、聚合错误习惯、分类宽泛提示）。错误习惯只记录算法/逻辑类问题，环境配置与编译错误不计入。
  - `order.json`：170 题推荐顺序（seq 1..170）
  - `config.json`：力扣 cookie（供 lc-submit 使用）
  - `problems/{题号}_{slug}/`：题目数据（problem.json / problem.md）+ 每题分析明细（analysis.json：判题结果、复杂度、错误习惯、reviews 复习事件流等）
  - `review_state.json`：复习调度状态（每题 last/next_review_date、间隔、掌握度、是否已掌握），由 lc-review 维护，供复习抽题与主页展示
  - `mode.json`：当前模式（`practice` 刷题 / `review` 复习），「下一题」按此路由；切换：`update_state.js mode review|practice`，一刷完成自动切为 review

## 可用的 lc skill（按用户意图触发）

| 用户说的话 | 使用的 skill | 职责 |
| --- | --- | --- |
| 「开始刷题」「今天刷什么」「拉题」「下一题」 | lc-practice（mode=practice 时） | 读取进度、选定下一题、拉题并生成 `src/` 下的 Java 文件 |
| 「提交」「提交力扣」「帮我提交」「测试过了」 | lc-submit | 确认本地测试通过后提交力扣，Accepted 后更新进度 |
| 「帮我分析」「我哪里错了」「测试不过」「看看我的代码」 | lc-analyze | 编译/运行拿到报错，指出思路中哪步假设或写法导致问题（只给宽泛提示） |
| 「不懂」「不会写」「卡住了」「帮我看看思路」（尚未跑测试） | lc-guide | 名词先解释清楚、先规划变量及用途、分块推进，一次只讲一块，不直接给答案 |
| 「开始复习」「二刷」「复习下一题」「开始第二轮」 | lc-review（mode=review 时） | 二刷 + 间隔复习调度（较强×2.5 / 较弱重置），重开题目重写并重提交，按掌握度更新复盘 |

> 「下一题」按当前模式路由（`.lc/mode.json`）：`review` → lc-review 复习队列；`practice` → lc-practice 刷题。切换用 `update_state.js mode review|practice`；一刷全部完成时 `next` 自动切为 `review`。

## 工作流约定

1. 选题建题走 `lc-practice`：先跑 `node ".agents/skills/lc-practice/scripts/update_state.js" next` 看进度和下一题，再拉题、生成 Java 文件；拉题需要联网时先向用户说明。
2. 只给宽泛解题方向（从 `progress.json` 的 `category_hints` 取当前分类提示），不要替用户写题解或直接给出正确代码。
3. 拉题展示后先让用户口述思路（lc-practice 第 5 步）：Codex 评估可行性并提示边界，不先给方向、不写代码。
4. 用户补全 `// ==== 提交代码开始 ====` 与 `// ==== 提交代码结束 ====` 之间的方法体。
4. 本地测试：`node ".agents/skills/lc-practice/scripts/run_tests.js" src/<文件>.java`，或让用户在 IDEA 里直接跑 `main`。
5. 测试失败 → 用 `lc-analyze` 分析；测试通过 → 用 `lc-submit` 提交（提交前先确认本地通过）。
6. 每题「已完成」状态只在 `lc-submit` 确认 Accepted 后写入 `progress.json`；`lc-practice` 不写 done。
7. 拉题失败或题目需会员：向用户说明后按 `order.json` 跳到下一题，不标记完成。
8. 文件分门别类存放：新增文件先按类别放入对应目录（`src/`、`reviews/`、`tools/`、`.lc/`、`.agents/`），不要把零散文件堆在仓库根目录。
9. 错误习惯只记录算法思路/边界/逻辑类问题；环境配置、编译错误、占位未实现等不计入。
10. 每题 Accepted 后自动打卡：重新生成 `reviews/index.html`（打卡表 + 进度 + 复盘列表，数据源 progress.json）并提交 git，不要等用户提醒。
11. 每题 Accepted 复盘必写完整思路拆解（一次 AC、未与用户讨论的题也一样）：问题本质与解法选择理由、关键设计决策、边界与细节、复杂度下限论证，写入 `analysis.json` 的 `approach_detail`，复盘页「思路拆解」区块自动呈现；只写「解法名 + 复杂度」属于偷懒，不允许。
12. 分析/复盘讲解禁止用「隐含」「显然」带过关键逻辑：必须配具体 case（2~3 个元素的小输入）逐层推演状态变化，写进 `approach_detail` 与复盘页；讲解中同样要给出这样的 case。

## 环境说明

- Java 21（`javac` / `java` 需可用）
- Node.js：lc 系列 skill 的脚本均为 Node 脚本
- Python 3.13：已安装（部分工具脚本使用）

12. 复习轮（二刷）走 `lc-review`：抽题看 `node ".agents/skills/lc-review/scripts/review.js" next`；复习提交由 `lc-submit` 自动识别，Accepted 后调用 `review.js done` 写入 analysis.json `reviews` + 更新 `review_state.json`，**不覆盖 progress.json 的一刷事实**（date/firstPass/notes 保留，仅 optimal 可更新为当前最优）；掌握度判定：一次 AC 且无探讨 = 较强，探讨过/非一次 AC = 较弱。

## 展示约定

- 处理文件/分析/复盘时，不要在 CLI 里整段输出代码或长文件内容：查看文件用 `sed`/`rg` 定位到相关行，只展示关键片段；中间产物（如复盘 HTML、生成结果）只给文件链接，不在 CLI 里整段贴出。
- 生成文件完成后只告知「已完成 + 文件路径」，不展示文件内容——用户会自行打开查看；复盘 HTML 等大模板文件用脚本写入，避免改动预览整段刷屏。
- commentary 与最终回复保持简短：结论先行，必要证据（行号、用例）点到为止，长内容指向文件。
