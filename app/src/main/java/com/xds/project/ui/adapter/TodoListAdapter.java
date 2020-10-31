package com.xds.project.ui.adapter;

import android.app.Activity;
import android.view.View;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.base.utils.TimeUtil;
import com.xds.base.utils.Utils;
import com.xds.project.R;
import com.xds.project.data.beanv2.Things;

/**
 * @author .
 * @email
 */
public class TodoListAdapter extends BaseQuickAdapter<Things> {

    public TodoListAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.item_todo_list;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final Things item) {
        holder.setText(R.id.tvName, Utils.stringformat("%s", item.getName()));
        holder.setText(R.id.tvContent, Utils.stringformat("%s", item.getContent()));
        holder.setText(R.id.tvLevel, Utils.stringformat("%s", item.getLevelName()));
        holder.setTextColor(R.id.tvLevel, mContext.getColor(item.getLevelColor()));
        holder.setText(R.id.tvDate, TimeUtil.getStrTime(item.getDate(), "yyyy-MM-dd HH:mm:ss"));
//        holder.setOnClickListener(R.id.tvType, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onSubClickListener != null) {
//                    onSubClickListener.onItemClick(holder.getView(R.id.tvSelect), holder.getAdapterPosition());
//                }
//            }
//        });
    }

    private OnSubClickListener onSubClickListener;

    public void setOnSubClickListener(OnSubClickListener onSubClickListener) {
        this.onSubClickListener = onSubClickListener;
    }

    public interface OnSubClickListener {
        void onItemClick(View view, int position);
    }
}
