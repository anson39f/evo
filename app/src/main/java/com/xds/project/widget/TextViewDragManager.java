package com.xds.project.widget;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.freelib.multiitem.adapter.holder.BaseViewHolder;
import com.freelib.multiitem.adapter.holder.BaseViewHolderManager;
import com.xds.project.R;
import com.xds.project.entity.TextDragBean;

/**
 * @version v1.0
 */
public class TextViewDragManager extends BaseViewHolderManager<TextDragBean> {


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull TextDragBean data) {
        TextView textView = getView(holder, R.id.text);
        textView.setText(data.getText());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_text;
    }

}
