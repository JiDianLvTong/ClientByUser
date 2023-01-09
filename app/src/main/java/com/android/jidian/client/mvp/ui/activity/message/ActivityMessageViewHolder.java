package com.android.jidian.client.mvp.ui.activity.message;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.adapter.AdvicesListsFragment2_RecyclerView_Adapter_TypeAbstractViewHolder;
import com.android.jidian.client.bean.AdvicesLists2Bean;
import com.android.jidian.client.util.ViewUtil;
import com.bumptech.glide.Glide;

public class ActivityMessageViewHolder extends AdvicesListsFragment2_RecyclerView_Adapter_TypeAbstractViewHolder {
    private Context mContext;
    private View itemView;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView ivContent;
    private TextView tvTitle;
    private TextView tvTime;
    private ImageView ivActivityStatus;

    public ActivityMessageViewHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        this.itemView = itemView;
        imageView1 = itemView.findViewById(R.id.imageView1);
        imageView2 = itemView.findViewById(R.id.imageView2);
        ivContent = itemView.findViewById(R.id.imageView3);
        tvTitle = itemView.findViewById(R.id.tv_item_msg_activity_title);
        tvTime = itemView.findViewById(R.id.tv_item_msg_activity_time);
        ivActivityStatus = itemView.findViewById(R.id.iv_item_msg_activity_ing);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTypeTwoClickListener != null) {
                    onTypeTwoClickListener.onItemViewClick(itemView);
                    imageView2.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void bindHolder(AdvicesLists2Bean.DataBean dataBean) {
        if (dataBean != null) {
            itemView.setTag(R.id.view_holder1, dataBean);
            tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_333333));
            ivActivityStatus.setImageResource(R.drawable.u6_message_item_type_1);

            if ("2".equals(dataBean.getType())) {
                //活动消息
                imageView1.setImageResource(R.drawable.u6_message_item_alert);
                ivActivityStatus.setVisibility(View.VISIBLE);
                //expire != 0 已结束
                int expire = dataBean.getExpire();
                if (expire != 0) {
                    tvTitle.setTextColor(ContextCompat.getColor(mContext, R.color.color_CBCBCB));
                    tvTime.setTextColor(ContextCompat.getColor(mContext, R.color.color_CBCBCB));
                    ivActivityStatus.setImageResource(R.drawable.u6_message_item_type_2);
                }
            } else {
                //系统消息
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
            if (!TextUtils.isEmpty(dataBean.getCreate_time())) {
                tvTime.setText(dataBean.getCreate_time().substring(0, 10));
            } else {
                tvTime.setText("");
            }

            int width = ViewUtil.getScreenWidth(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, (width - ViewUtil.dp2px(mContext, 32)) * 240 / 640);
            ivContent.setLayoutParams(params);
            if (TextUtils.isEmpty(dataBean.getImages())) {
                ivContent.setImageResource(R.drawable.u6_pub_null_image);
            } else {
                Glide.with(mContext)
                        .load(dataBean.getImages())
                        .error(R.drawable.u6_pub_null_image)
                        .placeholder(R.drawable.u6_pub_null_image)
                        .fallback(R.drawable.u6_pub_null_image)
                        .into(ivContent);
            }
        }
    }

    public interface onTypeTwoClickListener {
        void onItemViewClick(View itemView);
    }

    public void setOnTypeTwoClickListener(ActivityMessageViewHolder.onTypeTwoClickListener onTypeTwoClickListener) {
        this.onTypeTwoClickListener = onTypeTwoClickListener;
    }

    private onTypeTwoClickListener onTypeTwoClickListener;
}