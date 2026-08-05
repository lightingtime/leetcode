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
   - 生成后立即检查示例测试覆盖：典型边界（空输入、单元素、奇数/偶数长度、全相同、大数、以及题型特有边界如链表相交/判环）未覆盖的，直接用现有辅助方法（必要时补构造辅助方法）把测试写进 main 测试区，不等用户提醒、不留给用户补。void 原地修改题要确认每个测试都断言了修改后的数组/对象（生成器对单数组参数已自动生成调用后断言）。
4. 读取生成的 .java 与 `problems/.../problem.md`，向用户展示：题目链接、难度、分类、题目简述、需要在 IDEA 里打开哪个文件。
5. 分类宽泛提示：从 progress.json 的 `category_hints` 取当前题分类的提示，告诉用户「这类题应该先想到……」；只给方向，不给具体算法或代码。
6. 告知用户流程：补全 `// ==== 提交代码开始 ====` 与 `// ==== 提交代码结束 ====` 之间的方法体，运行 main 测试；测试失败说「帮我分析」（lc-analyze），通过后说「提交」（lc-submit）。

## 注意

- 拉取失败或题目需会员：报告用户并跳到顺序中的下一题，不写入 done。
- 不要替用户写题解；用户索要答案时说明「先尝试，失败后让我分析」。
- 每题完成状态只由 lc-submit 在 Accepted 后写入；本 skill 不改 progress.json 的 done。
