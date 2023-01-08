package com.android.jidian.client.mvp.ui.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.client.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DialogByChoice extends BaseDialog {

    public interface DialogChoiceListener{
        void enterReturn();
        void cancelReturn();
    }

    private String title;

    @BindView(R.id.titleView)
    public TextView titleView;
    @BindView(R.id.enterView)
    public TextView enterView;
    @BindView(R.id.cancelView)
    public TextView cancelView;

    private String enter = "";
    private String cancel = "";

    private DialogChoiceListener dialogChoiceListener;

    public DialogByChoice(Activity activity , String title , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.title = title;
        this.dialogChoiceListener = dialogChoiceListener;
        setContentView(R.layout.u6_pub_dialog_choice);
        init();
    }

    public DialogByChoice(Activity activity , String title , String enter , String cancel , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.title = title;
        this.enter = enter;
        this.cancel = cancel;
        this.dialogChoiceListener = dialogChoiceListener;
        setContentView(R.layout.u6_pub_dialog_choice);
        init();
    }

    private void init(){
        titleView.setText(title);
        if(!enter.equals("")){
            enterView.setText(enter);
        }
        if(!cancel.equals("")){
            cancelView.setText(cancel);
        }
    }

    @OnClick(R.id.enterView)
    void onClickEnterView(){
        this.dismiss();
        if(dialogChoiceListener!=null){
            dialogChoiceListener.enterReturn();
        }

    }

    @OnClick(R.id.cancelView)
    void onClickCancelView(){
        this.dismiss();
        if(dialogChoiceListener!=null) {
            dialogChoiceListener.cancelReturn();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
