package com.android.jidian.client.mvp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.jidian.client.BaseActivity;
import com.android.jidian.client.R;
import com.android.jidian.client.pub.PubFunction;
import com.android.jidian.client.util.BuryingPointManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreQuestion extends BaseActivity {
    @BindView(R.id.mWebView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_question);
        ButterKnife.bind(this);
        if (PubFunction.isConnect(activity, true)) {
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewController());
            mWebView.loadUrl("https://appx.mixiangx.com/web/problem3.html");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //更多问题页面访问
                BuryingPointManager.sendBuryingPoint(BuryingPointManager.ACTIVITY_MORE_QUESTIONS);
            }
        }, 500);
    }

    @OnClick(R.id.page_return)
    public void onViewClicked() {
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