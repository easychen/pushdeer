package com.wh.common.toy.portainer.repository

import androidx.lifecycle.MutableLiveData
import com.wh.common.toy.portainer.data.Auth
import com.wh.common.toy.portainer.data.Portainer
import com.wh.common.toy.portainer.helper.PortainerHttpApiHelper
import com.wh.common.toy.portainer.store.PortainerStore

class PortainerRepository(private val portainerStore: PortainerStore) {
    var auth = Auth(
        portainerStore.authToken,
        portainerStore.authExpireTimestamp
    )

    val portainers = MutableLiveData<List<Portainer>>(listOf())
    private val portainersTmp = mutableListOf<Portainer>()

    var isLoading = MutableLiveData(false)
    var lastUpdateTimestamp = MutableLiveData(System.currentTimeMillis())

    suspend fun refreshContainers() {
        if (!portainerStore.enablePortainerMonitor) {
            return
        }
        isLoading.postValue(true)
        if (auth.isOutDate) {
            auth = try {
                PortainerHttpApiHelper.fetchPortainerAuth(
                    baseUrl = portainerStore.portainerBaseUrl,
                    username = portainerStore.adminUsername,
                    password = portainerStore.adminPassword,
                    proxyType = portainerStore.resolveProxyType(),
                    proxyHost = portainerStore.proxyHost,
                    proxyPort = portainerStore.proxyPort
                ).also {
                    portainerStore.authToken = it.jwt
                    portainerStore.authExpireTimestamp = it.expireTimestamp
                }
            } catch (e: Exception) {
                throw e
            }
        }
        portainersTmp.clear()
        PortainerHttpApiHelper.fetchPortainerEndpoints(
            portainerStore.portainerBaseUrl,
            auth.jwt,
            portainerStore.resolveProxyType(),
            portainerStore.proxyHost,
            portainerStore.proxyPort
        ).forEach {
            PortainerHttpApiHelper.fetchPortainerContainer(
                portainerStore.portainerBaseUrl,
                it.id,
                auth.jwt,
                portainerStore.resolveProxyType(),
                portainerStore.proxyHost,
                portainerStore.proxyPort
            )
                .forEach { portainer ->
                    portainersTmp.add(portainer)
                }
        }
        portainers.postValue(portainersTmp)
        isLoading.postValue(false)
        lastUpdateTimestamp.postValue(System.currentTimeMillis())
    }
}