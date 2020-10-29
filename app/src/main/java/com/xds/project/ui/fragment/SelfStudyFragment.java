package com.xds.project.ui.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xds.base.ui.fragment.BaseFragment;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.AddStudyActivity;
import com.xds.project.ui.activity.SearchActivity;
import com.xds.project.util.ActivityTools;
import com.xds.project.widget.PaperButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author .
 * @TODO 自学室
 * @email
 */
public class SelfStudyFragment extends BaseFragment {

    @BindView(R.id.start)
    PaperButton start;
    @BindView(R.id.history)
    PaperButton history;
    private User user;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_self_study;
    }

    @Override
    public void initUI() {
        user = BaseApplication.getUser();
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Self-Study Room");
//        toolbar.setTitle("Prepare For your Study");
//        toolbar.inflateMenu(R.menu.toolbar_main);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if (item.getItemId() == R.id.action_set) {
//                    Intent intent = new Intent(getActivity(),
//                            AddThingsActivity.class);
//                    startActivity(intent);
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {
    }

    @OnClick({R.id.start, R.id.history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start:
                ActivityTools.startToNextActivity(getActivity(), AddStudyActivity.class);
                break;
            case R.id.history:
                ActivityTools.startToNextActivity(getActivity(), SearchActivity.class);
                break;
        }
    }

    private boolean checkUser() {
        if (user == null || "普通用户".equals(user.type)) {
            showToast("没有权限进行操作");
            return true;
        }
        return false;
    }
}
