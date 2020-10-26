package com.xds.project.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.util.SPUtils;
import com.xds.project.util.VersionUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.ll_center_logout)
    LinearLayout llCenterLogout;
    @BindView(R.id.tv_center_version)
    TextView tvCenterVersion;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "设置");
        tvCenterVersion.setText("当前版本号：" + VersionUtils.getVersionName(this));
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @OnClick({R.id.ll_center_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_center_logout:
                SPUtils.setLogin(this, false);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
