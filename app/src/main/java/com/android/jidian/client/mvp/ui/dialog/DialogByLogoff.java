package com.android.jidian.client.mvp.ui.dialog;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.widgets.MyToast;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : xiaoming
 * date: 2023/1/6 10:52
 * company: 兴达智联
 * description:
 */
public class DialogByLogoff extends BaseDialog {

    public interface DialogChoiceListener{
        void enterReturn(String code);
        void cancelReturn();
        void verificationCodeClick();
    }

    private Activity activity;
    private DialogChoiceListener mListener;

    @BindView(R.id.getCodeView)
    public TextView getCodeView;
    @BindView(R.id.enterView)
    public TextView enterView;
    @BindView(R.id.cancelView)
    public TextView cancelView;
    @BindView(R.id.inputView)
    public EditText inputView;


    public DialogByLogoff(Activity activity , DialogChoiceListener listener) {
        super(activity);
        this.activity = activity;
        this.mListener = listener;
        setContentView(R.layout.u6_pub_dialog_logoff);
    }

    @OnClick(R.id.enterView)
    void onClickEnterView(){
        this.dismiss();
        String s = inputView.getText().toString().trim();
        if (!s.isEmpty()) {
            mListener.enterReturn(s);
            dismiss();
        } else {
            MyToast.showTheToast(activity, "验证码不能为空");
        }

    }

    @OnClick(R.id.cancelView)
    void onClickCancelView(){
        this.dismiss();
        if(mListener!=null) {
            mListener.cancelReturn();
            dismiss();
        }
    }

    @OnClick(R.id.getCodeView)
    void onClickGetCodeView(){
        mListener.verificationCodeClick();
        setCountDown();
    }

    private void setCountDown() {
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                getCodeView.setText("倒计时" + millisUntilFinished / 1000 + "秒");
                getCodeView.setClickable(false);
                getCodeView.setBackground(activity.getResources().getDrawable(R.drawable.u6_shape_999999_corner_5));
            }

            public void onFinish() {
                getCodeView.setText("获取验证码");
                getCodeView.setClickable(true);
                getCodeView.setBackground(activity.getResources().getDrawable(R.drawable.u6_shape_d7a64a_corner_5));
            }
        };
        timer.start();
    }

}
