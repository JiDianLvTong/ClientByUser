package com.android.jidian.client.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.MainDiscount_;
import com.android.jidian.client.R;
import com.android.jidian.client.bean.CouponCashv2Bean;
import com.android.jidian.client.bean.CouponScanBean;
import com.android.jidian.client.mvp.contract.CouponCashv2Contract;
import com.android.jidian.client.mvp.presenter.CouponCashv2Presenter;
import com.timmy.tdialog.TDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CouponCashv2ActivityU6 extends U6BaseActivityByMvp<CouponCashv2Presenter> implements CouponCashv2Contract.View {
    @BindView(R.id.title_layout_imageView1)
    ImageView titleLayoutImageView1;
    @BindView(R.id.title_layout_textView1)
    TextView titleLayoutTextView1;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView6)
    TextView textView6;

    private boolean isFirstIn = true;
    private String code;
    private String alter;
    private int from = -1;
    private String name;
    private String type;
    private String fval;
    private String expire;
    private String code1;
    private String code_info;
    private String rule;
    private String alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        if (intent.hasExtra("name")) {
            from = 0;
            name = intent.getStringExtra("name");
            type = intent.getStringExtra("type");
            fval = intent.getStringExtra("fval");
            expire = intent.getStringExtra("expire");
            code = intent.getStringExtra("code");
            code_info = intent.getStringExtra("code_info");
            rule = intent.getStringExtra("rule");
            alert = intent.getStringExtra("alert");
        } else {
            from = 1;
        }
        setContentView(R.layout.activity_coupon_cashv2);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mPresenter = new CouponCashv2Presenter();
        mPresenter.attachView(this);
        if (from == 1) {
            //        mPresenter.couponCashv2(Integer.parseInt(uid), null, code, "scan");
            mPresenter.couponScan(Integer.parseInt(uid), code);
        } else {
            alter = alert;
            textView.setText(name);
            textView3.setText(code_info);
            textView4.setText(fval);
            textView5.setText(expire);
            textView6.setText(rule);
        }
        titleLayoutTextView1.setText("优惠券信息");
    }

    @Override
    public void onCouponCashv2Success(CouponCashv2Bean couponCashv2Bean) {
        int status = couponCashv2Bean.getStatus();
        showMessage(couponCashv2Bean.getMsg());
        Message message = new Message();
        if (status == 1) {
//            CouponCashv2Bean.DataBean dataBean = couponCashv2Bean.getData();
//            if (isFirstIn) {
//                textView.setText(dataBean.getName());
//                textView3.setText(getResources().getString(R.string.s2020010601, dataBean.getCode()));
//                textView4.setText(getResources().getString(R.string.s2020010602, dataBean.getTime()));
//                code = dataBean.getCode();
//                isFirstIn = false;
//            } else {
//            showMessage(couponCashv2Bean.getMsg());
            message.arg1 = 1;
//            }
        } else {
            message.arg1 = 0;
        }
        if (from == 0) {
            Intent intent = new Intent(activity, MainDiscount_.class);
            activity.startActivity(intent);
        } else {
            MainDiscount_.setCurrentPageHandler.sendMessage(message);
        }
        finish();
    }

    @Override
    public void onCouponScanSuccess(CouponScanBean couponScanBean) {
        int status = couponScanBean.getStatus();
        if (status == 1) {
            CouponScanBean.DataBean dataBean = couponScanBean.getData();
            alter = dataBean.getAlert();
            textView.setText(dataBean.getName());
            textView3.setText(dataBean.getCode_info());
            textView4.setText(dataBean.getFval());
            textView5.setText(dataBean.getExpire());
            textView6.setText(dataBean.getRule());
            code = dataBean.getCode();
        } else {
            showMessage(couponScanBean.getMsg());
            finish();
        }
    }

    @Override
    public void onCouponScanError(Throwable throwable) {
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @OnClick({R.id.title_layout_imageView1, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_layout_imageView1:
                finish();
                break;
            case R.id.button:
                confirmAgain();
                break;
        }
    }

    public void confirmAgain() {
        View dialog_view = View.inflate(this, R.layout.cashv2_confirm, null);
        TextView textView = dialog_view.findViewById(R.id.textView);
        textView.setText(alter);
        new TDialog.Builder(getSupportFragmentManager())
//                .setLayoutRes(R.layout.cashv2_confirm)
                .setDialogView(dialog_view)
                .setScreenWidthAspect(this, 0.7f)
                .addOnClickListener(R.id.textView2, R.id.textView3)
                .setOnViewClickListener((viewHolder, view, tDialog) -> {
                    switch (view.getId()) {
                        case R.id.textView2:
                            tDialog.dismiss();
                            break;
                        case R.id.textView3:
                            mPresenter.couponCashv2(Integer.parseInt(uid), null, code, null);
                            tDialog.dismiss();
                            break;
                    }
                })
                .create()
                .show();
    }

}
