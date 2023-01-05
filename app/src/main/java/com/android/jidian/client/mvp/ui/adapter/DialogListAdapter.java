package com.android.jidian.client.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.DialogListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : PTT
 * date: 9/22/21 5:06 PM
 * company: 兴达智联
 * description:
 */
public class DialogListAdapter extends BaseAdapter {
    private Context mContext;
    private List<DialogListBean> data = new ArrayList<>();

    public DialogListAdapter(Context activity, List<DialogListBean> data) {
        this.mContext = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.pay_rent_select_item, null);
        TextView t_1 = view.findViewById(R.id.date_text);
        if (data.size() > 0) {
            DialogListBean mBean = data.get(position);
            if (mBean != null) {
                t_1.setText(mBean.getName());
                view.setTag(mBean.getValue());
                t_1.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onItemClick(mBean);
                    }
                });
            }
        }
        return view;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DialogListBean bean);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}