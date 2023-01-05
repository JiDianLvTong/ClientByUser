package com.android.jidian.client.mvp.ui.dialog;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
public class CommonTipDialog extends AbstractXdzlDialog {
    public OnDismissListener onDismissListener;
    private String mStrTitle = "", mStrContent = "", mStrBtnLeft = "", mStrBtnRight = "", mSelectStr = "";
    private int mSelectColor;

    @BindView(R.id.tv_common_tip_title)
    TextView tvTitle;
    @BindView(R.id.tv_common_tip_content)
    TextView tvContent;
    @BindView(R.id.btn_common_tip_left)
    TextView btnLeft;
    @BindView(R.id.btn_common_tip_right)
    TextView btnRight;

    public CommonTipDialog init(String strContent, String strBtnRight, OnDismissListener listener) {
        this.onDismissListener = listener;
        this.mStrContent = strContent;
        this.mStrBtnRight = strBtnRight;
        return this;
    }

    public CommonTipDialog init(String strContent, String strBtnRight, String selectStr, int selectColor, OnDismissListener listener) {
        this.onDismissListener = listener;
        this.mStrContent = strContent;
        this.mStrBtnRight = strBtnRight;
        this.mSelectStr = selectStr;
        this.mSelectColor = selectColor;
        return this;
    }

    public CommonTipDialog init(String strContent, String strBtnLeft, String strBtnRight, OnDismissListener listener) {
        this.onDismissListener = listener;
        this.mStrContent = strContent;
        this.mStrBtnLeft = strBtnLeft;
        this.mStrBtnRight = strBtnRight;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_common_tip;
    }

    @Override
    public void convertView(ViewHolder holder, AbstractXdzlDialog dialog) {
        if (!TextUtils.isEmpty(mStrTitle)) {
            tvTitle.setText(mStrTitle);
        }
        if (!TextUtils.isEmpty(mStrBtnLeft)) {
            btnLeft.setText(mStrBtnLeft);
        }
        if (!TextUtils.isEmpty(mStrBtnRight)) {
            btnRight.setText(mStrBtnRight);
        }

        tvContent.setText(mStrContent);
        if (!TextUtils.isEmpty(mSelectStr)) {
            SpannableString spannableString = new SpannableString(tvContent.getText().toString());
            ForegroundColorSpan span = new ForegroundColorSpan(mSelectColor);
            spannableString.setSpan(span, mStrContent.indexOf(mSelectStr), mStrContent.indexOf(mSelectStr) + mSelectStr.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tvContent.setText(spannableString);
        }

        if ("我已了解".equals(mStrBtnRight)) {
            btnLeft.setVisibility(View.GONE);
            tvTitle.setText("未通过审核原因");
        }
    }

    @OnClick({R.id.btn_common_tip_left, R.id.btn_common_tip_right})
    public void onViewClicked(View v) {
        int id = v.getId();
        if (id == R.id.btn_common_tip_left) {
            dismiss();
        } else if (id == R.id.btn_common_tip_right) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onSureDismiss();
            }
        }
    }

    public interface OnDismissListener {
        void onSureDismiss();
    }
}