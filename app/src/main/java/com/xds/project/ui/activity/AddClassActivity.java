package com.xds.project.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.PreferencesUtils;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.app.Constant;
import com.xds.project.data.beanv2.CourseV2;
import com.xds.project.mvp.AddContract;
import com.xds.project.mvp.AddPresenter;
import com.xds.project.util.AppUtils;
import com.xds.project.util.LogUtil;
import com.xds.project.util.ScreenUtils;
import com.xds.project.util.event.CourseDataChangeEvent;
import com.xds.project.widget.PopupWindowDialog;
import com.xds.project.widget.custom.EditTextLayout;
import com.xds.project.widget.custom.course.CourseAncestor;

import org.greenrobot.eventbus.EventBus;

/**
 * @author .
 * @TODO 新增
 * @email
 */
public class AddClassActivity extends BaseActivity implements AddContract.View, View.OnClickListener {
    private AddContract.Presenter mPresenter;

    private boolean mAddMode = true;

    private CourseAncestor mAncestor;
    private CourseV2 mIntentCourseV2;
    private ImageView mIvAddLocation;
    private LinearLayout mLayoutLocationContainer;
    private ImageView mIvSubmit;
    private EditTextLayout mEtlName;
    private EditTextLayout mEtlTeacher;
    private Toolbar toobar;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_add;
    }

    @Override
    protected void initViews() {
        handleIntent();
        toobar = findViewById(R.id.toobar);
        mEtlName = findViewById(R.id.etl_name);
        mEtlTeacher = findViewById(R.id.etl_teacher);
        mIvAddLocation = findViewById(R.id.iv_add_location);
        mLayoutLocationContainer = findViewById(R.id.layout_location_container);
        mIvSubmit = findViewById(R.id.iv_submit);
        mIvAddLocation.setImageResource(R.drawable.ic_add_black_24dp);
        mIvSubmit.setImageResource(R.drawable.ic_done_black_24dp);
        initToolBar(toobar, true, mAddMode ? "New courses" : "Edit course");
        addLocation(false);

        new AddPresenter(this).start();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        mAncestor = (CourseAncestor) intent.getSerializableExtra(Constant.INTENT_ADD_COURSE_ANCESTOR);
        if (mAncestor != null) {
            mAddMode = true;
        } else {
            mIntentCourseV2 = (CourseV2) intent.getSerializableExtra(Constant.INTENT_EDIT_COURSE);
            if (mIntentCourseV2 != null) {
                mAddMode = false; //is edit mode
                mIntentCourseV2.init();// 从桌面点击过来必然已经初始化 其他位置过来不一定
            }
        }
    }

    @Override
    protected void initAction() {
        mIvAddLocation.setOnClickListener(this);
        mIvSubmit.setOnClickListener(this);
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_location:
                addLocation(true);
                break;
            case R.id.iv_submit:
                submit();
                break;
        }
    }

    private void submit() {
        //name
        String name = mEtlName.getText().trim();
        if (TextUtils.isEmpty(name)) {
            showToast(getString(R.string.tip_add_class_empty));
            return;
        }

        //teacher
        String teacher = mEtlTeacher.getText().trim();
        //group

        long couCgId = PreferencesUtils.getLong(getContext(), getString(R.string.app_preference_current_cs_name_id), 0);
        int childCount = mLayoutLocationContainer.getChildCount();
        boolean hasLocation = false;
        for (int i = 0; i < childCount; i++) {
            View locationItem = mLayoutLocationContainer.getChildAt(i);
            Object obj = locationItem.getTag();

            if (obj != null) {
                hasLocation = true;
                CourseV2 courseV2 = (CourseV2) obj;
                courseV2.setCouName(name);
                courseV2.setCouTeacher(teacher);

                if (mAddMode || courseV2.getCouId() == null) {
                    courseV2.setCouCgId(couCgId);
                    courseV2.init();
                    Cache.instance().getCourseV2Dao().insert(courseV2);
                } else {
                    courseV2.init();
                    Cache.instance().getCourseV2Dao().update(courseV2);
                }

            }
        }
        if (!hasLocation) {
            showToast(getString(R.string.tip_not_class_time));
        }

        if (mAddMode) {
            showToast(getString(R.string.tip_add_success));
        } else {
            showToast(getString(R.string.tip_edit_success));
        }
        EventBus.getDefault().post(new CourseDataChangeEvent());
        finish();
    }

    private void addLocation(boolean closeable) {
        final LinearLayout locationItem = (LinearLayout) View.inflate(this,
                R.layout.layout_location_item, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = ScreenUtils.dp2px(8);

        if (closeable) {
            locationItem.findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLayoutLocationContainer.removeView(locationItem);
                }
            });

            initEmptyLocation(locationItem);

        } else {// 建立默认的上课时间和上课地点
            locationItem.findViewById(R.id.iv_clear).setVisibility(View.INVISIBLE);

            if (mAncestor != null) {
                // 屏幕点击过来

                CourseV2 defaultCourse = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                        .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                        .setCouWeek(mAncestor.getRow())
                        .setCouStartNode(mAncestor.getCol())
                        .setCouNodeCount(mAncestor.getRowNum())
                        .init();

                initNodeInfo(locationItem, defaultCourse);
            } else if (mIntentCourseV2 != null) {
                // 编辑过来
                initNodeInfo(locationItem, mIntentCourseV2);

                mEtlName.setText(mIntentCourseV2.getCouName());
                mEtlTeacher.setText(mIntentCourseV2.getCouTeacher());
            } else {
                //
                LogUtil.e(this, "initEmptyLocation");
                initEmptyLocation(locationItem);
            }
        }

        locationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLocationItem(locationItem);
            }
        });

        mLayoutLocationContainer.addView(locationItem, params);
    }

    private void initEmptyLocation(LinearLayout locationItem) {
        CourseV2 defaultCourse = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                .setCouWeek(1)
                .setCouStartNode(1)
                .setCouNodeCount(1);
        initNodeInfo(locationItem, defaultCourse);
    }

    private void initNodeInfo(LinearLayout locationItem, CourseV2 courseV2) {
        TextView tvText = locationItem.findViewById(R.id.tv_text);
        String Node = Constant.TIMES[courseV2.getCouStartNode() - 1] + "-" + Constant.TIMES[courseV2.getCouStartNode() + courseV2.getCouNodeCount() - 2];
//        String builder = "周" + Constant.WEEK_SINGLE[courseV2.getCouWeek() - 1] +
//                " 第" + courseV2.getCouStartNode() + "-" +
//                (courseV2.getCouStartNode() + courseV2.getCouNodeCount() - 1) + "节";
        String builder = Constant.WEEK_SINGLE[courseV2.getCouWeek() - 1] +
                " " + Node;
        tvText.setText(builder);

        locationItem.setTag(courseV2);
    }

    private void clickLocationItem(final LinearLayout locationItem) {
        PopupWindowDialog dialog = new PopupWindowDialog();

        CourseV2 courseV2 = null;
        Object obj = locationItem.getTag();
        // has tag data
        if (obj != null && obj instanceof CourseV2) {
            courseV2 = (CourseV2) obj;
        } else {
            throw new RuntimeException("Course data tag not be found");
        }

        dialog.showSelectTimeDialog(this, courseV2, new PopupWindowDialog.SelectTimeCallback() {
            @Override
            public void onSelected(CourseV2 course) {
                StringBuilder builder = new StringBuilder();
//                builder.append("周").append(Constant.WEEK_SINGLE[course.getCouWeek() - 1])
//                        .append(" 第").append(course.getCouStartNode()).append("-")
//                        .append(course.getCouStartNode() + course.getCouNodeCount() - 1).append("节");
                builder.append(Constant.WEEK_SINGLE[course.getCouWeek() - 1])
                        .append(" ").append(Constant.TIMES[course.getCouStartNode() - 1]).append("-")
                        .append(Constant.TIMES[course.getCouStartNode() + course.getCouNodeCount() - 2]);
                if (!TextUtils.isEmpty(course.getCouLocation())) {
                    builder.append("【").append(course.getCouLocation()).append("】");
                }

                ((TextView) locationItem.findViewById(R.id.tv_text))
                        .setText(builder.toString());
            }
        });
    }

    @Override
    public void showAddFail(String msg) {

    }

    @Override
    public void onAddSucceed(CourseV2 courseV2) {

    }

    @Override
    public void onDelSucceed() {

    }

    @Override
    public void onUpdateSucceed(CourseV2 courseV2) {

    }

    @Override
    public void setPresenter(AddContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

//
//    @OnClick({R.id.btAdd, R.id.btUpLoadLocal})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btAdd:
//                RxPermissions rxPermissions = new RxPermissions(AddClassActivity.this);
//                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        if (aBoolean) {
//                            //申请的权限全部允许
////                            showToast.makeText(SearchActivity.this, "允许了权限!", showToast.LENGTH_SHORT).showToast();
//                            new LFilePicker()
//                                    .withActivity(AddClassActivity.this)
//                                    .withMutilyMode(false)//单选
//                                    .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
//                                    .start();
//                        } else {
//                            //只要有一个权限被拒绝，就会执行
//                            showToast.makeText(AddClassActivity.this, "未授权权限，功能不能使用", showToast.LENGTH_SHORT).showToast();
//                        }
//                    }
//                });
//
//                break;
//        }
//    }

}
