package com.brotherjing.danmakubay.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by ljj on 2014/11/29.
 * 数据工具：
 * 1.SharePreference对简单数据的存储的封装
 */
public class DataUtil {
    private static Context context;

    private static SharedPreferences getSharedPreferences(String spfName) {
        return context.getSharedPreferences(spfName, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        DataUtil.context = context;

        String versionName = "";
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String spfName, String key) {
        return getSharedPreferences(spfName).getString(key, "");
    }

    public static String getString(String spfName, String key, String defaultValue) {
        return getSharedPreferences(spfName).getString(key, defaultValue);
    }

    public static long getLong(String spfName, String key, long defValue) {
        return getSharedPreferences(spfName).getLong(key, defValue);
    }

    public static boolean getBoolean(String spfName, String key, boolean defVal) {
        return getSharedPreferences(spfName).getBoolean(key, defVal);
    }

    /**
     * 下面这种成对接口主要是同步异步的差别, force后缀的是立即生效
     */
    public static void putString(String spfName, String key, String value) {
        getSharedPreferences(spfName).edit().putString(key, value).apply();
    }

    public static void putStringForce(String spfName, String key, String value) {
        getSharedPreferences(spfName).edit().putString(key, value).commit();
    }

    public static void putLong(String spfName, String key, long value) {
        getSharedPreferences(spfName).edit().putLong(key, value).apply();
    }

    public static void putLongForce(String spfName, String key, long value) {
        getSharedPreferences(spfName).edit().putLong(key, value).commit();
    }

    public static void putBoolean(String spfName, String key, boolean value) {
        getSharedPreferences(spfName).edit().putBoolean(key, value).apply();
    }

    public static void putBooleanForce(String spfName, String key, boolean value) {
        getSharedPreferences(spfName).edit().putBoolean(key, value).commit();
    }

    public static void clearSpf(String spfName) {
        getSharedPreferences(spfName).edit().clear().commit();
    }
}
