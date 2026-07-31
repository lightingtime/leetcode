# 一键：从 Chrome 的登录态读取力扣 cookie，写入 .lc/config.json
# 用法: 双击 setup_cookie.cmd，或在当前账号的终端里运行本脚本
# 注意: 脚本会先优雅退出 Chrome（标签页可恢复），请在运行前保存重要的网页表单内容
$ErrorActionPreference = 'Stop'
Set-Location $PSScriptRoot

$chromeProc = Get-Process chrome -ErrorAction SilentlyContinue
if ($chromeProc) {
    Write-Host '检测到 Chrome 正在运行，将先优雅退出 Chrome（会话会被保存）。' -ForegroundColor Yellow
    & taskkill /IM chrome.exe 2>$null | Out-Null
    $waited = 0
    while ((Get-Process chrome -ErrorAction SilentlyContinue) -and $waited -lt 30) {
        Start-Sleep -Milliseconds 500
        $waited++
    }
    if (Get-Process chrome -ErrorAction SilentlyContinue) {
        Write-Host 'Chrome 未能在 15 秒内退出，可能有页面阻止关闭。请手动关闭后重试。' -ForegroundColor Red
        exit 1
    }
    Write-Host 'Chrome 已退出。' -ForegroundColor Green
}

node ".agents/skills/lc-submit/scripts/read_cookie_cdp.js"
if ($LASTEXITCODE -ne 0) {
    Write-Host '读取失败，请查看上方错误信息。' -ForegroundColor Red
    exit 1
}

Write-Host '完成！Cookie 已写入 .lc/config.json。' -ForegroundColor Green
$answer = Read-Host '是否重新打开 Chrome？(y/n，默认 y)'
if ($answer -ne 'n') {
    $chromeExe = 'C:\Program Files\Google\Chrome\Application\chrome.exe'
    if (Test-Path $chromeExe) { Start-Process $chromeExe }
}
