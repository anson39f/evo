package com.xds.project.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.ui.SimItemBottomDialog;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.data.beanv2.SelfStudy;
import com.xds.project.util.ToastUtil;
import com.xds.project.widget.PaperButton;
import com.xds.project.widget.TimePopupWindowDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author .
 * @TODO 新增学习
 * @email
 */
public class AddStudyActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.btModify)
    PaperButton btModify;
    private List<SimItemBottomDialog.CommonEntity> commonEntities;
    private int level;
    private int mTimeStart;
    private int mTimeEnd;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_add_self_study;
    }

    @Override
    protected void initViews() {

        initToolBar(toobar, true, "Prepare For your Study");
        commonEntities = new ArrayList<SimItemBottomDialog.CommonEntity>() {
            {
//                add(new SimItemBottomDialog.CommonEntity("low mode"));
//                add(new SimItemBottomDialog.CommonEntity("middle mode"));
                add(new SimItemBottomDialog.CommonEntity("focus mode"));
            }
        };
        tvType.setText("focus mode");
    }

    @Override
    protected void initAction() {
//        RxPermissions rxPermissions = new RxPermissions(this);
//        rxPermissions.request("android.permission.SYSTEM_ALERT_WINDOW").subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean aBoolean) throws Exception {
//                if (aBoolean) {
//                } else {
//                    //只要有一个权限被拒绝，就会执行
//                    showToast( "未授权权限，功能不能使用");
//                }
//            }
//        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


    @OnClick({R.id.btModify, R.id.tvType, R.id.tvTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btModify:
                final String content = etContent.getText().toString().trim();
                final String mode = tvType.getText().toString().trim();
                final String time = tvTime.getText().toString().trim();
                if (TextUtils.isEmpty(content) || TextUtils.isEmpty(mode) || TextUtils.isEmpty(time)) {
                    ToastUtil.show(getContext(), "Please complete the information");
                    return;
                }
                Intent intent = new Intent(getActivity(),
                        LockActivity.class);
                SelfStudy selfStudy = new SelfStudy();
                selfStudy.setContent(content);
                selfStudy.setModel(3);
                selfStudy.setMinute(this.mTimeStart);
                selfStudy.setSecond(this.mTimeEnd);
                selfStudy.setDate(new Date());
                intent.putExtra("data", selfStudy);
                startActivity(intent);
                finish();
                break;
            case R.id.tvType:
                SimItemBottomDialog dialog = SimItemBottomDialog.getDialog(getActivity(), commonEntities
                        , new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                level = position;
                                tvType.setText(getModelName(level));
                            }
                        });
                dialog.show();
                break;
            case R.id.tvTime:
                TimePopupWindowDialog timePopupWindowDialog = new TimePopupWindowDialog();
                timePopupWindowDialog.showSelectTimeDialog(getActivity(), new TimePopupWindowDialog.SelectTimeCallback() {

                    @Override
                    public void onSelected(int mTimeStart, int mTimeEnd) {
                        AddStudyActivity.this.mTimeStart = mTimeStart;
                        AddStudyActivity.this.mTimeEnd = mTimeEnd;
                        tvTime.setText(String.format("%02d:%02d", mTimeStart, mTimeEnd));
                    }
                });
                break;
        }

    }

    public String getModelName(int level) {
        switch (level) {
            case 2:
                return "focus mode";
            case 1:
                return "middle mode";
            default:
                return "low mode";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
