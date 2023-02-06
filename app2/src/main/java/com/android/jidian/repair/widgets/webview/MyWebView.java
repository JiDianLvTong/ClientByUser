package com.android.jidian.repair.widgets.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebView extends CustomWebView {
    public static final String ABOUT_BLANK_URL = "about:blank";
    WebViewClientCallBack mWebViewClientCallBack;

    public void setWebViewClientCallBack(WebViewClientCallBack webViewClientCallBack) {
        mWebViewClientCallBack = webViewClientCallBack;
    }

    public MyWebView(Context context) {
        super(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_NONE, null);
        clearCache(true);

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        setBackgroundColor(0);
        settings.setUserAgentString(settings.getUserAgentString().concat("CP6.TgzO"));
        settings.setDomStorageEnabled(true);
        //WebView里的字体不随系统字体大小设置发生变化
        settings.setTextZoom(100);
        //设置默认为utf-8
        settings.setDefaultTextEncodingName("UTF-8");
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 设置允许访问文件数据
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(false);
        settings.setDatabaseEnabled(true);
        //WebView里的字体不随系统字体大小设置发生变化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        //允许混合模式（http与https）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();//忽略证书的错误继续Load页面内容，不会显示空白页面
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("http")) {
                    view.loadUrl(url);
                }
                if (mWebViewClientCallBack != null) {
                    mWebViewClientCallBack.shouldOverrideUrlLoading(view, url);
                }

                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //注意:为了解决部分手机loading不消失的问题，对应webeview onDestory方法中的loadBlank
                if (TextUtils.isEmpty(url) || ABOUT_BLANK_URL.equals(url)) {
                    return;
                }

                if (mWebViewClientCallBack != null) {
                    mWebViewClientCallBack.onPageStarted(view, url, favicon);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //加载页面成功的时候把
//                if (isError) {
//                    //页面加载异常时显示空白页；
//                    if(!ABOUT_BLANK_URL.equals(url)) {
//                        mErrorUrl = url;
//                        loadUrl(ABOUT_BLANK_URL);
//                    }
//                }
                if (mWebViewClientCallBack != null) {
                    mWebViewClientCallBack.onPageFinished(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                if (mWebViewClientCallBack != null) {
                    mWebViewClientCallBack.onReceivedError(view, request, error);
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                if (mWebViewClientCallBack != null) {
                    mWebViewClientCallBack.onReceivedHttpError(view, request, errorResponse);
                }
            }
        });

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && MyWebView.this.canGoBack()) {
                        MyWebView.this.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void onDestroy() {
//        loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        clearHistory();
        ((ViewGroup) getParent()).removeView(this);
        destroy();
    }
}