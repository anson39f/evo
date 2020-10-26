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
import com.xds.base.net.HttpListener;
import com.xds.base.net.RetrofitService;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.BlankUtil;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.api.remote.BaseService;
import com.xds.project.entity.FileResult;
import com.xds.project.entity.TypeBean;
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
 * @TODO 新增语料库
 * @email
 */
public class AddKuActivity extends BaseActivity {
    private static final int REQUESTCODE_FROM_ACTIVITY = 11;
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.btType)
    PaperButton btType;
    @BindView(R.id.btUpLoadLocal)
    PaperButton btUpLoadLocal;
    @BindView(R.id.tvFile)
    TextView tvFile;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.btAdd)
    PaperButton btAdd;
    private String id, typeId;
    private String flie;
    private String type;
    private String typeName;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_add;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "建立语料库");
        id = getIntent().getStringExtra("id");
        typeId = getIntent().getStringExtra("typeId");
        btAdd.setText("选择语料库文件");
        btUpLoadLocal.setText("上传语料库");
        btType.setVisibility(View.VISIBLE);
        tvType.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


    @OnClick({R.id.btAdd, R.id.btUpLoadLocal, R.id.btType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btType:
                Intent intent = new Intent(getContext(), TypeListActivity.class);
                startActivityForResult(intent, 1000);
                break;
            case R.id.btAdd:
                RxPermissions rxPermissions = new RxPermissions(AddKuActivity.this);
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
//                            Toast.makeText(SearchActivity.this, "允许了权限!", Toast.LENGTH_SHORT).show();
                            new LFilePicker()
                                    .withActivity(AddKuActivity.this)
                                    .withMutilyMode(false)//单选
                                    .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                                    .start();
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            Toast.makeText(AddKuActivity.this, "未授权权限，功能不能使用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.btUpLoadLocal:
                if (TextUtils.isEmpty(type)) {
                    ToastUtil.show(getContext(), "请选择分类");
                    return;
                }
                if (TextUtils.isEmpty(flie)) {
                    ToastUtil.show(getContext(), "请选择文件");
                    return;
                }
                showDialog();
                File file = new File(flie);
                MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                RetrofitService.getService(BaseService.class).uploadFile1(part, type).compose(this.<FileResult>bindToLife()).subscribeOn(Schedulers.io())
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
                                    BaseAppApi.train(new HttpListener<List<TypeBean>>() {
                                        @Override
                                        public void onSuccess(List<TypeBean> response) {
                                            hideDialog();
                                            showToast("训练成功");
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
            if (requestCode == 1000) {
                type = data.getStringExtra("id");
                typeName = data.getStringExtra("name");
                tvType.setText(String.format("分类：%s", typeName));
            }
        }
    }
}
