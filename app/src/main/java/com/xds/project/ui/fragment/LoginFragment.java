package com.xds.project.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.base.utils.JsonParser;
import com.xds.base.utils.PreferencesUtils;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.greendao.UserDao;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.MainActivity;
import com.xds.project.util.ActivityTools;
import com.xds.project.util.SPUtils;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.EditTextWithDel;
import com.xds.project.widget.PaperButton;

import java.util.List;

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
//        ActivityTools.startToNextActivity(getContext(), MainActivity.class);
//        finish();
        if (userph.getText().toString().equals("")) {
            ToastUtil.show(mContext, "Please enter the account");
        } else if (userpass.getText().toString().equals("")) {
            ToastUtil.show(mContext, "Please enter the password");
        } else {
            List<User> user = Cache.instance().getUserDao().queryBuilder().where(UserDao.Properties.Username.eq(userph.getText().toString())).list();
            if (user != null && !user.isEmpty() && user.get(0).getPassword().equals(userpass.getText().toString())) {
                BaseApplication.setUser(user.get(0));
                SPUtils.setLogin(mContext, true);
                PreferencesUtils.clear(getContext(), "user");
                PreferencesUtils.putString(getContext(), "user", JsonParser.toJson(user.get(0)));
                ActivityTools.startToNextActivity(getActivity(), MainActivity.class);
                ToastUtil.show(mContext, "Login success");
                finish();
            } else {
                ToastUtil.show(mContext, "Please enter the correct account and password");
            }

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
