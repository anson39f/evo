package com.xds.base.config;


import com.xds.base.utils.PreferencesUtils;
import com.xds.project.BaseApplication;
import com.xds.project.BuildConfig;

/**
 * 环境配置类
 */
public class Env {
    public static final String PREF_URI_SETTING = "env_pref_uri_setting";

    public static final String PREF_URI_HOST = "env_pre_uri_host";
    public static final boolean ISCONFIGURL = BuildConfig.DEBUG;
    /**
     * 环境配置  0:开发；1：测试；2：生产,3自定义
     */
//    protected static int mEnvironment = 0;


    private static final Env mEnv = new Env();

    private Env() {

    }

    public static Env instance() {
        return mEnv;
    }

    public void init() {

        if (ISCONFIGURL) {
            UriProvider.init(getUriSetting());
        } else {
            UriProvider.init(UriSetting.Product);
        }

    }


    /**
     * 设置ip
     *
     * @param host
     */
    public void setHost(String host) {
        if (host != null && ISCONFIGURL) {
            PreferencesUtils.putString(BaseApplication.getContext(), PREF_URI_HOST, host);
        }
    }

    /**
     * 设置uri环境
     *
     * @param uriSetting
     */
    public void setUriSetting(UriSetting uriSetting) {
        if (uriSetting != null && ISCONFIGURL) {
            PreferencesUtils.putInt(BaseApplication.getContext(), PREF_URI_SETTING, uriSetting.ordinal());

        }
    }


    public String getHost() {
        String host = "";
        if (ISCONFIGURL) {
            host = PreferencesUtils.getString(BaseApplication.getContext(), PREF_URI_HOST);
        }
        return host;
    }


    /**
     * 取uri环境
     *
     * @return
     */
    public UriSetting getUriSetting() {
        if (ISCONFIGURL) { //如果debug
            UriSetting uriSetting = UriSetting.Dev;
            if ("dev".equals(BuildConfig.FLAVOR)) {
                uriSetting = UriSetting.Dev;
            }

            int ordinal = PreferencesUtils.getInt(BaseApplication.getContext(), PREF_URI_SETTING, uriSetting.getIntValue());
            if (ordinal > -1) {
                return UriSetting.mapIntToValue(ordinal);
            }
        }
        return UriSetting.Product;
    }


    public static enum UriSetting {
        Dev(0, "开发"), Test(1, "测试"), Demo(2, "演示"), Product(3, "生产"), Custom(4, "自定义");

        private int intValue;
        private String nickname;

        UriSetting(int intValue, String nickname) {
            this.intValue = intValue;
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }

        public int getIntValue() {
            return intValue;
        }

        static UriSetting mapIntToValue(final int modeInt) {
            for (UriSetting value : UriSetting.values()) {
                if (modeInt == value.getIntValue()) {
                    return value;
                }
            }
            return Product;
        }

    }

}
