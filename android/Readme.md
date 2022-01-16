# PushDeer for Android



### 适配进度

* MiPush（状态：已调通、已接入）
  * miui 12.5.6 正常
  * 原生、类原生 可成功注册，无法收到推送，可能和地区识别有关，待解决

### TODO

* ~~调通 MiPush~~
* 接入 MiPush
* 完善 log ~~采集~~ 回传机制，便于调试
* 测试不同厂商设备上的通知推送效果
* ~~接入 PushDeer~~
* ~~界面设计：BottomBar+Navigation(Device Key Message Setting)~~
* 调整 KeyList MessageList 等处的自定义绘制
* ~~增加 DeviceList 外的侧滑手势~~
* ~~增加侧滑手势相关动作~~
* 增加各种操作前的二次确认弹窗，包括自动登陆
* 增加非Miui设备的权限获取

### 日志

* 2021-12-25
  * 文件夹已经创建完毕
  * MiPush
    * 小米官方文档写的像shit，陈年旧注释都不删
    * 测试环境推送消息不推送
    * 增加了 channel 参数，默认属于 运营，推送数量太少，需要申请新 channel
    * 因为不是所有SDK都支持透传，暂时选择跳过这个功能

* 2022-01-01
  * 增加图片、颜色等资源，抽取字符串
  * 增加登陆界面
  * 重新组织主题
  * 使用"纯自研" TopBar 替换 TopAppBar
  * PushDeer api已调通，未接入

* 2022-01-04
  * PushDeer /user/info 响应类型去掉列表
  * 为 MessageList 界面适配右上角箭头icon、增加输入框及其切换动画
  * 适配设置界面不显示右上角icon
  * MessageList、KeyList item适配
  * DeviceList 增加侧滑手势
  * 抽象 item 相关 ui，提高代码复用率

* 2022-01-05
  * 从 AppDatabase.kt 删除 message
  * 调通user/info device/list key/list message/list四个路由
  * 去掉message相关的各种repository、viewmodel等
  * 一些小修改

* 2022-01-06
  * 完善device/key/message的删除逻辑
  * 创建 KeyListPage.kt
  * 一些小修改

* 2022-01-08
  * 增加二维码扫描组件
  * 重绘设置界面
  * 增加日志列表界面
  * 调整滑动删除界面绘制逻辑和操作逻辑
  * 调整列表界面绘制逻辑
  * 增加消息列表对Markdown类型消息的渲染支持
  * 增加消息列表对Image类型消息的显示支持（demo级别，硬编码）
  * 调整Key列表中项目绘制逻辑
  * 一些细节变化

* 2022-01-09
  * 增加列表尾部占位符
  * 微调Key和Message列表项的构图
  * 微调滑动删除图标背景
  * 调整列表项阴影
  * 调整设置界面的按钮颜色
  * 界面整体配色几近处理完善
  * Markdown:
    * 增加链接解析支持
    * 增加图片显示支持
    * 增加html解析支持
    * 增加task-list解析支持
    * 增加table解析支持

* 2022-01-15
  * 增加 Message 的数据库表
  * 调整项目文件结构
  * 将数据库用作message列表的直接数据来源

### 感谢

https://github.com/taoweiji/MixPush