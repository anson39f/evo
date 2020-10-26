package com.xds.base.net;


import com.orhanobut.logger.Logger;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 数据处理，处理返回数据转换，异常转换，设置自定义监听器
 * @param <T>
 */
public class BaseApi<T> implements Function<IResponse<T>,ObservableSource< T>> {

    private BaseSubscriber<T> mSubscriber;
    private HttpListener listener;

    public BaseApi(HttpListener listener) {
        this.listener = listener;
        mSubscriber = new BaseSubscriber<T>(this);
    }

    public HttpListener getListener() {
        return listener;
    }

    public BaseSubscriber getSubscriber() {
        return mSubscriber;
    }

    @Override
    public ObservableSource<T> apply(IResponse<T> baseResult) {

        Logger.d("IResponse code=%d,message=%s", baseResult.getCode(), baseResult.getMsg());
        mSubscriber.isApply = true;
        if (!baseResult.isOk()) {

            throw new ApiException(baseResult.getCode(), baseResult.getDesc(), baseResult.getMsg());
        }
        if(baseResult.getResponse()==null){
            return Observable.empty();
        }
        return Observable.just(baseResult.getResponse());

    }

}

