package com.xds.project.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.freelib.multiitem.adapter.BaseItemAdapter;
import com.freelib.multiitem.adapter.holder.BaseViewHolder;
import com.freelib.multiitem.adapter.holder.BaseViewHolderManager;
import com.freelib.multiitem.helper.ItemDragHelper;
import com.freelib.multiitem.helper.ViewScaleHelper;
import com.freelib.multiitem.item.UniqueItemManager;
import com.freelib.multiitem.listener.OnItemClickListener;
import com.freelib.multiitem.listener.OnItemDragListener;
import com.xds.base.ui.fragment.BaseFragment;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.app.Cache;
import com.xds.project.data.beanv2.Things;
import com.xds.project.data.greendao.ThingsDao;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.AddThingsActivity;
import com.xds.project.ui.activity.TodoListActivity;
import com.xds.project.util.ActivityTools;
import com.xds.project.util.LogUtil;
import com.xds.project.util.event.ThingsEvent;
import com.xds.project.widget.ThingsViewManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author .
 * @TODO 待办
 * @email
 */
public class ToDoListFragment extends BaseFragment {

    private User user;
    protected View contentView;

    public static final int NONE = -1;
    private RecyclerView horizontalRecycler;
    private BaseItemAdapter adapter;
    private ItemDragHelper dragHelper;
    private ViewScaleHelper scaleHelper;
    private List<Things> todoList = new ArrayList<>();
    private List<Things> processingList = new ArrayList<>();
    private List<Things> doneList = new ArrayList<>();
    private View foot;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_to_do_list;
    }


    @Override
    public void initUI() {
        EventBus.getDefault().register(this);
        user = BaseApplication.getUser();
        initToolbar();
        contentView = findViewById(R.id.root);
        horizontalRecycler = (RecyclerView) findViewById(R.id.mRecyclerView);

        adapter = new BaseItemAdapter();
        //此处为了简单所以使用不可复用的模式，正式业务视具体情况而定！！！
//        adapter.addDataItems(Arrays.asList(new UniqueItemManager(new RecyclerViewManager(15)),
//                new UniqueItemManager(new RecyclerViewManager(1)), new UniqueItemManager(new RecyclerViewManager(25)),
//                new UniqueItemManager(new RecyclerViewManager(15)), new UniqueItemManager(new RecyclerViewManager(5))));
//        adapter.addDataItems(Arrays.asList(new UniqueItemManager(new RecyclerViewManager(2)), new UniqueItemManager(new RecyclerViewManager(2)),
//                new UniqueItemManager(new RecyclerViewManager(2))));
        //设置横向滚动LinearLayoutManager
        foot = getLayoutInflater().inflate(R.layout.layout_foot, null);
        adapter.addFootView(foot);
        horizontalRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        horizontalRecycler.setAdapter(adapter);

        //ItemDragHelper，需要传入外层的横向滚动的RecyclerView
        dragHelper = new ItemDragHelper(horizontalRecycler);
        dragHelper.setOnItemDragListener(new OnBaseDragListener());

        scaleHelper = new ViewScaleHelper();
        //设置最外层的Content视图
        scaleHelper.setContentView(contentView);
        //设置横向的Recycler列表视图
        scaleHelper.setHorizontalView(horizontalRecycler);

        //监听横向滚动RecyclerView双击事件，并开启关闭缩放模式
        doubleTapToggleScale();
    }

    @Override
    public void initAction() {
        foot.findViewById(R.id.addHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doneList != null && !doneList.isEmpty()) {
                    for (Things things : doneList) {
                        things.setHistory(true);
                        Cache.instance().getThingsDao().update(things);
                    }
                    updateViews(true);
                    showToast("add success!");
                }
            }
        });
        foot.findViewById(R.id.historyReview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityTools.startToNextActivity(getActivity(), TodoListActivity.class);
            }
        });
    }

    @Override
    protected void updateViews(boolean isRefresh) {
        reLoadList();
        adapter.setDataItems(Arrays.asList(new UniqueItemManager(new RecyclerViewManager(todoList)), new UniqueItemManager(new RecyclerViewManager(processingList)),
                new UniqueItemManager(new RecyclerViewManager(doneList))));
    }

    private void reLoadList() {
        todoList = Cache.instance()
                .getThingsDao()
                .queryBuilder()
//                .where(ThingsDao.Properties.UserId.eq(UserId))//根据当前课表组id查询
                .where(ThingsDao.Properties.Deleted.eq(false))//查询没有删除的
                .where(ThingsDao.Properties.History.eq(false))//查询不是历史的
                .where(ThingsDao.Properties.State.eq(3))
                .list();
        processingList = Cache.instance()
                .getThingsDao()
                .queryBuilder()
//                .where(ThingsDao.Properties.UserId.eq(UserId))//根据当前课表组id查询
                .where(ThingsDao.Properties.Deleted.eq(false))//查询没有删除的
                .where(ThingsDao.Properties.History.eq(false))//查询不是历史的
                .where(ThingsDao.Properties.State.eq(2))
                .list();
        doneList = Cache.instance()
                .getThingsDao()
                .queryBuilder()
//                .where(ThingsDao.Properties.UserId.eq(UserId))//根据当前课表组id查询
                .where(ThingsDao.Properties.Deleted.eq(false))//查询没有删除的
                .where(ThingsDao.Properties.History.eq(false))//查询不是历史的
                .where(ThingsDao.Properties.State.eq(1))
                .list();
        Collections.sort(todoList);
        Collections.sort(processingList);
        Collections.sort(doneList);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("To Do List");
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_set) {
                    Intent intent = new Intent(getActivity(),
                            AddThingsActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void doubleTapToggleScale() {
        final GestureDetector doubleTapGesture = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                scaleHelper.toggleScaleModel();
                return super.onDoubleTap(e);
            }
        });
        horizontalRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                doubleTapGesture.onTouchEvent(event);
                return false;
            }
        });
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        //需要把touch事件传给dragHelper，true表示消耗掉事件
        //需要保证在Activity或者外层的ViewGroup或可以拦截Touch事件的地方回调都可以
        return dragHelper.onTouch(ev);
    }

    class OnBaseDragListener extends OnItemDragListener {

        @Override
        public float getScale() {
            return scaleHelper.isInScaleMode() ? scaleHelper.getScale() : super.getScale();
        }

        @Override
        public void onDragFinish(RecyclerView recyclerView, int itemRecyclerPos, int itemPos) {
            super.onDragFinish(recyclerView, itemRecyclerPos, itemPos);
            String text = String.format("拖动起始第%s个列表的第%s项 结束第%s个列表的第%s项 \n\n拖动数据:%s", originalRecyclerPosition,
                    originalItemPosition, itemRecyclerPos, itemPos, dragItemData);
            LogUtil.d(this, text);
//            if (originalRecyclerPosition == 0) {
//                todoList.remove(Math.max(0, originalItemPosition - 1));
//            } else if (originalRecyclerPosition == 1) {
//                processingList.remove(Math.max(0, originalItemPosition - 1));
//            } else if (originalRecyclerPosition == 2) {
//                doneList.remove(Math.max(0, originalItemPosition - 1));
//            }
            int state = 3;
            Things things = (Things) dragItemData;
            if (itemRecyclerPos == 0) {
//                todoList.add(itemPos, things);
                state = 3;
            } else if (itemRecyclerPos == 1) {
//                processingList.add(itemPos, things);
                state = 2;
            } else if (itemRecyclerPos == 2) {
//                doneList.add(itemPos, things);
                state = 1;
            }
            things.setState(state);
            Cache.instance().getThingsDao().update(things);

        }
    }

    class RecyclerViewManager extends BaseViewHolderManager<UniqueItemManager> {
        private List<Things> data;

        RecyclerViewManager(List<Things> data) {
            this.data = data;
        }

        @Override
        protected void onCreateViewHolder(@NonNull BaseViewHolder holder) {
            super.onCreateViewHolder(holder);
            View view = holder.itemView;
            view.getLayoutParams().width = -1;

            scaleHelper.addVerticalView(view);
            final RecyclerView recyclerView = getView(view, R.id.item_group_recycler);
//            horizontalRecycler.setClipToPadding(false);

            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            final BaseItemAdapter baseItemAdapter = new BaseItemAdapter();
            //为XXBean数据源注册XXHolderManager管理类 数据源必须实现ItemData接口
//            baseItemAdapter.register(TextDragBean.class, new TextViewDragManager());
            ThingsViewManager thingsViewManager = new ThingsViewManager();

            thingsViewManager.setOnItemLongClickListener(new ThingsViewManager.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(BaseViewHolder viewHolder) {
                    dragHelper.startDrag(viewHolder);
                }
            });
            thingsViewManager.setOnItemDeleteClickListener(new ThingsViewManager.OnItemDeleteClickListener() {
                @Override
                public void onItemDeleteClick(BaseViewHolder viewHolder) {
                    Things things = (Things) viewHolder.getItemData();
                    Cache.instance().getThingsDao().delete(things);
                    updateViews(true);
                }
            });
            baseItemAdapter.register(Things.class, thingsViewManager);
            baseItemAdapter.setDataItems(data);
            recyclerView.setAdapter(baseItemAdapter);
            baseItemAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseViewHolder viewHolder) {
                    Intent intent = new Intent(mContext, AddThingsActivity.class);
//                    intent.putExtra("id", item.id);
//                    intent.putExtra("typeId", item.typeid);
                    intent.putExtra("data", (Things) viewHolder.getItemData());
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull UniqueItemManager data) {
            TextView groupTxt = getView(holder.itemView, R.id.item_group_name);
            if (holder.getItemPosition() == 0) {
                groupTxt.setText("Things To Do");
            } else if (holder.getItemPosition() == 1) {
                groupTxt.setText("Processing");
            } else {
                groupTxt.setText("Done");
            }
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.item_recycler_view;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void thingsChangeEvent(ThingsEvent event) {
        //更新主界面
        updateViews(true);
    }

    private boolean checkUser() {
        if (user == null || "普通用户".equals(user.getType())) {
            showToast("没有权限进行操作");
            return true;
        }
        return false;
    }
}
