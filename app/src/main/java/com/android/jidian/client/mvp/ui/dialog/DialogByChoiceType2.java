package com.android.jidian.client.mvp.ui.dialog;

import android.app.Activity;
import android.widget.TextView;

import com.android.jidian.client.R;

import butterknife.BindView;
import butterknife.OnClick;

public class DialogByChoiceType2 extends BaseDialog {

    public interface DialogChoiceListener{
        void enterReturn();
        void cancelReturn();
    }

    private String title;
    private String title2;

    @BindView(R.id.titleView)
    public TextView titleView;
    @BindView(R.id.titleView2)
    public TextView titleView2;
    @BindView(R.id.enterView)
    public TextView enterView;
    @BindView(R.id.cancelView)
    public TextView cancelView;

    private String enter = "";
    private String cancel = "";

    private DialogChoiceListener dialogChoiceListener;

    public DialogByChoiceType2(Activity activity , String title, String title2 , DialogChoiceListener dialogChoiceListener) {
        super(activity.getApplicationContext());
        this.title = title;
        this.title2 = title2;
        this.dialogChoiceListener = dialogChoiceListener;
        setContentView(R.layout.u6_pub_dialog_choice_type_2);
        init();
    }

    private void init(){
        titleView.setText(title);
        titleView2.setText(title2);
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
