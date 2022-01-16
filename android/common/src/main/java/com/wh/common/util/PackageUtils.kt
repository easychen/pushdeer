package com.wh.common.util

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import com.wh.common.typeExt.isUserPackage


object PackageUtils {

    var manager: PackageManager? = null

//    @SuppressLint("QueryPermissionsNeeded")
//    fun getAllPackages(): Flow<PackageInfo> {
//        return manager!!.getInstalledPackages(0).asFlow()
//    }

    fun getUserPackages(packageManager: PackageManager): MutableList<PackageInfo> {
        val tmp = mutableListOf<PackageInfo>()
        packageManager.getInstalledPackages(0).forEach {
            if (it.isUserPackage()) {
                tmp.add(it)

                Log.d("WH_", "getUserPackages: ${it.packageName}")
            }
        }
        return tmp
    }

//    fun getSystemPackages(): Flow<PackageInfo> {
//        val tmp = mutableListOf<PackageInfo>()
//        manager!!.getInstalledPackages(0).forEach {
//            if (it.isSystemPackage()){
//                tmp.add(it)
//            }
//        }
//        return tmp.asFlow()
//    }

    private fun getApplicationInfoByPackageName(packageName: String): ApplicationInfo {
        return manager!!.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }

    fun getApplicationLabel(packageName: String): CharSequence {
        return try {
            manager!!.getApplicationLabel(getApplicationInfoByPackageName(packageName))
        } catch (e: Exception) {
            packageName
        }.also {
            Log.d("WH_", "getApplicationLabel: $packageName $it")
        }
//        return getApplicationInfoByPackageName(packageName).loadLabel(manager!!).also {
//            Log.d("WH_", "getApplicationLabel: $it")
//        }
    }

    fun getApplicationIcon(pkgName: String): Drawable {
        return getApplicationInfoByPackageName(pkgName).loadIcon(manager!!)
    }
}