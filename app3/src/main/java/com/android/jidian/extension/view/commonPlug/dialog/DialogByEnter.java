package com.android.jidian.extension.view.commonPlug.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.extension.R;

import razerdp.basepopup.BasePopupWindow;

public class DialogByEnter extends BasePopupWindow {

    public interface DialogChoiceListener{
        void enterReturn();
    }

    private Activity activity;
    private String title;
    private DialogByEnter dialogChoice;

    private TextView titleView;
    private TextView enterView;

    private DialogChoiceListener dialogChoiceListener;
    private boolean clickBackgroundHasDismiss = false;

    public DialogByEnter(Activity activity , String title , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.title = title;
        this.dialogChoiceListener = dialogChoiceListener;
        dialogChoice = this;
        setContentView(R.layout.dialog_enter);
        init();
    }

    public DialogByEnter(Activity activity , String title) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.title = title;
        dialogChoice = this;
        setContentView(R.layout.dialog_enter);
        init();
    }

    private void init(){
        titleView = this.findViewById(R.id.title);
        titleView.setText(title);
        enterView = this.findViewById(R.id.enterView);
        enterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice.dismiss();
                if(dialogChoiceListener!=null){
                    dialogChoiceListener.enterReturn();
                }
            }
        });
        setOutSideDismiss(clickBackgroundHasDismiss);
    }

    public void setClickBackgroundHasDismiss(boolean b){
        this.clickBackgroundHasDismiss = b;
        setOutSideDismiss(clickBackgroundHasDismiss);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
