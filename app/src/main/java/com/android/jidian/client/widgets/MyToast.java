package com.android.jidian.client.widgets;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.client.R;
import com.android.jidian.client.application.MyApplication;

/**
 * Created by hasee on 2017/6/8.
 */
public class MyToast {
    /**
     * 判断主线程Toast
     */
    public static void showTheToast(Context context, String message) {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(Looper.getMainLooper()).post(() -> showToast(message));
            } else {
                showToast(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showToast(String string) {
        if (!TextUtils.isEmpty(string)) {
            if (MyApplication.getContext() == null) {
                return;
            }
            try {
                View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.toast_panel, null);
                TextView textView = view.findViewById(R.id.text_1);
                textView.setText(string);
                Toast toast = new Toast(MyApplication.getContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setView(view);
                toast.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}