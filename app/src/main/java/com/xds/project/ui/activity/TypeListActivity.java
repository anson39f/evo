package com.xds.project.ui.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.base.net.RetrofitService;
import com.xds.base.ui.activity.BaseActivity;
import com.xds.base.utils.BlankUtil;
import com.xds.project.R;
import com.xds.project.api.remote.BaseService;
import com.xds.project.entity.TypeBean;
import com.xds.project.ui.adapter.TypeListAdapter;
import com.xds.project.ui.adapter.TypeTreeListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 分类列表
 *
 * @author .
 * @email
 */
public class TypeListActivity extends BaseActivity {
    @BindView(R.id.toobar)
    Toolbar toobar;
    @BindView(R.id.recyclerview1)
    RecyclerView recyclerview1;
    @BindView(R.id.recyclerview2)
    RecyclerView recyclerview2;
    @BindView(R.id.recyclerview3)
    RecyclerView recyclerview3;
    private TypeListAdapter adapter1, adapter2, adapter3;
    private int type;
    private TypeTreeListAdapter treeAdapter;

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_type_list;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initViews() {
        initToolBar(toobar, true, "分类列表");
        type = getIntent().getIntExtra("type", 0);
        adapter1 = new TypeListAdapter(this);
        treeAdapter = new TypeTreeListAdapter(this);
//        RecyclerViewHelper.initRecyclerViewV(getContext(), recyclerview1, true, adapter1);
        RecyclerViewHelper.initRecyclerViewV(getContext(), recyclerview1, true, treeAdapter);
        adapter2 = new TypeListAdapter(this);
        RecyclerViewHelper.initRecyclerViewV(getContext(), recyclerview2, true, adapter2);
        adapter3 = new TypeListAdapter(this);
        RecyclerViewHelper.initRecyclerViewV(getContext(), recyclerview3, true, adapter3);
    }

    @Override
    protected void initAction() {
        adapter1.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TypeBean bean = adapter1.getItem(position);
                adapter2.cleanItems();
                adapter3.cleanItems();
                getList(bean.id, adapter2);

            }
        });
        adapter2.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (type == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("id", adapter2.getItem(position).id);
                    intent.putExtra("name", adapter2.getItem(position).name);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    TypeBean bean = adapter2.getItem(position);
                    adapter3.cleanItems();
                    getList(bean.id, adapter3);
                }
            }
        });
        adapter3.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("id", adapter3.getItem(position).id);
                intent.putExtra("name", adapter3.getItem(position).name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        adapter1.setOnSubClickListener(new TypeListAdapter.OnSubClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("id", adapter1.getItem(position).id);
                intent.putExtra("name", adapter1.getItem(position).name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        adapter2.setOnSubClickListener(new TypeListAdapter.OnSubClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("id", adapter2.getItem(position).id);
                intent.putExtra("name", adapter2.getItem(position).name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        treeAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TypeBean bean = treeAdapter.getItem(position);
                if (bean.show) {
                    if (bean.child != null) {
                        treeAdapter.getData().removeAll(bean.child);
//                        treeAdapter.notifyItemMoved(position + 1, bean.child.size());
                        treeAdapter.notifyDataSetChanged();
                    }
                } else {
                    if (bean.child == null) {
                        getList(position);
                    } else {
                        treeAdapter.getData().addAll(position + 1, bean.child);
                        treeAdapter.notifyItemChanged(position + 1, bean.child.size());
                    }
                }
            }
        });
        //        adapter3.setOnSubClickListener(new TypeListAdapter.OnSubClickListener() {
        //            @Override
        //            public void onItemClick(View 3view, int position) {
        //                TypeBean bean = adapter.getItem(position);
        //                getList(bean.id, adapter3);
        //            }
        //        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        //        BaseAppApi.selectAllByType("1", Integer.MAX_VALUE + "", new HttpListener<List<TypeBean>>() {
        //            @Override
        //            public void onSuccess(List<TypeBean> response) {
        //
        //                adapter1.updateItems(response);
        //            }
        //        });
        getList("-1", adapter1);
    }

    public void request() {
        updateViews(true);
    }

    private void getList(String id, final TypeListAdapter adapter) {
        getListObservable(id).compose(this.<List<TypeBean>>bindToLife()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(new Observer<List<TypeBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<TypeBean> data) {
                        if (BlankUtil.isBlank(data)) {
                            showToast("没有数据");
                            adapter.updateItems(data);
                            treeAdapter.updateItems(data);
                        } else {
                            treeAdapter.updateItems(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getList(final int position) {
        final TypeBean bean = treeAdapter.getItem(position);
        getListObservable(bean.id).compose(this.<List<TypeBean>>bindToLife()).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .subscribe(new Observer<List<TypeBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<TypeBean> data) {
                        if (BlankUtil.isBlank(data)) {
                            showToast("没有数据");
                            bean.child = new ArrayList<>();
                        } else {
                            bean.show = true;
                            for (TypeBean typeBean : data) {
                                typeBean.level = bean.level + 1;
                            }
                            bean.child = data;
                            treeAdapter.getData().addAll(position + 1, bean.child);
                            treeAdapter.notifyItemChanged(position + 1, bean.child.size());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<List<TypeBean>> getListObservable(String id) {
        return RetrofitService.getService(BaseService.class).selectParentid(id);
    }
}
