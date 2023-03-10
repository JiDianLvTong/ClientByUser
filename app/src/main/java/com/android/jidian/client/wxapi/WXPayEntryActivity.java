package com.android.jidian.client.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.jidian.client.bean.MainAppEventBean;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.mvp.ui.activity.main.MainActivity;
import com.android.jidian.client.mvp.ui.activity.main.MainActivityEvent;
import com.android.jidian.client.mvp.ui.activity.main.mainEquipmentFragment.MainEquipmentEvent;
import com.android.jidian.client.mvp.ui.activity.order.OrderListActivity;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.pub.weixin.Constants;
import com.android.jidian.client.util.BuryingPointManager;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//import com.tencent.mm.sdk.constants.ConstantsAPI;
//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESS;
import static com.android.jidian.client.bean.MainAppEventBean.PAYSUCCESSCLOSEORDERLIST;
import static com.android.jidian.client.pub.PubFunction.MY_WALLET_EWNEW;
import static com.android.jidian.client.pub.PubFunction.PAT_ING_TYPE;
import static com.android.jidian.client.pub.PubFunction.RECHARGE_HELLO_COIN;

/**
 * ?????????????????????
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * ??????????????????
     */
    @Override
    public void onResp(BaseResp baseResp) {
        //0 	?????? 	??????????????????
        //-1 	?????? 	??????????????????????????????????????????APPID???????????????APPID?????????????????????APPID??????????????????????????????????????????
        //-2 	???????????? 	????????????????????????????????????????????????????????????????????????APP???
        Log.e(TAG, "????????????onResp  onResp: WXPID="+Constants.APP_ID);
        Log.e(TAG, "????????????onResp??????????????? baseResp.errCode=" + baseResp.errCode
                + ",---baseResp.getType():" + baseResp.getType() + ",---baseResp.errStr:" + baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (baseResp.errCode == 0) {
                //????????????
                payHandler.sendEmptyMessageDelayed(0, 500);
            } else if (baseResp.errCode == -1) {
                //??????????????????
                showPayFailDialog("??????????????????");
//                startActivity(new Intent(WXPayEntryActivity.this, OrderListActivity.class));
            } else if (baseResp.errCode == -2) {
                showPayFailDialog("????????????");
//                startActivity(new Intent(WXPayEntryActivity.this, OrderListActivity.class));
            }
        } else {
            this.finish();
        }
    }

    /**
     * ??????????????????
     */
    private void showPayFailDialog(String msg) {
        Toast.makeText(WXPayEntryActivity.this, msg, Toast.LENGTH_LONG).show();
        this.finish();
    }

    Handler payHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
            String uid = sharedPreferences.getString("id", "");
            if (!uid.isEmpty()) {
                List<ParamTypeData> dataList1 = new ArrayList<>();
                dataList1.add(new ParamTypeData("sourceType", "5"));
                dataList1.add(new ParamTypeData("sourceId", uid));
                dataList1.add(new ParamTypeData("type", "202"));
                dataList1.add(new ParamTypeData("title", "????????????"));
                dataList1.add(new ParamTypeData("client", "Android"));
                HttpVisitLogs(dataList1);
            }
            Toast.makeText(WXPayEntryActivity.this, "???????????????", Toast.LENGTH_LONG).show();
            startActivity(new Intent(WXPayEntryActivity.this, MainActivity.class));
            finish();
            EventBus.getDefault().post(new MainActivityEvent(MainActivityEvent.LOGIN_SUCCESS));
            EventBus.getDefault().post(new MainAppEventBean(PAYSUCCESS));
            EventBus.getDefault().post(new MainAppEventBean(PAYSUCCESSCLOSEORDERLIST));
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return true;
    }

    //????????????????????????
    void HttpVisitLogs(List<ParamTypeData> dataList) {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        String uid = sharedPreferences.getString("id", "");
        new OkHttpConnect(this, PubFunction.api + "VisitLogs/index.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(this, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onVisitLogs(response, type);
            }
        }).startHttpThread();
    }

    void onVisitLogs(String response, String type) {
        if ("0".equals(type)) {
            System.out.println(response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                System.out.println(msg);
            } catch (JSONException e) {
                e.getLocalizedMessage();
            }
        }
    }

    private void requestPaymentBuryingPoint() {
        // ??????????????????
        if (!TextUtils.isEmpty(PAT_ING_TYPE)) {
            switch (PAT_ING_TYPE) {
                case PubFunction.PURCHASE_SHOPPING_BUY:
                    //??????????????????
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_PURCHASE_PAYMENT_WECHAT_SUCCESS);
                    break;
                case PubFunction.LEASING_SHOPPING_LEASE:
                    //??????????????????
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_EASE_PAYMENT_WECHAT_SUCCESS);
                    break;
                case MY_WALLET_EWNEW:
                    //??????????????????
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_RENEWAL_PAYMENT_WECHAT_SUCCESS);
                    break;
                case RECHARGE_HELLO_COIN:
                    //???????????????
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_HELLO_COIN_PAYMENT_WECHAT_SUCCESS);
                    break;
                case PubFunction.MY_ORDER_PAID:
                    //?????????????????????
                    BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_ORDER_TO_BE_PAID_WECHAT_SUCCESS);
                    break;
                default:
                    break;
            }
        }
    }
}