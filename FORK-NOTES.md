# PushDeer Android · 自维护 Fork 说明

> 这不是官方仓库。上游是 [easychen/pushdeer](https://gitee.com/easychen/pushdeer)（GitHub 镜像：[easychen/pushdeer](https://github.com/easychen/pushdeer)）。
> 这个 fork 只做一件事：**让 Android 端重新能收到通知**。

## 为什么要这个 fork

PushDeer 官方 Android 客户端依赖小米推送，而小米在 2022 年 6 月改了协议（未上架小米商店的应用不再获得推送服务）。官方 1.0 beta 的更新说明里写着"受小米推送新协议影响，目前 Android 版的推送已不可用，只能等待之后的 MQTT 版本"，此后四年没有再发版。

结果就是：消息在服务器上是发了的（接口返回 `code:0`），但 App 只有打开时才去拉列表，**系统通知永远不弹**。Bark 只有 iOS，Qmsg 只推 QQ，安卓用户等于没有任何可用通道。

## 相对上游改了什么

| 模块 | 文件 | 说明 |
|---|---|---|
| 轮询 + 本地通知 | `service/PollingService.kt`、`service/PollingServiceStarter.kt`、`App.kt`、`NotificationUtil.kt`、`AndroidManifest.xml` | 前台服务定时拉消息列表，新消息用本地通知弹出；间隔可在设置页选 30 秒 ~ 6 小时，默认 5 分钟 |
| 保活 | `service/PollingServiceStarter.kt`、`AndroidManifest.xml` | 开机自启、网络恢复重启；最近任务隐藏参考 [gkd-kit/gkd](https://github.com/gkd-kit/gkd) 的做法（`setExcludeFromRecents` 在 `onCreate` 应用），规避 ColorOS 上"退出后隐藏、再从图标启动白屏"的问题 |
| 登录失效自检 | `service/PollingService.kt`、`MainActivity.kt` | 轮询连续 3 次接口异常判定会话过期：清登录态、弹通知、打开 App 回登录页 |
| 设置页 | `ui/compose/page/main/SettingPage.kt`、`store/SettingStore.kt` | 新增「保活增强」「隐藏最近任务」「轮询间隔」三个入口，沿用 App 自带的开关样式；顺带修掉"退出登录只清存储不清内存"导致的假登录 |
| 构建 | `gradle.properties`、`gradle/wrapper/gradle-wrapper.properties` | Gradle 7.0.2 → 7.3.3（兼容 JDK 17 环境）、kapt 在 Java 9+ 需要 `--add-exports` |

未改动：服务端、iOS 端、快应用端、MI push 相关代码（保留原逻辑，只是不再依赖它）。

## 构建

本机只有 Windows PowerShell 5.1（没有 pwsh 7），所以用 `powershell.exe` 跑：

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1                        # 用 build.gradle 里现有 versionName
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -Version 1.1-alpha-12  # 改版本号并出包
powershell -NoProfile -ExecutionPolicy Bypass -File .\build.ps1 -Clean                 # 清缓存重编
```

脚本会自动找 JDK 11 和 Android SDK，产物投递到仓库同级的 `发布\`，并打印 versionName / 体积 / SHA256。

注意：`build.ps1` 必须保持 **UTF-8 带 BOM** 保存，否则 PowerShell 5.1 会把中文读成乱码（脚本会去找不存在的路径）。改动脚本后可用下面一行补回 BOM：

```powershell
$p = ".\build.ps1"; [IO.File]::WriteAllText($p, [IO.File]::ReadAllText($p, [Text.Encoding]::UTF8), (New-Object Text.UTF8Encoding $true))
```

依赖（一次性准备）：

- **JDK 11**：必须 11，Kotlin 1.5.31 的 kapt 在 JDK 17 上会 `module java.compiler does not export` 报错。
- **Android SDK**：`platforms;android-31`、`build-tools;30.0.2`、`platform-tools`。
- **Gradle 缓存**：默认放仓库同级的 `.gradle-home`；首次构建需要联网拉 Gradle 7.3.3 和依赖，之后可 `--offline`。

## 签名（重要）

`android/key_debug.jks` 和 `android/key_debug.properties` 是 APK 签名凭据，**已被 `.gitignore` 排除，也不要提交到任何仓库**。

- 手机上的覆盖安装必须用**同一个 keystore**，换签名会直接安装失败（"签名不一致"）。
- 证书 SHA-256（当前）：`cecc06d0f1548dbb5ed27c1341fed41e9033b9588543a56559051c55b84c5cd5`
- 换新 keystore 意味着已装用户必须先卸载，数据会丢。

微信登录不可用也是签名问题——微信开放平台绑定的是原作者签名，重打包后校验必然失败。请用 **Sign in with Apple** 登录（走苹果 OAuth 网页流程，与 APK 签名无关，不需要 iPhone，有 Apple ID 即可）。

## 版本号约定

- `versionName` 在 `android/app/build.gradle`，当前 `1.1-alpha-11`。
- `versionCode` 保持 15：既方便覆盖安装，也方便回退到旧包（versionCode 一旦提高，旧包就装不回去了）。
- 发布文件名按 `PushDeer-轮询通知版-<versionName>.apk`，历史包放 `发布\旧版本\`。

## 已知限制

- **不是秒推**：轮询间隔决定延迟，预警场景 5 分钟够用；要秒级就调到 30 秒，代价是耗电（一天 2880 次请求）。
- **必须保活**：ColorOS / MIUI 等对后台限制严格，需在系统设置里给 PushDeer 开"自启动、后台运行、关闭电池优化"，并在最近任务里锁定（若关闭了「隐藏最近任务」开关）。
- **登录会过期**：Apple token 失效后必须手动重新授权一次，这是系统安全机制，无法后台自动完成；自检机制只把"发现失效"缩到最长 15 分钟内。

## 许可证

上游 LICENSE 只有一行：**禁止商用**（包括搭建后挂广告或售卖会员、打包后上架商店销售等），非商业用途采用 GPLv2。这个 fork 沿用同一许可，自用可以，别打包上架商店。

## 发布到自己的仓库

当前这份是**浅克隆**（`--depth 1`），直接 push 会被拒（`shallow update not allowed`），先补全历史：

```powershell
git fetch --unshallow origin                                    # 1) 补成完整历史
git remote rename origin upstream                               # 2) 原上游改名 upstream
git remote add origin https://gitee.com/<你的名字>/pushdeer.git  # 3) origin 指向自己的仓库
git push -u origin main                                         # 4) 推上去
git tag -a v1.1-alpha-11 -m "polling + local notification"      # 5) 可选打标签
git push origin v1.1-alpha-11
```

如果 `--unshallow` 因为网络失败，用仓库旁的补丁在新克隆里重放：

```powershell
git clone https://gitee.com/<你的名字>/pushdeer.git
cd pushdeer
git am ..\pushdeer-fork-patches\*.patch
```

## 同步上游

```powershell
git remote add upstream https://gitee.com/easychen/pushdeer.git   # 只加一次
git fetch upstream
git switch main
git rebase upstream/main        # 冲突主要集中在 App.kt / MainActivity.kt / SettingPage.kt
```
