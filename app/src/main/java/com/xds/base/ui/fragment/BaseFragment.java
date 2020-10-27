package com.xds.base.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.xds.base.ui.IBaseView;
import com.xds.project.R;
import com.xds.project.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author
 * @todo fragment基类
 * @email
 * @date
 */
public abstract class BaseFragment extends RxFragment implements IBaseView {
    /**
     * 注意，资源的ID一定要一样
     */

    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    private FragmentManager fragmentManager;

    //缓存Fragment view
    protected View main;
    protected boolean mIsMulti = false;
    private Unbinder unbinder;

    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        fragmentManager = getFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main = inflater.inflate(attachLayoutRes(), container, false);
        unbinder = ButterKnife.bind(this, main);
        initUI();
        mIsMulti = false;
        return main;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAction();
        if (getUserVisibleHint() && main != null && !mIsMulti) {
            mIsMulti = true;
            updateViews(false);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible() && main != null && !mIsMulti) {
            mIsMulti = true;
            updateViews(false);
        }
    }

    @Override
    public void showLoading() {
        showDialog();
    }

    @Override
    public void hideLoading() {
        hideDialog();
    }


    @Override
    public void showDialog() {
        if (null == dialog || !dialog.isShowing()) {
            dialog = new Dialog(mContext, R.style.loading_dialog);
            dialog.setContentView(R.layout.load_dialog);
            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    @Override
    public <M> LifecycleTransformer<M> bindToLife() {
        return bindUntilEvent(FragmentEvent.DESTROY_VIEW);
    }


    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    protected abstract int attachLayoutRes();

    /**
     * 控件初始化
     */
    public abstract void initUI();

    /**
     * 事件初始化
     */
    public abstract void initAction();

    /**
     * 更新视图控件
     *
     * @param isRefresh 新增参数，用来判断是否为下拉刷新调用，下拉刷新的时候不应该再显示加载界面和异常界面
     */
    protected abstract void updateViews(boolean isRefresh);

    public FragmentManager getSuperFragmentManager() {
        return fragmentManager;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideDialog();
        unbinder.unbind();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.show(getContext(), msg);
    }


    @Override
    public void finish() {
        if (getActivity() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (getActivity().isDestroyed()) {
                    return;
                }
            }
            if (getActivity().isFinishing()) {
                return;
            }
            getActivity().finish();
        }
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return main.findViewById(id);
    }

    @Override
    public boolean isDestroyed() {
        if (getActivity() != null) {
            return ((IBaseView) getActivity()).isDestroyed();
        }
        return true;
    }
}
