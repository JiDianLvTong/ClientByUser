package com.android.jidian.repair.net;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.login.LoginBean;
import com.android.jidian.repair.mvp.main.PatrolFragment.PatrolIndexBean;
import com.android.jidian.repair.mvp.main.TimeLimitTaskFragment.WorktaskListsBean;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.mvp.task.WorktaskDetailBean;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * @author : xiaoming
 * date: 2023/1/10 15:06
 * description:
 */
public interface APEService {
    //更新接口
    @FormUrlEncoded
    @POST("Login/jdLogin.html")
    Flowable<LoginBean> requestLoginJdLogin(@Field("phone") String phone, @Field("passwd") String passwd);

    @FormUrlEncoded
    @POST("Login/jdCheckAcc.html")
    Flowable<BaseBean> requestCheckAcc(@Field("uid") String uid, @Field("apptoken") String apptoken,
                                       @Field("nowlng") String nowlng, @Field("nowlat") String nowlat);

    /**
     * 绑定长链接
     */
    @FormUrlEncoded
    @POST("Connection/workBind.html")
    Flowable<BaseBean> requestConnectionWorkBind(@Field("fd") String fd, @Field("uid") String uid);

    /**
     * 即时任务列表
     */
    @FormUrlEncoded
    @POST("Worktask/lists.html")
    Flowable<WorktaskListsBean> requestWorktaskLists(@Field("page") String page);

    /**
     * 即时任务详情
     */
    @FormUrlEncoded
    @POST("Worktask/detail.html")
    Flowable<WorktaskDetailBean> requestWorktaskDetail(@Field("wtid") String wtid);

    /**
     * 首页巡检
     */
    @FormUrlEncoded
    @POST("Patrol/index.html")
    Flowable<PatrolIndexBean> requestPatrolIndex(@Field("lng") String lng, @Field("lat") String lat, @Field("page") String page);

    /**
     * 获取上传图片的地址
     */
    @FormUrlEncoded
    @POST("Upload/uploadUrlSet.html")
    Flowable<UploadUploadUrlSetBean> requestUploadUploadUrlSet(@Field("token") String token);

    /**
     * 上传图片
     */
    @Multipart
    @POST
    Observable<UploadImageBean> requestUpLoadImg(@Url String url, @Part MultipartBody.Part upFile,
                                                 @Part("proj") RequestBody proj, @Part("upToken") String upToken,
                                                 @Part("companyid") String companyid);

    /**
     * 获取上传图片的地址
     * wtid,任务id
     * ustat,处理状态：-2=无法完成，1=已完成。
     * content,处理结果描述
     * img1,设备图
     * img2,合影图
     * img3,关建位置图
     * img4,其他图（可选）
     */
    @FormUrlEncoded
    @POST("Worktask/resolve.html")
    Flowable<BaseBean> requestWorktaskResolve(@Field("wtid") String wtid, @Field("ustat") String ustat,
                                                            @Field("content") String content, @Field("img1") String img1,
                                                            @Field("img2") String img2, @Field("img3") String img3,
                                                            @Field("img4") String img4);

    /**
     *
     * cabid,电柜编号
     * img1,打卡合影
     * img2,屏幕清洁
     * img3,后门锁状态，
     * img4_1,柜体前侧清洁
     * img4_2,柜体左则清洁
     * img4_3,柜体右则清洁
     * img4_4,柜体顶部清洁
     * img5_1,柜体内上部清洁
     * img5_2,柜体内中部清洁
     * img5_3,柜体内下部清洁
     * img6_1,是否漏电检测笔图
     * isnetdbm, 网络信号：1=有，0=无
     * isdixian, 电柜地线：1=有，0=无
     * isopenbtn,开仓正常：1=是，0=否
     * isdraft，是否为草稿，0草稿，1正式
     */
    @FormUrlEncoded
    @POST("Patrol/addpatrol.html")
    Flowable<BaseBean> requestPatrolAddpatrol(@Field("cabid") String cabid, @Field("img1") String img1,
                                              @Field("img2") String img2, @Field("img3") String img3,
                                              @Field("img4_1") String img4_1, @Field("img4_2") String img4_2,
                                              @Field("img4_3") String img4_3,@Field("img4_4") String img4_4);

}
