package com.xds.project.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.net.ApiException;
import com.xds.base.net.HttpListener;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.entity.TestCase;
import com.xds.project.ui.adapter.TestCaseListAdapter;
import com.xds.project.widget.PaperButton;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * 测试用例列表
 *
 * @author .
 * @email
 */
public class TestCaseListActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btDelete)
    PaperButton btDelete;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private TestCaseListAdapter adapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_test_case_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null && adapter.getData().size() > 0)
            updateViews(true);
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "测试用例");
        adapter = new TestCaseListAdapter(this);
        RecyclerViewHelper.initRecyclerViewV(getContext(), recyclerview, true, adapter);
    }

    @Override
    protected void initAction() {
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

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
                        requestList();

                    }
                });
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        showDialog();
        BaseAppApi.selectAllByLayui("1", Integer.MAX_VALUE + "", new HttpListener<List<TestCase>>() {
            @Override
            public void onSuccess(List<TestCase> response) {
                hideDialog();
                adapter.updateItems(response);
            }
            @Override
            public void onError(ApiException e) {
                super.onError(e);
                hideDialog();
            }
        });
    }

    public void requestList() {
        updateViews(true);
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
                } else {
                    //批量操作
                    adapter.setShow(true);
                    adapter.notifyDataSetChanged();
                    btDelete.setVisibility(View.VISIBLE);
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
}
