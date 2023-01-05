package com.android.jidian.client.util;

import android.util.Log;

/**
 * @author : PTT
 * date: 2020/8/26 11:30
 * company: 兴达智联
 * description: 埋点管理
 */
public class BuryingPointManager {
    /**
     * 首页
     */
    public static final String ACTIVITY_HOME_PAGE = "app_user_shouYe";
    /**
     * 点击个人中心按钮
     */
    public static final String BUTTON_HOME_PAGE_PERSONAL_CENTER = "app_user_shouYe_gRzX";
    /**
     * 点击新用户必读按钮
     */
    public static final String BUTTON_HOME_PAGE_NEW_USERS_MUST_READ = "app_user_shouYe_xYhBd";
    /**
     * 点击消息按钮
     */
    public static final String BUTTON_HOME_PAGE_MESSAGE = "app_user_shouYe_xiaoXi";
    /**
     * 点击列表按钮
     */
    public static final String BUTTON_HOME_PAGE_LIST = "app_user_shouYe_lieBiao";
    /**
     * 点击客服按钮
     */
    public static final String BUTTON_HOME_PAGE_CUSTOMER_SERVICE = "app_user_shouYe_keFu";
    /**
     * 点击刷新按钮
     */
    public static final String BUTTON_HOME_PAGE_REFRESH = "app_user_shouYe_shuaXin";
    /**
     * 点击扫一扫按钮
     */
    public static final String BUTTON_HOME_PAGE_SCAN = "app_user_shouYe_sao";
    /**
     * 点击定位按钮
     */
    public static final String BUTTON_HOME_PAGE_NAVIGATION = "app_user_shouYe_dingWei";
    /**
     * 点击换电站按钮
     */
    public static final String BUTTON_HOME_PAGE_STATION_CHANG = "app_user_shouYe_huanDianZhan";
    /**
     * 点击车行按钮
     */
    public static final String BUTTON_HOME_PAGE_CAR = "app_user_shouYe_cheHang";
    /**
     * 点击便利店按钮
     */
    public static final String BUTTON_HOME_PAGE_CONVENIENCE_STORE = "app_user_shouYe_bianLiDian";
    /**
     * 点击全部按钮
     */
    public static final String BUTTON_HOME_PAGE_ALL = "app_user_shouYe_quanBu";
    /**
     * 点击HELLO商城按钮
     */
    public static final String BUTTON_HOME_PAGE_HELLO_MALL = "app_user_shouYe_shangCheng";
    /**
     * 点击租车按钮
     */
    public static final String BUTTON_HOME_PAGE_CAR_RENTAL = "app_user_shouYe_zuChe";
    /**
     * 点击买车按钮
     */
    public static final String BUTTON_HOME_PAGE_BUY_A_CAR = "app_user_shouYe_maiChe";
    /**
     * 点击还车按钮
     */
    public static final String BUTTON_HOME_PAGE_RETURN_THE_CAR = "app_user_shouYe_huanChe";
    /**
     * 点击维修按钮
     */
    public static final String BUTTON_HOME_PAGE_REPAIR = "app_user_shouYe_weiXiu";
    /**
     * 点击全部服务按钮
     */
    public static final String BUTTON_HOME_PAGE_ALL_SERVICES = "app_user_shouYe_quanBuFuWu";
    /**
     * 点击站点
     */
    public static final String BUTTON_HOME_PAGE_SITE = "app_user_shouYe_zhanDian";

    /**
     * 租车地图页访问
     */
    public static final String ACTIVITY_CAR_RENTAL_MAP = "app_user_carRentalMap";

    /**
     * 买车地图页访问
     */
    public static final String ACTIVITY_CAR_BUY_MAP = "app_user_carBuyingMap";
    /**
     * 还车地图页访问
     */
    public static final String ACTIVITY_CAR_RETURN_MAP = "app_user_carAlsoMap";
    /**
     * 维修地图页访问
     */
    public static final String ACTIVITY_CAR_REPAIR_MAP = "app_user_maintenanceMap";

