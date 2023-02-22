package com.android.jidian.extension.view.commonPlug.dialog;

import android.app.Activity;

import com.android.jidian.extension.R;

import razerdp.basepopup.BasePopupWindow;

public class DialogByLoading extends BasePopupWindow {

    private Activity activity;

    public DialogByLoading(Activity activity) {
        super(activity.getApplicationContext());
        this.activity = activity;
        setContentView(R.layout.dialog_loading);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
