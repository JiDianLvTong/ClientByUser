package com.android.jidian.client.mvp.ui.activity.main.mainShopFragment;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.ShopBuyBean;
import com.android.jidian.client.bean.ShopRentBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author : xiaoming
 * date: 2022/12/29 16:24
 * company: 兴达智联
 * description:
 */
public class MainShopAdapter extends BaseQuickAdapter<MainShopBean, BaseViewHolder> {

    public MainShopAdapter() {
        super(R.layout.u6_activity_main_fragment_shop_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainShopBean mainShopBean) {

        String type = mainShopBean.getMainShopDataType();
        if(type.equals("buy")){
            ShopBuyBean.DataBean.PacksBean bean = (ShopBuyBean.DataBean.PacksBean) mainShopBean.getObjectBean();
            String color = bean.getFcolor();
            helper.setTextColor(R.id.tv_main_shop_item_title, Color.parseColor(color));
            helper.setTextColor(R.id.tv_main_shop_item_price, Color.parseColor(color));
            helper.setTextColor(R.id.tv_main_shop_item_price_unit, Color.parseColor(color));
            helper.setText(R.id.tv_main_shop_item_title,bean.getName());
            helper.setText(R.id.tv_main_shop_item_price,bean.getRprice());
            Glide.with(mContext).load(bean.getBg_img()).into(((ImageView)helper.getView(R.id.iv_main_shop_bg)));
            List<ShopBuyBean.DataBean.PacksBean.MainsBean> mainsBean = bean.getMains();
            ((LinearLayout)helper.getView(R.id.ll_main_shop_item_detail)).removeAllViews();
            if (mainsBean.size() > 0) {
                for (int i = 0; i < mainsBean.size(); i++) {
                    if (!TextUtils.isEmpty(mainsBean.get(0).getName())) {
                        if (mainsBean.get(0).getName().contains("卡")) {
                            break;
                        }
                    }
                    LinearLayout linearLayout = (LinearLayout) View.inflate(mContext, R.layout.main_shop_recyclerview_item_item, null);
                    if (i != 0) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(20, 0, 0, 0);
                        linearLayout.setLayoutParams(layoutParams);
                    }
                    TextView t_1 = linearLayout.findViewById(R.id.t_1);
                    TextView t_2 = linearLayout.findViewById(R.id.t_2);
                    t_1.setText(mainsBean.get(i).getName());
                    t_1.setTextColor(Color.parseColor(color));
                    t_2.setText(mainsBean.get(i).getSize());
                    t_2.setTextColor(Color.parseColor(color));
                    ((LinearLayout)helper.getView(R.id.ll_main_shop_item_detail)).addView(linearLayout);
                }
            }
        }else if(type.equals("rent")){
            ShopRentBean.DataBean.PacksBean bean = (ShopRentBean.DataBean.PacksBean) mainShopBean.getObjectBean();
            String color = bean.getFcolor();
            helper.setTextColor(R.id.tv_main_shop_item_title, Color.parseColor(color));
            helper.setTextColor(R.id.tv_main_shop_item_price, Color.parseColor(color));
            helper.setTextColor(R.id.tv_main_shop_item_price_unit, Color.parseColor(color));
            helper.setText(R.id.tv_main_shop_item_title,bean.getName());
            helper.setText(R.id.tv_main_shop_item_price,bean.getRprice());
            Glide.with(mContext).load(bean.getBg_img()).into(((ImageView)helper.getView(R.id.iv_main_shop_bg)));
            List<ShopRentBean.DataBean.PacksBean.MainsBean.LeftBean> mainsBean = bean.getMains().getLeft();
            ((LinearLayout)helper.getView(R.id.ll_main_shop_item_detail)).removeAllViews();
            if (mainsBean.size() > 0) {
                for (int i = 0; i < mainsBean.size(); i++) {
                    if (!TextUtils.isEmpty(mainsBean.get(0).getName())) {
                        if (mainsBean.get(0).getName().contains("卡")) {
                            break;
                        }
                    }
                    LinearLayout linearLayout = (LinearLayout) View.inflate(mContext, R.layout.main_shop_recyclerview_item_item, null);
                    if (i != 0) {
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(20, 0, 0, 0);
                        linearLayout.setLayoutParams(layoutParams);
                    }
                    TextView t_1 = linearLayout.findViewById(R.id.t_1);
                    TextView t_2 = linearLayout.findViewById(R.id.t_2);
                    t_1.setText(mainsBean.get(i).getName());
                    t_1.setTextColor(Color.parseColor(color));
                    t_2.setText(mainsBean.get(i).getSize());
                    t_2.setTextColor(Color.parseColor(color));
                    ((LinearLayout)helper.getView(R.id.ll_main_shop_item_detail)).addView(linearLayout);
                }
            }
        }
    }

}
