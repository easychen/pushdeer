#Requires -Version 5.1
<#
.SYNOPSIS
  一条命令构建「PushDeer 轮询通知版」，并把产物投递到 ..\发布\。

.DESCRIPTION
  本机只有 Windows PowerShell 5.1（没有 pwsh 7），所以脚本按 5.1 写。
  这个文件必须以「UTF-8 带 BOM」保存，否则 5.1 会把中文读成乱码。
  外部命令（java/aapt/gradlew）的 stderr 会被捕获，不用 -ErrorAction Stop 判错，
  统一看 $LASTEXITCODE。

.EXAMPLE
  powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1

.EXAMPLE
  powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -Version 1.1-alpha-12

.EXAMPLE
  powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -Clean
#>
param(
    [string]$Version,
    [switch]$Clean,
    [string]$JavaHome,
    [string]$AndroidHome,
    [string]$GradleHome
)

$ErrorActionPreference = "Continue"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$androidDir = Join-Path $root "android"
$appGradle = Join-Path $androidDir "app\build.gradle"
$releaseDir = Join-Path (Split-Path -Parent $root) "发布"

function Resolve-FirstPath {
    param([string[]]$Candidates, [string]$Label)
    foreach ($c in $Candidates) {
        if ($c -and (Test-Path -LiteralPath $c)) { return (Resolve-Path -LiteralPath $c).Path }
    }
    throw "找不到 $Label，请用参数指定或用环境变量设置。试过：`n  " + ($Candidates -join "`n  ")
}

# ---- JDK 11：项目用 Kotlin 1.5.31 + kapt，JDK 17 编不过 ----
if (-not $JavaHome) { $JavaHome = $env:JAVA_HOME }
$JavaHome = Resolve-FirstPath -Label "JDK 11" -Candidates @(
    $JavaHome,
    "C:\Users\$env:USERNAME\jdk11\jdk-11.0.25+9",
    "$env:ProgramFiles\Microsoft\jdk-11.0*"
)
$jdkRaw = (& (Join-Path $JavaHome "bin\java.exe") -version 2>&1 | Out-String)
if ($jdkRaw -notmatch 'version "11\.') {
    throw "JAVA_HOME 指向的不是 JDK 11（实际：$($jdkRaw.Trim())）。Kotlin 1.5.31 的 kapt 在 JDK 17 上会失败。"
}

# ---- Android SDK ----
if (-not $AndroidHome) { $AndroidHome = if ($env:ANDROID_HOME) { $env:ANDROID_HOME } else { $env:ANDROID_SDK_ROOT } }
$AndroidHome = Resolve-FirstPath -Label "Android SDK" -Candidates @(
    $AndroidHome,
    "C:\Users\$env:USERNAME\Android\Sdk",
    "$env:LOCALAPPDATA\Android\Sdk"
)
foreach ($p in @("platforms\android-31", "build-tools\30.0.2")) {
    if (-not (Test-Path -LiteralPath (Join-Path $AndroidHome $p))) {
        throw "Android SDK 缺少 $p，用 sdkmanager 装上再试。"
    }
}

# ---- Gradle 缓存：默认放仓库同级的 .gradle-home，不污染用户目录 ----
if (-not $GradleHome) {
    $GradleHome = if ($env:GRADLE_USER_HOME) { $env:GRADLE_USER_HOME } else { Join-Path (Split-Path -Parent $root) ".gradle-home" }
}
if (-not (Test-Path -LiteralPath $GradleHome)) {
    New-Item -ItemType Directory -Force -Path $GradleHome | Out-Null
}

# ---- 可选：改版本号 ----
if ($Version) {
    $text = [IO.File]::ReadAllText($appGradle)
    $new = [regex]::Replace($text, 'versionName\s+"[^"]*"', "versionName `"$Version`"")
    if ($new -eq $text) { throw "build.gradle 里没找到 versionName，未做修改。" }
    # 用 UTF8 无 BOM 写回：Set-Content -Encoding utf8 在 5.1 会加 BOM
    [IO.File]::WriteAllText($appGradle, $new, (New-Object Text.UTF8Encoding $false))
    Write-Host "versionName -> $Version"
}

$versionName = ([regex]::Match([IO.File]::ReadAllText($appGradle), 'versionName\s+"([^"]*)"')).Groups[1].Value
Write-Host "构建 $versionName" -ForegroundColor Cyan

# ---- 构建 ----
$env:JAVA_HOME = $JavaHome
$env:ANDROID_HOME = $AndroidHome
$env:ANDROID_SDK_ROOT = $AndroidHome
$env:GRADLE_USER_HOME = $GradleHome

Push-Location $androidDir
try {
    # 参数必须逐字写出来：splatting / 拼字符串调用 .bat 时 PowerShell 5.1 会把空格吃掉
    if ($Clean) {
        $out = & .\gradlew.bat clean assembleDebug --offline --no-daemon 2>&1
    } else {
        $out = & .\gradlew.bat assembleDebug --offline --no-daemon 2>&1
    }
    $code = $LASTEXITCODE
    $out | Select-Object -Last 15
} finally {
    Pop-Location
}
if ($code -ne 0) { throw "Gradle 失败（exit $code）" }

$apk = Join-Path $androidDir "app\build\outputs\apk\debug\app-debug.apk"
if (-not (Test-Path -LiteralPath $apk)) { throw "没找到产物：$apk" }

$hash = (Get-FileHash -LiteralPath $apk -Algorithm SHA256).Hash
$size = (Get-Item -LiteralPath $apk).Length

$aaptRaw = (& (Join-Path $AndroidHome "build-tools\30.0.2\aapt.exe") dump badging $apk 2>&1 | Out-String)
$pkgLine = ($aaptRaw -split "`r?`n" | Where-Object { $_ -match "^package:" } | Select-Object -First 1)
if ($pkgLine) { Write-Host $pkgLine }

# ---- 投递 ----
if (Test-Path -LiteralPath $releaseDir) {
    $target = Join-Path $releaseDir "PushDeer-轮询通知版-$versionName.apk"
    Copy-Item -LiteralPath $apk -Destination $target -Force
    Write-Host "已投递 $target" -ForegroundColor Green
} else {
    Write-Host "发布目录不存在，跳过投递：$releaseDir" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "versionName : $versionName"
Write-Host "size        : $size"
Write-Host "sha256      : $hash"
