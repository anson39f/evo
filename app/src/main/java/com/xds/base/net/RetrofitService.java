package com.xds.base.net;

import android.support.v4.util.ArrayMap;

import com.xds.base.config.UriProvider;
import com.xds.base.utils.NetUtils;
import com.xds.project.BaseApplication;
import com.xds.project.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author
 * @todo 网络通信服务类，必须先调用初始化函数才能正常使用网络通信接口
 * @email
 * @date .
 */

public class RetrofitService {

    //设缓存有效期为1天
    static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)
    static final String CACHE_CONTROL_NETWORK = "Cache-Control: public, max-age=3600";
    // 避免出现 HTTP 403 Forbidden，参考：http://stackoverflow.com/questions/13670692/403-forbidden-with-java-but-not-web-browser
    static final String AVOID_HTTP403_FORBIDDEN = "UserEntity-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";

    // 递增页码
    private static final int INCREASE_PAGE = 20;
    private static Retrofit retrofit;
    private static ArrayMap<Class<?>, Object> service = new ArrayMap<>();


    private RetrofitService() {
        throw new AssertionError();
    }

    /**
     * 初始化网络通信服务
     */
    public static void init() {
        service.clear();
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(BaseApplication.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //debug环境开启日志打印
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }


        OkHttpClient okHttpClient = new OkHttpClient.Builder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(sRewriteCacheControlInterceptor)
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(UriProvider.API_HOST)
                .build();
    }


    public static <T> T getService(Class<T> cl) {
        Object o = service.get(cl);
        if (null == o) {
            o = retrofit.create(cl);
            service.put(cl, o);
        }
        return (T) o;
    }


    public static Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private static final Interceptor sRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtils.isNetConnected(BaseApplication.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();

            }

            Response originalResponse = chain.proceed(request);

            if (NetUtils.isNetConnected(BaseApplication.getContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    public static <T> void request(Observable<? extends IResponse<T>> observable, ObservableTransformer
            transformer, HttpListener<T> listener) {
        BaseApi api = new BaseApi(listener);
        observable.flatMap(api).compose(transformer).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(api.getSubscriber());

    }

    public static <T> void request(Observable<? extends IResponse<T>> observable, HttpListener<T> listener) {
        BaseApi api = new BaseApi(listener);
        observable.flatMap(api).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(api.getSubscriber());

    }

    public static <T> ObservableTransformer<T, T> schedulersTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        /*回调线程*/
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
