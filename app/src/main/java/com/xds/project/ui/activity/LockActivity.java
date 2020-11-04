package com.xds.project.ui.activity;

import android.Manifest;
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
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.app.Constant;
import com.xds.project.data.beanv2.SelfStudy;
import com.xds.project.service.ScreenListenerService;
import com.xds.project.util.StatusBarUtils;
import com.xds.project.util.event.UserEvent;
import com.xds.project.widget.DialogHelper;
import com.xds.project.widget.DialogListener;
import com.xds.project.widget.PaperButton;
import io.reactivex.functions.Consumer;
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
    @BindView(R.id.btExit)
    PaperButton btExit;
    private SelfStudy selfStudy;
    private int sec = 60;
    private int min;
    private int mHasFocusTime;
    private boolean mHasFocus;
    private boolean over;
    private boolean fail;
    private boolean isLock;

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
        selfStudy = (SelfStudy) getIntent().getSerializableExtra("data");
        if (selfStudy == null) {
            return;
        }
        min = selfStudy.getMinute();
        sec = selfStudy.getSecond();
//        min = 0;
//        sec = 10;
        btTime.setText(String.format("%02d:%02d", min, sec));
        if (selfStudy.getModel() == 0) {
            isLock = true;
            Intent service = new Intent(this, ScreenListenerService.class);
            startService(service);
        }
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                } else {
                    //只要有一个权限被拒绝，就会执行
                    showToast("The function cannot be used without authorization");
                }
            }
        });
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (over || fail) {
                    return;
                }
                if (mHasFocusTime >= 60) {
                    showOverTimeDialog();
                    return;
                } else {
                    mHasFocusTime = 0;
                }
                if (sec == 0) {
                    if (min == 0) {
                        //                            showToast("Over");
                        over = true;
                        btTime.setText(String.format("%02d:%02d", min, sec));
                        stopService(new Intent(getActivity(), ScreenListenerService.class));
                        DialogHelper dialogHelper = new DialogHelper();
                        dialogHelper.showNormalDialog(getActivity(), "Success?", "You studied for " + selfStudy.getMinute() + " minute ", new DialogListener() {
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
                        selfStudy.setState(1);
                        Cache.instance().getSelfStudyDao().insert(selfStudy);
                        return;
                    } else {
                        min--;
                    }
                    sec = 59;
                } else {
                    sec--;
                }
                btTime.setText(String.format("%02d:%02d", min, sec));
                if (!mHasFocus) {
                    mHasFocusTime++;
                }
                updateViews(true);
            }

        }, 1000);
    }

    private void showOverTimeDialog() {
        fail = true;
        stopService(new Intent(getActivity(), ScreenListenerService.class));
        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showNormalDialog(getActivity(), "Fail", "exit the focus model more than 1 minute", new DialogListener() {
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
                selfStudy.setState(0);
                Cache.instance().getSelfStudyDao().insert(selfStudy);
                finish();
            }
        });
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

        if (isLock) {
            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(getTaskId(), 0);
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mHasFocus = hasFocus;
        if (fail || over) {
            return;
        }
        if (!hasFocus && isLock) {
            //            if (selfStudy != null && selfStudy.getModel() == 0) {
            //在recent按下时 发送一个recent事件 使recent失效
            Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            intent.putExtra("reason", "globalactions");
            sendBroadcast(intent);

            Intent in = new Intent(getContext(), LockActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra(Constant.JUMP_FROM, Constant.JUMP_FROM_SCREEN_LISTENER);
            startActivity(in);
            //            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (over || fail) {
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_HOME) {
            exitStudy();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitStudy() {
        DialogHelper dialogHelper = new DialogHelper();
        dialogHelper.showNormalDialog(getActivity(), "Exit?", "Are your sure to exit the focus model?" +
                "Once you exit,you will not receive any reward point", new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                dialog.dismiss();
                fail = true;
                if (selfStudy != null) {
                    selfStudy.setState(0);
                    Cache.instance().getSelfStudyDao().insert(selfStudy);
                }
                stopService(new Intent(getActivity(), ScreenListenerService.class));
                isLock = false;
                finish();
            }

            @Override
            public void onNegative(DialogInterface dialog, int which) {
                super.onNegative(dialog, which);
                dialog.dismiss();

            }
        });
    }

    @OnClick({R.id.btExit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btExit:
                exitStudy();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getActivity(), ScreenListenerService.class));
    }
}
