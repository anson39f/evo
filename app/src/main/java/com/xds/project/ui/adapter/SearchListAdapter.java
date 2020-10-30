package com.xds.project.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckedTextView;

import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.base.utils.Utils;
import com.xds.project.R;
import com.xds.project.app.Constant;
import com.xds.project.data.beanv2.CourseV2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author .
 * @email
 */
public class SearchListAdapter extends BaseQuickAdapter<CourseV2> {
    private List<Long> ids;
    private boolean isShow;

    public SearchListAdapter(Context context) {
        super(context);
        ids = new ArrayList<>();
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.item_search_list;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final CourseV2 item) {
        //        holder.setText(R.id.tvName, Utils.stringformat("%s", item.text));
        holder.setText(R.id.tvType, Utils.stringformat("Course:\n%s", item.getCouName()));
        //        holder.setText(R.id.tvContent, Utils.stringformat("%s", item.contentHL));
        holder.setText(R.id.tvContent, Utils.stringformat("Teacher:%s", item.getCouTeacher()));
        StringBuilder nodeInfo = new StringBuilder();
        nodeInfo.append(Constant.TIMES[item.getCouStartNode() - 1]);
        if (item.getCouNodeCount() > 1) {
            nodeInfo.append("-");
            nodeInfo.append(Constant.TIMES[item.getCouStartNode() + item.getCouNodeCount() - 2]);
        }
        holder.setText(R.id.tvTime, Constant.WEEK_SINGLE[item.getCouWeek() - 1] + " " + nodeInfo.toString());

        holder.setVisible(R.id.checkbox, isShow);
        final CheckedTextView checkedTextView = holder.getView(R.id.checkbox);
        if (ids.contains(item.getCouId())) {
            checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_checked_checked, 0);
            checkedTextView.setChecked(true);
        } else {
            checkedTextView.setChecked(false);
            checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_checked_normal, 0);
        }

        checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkedTextView.isChecked()) {
                    if (!ids.contains(item.getCouId())) {
                        ids.add(item.getCouId());
                    }
                    checkedTextView.setChecked(true);
                    checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.ic_checked_checked, 0);
                } else {
                    if (ids.contains(item.getCouId())) {
                        ids.remove(item.getCouId());
                    }
                    checkedTextView.setChecked(false);
                    checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.ic_checked_normal, 0);
                }
            }
        });
    }
}
