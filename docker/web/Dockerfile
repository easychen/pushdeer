FROM webdevops/php-apache:8.0

# 首先配置 vhost
COPY vhost.conf /opt/docker/etc/httpd/vhost.conf
# COPY web.vhost.conf /opt/docker/etc/httpd/vhost.common.d/

# 然后运行初始化脚本
# https://dockerfile.readthedocs.io/en/latest/content/Customization/provisioning.html
COPY init.sh /opt/docker/provision/entrypoint.d/
#CMD chmod +x /opt/docker/provision/entrypoint.d/init.sh
RUN echo "session.save_handler = redis\n" >> /opt/docker/etc/php/php.webdevops.ini
RUN echo "session.save_path = 'tcp://redis:6379'\n" >> /opt/docker/etc/php/php.webdevops.ini
RUN echo "session.gc_maxlifetime = '259200'\n" >> /opt/docker/etc/php/php.webdevops.ini

# ADD supervisord-proxy.conf /opt/docker/etc/supervisor.d/prism-proxy.conf
RUN mkdir /data
COPY gorush /data/gorush
RUN chmod +x /data/gorush 

ADD supervisord-ios.conf /opt/docker/etc/supervisor.d/push-ios.conf
ADD supervisord-clip.conf /opt/docker/etc/supervisor.d/push-clip.conf

ADD larave-cron /etc/cron.d
RUN chmod +x /etc/cron.d/larave-cron 

# 配置 https
# 在本目录下创建ssl目录，放入证书（server.crt，server.key），然后去掉下一行的注释
# ADD ssl /app/ssl

EXPOSE 80 

