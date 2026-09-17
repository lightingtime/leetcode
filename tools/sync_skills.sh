#!/usr/bin/env bash
# 把仓库里的 lc skill 同步到全局 skills 目录（默认 ~/.codex/skills，遵守 CODEX_HOME）
#
# 用法（仓库内任意目录）：
#   bash tools/sync_skills.sh            # 同步
#   bash tools/sync_skills.sh --dry-run  # 只列出会改哪些文件，不写入
#   bash tools/sync_skills.sh --list     # 列出可同步 / 被跳过的 skill
#
# 规则：
#   - 只同步全局目录里**已存在**的 skill，不新建目录；lc-guide / lc-review / lc-status
#     与项目强关联，本来就只放在仓库 .agents/skills/，不会同步过去；
#   - 仓库 .agents/skills/ 是唯一源，同步是单向覆盖（rsync --delete）。
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
src_dir="$repo_root/.agents/skills"
dst_root="${CODEX_HOME:-$HOME/.codex}/skills"

mode="sync"
case "${1:-}" in
  "" ) ;;
  --dry-run) mode="dry" ;;
  --list)    mode="list" ;;
  -h|--help) sed -n '2,13p' "${BASH_SOURCE[0]}"; exit 0 ;;
  *) echo "未知参数：${1}（可用 --dry-run / --list / --help）" >&2; exit 2 ;;
esac

[ -d "$src_dir" ] || { echo "找不到 $src_dir" >&2; exit 1; }
[ -d "$dst_root" ] || { echo "找不到全局 skills 目录：$dst_root" >&2; exit 1; }

synced=0
skipped=0
for path in "$src_dir"/*/; do
  name="$(basename "$path")"
  if [ ! -d "$dst_root/$name" ]; then
    skipped=$((skipped + 1))
    [ "$mode" = "list" ] && echo "跳过 ${name}（全局目录未安装）"
    continue
  fi
  case "$mode" in
    list)
      echo "可同步 ${name}"
      ;;
    dry)
      echo "--- ${name} ---"
      rsync -an --delete --itemize-changes "$path" "$dst_root/$name/" | grep -v '^\.d' || true
      ;;
    sync)
      rsync -a --delete "$path" "$dst_root/$name/"
      echo "已同步 ${name} → $dst_root/$name"
      synced=$((synced + 1))
      ;;
  esac
done

if [ "$mode" = "sync" ]; then
  echo "完成：同步 ${synced} 个 skill，跳过 ${skipped} 个（源：${src_dir}）"
fi
