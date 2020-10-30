package com.xds.project.ui.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.ui.SimItemBottomDialog;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.beanv2.Things;
import com.xds.project.util.ToastUtil;
import com.xds.project.util.event.ThingsEvent;
import com.xds.project.widget.PaperButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author .
 * @TODO 新增待办
 * @email
 */
public class AddThingsActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvName)
    EditText tvName;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.btModify)
    PaperButton btModify;
    private String id, typeId;
    private Things things;
    private List<SimItemBottomDialog.CommonEntity> commonEntities;
    private int level;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_modify;
    }

    @Override
    protected void initViews() {
        id = getIntent().getStringExtra("id");
        things = (Things) getIntent().getSerializableExtra("data");
        //        typeId = getIntent().getStringExtra("typeId");

        initToolBar(toobar, true, things == null ? "Add new item " : "Edit item");
        if (things != null) {
            tvType.setText(things.getLevelName());
            tvName.setText(things.getName());
            etContent.setText(things.getContent());
            level = things.getLevel();
        } else {
            tvType.setText("low");
        }
        btModify.setText(things == null ? "Add" : "Save");
        commonEntities = new ArrayList<SimItemBottomDialog.CommonEntity>() {
            {
                add(new SimItemBottomDialog.CommonEntity("low"));
                add(new SimItemBottomDialog.CommonEntity("middle"));
                add(new SimItemBottomDialog.CommonEntity("high"));
            }
        };
    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


    @OnClick({R.id.btModify, R.id.tvType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btModify:
                final String content = etContent.getText().toString().trim();
                final String name = tvName.getText().toString().trim();
                if (TextUtils.isEmpty(content) || TextUtils.isEmpty(name)) {
                    ToastUtil.show(getContext(), "Please complete the information");
                    return;
                }
                if (things == null) {
                    Things things = new Things();
                    things.setContent(content);
                    things.setLevel(level);
                    things.setName(name);
                    things.setDate(new Date());
                    things.setState(3);
                    Cache.instance().getThingsDao().insert(things);
                } else {
                    things.setContent(content);
                    things.setLevel(level);
                    things.setName(name);
                    Cache.instance().getThingsDao().update(things);
                }
                EventBus.getDefault().post(new ThingsEvent());
                finish();
                break;
            case R.id.tvType:
                SimItemBottomDialog dialog = SimItemBottomDialog.getDialog(getActivity(), commonEntities
                        , new OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                level = position;
                                tvType.setText(getLevelName(level));
                            }
                        });
                dialog.show();
                break;
        }

    }

    public String getLevelName(int level) {
        switch (level) {
            case 2:
                return "high";
            case 1:
                return "middle";
            default:
                return "low";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                typeId = data.getStringExtra("id");
                String typeName = data.getStringExtra("name");
                tvType.setText(typeName);
            }
        }
    }
}