    /**
     * 站点详情页访问
     */
    public static final String ACTIVITY_SITE_DETAILS = "app_user_siteDetails";
    /**
     * 点击评论按钮
     */
    public static final String BUTTON_SITE_DETAILS_COMMENT = "app_user_siteDetails_comment";
    /**
     * 点击商城按钮
     */
    public static final String BUTTON_SITE_DETAILS_SHOPPING_MALL = "app_user_siteDetails_shoppingMall";
    /**
     * 点击电话按钮
     */
    public static final String BUTTON_SITE_DETAILS_TELEPHONE = "app_user_siteDetails_telephone";
    /**
     * 点击导航按钮
     */
    public static final String BUTTON_SITE_DETAILS_NAVIGATION = "app_user_siteDetails_navigation";

    /**
     * 站点评论页访问
     */
    public static final String ACTIVITY_SITE_REVIEWS = "app_user_siteReviews";
    /**
     * 点击评价按钮
     */
    public static final String BUTTON_SITE_REVIEWS_EVALUATE = "app_user_siteReviews_evaluate";

    /**
     * 商家购买页访问（通过点击站点-商城按钮进入的）
     */
    public static final String ACTIVITY_MERCHANT_PURCHASE_SHOPPING = "app_user_siteShoppingMall";
    /**
     * 点击购买按钮
     */
    public static final String BUTTON_MERCHANT_PURCHASE_SHOPPING_BUY = "app_user_siteShoppingMall_purchase";

    /**
     * 商家租赁页访问（通过点击站点-商城按钮进入的）
     */
    public static final String ACTIVITY_BUSINESS_LEASING_SHOPPING = "app_user_siteMallLeasing";
    /**
     * 点击租赁按钮
     */
    public static final String BUTTON_BUSINESS_LEASING_SHOPPING_LEASE = "app_user_siteMallLeasing_lease";

    /**
     * 购买支付页访问
     */
    public static final String ACTIVITY_PURCHASE_PAYMENT = "app_user_purchasePayment";
    /**
     * 点击确认支付按钮（支付宝）
     */
    public static final String BUTTON_PURCHASE_PAYMENT_ALIPAY = "app_user_purchasePayment_zhiFuBao";
    /**
     * 点击确认支付按钮（微信）
     */
    public static final String BUTTON_PURCHASE_PAYMENT_WECHAT = "app_user_purchasePayment_weiXin";
    /**
     * 点击确认支付按钮（线下支付）
     */
    public static final String BUTTON_PURCHASE_PAYMENT_OFFLINE = "app_user_purchasePayment_xianXia";
    /**
     * 支付宝支付成功
     */
    public static final String BUTTON_PURCHASE_PAYMENT_ALIPAY_SUCCESS = "app_user_purchasePayment_zhiFuBaoOK";
    /**
     * 微信支付成功
     */
    public static final String BUTTON_PURCHASE_PAYMENT_WECHAT_SUCCESS = "app_user_purchasePayment_weiXinOK";
    /**
     * 线下支付成功
     */
    public static final String BUTTON_PURCHASE_PAYMENT_OFFLINE_SUCCESS = "app_user_purchasePayment_xianXiaOK";

