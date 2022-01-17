package com.pushdeer.os.data.api.data.response

import com.pushdeer.os.data.api.data.request.DeviceInfo


class DeviceInfoList{
    var devices:List<DeviceInfo> = emptyList()

    override fun toString(): String {
        return "devices:$devices"
    }
}