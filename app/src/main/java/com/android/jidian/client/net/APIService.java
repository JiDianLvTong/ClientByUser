package com.android.jidian.client.net;

import com.android.jidian.client.bean.AdvicesAllReadBean;
import com.android.jidian.client.bean.AdvicesReadBean;
import com.android.jidian.client.bean.BaseBean;
import com.android.jidian.client.bean.CabinetDetailBean;
import com.android.jidian.client.bean.ContactV2Bean;
import com.android.jidian.client.bean.CouponCashv2Bean;
import com.android.jidian.client.bean.CouponScanBean;
import com.android.jidian.client.bean.DepositRefundOrderBean;
import com.android.jidian.client.bean.EvaluateAddsBean;
import com.android.jidian.client.bean.EvaluateLabsBean;
import com.android.jidian.client.bean.EvaluateListsBean;
import com.android.jidian.client.bean.ExpenseBean;
import com.android.jidian.client.bean.FindIndexBean;
import com.android.jidian.client.bean.LoginBean;
import com.android.jidian.client.bean.LoginCheckAccv2Bean;
import com.android.jidian.client.bean.MainActiyivyExpenseBean;
import com.android.jidian.client.bean.MainAppVersionBean;
import com.android.jidian.client.bean.MapJwduV5Bean;
import com.android.jidian.client.bean.MapListsBean;
import com.android.jidian.client.bean.MapTopTabBean;
import com.android.jidian.client.bean.MyEbikeBtyBean;
import com.android.jidian.client.bean.PersonalInfoBean;
import com.android.jidian.client.bean.PullActivityInviteListsBean;
import com.android.jidian.client.bean.PullCashGetCashRecordBean;
import com.android.jidian.client.bean.PullCashGetUserAccountInfoBean;
import com.android.jidian.client.bean.PullCashGetUserProfitBean;
import com.android.jidian.client.bean.QrCodeScanBean;
import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;
import com.android.jidian.client.bean.UserConfirmBean;
import com.android.jidian.client.bean.UserPersonalBean;
import com.android.jidian.client.bean.UserUOrderBean;
import com.android.jidian.client.mvp.bean.ChargeSiteMapBean;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {


    /**
     * 获得地图全部站点
     */
    @FormUrlEncoded
    @POST("Map/mapJwduV5")
    Flowable<ChargeSiteMapBean> getAllMarker(@Field("uid") String uid, @Field("jingdu") String lng, @Field("weidu") String lat, @Field("type") String tab);

    /**
     * 电柜详情
     *
     * @param cabid 电柜id
     */
    @FormUrlEncoded
    @POST("Cabinet/detailv3")
    Flowable<CabinetDetailBean> cabinetDeatil(@Field("cabid") int cabid, @Field("repair") String repair, @Field("jingdu") String jingdu, @Field("weidu") String weidu);

    /**
     * 电柜评价列表
     *
     * @param cabid 电柜id
     * @param page  分页号
     */
    @FormUrlEncoded
    @POST("Evaluate/lists")
    Flowable<EvaluateListsBean> evaluateLists(@Field("cabid") int cabid, @Field("page") int page, @Field("repair") String repair);

    /**
     * @param cabid   电柜ID
     * @param stars   几星评价
     * @param labids  标签id集合，用逗号分开如：1,2,5,6
     * @param content 255字符
     */
    @FormUrlEncoded
    @POST("Evaluate/adds")
    Flowable<EvaluateAddsBean> evaluateAdds(@Field("cabid") int cabid, @Field("stars") int stars,
                                            @Field("labids") String labids, @Field("content") String content, @Field("repair") String repair);

    /**
     * @param cabid 电柜ID
     */
    @FormUrlEncoded
    @POST("Evaluate/labs")
    Flowable<EvaluateLabsBean> evaluateLabs(@Field("cabid") int cabid, @Field("repair") String repair);

    /**
     * 优惠码兑换接口（事务类型）
     */
    @FormUrlEncoded
    @POST("Coupon/cashv3")
    Flowable<CouponCashv2Bean> couponCashv2(@Field("uid") int uid, @Field("cid") Integer cid, @Field("code") String code, @Field("type") String type);

    @FormUrlEncoded
    @POST("Coupon/scan")
    Flowable<CouponScanBean> couponScan(@Field("uid") int uid, @Field("code") String code);

    /**
     * @param type 默认是0；0-系统消息，1-预警消息，2-活动消息，3-短信消息，4-微信公众号通知
     * @param page 默认是1
     * @return ResponseBody
     */
    @FormUrlEncoded
    @POST("Advices/listsV3")
    Flowable<ResponseBody> advicesListsV2(@Field("type") int type, @Field("page") int page);

    @FormUrlEncoded
    @POST("Advices/read")
    Flowable<AdvicesReadBean> advicesRead(@Field("msg_id") String msg_id);

    @FormUrlEncoded
    @POST("Advices/allRead")
    Flowable<AdvicesAllReadBean> advicesAllRead(@Field("type") int type);

    /**
     * @param uid 设备id
     */
    @FormUrlEncoded
    @POST("Wallet/myEbikeBtyV2")
    Flowable<MyEbikeBtyBean> myEbikeBty(@Field("uid") String uid);

    /**
     * 用户端 - 扫一扫接口（首页 和 个人中心车电待绑定页面）
     *
     * @param uid       用户uid
     * @param code      二维码内容
     * @param entercode 新增手动输入的扫框
     * @param type      2 = 车   4 = 电池
     * @param sync      是否同步关联（车架号/GPS），需要同步关联参数值必须等于1
     */
    @FormUrlEncoded
    @POST("QrCode/scan")
    Flowable<QrCodeScanBean> requestQrCodeScan(@Field("uid") String uid, @Field("code") String code, @Field("entercode") String entercode
            , @Field("type") String type, @Field("sync") String sync);

    /**
     * 用户端 - 扫一扫 结束绑定车辆信息接口
     *
     * @param uid 用户uid
     */
    @FormUrlEncoded
    @POST("QrCode/endBindBike")
    Flowable<QrCodeScanBean> requestEndBindBike(@Field("uid") String uid);

    /**
     * @param uid     用户uid
     * @param id      设备id
     * @param type    shop_type
     * @param confirm 1 = 确认解绑弹窗  0 = 确认解绑弹窗确定按钮
     */
    @FormUrlEncoded
    @POST("User/confirm")
    Flowable<UserConfirmBean> userConfirm(@Field("uid") String uid, @Field("id") String id, @Field("type") int type, @Field("confirm") String confirm);

//    /**
//     * @param uid     用户uid
//     * @param id      设备id
//     * @param type    shop_type
//     * @param confirm 1 = 确认解绑弹窗  0 = 确认解绑弹窗确定按钮
//     */
//    @FormUrlEncoded
//    @POST("User/confirm")
//    Flowable<UserConfirmBean> userConfirmRe(@Field("uid") String uid, @Field("id") String id, @Field("type") int type, @Field("confirm") String confirm);

    /**
     * 用户首页地图
     *
     * @param jingdu 经度
     * @param weidu  维度
     * @param type   站点类型 0=全部，-1=换电站，1=综合站，2=车行,3=便利店
     * @param tab    标签栏 1=换电，2=租车，3=买车，4=还车，5=维修
     * @return MapJwduV5Bean
     */
    @FormUrlEncoded
    @POST("Map/mapJwduV5")
    Flowable<MapJwduV5Bean> mapJwduV5(@Field("jingdu") String jingdu,
                                      @Field("weidu") String weidu,
                                      @Field("type") String type,
                                      @Field("tab") String tab);

    @POST("Map/topTab")
    Flowable<MapTopTabBean> mapTopTab();

    /**
     * 用户首页地图
     *
     * @param jingdu 经度
     * @param weidu  维度
     * @param type   站点类型 0=全部，-1=换电站，1=综合站，2=车行,3=便利店
     * @param tab    标签栏 1=换电，2=租车，3=买车，4=还车，5=维修
     * @return MapListsBean
     */
    @FormUrlEncoded
    @POST("Map/mapLists")
    Flowable<MapListsBean> mapLists(@Field("jingdu") String jingdu,
                                    @Field("weidu") String weidu,
                                    @Field("type") String type,
                                    @Field("tab") String tab);

    /**
     * 获取客服电话号码
     */
    @POST("Service/getContactV2")
    Flowable<ContactV2Bean> getContactV2();

    /**
     * 新增埋点
     */
    @FormUrlEncoded
    @POST("Tracking/tracking.html")
    Flowable<BaseBean> sendBuryingPoint(@Field("tag") String tag);

    /**
     * 查询该二维码是否存在
     */
    @FormUrlEncoded
    @POST("Coupon/scan.html")
    Flowable<BaseBean> sendScan(@Field("uid") String uid, @Field("code") String code);

    /**
     * 获取用户信息
     */
    @FormUrlEncoded
    @POST("User/personal.html")
    Flowable<PersonalInfoBean> requestPersonalInfo(@Field("uid") String uid);

    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("Sms/msgSend.html")
    Flowable<BaseBean> sendVerificationCode(@Field("phone") String phone);

    /**
     * 登录接口
     * phone 手机号
     * vcode 验证码
     * confirm 确认标识 10=切换新设备登录标识
     * appsn 设备唯一码
     */
    @FormUrlEncoded
    @POST("Login/loginv2.html")
    Flowable<LoginBean> requestLogin(@Field("phone") String phone, @Field("vcode") String vcode, @Field("confirm") String confirm, @Field("appsn") String appsn);

    /**
     * 检测账户变化，每次启动APP时请求该接口
     * apptoken app登录token
     * appsn 设备唯一码
     */
    @FormUrlEncoded
    @POST("Login/checkAccv2.html")
    Flowable<LoginCheckAccv2Bean> requestCheckAccv2(@Field("apptoken") String apptoken, @Field("appsn") String appsn);

    /**
     * 一键登录接口
     */
    @FormUrlEncoded
    @POST("Login/oneKeyLoginv2.html")
    Flowable<LoginBean> sendJgVerificationLogin(@Field("token") String token, @Field("confirm") String confirm, @Field("appsn") String appsn);

    /**
     * 发送登录统计
     */
    @FormUrlEncoded
    @POST("User/operGrant.html")
    Flowable<BaseBean> sendLoginStatistics(@Field("type") String type, @Field("sourceId") String sourceId, @Field("uid") String uid);

    /**
     * 首页访问节点日志
     */
    @FormUrlEncoded
    @POST("VisitLogs/index.html")
    Flowable<BaseBean> sendHttpVisitLogs(@Field("title") String title, @Field("sourceType") String sourceType
            , @Field("sourceId") String sourceId, @Field("type") String type, @Field("client") String client);

    /**
     * 我的钱包
     */
    @FormUrlEncoded
    @POST("Wallet/v8.html")
    Flowable<ExpenseBean> requestWalletInfo(@Field("uid") String uid);

    /**
     * 我的钱包
     */
    @FormUrlEncoded
    @POST("Wallet/v8.html")
    Flowable<MainActiyivyExpenseBean> requestMainInfo(@Field("uid") String uid);

    /**
     * 我的钱包-可退押金订单列表
     */
    @FormUrlEncoded
    @POST("User/depositRefundOrder.html")
    Flowable<DepositRefundOrderBean> requestDepositRefundOrder(@Field("uid") String uid);

    /**
     * 申请电池押金退款提交
     */
    @FormUrlEncoded
    @POST("User/depositRefundData.html")
    Flowable<BaseBean> submitDepositRefund(@Field("uid") String uid, @Field("oid") String oid, @Field("id") String id);

    /**
     * 获取当前收益
     */
    @POST("PullCash/getUserProfit.html")
    Flowable<PullCashGetUserProfitBean> requestPullCashGetUserProfit();

    /**
     * 提现申请
     */
    @FormUrlEncoded
    @POST("PullCash/addCash.html")
    Flowable<BaseBean> requestPullCashAddCash(@Field("cash_amount") String cash_amount);

    /**
     * 提现记录
     */
    @POST("PullCash/getCashRecord.html")
    Flowable<PullCashGetCashRecordBean> requestPullCashGetCashRecord();

    /**
     * 提现记录
     */
    @FormUrlEncoded
    @POST("PullActivity/inviteLists.html")
    Flowable<PullActivityInviteListsBean> requestPullActivityInviteLists(@Field("aid") String aid, @Field("type") String type, @Field("page") String page);

    /**
     * 获取授权信息
     */
    @POST("PullCash/getUserAccountInfo.html")
    Flowable<PullCashGetUserAccountInfoBean> requestPullCashGetUserAccountInfo();

    /**
     * 设置支付宝授权信息
     */
    @FormUrlEncoded
    @POST("PullCash/setUserAccountInfo.html")
    Flowable<BaseBean> requestPullCashSetUserAccountInfo(@Field("access_code") String access_code);

    /**
     * 更新接口
     */
    @FormUrlEncoded
    @POST("AppVer/upgrade.html")
    Flowable<MainAppVersionBean> appVerUpgrade(@Field("uid") String uid);

    /**
     * 商城接口
     */
    @FormUrlEncoded
    @POST("Shop/buy.html")
    Flowable<ShopBuyBean> requestShopBuy(@Field("lng") String lng, @Field("lat") String lat);

    /**
     * 商城接口
     */
    @FormUrlEncoded
    @POST("Shop/rent.html")
    Flowable<ShopRentBean> requestShopRent(@Field("lng") String lng, @Field("lat") String lat);

    /**
     * 获取个人信息
     */
    @FormUrlEncoded
    @POST("User/personal.html")
    Flowable<UserPersonalBean> requestUserPersonal(@Field("uid") String uid);

    /**
     * 商城接口
     */
    @FormUrlEncoded
    @POST("Find/index.html")
    Flowable<FindIndexBean> requestFindIndex(@Field("lng") String lng, @Field("lat") String lat);

    /**
     * 注销用户
     */
    @FormUrlEncoded
    @POST("User/cancel.html")
    Flowable<BaseBean> requestUserCancel(@Field("uid") String uid, @Field("code") String code, @Field("confirm") String confirm);

    /**
     * 注销用户获取验证码
     */
    @POST("User/smsCancel.html")
    Flowable<BaseBean> requestUserSmsCancel();
    /**
     * 订单列表
     */
    @FormUrlEncoded
    @POST("User/uOrder.html")
    Flowable<UserUOrderBean> requestUserUOrder(@Field("uid") String uid,@Field("lastid") String lastid);
}