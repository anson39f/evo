package com.xds.project.widget;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.freelib.multiitem.adapter.holder.BaseViewHolder;
import com.freelib.multiitem.adapter.holder.BaseViewHolderManager;
import com.xds.project.R;
import com.xds.project.entity.ImageTextBean;

/**
 * @version v1.0
 */
public class ImageAndTextManager extends BaseViewHolderManager<ImageTextBean> {


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, @NonNull ImageTextBean data) {
        TextView textView = getView(holder, R.id.text);
        textView.setText(data.getText());
        ImageView imageView = getView(holder, R.id.image);
        imageView.setImageResource(data.getImg());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_image_text;
    }

}
