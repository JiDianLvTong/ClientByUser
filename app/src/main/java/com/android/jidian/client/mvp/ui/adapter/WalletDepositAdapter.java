package com.android.jidian.client.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.DepositRefundOrderBean;

import java.util.ArrayList;
import java.util.List;

public class WalletDepositAdapter extends RecyclerView.Adapter<WalletDepositAdapter.MyViewHolder> {
    private List<DepositRefundOrderBean.DataBean> mList = new ArrayList<>();
    private Context mContext;

    public WalletDepositAdapter(Context context) {
        this.mContext = context;
    }

    public List<DepositRefundOrderBean.DataBean> setList() {
        return mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_wallet_deposit, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder helper, final int position) {
        if (mList.size() >= position) {
            DepositRefundOrderBean.DataBean bean = mList.get(position);
            if (bean != null) {
                helper.tvNum.setText("订单号:" + bean.getOrder_num() + "\n支付方式:" + bean.getTyper());
                String goods = "";
                for (int i = 0; i < bean.getGoods().size(); i++) {
                    if (i == 0) {
                        goods = bean.getGoods().get(i);
                    } else {
                        goods = goods + "\n" + bean.getGoods().get(i);
                    }
                }
                helper.tvProduct.setText(goods);
                helper.tvStatus.setText(bean.getApply_status());
                //0 => '已申请', 1 => '审核通过', 2 => '驳回', -1 无
                if ("0".equals(bean.getApply()) || "1".equals(bean.getApply())) {
                    helper.tvStatus.setTextColor(Color.parseColor("#666666"));
                    helper.btnItem.setTextColor(Color.parseColor("#666666"));
                    helper.btnItem.setBackgroundResource(R.drawable.shape_cecece_12point5);
                } else if ("2".equals(bean.getApply()) || "-1".equals(bean.getApply())) {
                    helper.tvStatus.setTextColor(Color.parseColor("#666666"));
                    helper.btnItem.setTextColor(Color.parseColor("#FFFFFF"));
                    helper.btnItem.setBackgroundResource(R.drawable.shape_2ba245_12point5);
                }
                helper.tvPrice.setText("¥" + bean.getRefprice() + "元");

                helper.btnItem.setOnClickListener(v -> {
                    if ("2".equals(bean.getApply()) || "-1".equals(bean.getApply())) {
                        if (listener != null) {
                            listener.onItemClick(bean);
                        }
                    }
                });

                helper.tvStatus.setOnClickListener(v -> {
                    if ("2".equals(bean.getApply())) {
                        if (listener != null) {
                            listener.onRejectClick(bean.getReject_reason());
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvNum, tvProduct, tvStatus, tvPrice, btnItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNum = itemView.findViewById(R.id.tv_wallet_deposit_item_num);
            tvProduct = itemView.findViewById(R.id.tv_wallet_deposit_item_product);
            tvStatus = itemView.findViewById(R.id.tv_wallet_deposit_item_status);
            tvPrice = itemView.findViewById(R.id.tv_wallet_deposit_item_price);
            btnItem = itemView.findViewById(R.id.btn_wallet_deposit_item);
        }
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DepositRefundOrderBean.DataBean bean);

        void onRejectClick(String msg);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}