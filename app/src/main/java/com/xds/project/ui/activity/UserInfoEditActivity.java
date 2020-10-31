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
import com.xds.project.util.event.UserEvent;
import com.xds.project.widget.PaperButton;
import org.greenrobot.eventbus.EventBus;

/**
 * @todo
 * @date 2020/10/31.
 */
public class UserInfoEditActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.btModify)
    PaperButton btModify;
    private User user;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_edit_user_info;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "Edit Your Personal Detail");
        user = BaseApplication.getUser();
        etPhone.setText(user.getPhone());
        etEmail.setText(user.getEmail());
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
                final String phone = etPhone.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)) {
                    ToastUtil.show(getContext(), "Please complete the information");
                    return;
                }
                user.setPhone(phone);
                user.setEmail(email);
                Cache.instance().getUserDao().update(user);
                PreferencesUtils.clear(getContext(), "user");
                PreferencesUtils.putString(getContext(), "user", JsonParser.toJson(user));
                EventBus.getDefault().post(new UserEvent());
                finish();
                break;
        }

    }
}
