package com.xds.project.ui.adapter;

import android.app.Activity;
import android.view.View;

import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.base.utils.Utils;
import com.xds.project.R;
import com.xds.project.data.bean.DayStudy;

/**
 * @author .
 * @email
 */
public class StuyHistoryListAdapter extends BaseQuickAdapter<DayStudy> {

    public StuyHistoryListAdapter(Activity context) {
        super(context);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.item_study_list;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final DayStudy item) {
        holder.setText(R.id.tvName, Utils.stringformat("%s", item.day));
        int minutes = item.min;
        int second = item.sec;
        String and = minutes > 0 && second > 0 ? "and" : "";
        if (minutes == 0 && second == 0) {
            holder.setText(R.id.tvTime, "Time in Studying : 0");
        } else {
            String minutesString = minutes > 0 ? String.format("%s minutes", String.valueOf(minutes)) : "";
            String secondString = second > 0 ? String.format(" %s %s seconds", and,
                    String.valueOf(second)) : "";
            holder.setText(R.id.tvTime, Utils.stringformat("Time in Studying : %s %s", minutesString,
                    secondString));
        }


        holder.setText(R.id.tvSuccess, String.format("Success times : %s",
                String.valueOf(item.successNum)));
        holder.setText(R.id.tvFail, String.format("Fail times : %s",
                String.valueOf(item.failNum)));
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
