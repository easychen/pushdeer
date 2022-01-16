package com.wh.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionCodeUtil {
    /**
     * 获取版本号
     *
     * @param context Context
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        PackageInfo pi;
        int code = -1;
        PackageManager pm = context.getPackageManager();
        try {
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            code = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }
}
