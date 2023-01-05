package com.android.jidian.client.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.BaseFragment;
import com.android.jidian.client.MainHello_;
import com.android.jidian.client.R;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.widgets.ProgressDialog;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.wallte_hello_fragment)
public class WallteHelloFragment extends BaseFragment {
    @ViewById
    TextView coin, coin_1, submit;
    List<Map<String, String>> datalist_1 = new ArrayList<>();
    public static Handler wallteHelloReloadHandler;

    @SuppressLint("HandlerLeak")
    private void handler() {
        wallteHelloReloadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                HttpGetWalletInfo();
            }
        };
    }

    @AfterViews
    void afterviews() {
        handler();
        progressDialog = new ProgressDialog(getActivity());
        HttpGetWalletInfo();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击立即续费
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.BUTTON_COIN_RECHARGE_IMMEDIATE_RENEWAL);
                if (getActivity() != null) {
                    getActivity().startActivity(new Intent(getActivity(), MainHello_.class));
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //钱包-换电币充值页访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_COIN_RECHARGE);
            }
        }, 500);
    }

    /**
     * http接口：User/walletv2.html  获取钱包信息
     */
    @Background
    void HttpGetWalletInfo() {
        sharedPreferences = requireActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        uid = sharedPreferences.getString("id", "");
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        new OkHttpConnect(getActivity(), PubFunction.app + "Wallet/hello.html", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(getActivity(), uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpGetWalletInfo(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }


    @UiThread
    void onDataHttpGetWalletInfo(String response, String type) {
        if (getActivity() == null) {
            return;
        }
        datalist_1.clear();
        if ("0".equals(type)) {
            MyToast.showTheToast(getActivity(), response);
        } else {
            try {
                JSONObject jsonObject_response = new JSONObject(response);
                String msg = jsonObject_response.getString("msg");
                String status = jsonObject_response.getString("status");
                System.out.println(jsonObject_response);
                if ("1".equals(status)) {
                    JSONObject jsonObject1 = jsonObject_response.getJSONObject("data");
                    JSONObject jsonObject = jsonObject1.getJSONObject("lists");
                    String hello_str = jsonObject.getString("hello");
                    AssetManager assetManager = getActivity().getAssets();
                    Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/DIN-Bold.otf");
                    coin.setText(hello_str);
                    coin.setTypeface(typeface);
                    String hello_free_str = jsonObject.getString("hello_free");
                    coin_1.setText(hello_free_str);
                    coin_1.setTypeface(typeface);
                } else {
                    MyToast.showTheToast(getActivity(), msg);
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                if (getActivity() != null) {
                    Toast.makeText(getContext(), "JSON：" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}