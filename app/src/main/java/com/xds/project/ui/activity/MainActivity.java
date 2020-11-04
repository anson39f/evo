package com.xds.project.ui.activity;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import butterknife.BindView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.project.R;
import com.xds.project.ui.fragment.CourseFragment;
import com.xds.project.ui.fragment.MeFragment;
import com.xds.project.ui.fragment.SelfStudyFragment;
import com.xds.project.ui.fragment.ToDoListFragment;
import io.reactivex.functions.Consumer;


/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private int mLastPage;
    private MainPagerAdapter adapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void initViews() {

//        StatusBarUtils.setStatusFullScreen(MainActivity.this);
        //        disableShiftMode(navigation);
        //        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.setOffscreenPageLimit(4);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (0 == position) {
                    navigation.setSelectedItemId(R.id.navigation_home);
                } else if (1 == position) {
                    navigation.setSelectedItemId(R.id.navigation_do_list);
                } else if (2 == position) {
                    navigation.setSelectedItemId(R.id.navigation_self_study);
                } else if (3 == position) {
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
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                } else {
                    //只要有一个权限被拒绝，就会执行
                    showToast(getString(R.string.tip_authorization_miss));
                }
            }
        });
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
                    viewPager.setCurrentItem(0, false);
                    return true;
                case R.id.navigation_do_list:
                    viewPager.setCurrentItem(1, false);
                    return true;
                case R.id.navigation_self_study:
                    viewPager.setCurrentItem(2, false);
                    return true;
                case R.id.navigation_person:
                    viewPager.setCurrentItem(3, false);
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
                return Fragment.instantiate(getContext(), CourseFragment.class.getName());
            } else if (position == 1) {
                return Fragment.instantiate(getContext(), ToDoListFragment.class.getName());
            } else if (position == 2) {
                return Fragment.instantiate(getContext(), SelfStudyFragment.class.getName());
            } else if (position == 3) {
                return Fragment.instantiate(getContext(), MeFragment.class.getName());
            }
            return Fragment.instantiate(getContext(), CourseFragment.class.getName());
        }

        @Override
        public int getCount() {
            return 4;
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //需要把touch事件传给dragHelper，true表示消耗掉事件
        //需要保证在Activity或者外层的ViewGroup或可以拦截Touch事件的地方回调都可以
        if (adapter.getCurrentFragment() instanceof ToDoListFragment) {
            return ((ToDoListFragment) adapter.getCurrentFragment()).dispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
