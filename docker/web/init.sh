#!/bin/bash

# 初始化 laravel
cd /app/api && composer install && cp -n .env.example .env && php artisan key:generate && php artisan migrate --seed

chmod -R 0777 /app/api/storage
chmod -R 0777 /app/api/bootstrap/cache/



# 启动 proxy
# 已经设置为 deamon
# cd /app/proxy && ./server-linux &
