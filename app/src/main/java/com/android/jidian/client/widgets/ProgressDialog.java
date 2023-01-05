package com.android.jidian.client.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.R;


public class ProgressDialog {
    private Dialog progressDialog;
    private AnimationDrawable animationDrawable;
    private Activity context;


    private String text = "请稍候...";

    public ProgressDialog(Activity context) {
        this.context = context;
        init();

    }

    private void init() {
        progressDialog = new Dialog(context, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.dialog);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText(text);
        ImageView bar = progressDialog.findViewById(R.id.loadingPrigressBar);
        bar.setImageResource(R.drawable.progress_drawable_white);
        animationDrawable = (AnimationDrawable) bar.getDrawable();
    }

    public void setText(String str) {
        text = str;
    }

    public void setcanCancele(boolean b) {
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
            animationDrawable.stop();
            progressDialog.dismiss();
        }
    }

    public void destory() {
        progressDialog = null;
        animationDrawable = null;
    }
}