#!/bin/bash

# 初始化 laravel
cd /app/api && composer install && cp -n .env.example .env && php artisan key:generate && php artisan migrate --seed

mkdir -p /app/api/storage
chmod -R 0777 /app/api/storage

mkdir -p /app/api/bootstrap/cache/
chmod -R 0777 /app/api/bootstrap/cache/
