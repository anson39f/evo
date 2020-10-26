package com.xds.project.api;

/**
 * 返回数据为object
 *
 * @author
 * @email
 * @date
 */
public class ResData<T> extends BaseResponse<T> {

    public T data;


    @Override
    public T getResponse() {
        return data;
    }

}
