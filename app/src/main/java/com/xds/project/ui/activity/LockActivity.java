package com.xds.project.ui.activity;

import android.view.KeyEvent;
import android.view.View;

import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.util.StatusBarUtils;
import com.xds.project.widget.PaperButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author .
 * @TODO 锁机学习
 * @email
 */
public class LockActivity extends BaseActivity {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    @BindView(R.id.btTime)
    PaperButton btTime;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_lock_study;
    }

    @Override
    protected void initViews() {
        StatusBarUtils.setStatusFullScreen(LockActivity.this);
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }

    @Override
    public void onAttachedToWindow() {
        //        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
                getWindow().addFlags(5);

        this.getWindow().addFlags(FLAG_HOMEKEY_DISPATCHED);
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 返回true，不响应其他key
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
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

}
