package com.xds.project.ui.activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dl7.recycler.helper.RecyclerViewHelper;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.BlankUtil;
import com.xds.base.utils.PreferencesUtils;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.app.Constant;
import com.xds.project.data.beanv2.CourseV2;
import com.xds.project.data.greendao.CourseV2Dao;
import com.xds.project.ui.adapter.SearchListAdapter;
import com.xds.project.util.AppUtils;
import com.xds.project.util.event.CourseDataChangeEvent;
import com.xds.project.widget.PaperButton;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;

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
    @BindView(R.id.tvNum)
    TextView tvNum;
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
        initToolBar(toobar, true, "Import Course");
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
        mSearchView.setQueryHint("search course");//设置查询提示字符串
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
        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDrownlad();
            }
        });
    }

    private void initDrownlad() {
        List<Long> ids = adapter.getIds();
        if (ids.isEmpty()) {
            return;
        }
        long couCgId = PreferencesUtils.getLong(getContext(), getString(R.string.app_preference_current_cs_name_id), 0);

        StringBuffer stringBuffer = new StringBuffer();
        for (Long id : ids) {
//            stringBuffer.append(s);
//            stringBuffer.append(",");

            CourseV2 courseV2 = Cache.instance().getLocalDataDao().load(id);
            CourseV2 c = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                    .setCouCgId(couCgId)
                    .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                    .setCouWeek(courseV2.getCouWeek())
                    .setCouStartNode(courseV2.getCouStartNode())
                    .setCouNodeCount(courseV2.getCouNodeCount())
                    .setCouName(courseV2.getCouName())
                    .setCouTeacher(courseV2.getCouTeacher())
                    .setCouLocation(courseV2.getCouLocation())
                    .init();
            Cache.instance().getCourseV2Dao().insert(c);
        }
        EventBus.getDefault().post(new CourseDataChangeEvent());
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                finish();
            }
        }, 150);
//        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
    }

    private void search(String key) {

        List<CourseV2> courses = Cache.instance().getLocalDataDao()
                .queryBuilder()
//                .where(CourseV2Dao.Properties.CouCgId.eq(csNameId))//根据当前课表组id查询
                .where(CourseV2Dao.Properties.CouDeleted.eq(false))//查询没有删除的
                .where(CourseV2Dao.Properties.CouName.like("%" + key + "%"))//模糊查询
                .list();
        if (BlankUtil.isBlank(courses)) {
            //                            showToast("没有数据");
            tvNum.setText("no courses");
            adapter.updateItems(courses);
        } else {
            adapter.updateItems(courses);
            tvNum.setText(String.format("find %s courses", courses.size() + ""));
        }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_env_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting: {
//                if (checkUser()) {
//                    return true;
//                }
                if (adapter.isShow()) {
                    //批量操作
                    adapter.setShow(false);
                    adapter.notifyDataSetChanged();
                    btDownload.setVisibility(View.GONE);
                } else {
                    //批量操作
                    adapter.setShow(true);
                    adapter.notifyDataSetChanged();
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

}
