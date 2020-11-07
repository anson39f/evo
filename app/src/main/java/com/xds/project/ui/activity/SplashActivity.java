package com.xds.project.ui.activity;

import android.os.Handler;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.util.ActivityTools;
import com.xds.project.util.StatusBarUtils;

/**
 * @todo
 * @date 2020/11/7.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initViews() {
        StatusBarUtils.setStatusFullScreen(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityTools.startToNextActivity(getContext(), LoginActivity.class);
                finish();
            }
        }, 100);
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }
}
