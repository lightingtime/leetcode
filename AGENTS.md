# AGENTS.md — LeetCode 刷题项目

本项目是 LeetCode 算法刷题练习仓库（Java 21，建议用 IntelliJ IDEA 打开）。Codex 在本仓库内应遵循下面的约定。

## 目录结构

- `src/`：每道题一个 Java 文件，命名 `LC{题号}_{题名}.java`，如 `LC0001_TwoSum.java`。文件内用 `// ==== 提交代码开始 ====` / `// ==== 提交代码结束 ====` 标记要提交的方法体，并自带 `main` 示例测试。
  - `src/TestUtil.java`：本地测试共用的比较工具（`checkEq` / `checkEqUnordered` 等），只服务 `main` 自测，不参与力扣提交。
  - `src/ListNode.java` / `src/TreeNode.java`：公共节点类（标准 val+next / val+left+right 结构），链表/树题本地测试共用；节点结构不同的题目才在题文件内定义本地节点类。
- `reviews/`：训练主页 `reviews/index.html`（由 `build_site.js` 自动生成：进度、打卡表、题目复盘）+ 每道题的复盘网页/报告，按分类存到子目录，如 `reviews/哈希表/LC0001_TwoSum_Review.html`（分类目录名中的 `/` 等非法字符替换为 `-`）。
- `tools/`：用户侧辅助脚本，如 `tools/setup_cookie.cmd`（一键读取浏览器 cookie）。
- `.lc/`：刷题状态目录，不要手工改动
  - `progress.json`：精简索引（完成列表、聚合错误习惯、分类宽泛提示）。错误习惯只记录算法/逻辑类问题，环境配置与编译错误不计入。
  - `order.json`：178 题推荐顺序（seq 1..178）
  - `config.json`：力扣 cookie（供 lc-submit 使用）
  - `problems/{题号}_{slug}/`：题目数据（problem.json / problem.md）+ 每题分析明细（analysis.json：判题结果、复杂度、错误习惯等）

## 可用的 lc skill（按用户意图触发）

| 用户说的话 | 使用的 skill | 职责 |
| --- | --- | --- |
| 「开始刷题」「今天刷什么」「拉题」「下一题」 | lc-practice | 读取进度、选定下一题、拉题并生成 `src/` 下的 Java 文件 |
| 「提交」「提交力扣」「帮我提交」「测试过了」 | lc-submit | 确认本地测试通过后提交力扣，Accepted 后更新进度 |
| 「帮我分析」「我哪里错了」「测试不过」「看看我的代码」 | lc-analyze | 编译/运行拿到报错，指出思路中哪步假设或写法导致问题（只给宽泛提示） |

## 工作流约定

1. 选题建题走 `lc-practice`：先跑 `node ".agents/skills/lc-practice/scripts/update_state.js" next` 看进度和下一题，再拉题、生成 Java 文件；拉题需要联网时先向用户说明。
2. 只给宽泛解题方向（从 `progress.json` 的 `category_hints` 取当前分类提示），不要替用户写题解或直接给出正确代码。
3. 用户补全 `// ==== 提交代码开始 ====` 与 `// ==== 提交代码结束 ====` 之间的方法体。
4. 本地测试：`node ".agents/skills/lc-practice/scripts/run_tests.js" src/<文件>.java`，或让用户在 IDEA 里直接跑 `main`。
5. 测试失败 → 用 `lc-analyze` 分析；测试通过 → 用 `lc-submit` 提交（提交前先确认本地通过）。
6. 每题「已完成」状态只在 `lc-submit` 确认 Accepted 后写入 `progress.json`；`lc-practice` 不写 done。
7. 拉题失败或题目需会员：向用户说明后按 `order.json` 跳到下一题，不标记完成。
8. 文件分门别类存放：新增文件先按类别放入对应目录（`src/`、`reviews/`、`tools/`、`.lc/`、`.agents/`），不要把零散文件堆在仓库根目录。
9. 错误习惯只记录算法思路/边界/逻辑类问题；环境配置、编译错误、占位未实现等不计入。
10. 每题 Accepted 后自动打卡：重新生成 `reviews/index.html`（打卡表 + 进度 + 复盘列表，数据源 progress.json）并提交 git，不要等用户提醒。

## 环境说明

- Java 21（`javac` / `java` 需可用）
- Node.js：lc 系列 skill 的脚本均为 Node 脚本
- Python 3.13：已安装（部分工具脚本使用）
