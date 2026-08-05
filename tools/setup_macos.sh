#!/usr/bin/env bash
# macOS 首次配置：把仓库内的 lc 系列 skill 安装到 ~/.codex/skills/，
# 覆盖可能残留的旧版（含 Windows 硬编码路径）副本，保证 clone 后刷题体验一致。
#
# 用法（在仓库根目录）：
#   bash tools/setup_macos.sh
#
# 说明：
#   - 项目级 skill（.agents/skills/）在仓库内由 Codex 自动发现，本脚本主要解决
#     全局目录 ~/.codex/skills/ 下的旧副本问题；
#   - 幂等：重复执行会重新覆盖为仓库当前版本；
#   - 会生成 .lc/config.json 占位文件（力扣 cookie 需手动填入，该文件不入库）。
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
SKILLS_DEST="$HOME/.codex/skills"
SKILLS=("lc-analyze" "lc-practice" "lc-submit")

mkdir -p "$SKILLS_DEST"

for skill in "${SKILLS[@]}"; do
  src="$REPO_ROOT/.agents/skills/$skill"
  if [ ! -d "$src" ]; then
    echo "跳过（仓库中不存在）：$skill"
    continue
  fi
  # 白名单内固定 skill 名，确保删除目标安全
  case "$skill" in
    lc-analyze|lc-practice|lc-submit)
      rm -rf "$SKILLS_DEST/$skill"
      cp -R "$src" "$SKILLS_DEST/$skill"
      echo "已安装 skill：$skill"
      ;;
    *) echo "跳过（不在白名单）：$skill" ;;
  esac
done

if [ ! -f "$REPO_ROOT/.lc/config.json" ]; then
  printf '{\n  "leetcode_session": "",\n  "csrf_token": ""\n}\n' > "$REPO_ROOT/.lc/config.json"
  echo "已生成 .lc/config.json 占位文件（请手动填入力扣 cookie）"
fi

echo "完成。验证：ssh -T git@github.com"
