package com.xds.project.widget;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.freelib.multiitem.adapter.holder.BaseViewHolder;
import com.freelib.multiitem.adapter.holder.BaseViewHolderManager;
import com.xds.project.R;
import com.xds.project.data.beanv2.Things;

/**
 * @version v1.0
 */
public class ThingsViewManager extends BaseViewHolderManager<Things> {
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemDeleteClickListener onItemDeleteClickListener;

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder holder, @NonNull Things data) {
        TextView textView = getView(holder, R.id.text);
        TextView type = getView(holder, R.id.type);
        ImageView drag = getView(holder, R.id.drag);
        ImageView delete = getView(holder, R.id.delete);
        type.setTextColor(ContextCompat.getColor(type.getContext(), data.getLevelColor()));
        textView.setText(data.getName());
        type.setText(data.getLevelName());
        drag.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(holder);
                }
                return false;
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemDeleteClickListener != null) {
                    onItemDeleteClickListener.onItemDeleteClick(holder);
                }
            }

        });

    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_image_text;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener onItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener;
    }

    public interface OnItemLongClickListener {

        public abstract void onItemLongClick(BaseViewHolder viewHolder);

    }

    public interface OnItemDeleteClickListener {

        public abstract void onItemDeleteClick(BaseViewHolder viewHolder);

    }
}
