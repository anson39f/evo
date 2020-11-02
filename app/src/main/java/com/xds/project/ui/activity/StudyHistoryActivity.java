package com.xds.project.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.beanv2.SelfStudy;
import com.xds.project.data.greendao.SelfStudyDao;
import com.xds.project.ui.adapter.StuyHistoryListAdapter;

import java.util.List;

/**
 * 待办列表
 *
 * @author .
 * @email
 */
public class StudyHistoryActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private StuyHistoryListAdapter adapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_to_do_list;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "Study History");
        adapter = new StuyHistoryListAdapter(this);
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

        List<SelfStudy> list = Cache.instance().getSelfStudyDao().queryBuilder().orderDesc(SelfStudyDao.Properties.Date)
//                .where(SelfStudyDao.Properties.State.eq(1))//根据当前课表组id查询
//                .where(SelfStudyDao.Properties.Deleted.eq(false))//查询没有删除的
//                .orderDesc(SelfStudyDao.Properties.Date)
                .list();
        adapter.updateItems(list);
    }

    public void request() {
        updateViews(true);
    }

}
