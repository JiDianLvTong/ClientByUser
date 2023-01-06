package com.android.jidian.client.mvp.ui.dialog;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.AbstractXdzlDialog;
import com.android.jidian.client.base.ViewHolder;
import com.android.jidian.client.widgets.MyToast;

/**
 * @author : xiaoming
 * date: 2023/1/6 10:52
 * company: 兴达智联
 * description:
 */
public class UserLogOffDialog extends AbstractXdzlDialog {
    private Context mContext;
    private OnClickListener mListener;

    public UserLogOffDialog init(Context context, OnClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.u6_dialog_user_logoff;
    }

    @Override
    public void convertView(ViewHolder holder, AbstractXdzlDialog dialog) {
        holder.getView(R.id.success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    String s = ((EditText) holder.getView(R.id.input_code)).getText().toString().trim();
                    if (!s.isEmpty()) {
                        mListener.OnSuccessClick(s);
                        dismiss();
                    } else {
                        MyToast.showTheToast(mContext, "验证码不能为空");
                    }
                }
            }
        });
        holder.getView(R.id.error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnFailClick();
                    dismiss();
                }
            }
        });
        holder.getView(R.id.get_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnVerifiCodeClick();
                    setCountDown((TextView) holder.getView(R.id.get_code));
                }
            }
        });
    }

    private void setCountDown(TextView textView) {
        CountDownTimer timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (textView != null) {
                    textView.setText("倒计时" + millisUntilFinished / 1000 + "秒");
                    textView.setClickable(false);
                    textView.setBackground(getResources().getDrawable(R.drawable.button_corners_gray_radius_5));
                }
            }

            public void onFinish() {
                if (textView != null) {
                    textView.setText("获取验证码");
                    textView.setClickable(true);
                    textView.setBackground(getResources().getDrawable(R.drawable.circular_orange_d7a64a_5));
                }
            }
        };
        timer.start();
    }

    public interface OnClickListener {
        void OnSuccessClick(String code);

        void OnFailClick();

        void OnVerifiCodeClick();
    }

}
