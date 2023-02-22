package com.android.jidian.extension.view.commonPlug.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.extension.R;

import razerdp.basepopup.BasePopupWindow;

public class DialogByChoice extends BasePopupWindow {

    public interface DialogChoiceListener{
        void enterReturn();
        void cancelReturn();
    }

    private Activity activity;
    private String title;
    private DialogByChoice dialogChoice;

    private TextView titleView;
    private TextView enterView;
    private TextView cancelView;

    private String enter = "";
    private String cancel = "";

    private DialogChoiceListener dialogChoiceListener;

    public DialogByChoice(Activity activity , String title , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.title = title;
        this.dialogChoiceListener = dialogChoiceListener;
        dialogChoice = this;
        setContentView(R.layout.dialog_choice);
        init();
    }

    public DialogByChoice(Activity activity , String title , String enter , String cancel , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.title = title;
        this.enter = enter;
        this.cancel = cancel;
        this.dialogChoiceListener = dialogChoiceListener;
        dialogChoice = this;
        setContentView(R.layout.dialog_choice);
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
                dialogChoiceListener.enterReturn();
            }
        });
        if(!enter.equals("")){
            enterView.setText(enter);
        }
        cancelView = this.findViewById(R.id.cancelView);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice.dismiss();
                dialogChoiceListener.cancelReturn();
            }
        });
        if(!cancel.equals("")){
            cancelView.setText(cancel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
