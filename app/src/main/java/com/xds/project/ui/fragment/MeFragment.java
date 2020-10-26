package com.xds.project.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xds.base.ui.fragment.BaseFragment;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.LoginActivity;
import com.xds.project.util.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * @author .
 * @TODO 我的界面
 * @email
 */
public class MeFragment extends BaseFragment {
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tv_center_tel)
    TextView tvCenterTel;
    @BindView(R.id.iv_center_user)
    CircleImageView ivCenterUser;
    @BindView(R.id.ll_part)
    LinearLayout llPart;

    @BindView(R.id.tvGender)
    TextView tvGender;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_me;
    }

    @Override
    public void initUI() {

    }

    @Override
    public void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {
        User user = BaseApplication.getUser();
        if (user == null) {
            return;
        }
        tvName.setText(user.username);
        tvCenterTel.setText(String.format("手机号：%s", user.phone));
        tvGender.setText(String.format("用户类型：%s", user.type));
    }


    @OnClick({R.id.iv_center_user, R.id.ll_team, R.id.ll_part, R.id.ll_center_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_center_logout:
                SPUtils.setLogin(mContext, false);
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            updateViews(true);
        }
    }
}
