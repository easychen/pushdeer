package com.pushdeer.common.api.data.response

import com.pushdeer.common.api.data.request.DeviceInfo


class DeviceInfoList{
    var devices:List<DeviceInfo> = emptyList()

    override fun toString(): String {
        return "devices:$devices"
    }
}