# PushDeer-快应用

PushDeer 是一个开放源码的无 App 推送服务

这里是 PushDeer 的 `快应用` 端的源码

![演示](https://s2.loli.net/2022/02/22/4vMP7VrutciCdw6.gif)

---
## 支持明细

厂商|支持|备注
-|-|-
华为	| ❌	| 华为快应用与联盟快应用属于不同体系，暂未完全适配
一加	| ✅	| -
小米	| ✅ | -
> 其他机型未进行测试

## TODO
- [ ] 添加自架支持
- [ ] 接入MiPush消息接收
- [ ] 优化界面显示效果
---



## 环境准备
- node环境(开发使用的版本是v16.13.1)
- [快应用IDE](https://www.quickapp.cn/docCenter/post/97)
- adb
- [调试器](https://www.quickapp.cn/docCenter/post/69)
- Android手机一台

## 配置项修改
> 由于快应用编译器貌似不支持从.env文件加载相关配置，此步骤通过手工进行

- 修改后端API地址

编辑`src/helper/api.js`文件，修改`baseUrl`常量为当前使用的API地址

- 修改包名

编辑`src/manifest.js`文件，修改`package`参数为目标包名
> 包名关系到各厂商的应用申请，及微信平台应用信息

- 修改微信开放平台APPID

编辑`src/manifest.js`文件，修改`features/{"name": "service.wxaccount"}`下的`appId`参数为微信开放平台ID
> 需要与微信开放平台的应用匹配，否则无法使用微信登录

## 编译及运行
- 克隆源码
- 安装依赖
```bash
cd quickapp && yarn
```
- 启动快应用IDE
启动IDE，打开quickapp文件夹
![快应用IDE界面](https://s2.loli.net/2022/02/22/ymgavTXHZ6zDLKx.png)  
IDE打开项目后，会自动编译执行，并把界面展示到模拟器
> 模拟器结果会与真机有所出入，请以真机调试效果为准

## 运行与调试
- 真机预览

`IDE`中，点击模拟器上方二维码按钮，弹出当前二维码
![预览二维码](https://s2.loli.net/2022/02/22/f75jwbEI9y8RJHl.png)  
手机保持与PC在同一网络，打开`快应用调试器`，点击`扫码安装`
<img src="https://s2.loli.net/2022/02/22/hBGlUcEKsw1fjz3.jpg" height='400px'>
- 真机调试

运行成功后，回到`快应用调试器`，点击`开始调试`，PC端会自动打开调试器，可以查看预览及日志等信息

![调试](https://s2.loli.net/2022/02/22/ZFghDiBWTRa42yd.png)  

> 若调试界面不弹出，请多试几次
> 调试界面容易卡死不显示最新日志，清关闭调试界面，重新在手机端开始调试

## 发布上线流程
基本步骤如下：
- 注册快应用及各厂商开发者账号
- 绑定厂商开发者账号
- 生成证书(证书路径位于`/sign/release` 及`/sign/debug`目录下)
- 获取证书指纹
- 在各厂商开放平台创建应用、完成审核

详细操作，请移步[快应用官网](https://www.quickapp.cn/docCenter)
![picture 5](https://s2.loli.net/2022/02/22/wGhKtBiNAmbjCls.png)  
