package com.xds.project.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.TimeUtil;
import com.xds.base.utils.Utils;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.bean.DayStudy;
import com.xds.project.data.beanv2.SelfStudy;
import com.xds.project.data.greendao.SelfStudyDao;
import com.xds.project.ui.adapter.StuyHistoryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

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
    @BindView(R.id.tvStudyTime)
    TextView tvStudyTime;
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
        List<SelfStudy> list = Cache.instance().getSelfStudyDao().queryBuilder().list();
        if (list != null) {
            int minutes = 0;
            int seconds = 0;
            for (SelfStudy selfStudy : list) {
                if (selfStudy.getState() == 1) {
                    minutes += selfStudy.getMinute();
                    seconds += selfStudy.getSecond();
                }
            }
            String and = minutes > 0 && seconds > 0 ? "and" : "";
            String minutesString = minutes > 0 ? String.format("%s minutes", String.valueOf(minutes)) : "";
            String secondsString = seconds > 0 ? String.format(" %s %s seconds", and, String.valueOf(seconds)) : "";
            if (minutes == 0 && seconds == 0) {
                tvStudyTime.setText(Utils.stringformat("Total Time in Studying : 0 minutes"));
            } else {
                tvStudyTime.setText(Utils.stringformat("Total Time in Studying:%s %s", minutesString, secondsString));
            }
        } else {
            tvStudyTime.setText(Utils.stringformat("Total Time in Studying : 0 minutes"));
        }
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
                .list();
        Map<String, DayStudy> map = new HashMap<>();
        List<DayStudy> dayStudyList = new ArrayList<>();
        DayStudy dayStudy = null;
        StringBuffer preDay = new StringBuffer();
        for (SelfStudy selfStudy : list) {
            String day = TimeUtil.getStrTime(selfStudy.getDate(), "yyyy-MM-dd");
            if (preDay.toString().contains(day)) {
                DayStudy cach = map.get(day);
                if (selfStudy.getState() == 1) {
                    cach.min += selfStudy.getMinute();
                    cach.sec += selfStudy.getSecond();
                    cach.successNum += 1;
                } else {
                    cach.failNum += 1;
                }
            } else {
                dayStudy = new DayStudy();
                map.put(day, dayStudy);
                preDay.append(day + ",");
                dayStudy.day = day;
                if (selfStudy.getState() == 1) {
                    dayStudy.min += selfStudy.getMinute();
                    dayStudy.sec += selfStudy.getSecond();
                    dayStudy.successNum += 1;
                } else {
                    dayStudy.failNum += 1;
                }
                dayStudyList.add(dayStudy);
            }
        }
        adapter.updateItems(dayStudyList);
    }

    public void request() {
        updateViews(true);
    }

}
