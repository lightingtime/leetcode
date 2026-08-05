#!/usr/bin/env bash
# macOS 一键：从 Chrome / Edge 的登录态读取 leetcode.cn cookie，写入 .lc/config.json
#
# 用法（在仓库根目录）：
#   bash tools/setup_macos_cookie.sh
#
# 说明：
#   - 读取前请先完全退出 Chrome / Edge（cookie 库被占用时复制可能失败）；
#   - 首次运行若弹出钥匙串授权提示，请选择「始终允许」；
#   - cookie 值不会打印到控制台，只显示来源与长度。
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$REPO_ROOT"

if command -v pgrep >/dev/null 2>&1; then
  running=""
  for app in "Google Chrome" "Microsoft Edge"; do
    if pgrep -f "$app.app/Contents/MacOS" >/dev/null 2>&1; then
      running="${running}${running:+、}$app"
    fi
  done
  if [ -n "$running" ]; then
    echo "警告：检测到 $running 正在运行，cookie 库可能被锁定，建议先完全退出再读取。"
  fi
fi

node ".agents/skills/lc-submit/scripts/read_cookie_macos.js"
