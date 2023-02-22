package com.android.jidian.extension.view.commonPlug.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.extension.R;

import razerdp.basepopup.BasePopupWindow;

public class DialogByUpdate extends BasePopupWindow {

    public interface DialogUpdateListener{
        void enterReturn();
        void cancelReturn();
    }

    private Activity activity;
    private DialogByUpdate dialogUpdate;
    private TextView title;
    private String titleStr;
    private TextView enterView;
    private TextView cancelView;
    private DialogUpdateListener dialogUpdateListener;

    public DialogByUpdate(Activity activity, String version , DialogUpdateListener dialogUpdateListener) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.titleStr = version;
        this.dialogUpdateListener = dialogUpdateListener;
        dialogUpdate = this;
        setContentView(R.layout.dialog_update);
        init();
    }

    private void init(){
        title = this.findViewById(R.id.title);
        title.setText("发现新版本：" + titleStr);
        enterView = this.findViewById(R.id.enterView);
        cancelView = this.findViewById(R.id.cancelView);
        enterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdate.dismiss();
                dialogUpdateListener.enterReturn();
            }
        });
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogUpdate.dismiss();
                dialogUpdateListener.cancelReturn();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
