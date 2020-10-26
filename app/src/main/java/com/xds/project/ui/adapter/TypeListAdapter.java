package com.xds.project.ui.adapter;

import android.app.Activity;
import android.view.View;

import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.base.utils.Utils;
import com.xds.project.R;
import com.xds.project.entity.TypeBean;

/**
 * @author .
 * @email
 */
public class TypeListAdapter extends BaseQuickAdapter<TypeBean> {
    public TypeListAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.item_type_list;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final TypeBean item) {
        holder.setText(R.id.tvName, Utils.stringformat("%s", item.name));
        holder.setOnClickListener(R.id.tvSelect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSubClickListener != null) {
                    onSubClickListener.onItemClick(holder.getView(R.id.tvSelect), holder.getAdapterPosition());
                }
            }
        });
    }

    private OnSubClickListener onSubClickListener;

    public void setOnSubClickListener(OnSubClickListener onSubClickListener) {
        this.onSubClickListener = onSubClickListener;
    }

    public interface OnSubClickListener {
        void onItemClick(View view, int position);
    }
}
