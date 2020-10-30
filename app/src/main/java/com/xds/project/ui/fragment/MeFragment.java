package com.xds.project.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.LoginActivity;
import com.xds.project.util.SPUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author .
 * @TODO 我的界面
 * @email
 */
public class MeFragment extends BaseFragment {
    private static final int REQUESTCODE_FROM_ACTIVITY = 111;
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
        tvName.setText(user.getUsername());
        tvCenterTel.setText(String.format("手机号：%s", user.getPhone()));
        tvGender.setText(String.format("用户类型：%s", user.getType()));
    }


    @OnClick({R.id.iv_center_user, R.id.ll_team, R.id.ll_part, R.id.ll_center_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_center_user:
                RxPermissions rxPermissions = new RxPermissions(getActivity());
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
//                            showToast.makeText(SearchActivity.this, "允许了权限!", showToast.LENGTH_SHORT).showToast();
                            new LFilePicker()
                                    .withFileFilter(new String[]{".jpg", ".png"})
                                    .withActivity(getActivity())
                                    .withMutilyMode(false)//单选
                                    .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                                    .start();
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            showToast("未授权权限，功能不能使用");
                        }
                    }
                });
                break;
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
            if (REQUESTCODE_FROM_ACTIVITY == requestCode) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                if (list != null && !list.isEmpty()) {
                    Glide.with(getContext()).load(list.get(0)).into(ivCenterUser);
                }
            } else {
                updateViews(true);
            }
        }
    }
}
