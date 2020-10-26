package com.xds.project.api;

import java.util.Collections;
import java.util.List;

/**
 * 返回数据为list
 *
 * @author
 * @email
 * @date
 */
public class ResList<T> extends BaseResponse<List<T>> {
    public List<T> data;

    @Override
    public List<T> getResponse() {
        if (null == data) {
            return Collections.emptyList();
        }
        return data;
    }

}
