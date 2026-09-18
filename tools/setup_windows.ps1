# tools/setup_windows.ps1 - Windows 首次配置：生成 .lc\config.json 占位文件
#
# 用法（在仓库根目录的 PowerShell 里执行）：
#   powershell -ExecutionPolicy Bypass -File .\tools\setup_windows.ps1
#   powershell -ExecutionPolicy Bypass -File .\tools\setup_windows.ps1 -InstallGlobalSkills
#
# 说明：
#   - 项目级 skill（.agents/skills/）在仓库内由 Codex 自动发现，**默认不再装全局副本**：
#     重复安装会让 skill 清单里每个 lc skill 出现两次（描述重复计费），且全局副本容易过期；
#     确实要在别的目录也用这些 skill 时才加 -InstallGlobalSkills；
#   - 幂等：重复执行无副作用；
#   - 会生成 .lc\config.json 占位文件（力扣 cookie 需手动填入，该文件不入库）。
param(
    [switch]$InstallGlobalSkills
)

$ErrorActionPreference = 'Stop'

$repoRoot = Split-Path -Parent $PSScriptRoot
$skillsDest = Join-Path $HOME '.codex\skills'
$skills = @('lc-analyze', 'lc-practice', 'lc-submit')

if ($InstallGlobalSkills) {
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
} else {
    Write-Host '默认不安装全局 skill 副本（避免清单重复与过期分叉）；需要时加 -InstallGlobalSkills'
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
