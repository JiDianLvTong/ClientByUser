package com.android.jidian.repair.net;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.UserAuditCab.AuditCabListBean;
import com.android.jidian.repair.mvp.UserLog.BatteryInquiryBean;
import com.android.jidian.repair.mvp.UserLog.MeterReadingBean;
import com.android.jidian.repair.mvp.UserLog.OperationLogBean;
import com.android.jidian.repair.mvp.UserMyPatrol.patrolDetail.PatrolDetailBean;
import com.android.jidian.repair.mvp.UserMyPatrol.patrolList.PatrolMyListBean;
import com.android.jidian.repair.mvp.UserTask.UserTaskList.WorktaskMylistsBean;
import com.android.jidian.repair.mvp.login.LoginBean;
import com.android.jidian.repair.mvp.main.PatrolFragment.PatrolIndexBean;
import com.android.jidian.repair.mvp.main.TimeLimitTaskFragment.WorktaskListsBean;
import com.android.jidian.repair.mvp.message.fragment.AdvicesListsBean;
import com.android.jidian.repair.mvp.task.UploadImageBean;
import com.android.jidian.repair.mvp.task.UploadUploadUrlSetBean;
import com.android.jidian.repair.mvp.task.WorktaskDetailBean;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
     * 任务提交
     *
     * @param wtid    任务id
     * @param ustat   处理状态：-2=无法完成，1=已完成。
     * @param content 处理结果描述
     * @param img1    设备图
     * @param img2    合影图
     * @param img3    关建位置图
     * @param img4    其他图（可选）
     */
    @FormUrlEncoded
    @POST("Worktask/resolve.html")
    Flowable<BaseBean> requestWorktaskResolve(@Field("wtid") String wtid, @Field("ustat") String ustat,
                                              @Field("content") String content, @Field("img1") String img1,
                                              @Field("img2") String img2, @Field("img3") String img3,
                                              @Field("img4") String img4);

    /**
     * 巡检提交
     *
     * @param cabid     电柜编号
     * @param img1      打卡合影
     * @param img2      屏幕清洁
     * @param img3      后门锁状态，
     * @param img4_1    柜体前侧清洁
     * @param img4_2    柜体左则清洁
     * @param img4_3    柜体右则清洁
     * @param img4_4    柜体顶部清洁
     * @param img5_1    柜体内上部清洁
     * @param img5_2    柜体内中部清洁
     * @param img5_3    柜体内下部清洁
     * @param img6_1    是否漏电检测笔图
     * @param isnetdbm  网络信号：1=有，0=无
     * @param isdixian  电柜地线：1=有，0=无
     * @param isopenbtn 换点按钮是否可用：1=是，0=否
     */
    @FormUrlEncoded
    @POST("Patrol/addpatrol.html")
    Flowable<BaseBean> requestPatrolAddpatrol(@Field("cabid") String cabid, @Field("img1") String img1,
                                              @Field("img2") String img2, @Field("img3") String img3,
                                              @Field("img4_1") String img4_1, @Field("img4_2") String img4_2,
                                              @Field("img4_3") String img4_3, @Field("img4_4") String img4_4,
                                              @Field("img5_1") String img5_1, @Field("img5_2") String img5_2,
                                              @Field("img5_3") String img5_3, @Field("img6_1") String img6_1,
                                              @Field("isnetdbm") String isnetdbm, @Field("isdixian") String isdixian,
                                              @Field("isopenbtn") String isopenbtn);


    /**
     * 故障上报
     * cabid,
     * content  描述，
     * img1  必传图，
     * img2  必传图，
     * img3  可选上传
     * img4  可选上传
     */
    @FormUrlEncoded
    @POST("Worktask/addFault.html")
    Flowable<BaseBean> requestWorktaskAddFault(@Field("cabid") String cabid, @Field("content") String content, @Field("img1") String img1,
                                               @Field("img2") String img2, @Field("img3") String img3, @Field("img4") String img4);

    /**
     * 已巡检列表
     */
    @FormUrlEncoded
    @POST("Patrol/mylists.html")
    Flowable<PatrolMyListBean> requestPatrolMylists(@Field("lng") String lng, @Field("lat") String lat, @Field("page") String page);

    /**
     * 巡检详情
     */
    @FormUrlEncoded
    @POST("Patrol/detail.html")
    Flowable<PatrolDetailBean> requestPatrolDetail(@Field("patid") String patid);

    /**
     * 我的任务列表
     */
    @FormUrlEncoded
    @POST("Worktask/mylists.html")
    Flowable<WorktaskMylistsBean> requestWorktaskMylists(@Field("page") String page);

    /**
     * 地维—待审核电柜列表
     */
    @FormUrlEncoded
    @POST("Cabinet/auditCabList.html")
    Flowable<AuditCabListBean> requestAuditCabList(@Field("page") String page);

    /**
     * 全部已读地维消息
     *
     * @param type 默认是0；0-系统消息，1-预警消息，2-活动消息，3-短信消息，4-微信公众号通知
     */
    @FormUrlEncoded
    @POST("Advices/allRead.html")
    Flowable<BaseBean> requestAdvicesAllRead(@Field("type") String type);

    /**
     * 地维消息列表
     *
     * @param type 默认是0；0-系统消息，1-预警消息，2-活动消息，3-短信消息，4-微信公众号通知
     * @param page 默认是1
     * @return ResponseBody
     */
    @FormUrlEncoded
    @POST("Advices/lists.html")
    Flowable<AdvicesListsBean> requestAdvicesLists(@Field("type") String type, @Field("page") String page);

    /**
     * 更新已读地维消息
     *
     * @param msg_id 消息id
     */
    @FormUrlEncoded
    @POST("Advices/read.html")
    Flowable<BaseBean> requestAdvicesRead(@Field("msg_id") String msg_id);

    /**
     * 请求地维日志
     *
     * @param month  月份  2020-07（非必须）
     * @param uid    用户ID 305
     * @param keywd  搜索文字（非必须）
     * @param lastid 下一页页码（非必须）
     */
    @FormUrlEncoded
    @POST("Cabinet/dwLogsList.html")
    Flowable<OperationLogBean> requestDwLogsList(@Field("month") String month, @Field("uid") String uid, @Field("keywd") String keywd,
                                                 @Field("lastid") String lastid);

    /**
     * 请求电池绑定记录
     *
     * @param month   月份  2020-07（必须）
     * @param uid     用户ID 305
     * @param battery 电池编码（必须）
     * @param lastid  下一页页码（非必须）
     */
    @FormUrlEncoded
    @POST("Battery/btyHistoryList.html")
    Flowable<BatteryInquiryBean> requestBtyHistoryList(@Field("month") String month, @Field("uid") String uid, @Field("battery") String battery,
                                                       @Field("lastid") String lastid);

    /**
     * 请求电池绑定记录
     *
     * @param month  月份  2020-07（必须）
     * @param uid    用户ID 305
     * @param cabid  电池id  4476
     * @param lastid 下一页页码（非必须）
     */
    @FormUrlEncoded
    @POST("Battery/cabBatteryList.html")
    Flowable<BatteryInquiryBean> requestCabBatteryList(@Field("month") String month, @Field("uid") String uid, @Field("cabid") String cabid,
                                                       @Field("lastid") String lastid);

    /**
     * 电表读数
     *
     * @param month  月份  2020-07（必须）
     * @param uid    用户ID 305
     * @param cabid  电池id  4476
     * @param lastid 下一页页码（非必须）
     */
    @FormUrlEncoded
    @POST("Battery/cabEleList.html")
    Flowable<MeterReadingBean> requestCabEleList(@Field("month") String month, @Field("uid") String uid, @Field("cabid") String cabid,
                                                 @Field("lastid") String lastid);

}
