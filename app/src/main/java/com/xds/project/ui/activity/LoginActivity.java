package com.xds.project.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.ui.activity.EnvSettingActivity;
import com.xds.base.utils.JsonParser;
import com.xds.base.utils.PreferencesUtils;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.entity.User;
import com.xds.project.listener.OnFragmentInteractionListener;
import com.xds.project.ui.adapter.TabViewPagerAdapter;
import com.xds.project.ui.fragment.LoginFragment;
import com.xds.project.ui.fragment.RegisterFragment;
import com.xds.project.util.ActivityTools;
import com.xds.project.util.SPUtils;
import com.xds.project.util.StatusBarUtils;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements OnFragmentInteractionListener {
    @BindView(R.id.login_tabs)
    TabLayout loginTabs;
    @BindView(R.id.login_viewpager)
    ViewPager loginViewpager;

    public static boolean isLogin = false;


    private void setupViewPager(ViewPager viewPager) {
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(LoginFragment.getInstance("0"), "登录");
        adapter.addFrag(RegisterFragment.getInstance("1"), "注册");
        viewPager.setAdapter(adapter);
        loginTabs.setupWithViewPager(loginViewpager);
        if (isLogin) {

        }
    }

    @Override
    public void onFragmentInteraction(Object object) {

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        isLogin = getIntent().getBooleanExtra("is", false);
        if (SPUtils.isLogin(getActivity())) {
            String userJson = PreferencesUtils.getString(getContext(), "user");
            User user = JsonParser.parseJsonObject(userJson, User.class);
            BaseApplication.setUser(user);
            ActivityTools.startToNextActivity(getContext(), MainActivity.class);
            //            ToastUtil.show(this, "登陆成功");
            finish();
        }
        StatusBarUtils.setStatusFullScreen(LoginActivity.this);
        setupViewPager(loginViewpager);
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {
    }


    public void toEnv(View view) {
        EnvSettingActivity.launcher(this);
    }

    //点击注册
    public void toRegister(View view) {
        addFragment(R.id.content,
                RegisterFragment.getInstance(loginViewpager.getCurrentItem() + ""),
                "RegisterFragment");
    }
}
