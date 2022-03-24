FROM node:16-alpine3.15

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN sed -i 's/dl-4.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories
RUN apk --no-cache add mosquitto mosquitto-clients
RUN npm install -g forever

ADD mosquitto.conf /mosquitto.conf
RUN /usr/sbin/mosquitto -c /mosquitto.conf -v -d 

# 测试时注释掉下一行
COPY api /api

COPY init.sh /init.sh
RUN chmod +x /init.sh
EXPOSE 1883
EXPOSE 9001
EXPOSE 80

ENTRYPOINT ["/bin/sh", "/init.sh"]


# ENTRYPOINT ["/usr/sbin/mosquitto", "-c", "/mosquitto.conf",""]