package com.xds.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.LifecycleTransformer;


/**
 * @todo view接口
 * @email
 * @date
 */

public interface IBaseView {
    /**
     * 显示加载动画
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    void hideDialog();

    void showDialog();


    void finish();

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    <T> LifecycleTransformer<T> bindToLife();

    /**
     * 提示toast
     *
     * @param msg
     */
    void showToast(String msg);


    void startActivity(Intent intent);

    void startActivity(Intent intent, @Nullable Bundle options);

    void startActivityForResult(Intent intent, int requestCode);

    void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    Activity getActivity();

    Context getContext();

    boolean isDestroyed();
}
