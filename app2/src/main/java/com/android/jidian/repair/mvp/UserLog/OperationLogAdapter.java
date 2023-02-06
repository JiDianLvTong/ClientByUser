package com.android.jidian.repair.mvp.UserLog;

import com.android.jidian.repair.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : PTT
 * date: 2020/8/4 17:03
 * company: 兴达智联
 * description:
 */
public class OperationLogAdapter extends BaseQuickAdapter<OperationLogBean.DataBean, BaseViewHolder> {
    private int mDataLength = 0;

    public OperationLogAdapter() {
        super(R.layout.item_operation_log_layout);
    }

    public void setTotalLength(int len) {
        this.mDataLength = len;
    }

    @Override
    protected void convert(BaseViewHolder helper, OperationLogBean.DataBean item) {
        if (item != null) {
            helper.setText(R.id.tv_item_operation_log_name, item.getUname() + "  " + item.getUphone())
                    .setText(R.id.tv_item_operation_log_content, "操作内容：" + item.getDiftxt())
                    .setText(R.id.tv_item_operation_log_time, "时间：" + item.getCreate_time());
            if (mDataLength == (helper.getLayoutPosition() + 1)) {
                helper.setGone(R.id.li_item_list_foot, true);
            } else {
                helper.setGone(R.id.li_item_list_foot, false);
            }
        }
    }
}