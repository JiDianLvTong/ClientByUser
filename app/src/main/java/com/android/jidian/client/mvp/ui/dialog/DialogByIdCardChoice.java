package com.android.jidian.client.mvp.ui.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;


import com.android.jidian.client.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DialogByIdCardChoice extends BaseDialog {

    public interface DialogCardChoiceListener{
        void cameraReturn();
        void albumReturn();
    }

    @BindView(R.id.l_1)
    public LinearLayout l_1;
    @BindView(R.id.l_2)
    public LinearLayout l_2;

    private DialogCardChoiceListener dialogCardChoiceListener;

    public DialogByIdCardChoice(Activity activity , DialogCardChoiceListener dialogCardChoiceListener) {
        super(activity.getApplicationContext());
        this.dialogCardChoiceListener = dialogCardChoiceListener;
        setContentView(R.layout.u6_pub_dialog_card_choice);
    }

    @OnClick(R.id.l_1)
    void onClickL_1(){
        this.dismiss();
        dialogCardChoiceListener.cameraReturn();
    }

    @OnClick(R.id.l_2)
    void onClickL_2(){
        this.dismiss();
        dialogCardChoiceListener.albumReturn();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
