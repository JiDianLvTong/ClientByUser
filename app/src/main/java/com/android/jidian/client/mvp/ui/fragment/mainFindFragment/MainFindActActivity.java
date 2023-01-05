package com.android.jidian.client.mvp.ui.fragment.mainFindFragment;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.android.jidian.client.BaseActivity;
import com.android.jidian.client.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainFindActActivity extends BaseActivity {

    @BindView(R.id.pageReturn)
    public LinearLayout pageReturn;
    @BindView(R.id.webView)
    public WebView webView;

    private String tarUrl = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_find_act);
        ButterKnife.bind(this);
        init();
    }

    private void init(){

        tarUrl = getIntent().getStringExtra("url");

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
        webView.loadUrl(tarUrl);
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
