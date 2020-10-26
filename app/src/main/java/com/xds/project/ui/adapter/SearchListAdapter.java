package com.xds.project.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.CheckedTextView;

import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.base.utils.Utils;
import com.xds.project.R;
import com.xds.project.entity.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author .
 * @email
 */
public class SearchListAdapter extends BaseQuickAdapter<TestCase> {
    private List<String> ids;
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

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.item_search_list;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final TestCase item) {
        //        holder.setText(R.id.tvName, Utils.stringformat("%s", item.text));
        holder.setText(R.id.tvType, Utils.stringformat("分类:%s", item.typename));
        //        holder.setText(R.id.tvContent, Utils.stringformat("%s", item.contentHL));
        holder.setText(R.id.tvContent, Html.fromHtml(item.content));

        holder.setVisible(R.id.checkbox, isShow);
        final CheckedTextView checkedTextView = holder.getView(R.id.checkbox);
        if (ids.contains(item.id)) {
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
                    if (!ids.contains(item.id)) {
                        ids.add(item.id);
                    }
                    checkedTextView.setChecked(true);
                    checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.ic_checked_checked, 0);
                } else {
                    if (ids.contains(item.id)) {
                        ids.remove(item.id);
                    }
                    checkedTextView.setChecked(false);
                    checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.mipmap.ic_checked_normal, 0);
                }
            }
        });
    }
}
