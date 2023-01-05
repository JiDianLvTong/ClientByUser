package com.android.jidian.client.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.jidian.client.BaseActivity;
import com.android.jidian.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wendu.webviewjavascriptbridge.WVJBWebView;

/**
 * 通用的H5页面
 */
public class CustomH5Page extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.web_view)
    WVJBWebView webView;

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            CustomH5Page.this.tvTitle.setText(title);
        }
    };

    class WebViewController extends WebViewClient {
        WebViewController() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            CustomH5Page.this.progressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CustomH5Page.this.progressDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_h5_page);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        ButterKnife.bind(this);
        tvTitle.setText("新用户必读");
        WebSettings webSettings = this.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.requestFocus();
        this.webView.getSettings().setUseWideViewPort(true);
        this.webView.getSettings().setLoadWithOverviewMode(true);
        this.webView.getSettings().setSupportZoom(true);
        this.webView.getSettings().setTextZoom(80);
        this.webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        this.webView.getSettings().setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= 21) {
            this.webView.getSettings().setMixedContentMode(0);
        }
        this.webView.getSettings().setBlockNetworkImage(false);
        this.webView.setWebViewClient(new WebViewController());
        this.webView.setWebChromeClient(this.webChromeClient);
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("https")) {
                this.webView.loadUrl(url);
            } else {
                this.webView.loadUrl("https://h5x.mixiangx.com/" + url);
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}