package com.android.jidian.client.mvp.ui.dialog;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.AbstractXdzlDialog;
import com.android.jidian.client.base.ViewHolder;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : PTT
 * date: 2020/11/20 4:45 PM
 * company: 兴达智联
 * description: 默认提示弹框
 */
public class SplashAgainAgreeDialog extends AbstractXdzlDialog {
    public OnDismissListener onDismissListener;
    private String mStrContent = "", mStrBtnLeft = "", mStrBtnRight = "";

    @BindView(R.id.tv_splash_agree_content)
    TextView tvContent;
    @BindView(R.id.btn_splash_again_agree_left)
    TextView btnLeft;
    @BindView(R.id.btn_splash_again_agree_right)
    TextView btnRight;

    public SplashAgainAgreeDialog init(String strContent, String strBtnLeft, String strBtnRight, OnDismissListener listener) {
        this.onDismissListener = listener;
        this.mStrContent = strContent;
        this.mStrBtnLeft = strBtnLeft;
        this.mStrBtnRight = strBtnRight;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_splash_again_agree;
    }

    @Override
    public void convertView(ViewHolder holder, AbstractXdzlDialog dialog) {
        if (!TextUtils.isEmpty(mStrBtnLeft)) {
            btnLeft.setText(mStrBtnLeft);
        }
        if (!TextUtils.isEmpty(mStrBtnRight)) {
            btnRight.setText(mStrBtnRight);
        }
        tvContent.setText(mStrContent);
    }

    @OnClick({R.id.btn_splash_again_agree_left, R.id.btn_splash_again_agree_right})
    public void onViewClicked(View v) {
        int id = v.getId();
        if (id == R.id.btn_splash_again_agree_left) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onSureDismiss();
            }
        } else if (id == R.id.btn_splash_again_agree_right) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onCancelDismiss();
            }
        }
    }

    public interface OnDismissListener {
        void onSureDismiss();

        void onCancelDismiss();
    }
}