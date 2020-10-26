package com.xds.project.ui.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ProgressBar;

import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;
import com.xds.base.config.UriProvider;
import com.xds.base.net.HttpListener;
import com.xds.base.utils.Utils;
import com.xds.project.BaseApplication;
import com.xds.project.R;
import com.xds.project.api.remote.BaseAppApi;
import com.xds.project.entity.TestCase;
import com.xds.project.entity.User;
import com.xds.project.ui.activity.ModifyActivity;
import com.xds.project.ui.activity.TestCaseListActivity;
import com.xds.project.util.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author .
 * @email
 */
public class TestCaseListAdapter extends BaseQuickAdapter<TestCase> {
    private List<String> ids;
    private boolean isShow;
    private Activity context;
    private ProgressBar mProgDownload;

    public TestCaseListAdapter(Activity context) {
        super(context);
        this.context = context;
        ids = new ArrayList<>();
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.item_test_case_list;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final TestCase item) {
        holder.setVisible(R.id.checkbox, isShow);
        final CheckedTextView checkedTextView = holder.getView(R.id.checkbox);
        if (ids.contains(item.id)) {
            checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_checked_checked, 0);
            checkedTextView.setChecked(true);
        } else {
            checkedTextView.setChecked(false);
            checkedTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_checked_normal, 0);
        }
        holder.setText(R.id.tvName, Utils.stringformat("%s", item.name));
        holder.setText(R.id.tvType, Utils.stringformat("分类:%s", item.typename));
        //        holder.setText(R.id.tvKey, Utils.stringformat("索引:%s", item.keyss));
        //        holder.setText(R.id.tvContent, Utils.stringformat("%s", item.content));
        holder.setText(R.id.tvContent, Html.fromHtml(item.content));
        User user = BaseApplication.getUser();
        if (user == null || "普通用户".equals(user.type)) {
            holder.setVisible(R.id.tvModify, false);
            holder.setVisible(R.id.tvDelete, false);
        } else {
            holder.setVisible(R.id.tvModify, true);
            holder.setVisible(R.id.tvDelete, true);
        }
        holder.setOnClickListener(R.id.tvModify, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ModifyActivity.class);
                intent.putExtra("id", item.id);
                intent.putExtra("typeId", item.typeid);
                intent.putExtra("data", item);
                mContext.startActivity(intent);
            }
        });
        holder.setOnClickListener(R.id.tvDelete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("是否删除");
                dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BaseAppApi.delete(item.id, new HttpListener<Void>() {
                            @Override
                            public void onSuccess(Void response) {
                                ToastUtil.show(mContext, "删除成功");
                                ((TestCaseListActivity) mContext).requestList();
                            }
                        });
                    }
                });
                dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        holder.setOnClickListener(R.id.tvDownload, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("提示");
                dialog.setMessage("是否下载");
                dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drownload(item.id);
                    }
                });
                dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        //        holder.setOnCheckedChangeListener(R.id.checkbox, new CompoundButton.OnCheckedChangeListener() {
        //            @Override
        //            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //                if (isChecked) {
        //                    if (!ids.contains(item.id)) {
        //                        ids.add(item.id);
        //                    }
        //                } else {
        //                    if (ids.contains(item.id)) {
        //                        ids.remove(item.id);
        //                    }
        //                }
        //            }
        //        });
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

    private void drownload(final String id) {
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    String url = UriProvider.API_HOST + "Android/fileupload/downloadXls?fileName" +
                            "=" + id;
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .addHeader("Accept-Encoding", "identity")
                            .build();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    //获取下载的内容输入流
                    ResponseBody body = response.body();
                    InputStream inputStream = body.byteStream();
                    final long lengh = body.contentLength();
                    System.out.println("文件大小" + lengh);
                    // 文件保存到本地
                    File file1 = new File(Environment.getExternalStorageDirectory(), "/testCase");
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    File file = new File(file1, "test_" + id + ".xls");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    int lien = 0;
                    int losing = 0;
                    byte[] bytes = new byte[1024];
                    while ((lien = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, lien);
                        losing += lien;
                        final float i = losing * 1.0f / lengh;
                        System.out.println("下载进度" + i);
                        //((TestCaseListActivity) mContext).showProgress(file, i);
                        //使用rxjava切换线程
                        // UpdateAppearanceProgress(i);
                    }

                    ((TestCaseListActivity) mContext).showProgress(file, 0);
                    outputStream.flush();
                    inputStream.close();
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
