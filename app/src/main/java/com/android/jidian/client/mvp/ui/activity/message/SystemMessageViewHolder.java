package com.android.jidian.client.mvp.ui.activity.message;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.adapter.AdvicesListsFragment2_RecyclerView_Adapter_TypeAbstractViewHolder;
import com.android.jidian.client.bean.AdvicesLists2Bean;

public class SystemMessageViewHolder extends AdvicesListsFragment2_RecyclerView_Adapter_TypeAbstractViewHolder {
    private Context mContext;
    private View itemView;
    private ImageView imageView1;
    private ImageView imageView2;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvContent;
    private ImageView ivActivityStatus;
    private TextView tvLookDetail;

    public SystemMessageViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        this.itemView = itemView;
        imageView1 = itemView.findViewById(R.id.imageView1);
        imageView2 = itemView.findViewById(R.id.imageView2);
        tvTitle = itemView.findViewById(R.id.tv_item_msg_system_title);
        tvTime = itemView.findViewById(R.id.tv_item_msg_system_time);
        tvContent = itemView.findViewById(R.id.tv_item_msg_system_content);
        ivActivityStatus = itemView.findViewById(R.id.iv_item_msg_system_ing);
        tvLookDetail = itemView.findViewById(R.id.tv_item_msg_system_lookdetail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTypeOneClickListener != null) {
                    onTypeOneClickListener.onItemViewClick(itemView);
                    imageView2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void bindHolder(AdvicesLists2Bean.DataBean dataBean) {
        if ((dataBean != null)) {
            itemView.setTag(R.id.view_holder1, dataBean);
            tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white_ffffff));
            tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.white_ffffff));
            tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.white_ffffff));
            ivActivityStatus.setImageResource(R.drawable.u6_message_item_type_1);
            tvLookDetail.setTextColor(ContextCompat.getColor(mContext, R.color.white_ffffff));

            if ("2".equals(dataBean.getType())) {
                imageView1.setImageResource(R.drawable.u6_message_item_alert);
                ivActivityStatus.setVisibility(View.VISIBLE);
                //expire != 0 已结束
                int expire = dataBean.getExpire();
                if (expire != 0) {
                    tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white_ffffff));
                    tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.white_ffffff));
                    tvContent.setTextColor(ContextCompat.getColor(mContext, R.color.white_ffffff));
                    ivActivityStatus.setImageResource(R.drawable.u6_message_item_type_2);
                    tvLookDetail.setTextColor(ContextCompat.getColor(mContext, R.color.white_ffffff));
                }
            } else {
                imageView1.setImageResource(R.drawable.icon_system_message);
                ivActivityStatus.setVisibility(View.GONE);
            }

            //已读
            if ("0".equals(dataBean.getIs_read())) {
                imageView2.setVisibility(View.VISIBLE);
            } else {
                imageView2.setVisibility(View.GONE);
            }

            tvTitle.setText(dataBean.getTitle());
            tvContent.setText(dataBean.getContent());
            if (!TextUtils.isEmpty(dataBean.getCreate_time())) {
                tvTime.setText(dataBean.getCreate_time().substring(0, 10));
            } else {
                tvTime.setText("");
            }
        }
    }

    public interface onTypeOneClickListener {
        void onItemViewClick(View itemView);
    }

    public void setOnTypeOneClickListener(SystemMessageViewHolder.onTypeOneClickListener onTypeOneClickListener) {
        this.onTypeOneClickListener = onTypeOneClickListener;
    }

    private onTypeOneClickListener onTypeOneClickListener;
}