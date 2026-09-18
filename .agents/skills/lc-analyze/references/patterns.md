# 套路沉淀库（Patterns）

> 本文件原为 68KB 单文件，已拆成按分类的小文件。**不要整篇读**，用脚本按关键词查：
>
> - 索引：`references/patterns/index.md`（编号 → 套路 → 分类 → 文件）
> - 正文：`references/patterns/{分类}.md`（双指针与滑动窗口 / 动态规划 / 数组与矩阵 / 链表 / 树与二叉树 / 回溯 / 字符串 / 栈、队列与优先队列 / 图与并查集 / 哈希表 / 数学与位运算 / 贪心与区间）
> - 查询：`node ".agents/skills/lc-analyze/scripts/patterns.js" find <关键词>`（`--titles` 只看标题；`list [--grep] [--category]` 列索引；`show <编号>` 看单条）
> - 新增：`node ".agents/skills/lc-analyze/scripts/patterns.js" add --title "<套路名>" --text "<对照/取舍总结>" --source LC0141`（写入分类文件 + 更新索引），再跑 `update_state.js pattern add --slug <slug> --title ... --text ...` 把同一条写进该题 `analysis.json` 的 `patterns` 字段（复盘页「套路沉淀」区块由此呈现）
> - 校验：`node ".agents/skills/lc-analyze/scripts/patterns.js" check`
