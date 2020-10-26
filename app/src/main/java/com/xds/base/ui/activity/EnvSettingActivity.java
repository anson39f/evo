package com.xds.base.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xds.base.config.Env;
import com.xds.base.config.UriProvider;
import com.xds.base.utils.SystemBarHelper;
import com.xds.project.R;
import com.xds.project.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class EnvSettingActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dev)
    RadioButton dev;
    @BindView(R.id.test)
    RadioButton test;
    @BindView(R.id.product)
    RadioButton product;
    @BindView(R.id.custom)
    RadioButton custom;
    @BindView(R.id.uri_setting)
    RadioGroup radioGroup;
    @BindView(R.id.host)
    EditText host;
    @BindView(R.id.save)
    TextView save;

    public static void launcher(Context context) {
        context.startActivity(new Intent(context, EnvSettingActivity.class));
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_env_settting;
    }


    @Override
    protected void initViews() {
        SystemBarHelper.setStatusBarDarkMode(this, true);
        initToolBar(toolbar, true, "服务器环境设置");
        Env.UriSetting uriSetting = Env.instance().getUriSetting();
        radioGroup.setOnCheckedChangeListener(this);
        if (Env.UriSetting.Dev == uriSetting) {
            radioGroup.check(R.id.dev);
        } else if (Env.UriSetting.Test == uriSetting) {
            radioGroup.check(R.id.test);
        } else if (Env.UriSetting.Demo == uriSetting) {
            radioGroup.check(R.id.demo);
        } else if (Env.UriSetting.Product == uriSetting) {
            radioGroup.check(R.id.product);
        } else if (Env.UriSetting.Custom == uriSetting) {
            radioGroup.check(R.id.custom);
        }
        host.setText(UriProvider.API_HOST);
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


    public boolean savaCustom() {
        if (radioGroup.getCheckedRadioButtonId() == R.id.custom) {
            String sHost = host.getText().toString().trim();
            if (TextUtils.isEmpty(sHost)) {
                ToastUtil.show(this, "服务器地址不能为空");
                return false;
            }
            Env.instance().setHost(sHost);
        }
        return true;

    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.dev:
                host.setText(UriProvider.API_HOST_DEV);
                host.setEnabled(false);
                break;
            case R.id.test:
                host.setText(UriProvider.API_HOST_TEST);
                host.setEnabled(false);
                break;
            case R.id.demo:
                host.setText(UriProvider.API_HOST_DEMO);
                host.setEnabled(false);
                break;
            case R.id.product:
                host.setText(UriProvider.API_HOST_PRODUCT);
                host.setEnabled(false);
                break;
            case R.id.custom:
                host.setEnabled(true);
                host.setText(Env.instance().getHost());
                break;
        }
    }


    @OnClick(R.id.save)
    public void onViewClicked() {
        Env.UriSetting uriSetting = Env.instance().getUriSetting();
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.dev:
                uriSetting = Env.UriSetting.Dev;
                break;
            case R.id.test:
                uriSetting = Env.UriSetting.Test;
                break;
            case R.id.demo:
                uriSetting = Env.UriSetting.Demo;
                break;
            case R.id.product:
                uriSetting = Env.UriSetting.Product;
                break;
            case R.id.custom:
                if (!savaCustom()) {
                    return;
                }
                uriSetting = Env.UriSetting.Custom;
                break;

        }
        Env.instance().setUriSetting(uriSetting);
        UriProvider.init(uriSetting);
        ToastUtil.show(this, String.format("已切换为“%s”环境:%s", uriSetting.getNickname(), host.getText().toString()));
        finish();
    }
}
