package com.android.jidian.client.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.AdvicesLists2Bean;
import com.android.jidian.client.mvp.ui.activity.message.ActivityMessageViewHolder;
import com.android.jidian.client.mvp.ui.activity.message.SystemMessageViewHolder;
import com.android.jidian.client.util.ViewUtil;

import java.util.List;

public class AdvicesListsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private List<AdvicesLists2Bean.DataBean> mList;

    public AdvicesListsAdapter(Context context, List<AdvicesLists2Bean.DataBean> mList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    public void setList(List<AdvicesLists2Bean.DataBean> list) {
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 2) {
            ActivityMessageViewHolder viewHolder = new ActivityMessageViewHolder(mLayoutInflater.inflate(R.layout.item_message_activity_layout, parent, false));
            viewHolder.setOnTypeTwoClickListener(new ActivityMessageViewHolder.onTypeTwoClickListener() {
                @Override
                public void onItemViewClick(View itemView) {
                    if (onClickListener != null) {
                        onClickListener.onItemViewClick(itemView);
                    }
                }
            });
            return viewHolder;
        } else {
            SystemMessageViewHolder viewHolder = new SystemMessageViewHolder(mLayoutInflater.inflate(R.layout.item_message_system_layout, parent, false));
            viewHolder.setOnTypeOneClickListener(new SystemMessageViewHolder.onTypeOneClickListener() {
                @Override
                public void onItemViewClick(View itemView) {
                    if (onClickListener != null) {
                        onClickListener.onItemViewClick(itemView);
                    }
                }
            });
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AdvicesListsFragment2_RecyclerView_Adapter_TypeAbstractViewHolder) holder).bindHolder(mList.get(position));
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            params.topMargin = ViewUtil.dp2px(holder.itemView.getContext(), 10);
        } else {
            params.topMargin = 0;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String show_type = mList.get(position).getShow_type();
        if ("2".equals(show_type)) {
            return 2;
        }
        return 1;
    }

    public interface onClickListener {
        void onItemViewClick(View itemView);
    }

    public void setOnClickListener(AdvicesListsAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private onClickListener onClickListener;
}