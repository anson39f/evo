package com.xds.project.api.remote;

import com.xds.base.net.HttpListener;
import com.xds.base.net.RetrofitService;
import com.xds.base.ui.IBaseView;
import com.xds.base.utils.BlankUtil;
import com.xds.project.entity.TestCase;
import com.xds.project.entity.TypeBean;
import com.xds.project.entity.User;

import java.util.HashMap;
import java.util.List;

/**
 * @author 访问接口
 * @email
 * @date
 */
public class BaseAppApi {


    public static BaseService getBaseService() {
        return RetrofitService.getService(BaseService.class);
    }

    /**
     * 登陆
     *
     * @param view
     * @param name
     * @param pasword
     * @param listener
     */
    public static void login(IBaseView view, String name, String pasword,
                             HttpListener<User>
                                     listener) {
        HashMap<String, String> map = new HashMap();
        map.put("username", name);
//        map.put("password", MD5Util.MD5(pasword));
        map.put("password", pasword);
        RetrofitService.request(getBaseService().loginUser(map), view.bindToLife(),
                listener);
    }

    //注册
    public static void register(IBaseView view, String account, String passwd,
                                String type, String phone,
                                HttpListener<User>
                                        listener) {
        HashMap<String, String> map = new HashMap();
        map.put("username", account);
//        map.put("password", MD5Util.MD5(passwd));
        map.put("password", passwd);
        map.put("type", type);
        map.put("phone", phone);
        RetrofitService.request(getBaseService().registerUser(map), view.bindToLife(), listener);
    }

    public static void addType(IBaseView view, String name, String typeId,
                               HttpListener<User>
                                       listener) {
        HashMap<String, String> map = new HashMap();
        map.put("name", name);
        map.put("parentid", typeId);
        RetrofitService.request(getBaseService().addType(map), view.bindToLife(), listener);
    }

    //修改分类
    public static void updateType(IBaseView view, String id, String typename, String content, String typeid,
                                  HttpListener<User>
                                          listener) {
        HashMap<String, String> map = new HashMap();
        map.put("id", id);
        if (!BlankUtil.isBlank(content)) {
            map.put("content", content);
        }
        if (!BlankUtil.isBlank(typeid)) {
            map.put("typename", typename);
            map.put("typeid", typeid);
        }
        RetrofitService.request(getBaseService().updateType(map), view.bindToLife(), listener);
    }

    //修改分类之前判断
    public static void judge(IBaseView view, String id, String typename, String content, String typeid,
                             HttpListener<Void>
                                     listener) {
        HashMap<String, String> map = new HashMap();
        map.put("id", id);
        if (!BlankUtil.isBlank(content)) {
            map.put("content", content);
        }
        if (!BlankUtil.isBlank(typeid)) {
            map.put("typename", typename);
            map.put("typeid", typeid);
        }
        RetrofitService.request(getBaseService().judge(map), view.bindToLife(), listener);
    }

    //获取测试用例列表
    public static void selectAllByLayui(String page,
                                        String limit,
                                        HttpListener<List<TestCase>>
                                                listener) {
        RetrofitService.request(getBaseService().selectAllByLayui(page, limit),
                listener);
    }

    //获取分类列表
    public static void selectAllByType(String page,
                                       String limit,
                                       HttpListener<List<TypeBean>>
                                               listener) {
        RetrofitService.request(getBaseService().selectAllByType(page, limit),
                listener);
    }

    public static void train(
            HttpListener<List<TypeBean>>
                    listener) {
        RetrofitService.request(getBaseService().train(),
                listener);
    }

    //    public static void uploadImages(String filePath, HttpListener<FileResult>
    //            listener) {
    //        File file = new File(filePath);
    //        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
    //        RetrofitService.requestList(getBaseService().uploadFile(part), listener);
    //    }

    public static void addCase(IBaseView view, String id,
                               HttpListener<Void>
                                       listener) {
        HashMap<String, String> map = new HashMap();
        map.put("id", id);
        RetrofitService.request(getBaseService().addCase(map), view.bindToLife(), listener);
    }

    public static void delete(String id,
                              HttpListener<Void>
                                      listener) {
        //        RetrofitService.requestList(getBaseService().delete(id), view.bindToLife(), listener);
        RetrofitService.request(getBaseService().delete(id), listener);
    }

    public static void deletes(String id,
                               HttpListener<Void>
                                       listener) {
        //        RetrofitService.requestList(getBaseService().delete(id), view.bindToLife(), listener);
        RetrofitService.request(getBaseService().deletes(id), listener);
    }

}
