package com.xds.project;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.os.StrictMode;

import com.xds.base.config.Env;
import com.xds.project.app.Cache;
import com.xds.project.entity.User;
import com.xds.project.util.ScreenUtils;

import java.util.List;

/**
 * 全局
 * @author .
 * @TODO
 * @email
 */
public class BaseApplication extends Application {
    private static BaseApplication context;
    private static User user;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        if (isMainProcess()) {
            //开启违例检测:StrictMode
            if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
            }
            Env.instance().init();
            Cache.instance().init(getContext());
            ScreenUtils.init(getContext());
        }
    }

    /**
     * 检测是否主进程
     * @return
     */
    private boolean isMainProcess() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }



    public static BaseApplication getContext() {
        return context;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        BaseApplication.user = user;
    }

    /**
     * 关闭app
     */
    public void closeApp() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        }, 300);

    }

}