    /**
     * 租赁支付页访问
     */
    public static final String ACTIVITY_LEASE_PAYMENT = "app_user_leasePayment";
    /**
     * 点击确认支付按钮（支付宝）
     */
    public static final String BUTTON_LEASE_PAYMENT_ALIPAY = "app_user_leasePayment_zhiFuBao";
    /**
     * 点击确认支付按钮（微信）
     */
    public static final String BUTTON_EASE_PAYMENT_WECHAT = "app_user_leasePayment_weiXin";
    /**
     * 点击确认支付按钮（线下支付）
     */
    public static final String BUTTON_EASE_PAYMENT_OFFLINE = "app_user_leasePayment_xianXia";
    /**
     * 点击确认支付按钮（信用免押）
     */
    public static final String BUTTON_EASE_PAYMENT_CREDIT_FREE = "app_user_leasePayment_xinYongMY";
    /**
     * 支付宝支付成功
     */
    public static final String BUTTON_EASE_PAYMENT_ALIPAY_SUCCESS = "app_user_leasePayment_zhiFuBaoOK";
    /**
     * 微信支付成功
     */
    public static final String BUTTON_EASE_PAYMENT_WECHAT_SUCCESS = "app_user_leasePayment_weiXinOK";
    /**
     * 线下支付成功
     */
    public static final String BUTTON_EASE_PAYMENT_OFFLINE_SUCCESS = "app_user_leasePayment_xianXiaOK";
    /**
     * 信用免押支付成功
     */
    public static final String BUTTON_EASE_PAYMENT_CREDIT_FREE_SUCCESS = "app_user_leasePayment_xinYongMYOK";

    /**
     * 导航页访问
     */
    public static final String ACTIVITY_NAVIGATION = "app_user_navigation";

    /**
     * 换电列表页访问
     */
    public static final String ACTIVITY_POWER_CHANGE_LIST = "app_user_powerChangeList";
    /**
     * 点击《点我查看租车/买车/换电相关问题》按钮
     */
    public static final String BUTTON_POWER_CHANGE_LIST_WEB = "app_user_powerChangeList_chaKanWenTi";
    /**
     * 点击地图按钮
     */
    public static final String BUTTON_POWER_CHANGE_LIST_MAP = "app_user_powerChangeList_diTu";
    /**
     * 点击拨打商家带电话按钮
     */
    public static final String BUTTON_POWER_CHANGE_LIST_CALL_BUSINESS = "app_user_powerChangeList_boDa";
    /**
     * 点击导航按钮
     */
    public static final String BUTTON_POWER_CHANGE_LIST_NAVIGATION = "app_user_powerChangeList_daoHang";
    /**
     * 点击站点进入商家
     */
    public static final String BUTTON_POWER_CHANGE_LIST_ITEM = "app_user_powerChangeList_zhanDian";

    /**
     * 租车列表页访问
     */
    public static final String ACTIVITY_CAR_RENTAL_LIST = "app_user_carRentalList";

    /**
     * 买车列表页访问
     */
    public static final String ACTIVITY_BUY_CAR_LIST = "app_user_purchaseList";

    /**
     * 还车列表页访问
     */
    public static final String ACTIVITY_RETURN_CAR_LIST = "app_user_returnList";

    /**
     * 维修列表页访问
     */
    public static final String ACTIVITY_REPAIR_LIST = "app_user_maintenanceList";

//    /**
//     * 新用户指南-新用户页访问
//     */
//    public static final String ACTIVITY_NEW_USER_GUIDE = "app_user_newUserGuide";
//
//    /**
//     * 新用户指南-如何租页访问
//     */
//    public static final String ACTIVITY_NEW_USER_CAR_RENTAL_GUIDE = "app_user_newUserRent";
//
//    /**
//     * 新用户指南-缴费页访问
//     */
//    public static final String ACTIVITY_NEW_USER_PAY_GUIDE = "app_user_newUserPay";
//
//    /**
//     * 新用户指南-取电池页访问
//     */
//    public static final String ACTIVITY_NEW_USER_TAKE_BATTERY_GUIDE = "app_user_newUserBattery";
//
//    /**
//     * 新用户指南-还车页访问
//     */
//    public static final String ACTIVITY_NEW_USER_RETURN_CAR_GUIDE = "app_user_newUserStill";
//
//    /**
//     * 新用户指南-换电页访问
//     */
//    public static final String ACTIVITY_NEW_USER_CHANGE_GUIDE = "app_user_newUserChange";

