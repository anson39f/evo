package com.xds.project.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.beanv2.Things;
import com.xds.project.data.greendao.ThingsDao;
import com.xds.project.ui.adapter.TodoListAdapter;

import java.util.List;

/**
 * 待办列表
 *
 * @author .
 * @email
 */
public class TodoListActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tvStudyTime)
    TextView tvStudyTime;
    private TodoListAdapter adapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_to_do_list;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "To-do");
        adapter = new TodoListAdapter(this);
        RecyclerViewHelper.initRecyclerViewV(getContext(), recyclerview, true, adapter);
        tvStudyTime.setVisibility(View.GONE);
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

        List<Things> thingsList = Cache.instance().getThingsDao().queryBuilder()
                .where(ThingsDao.Properties.State.eq(1))//根据当前课表组id查询
                .where(ThingsDao.Properties.Deleted.eq(false))//查询没有删除的
                .orderDesc(ThingsDao.Properties.Date)
                .list();
        adapter.updateItems(thingsList);
    }

    public void request() {
        updateViews(true);
    }

}
