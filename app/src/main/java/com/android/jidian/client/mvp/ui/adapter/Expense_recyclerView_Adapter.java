package com.android.jidian.client.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.jidian.client.R;
import com.android.jidian.client.bean.ExpenseBean;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Expense_recyclerView_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_BIKE = 1;
    private static final int TYPE_BATTERY = 2;
    private static final int TYPE_UCARD = 3;
    private static final int TYPE_PACKETS = 4;
    private LayoutInflater mLayoutInflater;
    private List<ExpenseBean.DataBean.EbikeBean> list1;
    private List<ExpenseBean.DataBean.BatteryBean> list2;
    private List<ExpenseBean.DataBean.UmonthBean.UcardBean> list3;
    private List<ExpenseBean.DataBean.UmonthBean.PacketsBean> list4;

    private List<Integer> types = new ArrayList<>();
    //存储多个list在types中的起始位置
    private Map<Integer, Integer> mPositions = new HashMap<>();
    private Context context;
    private OnItemClickListener listener;

    public Expense_recyclerView_Adapter(Context context, List<ExpenseBean.DataBean.EbikeBean> list1, List<ExpenseBean.DataBean.BatteryBean> list2,
                                        List<ExpenseBean.DataBean.UmonthBean.UcardBean> list3, List<ExpenseBean.DataBean.UmonthBean.PacketsBean> list4) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
        addListByType(TYPE_BIKE, list1);
        addListByType(TYPE_BATTERY, list2);
        addListByType(TYPE_PACKETS, list4);
        addListByType(TYPE_UCARD, list3);
        this.list1 = list1;
        this.list2 = list2;
        this.list3 = list3;
        this.list4 = list4;
    }

    public void setList(List<ExpenseBean.DataBean.EbikeBean> list1, List<ExpenseBean.DataBean.BatteryBean> list2,
                        List<ExpenseBean.DataBean.UmonthBean.UcardBean> list3, List<ExpenseBean.DataBean.UmonthBean.PacketsBean> list4) {
        types.clear();
        mPositions.clear();
        addListByType(TYPE_BIKE, list1);
        addListByType(TYPE_BATTERY, list2);
        addListByType(TYPE_PACKETS, list4);
        addListByType(TYPE_UCARD, list3);
        this.list1 = list1;
        this.list2 = list2;
        this.list3 = list3;
        this.list4 = list4;
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
            return new TypeTwoViewHolder(mLayoutInflater.inflate(R.layout.expense_item, parent, false));
        } else if (viewType == TYPE_BIKE) {
            return new TypeOneViewHolder(mLayoutInflater.inflate(R.layout.expense_item, parent, false));
        } else if (viewType == TYPE_UCARD) {
            return new TypeThrViewHolder(mLayoutInflater.inflate(R.layout.expense_item2, parent, false));
        } else {
            return new TypeFouViewHolder(mLayoutInflater.inflate(R.layout.expense_item1, parent, false));
        }
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
            case TYPE_UCARD:
                ((TypeThrViewHolder) holder).bindHolder(list3.get(realPosition));
                break;
            case TYPE_PACKETS:
                ((TypeFouViewHolder) holder).bindHolder(list4.get(realPosition));
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
        void onEbikeSelectClick(View view, ExpenseBean.DataBean.EbikeBean bean);

        void onBatterySelectClick(View view, ExpenseBean.DataBean.BatteryBean bean);

        void onUcardSelectClick(View view, ExpenseBean.DataBean.UmonthBean.UcardBean bean);

        void onUmonthSelectClick(View view, ExpenseBean.DataBean.UmonthBean.PacketsBean bean);
    }

    abstract class TypeAbstractViewHolder extends RecyclerView.ViewHolder {

        TypeAbstractViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindHolder(Object o);
    }

    //bike
    class TypeOneViewHolder extends TypeAbstractViewHolder {
        ImageView i_1;//bg_img
        ImageView i_2;
        ImageView i_3;
        TextView t_1;//name
        TextView t_2;
        TextView t_3;
        TextView t_4;
        TextView t_5;
        TextView t_7;
        FrameLayout f1;

        TypeOneViewHolder(View itemView) {
            super(itemView);
            i_1 = itemView.findViewById(R.id.i_1);
            i_2 = itemView.findViewById(R.id.i_2);
            i_3 = itemView.findViewById(R.id.i_3);
            t_1 = itemView.findViewById(R.id.t_1);
            t_2 = itemView.findViewById(R.id.t_2);
            t_3 = itemView.findViewById(R.id.t_3);
            t_4 = itemView.findViewById(R.id.t_4);
            t_5 = itemView.findViewById(R.id.t_5);
            t_7 = itemView.findViewById(R.id.t_7);
            f1 = itemView.findViewById(R.id.f1);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindHolder(Object o) {
            ExpenseBean.DataBean.EbikeBean bean = (ExpenseBean.DataBean.EbikeBean) o;
            Glide.with(context).load(bean.getBg_img()).into(i_3);
            t_2.setText("编号:" + bean.getNumber());
            if ("10".equals(bean.getUse_type())) {//购买
                f1.setVisibility(View.GONE);
                t_1.setText(bean.getName() + bean.getExt());
                i_2.setVisibility(View.INVISIBLE);
                t_3.setVisibility(View.GONE);
                t_4.setText("价格:" + bean.getRprice() + "元");
                t_5.setText("购买时间:" + bean.getCreate_time());
            } else {//租赁
                f1.setVisibility(View.VISIBLE);
                t_1.setText(bean.getName());
                i_2.setVisibility(View.VISIBLE);
                t_3.setVisibility(View.GONE);
                t_3.setText("押金:" + bean.getRprice());
                t_4.setText("租金:" + bean.getMrent() + "元");
                t_5.setText("到期时间:" + bean.getEnd_ts());
                t_7.setText(bean.getExt());
            }
            if (bean.getChecked() == 0) {//0=不选
                bean.setIschecked(0);
                i_2.setImageResource(R.drawable.ic_disclaimer_agreement_uncheck);
            } else if (bean.getChecked() == 1) {//1=选中，可以点击
                bean.setIschecked(1);
                i_2.setImageResource(R.drawable.qianbao_select_2);
            } else {//2=选中，不可以点击
                bean.setIschecked(1);
                i_2.setImageResource(R.drawable.qianbao_select_2);
            }
            i_2.setTag(bean);
            if (listener != null) {
                i_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onEbikeSelectClick(v, (ExpenseBean.DataBean.EbikeBean) i_2.getTag());
                    }
                });
            }

        }
    }

    //battery
    class TypeTwoViewHolder extends TypeAbstractViewHolder {
        ImageView i_1;//bg_img
        ImageView i_2;
        ImageView i_3;
        TextView t_1;//name
        TextView t_2;
        TextView t_3;
        TextView t_4;
        TextView t_5;
        TextView t_7;
        FrameLayout f1;

        TypeTwoViewHolder(View itemView) {
            super(itemView);
            i_1 = itemView.findViewById(R.id.i_1);
            i_2 = itemView.findViewById(R.id.i_2);
            i_3 = itemView.findViewById(R.id.i_3);
            t_1 = itemView.findViewById(R.id.t_1);
            t_2 = itemView.findViewById(R.id.t_2);
            t_3 = itemView.findViewById(R.id.t_3);
            t_4 = itemView.findViewById(R.id.t_4);
            t_5 = itemView.findViewById(R.id.t_5);
            t_7 = itemView.findViewById(R.id.t_7);
            f1 = itemView.findViewById(R.id.f1);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindHolder(Object o) {
            ExpenseBean.DataBean.BatteryBean bean = (ExpenseBean.DataBean.BatteryBean) o;
            Glide.with(context).load(bean.getBg_img()).into(i_3);
            t_2.setText("编号:" + bean.getNumber());
            if ("10".equals(bean.getUse_type())) {//购买
                t_1.setText(bean.getName() + bean.getExt());
                f1.setVisibility(View.GONE);
                i_2.setVisibility(View.INVISIBLE);
                t_3.setVisibility(View.GONE);
                t_4.setText("价格:" + bean.getRprice() + "元");
                t_5.setText("购买时间:" + bean.getCreate_time());
            } else {//租赁
                t_1.setText(bean.getName());
                i_2.setVisibility(View.VISIBLE);
                f1.setVisibility(View.VISIBLE);
                t_3.setVisibility(View.GONE);
                t_3.setText("押金:" + bean.getRprice());
                t_4.setText("租金:" + bean.getMrent() + "元");
                t_5.setText("到期时间:" + bean.getEnd_ts());
                t_7.setText(bean.getExt());
            }
            if (bean.getChecked() == 0) {//0=不选
                bean.setIschecked(0);
                i_2.setImageResource(R.drawable.ic_disclaimer_agreement_uncheck);
            } else if (bean.getChecked() == 1) {//1=选中，可以点击
                bean.setIschecked(1);
                i_2.setImageResource(R.drawable.qianbao_select_2);
            } else {//2=选中，不可以点击
                bean.setIschecked(1);
                i_2.setImageResource(R.drawable.qianbao_select_2);
            }
            i_2.setTag(bean);
            if (listener != null) {
                i_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onBatterySelectClick(v, (ExpenseBean.DataBean.BatteryBean) i_2.getTag());
                    }
                });
            }
        }
    }

    //ucard
    class TypeThrViewHolder extends TypeAbstractViewHolder {
        TextView t_1;//name
        TextView t_2;
        TextView t_3;
        TextView t_4;
        TextView t_41;
        TextView t_5;
        TextView t_6;
        TextView t_11;
        TextView t_7;
        TextView t_8;
        ImageView i_2;
        ImageView imageview1;
        ImageView imageview2;
        LinearLayout l_1;

        TypeThrViewHolder(View itemView) {
            super(itemView);
            t_1 = itemView.findViewById(R.id.t_1);
            t_2 = itemView.findViewById(R.id.t_2);
            t_3 = itemView.findViewById(R.id.t_3);
            t_4 = itemView.findViewById(R.id.t_4);
            t_41 = itemView.findViewById(R.id.t_41);
            t_5 = itemView.findViewById(R.id.t_5);
            t_6 = itemView.findViewById(R.id.t_6);
            t_11 = itemView.findViewById(R.id.t_11);
            i_2 = itemView.findViewById(R.id.i_2);
            t_7 = itemView.findViewById(R.id.t_7);
            t_8 = itemView.findViewById(R.id.t_8);
            imageview1 = itemView.findViewById(R.id.imageview1);
            imageview2 = itemView.findViewById(R.id.imageview2);
            l_1 = itemView.findViewById(R.id.l_1);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindHolder(Object o) {
            ExpenseBean.DataBean.UmonthBean.UcardBean bean = (ExpenseBean.DataBean.UmonthBean.UcardBean) o;
            t_1.setText(bean.getName());
            t_2.setText(bean.getRprice() + "元");

            if ("20".equals(bean.getShow_type())) {
                //C端换电卡
                t_3.setText("使用说明:" + bean.getRule());
                t_7.setText(bean.getLtimes());
                if ("240".equals(bean.getOtype())) {
                    t_4.setText("开始时间:" + bean.getStart_ts() + "    当月剩余");
                    t_8.setText("次");
                } else if ("241".equals(bean.getOtype())) {
                    t_4.setText("开始时间:" + bean.getStart_ts() + "    当前剩余");
                    t_8.setText("循环");
                } else {
                    t_4.setText("开始时间:" + bean.getStart_ts() + "    当前剩余");
                    t_8.setText("次");
                }
                t_5.setText("注意：此卡可在所有商家换电");
                l_1.setVisibility(View.GONE);
                t_41.setVisibility(View.VISIBLE);
                t_41.setText("结束时间:" + bean.getEnd_ts());
            } else if ("30".equals(bean.getShow_type())) {
                //年卡
                t_3.setText("如需再次换电可使用换电币");
                t_4.setText("有效期:" + bean.getEnd_ts() + "    当前剩余");
                t_7.setText(bean.getDays() + "");
                t_8.setText("天");
                t_5.setText("注意：此卡仅限在本商家使用");
                l_1.setVisibility(View.VISIBLE);
                t_41.setVisibility(View.GONE);
                String expire = bean.getExpire();
                if ("0".equals(expire)) {// 0 未过期 1 已过期
                    String otype_str = bean.getOtype();
                    if ("230".equals(otype_str)) {
                        Glide.with(context).load(R.mipmap.rule1).into(imageview2);
                        Glide.with(context).load(R.mipmap.y299).into(imageview1);
                    } else if ("231".equals(otype_str)) {
                        Glide.with(context).load(R.mipmap.rule1).into(imageview2);
                        Glide.with(context).load(R.mipmap.y499).into(imageview1);
                    } else {
                        Glide.with(context).load(R.mipmap.rule2).into(imageview2);
                        Glide.with(context).load(R.mipmap.y699).into(imageview1);
                    }
                } else {
                    String otype_str = bean.getOtype();
                    if ("230".equals(otype_str)) {
                        Glide.with(context).load(R.mipmap.rule1).into(imageview2);
                    } else if ("231".equals(otype_str)) {
                        Glide.with(context).load(R.mipmap.rule1).into(imageview2);
                    } else {
                        Glide.with(context).load(R.mipmap.rule2).into(imageview2);
                    }
                    Glide.with(context).load(R.mipmap.expire_year).into(imageview1);
                }
            }

            i_2.setTag(bean);
            if (listener != null) {
                i_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onUcardSelectClick(v, (ExpenseBean.DataBean.UmonthBean.UcardBean) i_2.getTag());
                    }
                });
            }
        }
    }

    //umonth
    class TypeFouViewHolder extends TypeAbstractViewHolder {
        TextView t_1;//name
        TextView t_2;//name
        TextView t_4;
        TextView t_5;
        TextView t_6;
        TextView t_7;
        ImageView i_1;
        ImageView i_2;

        TypeFouViewHolder(View itemView) {
            super(itemView);
            t_1 = itemView.findViewById(R.id.t_1);
            t_2 = itemView.findViewById(R.id.t_2);
            t_4 = itemView.findViewById(R.id.t_4);
            t_5 = itemView.findViewById(R.id.t_5);
            t_6 = itemView.findViewById(R.id.t_6);
            t_7 = itemView.findViewById(R.id.t_7);
            i_1 = itemView.findViewById(R.id.i_1);
            i_2 = itemView.findViewById(R.id.i_2);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bindHolder(Object o) {
            ExpenseBean.DataBean.UmonthBean.PacketsBean bean = (ExpenseBean.DataBean.UmonthBean.PacketsBean) o;
            t_1.setText(bean.getName());
            t_2.setText(bean.getRprice() + "元");
            t_4.setText(bean.getTips());
            t_5.setText("有效期:" + bean.getExpire());
            t_6.setText("注意:此卡可以在所有电柜上换电");
            t_7.setText("剩余" + bean.getDays() + "天");

            if (bean.getChecked() == 0) {//0=不选
                bean.setIschecked(0);
                i_2.setImageResource(R.drawable.ic_disclaimer_agreement_uncheck);
            } else if (bean.getChecked() == 1) {//1=选中，可以点击
                bean.setIschecked(1);
                i_2.setImageResource(R.drawable.qianbao_select_2);
            } else {//2=选中，不可以点击
                bean.setIschecked(1);
                i_2.setImageResource(R.drawable.qianbao_select_2);
            }

            i_2.setTag(bean);
            if (listener != null) {
                i_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onUmonthSelectClick(v, (ExpenseBean.DataBean.UmonthBean.PacketsBean) i_2.getTag());
                    }
                });
            }
        }
    }
}