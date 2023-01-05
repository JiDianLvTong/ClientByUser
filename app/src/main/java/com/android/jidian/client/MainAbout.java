package com.android.jidian.client;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.jidian.client.pub.PubFunction;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hasee on 2017/6/6.
 */
@EActivity(R.layout.main_about)
public class MainAbout extends BaseActivity {
    @ViewById
    WebView mWebView;
    private int clicktime = 0;
    private AlertDialog alertDialog;

    @AfterViews
    void afterViews() {
        if (PubFunction.isConnect(activity, true)) {
            mWebView.setInitialScale(300);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewController());
            mWebView.loadUrl("https://appx.mixiangx.com/web/pinpai/about3.html");
        }
        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(PubFunction.app);
        builder.setCancelable(false);
        builder.setPositiveButton("queding", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
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

    @Click
    void onDoubleClick() {
        Timer tExit;
        if (clicktime < 5) {
            clicktime++;
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    clicktime = 0;//取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            alertDialog.show();
        }
    }
}