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
import com.android.jidian.client.mvp.bean.MainAppVersionBean;
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

    //????????????
    @FormUrlEncoded
    @POST("AppVer/upgrade.html")
    Flowable<MainAppVersionBean> appVerUpgrade(@Field("uid") String uid);

    //??????????????????
    @FormUrlEncoded
    @POST("Wallet/v8.html")
    Flowable<MainActiyivyExpenseBean> requestMainInfo(@Field("uid") String uid);




    /**
     * ????????????????????????
     */
    @FormUrlEncoded
    @POST("Map/mapJwduV5")
    Flowable<ChargeSiteMapBean> getAllMarker(@Field("uid") String uid, @Field("jingdu") String lng, @Field("weidu") String lat, @Field("type") String tab);

    /**
     * ????????????
     *
     * @param cabid ??????id
     */
    @FormUrlEncoded
    @POST("Cabinet/detailv3")
    Flowable<CabinetDetailBean> cabinetDeatil(@Field("cabid") int cabid, @Field("repair") String repair, @Field("jingdu") String jingdu, @Field("weidu") String weidu);

    /**
     * ??????????????????
     *
     * @param cabid ??????id
     * @param page  ?????????
     */
    @FormUrlEncoded
    @POST("Evaluate/lists")
    Flowable<EvaluateListsBean> evaluateLists(@Field("cabid") int cabid, @Field("page") int page, @Field("repair") String repair);

    /**
     * @param cabid   ??????ID
     * @param stars   ????????????
     * @param labids  ??????id??????????????????????????????1,2,5,6
     * @param content 255??????
     */
    @FormUrlEncoded
    @POST("Evaluate/adds")
    Flowable<EvaluateAddsBean> evaluateAdds(@Field("cabid") int cabid, @Field("stars") int stars,
                                            @Field("labids") String labids, @Field("content") String content, @Field("repair") String repair);

    /**
     * @param cabid ??????ID
     */
    @FormUrlEncoded
    @POST("Evaluate/labs")
    Flowable<EvaluateLabsBean> evaluateLabs(@Field("cabid") int cabid, @Field("repair") String repair);

    /**
     * ???????????????????????????????????????
     */
    @FormUrlEncoded
    @POST("Coupon/cashv3")
    Flowable<CouponCashv2Bean> couponCashv2(@Field("uid") int uid, @Field("cid") Integer cid, @Field("code") String code, @Field("type") String type);

    @FormUrlEncoded
    @POST("Coupon/scan")
    Flowable<CouponScanBean> couponScan(@Field("uid") int uid, @Field("code") String code);

    /**
     * @param type ?????????0???0-???????????????1-???????????????2-???????????????3-???????????????4-?????????????????????
     * @param page ?????????1
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
     * @param uid ??????id
     */
    @FormUrlEncoded
    @POST("Wallet/myEbikeBtyV2")
    Flowable<MyEbikeBtyBean> myEbikeBty(@Field("uid") String uid);

    /**
     * ????????? - ???????????????????????? ??? ????????????????????????????????????
     *
     * @param uid       ??????uid
     * @param code      ???????????????
     * @param entercode ???????????????????????????
     * @param type      4 = ???   2 = ??????
     * @param sync      ??????????????????????????????/GPS?????????????????????????????????????????????1
     */
    @FormUrlEncoded
    @POST("QrCode/scan")
    Flowable<QrCodeScanBean> requestQrCodeScan(@Field("uid") String uid, @Field("code") String code, @Field("entercode") String entercode
            , @Field("type") String type, @Field("sync") String sync);

    /**
     * ????????? - ????????? ??????????????????????????????
     *
     * @param uid ??????uid
     */
    @FormUrlEncoded
    @POST("QrCode/endBindBike")
    Flowable<QrCodeScanBean> requestEndBindBike(@Field("uid") String uid);

    /**
     * @param uid     ??????uid
     * @param id      ??????id
     * @param type    shop_type
     * @param confirm 1 = ??????????????????  0 = ??????????????????????????????
     */
    @FormUrlEncoded
    @POST("User/confirm")
    Flowable<UserConfirmBean> userConfirm(@Field("uid") String uid, @Field("id") String id, @Field("type") int type, @Field("confirm") String confirm);

