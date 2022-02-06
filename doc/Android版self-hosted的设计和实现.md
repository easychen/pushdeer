# 问题

小米推送不再对个人开放，自架系统的推送权限申请很麻烦。公开secret会对官方应用带来安全风险。

# 解决思路

采用websocket来实现自架版的推送。

优点：

- 不受推送服务商限制，可以按自己需求随意推送

缺点：

- 无法实现后台推送，应用必须常驻后台。但考虑到MiPush在非小米系手机上也有同样的问题，也不是不能接受。

# 实现方案

## 架构选型

1. 暂定使用 socket.io V3 版本作为 websocket 的技术栈。


## 暂定流程

1. 在界面输入API endpoint时，以endpoint MD5值为Key查询本地存储获取之前（可能）存在的 device token，如不存在随机创建一个并写入到本地存储
1. 在注册设备时，检测是否为自架模式，如是则读取endpoint对应的device token。
1. 客户端以 device token 为 key 和服务器端的 socket 通信
1. API在推送Android类型的message时，检测是API endpoint，如果发现是自架host（非*.pushdeer.com），则将信息发送到 socket server 对应的channel上