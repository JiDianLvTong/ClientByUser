package com.android.jidian.client.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.PullCashGetCashRecordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaoming
 * date: 2021/11/26 下午3:53
 * company: 兴达智联
 * description:
 */
public class CashWithdrawalRecordAdapter extends RecyclerView.Adapter<CashWithdrawalRecordAdapter.MyViewHolder> {

    private Context mContext;
    private List<PullCashGetCashRecordBean.DataBean> mBean = new ArrayList<>();

    public CashWithdrawalRecordAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setList(List<PullCashGetCashRecordBean.DataBean> beans) {
        mBean = beans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_cash_withdrawal_record, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvItemCashWithdrawalTitle.setText(mBean.get(position).getCash_type());
        holder.tvItemCashWithdrawalNum.setText(mBean.get(position).getCash_amount());
        holder.tvItemCashWithdrawalTime.setText(mBean.get(position).getCash_time());
        holder.tvItemCashWithdrawalStatus.setText(mBean.get(position).getCash_status());
        holder.tvItemCashWithdrawalStatus.setTextColor(Color.parseColor("已提现".equals(mBean.get(position).getCash_status()) ? "#FD4931" : "#555555"));
    }

    @Override
    public int getItemCount() {
        return mBean.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemCashWithdrawalTitle, tvItemCashWithdrawalNum, tvItemCashWithdrawalTime, tvItemCashWithdrawalStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemCashWithdrawalTitle = itemView.findViewById(R.id.tv_item_cash_withdrawal_title);
            tvItemCashWithdrawalNum = itemView.findViewById(R.id.tv_item_cash_withdrawal_num);
            tvItemCashWithdrawalTime = itemView.findViewById(R.id.tv_item_cash_withdrawal_time);
            tvItemCashWithdrawalStatus = itemView.findViewById(R.id.tv_item_cash_withdrawal_status);
        }
    }

}
