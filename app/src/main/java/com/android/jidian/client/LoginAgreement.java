package com.android.jidian.client;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.android.jidian.client.widgets.ScrollWebView;
import com.android.jidian.client.http.HeaderTypeData;
import com.android.jidian.client.http.OkHttpConnect;
import com.android.jidian.client.http.ParamTypeData;
import com.android.jidian.client.widgets.MyToast;
import com.android.jidian.client.pub.PubFunction;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.login_agreement)
public class LoginAgreement extends BaseActivity {

    @ViewById
    ScrollWebView web_view;
    @ViewById
    TextView agreement_buttom;
    private int timeThread_code = 10;
    private Thread timeThread = new Thread() {
        @Override
        public void run() {
            super.run();
            while (timeThread_code > 0) {
                try {
                    sleep(1000);
                    setTextHandler_1();
                    timeThread_code = timeThread_code - 1;
                } catch (InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            }
            setTextHandler_2();
        }
    };

    @AfterViews
    void afterViews() {
        init();
        timeThread.start();
    }

    @UiThread
    void setTextHandler_1() {
        agreement_buttom.setText("请下拉到底部并阅读协议（ " + timeThread_code + " ）");
    }

    @UiThread
    void setTextHandler_2() {
        agreement_buttom.setBackgroundColor(0xfffb2408);
        agreement_buttom.setText("我已阅读完并同意此协议");
        agreement_buttom.setClickable(true);
        agreement_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpIsAgreement();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeThread_code = 0;
    }

    private void init() {
        WebSettings webSettings = web_view.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web_view.loadUrl("https://www.mixiangx.com/userAgreement.html");
        web_view.setInitialScale(200);
        web_view.setOnScrollChangeListener(new ScrollWebView.OnScrollChangeListener() {
            @Override
            public void onPageEnd(int l, int t, int oldl, int oldt) {
                agreement_buttom.setBackgroundColor(0xfffb2408);
                agreement_buttom.setText("我已阅读完并同意此协议");
                agreement_buttom.setClickable(true);
                agreement_buttom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpIsAgreement();
                        progressDialog.show();
                    }
                });
                timeThread_code = 0;
            }

            @Override
            public void onPageTop(int l, int t, int oldl, int oldt) {
            }

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * http接口：User/save_agreement    设置协议已读
     */
    @Background
    void HttpIsAgreement() {
        List<ParamTypeData> dataList = new ArrayList<>();
        dataList.add(new ParamTypeData("uid", uid));
        dataList.add(new ParamTypeData("status", "1"));
        new OkHttpConnect(activity, PubFunction.app + "User/save_agreement", dataList, HeaderTypeData.HEADER_Whit_APTK_APUD_PRO(activity, uid), new OkHttpConnect.ResultListener() {
            @Override
            public void onSuccessResult(String response, String type) {
                onDataHttpIsAgreement(response, type);
                progressDialog.dismiss();
            }
        }).startHttpThread();
    }

    @UiThread
    void onDataHttpIsAgreement(String response, String type) {
        if ("0".equals(type)) {
            MyToast.showTheToast(activity, response);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                String status = jsonObject.getString("status");
                if ("1".equals(status)) {
                    MyToast.showTheToast(activity, msg);
                }
            } catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        this.finish();
    }
}