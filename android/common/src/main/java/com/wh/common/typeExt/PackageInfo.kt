package com.wh.common.typeExt

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.wh.common.data.app.AppInfo

fun PackageInfo.toAppInfo(packageManager: PackageManager): AppInfo {
    return AppInfo(
        packageName = packageName,
        icon = applicationInfo.loadIcon(packageManager).foreground(),
        label = applicationInfo.loadLabel(packageManager).toString()
    )
}

fun PackageInfo.isUserPackage(): Boolean {
    return this.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) == 0
}

fun PackageInfo.isSystemPackage(): Boolean {
    return this.applicationInfo.flags.and(ApplicationInfo.FLAG_SYSTEM) != 0
}