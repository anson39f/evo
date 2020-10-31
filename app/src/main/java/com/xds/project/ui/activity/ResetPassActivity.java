package com.xds.project.ui.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.JsonParser;
import com.xds.base.utils.PreferencesUtils;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.entity.User;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.PaperButton;

/**
 * @todo
 * @date 2020/10/31.
 */
public class ResetPassActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.etNewPassword)
    EditText et_newPassword;
    @BindView(R.id.etOldPass)
    EditText et_oldPass;
    @BindView(R.id.btModify)
    PaperButton btModify;
    private User user;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_reset_pass;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "Password Reset");
        user = BaseApplication.getUser();
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


    @OnClick({R.id.btModify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btModify:
                final String oldPass = et_oldPass.getText().toString().trim();
                final String newPassword = et_newPassword.getText().toString().trim();
                if (TextUtils.isEmpty(oldPass) || TextUtils.isEmpty(newPassword)) {
                    ToastUtil.show(getContext(), "Please complete the information");
                    return;
                }
                if (user != null && user.getPassword().equals(oldPass)) {
                    user.setPassword(newPassword);
                    Cache.instance().getUserDao().update(user);
                    PreferencesUtils.clear(getContext(), "user");
                    PreferencesUtils.putString(getContext(), "user", JsonParser.toJson(user));
                    showToast("Reset success");
                    finish();
                } else {
                    showToast("Reset fail");
                }
                break;
        }

    }
}
