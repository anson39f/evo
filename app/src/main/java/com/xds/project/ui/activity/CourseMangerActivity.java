package com.xds.project.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.JsonParser;
import com.xds.base.utils.PreferencesUtils;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.beanv2.CourseV2;
import com.xds.project.data.greendao.CourseV2Dao;
import com.xds.project.util.event.CourseDataChangeEvent;
import io.reactivex.functions.Consumer;
import org.greenrobot.eventbus.EventBus;

import java.io.*;
import java.util.List;

public class CourseMangerActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    @BindView(R.id.ll_import)
    LinearLayout ll_import;
    private RxPermissions rxPermissions;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_courses_manger;
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "Course Manger");

        rxPermissions = new RxPermissions(getActivity());
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @OnClick({R.id.ll_share, R.id.ll_import})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_share:
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            toShare();
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            showToast(getString(R.string.tip_authorization_miss));
                        }
                    }
                });
                break;
            case R.id.ll_import:
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            final File file = new File(Environment.getExternalStorageDirectory(), "/bluetooth");
                            final File courseFile = new File(file, "course.txt");
                            if (file == null || courseFile == null || !courseFile.exists()) {
                                showToast(courseFile + " does not exist");
                                return;
                            }
                            String courseJson = getCourseString(courseFile);
                            List<CourseV2> list = JsonParser.parseJsonList(courseJson, CourseV2.class);
                            try {
                                for (CourseV2 courseV2 : list) {
                                    courseV2.setCouId(null);
                                }
                                Cache.instance().getCourseV2Dao().insertInTx(list);
                                EventBus.getDefault().post(new CourseDataChangeEvent());
                                showToast("Import success");
                            } catch (Exception e) {
                                showToast("Bluetooth file import fail");
                            }
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            showToast(getString(R.string.tip_authorization_miss));
                        }
                    }
                });
                break;
        }
    }

    private void toShare() {
        //调用android分享窗口
        final File file = new File(Environment.getExternalStorageDirectory(), "/EvoClass");
        if (!file.exists()) {
            file.mkdirs();
        }
        final File courseFile = new File(file, "course.txt");
        //get id
        long currentCsNameId = PreferencesUtils.getLong(getActivity(), getString(R.string.app_preference_current_cs_name_id), 0L);
        List<CourseV2> courses = Cache.instance().getCourseV2Dao()
                .queryBuilder()
                .where(CourseV2Dao.Properties.CouCgId.eq(currentCsNameId))//根据当前课表组id查询
                .where(CourseV2Dao.Properties.CouDeleted.eq(false))//查询没有删除的
                .list();
        writeCourse(courseFile, JsonParser.toJson(courses));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.setPackage("com.android.bluetooth");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(courseFile));//path为文件的路径
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Intent chooser = Intent.createChooser(intent, "Share app");
                chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooser);
            }
        }, 200);
    }


    private void writeCourse(File file, String message) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file), 1024);
            bufferedWriter.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null)
                try {
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public String getCourseString(File file) {
        if (!file.exists())
            return null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String read = "";
            String line;
            while ((line = reader.readLine()) != null) {
                read += line;
            }
            return read;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
