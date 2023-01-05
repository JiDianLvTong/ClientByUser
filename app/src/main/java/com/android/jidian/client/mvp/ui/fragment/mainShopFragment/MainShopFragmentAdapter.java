package com.android.jidian.client.mvp.ui.fragment.mainShopFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.LocalShopActivityBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

class MainShopFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface MainShopViewPagerItem_1_AdapterListener{
        void onClickReturn(int position);
    }

    private Context mContext;
    private ArrayList<LocalShopActivityBean.Data> dataArrayList;
    private MainShopViewPagerItem_1_AdapterListener mainShopViewPagerItem_1_adapterListener;

    public MainShopFragmentAdapter(Context context, ArrayList<LocalShopActivityBean.Data> dataArrayList , MainShopViewPagerItem_1_AdapterListener mainShopViewPagerItem_1_adapterListener) {
        mContext = context;
        this.dataArrayList = dataArrayList;
        this.mainShopViewPagerItem_1_adapterListener = mainShopViewPagerItem_1_adapterListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.activity_pay_local_shop_item, parent, false);
        return new NormalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int fPosition = position;
        NormalHolder normalHolder = (NormalHolder) holder;
        normalHolder.t_1.setText(dataArrayList.get(position).getFname());
        normalHolder.t_2.setText(dataArrayList.get(position).getRprice());
        normalHolder.t_3.setText(dataArrayList.get(position).getOprice());
        Glide.with(mContext).load(dataArrayList.get(position).getBgurl()).into(normalHolder.i_1);
        normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainShopViewPagerItem_1_adapterListener.onClickReturn(fPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    public class NormalHolder extends RecyclerView.ViewHolder{

        private TextView t_1;
        private TextView t_2;
        private TextView t_3;
        private ImageView i_1;

        public NormalHolder(View itemView) {
            super(itemView);
            t_1 = itemView.findViewById(R.id.t_1);
            t_2 = itemView.findViewById(R.id.t_2);
            t_3 = itemView.findViewById(R.id.t_3);
            i_1 = itemView.findViewById(R.id.i_1);
        }
    }

}