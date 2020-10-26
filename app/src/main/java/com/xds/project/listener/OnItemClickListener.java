package com.xds.project.listener;

import android.view.View;

/**
 */
public interface OnItemClickListener<T> {
    /**
     * @Auth Mr.Jobs(乔元培)
     * @Date 8.25
     * 点击事件
     **/
    void onItemClick(View view, int position, T model);

    void onItemLongClick(View view, int position, T model);
}
