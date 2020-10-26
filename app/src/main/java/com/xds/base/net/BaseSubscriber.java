package com.xds.base.net;


import com.orhanobut.logger.Logger;

import io.reactivex.observers.DefaultObserver;


/**
 * 自定义监听
 * @param <T>
 */
public class BaseSubscriber<T> extends DefaultObserver<T> {
    private HttpListener listener;
    T t;

    boolean isApply = false;
    @Override
    public void onComplete() {
        if (null != listener && isApply) {
            listener.onSuccess(t);
        }
    }

    BaseSubscriber(BaseApi baseApi) {
        this.listener = baseApi.getListener();
    }

    @Override
    public void onError(Throwable e) {
        Logger.e(e, "BaseSubscriber" + e.getMessage());

        if (null != listener) {
            listener.onError(ApiException.handleExceptioin(e));
        }
    }

    @Override
    public void onNext(T tmPair) {
        t = tmPair;
    }


    @Override
    public void onStart() {
        if (null != listener) {
            listener.onStart();
        }
    }

}
