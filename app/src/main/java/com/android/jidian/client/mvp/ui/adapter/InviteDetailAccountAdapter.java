package com.android.jidian.client.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.PullActivityInviteListsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : xiaoming
 * date: 2021/11/30 下午1:57
 * company: 兴达智联
 * description:
 */
public class InviteDetailAccountAdapter extends RecyclerView.Adapter<InviteDetailAccountAdapter.MyViewHolder> {
    private Context mContext;
    private String mType;
    private List<PullActivityInviteListsBean.DataBean.ListBean> mList = new ArrayList<>();

    public InviteDetailAccountAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setLists(List<PullActivityInviteListsBean.DataBean.ListBean> lists) {
        mList = lists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_intive_detail_acount, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvItemInviteDetailAccountPhone.setText(mList.get(position).getPhone());
//        holder.tvItemInviteDetailAccountLevel.setText("1".equals(mList.get(position).getLevel()) ? "一级" : "二级");
//        holder.tvItemInviteDetailAccountLevel.setBackgroundResource("1".equals(mList.get(position).getLevel()) ? R.drawable.gradient_fd4931_corner_21 : R.drawable.gradient_fd9034_corner_21);
        holder.tvItemInviteDetailAccountTime.setText(mList.get(position).getPay_time());
        holder.tvItemInviteDetailAccountOrderType.setText(mList.get(position).getOrder_type_name());
        holder.tvItemInviteDetailAccountOrderAccount.setText("¥" + mList.get(position).getOrder_fee());
        holder.tvItemInviteDetailAccountOrderPer.setText(mList.get(position).getRate() +"%");
        holder.tvItemInviteDetailAccountOrderCash.setText("¥" + mList.get(position).getProfit());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemInviteDetailAccountPhone, tvItemInviteDetailAccountTime,
                tvItemInviteDetailAccountOrderType, tvItemInviteDetailAccountOrderAccount, tvItemInviteDetailAccountOrderPer, tvItemInviteDetailAccountOrderCash;//tvItemInviteDetailAccountLevel

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemInviteDetailAccountPhone = itemView.findViewById(R.id.tv_item_invite_detail_account_phone);
//            tvItemInviteDetailAccountLevel = itemView.findViewById(R.id.tv_item_invite_detail_account_level);
            tvItemInviteDetailAccountTime = itemView.findViewById(R.id.tv_item_invite_detail_account_time);
            tvItemInviteDetailAccountOrderType = itemView.findViewById(R.id.tv_item_invite_detail_account_order_type);
            tvItemInviteDetailAccountOrderAccount = itemView.findViewById(R.id.tv_item_invite_detail_account_order_account);
            tvItemInviteDetailAccountOrderPer = itemView.findViewById(R.id.tv_item_invite_detail_account_order_per);
            tvItemInviteDetailAccountOrderCash = itemView.findViewById(R.id.tv_item_invite_detail_account_order_cash);
        }
    }
}
