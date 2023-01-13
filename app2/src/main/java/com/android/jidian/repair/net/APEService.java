package com.android.jidian.repair.net;

import com.android.jidian.repair.base.BaseBean;
import com.android.jidian.repair.mvp.login.LoginBean;
import com.android.jidian.repair.mvp.main.PatrolFragment.PatrolIndexBean;
import com.android.jidian.repair.mvp.main.TimeLimitTaskFragment.WorktaskListsBean;
import com.android.jidian.repair.mvp.task.WorktaskDetailBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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

}
