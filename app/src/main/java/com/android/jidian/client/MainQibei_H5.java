package com.android.jidian.client;

import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.jidian.client.pub.PubFunction;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by hasee on 2017/6/6.
 */
@EActivity(R.layout.main_qibei_h5)
public class MainQibei_H5 extends BaseActivity {
    @ViewById
    WebView web_view;
    @ViewById
    TextView top_text;

    @AfterViews
    void afterViews() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String name = intent.getStringExtra("name");
        top_text.setText(name);
        if (PubFunction.isConnect(activity, true)) {
            web_view.setInitialScale(200);
            WebSettings webSettings = web_view.getSettings();
            webSettings.setJavaScriptEnabled(true);
            web_view.loadUrl(url);
        }
    }

    @Click
    void page_return() {
        this.finish();
    }
}