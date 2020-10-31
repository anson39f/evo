package com.xds.project.ui.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.app.Constant;
import com.xds.project.data.beanv2.SelfStudy;
import com.xds.project.service.ScreenListenerService;
import com.xds.project.util.LogUtil;
import com.xds.project.util.StatusBarUtils;
import com.xds.project.util.event.UserEvent;
import com.xds.project.widget.DialogHelper;
import com.xds.project.widget.DialogListener;
import com.xds.project.widget.PaperButton;
import org.greenrobot.eventbus.EventBus;

import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;

/**
 * @author .
 * @TODO 锁机学习
 * @email
 */
public class LockActivity extends BaseActivity {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    @BindView(R.id.btTime)
    PaperButton btTime;
    private SelfStudy selfStudy;
    private int sec = 60;
    private int min;
    private int hour;
    private int mHasFocusTime;
    private boolean mHasFocus;
    private boolean over;
    private boolean fail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(0x00020000 | FLAG_SHOW_WHEN_LOCKED | FLAG_DISMISS_KEYGUARD);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_lock_study;
    }

    @Override
    protected void initViews() {
        StatusBarUtils.setStatusFullScreen(LockActivity.this);
        Intent service = new Intent(this, ScreenListenerService.class);
        startService(service);
        selfStudy = (SelfStudy) getIntent().getSerializableExtra("data");
        if (selfStudy == null) {
            return;
        }
        hour = selfStudy.getMinute();
        min = selfStudy.getSecond();
        btTime.setText(String.format("%02d:%02d", hour, min));
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                sec--;
                if (sec % 5 == 0) {
                    LogUtil.d("倒计时：", String.valueOf(sec));
                }
                if (sec == 0) {
                    sec = 60;
                } else {
                    updateViews(true);
                    return;
                }
                if (over || fail) {
                    return;
                }
                if (mHasFocus) {
                    mHasFocusTime = 0;
                    min--;
                    if (min == 0) {
                        if (hour == 0) {
//                            showToast("Over");
                            over = true;
                            btTime.setText(String.format("%02d:%02d", hour, min));
                            stopService(new Intent(getActivity(), ScreenListenerService.class));
                            DialogHelper dialogHelper = new DialogHelper();
                            dialogHelper.showNormalDialog(getActivity(), "Success?", "You studied for " + selfStudy.getMinute() + " hours " + selfStudy.getSecond() + " minutes ", new DialogListener() {
                                @Override
                                public void onPositive(DialogInterface dialog, int which) {
                                    super.onPositive(dialog, which);
                                    dialog.dismiss();
                                    EventBus.getDefault().post(new UserEvent());
                                    finish();
                                }

                                @Override
                                public void onNegative(DialogInterface dialog, int which) {
                                    super.onNegative(dialog, which);
                                    dialog.dismiss();
                                    EventBus.getDefault().post(new UserEvent());
                                    finish();
                                }
                            });
                            Cache.instance().getSelfStudyDao().insert(selfStudy);
                            return;
                        } else {
                            min = 60;
                            hour--;
                        }
                    }
                    btTime.setText(String.format("%02d:%02d", hour, min));
                } else {
                    mHasFocusTime++;
                    if (mHasFocusTime >= 60) {
                        fail = true;
                        stopService(new Intent(getActivity(), ScreenListenerService.class));
                        DialogHelper dialogHelper = new DialogHelper();
                        dialogHelper.showNormalDialog(getActivity(), "Fail", "exit the focus model more than 1 hour", new DialogListener() {
                            @Override
                            public void onPositive(DialogInterface dialog, int which) {
                                super.onPositive(dialog, which);
                                dialog.dismiss();
                                finish();
                            }

                            @Override
                            public void onNegative(DialogInterface dialog, int which) {
                                super.onNegative(dialog, which);
                                dialog.dismiss();
                                finish();
                            }
                        });
                        return;
                    }
                }
                updateViews(true);
            }
        }, 1000);
    }

    @Override
    public void onAttachedToWindow() {
        //        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        getWindow().addFlags(5);
//
//        this.getWindow().addFlags(FLAG_HOMEKEY_DISPATCHED);
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        // 返回true，不响应其他key
//        return true;
//    }

    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mHasFocus = hasFocus;
        if (fail || over) {
            return;
        }
        if (!hasFocus) {
            //在recent按下时 发送一个recent事件 使recent失效
            Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            intent.putExtra("reason", "globalactions");
            sendBroadcast(intent);

//            ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                    .getSystemService(Context.ACTIVITY_SERVICE);
//            activityManager.moveTaskToFront(getTaskId(), 0);

            Intent in = new Intent(getContext(), LockActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra(Constant.JUMP_FROM, Constant.JUMP_FROM_SCREEN_LISTENER);
            startActivity(in);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (over || fail) {
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_HOME) {
            DialogHelper dialogHelper = new DialogHelper();
            dialogHelper.showNormalDialog(getActivity(), "Exit?", "Are your sure to exit the focus model?" +
                    "Once you exit,you will not receive any reward point", new DialogListener() {
                @Override
                public void onPositive(DialogInterface dialog, int which) {
                    super.onPositive(dialog, which);
                    finish();
                }

                @Override
                public void onNegative(DialogInterface dialog, int which) {
                    super.onNegative(dialog, which);
                    dialog.dismiss();
                    finish();
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.btTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btTime:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getActivity(), ScreenListenerService.class));
    }
}
