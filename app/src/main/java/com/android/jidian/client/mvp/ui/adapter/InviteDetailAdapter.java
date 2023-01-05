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
 * date: 2021/11/27 上午10:11
 * company: 兴达智联
 * description:
 */
public class InviteDetailAdapter extends RecyclerView.Adapter<InviteDetailAdapter.MyViewHolder> {

    private Context mContext;
    private List<PullActivityInviteListsBean.DataBean.ListBean> mList = new ArrayList<>();

    public InviteDetailAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setLists(List<PullActivityInviteListsBean.DataBean.ListBean> lists) {
        mList = lists;
    }

    @NonNull
    @Override
    public InviteDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_intive_detail, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteDetailAdapter.MyViewHolder holder, int position) {
        holder.tvItemInviteDetailIndex.setText(position + 1 + "");
        holder.tvItemInviteDetailPhone.setText(mList.get(position).getPhone());
        holder.tvItemInviteDetailTime.setText(mList.get(position).getReg_time());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemInviteDetailIndex, tvItemInviteDetailPhone, tvItemInviteDetailTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemInviteDetailIndex = itemView.findViewById(R.id.tv_item_invite_detail_index);
            tvItemInviteDetailPhone = itemView.findViewById(R.id.tv_item_invite_detail_phone);
            tvItemInviteDetailTime = itemView.findViewById(R.id.tv_item_invite_detail_time);
        }
    }
}
