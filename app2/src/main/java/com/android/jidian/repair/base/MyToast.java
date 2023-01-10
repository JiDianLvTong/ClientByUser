package com.android.jidian.repair.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jidian.repair.R;

/**
 * @author : xiaoming
 * date: 2023/1/10 14:52
 * description:
 */
public class MyToast {
    /**
     * 判断主线程Toast
     */
    public static void showTheToast(final Context context, final String message) {
        try {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(context, message);
                    }
                });
            } else {
                showToast(context, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showToast(Context context, String string) {
        if (!TextUtils.isEmpty(string)) {
            if (context == null) {
                return;
            }
            try {
                View view = LayoutInflater.from(context).inflate(R.layout.common_toast, null);
                TextView textView = view.findViewById(R.id.text_1);
                textView.setText(string);
                Toast toast = new Toast(context);
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
