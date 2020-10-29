package com.xds.project.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.xds.project.R;
import com.xds.project.widget.custom.WheelView;

import java.util.ArrayList;

/**
 * 选择时间
 */

public class TimePopupWindowDialog implements View.OnClickListener {
    private int mTimeStart;
    private int mTimeEnd;


    public interface SelectTimeCallback {
        void onSelected(int mTimeStart, int mTimeEnd);
    }

    public void showSelectTimeDialog(Activity activity, final SelectTimeCallback callback) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_select_time, null);

        final ArrayList<String> times = new ArrayList<>();

        WheelView wvStart = (WheelView) view.findViewById(R.id.wv_start_node);
        final WheelView wvEnd = (WheelView) view.findViewById(R.id.wv_end_node);

        int maxNode = 60;
        for (int i = 0; i < maxNode; i++) {
            times.add(String.format("%02d", i));
        }

        wvStart.setItems(times);
        wvEnd.setItems(times);

        wvStart.setSeletion(0);
        wvEnd.setSeletion(0);

        wvStart.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mTimeStart = selectedIndex - 1;
                //                if (mTimeStart > mTimeEnd) {
                //                    wvEnd.setSeletion(mTimeStart - 1);
                //                    return;
                //                }
            }
        });

        wvEnd.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                mTimeEnd = selectedIndex - 1;

                //                if (mTimeStart > mTimeEnd) {
                //                    wvEnd.setSeletion(mTimeStart - 1);
                //                }
            }
        });

        show(activity, callback, view);
    }

    private void show(Activity activity, final SelectTimeCallback callback, View view) {
        DialogHelper helper = new DialogHelper();
        helper.showCustomDialog(activity, view, null, new DialogListener() {
            @Override
            public void onPositive(DialogInterface dialog, int which) {
                super.onPositive(dialog, which);
                dialog.dismiss();
                callback.onSelected(mTimeStart, mTimeEnd);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
