package com.android.jidian.repair.mvp.user.userLog;

import android.content.Context;

import com.android.jidian.repair.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : PTT
 * date: 2020/10/22 14:04
 * company: 兴达智联
 * description: 电池查询
 */
public class BatteryInquiryAdapter extends BaseQuickAdapter<BatteryInquiryBean.DataBean, BaseViewHolder> {
    private Context mContext;
    private int mDataLength = 0;
    private String mTitle;

    public BatteryInquiryAdapter(Context context, String title) {
        super(R.layout.item_battery_inquiry_layout);
        this.mContext = context;
        this.mTitle = title;
    }

    public void setTotalLength(int len) {
        this.mDataLength = len;
    }

    @Override
    protected void convert(BaseViewHolder helper, BatteryInquiryBean.DataBean item) {
        if (item != null) {
            if ("电池查询".equals(mTitle)) {
                helper.setText(R.id.tv_item_battery_inquiry_name, "所在电柜：" + item.getCabid() + item.getCity()
                        + "\n电池编号：" + item.getBattery()
                        + "\n当前绑定用户：" + item.getUname()
                        + "\n时间：" + item.getCreate_time());
            } else {
                helper.setText(R.id.tv_item_battery_inquiry_name, "所在电柜：" + item.getCabid() + item.getCity()
                        + "\n电池编号：" + item.getBattery()
                        + "\n租卖类型：" + item.getGet_type()
                        + "\n用户信息：" + item.getUname()
                        + "\n时间：" + item.getCreate_time());
            }
            if (mDataLength == (helper.getLayoutPosition() + 1)) {
                helper.setGone(R.id.li_item_list_foot, true);
            } else {
                helper.setGone(R.id.li_item_list_foot, false);
            }
        }
    }
}