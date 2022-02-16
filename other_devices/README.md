# 使用MQTT接受PushDeer推送的消息

PushDeer自架版支持通过MQTT协议向兼容的设备（以下简称设备）发送信息，其主要工作原理是：

1. 自架版docker-compose文件中预置了MQTT服务器，手动开启后，设备可以通过配置的端口连接到服务器
1. 设备通过订阅主题实时获得消息，文字类型（text/markdown）消息主题为：`{{pushkey}}_text`， 图片类型的主题为`{{pushkey}}_bg_url`

## 开启MQTT服务

修改根目录下的 `docker-compose.self-hosted.yml`:

```yml
  app:
    #image: 'webdevops/php-apache:8.0-alpine'
    build: './docker/web/'
    ports:
      - '8800:80'
    volumes:
      - './:/app'
    depends_on:
      - mariadb
      - redis
    environment:
      - DB_HOST=mariadb
      - DB_PORT=3306
      - DB_USERNAME=root
      - DB_DATABASE=pushdeer
      - DB_PASSWORD=theVeryp@ssw0rd
      - GO_PUSH_IOS_TOPIC=com.pushdeer.self.ios
      - GO_PUSH_IOS_CLIP_TOPIC=com.pushdeer.self.ios.Clip
      - APP_DEBUG=false
      - MQTT_API_KEY=9LKo3
      - MQTT_ON=false <---- 这里改为 true 
      # 下边去掉注释
  # mqtt:
  #   image: 'ccr.ccs.tencentyun.com/ftqq/pushdeeresp'
  #   ports:
  #     - '1883:1883'
  #   environment:
  #     - API_KEY=9LKo3 <---- 这里和上边的MQTT_API_KEY值一致
  #     - MQTT_PORT=1883
  #     - MQTT_USER=easy <---- 自己起一个用户名
  #     - MQTT_PASSWORD=y0urp@ss <---- 自己起一个密码
  #     - MQTT_BASE_TOPIC=default  
```

修改完成后类似：

```yml
      - MQTT_API_KEY=2134grt
      - MQTT_ON=true
  mqtt:
    image: 'ccr.ccs.tencentyun.com/ftqq/pushdeeresp'
    ports:
      - '1883:1883'
    environment:
      - API_KEY=2134grt
      - MQTT_PORT=1883
      - MQTT_USER=mynameisLili
      - MQTT_PASSWORD=howoldareU
      - MQTT_BASE_TOPIC=default  
```

再通过以下命令启动服务：

```bash
docker-compose -f docker-compose.self-hosted.yml up --build -d
``` 

## 连接示例

这里以上边的设置为例，详细说明MQTT连接中用到的值，假设你的`Pushkey` 为 `PDU01234`，那么：

1. MQTT服务器的IP为你架设PushDeer服务的IP，注意127.0.0.1和localhost是环回地址，如果想其他设备能连接，至少需要局域网IP（设备在同一个局域网里）或者公网IP。
1. MQTT用户名为：`mynameisLili` （MQTT_USER）
1. MQTT密码为：`howoldareU`（MQTT_PASSWORD）

有了上边三个信息，你就可以连接上MQTT服务了。如果你不想要访问权限控制，可以这样：

```yml
      - MQTT_USER=
      - MQTT_PASSWORD=
```

连上服务器以后，还需要知道`topic`才能接收消息，你可以把它当成消息分组名称，只有订阅了这个分组，才能收到对应的消息。

当设置 `MQTT_ON=true` 以后，在发送信息时，PushDeer会抄送一份给MQTT，这样连接着的设备就可以收到消息了。为了方便设备进行消息处理，我们分两个分组抄送信息：

1. text/markdown类型的消息会通过`PDU01234_text`发送
1. image类型的消息会通过`PDU01234_bg_url`发送


