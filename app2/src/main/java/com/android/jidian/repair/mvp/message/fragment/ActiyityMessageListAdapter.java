package com.android.jidian.repair.mvp.message.fragment;

import android.text.TextUtils;
import android.widget.ImageView;

import com.android.jidian.repair.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : xiaoming
 * date: 2023/2/6 11:40
 * description:
 */
public class ActiyityMessageListAdapter extends BaseQuickAdapter<AdvicesListsBean.DataBean, BaseViewHolder> {

    public ActiyityMessageListAdapter() {
        super(R.layout.item_message_activity_layout);
    }

    @Override
    protected void convert(BaseViewHolder holder, AdvicesListsBean.DataBean dataBean) {
        holder.setImageResource(R.id.iv_item_msg_activity, R.drawable.icon_activity_message);
        //已读
        holder.setGone(R.id.point_item_msg_activity, "0".equals(dataBean.getIs_read()));
        holder.setText(R.id.tv_item_msg_activity_title, dataBean.getTitle());
        //进行中
        holder.setImageResource(R.id.iv_item_msg_activity_content, "0".equals(dataBean.getExpire()) ? R.drawable.icon_activity_ing : R.drawable.icon_activity_end);

        if (!TextUtils.isEmpty(dataBean.getCreate_time())) {
            holder.setText(R.id.tv_item_msg_activity_time, dataBean.getCreate_time().substring(0, 10));
        } else {
            holder.setText(R.id.tv_item_msg_activity_time, "");
        }
        if (!TextUtils.isEmpty(dataBean.getImages())) {
            Glide.with(mContext)
                    .load(dataBean.getImages())
                    .error(R.drawable.image032504)
                    .placeholder(R.drawable.image032504)
                    .fallback(R.drawable.image032504)
                    .into((ImageView) holder.getView(R.id.iv_item_msg_activity_content));
        } else {
            holder.setImageResource(R.id.iv_item_msg_activity_content, R.drawable.image032504);
        }
    }
}
