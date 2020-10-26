package com.xds.project.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xds.base.net.ApiException;
import com.xds.base.net.HttpListener;
import com.xds.base.net.RetrofitService;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.BlankUtil;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.api.remote.BaseService;
import com.xds.project.entity.FileResult;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.PaperButton;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author .
 * @TODO 新增用例
 * @email
 */
public class AddCaseActivity extends BaseActivity {
    private static final int REQUESTCODE_FROM_ACTIVITY = 11;
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.btUpLoadLocal)
    PaperButton btUpLoadLocal;
    @BindView(R.id.tvFile)
    TextView tvFile;
    @BindView(R.id.btAdd)
    PaperButton btAdd;
    private String id, typeId;
    private String flie;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_add;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "新增测试用例");
        id = getIntent().getStringExtra("id");
        typeId = getIntent().getStringExtra("typeId");
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
                RxPermissions rxPermissions = new RxPermissions(AddCaseActivity.this);
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
//                            Toast.makeText(SearchActivity.this, "允许了权限!", Toast.LENGTH_SHORT).show();
                            new LFilePicker()
                                    .withActivity(AddCaseActivity.this)
                                    .withMutilyMode(false)//单选
                                    .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                                    .start();
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            Toast.makeText(AddCaseActivity.this, "未授权权限，功能不能使用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.btUpLoadLocal:
                if (TextUtils.isEmpty(flie)) {
                    ToastUtil.show(getContext(), "请选择文件");
                    return;
                }
                showDialog();
                File file = new File(flie);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                RetrofitService.getService(BaseService.class).uploadFile(part).compose(this.<FileResult>bindToLife()).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        /*回调线程*/
                        .observeOn(AndroidSchedulers.mainThread())
                        /*结果判断*/
                        .subscribe(new Observer<FileResult>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(FileResult result) {
                                if (BlankUtil.isBlank(result.path)) {
                                    showToast("上传失败");
                                    hideDialog();
                                } else {
//                                    showToast("上传成功");
                                    BaseAppApi.addCase(AddCaseActivity.this, result.path, new HttpListener<Void>() {
                                        @Override
                                        public void onSuccess(Void response) {
                                            showToast("新增成功");
                                            hideDialog();
                                        }

                                        @Override
                                        public void onError(ApiException e) {
                                            super.onError(e);
                                            hideDialog();
                                            showToast("新增失败");
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                showToast("上传失败");
                                hideDialog();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                if (list != null && list.size() > 0) {
                    tvFile.setText(String.format("测试用例文件：%s", list.get(0)));
                    flie = list.get(0);
                }
                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
