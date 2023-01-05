package com.android.jidian.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.jidian.client.pub.PubFunction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hasee on 2017/6/6.
 */
public class MainAdv extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.mWebView)
    WebView mWebView;

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    class WebViewController extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (!url.startsWith("http:") && !url.startsWith("https:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    MainAdv.this.startActivity(intent);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_adv);
        ButterKnife.bind(this);
        String url = "";
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
        }
        tvTitle.setText(getString(R.string.str_activity_detail));
        if (!TextUtils.isEmpty(url)) {
            if (PubFunction.isConnect(activity, true)) {
                WebSettings webSettings = mWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setLoadWithOverviewMode(true);
                mWebView.setWebViewClient(new WebViewController());
                mWebView.loadUrl(url);
            }
        }
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        finish();
    }
}