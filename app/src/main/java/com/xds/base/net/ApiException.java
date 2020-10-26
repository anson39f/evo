package com.xds.base.net;


/**
 * 自定义异常类
 */
public class ApiException extends RuntimeException {


    public int resultCode;
    public String des;

    public ApiException(int resultCode, String des, String message) {
        super(message);
        this.resultCode = resultCode;
        this.des = des;
    }

    public static ApiException handleExceptioin(Throwable e) {

        ApiException exception;
        if (e instanceof ApiException) {
            exception = (ApiException) e;
        } else {
            exception = new ApiException(-200, IResponse.ERRDATA, IResponse.ERRDATA);
        }
        return exception;
    }

}

