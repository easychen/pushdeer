package com.pushdeer.os.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created zmm
 * <p>
 * Functions: 设置状态栏透明并改变状态栏颜色为深色工具类
 */

public class StatusBarUtils {

    public static int getStatusBarHeight(Resources resources,Context context) {
        int statusBarHeight = 0;
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        Log.d("CompatToolbar", "状态栏高度：" + px2dp(statusBarHeight,context) + "dp");
        return statusBarHeight;
    }

    public static float px2dp(float pxVal, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }


    public static void setStatusBarFontIconDark(Window window,boolean dark) {
        // 小米MIUI
        try {
            Class clazz = window.getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 魅族FlymeUI
        try {
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                window.getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }


    /**
     * 设置魅族手机状态栏图标颜色风格
     * <p>
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {

        boolean result = false;

        if (window != null) {

            try {

                WindowManager.LayoutParams lp = window.getAttributes();

                Field darkFlag = WindowManager.LayoutParams.class

                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");

                Field meizuFlags = WindowManager.LayoutParams.class

                        .getDeclaredField("meizuFlags");

                darkFlag.setAccessible(true);

                meizuFlags.setAccessible(true);

                int bit = darkFlag.getInt(null);

                int value = meizuFlags.getInt(lp);

                if (dark) {

                    value |= bit;

                } else {

                    value &= ~bit;

                }

                meizuFlags.setInt(lp, value);

                window.setAttributes(lp);

                result = true;

            } catch (Exception e) {

            }

        }

        return result;

    }

    /**
     * 设置小米手机状态栏字体图标颜色模式，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {

        boolean result = false;

        if (window != null) {

            Class clazz = window.getClass();

            try {

                int darkModeFlag = 0;

                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");

                darkModeFlag = field.getInt(layoutParams);

                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);

                if (dark) {//状态栏透明且黑色字体

                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);

                } else {//清除黑色字体

                    extraFlagField.invoke(window, 0, darkModeFlag);

                }

                result = true;

            } catch (Exception e) {

            }

        }

        return result;

    }

    /**
     * 在不知道手机系统的情况下尝试设置状态栏字体模式为深色
     * <p>
     * 也可以根据此方法判断手机系统类型
     * <p>
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0 0:设置失败
     */

    public static void statusBarLightMode(Activity activity) {

        int result = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {

//result = 1;

                StatusBarLightMode(activity, 1);

            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {

//result = 2;

                StatusBarLightMode(activity, 2);

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

//activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//result = 3;

                StatusBarLightMode(activity, 3);

            }

        }

    }

    /**
     * 已知系统类型时，设置状态栏字体图标为深色。
     * <p>
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */

    public static void StatusBarLightMode(Activity activity, int type) {

        if (type == 1) {

            MIUISetStatusBarLightMode(activity.getWindow(), true);

        } else if (type == 2) {

            FlymeSetStatusBarLightMode(activity.getWindow(), true);

        } else if (type == 3) {

            Window window = activity.getWindow();

            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

    }

    /**
     * 已知系统类型时，清除MIUI或flyme或6.0以上版本状态栏字体深色模式
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */

    public static void StatusBarDarkMode(Activity activity, int type) {

        if (type == 1) {

            MIUISetStatusBarLightMode(activity.getWindow(), false);

        } else if (type == 2) {

            FlymeSetStatusBarLightMode(activity.getWindow(), false);

        } else if (type == 3) {

            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        }

    }

    /**
     * 状态栏背景透明
     *
     * @param activity
     */

    public static void StatusBarTransport(Activity activity) {

        Window window = activity.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS

                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            window.setStatusBarColor(Color.TRANSPARENT);

            window.setNavigationBarColor(Color.TRANSPARENT);

        }

    }

}