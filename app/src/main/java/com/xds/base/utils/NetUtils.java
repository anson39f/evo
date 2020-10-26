package com.xds.base.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

/**
 *
 * 网络判断管理工具类
 */
public class NetUtils {


    private NetUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 检测网络是否连接
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null) {
                for (NetworkInfo ni : infos) {
                    if (ni.isAvailable() && ni.isConnected()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断当前网络连接状态
     *
     * @param context
     * @return
     */
//    public static int getNetworkState(Context context) {
//        if (!isNetConnected(context)) {
//            return NetWorkState.NETWORN_NONE;
//        }
//        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        // Wifi
//        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
//        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
//            return NetWorkState.NETWORN_WIFI;
//        }
//
//        // 3G
//        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
//        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
//            return NetWorkState.NETWORN_MOBILE;
//        }
//        return NetWorkState.NETWORN_UNKNOW;
//    }

    /**
     * 打开网络设置界面
     *
     * @param activity
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean networkCapabilities(Context context) {

        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);


        Network[] infos = cm.getAllNetworks();
        if (infos != null) {
            for (Network ni : infos) {
                NetworkInfo info = cm.getNetworkInfo(ni);
                if (info.isAvailable() && info.isConnected()) {
                    NetworkCapabilities nc = cm.getNetworkCapabilities(ni);
                    //通过如下这个判断，可以达到预期的效果
                    if (nc != null && nc.hasCapability(nc.NET_CAPABILITY_VALIDATED)) {
                        return true;
                    }

                }
            }
        }
        return false;

    }
}
