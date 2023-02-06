package com.android.jidian.repair.mvp.message.fragment;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.jidian.repair.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : xiaoming
 * date: 2023/2/6 11:40
 * description:
 */
public class SystemMessageListAdapter extends BaseQuickAdapter<AdvicesListsBean.DataBean , BaseViewHolder> {

    public SystemMessageListAdapter() {
        super(R.layout.item_message_system_layout);
    }

    @Override
    protected void convert(BaseViewHolder holder, AdvicesListsBean.DataBean dataBean) {
        holder.setImageResource(R.id.iv_item_msg_system,R.drawable.icon_system_message);
        //已读
        if ("0".equals(dataBean.getIs_read())) {
            holder.setGone(R.id.point_item_msg_system, true);
            holder.setTextColor(R.id.tv_item_msg_system_title, Color.parseColor("#333333"))
                    .setTextColor(R.id.tv_item_msg_system_time,Color.parseColor("#999999"))
                    .setTextColor(R.id.tv_item_msg_system_content,Color.parseColor("#666666"))
                    .setTextColor(R.id.tv_item_msg_system_look,Color.parseColor("#666666"));
        } else {
            holder.setGone(R.id.point_item_msg_system, false);
            holder.setTextColor(R.id.tv_item_msg_system_title, Color.parseColor("#CCCCCC"))
                    .setTextColor(R.id.tv_item_msg_system_time,Color.parseColor("#CCCCCC"))
                    .setTextColor(R.id.tv_item_msg_system_content,Color.parseColor("#CCCCCC"))
                    .setTextColor(R.id.tv_item_msg_system_look,Color.parseColor("#CCCCCC"));
        }
        holder.setText(R.id.tv_item_msg_system_title, dataBean.getTitle())
                .setText(R.id.tv_item_msg_system_content,dataBean.getContent())
                .setText(R.id.tv_item_msg_system_look,"查看详情");
        if (!TextUtils.isEmpty(dataBean.getCreate_time())) {
            holder.setText(R.id.tv_item_msg_system_time, dataBean.getCreate_time().substring(0, 10));
        } else {
            holder.setText(R.id.tv_item_msg_system_time, "");
        }
    }
}
