package com.xds.project.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.net.ApiException;
import com.xds.base.net.HttpListener;
import com.xds.base.ui.SimItemBottomDialog;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.base.utils.FormatUtils;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.entity.User;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.tvType)
    TextView tvType;
    private String type;
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
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        String account = edPhone.getText().toString();
        String passwd = edPasswd.getText().toString();
        String phoneNumber = cEPhoneNumber.getText().toString();
        String type = tvType.getText().toString();
        if (account.equals("")) {
            ToastUtil.show(mContext, "请输入账号");
        } else if (passwd.equals("")) {
            ToastUtil.show(mContext, "请输入密码");
        } else if (phoneNumber.equals("")) {
            ToastUtil.show(mContext, "请输入手机号");
        } else if (!FormatUtils.checkPhone(phoneNumber)) {
            ToastUtil.show(mContext, "请输入正确的手机号码");
        } else if (type.equals("")) {
            ToastUtil.show(mContext, "请选择类型");
        } else {
            BaseAppApi.register(this, account, passwd, type, phoneNumber,
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
                        public void onSuccess(User response) {
                            hideDialog();
                            ToastUtil.show(mContext, "注册成功");
                            clearEditText();

                        }
                    });
        }
    }

    private void clearEditText() {
        edPhone.setText("");
        edPasswd.setText("");
        cEPhoneNumber.setText("");
        tvType.setText("");
    }

    @Override
    public void initUI() {
        commonEntities = new ArrayList<SimItemBottomDialog.CommonEntity>() {
            {
                add(new SimItemBottomDialog.CommonEntity("管理员"));
                add(new SimItemBottomDialog.CommonEntity("测试员"));
                add(new SimItemBottomDialog.CommonEntity("开发者"));
                add(new SimItemBottomDialog.CommonEntity("产品"));
                add(new SimItemBottomDialog.CommonEntity("普通用户"));
            }
        };
    }

    @Override
    public void initAction() {
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimItemBottomDialog dialog = SimItemBottomDialog.getDialog(getActivity(), commonEntities
                        , new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                tvType.setText(commonEntities.get(position).name);
                            }
                        });
                dialog.show();
            }
        });

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }
}
