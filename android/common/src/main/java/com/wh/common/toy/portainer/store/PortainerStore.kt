package com.wh.common.toy.portainer.store

import android.content.Context
import com.wh.common.store.IStore
import com.wh.common.store.Store
import com.wh.common.store.asStoreProvider
import java.net.Proxy

class PortainerStore(context: Context): IStore {
    private val store = Store(
        context.getSharedPreferences(
            "portainer",
            Context.MODE_PRIVATE
        ).asStoreProvider()
    )

    companion object {
        const val KEY_ENABLE_PORTAINER_MONITOR = "enable-portainer-monitor"

        const val KEY_ADMIN_USERNAME = "admin-username"
        const val KEY_ADMIN_PASSWORD = "admin-password"
        const val KEY_PORTAINER_BASE_URL = "portainer-base-url"
        const val KEY_USE_PROXY = "use-proxy"
        const val KEY_PROXY_TYPE = "proxy-type"
        const val KEY_PROXY_HOST = "proxy-host"
        const val KEY_PROXY_PORT = "proxy-port"

        const val KEY_AUTH_TOKEN = "auth-token"
        const val KEY_AUTH_EXPIRE_TIMESTAMP = "auth-expire-timestamp"
    }

    var enablePortainerMonitor by store.boolean(KEY_ENABLE_PORTAINER_MONITOR, true)

    var adminUsername by store.string(KEY_ADMIN_USERNAME, "admin")
    var adminPassword by store.string(KEY_ADMIN_PASSWORD, "1qaz2wsx")
    var portainerBaseUrl by store.string(KEY_PORTAINER_BASE_URL, "http://192.168.51.99:9000")
    var useProxy by store.boolean(KEY_USE_PROXY, true)
    var proxyType by store.int(KEY_PROXY_TYPE, 1)
    var proxyHost by store.string(KEY_PROXY_HOST, "192.168.50.83")
    var proxyPort by store.int(KEY_PROXY_PORT, 1080)
    var authToken by store.string(KEY_AUTH_TOKEN, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsInJvbGUiOjEsImV4cCI6MTYzOTAwMjAxOH0.yM1WjR4esoaKGcJ00uGeVt5-eKgtb99DI5n2wjpP_zg")
    var authExpireTimestamp by store.long(KEY_AUTH_EXPIRE_TIMESTAMP, 0L)

    fun resolveProxyType(): Proxy.Type {
        return when (proxyType) {
            1 -> Proxy.Type.SOCKS
            2 -> Proxy.Type.HTTP
            else -> Proxy.Type.DIRECT
        }
    }
}