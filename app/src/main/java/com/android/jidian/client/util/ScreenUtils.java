package com.android.jidian.client.util;

import android.content.Context;

/**
 * @author : PTT
 * date: 2020/7/24 11:03
 * company: 兴达智联
 * description:
 */
public class ScreenUtils {
    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}