package com.android.jidian.client.mvp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.EvaluateListsBean;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EvaluationListsActivity_RecyclerView_Adapter extends RecyclerView.Adapter<EvaluationListsActivity_RecyclerView_Adapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<EvaluateListsBean.DataBean> dataList;

    public EvaluationListsActivity_RecyclerView_Adapter(Context context, List<EvaluateListsBean.DataBean> dataList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ela_rv_a_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EvaluateListsBean.DataBean dataBean = dataList.get(position);
        Glide.with(context).load(dataBean.getAvater()).into(holder.elaRvAItemCirclrImageView);
        holder.elaRvAItemTextView1.setText(dataBean.getPhone());
        holder.elaRvAItemTextView2.setText(dataBean.getCreate_time());
        int stars = dataBean.getStars();
        if (stars > 4) {
            Glide.with(context).load(R.mipmap.yellow_stars).into(holder.elaRvAItemImageView5);
        } else {
            Glide.with(context).load(R.mipmap.gray_stars).into(holder.elaRvAItemImageView5);
        }
        if (stars > 3) {
            Glide.with(context).load(R.mipmap.yellow_stars).into(holder.elaRvAItemImageView4);
        } else {
            Glide.with(context).load(R.mipmap.gray_stars).into(holder.elaRvAItemImageView4);
        }
        if (stars > 2) {
            Glide.with(context).load(R.mipmap.yellow_stars).into(holder.elaRvAItemImageView3);
        } else {
            Glide.with(context).load(R.mipmap.gray_stars).into(holder.elaRvAItemImageView3);
        }
        if (stars > 1) {
            Glide.with(context).load(R.mipmap.yellow_stars).into(holder.elaRvAItemImageView2);
        } else {
            Glide.with(context).load(R.mipmap.gray_stars).into(holder.elaRvAItemImageView2);
        }
        if (stars > 0) {
            Glide.with(context).load(R.mipmap.yellow_stars).into(holder.elaRvAItemImageView1);
        } else {
            Glide.with(context).load(R.mipmap.gray_stars).into(holder.elaRvAItemImageView1);
        }
        StringBuilder builder = new StringBuilder();
        List<EvaluateListsBean.DataBean.TagsBean> tagsBeans = dataBean.getTags();
        for (int i = 0; i < tagsBeans.size(); i++) {
            EvaluateListsBean.DataBean.TagsBean tagsBean = tagsBeans.get(i);
            builder.append(tagsBean.getName());
            if (i == tagsBeans.size() - 1) {
                builder.append("\n");
            } else {
                builder.append(",");
            }
        }
        builder.append(dataBean.getContent());
        holder.elaRvAItemTextView3.setText(builder.toString());
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setData(List<EvaluateListsBean.DataBean> dataList) {
        this.dataList = dataList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView elaRvAItemCirclrImageView;
        TextView elaRvAItemTextView1;
        TextView elaRvAItemTextView2;
        ImageView elaRvAItemImageView1;
        ImageView elaRvAItemImageView2;
        ImageView elaRvAItemImageView3;
        ImageView elaRvAItemImageView4;
        ImageView elaRvAItemImageView5;
        TextView elaRvAItemTextView3;

        ViewHolder(View itemView) {
            super(itemView);
            elaRvAItemCirclrImageView = itemView.findViewById(R.id.ela_rv_a_item_circlrImageView);
            elaRvAItemTextView1 = itemView.findViewById(R.id.ela_rv_a_item_textView1);
            elaRvAItemTextView2 = itemView.findViewById(R.id.ela_rv_a_item_textView2);
            elaRvAItemImageView1 = itemView.findViewById(R.id.ela_rv_a_item_imageView1);
            elaRvAItemImageView2 = itemView.findViewById(R.id.ela_rv_a_item_imageView2);
            elaRvAItemImageView3 = itemView.findViewById(R.id.ela_rv_a_item_imageView3);
            elaRvAItemImageView4 = itemView.findViewById(R.id.ela_rv_a_item_imageView4);
            elaRvAItemImageView5 = itemView.findViewById(R.id.ela_rv_a_item_imageView5);
            elaRvAItemTextView3 = itemView.findViewById(R.id.ela_rv_a_item_textView3);
        }
    }
}