    /**
     * 消息中心-活动消息页面访问
     */
    public static final String ACTIVITY_ACTIVITY_MSG = "app_user_activityMess";
    /**
     * 点击具体活动消息
     */
    public static final String BUTTON_ACTIVITY_MSG_ITEM = "app_user_activityMess_content";
    /**
     * 点击全部标记已读（活动消息）
     */
    public static final String BUTTON_ACTIVITY_MSG_ALL_READ = "app_user_activityMess_read";

    /**
     * 消息中心-系统消息页面访问
     */
    public static final String ACTIVITY_SYSTEM_MSG = "app_user_systemMess";
    /**
     * 点击具体系统消息
     */
    public static final String BUTTON_SYSTEM_MSG_ITEM = "app_user_systemMess_content";
    /**
     * 点击全部标记已读（系统消息）
     */
    public static final String BUTTON_SYSTEM_MSG_ALL_READ = "app_user_systemMess_read";

    /**
     * 客服页访问
     */
    public static final String ACTIVITY_CUSTOMER_SERVICE = "app_user_keFu";
    /**
     * 点击反馈按钮
     */
    public static final String BUTTON_CUSTOMER_SERVICE_FEEDBACK = "app_user_keFu_fankui";
    /**
     * 点击拨打010客服电话
     */
    public static final String BUTTON_CUSTOMER_SERVICE_010 = "app_user_keFu_010";
    /**
     * 点击拨打400客服电话
     */
    public static final String BUTTON_CUSTOMER_SERVICE_400 = "app_user_keFu_400";

    /**
     * 反馈页访问
     */
    public static final String ACTIVITY_FEEDBACK = "app_user_fanKui";
    /**
     * 点击确定按钮
     */
    public static final String BUTTON_FEEDBACK_SURE = "app_user_fanKui_OK";

    /**
     * 个人中心页面访问
     */
    public static final String ACTIVITY_PERSONAL_CENTER = "app_user_perCenter";
    /**
     * 点击头像/手机号
     */
    public static final String BUTTON_PERSONAL_CENTER_USER = "app_user_perCenter_user";
    /**
     * 点击我的钱包
     */
    public static final String BUTTON_PERSONAL_CENTER_WALLET = "app_user_perCenter_wallet";
    /**
     * 点击我的订单
     */
    public static final String BUTTON_PERSONAL_CENTER_MY_ORDER = "app_user_perCenter_order";
    /**
     * 点击申请贷款
     */
    public static final String BUTTON_PERSONAL_CENTER_APPLY_FOR_LOAN = "app_user_perCenter_loan";
    /**
     * 点击我的优惠券
     */
    public static final String BUTTON_PERSONAL_CENTER_MY_COUPON = "app_user_perCenter_coupon";
    /**
     * 点击HELLO客服
     */
    public static final String BUTTON_PERSONAL_CENTER_CUSTOMER = "app_user_perCenter_keFu";
    /**
     * 点击我的电动车
     */
    public static final String BUTTON_PERSONAL_CENTER_MY_ELECTRIC_CAR = "app_user_perCenter_dianChe";
    /**
     * 点击充电器设置
     */
    public static final String BUTTON_PERSONAL_CENTER_CHARGER_SETTINGS = "app_user_perCenter_charger";
    /**
     * 点击常见问题
     */
    public static final String BUTTON_PERSONAL_CENTER_COMMON_PROBLEM = "app_user_perCenter_problem";
    /**
     * 点击系统设置
     */
    public static final String BUTTON_PERSONAL_CENTER_SYSTEM_SETTINGS = "app_user_perCenter_system";

    /**
     * HELLO商城页访问（点击HELLO商城进入）
     */
    public static final String ACTIVITY_HELLO_MALL = "app_user_shopping";
    /**
     * 点击扫一扫按钮
     */
    public static final String BUTTON_HELLO_MALL_SCAN = "app_user_shopping_sao";

    /**
     * 商家购买页访问（通过扫一扫进入的）
     */
    public static final String ACTIVITY_MERCHANT_PURCHASE = "app_user_saoPurchase";
    /**
     * 点击购买按钮
     */
    public static final String BUTTON_MERCHANT_PURCHASE_BUY = "app_user_saoPurchase_purchase";

