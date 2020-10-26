package com.xds.base.net;


public abstract class HttpListener<T> {
    public void onError(ApiException e) {

    }

    /**
     * 成功后回调方法
     *
     * @param response
     */
    public abstract void onSuccess(T response);

    public void onStart() {

    }

}
