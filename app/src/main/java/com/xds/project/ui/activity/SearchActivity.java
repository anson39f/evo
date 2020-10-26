package com.xds.project.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dl7.recycler.helper.RecyclerViewHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xds.base.config.UriProvider;
import com.xds.base.net.HttpListener;
import com.xds.base.net.RetrofitService;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.BlankUtil;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.api.remote.BaseService;
import com.xds.project.entity.SearchBean;
import com.xds.project.ui.adapter.SearchListAdapter;
import com.xds.project.widget.PaperButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @TODO
 * @email
 */
public class SearchActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.searchView)
    SearchView mSearchView;
    @BindView(R.id.tvSelect)
    TextView tvSelect;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.btDelete)
    PaperButton btDelete;
    @BindView(R.id.btDownload)
    PaperButton btDownload;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SearchListAdapter adapter;
    private String typeName;
    private String mKey;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "搜索");
        adapter = new SearchListAdapter(getContext());
        RecyclerViewHelper.initRecyclerViewV(getContext(), recyclerview, true, adapter);

        /*------------------ SearchView有三种默认展开搜索框的设置方式，区别如下： ------------------*/
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.setIconifiedByDefault(false);
        //设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
        mSearchView.onActionViewExpanded();

        mSearchView.setIconifiedByDefault(false);//设置搜索图标是否显示在搜索框内
        //1:回车
        //2:前往
        //3:搜索
        //4:发送
        //5:下一項
        //6:完成
        mSearchView.setImeOptions(2);//设置输入法搜索选项字段，默认是搜索，可以是：下一页、发送、完成等
        //        mSearchView.setInputType(1);//设置输入类型
        //        mSearchView.setMaxWidth(200);//设置最大宽度
        mSearchView.setQueryHint("全文搜索");//设置查询提示字符串
        //        mSearchView.setSubmitButtonEnabled(true);//设置是否显示搜索框展开时的提交按钮
        //设置SearchView下划线透明
        setUnderLinetransparent(mSearchView);
    }

    @Override
    protected void initAction() {
        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            //当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                mKey = newText;
                search(newText);
                return false;
            }
        });
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, TypeListActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> ids = adapter.getIds();
                if (ids.isEmpty()) {
                    showToast("请选择要删除测试用例");
                    return;
                }
                StringBuffer stringBuffer = new StringBuffer();
                for (String s : ids) {
                    stringBuffer.append(s);
                    stringBuffer.append(",");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                BaseAppApi.deletes(stringBuffer.toString(), new HttpListener<Void>() {
                    @Override
                    public void onSuccess(Void response) {
                        showToast("批量删除成功");
                        adapter.getIds().clear();
                        search(mKey);

                    }
                });
            }
        });
        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxPermissions rxPermissions = new RxPermissions(SearchActivity.this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
//                            Toast.makeText(SearchActivity.this, "允许了权限!", Toast.LENGTH_SHORT).show();
                            initDrownlad();
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            Toast.makeText(SearchActivity.this, "未授权权限，下载功能不能使用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void initDrownlad() {
        List<String> ids = adapter.getIds();
        if (ids.isEmpty()) {
            showToast("请选择要下载测试用例");
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (String s : ids) {
            stringBuffer.append(s);
            stringBuffer.append(",");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        drownload(stringBuffer.toString());
    }

    @Override
    protected void updateViews(boolean isRefresh) {
    }

    private void search(String key) {
        getListObservable(key, typeName).compose(this.<SearchBean>bindToLife()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(new Observer<SearchBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SearchBean searchBean) {
                        if (BlankUtil.isBlank(searchBean.data)) {
                            //                            showToast("没有数据");
                            tvNum.setText("查询到0条用例");
                            adapter.updateItems(searchBean.data);
                        } else {
                            adapter.updateItems(searchBean.data);
                            tvNum.setText(String.format("查询到%s条用例", searchBean.count + ""));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void drownload(final String id) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    String url = UriProvider.API_HOST + "Android/fileupload/downloadXls?fileName" +
                            "=" + id;
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .addHeader("Accept-Encoding", "identity")
                            .build();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    //获取下载的内容输入流
                    ResponseBody body = response.body();
                    InputStream inputStream = body.byteStream();
                    final long lengh = body.contentLength();
                    System.out.println("文件大小" + lengh);
                    // 文件保存到本地
                    File file1 = new File(Environment.getExternalStorageDirectory(), "/testCase");
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    File file = new File(file1, "test_" + new Date().getTime() + ".xls");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream outputStream = new FileOutputStream(file);
                    int lien = 0;
                    int losing = 0;
                    byte[] bytes = new byte[1024];
                    while ((lien = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, lien);
                        losing += lien;
                        final float i = losing * 1.0f / lengh;
                        System.out.println("下载进度" + i);
                        //((TestCaseListActivity) mContext).showProgress(file, i);
                        //使用rxjava切换线程
                        // UpdateAppearanceProgress(i);
                    }

                    showProgress(file, 0);
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showProgress(final File file, final float i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //                int ii = (int) (i * 100);
                //                progressBar.setVisibility(View.VISIBLE);
                //                progressBar.setProgress(ii);
                //                if (ii == 100) {
                //                    progressBar.setVisibility(View.GONE);
                //                    showToast("下载路径：" + file.toString());
                //                }
                //                showToast("下载完成，存放路径：" + file.toString());
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                showToast("下载完成，存放路径：" + file.toString());
            }
        }, 500);
    }

    private Observable<SearchBean> getListObservable(String key, String typename) {
        return RetrofitService.getService(BaseService.class).search(key, typename, "1", Integer.MAX_VALUE + "");
    }

    /**
     * 设置SearchView下划线透明
     **/
    private void setUnderLinetransparent(SearchView searchView) {
        try {
            Class<?> argClass = searchView.getClass();
            // mSearchPlate是SearchView父布局的名字
            Field ownField = argClass.getDeclaredField("mSearchPlate");
            ownField.setAccessible(true);
            View mView = (View) ownField.get(searchView);
            mView.setBackgroundColor(Color.TRANSPARENT);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting: {
                if (checkUser()) {
                    return true;
                }
                if (adapter.isShow()) {
                    //批量操作
                    adapter.setShow(false);
                    adapter.notifyDataSetChanged();
                    btDelete.setVisibility(View.GONE);
                    btDownload.setVisibility(View.GONE);
                } else {
                    //批量操作
                    adapter.setShow(true);
                    adapter.notifyDataSetChanged();
                    btDelete.setVisibility(View.VISIBLE);
                    btDownload.setVisibility(View.VISIBLE);
                }
                return true;
            }
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkUser() {
        if (BaseApplication.getUser() == null || "普通用户".equals(BaseApplication.getUser().type)) {
            showToast("没有权限进行操作");
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                //                type = data.getStringExtra("id");
                typeName = data.getStringExtra("name");
                tvSelect.setText(String.format("选择分类：%s", typeName));
                search(mKey);
            }
        }
    }
}
