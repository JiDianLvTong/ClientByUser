package com.android.jidian.repair.mvp.H5;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.base.BaseActivityByMvp;
import com.android.jidian.repair.mvp.login.LoginBean;
import com.android.jidian.repair.mvp.login.LoginContract;
import com.android.jidian.repair.mvp.login.LoginPresenter;
import com.android.jidian.repair.widgets.webview.MyWebView;
import com.android.jidian.repair.widgets.webview.WebViewClientCallBackImpI;

import butterknife.BindView;
import butterknife.OnClick;

public class CommonWebViewActivity extends BaseActivityByMvp<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv_layout)
    MyWebView wvLayout;
    /**
     * Web加载url ,标题
     */
    private String mUrl, mTitle;

    /**
     * 设置URL 完整url
     */
    public static final String SETURL = "common_web_url";
    /**
     * 设置标题 新的webView页面title
     */
    public static final String SETTITLE = "common_web_title";

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_webview;
    }

    @Override
    public void initView() {
        mUrl = getIntent().getStringExtra(SETURL);
        mTitle = getIntent().getStringExtra(SETTITLE);
        if (TextUtils.isEmpty(mUrl)) {
            finish();
        }

        if (TextUtils.isEmpty(mTitle)) {
            tvTitle.setText("活动");
        } else {
            tvTitle.setText(mTitle);
        }
        wvLayout.setWebViewClientCallBack(new WebViewClientCallBackImpI() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgress();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideProgress();
            }
        });
        wvLayout.loadUrl(mUrl);
    }

    @OnClick({R.id.pageReturn})
    public void onViewClicked() {
        finish();
    }

    @Override
    public void showProgress() {
        progressDialog.show();

    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();

    }

    @Override
    public void onError(Throwable throwable) {
    }

    /**
     * 重新Load初始链接
     */
    private void goLoadInitUrl() {
        wvLayout.loadUrl(mUrl);
        wvLayout.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                wvLayout.clearHistory();
            }
        });
    }

    @Override
    public void onDestroy() {
        if (wvLayout != null) {
            wvLayout.onDestroy();
            wvLayout = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (wvLayout.canGoBack()) {
            wvLayout.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void requestLoginJdLoginSuccess(LoginBean.DataBean bean) {

    }

    @Override
    public void requestShowTips(String msg) {

    }
}