    /**
     * 商家租赁页访问（通过扫一扫进入的）
     */
    public static final String ACTIVITY_BUSINESS_LEASING = "app_user_saoLease";
    /**
     * 点击租赁按钮
     */
    public static final String BUTTON_BUSINESS_LEASING_LEASE = "app_user_saoLease_lease";

    /**
     * 个人信息页面访问
     */
    public static final String ACTIVITY_PERSONAL_INFO = "app_user_perInformation";
    /**
     * 点击手机号
     */
    public static final String BUTTON_PERSONAL_INFO_MOBILE = "app_user_perInformation_phone";
    /**
     * 点击实名认证
     */
    public static final String BUTTON_PERSONAL_INFO_AUTH = "app_user_perInformation_realName";

    /**
     * 钱包-续费页访问
     */
    public static final String ACTIVITY_RENEWAL = "app_user_walletRenew";
    /**
     * 点击电池寿命
     */
    public static final String BUTTON_RENEWAL_BATTTERY_LIFE = "app_user_batteryLife";
//    /**
//     * 点击续费
//     */
//    public static final String BUTTON_RENEWAL_RENEW = "app_user_walletRenew_renew";
//    /**
//     * 点击砍价
//     */
//    public static final String BUTTON_RENEWAL_BARGAINING = "app_user_walletRenew_bargaining";
    /**
     * 点击换电记录
     */
    public static final String BUTTON_RENEWAL_POWER_CHANGE_RECORD = "app_user_huanDian";
    /**
     * 点击立即续费
     */
    public static final String BUTTON_RENEWAL_IMMEDIATE_RENEWAL = "app_user_walletRenew_renewNow";

    /**
     * 钱包-换电币充值页访问
     */
    public static final String ACTIVITY_COIN_RECHARGE = "app_user_walletHalou";
    /**
     * 点击立即续费
     */
    public static final String BUTTON_COIN_RECHARGE_IMMEDIATE_RENEWAL = "app_user_walletHalou_renew";

    /**
     * 钱包-绑定页访问
     */
    public static final String ACTIVITY_BINDING = "app_user_walletBang";
    /**
     * 点击待绑定
     */
    public static final String BUTTON_BINDING_TO_BE_BOUND = "app_user_walletBang_daiBang";
    /**
     * 点击确定解绑
     */
    public static final String BUTTON_BINDING_DEFINE_UNBINDING = "app_user_walletBang_jieBang";

    /**
     * 电池寿命页面访问
     */
    public static final String ACTIVITY_BATTERY_LIFE = "app_user_dianChiShouMing";

    /**
     * 换电记录页面访问
     */
    public static final String ACTIVITY_POWER_CHANGE_RECORD = "app_user_huanDianJiLu";

    /**
     * 续费支付页面访问
     */
    public static final String ACTIVITY_RENEWAL_PAYMENT = "app_user_renewalPayment";
    /**
     * 支付宝确认支付
     */
    public static final String BUTTON_RENEWAL_PAYMENT_ALIPAY = "app_user_renewalPayment_zhifubao";
    /**
     * 微信确认支付
     */
    public static final String BUTTON_RENEWAL_PAYMENT_WECHAT = "app_user_renewalPayment_weixin";
    /**
     * 线下支付确认支付
     */
    public static final String BUTTON_RENEWAL_PAYMENT_OFFLINE_PAYMENT = "app_user_renewalPayment_xianxia";
    /**
     * 支付宝支付成功
     */
    public static final String BUTTON_RENEWAL_PAYMENT_ALIPAY_SUCCESS = "app_user_renewalPayment_zhifubaoOK";
    /**
     * 微信支付成功
     */
    public static final String BUTTON_RENEWAL_PAYMENT_WECHAT_SUCCESS = "app_user_renewalPayment_weixinOK";
    /**
     * 线下支付成功
     */
    public static final String BUTTON_RENEWAL_PAYMENT_OFFLINE_PAYMENT_SUCCESS = "app_user_renewalPayment_xianxiaOK";
    /**
     * 日租
     */
    public static final String BUTTON_RENEWAL_PAYMENT_DAILY_RENT = "app_user_renewalPayment_rizu";
    /**
     * 周租
     */
    public static final String BUTTON_RENEWAL_PAYMENT_WEEKLY_RENT = "app_user_renewalPayment_zhouzu";
    /**
     * 月租
     */
    public static final String BUTTON_RENEWAL_PAYMENT_MONTHLY_RENT = "app_user_renewalPayment_yuezu";
    /**
     * 季租
     */
    public static final String BUTTON_RENEWAL_PAYMENT_QUARTERLY_RENT = "app_user_renewalPayment_jizu";

