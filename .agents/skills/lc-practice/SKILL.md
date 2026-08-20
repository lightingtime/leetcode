---
name: lc-practice
description: 力扣刷题主流程。当用户说「开始刷题」「执行 lc-practice / lc-practics」「今天刷什么」「拉题」「下一题」等，表示要开始一轮 LeetCode 练习时使用：读取进度与推荐顺序、选定下一题、从力扣官方接口拉取题目、在 仓库根目录（IntelliJ IDEA + Java 项目）中生成 Java 类文件与示例测试，并给出该分类对应的宽泛解题方向提示。本 skill 只负责「选题 + 建题」，不提供题解。
---

# LC Practice — 力扣刷题主流程

## 项目与状态文件

- 项目根目录：仓库根目录（IntelliJ IDEA 项目，Java 21）
- 状态目录：`仓库根目录\.lc`
  - `order.json`：178 题推荐顺序（seq 1..178，含阶段/分类/力扣链接）
  - `progress.json`：当前进度（done）、错误习惯（error_habits）、分类宽泛提示（category_hints）
  - `config.json`：力扣 cookie（可选，供 lc-submit 自动提交）
  - `problems/{题号}_{slug}/`：每次拉取的题目数据（problem.json / problem.md）

## 流程

1. 运行 `node ".agents/skills/lc-practice/scripts/update_state.js" next` 查看进度与下一题，记录其 seq、slug、分类。
   - 若全部完成：进入复习模式，随机抽已完成题让用户重写。
   - 复习模式 / 第二轮开始前：先跑 `node ".agents/skills/lc-practice/scripts/update_state.js" code-notes`，把「当时写法未达最精简」的题提醒给用户，重写时要求达到精简写法。
   - 若有历史错误习惯，只挑与当前题目分类/主题相关的读给用户听，提醒避免重犯；与本题无关的分类（如哈希表、树）跳过。
2. 拉取题目：`node ".agents/skills/lc-practice/scripts/fetch_problem.js" --slug <slug>`。网络请求需要用户批准，向用户说明这是正常流程。
3. 生成 Java 文件：`node ".agents/skills/lc-practice/scripts/create_problem.js" --slug <slug>`，写入 `src/LC题号_类名.java`（含方法签名、TODO 主体、示例测试 main、必要的 ListNode/TreeNode 辅助类）。
   - 节点类约定：标准 ListNode（val+next）/ TreeNode（val+left+right）用公共类 `src/ListNode.java`、`src/TreeNode.java`，不每题复制；仅当题目节点结构不同（如带 random/prev）才在题文件内生成本地节点类。
   - 生成后立即为本题补充边界测试并写进 main 测试区（不等用户提醒、不留给用户补）：边界用例必须**针对本题的具体逻辑与约束设计**，覆盖本题易错点（如空输入、单元素、全相同、大数、以及题型特有边界：链表相交/判环、树只有单边/链状/单节点、数组单调递增/递减或重复元素、n=0、输出可能为空等），直接用现有辅助方法（必要时补构造辅助方法）。
   - **先读题目约束再设计用例（必做）**：写边界用例前先看 `problem.md` 的「提示/约束」段，确认输入范围（如 `0 <= x,y <= 2^31-1`、`n >= 1`、元素取值范围），所有测试输入**必须在题目给定范围内**；禁止用范围外输入（如约束非负却用负数、约束 n>=1 却测空数组）。边界要覆盖范围的端点（最小值、最大值）和端点附近值，大输入也要在约束上限内。
   - **禁止偷懒**：不得套用与本题无关的通用模板用例凑数，也不得只留 `TODO: 补充边界` 占位；每个边界用例应能验证一个具体风险点。写完立即运行本地测试确认这些用例真实存在且全部通过，再进入下一步。
   - void 原地修改题要确认每个测试都断言了修改后的数组/对象（生成器对单数组参数已自动生成调用后断言）。
   - **子类型标注（分类细分的必做项）**：分类是「动态规划」时，生成文件后立即在文件头思路注释中标注具体子类型（线性/区间/树形/背包/状态机/数位/状压），识别方法见 `references/dp-subtypes.md`；其他大类有稳定子类型的（如「数学与位运算」拆位运算/数学公式）同样标注。子类型直接决定状态定义与转移套路，不能只写「动态规划」。
4. 读取生成的 .java 与 `problems/.../problem.md`，向用户展示：题目链接、难度、分类、题目简述、需要在 IDEA 里打开哪个文件。
   - 分类是「动态规划」时，展示中必须包含**子类型**（如「区间 DP」），并给用户一句话说明为什么是这一子类型。
5. **思路确认（用户先讲，Codex 再评）**：展示后先让用户口述解题思路，Codex 不要先给方向。评估思路是否可行：可行则指出关键边界与注意点（只给宽泛提示，不写代码）；不可行则指出哪一步假设/写法会有问题，引导修正，不直接给正确思路。
6. 分类宽泛提示：从 progress.json 的 `category_hints` 取当前题分类的提示，告诉用户「这类题应该先想到……」；只给方向，不给具体算法或代码。分类是「动态规划」时，改用 `references/dp-subtypes.md` 中对应子类型的宽泛提示（如区间 DP 的「正着做会破坏相邻关系，倒过来想最后一步处理谁」），category_hints 的通用提示只作兜底。
7. 告知用户流程：补全 `// ==== 提交代码开始 ====` 与 `// ==== 提交代码结束 ====` 之间的方法体，运行 main 测试；测试失败说「帮我分析」（lc-analyze），通过后说「提交」（lc-submit）。

## 注意

- **联网命令单独执行（必做）**：拉题（fetch_problem.js）和提交（submit.js）等联网命令必须单独运行，禁止用 `&&` 与本地命令（run_tests.js / create_problem.js）拼在一条命令里，避免本地命令连带进入提权请求；本地命令在沙箱内直接跑。
- **权限前缀用通用形式（必做）**：请求联网命令权限时，`prefix_rule` 用不带具体参数的通用前缀（如 `["node", ".agents/skills/lc-practice/scripts/fetch_problem.js"]`、`["node", ".agents/skills/lc-practice/scripts/create_problem.js"]`），禁止带 `--slug <具体值>` 等死参数，否则每次新题都要重新请求。
- 拉取失败或题目需会员：报告用户并跳到顺序中的下一题，不写入 done。
- 不要替用户写题解；用户索要答案时说明「先尝试，失败后让我分析」。
- 边界测试必须在建题时由 Codex 按本题具体设计并写入 main（见流程 3）；缺失或套用偷懒模板视为流程违规，后续 lc-submit 提交前会复查。
- 每题完成状态只由 lc-submit 在 Accepted 后写入；本 skill 不改 progress.json 的 done。
