package com.xds.project.util;

import android.content.Context;

public class SPUtils {
    public static boolean isLogin(Context context) {
        return BaseSharePreferencesUtils.getBoolean(context, "login", false);
    }

    public static void setLogin(Context context, boolean isLogin) {
        BaseSharePreferencesUtils.putBoolean(context, "login", isLogin);
    }

    public static boolean isFirst(Context context) {
        return BaseSharePreferencesUtils.getBoolean(context, "first", false);
    }

    public static void setFirst(Context context, boolean isLogin) {
        BaseSharePreferencesUtils.putBoolean(context, "first", isLogin);
    }

    public static boolean isGrade(Context context) {
        return BaseSharePreferencesUtils.getBoolean(context, "grade", false);
    }

    public static void setGrade(Context context, boolean isLogin) {
        BaseSharePreferencesUtils.putBoolean(context, "grade", isLogin);
    }

    public static void setLoginConfig(Context context, String userid, String userName, String passwd, String mobile) {
        BaseSharePreferencesUtils.putString(context, "userid", userid, "userName", userName,
                "passwd", passwd, "mobile", mobile);
    }

    public static void setLoginConfig(Context context, String userid, String passwd) {
        BaseSharePreferencesUtils.putString(context, "userid", userid,
                "passwd", passwd);
    }

    public static String getUserName(Context context) {
        return BaseSharePreferencesUtils.getString(context, "userName", "");
    }

    public static String getUserId(Context context) {
        return BaseSharePreferencesUtils.getString(context, "userid", "");
    }

    public static String getUserPsd(Context context) {
        return BaseSharePreferencesUtils.getString(context, "passwd", "");
    }

    public static String getMobile(Context context) {
        return BaseSharePreferencesUtils.getString(context, "mobile", "");
    }

    public static void setSubject(Context context, String type) {
        BaseSharePreferencesUtils.putString(context, "subject", type);
    }

    public static void setUserName(Context context, String type) {
        BaseSharePreferencesUtils.putString(context, "userName", type);
    }

    public static void setDept(Context context, String type) {
        BaseSharePreferencesUtils.putString(context, "userName", type);
    }

    public static String getSubject(Context context) {
        return BaseSharePreferencesUtils.getString(context, "subject", "");
    }

    public static void setId(Context context, String type) {
        BaseSharePreferencesUtils.putString(context, "id", type);
    }

    public static String getId(Context context) {
        return BaseSharePreferencesUtils.getString(context, "id", "");
    }
}
