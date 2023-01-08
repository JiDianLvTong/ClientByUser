package com.android.jidian.client.mvp.ui.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.base.U6BaseActivityByMvp;
import com.android.jidian.client.bean.PullCashGetCashRecordBean;
import com.android.jidian.client.mvp.contract.CashWithdrawalRecordContract;
import com.android.jidian.client.mvp.presenter.CashWithdrawalRecordPresenter;
import com.android.jidian.client.mvp.ui.adapter.CashWithdrawalRecordAdapter;

public class CashWithdrawalRecordActivity extends U6BaseActivityByMvp<CashWithdrawalRecordPresenter> implements CashWithdrawalRecordContract.View {

    private RecyclerView rvCashWithdrawalRecord;
    private LinearLayout nonePanel;
    private CashWithdrawalRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash_withdrawal_record);
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
        titleLayoutTextView1.setText("提现记录");
        rvCashWithdrawalRecord = findViewById(R.id.rv_cash_withdrawal_record);
        nonePanel = findViewById(R.id.none_panel);
        mPresenter = new CashWithdrawalRecordPresenter();
        mPresenter.attachView(this);
        mPresenter.requestPullCashGetCashRecord();
        mAdapter = new CashWithdrawalRecordAdapter(this);
        rvCashWithdrawalRecord.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCashWithdrawalRecord.setAdapter(mAdapter);
    }

    @Override
    public void requestPullCashGetCashRecordSuccess(PullCashGetCashRecordBean bean) {
        if (bean.getData() != null) {
            if (bean.getData().size() > 0) {
                rvCashWithdrawalRecord.setVisibility(View.VISIBLE);
                nonePanel.setVisibility(View.GONE);
                mAdapter.setList(bean.getData());
                mAdapter.notifyDataSetChanged();
            } else {
                rvCashWithdrawalRecord.setVisibility(View.GONE);
                nonePanel.setVisibility(View.VISIBLE);
            }
        } else {
            rvCashWithdrawalRecord.setVisibility(View.GONE);
            nonePanel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void requestShowTis(String msg) {
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