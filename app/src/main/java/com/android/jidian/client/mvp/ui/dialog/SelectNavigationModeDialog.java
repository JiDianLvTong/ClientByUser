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
 * date: 2020/8/10 18:25
 * company: 兴达智联
 * description: 选择导航方式
 */
public class SelectNavigationModeDialog extends AbstractXdzlDialog {
    public OnDismissListener onDismissListener;
    private boolean mHasGaoDe = false, mHasBaidu = false, mHasTencent = false;

    @BindView(R.id.btn_open_GaoDe_map)
    TextView btnOpenGaoDeMap;
    @BindView(R.id.line_open_GaoDe_map)
    TextView lineOpenGaoDeMap;
    @BindView(R.id.btn_open_Baidu_map)
    TextView btnOpenBaiduMap;
    @BindView(R.id.line_open_Baidu_map)
    TextView lineOpenBaiduMap;
    @BindView(R.id.btn_open_tencent_map)
    TextView btnOpenTencentMap;
    @BindView(R.id.line_open_tencent_map)
    TextView lineOpenTencentMap;

    public SelectNavigationModeDialog init(boolean hasGaoDe, boolean hasBaidu, boolean hasTencent, OnDismissListener listener) {
        this.onDismissListener = listener;
        this.mHasGaoDe = hasGaoDe;
        this.mHasBaidu = hasBaidu;
        this.mHasTencent = hasTencent;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_select_navigation_mode;
    }

    @Override
    public void convertView(ViewHolder holder, AbstractXdzlDialog dialog) {
        btnOpenGaoDeMap.setVisibility(mHasGaoDe ? View.VISIBLE : View.GONE);
        lineOpenGaoDeMap.setVisibility(mHasGaoDe ? View.VISIBLE : View.GONE);

        btnOpenBaiduMap.setVisibility(mHasBaidu ? View.VISIBLE : View.GONE);
        lineOpenBaiduMap.setVisibility(mHasBaidu ? View.VISIBLE : View.GONE);

        btnOpenTencentMap.setVisibility(mHasTencent ? View.VISIBLE : View.GONE);
        lineOpenTencentMap.setVisibility(mHasTencent ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.btn_open_GaoDe_map, R.id.btn_open_Baidu_map, R.id.btn_open_tencent_map, R.id.btn_cancel})
    public void onViewClicked(View v) {
        int id = v.getId();
        if (id == R.id.btn_open_GaoDe_map) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onDismiss("GaoDe");
            }
        } else if (id == R.id.btn_open_Baidu_map) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onDismiss("Baidu");
            }
        } else if (id == R.id.btn_open_tencent_map) {
            dismiss();
            if (onDismissListener != null) {
                onDismissListener.onDismiss("tencent");
            }
        } else if (id == R.id.btn_cancel) {
            dismiss();
        }
    }

    public interface OnDismissListener {
        void onDismiss(String selectMap);
    }
}