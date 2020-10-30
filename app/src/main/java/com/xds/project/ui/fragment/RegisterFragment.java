package com.xds.project.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.ui.SimItemBottomDialog;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.base.utils.FormatUtils;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.entity.User;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.ClearEditText;

import java.util.ArrayList;
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
            ToastUtil.show(mContext, "Please enter the account");
        } else if (passwd.equals("")) {
            ToastUtil.show(mContext, "Please enter the password");
        } else if (phoneNumber.equals("")) {
            ToastUtil.show(mContext, "Please enter the phone number");
        } else if (!FormatUtils.checkPhone(phoneNumber)) {
            ToastUtil.show(mContext, "Please enter the correct password number");
//        } else if (type.equals("")) {
//            ToastUtil.show(mContext, "请选择类型");
        } else {
            User user = new User();
            user.setUsername(account);
            user.setPassword(passwd);
            user.setPhone(phoneNumber);
            user.setType("common user");
            Cache.instance().getUserDao().insert(user);
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
