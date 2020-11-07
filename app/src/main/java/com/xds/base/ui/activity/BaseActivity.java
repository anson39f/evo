package com.xds.base.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.xds.base.ui.IBaseView;
import com.xds.project.R;
import com.xds.project.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author
 * @todo actiivty基类
 * @email
 * @date
 */

public abstract class BaseActivity extends RxAppCompatActivity implements
        IBaseView {
    protected final String TAG = this.getClass().getSimpleName();

    private View statusBarView;
    private int statusBarColor;
    private Unbinder unbinder;
    Dialog dialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        unbinder = ButterKnife.bind(this);
        initViews();
        initAction();
        updateViews(false);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindUntilEvent(ActivityEvent.DESTROY);
    }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    @LayoutRes
    protected abstract int attachLayoutRes();


    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    /**
     * 初始化事件
     */
    protected abstract void initAction();


    /**
     * 更新视图控件
     */
    protected abstract void updateViews(boolean isRefresh);

    @Override
    public void showLoading() {
        showDialog();
    }


    @Override
    public void hideLoading() {
        hideDialog();
    }

    @Override
    public void showDialog() {
        if (null == dialog || !dialog.isShowing()) {
            dialog = new Dialog(this, R.style.dialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            dialog.getWindow().setContentView(R.layout.load_dialog);
        }
    }

    @Override
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
        getSupportActionBar().setTitle(title);
        toolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (homeAsUpEnabled) {
//            toolbar.setNavigationIcon(android.R.drawable.ic_menu_);
        }

    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * 添加 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // 设置tag，不然下面 findFragmentByTag(tag)找不到
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    protected void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 替换 Fragment
     *
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // 设置tag
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // 这里要设置tag，上面也要设置tag
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            // 存在则弹出在它上面的所有fragment，并显示对应fragment
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }


    @Override
    protected void onDestroy() {
        hideDialog();
        super.onDestroy();
        unbinder.unbind();

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.show(getContext(), msg);
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting: {
                return true;
            }
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (Env.ISCONFIGURL && !(this instanceof EnvSettingActivity)) {
//            getMenuInflater().inflate(R.menu.base_env_setting, menu);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        }
        return getActivity().isFinishing();
    }
}
