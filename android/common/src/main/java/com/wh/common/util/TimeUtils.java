package com.wh.common.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class TimeUtils {
    public static final String fullDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String yMdTimeFormat = "yyyy-MM-dd";


    /**
     * @return 返回格式化后的字符串
     * @describe 将长整型的时间戳转换成"yyyy-MM-dd"格式的时间
     * @args MS long 长整型的毫秒时间戳
     */
    public static String getFormattedTime(Long MS) {
        return genDateFormat(fullDateTimeFormat).format(new Date(MS));
    }

    /**
     * @return 返回格式化后的字符串
     * @describe 将长整型的时间戳转换成自定义的格式
     * @args MS long 长整型的毫秒时间戳
     * format String 格式
     */
    public static String getFormattedTime(long MS, String format) {
        return genDateFormat(format).format(new Date(MS));
    }

    /**
     * @return Long 返回转换后的长整型时间戳
     * @describe 将格式化的时间戳字符串转换成长整型的毫秒时间戳
     * @args formattedTime String 格式化的时间戳
     */
    public static long getMSFromFormattedTime(String formattedTime) throws ParseException {
        Date date = genDateFormat(fullDateTimeFormat).parse(formattedTime);
        if (date != null) {
            return date.getTime();
        } else {
            return 0L;
        }
    }

    public static String getTodayDateYMD() {
        return getFormattedTime(System.currentTimeMillis(), "yyyy-MM-dd");
    }

    /**
     * @return 返回格式数据结构
     * @describe 输入格式字符串, 返回格式的数据结构
     * @args stringFormat String 格式字符串
     */
    public static SimpleDateFormat genDateFormat(String stringFormat) {
        return new SimpleDateFormat(stringFormat, Locale.getDefault());
    }

    public static long utcTS2ms(String utcTS, String utcFMT) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(utcFMT);
        Calendar calendar = Calendar.getInstance();
        Date date = sdf.parse(utcTS);
        assert date != null;
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + tz2utcMSOffset()/3600000);
        return calendar.getTime().getTime();
    }

    public static String msTSDis(long now,long then){
        long dis = Math.abs(now-then);
        if (dis<60000){
            return dis/1000+"s ago";
        }else if (dis<3600000){
            return dis/60000+"min ago";
        }else if (dis<86400000){
            return dis/3600000+"h ago";
        }else {
            return getFormattedTime(then,"yyyy-MM-dd HH:mm:ss");
        }
    }

    public static int tz2utcMSOffset(){
        return TimeZone.getDefault().getOffset(System.currentTimeMillis());
    }


    /**
     * @return String 返回日期
     * @describe 输入星期的日期 返回汉字
     * @args num int 输入的日期
     */
    public static String num2Chinese(int num) {
        String[] c = {"六", "日", "一", "二", "三", "四", "五"};
        if (num > -1 && num < c.length) {
            return c[num];
        } else {
            return c[0];
        }
    }
}