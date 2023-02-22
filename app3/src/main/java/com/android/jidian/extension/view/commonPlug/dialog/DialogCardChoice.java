package com.android.jidian.extension.view.commonPlug.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;


import com.android.jidian.extension.R;

import razerdp.basepopup.BasePopupWindow;

public class DialogCardChoice extends BasePopupWindow {

    public interface DialogCardChoiceListener{
        void cameraReturn();
        void albumReturn();
    }

    private Activity activity;
    private DialogCardChoice dialogCardChoice;
    private LinearLayout l_1;
    private LinearLayout l_2;

    private DialogCardChoiceListener dialogCardChoiceListener;

    public DialogCardChoice(Activity activity , DialogCardChoiceListener dialogCardChoiceListener) {
        super(activity.getApplicationContext());
        this.activity = activity;
        this.dialogCardChoiceListener = dialogCardChoiceListener;
        dialogCardChoice = this;
        setContentView(R.layout.dialog_card_choice);
        init();
    }

    private void init(){

        l_1 = this.findViewById(R.id.l_1);
        l_2 = this.findViewById(R.id.l_2);

        l_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCardChoice.dismiss();
                dialogCardChoiceListener.cameraReturn();
            }
        });

        l_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCardChoice.dismiss();
                dialogCardChoiceListener.albumReturn();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
