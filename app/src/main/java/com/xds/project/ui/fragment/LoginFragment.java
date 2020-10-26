package com.xds.project.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xds.base.net.ApiException;
import com.xds.base.net.HttpListener;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.base.utils.JsonParser;
import com.xds.base.utils.PreferencesUtils;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.MainActivity;
import com.xds.project.util.ActivityTools;
import com.xds.project.util.SPUtils;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.EditTextWithDel;
import com.xds.project.widget.PaperButton;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {
    @BindView(R.id.userph)
    EditTextWithDel userph;
    @BindView(R.id.rela_name)
    RelativeLayout relaName;
    @BindView(R.id.codeicon)
    ImageView codeicon;
    @BindView(R.id.userpass)
    EditTextWithDel userpass;
    @BindView(R.id.rela_pass)
    RelativeLayout relaPass;
    @BindView(R.id.bt_login)
    PaperButton btLogin;
    @BindView(R.id.ed_confrim)
    EditTextWithDel edConfirm;
    private String type;

    public static Fragment getInstance(String s) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(R.id.bt_login)
    void login() {
//                ActivityTools.startToNextActivity(getContext(), MainActivity.class);
//                ToastUtil.show(mContext, "登陆成功");
//                finish();
        if (userph.getText().toString().equals("")) {
            ToastUtil.show(mContext, "请先输入账号");
        } else if (userpass.getText().toString().equals("")) {
            ToastUtil.show(mContext, "请先输入密码");
        } else {
            BaseAppApi.login(this, userph.getText().toString(), userpass.getText().toString(),
                    new HttpListener<User>() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            showDialog();
                        }

                        @Override
                        public void onError(ApiException e) {
                            super.onError(e);
                            hideDialog();
                            showToast(e.getMessage());
                        }

                        @Override
                        public void onSuccess(User user) {
                            hideDialog();
//                            user.type = type;
                            BaseApplication.setUser(user);
                            SPUtils.setLogin(mContext, true);
                            PreferencesUtils.clear(getContext(), "user");
                            PreferencesUtils.putString(getContext(), "user", JsonParser.toJson(user));
                            ActivityTools.startToNextActivity(getActivity(), MainActivity.class);
                            finish();
                        }
                    });

        }
    }


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_login;
    }

    @Override
    public void initUI() {
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        SPUtils.setGrade(mContext, true);
        if (SPUtils.getUserPsd(mContext) != null) {
            userpass.setText(SPUtils.getUserPsd(mContext));
            userph.setText(SPUtils.getUserId(mContext));
        }

    }

    @Override
    public void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


}
