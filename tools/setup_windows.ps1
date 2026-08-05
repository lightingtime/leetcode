# tools/setup_windows.ps1 - Windows 首次配置：把仓库内的 lc 系列 skill 安装到 $HOME\.codex\skills\
# 覆盖可能残留的旧版（含 Windows 硬编码路径）副本，保证 clone 后刷题体验一致。
#
# 用法（在仓库根目录的 PowerShell 里执行）：
#   powershell -ExecutionPolicy Bypass -File .\tools\setup_windows.ps1
#
# 说明：
#   - 项目级 skill（.agents/skills/）在仓库内由 Codex 自动发现，本脚本主要解决
#     全局目录 $HOME\.codex\skills\ 下的旧副本问题；
#   - 幂等：重复执行会重新覆盖为仓库当前版本；
#   - 会生成 .lc\config.json 占位文件（力扣 cookie 需手动填入，该文件不入库）。
$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$skillsDest = Join-Path $HOME '.codex\skills'
$skills = @('lc-analyze', 'lc-practice', 'lc-submit')

New-Item -ItemType Directory -Path $skillsDest -Force | Out-Null

foreach ($skill in $skills) {
    $src = Join-Path $repoRoot ".agents\skills\$skill"
    if (-not (Test-Path -LiteralPath $src)) {
        Write-Host "跳过（仓库中不存在）：$skill"
        continue
    }
    # 白名单内固定 skill 名，确保删除目标安全
    if ($skill -notin @('lc-analyze', 'lc-practice', 'lc-submit')) {
        Write-Host "跳过（不在白名单）：$skill"
        continue
    }
    $dest = Join-Path $skillsDest $skill
    if (Test-Path -LiteralPath $dest) {
        Remove-Item -LiteralPath $dest -Recurse -Force
    }
    Copy-Item -LiteralPath $src -Destination $dest -Recurse -Force
    Write-Host "已安装 skill：$skill"
}

$configPath = Join-Path $repoRoot '.lc\config.json'
if (-not (Test-Path -LiteralPath $configPath)) {
    @'
{
  "leetcode_session": "",
  "csrf_token": ""
}
'@ | Set-Content -LiteralPath $configPath -Encoding UTF8
    Write-Host '已生成 .lc\config.json 占位文件（请手动填入力扣 cookie）'
}

Write-Host '完成。'