    /**
     * 充值换电币页面访问
     */
    public static final String ACTIVITY_RECHARGE_HELLO_COIN = "app_user_chongHalou";
    /**
     * 点击5元
     */
    public static final String BUTTON_RECHARGE_HELLO_COIN_5 = "app_user_chongHalou_5";
    /**
     * 点击50元
     */
    public static final String BUTTON_RECHARGE_HELLO_COIN_50 = "app_user_chongHalou_50";
    /**
     * 点击100元
     */
    public static final String BUTTON_RECHARGE_HELLO_COIN_100 = "app_user_chongHalou_100";
    /**
     * 点击200元
     */
    public static final String BUTTON_RECHARGE_HELLO_COIN_200 = "app_user_chongHalou_200";
    /**
     * 点击400元
     */
    public static final String BUTTON_RECHARGE_HELLO_COIN_400 = "app_user_chongHalou_400";

    /**
     * 换电币支付页面
     */
    public static final String ACTIVITY_HELLO_COIN_PAYMENT = "app_user_halouPay";
    /**
     * 支付宝支付成功
     */
    public static final String BUTTON_HELLO_COIN_PAYMENT_ALIPAY_SUCCESS = "app_user_halouPay_zhifubaoOK";
    /**
     * 微信支付成功
     */
    public static final String BUTTON_HELLO_COIN_PAYMENT_WECHAT_SUCCESS = "app_user_halouPay_weixinOK";

    /**
     * 我的订单页面访问
     */
    public static final String ACTIVITY_MY_ORDER = "app_user_order";
    /**
     * 点击待支付订单
     */
    public static final String BUTTON_MY_ORDER_ORDER_TO_BE_PAID = "app_user_order_daiZhiFu";

    /**
     * 待支付订单支付页面访问
     */
    public static final String ACTIVITY_ORDER_TO_BE_PAID = "app_user_orderPay";
    /**
     * 支付宝支付成功
     */
    public static final String BUTTON_ORDER_TO_BE_PAID_ALIPAY_SUCCESS = "app_user_orderPay_zhifubaoOK";
    /**
     * 微信支付成功
     */
    public static final String BUTTON_ORDER_TO_BE_PAID_WECHAT_SUCCESS = "app_user_orderPay_weixinOK";

    /**
     * 骑呗页面访问
     */
    public static final String ACTIVITY_RIDE = "app_user_qiBei";

    /**
     * 我的优惠券-未使用页面访问（默认页）
     */
    public static final String ACTIVITY_UNUSED_MY_COUPONS = "app_user_weiShiQuan";
    /**
     * 点击扫一扫
     */
    public static final String BUTTON_UNUSED_MY_COUPONS_SCAN = "app_user_weiShiQuan_sao";
    /**
     * 点击立即兑换
     */
    public static final String BUTTON_UNUSED_MY_COUPONS_REDEEM_NOW = "app_user_weiShiQuan_duiHuan";
    /**
     * 点击未兑换优惠券
     */
    public static final String BUTTON_UNUSED_MY_COUPONS_UN_REDEEMED_COUPONS = "app_user_weiShiQuan_weiDuiHuan";

