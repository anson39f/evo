package com.xds.project.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.base.utils.JsonParser;
import com.xds.base.utils.PreferencesUtils;
import com.xds.base.utils.Utils;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.beanv2.SelfStudy;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.*;
import com.xds.project.util.LogUtil;
import com.xds.project.util.SPUtils;
import com.xds.project.util.event.UserEvent;
import de.hdodenhof.circleimageview.CircleImageView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * @author .
 * @TODO 我的界面
 * @email
 */
public class MeFragment extends BaseFragment implements TakePhoto.TakeResultListener, InvokeListener {
    private static final int REQUESTCODE_FROM_ACTIVITY = 111;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tv_center_tel)
    TextView tvCenterTel;
    @BindView(R.id.tvStudyTime)
    TextView tvStudyTime;
    @BindView(R.id.iv_center_user)
    CircleImageView ivCenterUser;

    @BindView(R.id.tvGender)
    TextView tvGender;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private String headUrl;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(500 * 1024).create(), true);
        return takePhoto;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_me;
    }

    @Override
    public void initUI() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initAction() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userChangeEvent(UserEvent event) {
        //更新主界面
        updateViews(true);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        setUser();
    }

    private void setUser() {
        user = BaseApplication.getUser();
        if (user == null) {
            return;
        }
        tvName.setText(user.getUsername());
        tvCenterTel.setText(String.format("Phone：%s", user.getPhone()));
        tvGender.setText(String.format("Email：%s", user.getEmail()));
        List<SelfStudy> list = Cache.instance().getSelfStudyDao().queryBuilder().list();
        if (list != null) {
            int hours = 0;
            int minutes = 0;
            for (SelfStudy selfStudy : list) {
                hours += selfStudy.getMinute();
                minutes += selfStudy.getSecond();
            }
            String and = hours > 0 && minutes > 0 ? "and" : "";
            String hoursString = hours > 0 ? String.format("%s hours", String.valueOf(hours)) : "";
            String minutesString = minutes > 0 ? String.format(" %s %s minutes", and, String.valueOf(minutes)) : "";
            tvStudyTime.setText(Utils.stringformat("Total Time in Studying:%s %s", hoursString, minutesString));
        } else {
            tvStudyTime.setText(Utils.stringformat("Total Time in Studying:0 hours"));
        }
    }


    @Override
    public void takeSuccess(TResult result) {
        headUrl = result.getImage().getCompressPath();
        Glide.with(getActivity()).load(headUrl).into(ivCenterUser);
        user.setHeadPic(headUrl);
        Cache.instance().getUserDao().update(user);
        PreferencesUtils.clear(getContext(), "user");
        PreferencesUtils.putString(getContext(), "user", JsonParser.toJson(user));
    }

    @Override
    public void takeFail(TResult result, String msg) {
        LogUtil.e(result, msg);
    }

    @Override
    public void takeCancel() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @OnClick({R.id.iv_center_user, R.id.ll_password, R.id.ll_edit_info, R.id.ll_todo, R.id.ll_study_history, R.id.ll_center_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_center_user:
//                RxPermissions rxPermissions = new RxPermissions(getActivity());
//                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//                            //申请的权限全部允许
////                            showToast.makeText(SearchActivity.this, "允许了权限!", showToast.LENGTH_SHORT).showToast();
//                            new LFilePicker()
//                                    .withFileFilter(new String[]{".jpg", ".png"})
//                                    .withActivity(getActivity())
//                                    .withMutilyMode(false)//单选
//                                    .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
//                                    .start();
//                        } else {
//                            //只要有一个权限被拒绝，就会执行
//                            showToast("未授权权限，功能不能使用");
//                        }
//                    }
//                });
                getTakePhoto().onPickFromGallery();
                break;
            case R.id.ll_edit_info:
                startActivity(new Intent(mContext, UserInfoEditActivity.class));
                break;
            case R.id.ll_password:
                startActivity(new Intent(mContext, ResetPassActivity.class));
                break;
            case R.id.ll_todo:
                startActivity(new Intent(mContext, TodoListActivity.class));
                break;
            case R.id.ll_study_history:
                startActivity(new Intent(mContext, StudyHistoryActivity.class));
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
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
