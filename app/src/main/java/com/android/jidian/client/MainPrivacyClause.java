package com.android.jidian.client;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.jidian.client.pub.PubFunction;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.main_privacy_clause)
public class MainPrivacyClause extends BaseActivity {
    @ViewById
    WebView web_view;

    @AfterViews
    void afterViews() {
        if (PubFunction.isConnect(activity, true)) {
            web_view.setInitialScale(300);
            WebSettings webSettings = web_view.getSettings();
            webSettings.setJavaScriptEnabled(true);
            web_view.setWebViewClient(new WebViewController());
            web_view.loadUrl("https://appx.mixiangx.com/web/pinpai/privacy3.html");
        }
    }

    @Click
    void page_return() {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}