---
name: lc-submit
description: 力扣提交与复盘。当用户测试通过后说「提交」「提交力扣」「帮我提交」「测试过了」「提交一下」时使用：先确认本地测试通过，再通过力扣接口提交 Java 代码；Accepted 后分析写法是否最优并提示宽泛的最优思路（不替用户重写代码）；提交失败（WA/TLE/RE/编译错误）时做深度分析，指出错误位置但默认不直接给答案；同时更新进度与错误习惯。
---

# LC Submit — 提交与复盘

## 前置状态

- 项目：`D:\Projects\leetCode`；状态目录：`D:\Projects\leetCode\.lc`
- 依赖 `lc-practice` 的脚本：`.agents/skills/lc-practice/scripts/run_tests.js`、`update_state.js`

## 流程

1. 确认当前题：用户指定的文件，或 `src/` 最新的 `LC*.java`；从 `progress.json` 找到对应 seq/slug。
2. 先跑本地测试：`node ".agents/skills/lc-practice/scripts/run_tests.js" --file <文件路径>`。
   - 若未全部通过：回到 lc-analyze 流程，不提交。
3. 提交：`node ".agents/skills/lc-submit/scripts/submit.js" --file <文件路径> --slug <slug>`。
   - 若 `config.json` 未配置 cookie：明确告诉用户需要手动到 leetcode.cn 提交并把结果贴回来，或按提示把浏览器 cookie 填入 `config.json`（`leetcode_session` 与 `csrf_token`）。
   - 提交内容 = 两个 marker 之间的方法体，自动包装为 `class Solution`（设计题按原类名，脚本自动处理；必要时附带 ListNode/TreeNode 辅助类）。
4. 按判题结果分支：

   **Accepted（通过）**
   - 分析写法是否最优：时间复杂度/空间复杂度是否达最优、边界是否覆盖、代码是否清晰、有无明显冗余。
   - 若不够最优：只提示宽泛的最优思路（如「这题可以用双指针把 O(n²) 降到 O(n)」「应该想到单调栈」），让用户自己重写后再次提交；不替用户写。
   - 若已最优：点评优点，简要说明为什么这个复杂度已是下限。
   - 记录完成：`node ".agents/skills/lc-practice/scripts/update_state.js" done --seq <seq> --firstPass <是否一次通过> --optimal <是否最优> --notes "<一句话复盘>" --verdict Accepted --testcases 65/65 --memory <内存字节> --approach "解法名" --time O(n) --space O(n)`。
   - 存储约定：`progress.json` 只留精简完成索引；判题结果、复杂度、错误习惯等明细写入 `.lc/problems/{题号}_{slug}/analysis.json`（`done` 命令自动生成/合并，`update_state.js analysis --slug <slug>` 可查看）。
   - 自动打卡（无需用户提醒）：`node ".agents/skills/lc-practice/scripts/update_state.js" checkin --seq <seq>`，重新生成训练主页「reviews/index.html」（打卡表 + 进度 + 复盘列表，数据源 progress.json）。
   - 生成复盘报告（每次 Accepted 必做，无需用户提醒）：按 `reviews/{分类}/LC{题号}_{题名}_Review.html` 的既有格式（参考 `reviews/哈希表/LC0001_TwoSum_Review.html` 等），基于 `analysis.json` 与 `progress.json` 生成本期复盘页，内容包含「我的解法 / 解题过程 / 分析结果（判题指标、最优性、分类提醒、完成记录）」；报告存入当前题分类对应的 `reviews/{分类}/` 子目录（分类名中的 `/` 等非法字符替换为 `-`），页内相对链接按子目录层级写（`../../src/...`、`../../index.html`）。
     解题过程时间线只记录真实环节（如 独立实现 → 本地测试 → 提交 Accepted），**不包含「选题建题」**。
   - 报告内容只记录与算法学习相关的东西：解题思路、踩坑与修复、判题结果、错误习惯、复杂度分析。工具/环境问题（提交脚本 bug、cookie、IDE 配置、判题包装、编译包装等）**一律不写进报告**，也不出现在 analysis.json / progress.json 的笔记文字里。
   - 收尾（每次 Accepted 必做）：重新运行 `node ".agents/skills/lc-practice/scripts/build_site.js"` 让主页复盘列表指向新报告；随后用 git add + commit 提交当前分支的全部改动，至少包含：本题源码 `src/LC{题号}_{题名}.java`、`.lc/progress.json`、`.lc/problems/{题号}_{slug}/analysis.json`、`reviews/index.html` 与本期复盘报告，其余相关状态文件一并提交，不留未提交的工作区改动；汇报时把复盘报告链接主动发给用户。
   - 类似题型提醒：读取 `progress.json` 的 `category_hints`，把当前分类的宽泛思路再强调一遍，例如「以后遇到同类题，应该先想到双指针/单调栈/DP 状态定义」。

   **Wrong Answer / Time Limit Exceeded / Runtime Error / Compile Error**
   - 拿到失败用例（必做）：判题结果里的 `last_testcase`（输入）与 `expected_output`（期望输出）就是最可靠的回归用例；若脚本没打印出来，通过判题接口重新查询取回。
   - 补充到本地测试（必做）：把该失败用例转成 Java 测试，加进本题文件的 `main` 测试区（示例或边界区），让本地先能稳定复现这个错误，再进入分析。
   - 深度分析：引用该具体用例、期望 vs 实际输出、出错行/异常栈，指出逻辑上具体哪里错了。
   - 默认不直接给正确答案；只给宽泛提示与「错误出在哪一步」。用户明确索要当前思路的正确写法时才给出。
   - 记录错误习惯：`node ".agents/skills/lc-practice/scripts/update_state.js" habit add --text "..." --problem <slug> --category <分类>`。
   - 仅算法/逻辑类问题记录习惯；Compile Error 属于环境或代码格式问题，不记录。

5. 每次提交后向用户汇报：判题结果、用时/内存（如有）、复盘报告链接、下一步建议。

## 红线

- 未通过本地测试不提交。
- 提交失败时默认不给正确答案；除非用户明确说「给我当前思路的正确答案」。
- 不给用户重写整个解决方案；提示最优思路，让用户自己动手。
