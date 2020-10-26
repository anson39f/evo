package com.xds.project.ui.adapter;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.base.utils.Utils;
import com.xds.project.R;
import com.xds.project.entity.TypeBean;

/**
 * @author .
 * @email
 */
public class TypeTreeListAdapter extends BaseQuickAdapter<TypeBean> {
    public TypeTreeListAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.item_type_list;
    }

    public int getItemViewType(int position) {
        return mData.get(position).level;
    }

    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final TypeBean item) {
        TextView tvName = holder.getView(R.id.tvName);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) tvName.getLayoutParams();
        layoutParams.leftMargin = layoutParams.leftMargin * item.level;
        tvName.setLayoutParams(layoutParams);
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
