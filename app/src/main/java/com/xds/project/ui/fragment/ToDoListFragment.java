package com.xds.project.ui.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.base.utils.PreferencesUtils;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.app.Constant;
import com.xds.project.data.beanv2.CourseGroup;
import com.xds.project.data.beanv2.CourseV2;
import com.xds.project.data.greendao.CourseGroupDao;
import com.xds.project.data.greendao.CourseV2Dao;
import com.xds.project.entity.User;
import com.xds.project.mvp.CourseContract;
import com.xds.project.mvp.CoursePresenter;
import com.xds.project.ui.activity.AddClassActivity;
import com.xds.project.ui.adapter.SelectWeekAdapter;
import com.xds.project.util.AppUtils;
import com.xds.project.util.LogUtil;
import com.xds.project.util.ScreenUtils;
import com.xds.project.util.TimeUtils;
import com.xds.project.util.event.CourseDataChangeEvent;
import com.xds.project.widget.DialogHelper;
import com.xds.project.widget.DialogListener;
import com.xds.project.widget.ShowDetailDialog;
import com.xds.project.widget.custom.course.CourseAncestor;
import com.xds.project.widget.custom.course.CourseView;
import com.xds.project.widget.custom.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author .
 * @TODO 待办
 * @email
 */
public class ToDoListFragment extends BaseFragment  {

    private User user;


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_to_do_list;
    }


    @Override
    public void initUI() {
        user = BaseApplication.getUser();
        initToolbar();
    }

    @Override
    public void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_set) {
//                    Intent intent = new Intent(CourseActivity.this,
//                            HomeActivity.class);
//                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private boolean checkUser() {
        if (user == null || "普通用户".equals(user.type)) {
            showToast("没有权限进行操作");
            return true;
        }
        return false;
    }
}
