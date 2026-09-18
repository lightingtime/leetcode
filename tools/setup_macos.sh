#!/usr/bin/env bash
# macOS 首次配置：生成 .lc/config.json 占位文件（力扣 cookie 需手动填入，该文件不入库）。
#
# 用法（在仓库根目录）：
#   bash tools/setup_macos.sh                          # 只做 cookie 占位
#   bash tools/setup_macos.sh --install-global-skills   # 额外把 lc skill 装到 ~/.codex/skills/（可选）
#
# 说明：
#   - 项目级 skill（.agents/skills/）在仓库内由 Codex 自动发现，**默认不再装全局副本**：
#     重复安装会让 skill 清单里每个 lc skill 出现两次（描述重复计费），且全局副本容易过期；
#   - 确实要在别的目录也用这些 skill 时，加 --install-global-skills（等价于 sync_skills.sh --apply）。
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"

if [ "${1:-}" = "--install-global-skills" ]; then
  bash "$REPO_ROOT/tools/sync_skills.sh" --apply
fi

if [ ! -f "$REPO_ROOT/.lc/config.json" ]; then
  printf '{\n  "leetcode_session": "",\n  "csrf_token": ""\n}\n' > "$REPO_ROOT/.lc/config.json"
  echo "已生成 .lc/config.json 占位文件（可运行 bash tools/setup_macos_cookie.sh 从浏览器读取力扣 cookie）"
fi

echo "完成。"
