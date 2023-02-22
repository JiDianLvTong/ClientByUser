package com.android.jidian.extension.view.commonPlug.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.extension.R;

import razerdp.basepopup.BasePopupWindow;

public class DialogByChoice_2 extends BasePopupWindow {

    public interface DialogChoiceListener{
        void enterReturn();
        void cancelReturn();
    }

    private Activity activity;
    private String title;
    private String content;
    private DialogByChoice_2 dialogChoice;

    private TextView titleView;
    private TextView enterView;
    private TextView cancelView;
    private TextView contentView;

    private DialogChoiceListener dialogChoiceListener;

    public DialogByChoice_2(Activity activity , String title , String content , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.title = title;
        this.content = content;
        this.dialogChoiceListener = dialogChoiceListener;
        dialogChoice = this;
        setContentView(R.layout.dialog_choice_2);
        init();
    }

    private void init(){
        titleView = this.findViewById(R.id.title);
        titleView.setText(title);
        contentView = this.findViewById(R.id.content);
        contentView.setText(content);

        enterView = this.findViewById(R.id.enterView);
        enterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice.dismiss();
                dialogChoiceListener.enterReturn();
            }
        });
        cancelView = this.findViewById(R.id.cancelView);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice.dismiss();
                dialogChoiceListener.cancelReturn();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
