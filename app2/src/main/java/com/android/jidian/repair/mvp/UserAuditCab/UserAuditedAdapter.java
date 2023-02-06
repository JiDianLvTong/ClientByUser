package com.android.jidian.repair.mvp.UserAuditCab;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;

import com.android.jidian.repair.R;
import com.android.jidian.repair.mvp.main.PatrolFragment.PartolAdapter;
import com.android.jidian.repair.mvp.main.PatrolFragment.PatrolIndexBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author : PTT
 * date: 3/23/21 11:31 AM
 * company: 兴达智联
 * description:
 */
public class UserAuditedAdapter extends BaseQuickAdapter<AuditCabListBean.DataBean, BaseViewHolder> {

    public UserAuditedAdapter() {
        super(R.layout.item_user_audit);
    }

    @Override
    protected void convert(BaseViewHolder holder, AuditCabListBean.DataBean listsBean) {
        holder.setText(R.id.tv_audit_item_num, "编号:" + listsBean.getId())
                .setText(R.id.tv_audit_item_name, listsBean.getName())
                .setText(R.id.tv_audit_item_address, "地址:" + listsBean.getAddress() + "")
                .setText(R.id.tv_audit_item_distance, "距离:" + listsBean.getDistance() + "KM");
//        holder.setTextColor(R.id.tv_patrol_item_status, "在线".equals(listsBean.getOnline()) ? Color.parseColor("#D7A64A") : Color.parseColor("#cccccc"));
        Glide.with(mContext).load(listsBean.getImage()).into(((ImageView) holder.getView(R.id.iv_audit_item_img)));
    }
}