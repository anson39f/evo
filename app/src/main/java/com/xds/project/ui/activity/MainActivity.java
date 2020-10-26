package com.xds.project.ui.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.project.R;
import com.xds.project.ui.fragment.HomeFragment;
import com.xds.project.ui.fragment.MeFragment;
import com.xds.project.util.StatusBarUtils;

import butterknife.BindView;


/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private int mLastPage;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void initViews() {

        StatusBarUtils.setStatusFullScreen(MainActivity.this);
        //        disableShiftMode(navigation);
        //        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position) {
                    navigation.setSelectedItemId(R.id.navigation_home);
                } else if (1 == position) {
                    navigation.setSelectedItemId(R.id.navigation_person);
                }
                mLastPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLastPage = viewPager.getCurrentItem();
    }

    @Override
    protected void initAction() {

    }

    @Override
    protected void updateViews(boolean isRefresh) {

    }


    /**
     * 底部栏监听器
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_person:
                    viewPager.setCurrentItem(1, true);
                    return true;
            }
            return false;
        }
    };


    /**
     * 主页面适配器
     */
    class MainPagerAdapter extends FragmentPagerAdapter {
        public BaseFragment mCurrentFragment;

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return Fragment.instantiate(getContext(), HomeFragment.class.getName());
            } else if (position == 1) {
                return Fragment.instantiate(getContext(), MeFragment.class.getName());
            }
            return Fragment.instantiate(getContext(), HomeFragment.class.getName());
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentFragment = (BaseFragment) object;
            super.setPrimaryItem(container, position, object);
        }


        public BaseFragment getCurrentFragment() {
            return mCurrentFragment;
        }

    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 菜单点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_location:
                break;
            case R.id.action_upload:
                //检查文件读取权限

                break;
        }
        return super.onOptionsItemSelected(item);

    }



}
