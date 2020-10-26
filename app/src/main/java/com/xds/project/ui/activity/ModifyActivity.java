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
import com.xds.project.entity.TestCase;
import com.xds.project.entity.User;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.PaperButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author .
 * @TODO 修改
 * @email
 */
public class ModifyActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.btModify)
    PaperButton btModify;
    private String id, typeId;
    private TestCase testCase;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_modify;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "修改用例");
        id = getIntent().getStringExtra("id");
        testCase = getIntent().getParcelableExtra("data");
        //        typeId = getIntent().getStringExtra("typeId");

        tvType.setText(testCase.typename);
        etContent.setText(testCase.content);
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


    @OnClick({R.id.btModify, R.id.tvType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btModify:
                final String content = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    //            ToastUtil.show(getContext(), "请输入内容");
                    //            return;
                }

                BaseAppApi.judge(ModifyActivity.this, id, tvType.getText().toString().trim(),
                        content, typeId,
                        new HttpListener<Void>() {
                            @Override
                            public void onSuccess(Void response) {
                                //                                ToastUtil.show(getContext(), "修改成功");
                                ToastUtil.show(getContext(), "当前和已有测试用例相似度过高，建议不修改");
                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                //                                ToastUtil.show(getContext(), e.getMessage());
                                BaseAppApi.updateType(ModifyActivity.this, id, tvType.getText().toString().trim(),
                                        content, typeId,
                                        new HttpListener<User>() {
                                            @Override
                                            public void onSuccess(User response) {
                                                ToastUtil.show(getContext(), "修改成功");
                                            }

                                            @Override
                                            public void onError(ApiException e) {
                                                super.onError(e);
                                                ToastUtil.show(getContext(), "修改失败");
                                            }
                                        });
                            }
                        });

                break;
            case R.id.tvType:
                Intent intent = new Intent(getContext(), TypeListActivity.class);
                startActivityForResult(intent, 1000);
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                typeId = data.getStringExtra("id");
                String typeName = data.getStringExtra("name");
                tvType.setText(typeName);
            }
        }
    }
}
