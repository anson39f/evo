package com.xds.base.net;

import java.io.Serializable;

/**
 * 返回数据基类
 */

public interface IResponse<T> extends Serializable {
    public static final String NODATA = "数据飞向了外太空...";
    public static final String ERRDATA = "无法连接服务器，请检查你的网络或稍后重试";

    boolean isOk();

    T getResponse();

    int getCode();

    String getMsg();

    String getDesc();
}
