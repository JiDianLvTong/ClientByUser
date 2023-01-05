package com.android.jidian.client.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.AuthTask;
import com.android.jidian.client.R;
import com.android.jidian.client.base.BaseActivity2;
import com.android.jidian.client.bean.PullCashGetUserAccountInfoBean;
import com.android.jidian.client.mvp.contract.PullCashAddCashContract;
import com.android.jidian.client.mvp.presenter.PullCashAddCashPresenter;
import com.android.jidian.client.pub.zhifubao.SignUtils;
import com.android.jidian.client.util.CashierInputFilter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

public class CashWithdrawalActivity extends BaseActivity2<PullCashAddCashPresenter> implements PullCashAddCashContract.View {

    private EditText etCashWithdrawalCashNum;
    private TextView tvInviteDetailAccountTip, tvCashWithdrawalAlipay;
    private String mAppid, mPartner, mAppPrivateKey;
    private boolean mIsCash = false, mIsAliPay = false, mIsCashFinish = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash_withdrawal);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        tvCashWithdrawalAlipay = findViewById(R.id.tv_cash_withdrawal_alipay);
        tvCashWithdrawalAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsAliPay) {
                    if (!TextUtils.isEmpty(mAppid) && !TextUtils.isEmpty(mPartner) && !TextUtils.isEmpty(mAppPrivateKey)) {
                        bindAlipay();
                    } else {
                        showMessage("数据异常");
                    }
                }
            }
        });
        ImageView titleLayoutImageView1 = findViewById(R.id.title_layout_imageView1);
        titleLayoutImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView titleLayoutTextView1 = findViewById(R.id.title_layout_textView1);
        titleLayoutTextView1.setText("提现");
        String cash_amount = getIntent().getStringExtra("cash_amount");
        etCashWithdrawalCashNum = findViewById(R.id.et_cash_withdrawal_cash_num);
//        etCashWithdrawalCashNum.setHint("可提现金额¥" + cash_amount);
        tvInviteDetailAccountTip = findViewById(R.id.tv_invite_detail_account_tip);
        tvInviteDetailAccountTip.setText("可提现金额¥" + cash_amount);
        InputFilter[] filters = {new CashierInputFilter()};
        etCashWithdrawalCashNum.setFilters(filters);
        TextView tvCashWithdrawalCashAll = findViewById(R.id.tv_cash_withdrawal_cash_all);
        tvCashWithdrawalCashAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("0".equals(cash_amount)) {
                    showMessage("暂无可提现金额");
                    return;
                }
                etCashWithdrawalCashNum.setText(cash_amount);
            }
        });
        TextView tvCashWithdrawalCashWithdrawal = findViewById(R.id.tv_cash_withdrawal_cash_withdrawal);
        tvCashWithdrawalCashWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsCash) {
                    return;
                }
                String cashAmount = etCashWithdrawalCashNum.getText().toString().trim();
                if (TextUtils.isEmpty(cashAmount)) {
                    showMessage("请输入提现金额");
                    return;
                }
                BigDecimal intCashAmount = new BigDecimal(cashAmount);
                BigDecimal bigDecimal = new BigDecimal(cash_amount);
                if (intCashAmount.intValue() > bigDecimal.intValue()) {
                    showMessage("输入金额超过最大金额");
                    return;
                }
                if (mIsCashFinish) {
                    mIsCashFinish = false;
                    mPresenter.requestPullCashAddCash(cashAmount);
                }
            }
        });
        mPresenter = new PullCashAddCashPresenter();
        mPresenter.attachView(this);
        mPresenter.requestPullCashGetUserAccountInfo();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String authCode = msg.getData().getString("authCode");
                if (mPresenter != null) {
                    mPresenter.requestPullCashSetUserAccountInfo(authCode);
                }
            }
        };
    }

    private void bindAlipay() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String s = "apiname=com.alipay.account.auth&method=alipay.open.auth.sdk.code.get&app_id=" + mAppid + "&app_name=mc&biz_type=openservice&pid=" + mPartner + "&product_id=APP_FAST_LOGIN&scope=kuaijie&target_id=" + uid + "&auth_type=AUTHACCOUNT&sign_type=RSA";
                String sign = SignUtils.sign(s, mAppPrivateKey);
                try {
                    //仅需对sign 做URL编码
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    System.out.println(e.getLocalizedMessage());
                    Log.d("xiaoming1206", "getLocalizedMessage: " + e.getLocalizedMessage());
                }
                AuthTask authTask = new AuthTask(activity);
                String finalInfo = s + "&sign=" + sign;
                Map<String, String> map = authTask.authV2(finalInfo, true);
                Log.d("xiaoming1206", "run: " + map.toString());
                if ("9000".equals(map.get("resultStatus"))) {
                    //success=true&result_code=200&app_id=2017112400137717&auth_code=a072c55ad60a4f0595c571838066UE57&scope=kuaijie&alipay_open_id=20880061785474402853245262517857&user_id=2088732895397575&target_id=126798
                    String resultCode = "", authCode = "";
                    String[] strArray = map.get("result").split("&");
                    for (String value : strArray) {
                        String substring = value.substring(value.indexOf("=") + 1, value.length());
                        if (value.contains("result_code")) {
                            resultCode = substring;
                        }
                        if (value.contains("auth_code")) {
                            authCode = substring;
                        }
                    }
                    if (TextUtils.isEmpty(resultCode) || TextUtils.isEmpty(authCode)) {
                        showMessage("数据异常1");
                        return;
                    }
                    if ("200".equals(resultCode)) {
                        if (mHandler != null) {
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("authCode", authCode);
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        }
                    } else if ("1005".equals(resultCode)) {
                        showMessage("账户已冻结");
                    } else if ("202".equals(resultCode)) {
                        showMessage("系统异常，请稍后再试");
                    }
                } else if ("6002".equals(map.get("resultStatus"))) {
                    showMessage("网络连接出错");
                } else if ("4000".equals(map.get("resultStatus"))) {
                    showMessage("系统异常");
                } else if ("6001".equals(map.get("resultStatus"))) {
                    showMessage("取消授权");
                }
            }
        };
        // 必须异步调用
        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void requestPullCashAddCashSuccess(String msg) {
        showMessage(msg);
        mIsCashFinish = true;
        finish();
        startActivity(new Intent(CashWithdrawalActivity.this, CashWithdrawalRecordActivity.class));
    }

    @Override
    public void requestPullCashAddCashFail(String msg) {
        mIsCashFinish = true;
        showMessage(msg);
    }

    @Override
    public void requestPullCashGetUserAccountInfoSuccess(PullCashGetUserAccountInfoBean.DataBean bean) {
        mAppid = bean.getAppid();
        mPartner = bean.getPartner();
        mAppPrivateKey = bean.getApp_private_key();
        if ("1".equals(bean.getIsAliPay())) {
            mIsAliPay = true;
            tvCashWithdrawalAlipay.setText("已绑定");
        } else {
            mIsAliPay = false;
            tvCashWithdrawalAlipay.setText("立即绑定");
        }
        mIsCash = "1".equals(bean.getIsCash());
    }

    @Override
    public void requestPullCashSetUserAccountInfoSuccess(String msg) {
//        showMessage(msg);
//        Toast.makeText(CashWithdrawalActivity.this, msg, Toast.LENGTH_SHORT).show();
        Log.d("xiaoming1208", "requestPullCashSetUserAccountInfoSuccess: ");
//        mIsAliPay = true;
//        tvCashWithdrawalAlipay.setText("已绑定");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        mPresenter.requestPullCashGetUserAccountInfo();
//            }
//        }, 2000);
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