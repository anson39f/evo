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
import android.util.TypedValue;
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
import com.xds.project.ui.activity.SearchActivity;
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
 * @TODO 课程表页面
 * @email
 */
public class CourseFragment extends BaseFragment implements CourseContract.View {

    private User user;

    CourseContract.Presenter mPresenter;
    private TextView mTvWeekCount;
    private int mCurrentWeekCount;
    private int mCurrentMonth;
    private ShowDetailDialog mDialog;
    private CourseView mCourseViewV2;
    private LinearLayout mLayoutWeekGroup;
    private LinearLayout mLayoutNodeGroup;
    private int WEEK_TEXT_SIZE = 10;
    private int NODE_TEXT_SIZE = 9;
    private int NODE_WIDTH = 25;
    private TextView mMMonthTextView;
    private RecyclerView mRvSelectWeek;
    private int mHeightSelectWeek;
    private boolean mSelectWeekIsShow = false;
    private LinearLayout mLayoutCourse;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_course;
    }

    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void initUI() {
        user = BaseApplication.getUser();
        //EvenBus
        EventBus.getDefault().register(this);

        mLayoutWeekGroup = findViewById(R.id.layout_week_group);
        mLayoutNodeGroup = findViewById(R.id.layout_node_group);
        mLayoutCourse = findViewById(R.id.layout_course);

        initFirstStart();
        initToolbar();
        initWeek();
        initCourseView();
        initWeekNodeGroup();
        mPresenter = new CoursePresenter(this);
    }

    @Override
    public void initAction() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void courseChangeEvent(CourseDataChangeEvent event) {
        //更新主界面
        updateViews(true);
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        updateCoursePreference();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_set) {
                    Intent intent = new Intent(getActivity(),
                            SearchActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void initWeek() {
        initWeekTitle();
        initSelectWeek();
    }

    private void initWeekTitle() {
        mTvWeekCount = findViewById(R.id.tv_toolbar_subtitle);
        mTvWeekCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                weekTitle(v);
            }
        });
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getString(R.string.class_schedule));
    }

    private void weekTitle(View v) {
        animSelectWeek(!mSelectWeekIsShow);
    }

    @SuppressLint("SetTextI18n")
    public void updateCoursePreference() {
        updateCurrentWeek();
        mCurrentMonth = TimeUtils.getNowMonth();
        //        mMMonthTextView.setText(mCurrentMonth + "\n月");
        mMMonthTextView.setText(Constant.MONTHS[mCurrentMonth - 1]);

        //get id
        long currentCsNameId = PreferencesUtils.getLong(getActivity(), getString(R.string.app_preference_current_cs_name_id), 0L);

        LogUtil.i(this, "当前课表-->" + currentCsNameId);
        mPresenter.updateCourseViewData(currentCsNameId);
    }

    @SuppressLint("SetTextI18n")
    private void updateCurrentWeek() {
        mCurrentWeekCount = AppUtils.getCurrentWeek(getActivity());
        mTvWeekCount.setText("第" + mCurrentWeekCount + "周");
        mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
    }

    @Override
    public void initFirstStart() {
        boolean isFirst = PreferencesUtils.getBoolean(getActivity(), getString(R.string.app_preference_app_is_first_start), true);
        if (!isFirst) {
            return;
        }
        PreferencesUtils.putBoolean(mContext, getString(R.string.app_preference_app_is_first_start), false);

        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();
        CourseGroup defaultGroup = groupDao
                .queryBuilder()
                .where(CourseGroupDao.Properties.CgName.eq("默认课表"))
                .unique();

        long insert;
        if (defaultGroup == null) {
            insert = groupDao.insert(new CourseGroup(0L, "默认", ""));
        } else {
            insert = defaultGroup.getCgId();
        }

        //migrate old data
        AppUtils.copyOldData(getContext());
        PreferencesUtils.putLong(mContext, getString(R.string.app_preference_current_cs_name_id), insert);
    }

    private void initSelectWeek() {
        mRvSelectWeek = findViewById(R.id.recycler_view_select_week);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectWeek.getLayoutParams();
        params.topMargin = -ScreenUtils.dp2px(45);
        mRvSelectWeek.setLayoutParams(params);

        mRvSelectWeek.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            strings.add("第" + i + "周");
        }
        SelectWeekAdapter selectWeekAdapter = new SelectWeekAdapter(getActivity(), strings);
        mRvSelectWeek.setAdapter(selectWeekAdapter);
        mRvSelectWeek.scrollToPosition(AppUtils.getCurrentWeek(getContext()) - 1);

        mRvSelectWeek.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mHeightSelectWeek = bottom - top;
            }
        });


        selectWeekAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurrentWeekCount = position + 1;

                AppUtils.PreferencesCurrentWeek(getContext(), mCurrentWeekCount);
                mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
                mCourseViewV2.resetView();
                mTvWeekCount.setText("第" + mCurrentWeekCount + "周");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animSelectWeek(false);
                        AppUtils.updateWidget(getContext());
                    }
                }, 150);
            }
        });
    }


    private void animSelectWeek(boolean show) {
        mSelectWeekIsShow = show;

        int start = 0, end = 0;
        if (show) {
            start = -mHeightSelectWeek;
        } else {
            end = -mHeightSelectWeek;
        }

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectWeek.getLayoutParams();
                params.topMargin = (int) animation.getAnimatedValue();
                mRvSelectWeek.setLayoutParams(params);
            }
        });
        animator.start();
    }

    private void initWeekNodeGroup() {
        mLayoutNodeGroup.removeAllViews();
        mLayoutWeekGroup.removeAllViews();

        for (int i = -1; i < 5; i++) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);

            textView.setWidth(0);
            textView.setTextColor(getResources().getColor(R.color.primary_text));
            LinearLayout.LayoutParams params;

            if (i == -1) {
                params = new LinearLayout.LayoutParams(
                        Utils.dip2px(getContext(), NODE_WIDTH),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, NODE_TEXT_SIZE);
                //                textView.setText(mCurrentMonth + "\n月");
                textView.setText(Constant.MONTHS[Math.max(mCurrentMonth - 1, 0)]);

                mMMonthTextView = textView;
            } else {
                params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, WEEK_TEXT_SIZE);
                textView.setText(Constant.WEEK_SINGLE[i]);
            }

            mLayoutWeekGroup.addView(textView, params);
        }

        int nodeItemHeight = Utils.dip2px(getContext(), 55);
        for (int i = 0; i < Constant.TIMES.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(NODE_TEXT_SIZE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.GRAY);
            textView.setText(Constant.TIMES[i]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, nodeItemHeight);
            mLayoutNodeGroup.addView(textView, params);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCourseView() {
        mCourseViewV2 = findViewById(R.id.course_view_v2);
        mCourseViewV2.setCourseItemRadius(3)
                .setTextTBMargin(ScreenUtils.dp2px(1), ScreenUtils.dp2px(1));

        mCourseViewV2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("touch");
                return false;
            }
        });
        initCourseViewEvent();
    }

    /**
     * courseVIew事件
     */
    private void initCourseViewEvent() {
        mCourseViewV2.setOnItemClickListener(new CourseView.OnItemClickListener() {
            @Override
            public void onClick(List<CourseAncestor> course, View itemView) {
                mDialog = new ShowDetailDialog();
                mDialog.show(getActivity(), (CourseV2) course.get(0), new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mDialog = null;
                    }
                });
            }

            @Override
            public void onLongClick(List<CourseAncestor> courses, View itemView) {
                final CourseV2 course = (CourseV2) courses.get(0);
                DialogHelper dialogHelper = new DialogHelper();
                StringBuffer end = new StringBuffer();
                if (course.getCouNodeCount() > 1) {
                    end.append("-");
                    end.append(Constant.TIMES[course.getCouStartNode() + course.getCouNodeCount() - 2]);
                }
                dialogHelper.showNormalDialog(getActivity(), getString(R.string.confirm_to_delete),
                        "course 【" + course.getCouName() + "】" + Constant.WEEK[course.getCouWeek()]
                                + " " + Constant.TIMES[course.getCouStartNode() - 1] + end.toString(), new DialogListener() {
                            @Override
                            public void onPositive(DialogInterface dialog, int which) {
                                super.onPositive(dialog, which);
                                deleteCancelSnackBar(course);
                            }
                        });
            }

            public void onAdd(CourseAncestor course, View addView) {
                Intent intent = new Intent(getActivity(), AddClassActivity.class);
                intent.putExtra(Constant.INTENT_ADD_COURSE_ANCESTOR, course);
                intent.putExtra(Constant.INTENT_ADD, true);
                startActivity(intent);
            }

        });
    }

    /**
     * 撤销删除提示
     */
    private void deleteCancelSnackBar(final CourseV2 course) {
        course.setDisplayable(false);
        mCourseViewV2.resetView();
        Snackbar.make(mMMonthTextView, "delete success！", Snackbar.LENGTH_LONG).setAction("cancel",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                switch (event) {
                    case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                    case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                    case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                        //to do delete
                        mPresenter.deleteCourse(course.getCouId());
                        break;
                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                        //cancel
                        course.setDisplayable(true);
                        mCourseViewV2.resetView();
                        break;
                }
            }
        }).show();
    }

    @Override
    public void setCourseData(List<CourseV2> courses) {
        mCourseViewV2.clear();

        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();
        LogUtil.d(this, "当前课程数：" + courses.size());

        for (CourseV2 course : courses) {
            if (course.getCouColor() == null || course.getCouColor() == -1) {
                course.setCouColor(Utils.getRandomColor());
                courseV2Dao.update(course);
            }
            course.init();

            LogUtil.e(this, "即将显示：" + course.toString());

            mCourseViewV2.addCourse(course);
        }

        // 没有课程才显示logo
        //        if (courses.isEmpty()) {
        //            mLayoutCourse.setBackgroundResource(R.mipmap.logo);
        //        } else {
        //            mLayoutCourse.setBackgroundResource(0);
        //        }
    }

    private boolean checkUser() {
        if (user == null || "普通用户".equals(user.type)) {
            showToast("没有权限进行操作");
            return true;
        }
        return false;
    }
}
