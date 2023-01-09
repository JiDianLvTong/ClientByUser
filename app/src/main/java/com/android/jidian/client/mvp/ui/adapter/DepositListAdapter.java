package com.android.jidian.client.mvp.ui.adapter;

import android.graphics.Color;
import android.view.View;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.DepositRefundOrderBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : xiaoming
 * date: 2023/1/9 14:17
 * company: 兴达智联
 * description:
 */
public class DepositListAdapter extends BaseQuickAdapter<DepositRefundOrderBean.DataBean, BaseViewHolder> {

    private OnItemClickListener mListener;

    public DepositListAdapter() {
        super(R.layout.u6_item_deposit);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, DepositRefundOrderBean.DataBean bean) {
        if (bean != null) {
            String goods = "";
            for (int i = 0; i < bean.getGoods().size(); i++) {
                if (i == 0) {
                    goods = bean.getGoods().get(i);
                } else {
                    goods = goods + "\n" + bean.getGoods().get(i);
                }
            }
            holder.setText(R.id.tv_deposit_item_num, "订单号：" + bean.getOrder_num() + "\n支付方式：" + bean.getTyper())
                    .setText(R.id.tv_deposit_item_product, goods)
                    .setText(R.id.tv_deposit_item_status, bean.getApply_status())
                    .setText(R.id.tv_deposit_item_price,"¥" + bean.getRefprice() + "元");

            //0 => '已申请', 1 => '审核通过', 2 => '驳回', -1 无
            if ("0".equals(bean.getApply()) || "1".equals(bean.getApply())) {
                holder.setTextColor(R.id.tv_deposit_item_status, Color.parseColor("#666666"))
                        .setTextColor(R.id.btn_deposit_item, Color.parseColor("#666666"));
                holder.setBackgroundRes(R.id.btn_deposit_item, R.drawable.shape_cecece_12point5);
            } else if ("2".equals(bean.getApply()) || "-1".equals(bean.getApply())) {
                holder.setTextColor(R.id.tv_deposit_item_status, Color.parseColor("#666666"))
                        .setTextColor(R.id.btn_deposit_item, Color.parseColor("#FFFFFF"));
                holder.setBackgroundRes(R.id.btn_deposit_item, R.drawable.u6_shape_d7a64a_corner_5);
            }
            holder.getView(R.id.btn_deposit_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("2".equals(bean.getApply()) || "-1".equals(bean.getApply())) {
                        if (mListener != null) {
                            mListener.onItemClick(bean);
                        }
                    }
                }
            });
            holder.getView(R.id.tv_deposit_item_status).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("2".equals(bean.getApply())) {
                        if (mListener != null) {
                            mListener.onRejectClick(bean.getReject_reason());
                        }
                    }
                }
            });

        }
    }


    public interface OnItemClickListener {
        void onItemClick(DepositRefundOrderBean.DataBean bean);

        void onRejectClick(String msg);
    }

}
