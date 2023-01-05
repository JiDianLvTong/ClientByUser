package com.android.jidian.client;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.jidian.client.pub.PubFunction;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2017/6/6.
 */
@EActivity(R.layout.main_agreement)
public class MainAgreement extends BaseActivity {
    @ViewById
    WebView web_view;

    @AfterViews
    void afterViews() {
        if (PubFunction.isConnect(activity, true)) {
            web_view.setInitialScale(300);
            WebSettings webSettings = web_view.getSettings();
            webSettings.setJavaScriptEnabled(true);
            web_view.loadUrl("https://appx.mixiangx.com/web/pinpai/userAgreement3.html");
        }
    }

    @Click
    void page_return() {
        this.finish();
    }
}