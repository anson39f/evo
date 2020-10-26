package com.xds.project.api;

import com.xds.base.net.IResponse;

/**
 * 返回数据
 *
 * @param <T>
 */
public abstract class BaseResponse<T> implements IResponse<T> {

    public int code;
    public String message;

    @Override
    public String getDesc() {
        return message;
    }

    @Override
    public String getMsg() {
        return message;
    }

    @Override
    public boolean isOk() {
        if (code == 200 || code == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int getCode() {
        return code;
    }
}
