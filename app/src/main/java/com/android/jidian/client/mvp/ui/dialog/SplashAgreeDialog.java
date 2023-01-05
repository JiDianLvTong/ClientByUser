package com.android.jidian.client.mvp.ui.dialog;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.AbstractXdzlDialog;
import com.android.jidian.client.base.ViewHolder;
import com.android.jidian.client.widgets.URLSpanNoUnderline;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : PTT
 * date: 2020/11/20 4:45 PM
 * company: 兴达智联
 * description: 默认提示弹框
 */
public class SplashAgreeDialog extends AbstractXdzlDialog {
    private Context mContext;
    public OnDismissListener onDismissListener;
    private String mStrContent = "", mStrBtnLeft = "", mStrBtnRight = "";

    @BindView(R.id.tv_splash_agree_content)
    TextView tvContent;
    @BindView(R.id.tv_splash_agree_content_more)
    TextView tvContentMore;
    @BindView(R.id.btn_splash_agree_left)
    TextView btnLeft;
    @BindView(R.id.btn_splash_agree_right)
    TextView btnRight;

    public SplashAgreeDialog init(Context context, String strContent, String strBtnLeft, String strBtnRight, OnDismissListener listener) {
        this.onDismissListener = listener;
        this.mStrContent = strContent;
        this.mStrBtnLeft = strBtnLeft;
        this.mStrBtnRight = strBtnRight;
        this.mContext = context;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_splash_agree;
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
        String mStrContentMore = mContext.getResources().getString(R.string.str_splash_agree_more);
        SpannableString ss = new SpannableString(mStrContentMore);
        ss.setSpan(new URLSpanNoUnderline(mContext, "《吉电出行隐私权政策》"), mStrContentMore.indexOf("《"), (mStrContentMore.indexOf("》") + 1), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContentMore.setText(ss);
        tvContentMore.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick({R.id.btn_splash_agree_left, R.id.btn_splash_agree_right})
    public void onViewClicked(View v) {
        int id = v.getId();
        if (id == R.id.btn_splash_agree_left) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onCancelDismiss();
            }
        } else if (id == R.id.btn_splash_agree_right) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onSureDismiss();
            }
        }
    }

    public interface OnDismissListener {
        void onSureDismiss();

        void onCancelDismiss();
    }
}