package com.android.jidian.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.jidian.client.bean.AdvicesLists2Bean;

public abstract class AdvicesListsFragment2_RecyclerView_Adapter_TypeAbstractViewHolder
        extends RecyclerView.ViewHolder {

    public AdvicesListsFragment2_RecyclerView_Adapter_TypeAbstractViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindHolder(AdvicesLists2Bean.DataBean dataBean);
}
