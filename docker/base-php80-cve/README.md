# webdevops/php-apache:8.0 + CVE-2026-109618 / CVE-2026-109619 补丁

webdevops 已不再更新 8.0 镜像，PHP 8.0 也已 EOL，官方只提供源码补丁
（https://github.com/php/php-src/tree/PHP-8.0-CVE-2026-109618-109619 ，提交 `82f8f31b`）。
本目录在 `webdevops/php-apache:8.0`（PHP 8.0.30 FPM，Debian 10）上打补丁，只重新编译 PHP 本体（php / php-fpm / 内置共享扩展），
webdevops 自带的 pecl 扩展（redis、imagick 等）保持不动——补丁没有改结构体和已有函数签名，ABI 兼容。

## 构建

先构建补丁基础镜像，再用它构建 PushDeer 的 web 镜像：

```bash
docker build --platform linux/amd64 -t pushdeer-php80-cve-fix docker/base-php80-cve

# 用 compose 部署
docker compose -f docker-compose.self-hosted.yml build --build-arg BASE_IMAGE=pushdeer-php80-cve-fix
docker compose -f docker-compose.self-hosted.yml up -d

# 或者单独构建 web 镜像
docker build --build-arg BASE_IMAGE=pushdeer-php80-cve-fix docker/web
```

`docker/web/Dockerfile` 的 `BASE_IMAGE` 不传时默认为 `webdevops/php-apache:8.0`，**这个镜像没有打补丁**，只适合本地开发。

Debian 10 (buster) 已停止维护，构建时软件源用 `archive.debian.org`（国内慢，可加 `--build-arg http_proxy=...`）。

## 与原镜像的区别

- php / php-fpm 打了补丁（`/usr/local/etc/php-cve-patched` 内容为 `8.0.30+CVE-2026-109618-109619`）
- 新增 `conf.d/zz-cve-hardening.ini`：`session.upload_progress.enabled=0`、`expose_php=Off`
- 信任库删除 `emSign ECC Root CA - G3`（2026-10 eMudhra 事件）。buster 已归档，apt 里的 `ca-certificates` 去不掉这张根证书，所以在镜像里删掉，构建时会检查是否删干净
- 扩展列表与原镜像完全一致

## 验证

```bash
docker exec pushdeer-app-1 sh -c 'php -v | head -1; cat /usr/local/etc/php-cve-patched'
```

## 注意

- `docker/web/Dockerfile.serverless` 仍基于 `webdevops/php-apache:8.0`，未切换：它在构建时用 buster 官方源 `apt-get install redis-server`，
  该源已下线，需要单独处理。
- `bitnami/redis` 旧标签已从 Docker Hub 下架，compose 改用 `bitnamilegacy/redis:6.0.16`。
