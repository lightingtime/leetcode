# LeetCode 刷题

找工作前的算法面试刷题练习仓库（Java 21 + IntelliJ IDEA）。刷题流程约定见 [AGENTS.md](AGENTS.md)，完整题单与阶段计划见 [训练方案.md](训练方案.md)。

## 环境要求

- Java 21（`javac` / `java`）
- Node.js 18+（lc 系列 skill 脚本均为 Node，需支持 `fetch`）
- 建议 IntelliJ IDEA 打开 `src/`

## 目录结构

- `src/`：每道题一个 Java 文件，命名 `LC{题号}_{题名}.java`（如 `LC0001_TwoSum.java`），
  内含 `// ==== 提交代码开始 ====` / `// ==== 提交代码结束 ====` 标记的提交区与 `main` 自测；
  `TestUtil.java`、`ListNode.java`、`TreeNode.java` 为本地测试共用类。
- `reviews/`：训练主页 `index.html`（由脚本自动生成）+ 每题复盘页，按分类存放。
- `tools/`：用户侧辅助脚本。
- `.lc/`：刷题状态（进度、推荐顺序、题目数据、判题明细）；**`config.json` 存力扣 cookie，不入库**。
- `.agents/`：Codex 用的 lc 系列 skill（选题 / 分析 / 提交）。

## 日常刷题流程

1. 选题建题：`lc-practice`（读取进度、拉题、生成 Java 文件）；
2. 在提交区补全解法，运行 `main` 或 `node ".agents/skills/lc-practice/scripts/run_tests.js" src/<文件>`；
3. 测试失败：`lc-analyze` 分析思路；测试通过：`lc-submit` 提交力扣，Accepted 后自动更新进度与打卡。

## macOS / Linux 使用

- 本仓库脚本已改为相对仓库根目录的写法，跨平台直接可用（`clone` 到任何目录都能跑）。
- 若从早期快照恢复、或脚本重新出现硬编码 Windows 路径，可运行迁移脚本修正（幂等，重复执行无副作用）：

  ```bash
  node tools/migrate_windows_paths.js
  ```

  改写范围：`.agents/skills/*/scripts/*.js` 与 `SKILL.md`。
- 力扣 cookie 读取脚本（`read_cookie*.js`）依赖 Windows DPAPI / Chrome，macOS 上请手动登录
  leetcode.cn 后把 `LEETCODE_SESSION` 与 `csrftoken` 填入 `.lc/config.json`（该文件不入库）。

## 训练方案

完整题单分析、阶段计划与专题清单见 [训练方案.md](训练方案.md)。
