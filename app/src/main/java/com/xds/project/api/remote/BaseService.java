package com.xds.project.api.remote;


import com.xds.project.api.ResData;
import com.xds.project.api.ResList;
import com.xds.project.entity.FileResult;
import com.xds.project.entity.SearchBean;
import com.xds.project.entity.TestCase;
import com.xds.project.entity.TypeBean;
import com.xds.project.entity.User;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 接口
 *
 * @author
 * @email
 * @date
 */
public interface BaseService {

    @POST("Android/user/login")
    Observable<ResData<User>> loginUser(@Body HashMap map);

    @POST("Android/user/add")
    Observable<ResData<User>> registerUser(@Body HashMap map);

    @POST("Android/type/add")
    Observable<ResData<User>> addType(@Body HashMap map);

    @POST("Android/test/updateType")
    Observable<ResData<User>> updateType(@Body HashMap map);

    @POST("Android/test/judge")
    Observable<ResData<Void>> judge(@Body HashMap map);


    @GET("guideass/user/list")
    Observable<List<User>> userAll();


    @GET("Android/test/selectAllByLayui")
    Observable<ResList<TestCase>> selectAllByLayui(@Query("page") String page,
                                                   @Query("limit") String limit);

    @GET("Android/test/selectAllByLayui")
    Observable<SearchBean> search(@Query("name") String text, @Query("typename") String typename,
                                  @Query("page") String page,
                                  @Query("limit") String limit);

    @POST("Android/test/add")
    Observable<ResData<Void>> addCase(@Body HashMap map);

    @GET("Android/test/deletes")
    Observable<ResData<Void>> deletes(@Query("ids") String ids);

    @GET("Android/test/delete")
    Observable<ResData<Void>> delete(@Query("id") String id);

    @Multipart
    @POST("Android/fileupload/upLoadLocal")
    Observable<FileResult> uploadFile(@Part MultipartBody.Part file);

    @Multipart
    @POST("Android/fileupload/upLoadLocal1")
    Observable<FileResult> uploadFile1(@Part MultipartBody.Part file, @Query("name") String name);


    @GET("Android/Yuliaoku/selectAllByLayui")
    Observable<ResList<TypeBean>> selectAllByType(@Query("page") String page,
                                                  @Query("limit") String limit);

    @GET("Android/type/selectParentid")
    Observable<List<TypeBean>> selectParentid(@Query("id") String id);

    @GET("Android/Yuliaoku/train")
    Observable<ResList<TypeBean>> train();

}
