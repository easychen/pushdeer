# webdevops/php-apache:8.0（PHP 8.0.30 FPM, Debian 10）+ 官方 CVE-2026-109618/109619 补丁
# 补丁来源：https://github.com/php/php-src/tree/PHP-8.0-CVE-2026-109618-109619 （提交 82f8f31b）
# 只重新编译 PHP 本体（php / php-fpm / 内置共享扩展），webdevops 自带的 pecl 扩展（redis、imagick 等）保持不动：
# 补丁未改动结构体和已有函数签名，ABI 兼容。
FROM webdevops/php-apache:8.0

COPY php80-cve-2026-109618-109619.patch /usr/src/php-cve.patch

RUN set -eux; \
	# buster 已停止支持，改用官方归档源；构建期间临时停用其他源
	cp /etc/apt/sources.list /tmp/sources.list.orig; \
	mkdir -p /tmp/sources.list.d.orig; mv /etc/apt/sources.list.d/* /tmp/sources.list.d.orig/ 2>/dev/null || true; \
	printf 'deb http://archive.debian.org/debian buster main\ndeb http://archive.debian.org/debian-security buster/updates main\n' > /etc/apt/sources.list; \
	apt-get -o Acquire::Check-Valid-Until=false update; \
	dpkg-query -W -f '${Package}\n' | sort > /tmp/pkgs.before; \
	apt-get install -y --no-install-recommends \
		$PHPIZE_DEPS patch \
		libargon2-dev libcurl4-openssl-dev libonig-dev libreadline-dev libsodium-dev \
		libsqlite3-dev libssl-dev libxml2-dev zlib1g-dev; \
	dpkg-query -W -f '${Package}\n' | sort > /tmp/pkgs.after; \
	docker-php-source extract; \
	cd /usr/src/php; \
	patch -p1 < /usr/src/php-cve.patch; \
	export CFLAGS="$PHP_CFLAGS" CPPFLAGS="$PHP_CPPFLAGS" LDFLAGS="$PHP_LDFLAGS"; \
	eval "./configure $(php-config --configure-options)"; \
	make -j "$(nproc)"; \
	find -type f -name '*.a' -delete; \
	make install; \
	strip --strip-all /usr/local/bin/php /usr/local/sbin/php-fpm || :; \
	make clean; \
	cd /; \
	docker-php-source delete; \
	# 只删除本次为编译新装的包，不动 webdevops 原有的运行时依赖
	comm -13 /tmp/pkgs.before /tmp/pkgs.after | xargs -r apt-get purge -y; \
	rm -rf /var/lib/apt/lists/* /usr/src/php-cve.patch; \
	cp /tmp/sources.list.orig /etc/apt/sources.list; mv /tmp/sources.list.d.orig/* /etc/apt/sources.list.d/ 2>/dev/null || true; \
	echo "8.0.30+CVE-2026-109618-109619" > /usr/local/etc/php-cve-patched; \
	php --version; php-fpm -v

# CVE-2026-109619 官方缓解 + 隐藏版本号
RUN printf 'session.upload_progress.enabled = 0\nexpose_php = Off\n' > /usr/local/etc/php/conf.d/zz-cve-hardening.ini

# 信任库：移除被攻破的 emSign ECC Root CA - G3（2026-10 eMudhra 事件）
# 为什么必须在镜像里做：buster 已归档，apt 里的 ca-certificates 停在事件之前，装/升都去不掉这张根；
# 只在运行中的容器里删、或把宿主目录挂进来，换镜像/换机器就丢，等于没修。
RUN set -eux; \
    if [ -f /usr/share/ca-certificates/mozilla/emSign_ECC_Root_CA_-_G3.crt ]; then \
        rm -f /usr/share/ca-certificates/mozilla/emSign_ECC_Root_CA_-_G3.crt; \
        sed -i '/emSign_ECC_Root_CA_-_G3\.crt/s/^/#/' /etc/ca-certificates.conf; \
        update-ca-certificates --fresh; \
    fi; \
    test "$(openssl crl2pkcs7 -nocrl -certfile /etc/ssl/certs/ca-certificates.crt \
            | openssl pkcs7 -print_certs -noout | grep -ci 'emSign ECC Root CA - G3')" = "0"; \
    test "$(grep -c 'BEGIN CERTIFICATE' /etc/ssl/certs/ca-certificates.crt)" -gt 100; \
    test "$(ls /etc/ssl/certs | grep -c 'emSign_ECC_Root_CA_-_G3')" = "0"

LABEL org.opencontainers.image.title="webdevops-php-8.0-30-fix" \
      org.opencontainers.image.description="webdevops/php-apache:8.0 + CVE-2026-109618/109619; CA without emSign G3" \
      org.opencontainers.image.ca-fix="emSign-ECC-Root-CA-G3-removed" \
      org.opencontainers.image.ca-fix-date="2026-10-23"
