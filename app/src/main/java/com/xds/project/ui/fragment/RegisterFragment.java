package com.xds.project.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import butterknife.BindView;
import butterknife.OnClick;
import com.xds.base.ui.SimItemBottomDialog;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.base.utils.FormatUtils;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.greendao.UserDao;
import com.xds.project.entity.User;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.ClearEditText;

import java.util.List;

/**
 * @author .
 * @TODO 注册
 * @email
 */
public class RegisterFragment extends BaseFragment {

    @BindView(R.id.ed_phone)
    ClearEditText edPhone;
    @BindView(R.id.cEPhoneNumber)
    ClearEditText cEPhoneNumber;
    @BindView(R.id.ed_passwd)
    ClearEditText edPasswd;
    @BindView(R.id.et_email)
    ClearEditText et_email;
    private List<SimItemBottomDialog.CommonEntity> commonEntities;

    public static Fragment getInstance(String type) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_regist;
    }

    @OnClick(R.id.next)
    void next() {
        String account = edPhone.getText().toString();
        String passwd = edPasswd.getText().toString();
        String phoneNumber = cEPhoneNumber.getText().toString();
        String email = et_email.getText().toString();
        if (account.equals("")) {
            ToastUtil.show(mContext, "Please enter the account");
        } else if (email.equals("")) {
            ToastUtil.show(mContext, "Please enter the email");
        } else if (passwd.equals("")) {
            ToastUtil.show(mContext, "Please enter the password");
        } else if (phoneNumber.equals("")) {
            ToastUtil.show(mContext, "Please enter the phone number");
        } else if (!FormatUtils.checkPhone(phoneNumber)) {
            ToastUtil.show(mContext, "Please enter the correct password number");
//        } else if (type.equals("")) {
//            ToastUtil.show(mContext, "请选择类型");
        } else {
            List<User> users = Cache.instance().getUserDao().queryBuilder().where(UserDao.Properties.Username.eq(account)).list();
            if (users != null && !users.isEmpty()) {
                ToastUtil.show(mContext, "Account already exists");
                return;
            }
            User user = new User();
            user.setUsername(account);
            user.setPassword(passwd);
            user.setPhone(phoneNumber);
            user.setType("common user");
            user.setEmail(email);
            Cache.instance().getUserDao().insert(user);
            ToastUtil.show(mContext, "Register success");
            clearEditText();
        }
    }

    private void clearEditText() {
        edPhone.setText("");
        edPasswd.setText("");
        cEPhoneNumber.setText("");
        et_email.setText("");
    }

    @Override
    public void initUI() {
    }

    @Override
    public void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }
}
