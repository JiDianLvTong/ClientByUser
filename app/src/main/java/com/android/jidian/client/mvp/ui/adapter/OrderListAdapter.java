package com.android.jidian.client.mvp.ui.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.UserUOrderBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2023/1/6 14:10
 * company: 兴达智联
 * description:
 */
public class OrderListAdapter extends BaseQuickAdapter<UserUOrderBean.DataBean.ListsBean, BaseViewHolder> {

    private OnClickItemViewListener mListener;

    public OrderListAdapter() {
        super(R.layout.u6_order_list_item);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserUOrderBean.DataBean.ListsBean listsBean) {
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnClickItem(listsBean);
                }
            }
        });
        LinearLayout l_1 = baseViewHolder.getView(R.id.order_list_item_detail);
        l_1.removeAllViews();
        baseViewHolder.setText(R.id.order_list_item_title,"订单名称:" + listsBean.getOrder_title())
                .setText(R.id.order_list_item_explain,"订单编号:" + listsBean.getOrder_num()
                        + "\n" + "订单时间:" + listsBean.getOrder_time()
                        + "\n" + "支付方式:" + listsBean.getTyper())
                .setText(R.id.t_6,"合计:" + listsBean.getOrder_fee() + "元");

        String select_pack_month = listsBean.getSelect_pack_month();
        if (!TextUtils.isEmpty(select_pack_month)) {
            baseViewHolder.setText(R.id.tv_order_item_cycle, "无".equals(select_pack_month) ? "套餐周期：无" : ("套餐周期：X" + select_pack_month));
        }
        if (!TextUtils.isEmpty(listsBean.getRefund_info()) && !TextUtils.isEmpty(listsBean.getRefund_color())) {
            baseViewHolder.setText(R.id.tv_order_item_refund_info, listsBean.getRefund_info());
            ((TextView) baseViewHolder.getView(R.id.tv_order_item_refund_info)).setTextColor(Color.parseColor(listsBean.getRefund_color()));
        }
        TextView order_list_item_status = baseViewHolder.getView(R.id.order_list_item_status);
        if ("100".equals(listsBean.getType())) {
            String auth = listsBean.getAuth();
            if ("2".equals(auth)) {
                order_list_item_status.setText("未审核");
                order_list_item_status.setTextColor(Color.parseColor("#FF8C00"));
                order_list_item_status.setBackgroundResource(R.drawable.line_ff8b00_corner_10);
            } else {
                order_list_item_status.setText("已支付");
                order_list_item_status.setTextColor(Color.parseColor("#00C86C"));
                order_list_item_status.setBackgroundResource(R.drawable.line_00c76b_corner_10);
            }
        } else {
            order_list_item_status.setText(listsBean.getStatus_desc());
            if ("待支付".equals(listsBean.getStatus_desc())) {
                order_list_item_status.setTextColor(Color.parseColor("#00A8FF"));
                order_list_item_status.setBackgroundResource(R.drawable.line_00a8ff_corner_10);
            } else {
                order_list_item_status.setTextColor(Color.parseColor("#00C86C"));
                order_list_item_status.setBackgroundResource(R.drawable.line_00c76b_corner_10);
            }
        }
        List<UserUOrderBean.DataBean.ListsBean.GoodsBean> beans = listsBean.getGoods();
        if (beans != null && beans.size() > 0) {
            for (int i = 0; i < beans.size(); i++) {
                UserUOrderBean.DataBean.ListsBean.GoodsBean bean = beans.get(i);
                LinearLayout linearLayout = (LinearLayout) View.inflate(mContext, R.layout.main_message_1_item_1, null);
                TextView t_11 = linearLayout.findViewById(R.id.t_1);
                TextView t_22 = linearLayout.findViewById(R.id.t_2);
                String name_1 = bean.getName();
                String value_1 = bean.getRprice();
                t_11.setText(name_1);
                t_22.setText(value_1 + "元");
                l_1.addView(linearLayout);
            }
        }
    }

    public void setListener(OnClickItemViewListener listener) {
        mListener = listener;
    }

    public interface OnClickItemViewListener{
        void OnClickItem(UserUOrderBean.DataBean.ListsBean bean);
    }

}
