package com.android.jidian.repair.mvp.UserLog;

import com.android.jidian.repair.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : PTT
 * date: 2020/10/26 14:04
 * company: 兴达智联
 * description: 电表读数
 */
public class MeterReadingAdapter extends BaseQuickAdapter<MeterReadingBean.DataBean, BaseViewHolder> {
    private int mDataLength = 0;

    public MeterReadingAdapter() {
        super(R.layout.item_battery_inquiry_layout);
    }

    public void setTotalLength(int len) {
        this.mDataLength = len;
    }

    @Override
    protected void convert(BaseViewHolder helper, MeterReadingBean.DataBean item) {
        if (item != null) {
            helper.setText(R.id.tv_item_battery_inquiry_name, "电表读数：" + item.getNow_electric()
                    + "\n换电次数：" + item.getEx_nums()
                    + "\n使用电量：" + item.getElectric()
                    + "\n时间：" + item.getDate());
            if (mDataLength == (helper.getLayoutPosition() + 1)) {
                helper.setGone(R.id.li_item_list_foot, true);
            } else {
                helper.setGone(R.id.li_item_list_foot, false);
            }
        }
    }
}