    /**
     * 我的优惠券-已使用页面访问
     */
    public static final String ACTIVITY_MY_COUPONS = "app_user_yiShiQuan";

    /**
     * 我的电动车页面访问
     */
    public static final String ACTIVITY_MY_ELECTRIC_CAR = "app_user_myDianChe";
    /**
     * 点击实时跟踪
     */
    public static final String BUTTON_MY_ELECTRIC_CAR_REAL_TIME_TRACKING = "app_user_myDianChe_shiShiGenZong";
    /**
     * 点击轨迹回放
     */
    public static final String BUTTON_MY_ELECTRIC_CAR_TRACK_PLAYBACK = "app_user_myDianChe_guiJi";

    /**
     * 电动车实时跟踪页面访问
     */
    public static final String ACTIVITY_ELECTRIC_VEHICLE_REAL_TIME_TRACKING = "app_user_shiShiGenZong";
    /**
     * 点击寻车路线
     */
    public static final String BUTTON_ELECTRIC_VEHICLE_RT_SEARCH_ROUTE = "app_user_shiShiGenZong_xunChe";

    /**
     * 电动车轨迹回放页面访问
     */
    public static final String ACTIVITY_ELECTRIC_VEHICLE_TRACK_PLAYBACK = "app_user_guiJi";
    /**
     * 点击开始回放
     */
    public static final String BUTTON_ELECTRIC_VEHICLE_TRACK_PLAYBACK_PLAYBACK = "app_user_guiJi_start";

    /**
     * 充电器页面访问
     */
    public static final String ACTIVITY_CHARGER = "app_user_chongDianQi";
    /**
     * 点击设置充电器
     */
    public static final String BUTTON_CHARGER_SETTINGS = "app_user_chongDianQi_sheZhi";

    /**
     * 更多问题页面访问
     */
    public static final String ACTIVITY_MORE_QUESTIONS = "app_user_problem";

    /**
     * 系统设置页面访问
     */
    public static final String ACTIVITY_SYSTEM_SETTINGS = "app_user_sheZhi";
    /**
     * 点击关于我们
     */
    public static final String BUTTON_SYSTEM_SETTINGS_ABOUT_US = "app_user_sheZhi_we";
    /**
     * 点击用户协议
     */
    public static final String BUTTON_SYSTEM_SETTINGS_USER_AGREEMENT = "app_user_sheZhi_userAgreement";
    /**
     * 点击法律声明
     */
    public static final String BUTTON_SYSTEM_SETTINGS_LEGAL_STATEMENT = "app_user_legalStatement";
    /**
     * 点击注销用户
     */
    public static final String BUTTON_SYSTEM_SETTINGS_LOG_OFF_USER = "app_user_cancellation";
    /**
     * 点击退出登录
     */
    public static final String BUTTON_SYSTEM_SETTINGS_LOG_OUT = "app_user_signOut";

//    /**
//     * 注销用户页
//     */
//    public static final String ACTIVITY_LOG_OFF_USER = "app_user_cancellation";
    /**
     * 点击确定注销成功
     */
    public static final String BUTTON_LOG_OFF_USER_SURE = "app_user_cancellation_OK";
//    /**
//     * 点击取消
//     */
//    public static final String BUTTON_LOG_OFF_USER_CANCEL = "app_user_cancellation_NO";

    /**
     * 登录页面访问
     */
    public static final String ACTIVITY_LOGIN = "app_user_signIn";
    /**
     * 登录成功
     */
    public static final String BUTTON_LOGIN_SUCCESS = "app_user_signIn_OK";

    /**
     * 新增埋点接口
     */
    public static void sendBuryingPoint(String tag) {
        Log.e("http------tag打印：", tag);
        /*Disposable disposable = RetrofitClient.getInstance().getAppServiceApi()
                .sendBuryingPoint(tag)
                .compose(RxScheduler.Flo_io_main())
                .subscribe(new Consumer<BaseBean>() {
                    @Override
                    public void accept(BaseBean bean) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });*/
    }
}