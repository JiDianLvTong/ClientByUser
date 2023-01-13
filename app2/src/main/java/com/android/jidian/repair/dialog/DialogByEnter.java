package com.android.jidian.repair.dialog;

import android.app.Activity;
import android.widget.TextView;

import com.android.jidian.repair.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DialogByEnter extends BaseDialog {

    public interface DialogChoiceListener{
        void enterReturn();
    }

    private String title;

    @BindView(R.id.titleView)
    public TextView titleView;
    @BindView(R.id.enterView)
    public TextView enterView;

    private DialogChoiceListener dialogChoiceListener;

    public DialogByEnter(Activity activity , String title , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.title = title;
        this.dialogChoiceListener = dialogChoiceListener;
        setContentView(R.layout.pub_dialog_enter);
        init();
    }

    public DialogByEnter(Activity activity , String title) {
        super(activity.getApplicationContext());
        this.title = title;
        setContentView(R.layout.pub_dialog_enter);
        init();
    }

    private void init(){
        titleView.setText(title);
    }

    @OnClick(R.id.enterView)
    void onClickEnterView(){
        this.dismiss();
        if(dialogChoiceListener!=null){
            dialogChoiceListener.enterReturn();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