//    /**
//     * @param uid     ??????uid
//     * @param id      ??????id
//     * @param type    shop_type
//     * @param confirm 1 = ??????????????????  0 = ??????????????????????????????
//     */
//    @FormUrlEncoded
//    @POST("User/confirm")
//    Flowable<UserConfirmBean> userConfirmRe(@Field("uid") String uid, @Field("id") String id, @Field("type") int type, @Field("confirm") String confirm);

    /**
     * ??????????????????
     *
     * @param jingdu ??????
     * @param weidu  ??????
     * @param type   ???????????? 0=?????????-1=????????????1=????????????2=??????,3=?????????
     * @param tab    ????????? 1=?????????2=?????????3=?????????4=?????????5=??????
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
     * ??????????????????
     *
     * @param jingdu ??????
     * @param weidu  ??????
     * @param type   ???????????? 0=?????????-1=????????????1=????????????2=??????,3=?????????
     * @param tab    ????????? 1=?????????2=?????????3=?????????4=?????????5=??????
     * @return MapListsBean
     */
    @FormUrlEncoded
    @POST("Map/mapLists")
    Flowable<MapListsBean> mapLists(@Field("jingdu") String jingdu,
                                    @Field("weidu") String weidu,
                                    @Field("type") String type,
                                    @Field("tab") String tab);

    /**
     * ????????????????????????
     */
    @POST("Service/getContactV2")
    Flowable<ContactV2Bean> getContactV2();

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("Tracking/tracking.html")
    Flowable<BaseBean> sendBuryingPoint(@Field("tag") String tag);

    /**
     * ??????????????????????????????
     */
    @FormUrlEncoded
    @POST("Coupon/scan.html")
    Flowable<BaseBean> sendScan(@Field("uid") String uid, @Field("code") String code);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("User/personal.html")
    Flowable<PersonalInfoBean> requestPersonalInfo(@Field("uid") String uid);

    /**
     * ???????????????
     */
    @FormUrlEncoded
    @POST("Sms/msgSend.html")
    Flowable<BaseBean> sendVerificationCode(@Field("phone") String phone);

    /**
     * ????????????
     * phone ?????????
     * vcode ?????????
     * confirm ???????????? 10=???????????????????????????
     * appsn ???????????????
     */
    @FormUrlEncoded
    @POST("Login/loginv2.html")
    Flowable<LoginBean> requestLogin(@Field("phone") String phone, @Field("vcode") String vcode, @Field("confirm") String confirm, @Field("appsn") String appsn);

    /**
     * ?????????????????????????????????APP??????????????????
     * apptoken app??????token
     * appsn ???????????????
     */
    @FormUrlEncoded
    @POST("Login/checkAccv2.html")
    Flowable<LoginCheckAccv2Bean> requestCheckAccv2(@Field("apptoken") String apptoken, @Field("appsn") String appsn);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("Login/oneKeyLoginv2.html")
    Flowable<LoginBean> sendJgVerificationLogin(@Field("token") String token, @Field("confirm") String confirm, @Field("appsn") String appsn);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("User/operGrant.html")
    Flowable<BaseBean> sendLoginStatistics(@Field("type") String type, @Field("sourceId") String sourceId, @Field("uid") String uid);

    /**
     * ????????????????????????
     */
    @FormUrlEncoded
    @POST("VisitLogs/index.html")
    Flowable<BaseBean> sendHttpVisitLogs(@Field("title") String title, @Field("sourceType") String sourceType
            , @Field("sourceId") String sourceId, @Field("type") String type, @Field("client") String client);

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("Wallet/v8.html")
    Flowable<ExpenseBean> requestWalletInfo(@Field("uid") String uid);

    /**
     * ????????????-????????????????????????
     */
    @FormUrlEncoded
    @POST("User/depositRefundOrder.html")
    Flowable<DepositRefundOrderBean> requestDepositRefundOrder(@Field("uid") String uid);

    /**
     * ??????????????????????????????
     */
    @FormUrlEncoded
    @POST("User/depositRefundData.html")
    Flowable<BaseBean> requestSubmitDepositRefund(@Field("uid") String uid, @Field("oid") String oid, @Field("id") String id);

    /**
     * ??????????????????
     */
    @POST("PullCash/getUserProfit.html")
    Flowable<PullCashGetUserProfitBean> requestPullCashGetUserProfit();

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("PullCash/addCash.html")
    Flowable<BaseBean> requestPullCashAddCash(@Field("cash_amount") String cash_amount);

    /**
     * ????????????
     */
    @POST("PullCash/getCashRecord.html")
    Flowable<PullCashGetCashRecordBean> requestPullCashGetCashRecord();

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("PullActivity/inviteLists.html")
    Flowable<PullActivityInviteListsBean> requestPullActivityInviteLists(@Field("aid") String aid, @Field("type") String type, @Field("page") String page);

    /**
     * ??????????????????
     */
    @POST("PullCash/getUserAccountInfo.html")
    Flowable<PullCashGetUserAccountInfoBean> requestPullCashGetUserAccountInfo();

    /**
     * ???????????????????????????
     */
    @FormUrlEncoded
    @POST("PullCash/setUserAccountInfo.html")
    Flowable<BaseBean> requestPullCashSetUserAccountInfo(@Field("access_code") String access_code);


    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("Shop/buy.html")
    Flowable<ShopBuyBean> requestShopBuy(@Field("lng") String lng, @Field("lat") String lat);

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("Shop/rent.html")
    Flowable<ShopRentBean> requestShopRent(@Field("lng") String lng, @Field("lat") String lat);

    /**
     * ??????????????????
     */
    @FormUrlEncoded
    @POST("User/personal.html")
    Flowable<UserPersonalBean> requestUserPersonal(@Field("uid") String uid);

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("Find/index.html")
    Flowable<FindIndexBean> requestFindIndex(@Field("lng") String lng, @Field("lat") String lat);

    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("User/cancel.html")
    Flowable<BaseBean> requestUserCancel(@Field("uid") String uid, @Field("code") String code, @Field("confirm") String confirm);

    /**
     * ???????????????????????????
     */
    @POST("User/smsCancel.html")
    Flowable<BaseBean> requestUserSmsCancel();
    /**
     * ????????????
     */
    @FormUrlEncoded
    @POST("User/uOrder.html")
    Flowable<UserUOrderBean> requestUserUOrder(@Field("uid") String uid,@Field("lastid") String lastid);

    //????????????
    @FormUrlEncoded
    @POST("User/scanBind.html")
    Flowable<BaseBean> requestUserScanBind(@Field("qrstr") String qrstr);
}