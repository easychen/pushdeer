#!/bin/sh

# 生成密码文件
echo "$MQTT_USER:$MQTT_PASSWORD" > '/mospass.txt'
mosquitto_passwd -U /mospass.txt

# 启动 express
forever start /api/index.js
# mosquitto -c /mosquitto.conf -v
mosquitto -c /mosquitto.conf 