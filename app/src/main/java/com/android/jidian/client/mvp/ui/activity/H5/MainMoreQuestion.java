package com.android.jidian.client.mvp.ui.activity.H5;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hasee on 2017/6/6.
 */


public class MainMoreQuestion extends U6BaseActivity {
    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;
    @BindView(R.id.webView)
    public WebView webView;

    private String url = "https://appx.mixiangx.com/web/problem3.html";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.u6_activity_h5_more_question);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init(){
        webView.setInitialScale(300);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    @OnClick(R.id.pageReturn)
    public void onClickPageReturn(){
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}