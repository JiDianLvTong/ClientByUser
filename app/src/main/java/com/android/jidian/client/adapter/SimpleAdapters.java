package com.android.jidian.client.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.MapTopTabBean;
import com.bumptech.glide.Glide;

import java.util.List;

//ViewPager里面的GridView的适配器
public class SimpleAdapters extends BaseAdapter {
    private Activity activity;
    private List<MapTopTabBean.TopTabBean> list;

    public SimpleAdapters(Activity activity, List<MapTopTabBean.TopTabBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public MapTopTabBean.TopTabBean getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MapTopTabBean.TopTabBean bean = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_single_textview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.change_image = view.findViewById(R.id.change_image);
            viewHolder.change_text = view.findViewById(R.id.change_text);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.change_text.setText(bean.getName());
        Glide.with(activity).load(bean.getIcon()).into(viewHolder.change_image);
        return view;
    }

    static class ViewHolder {
        ImageView change_image;
        TextView change_text;
    }
}