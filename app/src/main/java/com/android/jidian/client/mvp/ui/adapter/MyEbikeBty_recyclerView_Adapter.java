package com.android.jidian.client.mvp.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.MyEbikeBtyBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyEbikeBty_recyclerView_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_BIKE = 1;
    private static final int TYPE_BATTERY = 2;
    private LayoutInflater mLayoutInflater;
    private List<MyEbikeBtyBean.DataBean.EbikeBean> list1;
    private List<MyEbikeBtyBean.DataBean.BatteryBean> list2;
    private List<Integer> types = new ArrayList<>();
    //存储多个list在types中的起始位置
    private Map<Integer, Integer> mPositions = new HashMap<>();
    private Context context;
    private OnItemClickListener listener;

    public MyEbikeBty_recyclerView_Adapter(Context context, List<MyEbikeBtyBean.DataBean.EbikeBean> list1, List<MyEbikeBtyBean.DataBean.BatteryBean> list2) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        addListByType(TYPE_BIKE, list1);
        addListByType(TYPE_BATTERY, list2);
        this.list1 = list1;
        this.list2 = list2;
    }

    public void setList(List<MyEbikeBtyBean.DataBean.EbikeBean> list1, List<MyEbikeBtyBean.DataBean.BatteryBean> list2) {
        types.clear();
        mPositions.clear();
        addListByType(TYPE_BIKE, list1);
        addListByType(TYPE_BATTERY, list2);
        this.list1 = list1;
        this.list2 = list2;
    }

    private void addListByType(int type, List list) {
        mPositions.put(type, types.size());
        for (int i = 0; i < list.size(); i++) {
            types.add(type);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BATTERY) {
            return new TypeTwoViewHolder(mLayoutInflater.inflate(R.layout.myebike_item, parent, false));
        }
        return new TypeOneViewHolder(mLayoutInflater.inflate(R.layout.myebike_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        //得到对应list中的相对位置（在该list中的真实位置）
        //mPositions.get(viewType)得到的是该list在types中的起始位置
        int realPosition = position - mPositions.get(viewType);
        switch (viewType) {
            case TYPE_BIKE:
                ((TypeOneViewHolder) holder).bindHolder(list1.get(realPosition));
                break;
            case TYPE_BATTERY:
                ((TypeTwoViewHolder) holder).bindHolder(list2.get(realPosition));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    @Override
    public int getItemViewType(int position) {
        return types.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onEBikeBindClick(View view,MyEbikeBtyBean.DataBean.EbikeBean bean );
        void onBatteryBindClick(View view,MyEbikeBtyBean.DataBean.BatteryBean bean );
    }

    abstract class TypeAbstractViewHolder extends RecyclerView.ViewHolder {

        TypeAbstractViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindHolder(Object o);
    }

    class TypeOneViewHolder extends TypeAbstractViewHolder {
        ImageView i_3;//bg_img
        TextView t_1;//name
        TextView t_5;//name
        TextView volt;
        TextView rprice;
        TextView mtitle;
        TextView number;
        TextView vin;
        TextView create_time;
//        TextView bind_time;
        TypeOneViewHolder(View itemView) {
            super(itemView);
            i_3 = itemView.findViewById(R.id.i_3);
            t_5 = itemView.findViewById(R.id.t_5);
            t_1 = itemView.findViewById(R.id.t_1);
            volt = itemView.findViewById(R.id.volt);
            rprice = itemView.findViewById(R.id.rprice);
            mtitle = itemView.findViewById(R.id.mtitle);
            number = itemView.findViewById(R.id.number);
            vin = itemView.findViewById(R.id.vin);
            create_time = itemView.findViewById(R.id.create_time);
//            bind_time = itemView.findViewById(R.id.bind_time);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindHolder(Object o) {
            MyEbikeBtyBean.DataBean.EbikeBean bean = (MyEbikeBtyBean.DataBean.EbikeBean) o;
            Glide.with(context).load(bean.getBg_img()).into(i_3);
            t_1.setText(bean.getName() + bean.getExt());
            volt.setVisibility(View.GONE);
            vin.setVisibility(View.VISIBLE);
            number.setText("GPS:" + bean.getNumber());
            mtitle.setText("商家:" + bean.getMtitle());
            if (bean.getIs_bike() == 1){
                vin.setText("编号/车架号:" +bean.getBike_num()+"/"+ bean.getVin());
            }else {
                vin.setText("编号/车架号:待绑定");
            }
            create_time.setText("下单时间:" + bean.getCreate_time());
//            bind_time.setText("绑定时间:"+bean.get);
            t_5.setText(bean.getBtips());
            if (bean.getUse_type().equals("10")){//购买
                rprice.setText("价格:" + bean.getRprice() + "元");
            }else {//租赁
                rprice.setText("押金:" + bean.getRprice() + "元");
            }
            if (bean.getFade_status().equals("2")){//等待用户确认
                t_5.setBackgroundResource(R.drawable.shape_c2672c_de892e_radius_24);
            }else {//fade_status = 0 正常  fade_status = 3 已退
                if (bean.getIs_bind().equals("2")){//待绑定
                    t_5.setBackgroundResource(R.drawable.gradient_f18a1c_corner_20);
                }else {//已绑定
                    t_5.setBackgroundResource(R.drawable.corner_gray);
                }
            }

            t_5.setTag(bean);
            if (listener != null) {
                t_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onEBikeBindClick(v,(MyEbikeBtyBean.DataBean.EbikeBean)t_5.getTag());
                    }
                });
            }

        }
    }

    class TypeTwoViewHolder extends TypeAbstractViewHolder {
        ImageView i_3;//bg_img
//        ImageView i_2;
        TextView t_1;//name
        TextView t_5;//name
        TextView volt;
        TextView rprice;
        TextView mtitle;
        TextView number;
        TextView vin;
        TextView create_time;

        TypeTwoViewHolder(View itemView) {
            super(itemView);
            i_3 = itemView.findViewById(R.id.i_3);
            t_5 = itemView.findViewById(R.id.t_5);
            t_1 = itemView.findViewById(R.id.t_1);
            volt = itemView.findViewById(R.id.volt);
            rprice = itemView.findViewById(R.id.rprice);
            mtitle = itemView.findViewById(R.id.mtitle);
            number = itemView.findViewById(R.id.number);
            vin = itemView.findViewById(R.id.vin);
            create_time = itemView.findViewById(R.id.create_time);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindHolder(Object o) {
            MyEbikeBtyBean.DataBean.BatteryBean bean = (MyEbikeBtyBean.DataBean.BatteryBean) o;
            Glide.with(context).load(bean.getBg_img()).into(i_3);
            vin.setVisibility(View.GONE);
            volt.setVisibility(View.VISIBLE);
            t_1.setText(bean.getName() + bean.getExt());
            volt.setText("规格:" + bean.getVolt() + "V");
            number.setText("编号：" + bean.getNumber());
            mtitle.setText("商家:" + bean.getMtitle());
            number.setText("编号:" + bean.getNumber());
            create_time.setText("下单时间:" + bean.getCreate_time());
            t_5.setText(bean.getBtips());
            if (bean.getUse_type().equals("10")){//购买
                rprice.setText("价格:" + bean.getRprice() + "元");
            }else {//租赁
                rprice.setText("押金:" + bean.getRprice() + "元");
            }
            if (bean.getFade_status().equals("2")){//等待用户确认
                t_5.setBackgroundResource(R.drawable.shape_c2672c_de892e_radius_24);
            }else {//fade_status = 0 正常  fade_status = 3 已退
                if (bean.getIs_bind().equals("2")){//待绑定
                    t_5.setBackgroundResource(R.drawable.gradient_f18a1c_corner_20);
                }else {//已绑定
                    t_5.setBackgroundResource(R.drawable.corner_gray);
                }
            }

            t_5.setTag(bean);
            if (listener != null) {
                t_5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onBatteryBindClick(v,(MyEbikeBtyBean.DataBean.BatteryBean)t_5.getTag());
                    }
                });
            }
        }
    }

}
