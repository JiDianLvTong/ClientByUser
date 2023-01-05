package com.android.jidian.client.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseActivity2;
import com.android.jidian.client.bean.LoginBean;
import com.android.jidian.client.bean.PullActivityPoliteBean;
import com.android.jidian.client.mvp.contract.LoginContract;
import com.android.jidian.client.mvp.presenter.LoginPresenter;
import com.google.gson.Gson;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import wendu.dsbridge.CompletionHandler;
import wendu.dsbridge.DWebView;

public class InviteActivity extends BaseActivity2<LoginPresenter> implements LoginContract.View {

    private String inviteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invite);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        inviteId = getIntent().getStringExtra("inviteId");
        String url = getIntent().getStringExtra("url");
        UMConfigure.init(this, "619ef303e0f9bb492b6d5fe9", "xiaoming", UMConfigure.DEVICE_TYPE_PHONE, null);
        ImageView titleLayoutImageView1 = findViewById(R.id.title_layout_imageView1);
        titleLayoutImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView titleLayoutTextView1 = findViewById(R.id.title_layout_textView1);
        titleLayoutTextView1.setText("活动");
        DWebView webView = findViewById(R.id.web_view);
        webView.addJavascriptObject(new JsApi(), null);
        webView.getSettings().setTextZoom(100);
//        webView.loadUrl(PubFunction.h5 + "/Polite/index.html");
        webView.loadUrl(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(InviteActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(InviteActivity.this, "分享失败" + t.getMessage(), Toast.LENGTH_LONG).show();
            if (mHandler != null) {
                mHandler.complete("分享失败");
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(InviteActivity.this, "分享取消", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    public void sendVerificationCodeResult(String msg) {

    }

    @Override
    public void requestLoginSuccess(LoginBean bean) {

    }

    @Override
    public void requestLoginError(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    private CompletionHandler<String> mHandler;

    public class JsApi {
        //同步API
//    @JavascriptInterface
//    public String testObjecCallback(Object msg)  {
//        Log.d("xiaoming923", "testObjecCallback: ");
//        return msg + "［syn call］";
//    }

        //异步API
        @JavascriptInterface
        public void getInitInfo(Object msg, CompletionHandler<String> handler) {
            Log.d("xiaoming1127", "getInitInfo:      " + uid + "       " + inviteId);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("uid", uid);
                jsonObject.put("inviteId", inviteId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            handler.complete(jsonObject.toString());
        }

        //异步API
        @JavascriptInterface
        public void OnClickWeChatShare(Object msg, CompletionHandler<String> handler) {
            if (msg != null) {
                mHandler = handler;
                PullActivityPoliteBean bean = new Gson().fromJson(msg.toString(), PullActivityPoliteBean.class);
                UMWeb web = new UMWeb(bean.getLink());
                web.setTitle(bean.getTitle());//标题
                web.setThumb(new UMImage(InviteActivity.this, bean.getImgUrl()));//缩略图
                web.setDescription(bean.getDesc());//描述

                new ShareAction(InviteActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                        .withMedia(web)
                        .setCallback(umShareListener)//回调监听器
                        .share();
            } else {
                showMessage("数据异常");
            }
        }

        //异步API
        @JavascriptInterface
        public void OnClickWeChatMoments(Object msg, CompletionHandler<String> handler) {
            if (msg != null) {
                mHandler = handler;
                PullActivityPoliteBean bean = new Gson().fromJson(msg.toString(), PullActivityPoliteBean.class);
                UMWeb web = new UMWeb(bean.getLink());
                web.setTitle(bean.getTitle());//标题
                web.setThumb(new UMImage(InviteActivity.this, bean.getImgUrl()));//缩略图
                web.setDescription(bean.getDesc());//描述

                new ShareAction(InviteActivity.this)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                        .withMedia(web)
                        .setCallback(umShareListener)//回调监听器
                        .share();
            } else {
                showMessage("数据异常");
            }
        }

        //异步API
        @JavascriptInterface
        public void OnClickInviteRecode(Object msg, CompletionHandler<String> handler) {
            Log.d("xiaoming923", "testObjecCallback: ");
            Intent intent = new Intent(InviteActivity.this, InviteDetailActivity.class);
            intent.putExtra("aid", inviteId);
            startActivity(intent);
        }
    }


}