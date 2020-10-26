package com.xds.base.config;

import android.text.TextUtils;

import com.xds.base.net.RetrofitService;


/**
 * API地址类
 */
public class UriProvider {
    //项目访问地址
    public static String API_HOST = "http://www.qinshanzhu.com/";
    public static String API_HOST_DEMO = "http://www.qinshanzhu.com/";
    public static String API_HOST_PRODUCT = "http://23.251.45.201:8080/";
    public static String API_HOST_TEST = "http://www.qinshanzhu.com/";//服务器
    public static String API_HOST_DEV = "http://www.qinshanzhu.com/";//本机


    public static void init(Env.UriSetting env) {
        if (env == Env.UriSetting.Dev) {//开发
            API_HOST = API_HOST_DEV;
        } else if (env == Env.UriSetting.Test) { //测试环境
            API_HOST = API_HOST_TEST;
        } else if (env == Env.UriSetting.Demo) {//演示
            API_HOST = API_HOST_DEMO;
        } else if (env == Env.UriSetting.Product) {//正式
            API_HOST = API_HOST_PRODUCT;
        } else if (env == Env.UriSetting.Custom) {
            API_HOST = getHost();
            if (TextUtils.isEmpty(API_HOST) || !(API_HOST.startsWith("http://") || API_HOST.startsWith("https://"))) {
                API_HOST = API_HOST_PRODUCT;
            }
            if (!API_HOST.endsWith("/")) {
                API_HOST = API_HOST + "/";
            }
        }

        RetrofitService.init();
    }

    public static String getHost() {

        return Env.instance().getHost();
    }

}
