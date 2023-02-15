package com.android.jidian.repair.mvp.main.PatrolFragment.hasPartol;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.android.jidian.repair.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : xiaoming
 * date: 2023/1/12 14:02
 * description:
 */
public class UserMyPatrolAdapter extends BaseQuickAdapter<PatrolMyListBean.DataBean.ListsBean, BaseViewHolder> {

    private OnItemViewClickListener mListener;

    public UserMyPatrolAdapter() {
        super(R.layout.item_user_my_patrol);
    }

    public void setListener(OnItemViewClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, PatrolMyListBean.DataBean.ListsBean bean) {
        holder.setText(R.id.tv_my_patrol_item_num, "编号:" + bean.getCabid())
                .setText(R.id.tv_my_patrol_item_name, bean.getCabname())
                .setText(R.id.tv_my_patrol_item_address, "地址:" + bean.getAddress() + "")
                .setText(R.id.tv_my_patrol_item_distance, "距离:" + bean.getDistance() + "KM")
                .setText(R.id.tv_my_patrol_item_status, bean.getOnline());
        holder.setTextColor(R.id.tv_my_patrol_item_status, "在线".equals(bean.getOnline()) ? Color.parseColor("#D7A64A") : Color.parseColor("#cccccc"));
        Glide.with(mContext).load(bean.getBgimg()).into(((ImageView) holder.getView(R.id.iv_my_patrol_item_img)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.OnClickItem(bean);
                }
            }
        });
    }

    public interface OnItemViewClickListener {
        void OnClickItem(PatrolMyListBean.DataBean.ListsBean bean);
    }
}
