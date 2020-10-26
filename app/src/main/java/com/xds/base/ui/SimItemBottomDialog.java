package com.xds.base.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.dl7.recycler.helper.RecyclerViewHelper;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.xds.project.R;

import java.util.List;


/**
 * 底部弹出列表
 */

public class SimItemBottomDialog {
    private BottomSheetDialog bottomSheetDialog;
    private SimAdapter adapter;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnCancelListener onCancelListener;

    private SimItemBottomDialog() {
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public static SimItemBottomDialog getDialog(Activity activity) {
        return getDialog(activity, null, null);
    }

    public static SimItemBottomDialog getDialog(Activity activity, List<CommonEntity> items, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        final SimItemBottomDialog bottomDialog = new SimItemBottomDialog();
        bottomDialog.bottomSheetDialog = new BottomSheetDialog(activity);
        bottomDialog.setOnRecyclerViewItemClickListener(onRecyclerViewItemClickListener);
        View view = activity.getLayoutInflater().inflate(R.layout.layout_dialog_recylerview, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        bottomDialog.adapter = new SimAdapter(activity, items);
        bottomDialog.adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bottomDialog.dismiss();
                if (null != bottomDialog.onRecyclerViewItemClickListener) {
                    bottomDialog.onRecyclerViewItemClickListener.onItemClick(view, position);
                }
            }
        });
        RecyclerViewHelper.initRecyclerViewV(activity, recyclerView, true, bottomDialog.adapter);
        bottomDialog.bottomSheetDialog.setContentView(view);
        bottomDialog.bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (null != bottomDialog.onDismissListener) {
                    bottomDialog.onDismissListener.onDismiss(dialog);
                }
            }
        });
        bottomDialog.bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (null != bottomDialog.onCancelListener) {
                    bottomDialog.onCancelListener.onCancel(dialog);
                }
            }
        });
        bottomDialog.bottomSheetDialog.show();

        return bottomDialog;
    }

    public void setItems(List<CommonEntity> items) {
        adapter.updateItems(items);
    }

    public void show() {
        bottomSheetDialog.show();
    }

    public void dismiss() {
        bottomSheetDialog.dismiss();
    }

    static class SimAdapter extends BaseQuickAdapter<CommonEntity> {
        public SimAdapter(Context context) {
            super(context);
        }

        public SimAdapter(Context context, List<CommonEntity> data) {
            super(context, data);
        }

        @Override
        protected int attachLayoutRes() {
            return R.layout.recy_item_sim;
        }

        @Override
        protected void convert(BaseViewHolder holder, CommonEntity item) {
            holder.setText(R.id.name, item.name);
        }
    }

    public static class CommonEntity {
        public String name;
        public String id;
        public String orderId;

        public CommonEntity(String name) {
            this.name = name;
        }
    }
}
