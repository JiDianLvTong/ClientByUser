package com.android.jidian.extension.view.activity.main.profitFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.jidian.extension.R;
import com.android.jidian.extension.bean.MainGetProfitInfoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : xiaoming
 * date: 2023/1/12 14:02
 * description:
 */
public class ProfitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MainGetProfitInfoBean.Lists> dataList;

    public ProfitAdapter(Context context ,  ArrayList<MainGetProfitInfoBean.Lists> dataList ) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main_fragment_profit_item,parent,false);
        return new ProfitAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProfitAdapterViewHolder chargeSiteListViewHolder = (ProfitAdapterViewHolder) holder;
        chargeSiteListViewHolder.t_1.setText("用户: "+dataList.get(position).getPhone());
        chargeSiteListViewHolder.t_2.setText("收益 "+dataList.get(position).getIncome());
        chargeSiteListViewHolder.t_3.setText("时间: "+ dataList.get(position).getIndate());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class ProfitAdapterViewHolder extends RecyclerView.ViewHolder{

        private TextView t_1;
        private TextView t_2;
        private TextView t_3;

        public ProfitAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            t_1 = itemView.findViewById(R.id.t_1);
            t_2 = itemView.findViewById(R.id.t_2);
            t_3 = itemView.findViewById(R.id.t_3);
        }
    }
}
