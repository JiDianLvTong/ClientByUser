package com.android.jidian.client.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.MainAuthentication_;
import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseActivity2;
import com.android.jidian.client.bean.PullCashGetUserProfitBean;
import com.android.jidian.client.mvp.contract.PullCashGetUserProfitContract;
import com.android.jidian.client.mvp.presenter.PullCashGetUserProfitPresenter;
import com.android.jidian.client.mvp.ui.dialog.CommonTipDialog;

public class InviteWallteActivity extends BaseActivity2<PullCashGetUserProfitPresenter> implements PullCashGetUserProfitContract.View {

    private TextView tvInviteWallteCashNum, tvInviteWallteCashTotal, tvInviteWallteCashRule;//tvInviteWallteLevelOnetvInviteWallteLevelTwo
    private String mCashAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_invite_wallte);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        ImageView titleLayoutImageView1 = findViewById(R.id.title_layout_imageView1);
        titleLayoutImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView titleLayoutTextView1 = findViewById(R.id.title_layout_textView1);
        titleLayoutTextView1.setText("邀请钱包");
        tvInviteWallteCashTotal = findViewById(R.id.tv_invite_wallte_cash_total);
        tvInviteWallteCashRule = findViewById(R.id.tv_invite_wallte_cash_rule);
//        tvInviteWallteLevelOne = findViewById(R.id.tv_invite_wallte_level_one);
//        tvInviteWallteLevelTwo = findViewById(R.id.tv_invite_wallte_level_two);
        tvInviteWallteCashNum = findViewById(R.id.tv_invite_wallte_cash_num);
        AssetManager assetManager = InviteWallteActivity.this.getAssets();
        Typeface typeface = Typeface.createFromAsset(assetManager, "fonts/DIN-Bold.otf");
        tvInviteWallteCashNum.setTypeface(typeface);
//        tvInviteWallteLevelOne.setTypeface(typeface);
//        tvInviteWallteLevelTwo.setTypeface(typeface);

        TextView tvInviteWallteWithdrawal = findViewById(R.id.tv_invite_wallte_withdrawal);
        tvInviteWallteWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mCashAmount) || "0".equals(mCashAmount)) {
                    showMessage("暂无提现金额");
                    return;
                }
                //点击立即提现
                sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
                String auth = sharedPreferences.getString("auth", "");//3=审核中，2=已驳回，1=是，0=否
                if (!"1".equals(auth)) {
                    new CommonTipDialog().init("您的账号没有实名认证，是否跳转到实名认证页", "确认", () -> {
                        startActivity(new Intent(InviteWallteActivity.this, MainAuthentication_.class));
                    }).setPosition(Gravity.CENTER).setWidth(1).setOutCancel(false).show(getSupportFragmentManager());
                    return;
                }
                Intent intent = new Intent(InviteWallteActivity.this, CashWithdrawalActivity.class);
                intent.putExtra("cash_amount", TextUtils.isEmpty(mCashAmount) ? "0" : mCashAmount);
                startActivity(intent);
            }
        });
        TextView tvInviteWallteCashRecord = findViewById(R.id.tv_invite_wallte_cash_record);
        tvInviteWallteCashRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击提现记录
                startActivity(new Intent(InviteWallteActivity.this, CashWithdrawalRecordActivity.class));
            }
        });

        mPresenter = new PullCashGetUserProfitPresenter();
        mPresenter.attachView(this);
//        mPresenter.requestPullCashGetUserProfit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.requestPullCashGetUserProfit();
        }
    }

    @Override
    public void requestPullCashGetUserProfitSuccess(PullCashGetUserProfitBean.DataBean bean) {
        mCashAmount = bean.getCash();
        tvInviteWallteCashNum.setText("¥" + bean.getCash());
        tvInviteWallteCashTotal.setText("总收益：¥" + bean.getTotal_profit());
//        tvInviteWallteLevelOne.setText("¥" + bean.getOne_profit());
//        tvInviteWallteLevelTwo.setText("¥" + bean.getTwo_profit());
        StringBuilder stringBuilder = new StringBuilder();
        if (bean.getCashRule().size() > 0) {
            for (int i = 0; i < bean.getCashRule().size(); i++) {
                stringBuilder.append(bean.getCashRule().get(i)).append("\n");
            }
        }
        tvInviteWallteCashRule.setText(stringBuilder);
    }

    @Override
    public void requestShowTips(String msg) {
        showMessage(msg);
    }

    @Override
    public void showProgress() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        showMessage("无网络链接，请检查您的网络设置！");
    }
}