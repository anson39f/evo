package com.xds.project.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xds.base.net.ApiException;
import com.xds.base.net.HttpListener;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.entity.User;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.PaperButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author .
 * @TODO 新增分类
 * @email
 */
public class AddTypeActivity extends BaseActivity {
    private static final int REQUESTCODE_FROM_ACTIVITY = 11;
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.btUpLoadLocal)
    PaperButton btUpLoadLocal;
    @BindView(R.id.tvFile)
    TextView tvFile;
    @BindView(R.id.btAdd)
    PaperButton btAdd;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.etType)
    EditText etType;
    private String typeName, typeId;
    private String flie;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_add_type;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "新增分类");
        btAdd.setText("选择父类");
        btUpLoadLocal.setText("新增");
        tvFile.setText("请选择父类");
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


    @OnClick({R.id.btAdd, R.id.btUpLoadLocal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btAdd:
                break;
            case R.id.btUpLoadLocal:
                if (TextUtils.isEmpty(typeId)) {
                    ToastUtil.show(getContext(), "请选择父类");
                    return;
                }
                if (TextUtils.isEmpty(etType.getText().toString().trim())) {
                    ToastUtil.show(getContext(), "请输入分类名称");
                    return;
                }
                BaseAppApi.addType(this, etType.getText().toString().trim(), typeId,
                        new HttpListener<User>() {
                            @Override
                            public void onSuccess(User response) {
                                showToast("新增成功");
                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                showToast("新增失败");
                            }
                        });
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                typeId = data.getStringExtra("id");
                typeName = data.getStringExtra("name");
                tvFile.setText(String.format("父类：%s", typeName));
            }
        }
    }
}
