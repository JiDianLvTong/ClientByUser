package com.android.jidian.client.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

import com.android.jidian.client.mvp.ui.activity.CustomH5Page;

/**
 * @author : PTT
 * date: 8/6/21 4:57 PM
 * company: 兴达智联
 * description:
 */
public class URLSpanNoUnderline extends URLSpan {
    private Context mContext;

    public URLSpanNoUnderline(Context context, String url) {
        super(url);
        this.mContext = context;
    }

    @Override
    public void onClick(View widget) {
        super.onClick(widget);
        if(mContext!=null){
            Intent mIntent = new Intent(mContext, CustomH5Page.class);
            mIntent.putExtra("url", "https://appx.mixiangx.com/web/pinpai/privacy3.html");
            mContext.startActivity(mIntent);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setUnderlineText(false);
        ds.setColor(Color.parseColor("#0669FF"));
    }
}