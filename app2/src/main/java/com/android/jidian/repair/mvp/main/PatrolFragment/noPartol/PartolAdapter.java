package com.android.jidian.repair.mvp.main.PatrolFragment.noPartol;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.android.jidian.repair.R;
import com.android.jidian.repair.mvp.main.PatrolFragment.hasPartol.PatrolMyListBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : xiaoming
 * date: 2023/2/1 11:51
 * description:
 */
public class PartolAdapter extends BaseQuickAdapter<PatrolIndexBean.DataBean.ListsBean, BaseViewHolder> {

    private OnItemViewClickListener mListener;

    public PartolAdapter() {
        super(R.layout.item_patrol_list);
    }

    public void setListener(OnItemViewClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, PatrolIndexBean.DataBean.ListsBean listsBean) {
        holder.setText(R.id.tv_patrol_item_num, "编号: " + listsBean.getId())
                .setText(R.id.tv_patrol_item_name,  listsBean.getName())
                .setText(R.id.tv_patrol_item_address, "地址: " + listsBean.getAddress() + "")
                .setText(R.id.tv_patrol_item_distance, "距离: " + listsBean.getDistance() + "KM")
                .setText(R.id.tv_patrol_item_status, listsBean.getOnline());
        holder.setTextColor(R.id.tv_patrol_item_status, "在线".equals(listsBean.getOnline()) ? Color.parseColor("#D7A64A") : Color.parseColor("#cccccc"));
        Glide.with(mContext).load(listsBean.getBgimg()).into(((ImageView) holder.getView(R.id.iv_patrol_item_img)));

        holder.getView(R.id.tv_patrol_item_guiide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnClickGuide(listsBean);
                }
            }
        });
        holder.getView(R.id.t_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnClickItemT1(listsBean);
                }
            }
        });
        holder.getView(R.id.t_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnClickItemT2(listsBean);
                }
            }
        });
    }

    public interface OnItemViewClickListener {
        void OnClickGuide(PatrolIndexBean.DataBean.ListsBean bean);
        void OnClickItemT1(PatrolIndexBean.DataBean.ListsBean bean);
        void OnClickItemT2(PatrolIndexBean.DataBean.ListsBean bean);
    }
}
