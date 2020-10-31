package com.xds.project.ui.adapter;

import android.app.Activity;
import android.view.View;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.base.utils.TimeUtil;
import com.xds.base.utils.Utils;
import com.xds.project.R;
import com.xds.project.data.beanv2.SelfStudy;

/**
 * @author .
 * @email
 */
public class StuyHistoryListAdapter extends BaseQuickAdapter<SelfStudy> {

    public StuyHistoryListAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.item_todo_list;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final SelfStudy item) {
        holder.setText(R.id.tvName, Utils.stringformat("%s", item.getContent()));
        int hours = item.getMinute();
        int minutes = item.getSecond();
        String and = hours > 0 && minutes > 0 ? "and" : "";
        String hoursString = hours > 0 ? String.format("%s hours", String.valueOf(hours)) : "";
        String minutesString = minutes > 0 ? String.format(" %s %s minutes", and, String.valueOf(minutes)) : "";
        holder.setText(R.id.tvContent, Utils.stringformat("Time in Studying : %s %s", hoursString, minutesString));
        holder.setVisible(R.id.tvLevel, false);
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
