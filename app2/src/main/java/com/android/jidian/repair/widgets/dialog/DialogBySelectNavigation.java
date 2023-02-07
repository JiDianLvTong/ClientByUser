package com.android.jidian.repair.widgets.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.jidian.repair.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author : xiaoming
 * date: 2023/1/12 14:55
 * description:
 */
public class DialogBySelectNavigation extends BaseDialog {

    private Context mContext;
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

    private DialogChoiceListener mListener;

    public DialogBySelectNavigation(Context context, boolean hasGaode, boolean hasBaidu, boolean hasTencent, DialogChoiceListener listener) {
        super(context);
        this.mListener = listener;
        this.mHasGaoDe = hasGaode;
        this.mHasBaidu = hasBaidu;
        this.mHasTencent = hasTencent;
        setContentView(R.layout.dialog_select_navigation_mode);
        init();
    }

    private void init() {
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
            if (mListener != null) {
                mListener.onChoose("GaoDe");
            }
        } else if (id == R.id.btn_open_Baidu_map) {
            dismiss();
            if (mListener != null) {
                mListener.onChoose("Baidu");
            }
        } else if (id == R.id.btn_open_tencent_map) {
            dismiss();
            if (mListener != null) {
                mListener.onChoose("tencent");
            }
        } else if (id == R.id.btn_cancel) {
            dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface DialogChoiceListener {
        void onChoose(String selectType);
    }

}
