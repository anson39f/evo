package com.xds.project.ui.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.*;
import com.xds.project.util.ActivityTools;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author .
 * @TODO 主页
 * @email
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_testList)
    TextView tvSignin;
    @BindView(R.id.llUser)
    LinearLayout llUser;
    private User user;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    public void initUI() {
        user = BaseApplication.getUser();
        llUser.setVisibility(View.VISIBLE);
        //显示广告
        List<Object> urls = new ArrayList<>();
        urls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2629587401,1371372309&fm=26&gp=0.jpg");
        urls.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3441631269,1764602770&fm=11&gp=0.jpg");
        urls.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=377301419,2465900538&fm=26&gp=0.jpg");
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                String url = (String) path;
                Glide.with(getContext()).load(url).into(imageView);
            }
        });
        banner.setDelayTime(5000);
        banner.update(urls);
        banner.start();
    }

    @Override
    public void initAction() {
    }

    @Override
    protected void updateViews(boolean isRefresh) {
    }

    @OnClick({R.id.ivSearch, R.id.tv_testList, R.id.tvNewTest, R.id.tvKu, R.id.tvAddType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSearch:
                ActivityTools.startToNextActivity(getActivity(), SearchActivity.class);
                break;
            case R.id.tv_testList:
                ActivityTools.startToNextActivity(getActivity(), TestCaseListActivity.class);
                break;
            case R.id.tvNewTest:
                if (checkUser())
                    return;
                ActivityTools.startToNextActivity(getActivity(), AddClassActivity.class);
                break;
            case R.id.tvKu:
                if (checkUser())
                    return;
                ActivityTools.startToNextActivity(getActivity(), AddKuActivity.class);
                break;
            case R.id.tvAddType:
                if (checkUser())
                    return;
                ActivityTools.startToNextActivity(getActivity(), AddTypeActivity.class);
                break;
        }
    }

    private boolean checkUser() {
        if (user == null || "普通用户".equals(user.getType())) {
            showToast("没有权限进行操作");
            return true;
        }
        return false;
    }
}
