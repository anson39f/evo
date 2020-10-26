package com.xds.project.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * @author xiaowg
 * @todo activity跳转工具
 * @date 2018/3/16.
 */

public class ActivityTools {

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivity(Context context, Class<?> nextActivity) {
        Intent intent = new Intent(context, nextActivity);
        context.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivityForResult(Activity activity, Class<?> nextActivity, int requestCode) {
        Intent intent = new Intent(activity, nextActivity);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startToNextActivityForResult(Fragment fragment, Class<?> nextActivity,
                                                    int requestCode) {
        Intent intent = new Intent(fragment.getContext(), nextActivity);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param context
     * @param nextActivity
     * @param key
     * @param value
     */
    public static void startToNextActivity(Context context, Class<?> nextActivity, String key, String value) {
        Intent intent = new Intent(context, nextActivity);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    public static void startToNextActivity(Context context, Class<?> nextActivity, String key,
                                           int value) {
        Intent intent = new Intent(context, nextActivity);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param activity
     * @param nextActivity
     * @param key
     * @param value
     * @param requestCode
     */
    public static void startToNextActivity(Activity activity, Class<?> nextActivity, String key, String value, int requestCode) {
        Intent intent = new Intent(activity, nextActivity);
        intent.putExtra(key, value);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivity(Context context, Class<?> nextActivity, String key, Serializable serializable) {
        Intent intent = new Intent(context, nextActivity);
        intent.putExtra(key, serializable);
        context.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivityForResult(Activity activity, Class<?> nextActivity, String key, Serializable serializable, int requestCode) {
        Intent intent = new Intent(activity, nextActivity);
        intent.putExtra(key, serializable);
        if (requestCode > 0)
            activity.startActivityForResult(intent, requestCode);
        else
            activity.startActivity(intent);
    }


    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivityForResult1(Activity activity, Class<?> nextActivity, String key, Parcelable parcelable, int requestCode) {
        Intent intent = new Intent(activity, nextActivity);
        intent.putExtra(key, parcelable);
        if (requestCode > 0)
            activity.startActivityForResult(intent, requestCode);
        else
            activity.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivity(Context context, Class<?> nextActivity, String[] keys, Serializable[] serializables) {
        Intent intent = new Intent(context, nextActivity);
        int length = keys.length;
        for (int i = 0; i < length; i++) {
            intent.putExtra(keys[i], serializables[i]);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivityForResult(Activity activity, Class<?> nextActivity, String[] keys, Serializable[] serializables, int requestCode) {
        Intent intent = new Intent(activity, nextActivity);
        int length = keys.length;
        for (int i = 0; i < length; i++) {
            intent.putExtra(keys[i], serializables[i]);
        }
        if (requestCode > 0)
            activity.startActivityForResult(intent, requestCode);
        else
            activity.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     aram dataPacket
     *                     数据包
     */
    public static void startToNextActivity(Context context, Class<?> nextActivity, String key, Parcelable parcelable) {
        Intent intent = new Intent(context, nextActivity);
        intent.putExtra(key, parcelable);
        context.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivityForResult(Activity activity, Class<?> nextActivity, String key, Parcelable parcelable, int requestCode) {
        Intent intent = new Intent(activity, nextActivity);
        intent.putExtra(key, parcelable);
        if (requestCode > 0)
            activity.startActivityForResult(intent, requestCode);
        else
            activity.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivity(Context context, Class<?> nextActivity, String[] keys, Parcelable[] parcelables) {
        Intent intent = new Intent(context, nextActivity);
        int length = keys.length;
        for (int i = 0; i < length; i++) {
            intent.putExtra(keys[i], parcelables[i]);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转到另外一个activity
     *
     * @param nextActivity 下一个Activity
     *                     param dataPacket
     *                     数据包
     */
    public static void startToNextActivityForResult(Activity activity, Class<?> nextActivity, String[] keys, Parcelable[] parcelables, int requestCode) {
        Intent intent = new Intent(activity, nextActivity);
        int length = keys.length;
        for (int i = 0; i < length; i++) {
            intent.putExtra(keys[i], parcelables[i]);
        }
        if (requestCode > 0)
            activity.startActivityForResult(intent, requestCode);
        else
            activity.startActivity(intent);
    }

}
