package com.xds.project.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.net.HttpListener;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.entity.TypeBean;
import com.xds.project.ui.adapter.TypeListAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * 语料库列表
 *
 * @author .
 * @email
 */
public class KuListActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private TypeListAdapter adapter;

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
        initToolBar(toobar, true, "语料库列表");
        adapter = new TypeListAdapter(this);
        RecyclerViewHelper.initRecyclerViewV(getContext(), recyclerview, true, adapter);
    }

    @Override
    protected void initAction() {
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //                Intent intent = new Intent();
                //                intent.putExtra("id", adapter.getItem(position).id);
                //                intent.putExtra("name", adapter.getItem(position).name);
                //                setResult(RESULT_OK, intent);
                //                finish();
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        BaseAppApi.train(new HttpListener<List<TypeBean>>() {
            @Override
            public void onSuccess(List<TypeBean> response) {

                adapter.updateItems(response);
            }
        });
    }

    public void request() {
        updateViews(true);
    }

}
