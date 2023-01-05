package com.android.jidian.client.mvp.ui.dialog;

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
 * description: 扫一扫绑定（车架号/GPS）
 */
public class ScanCodeBindingDialog extends AbstractXdzlDialog {
    public OnDismissListener onDismissListener;
    private String mStrTitle = "", mStrContent = "", mStrBtnRight = "";

    @BindView(R.id.tv_scan_code_binding_title)
    TextView tvTitle;
    @BindView(R.id.tv_scan_code_binding_content)
    TextView tvContent;
    @BindView(R.id.btn_scan_code_binding_left)
    TextView btnLeft;
    @BindView(R.id.btn_scan_code_binding_right)
    TextView btnRight;

    public ScanCodeBindingDialog init(String strTitle, String strContent, String strBtnRight, OnDismissListener listener) {
        this.onDismissListener = listener;
        this.mStrTitle = strTitle;
        this.mStrContent = strContent;
        this.mStrBtnRight = strBtnRight;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_scan_code_binding;
    }

    @Override
    public void convertView(ViewHolder holder, AbstractXdzlDialog dialog) {
        tvTitle.setText(mStrTitle);
        tvContent.setText(mStrContent);
        btnRight.setText(mStrBtnRight);
    }

    @OnClick({R.id.btn_scan_code_binding_left, R.id.btn_scan_code_binding_right})
    public void onViewClicked(View v) {
        int id = v.getId();
        if (id == R.id.btn_scan_code_binding_left) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onEndBindingDismiss();
            }
        } else if (id == R.id.btn_scan_code_binding_right) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onSureDismiss();
            }
        }
    }

    public interface OnDismissListener {
        void onSureDismiss();

        void onEndBindingDismiss();
    }
}
