package com.android.jidian.repair.widgets.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.widget.TextView;

import com.android.jidian.repair.R;


public class DialogByLoading {

    private Dialog progressDialog;
    private AnimationDrawable animationDrawable;
    private Activity context;

    private String text = "请稍候...";

    public DialogByLoading(Activity context) {
        this.context = context;
        init();

    }

    private void init() {
        progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.pub_dialog_loading);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = progressDialog.findViewById(R.id.loadingText);
        msg.setText(text);
    }

    public void setText(String str) {
        text = str;
    }

    public void setCanCancel(boolean b) {
        if (b == true) {
            progressDialog.setCancelable(true);
        } else {
            progressDialog.setCancelable(false);
        }
    }


    public void show() {
        init();
        if (animationDrawable != null) {
            animationDrawable.start();
        }
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    public void dismiss() {
        if (context != null && !context.isFinishing()) {
            progressDialog.dismiss();
        }
    }

    public void destroy() {
        progressDialog = null;
        animationDrawable = null;
    }
}