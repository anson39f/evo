package com.xds.project.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.project.R;

import java.util.List;

public class SelectWeekAdapter extends BaseQuickAdapter<String> {

    private int selectIndex = 2;

    public SelectWeekAdapter(Context context, @NonNull List<String> data) {
        super(context, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.tv_text, item);
        if (selectIndex == holder.getLayoutPosition()) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_select_week;
    }
}
